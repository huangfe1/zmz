package com.dreamer.view.delivery;

import com.dreamer.domain.goods.*;
import com.dreamer.domain.user.Agent;
import com.dreamer.domain.user.User;
import com.dreamer.repository.goods.DeliveryNoteDAO;
import com.dreamer.repository.goods.GoodsDAO;
import com.dreamer.repository.user.AgentDAO;
import com.dreamer.repository.user.MutedUserDAO;
import com.dreamer.util.ExcelFile;
import com.dreamer.view.goods.TransferApplySuccessDTO;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ps.mx.otter.utils.Constant;
import ps.mx.otter.utils.DatatableDTO;
import ps.mx.otter.utils.SearchParameter;
import ps.mx.otter.utils.WebUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/delivery")
public class DeliveryNoteQueryController {

	@RequestMapping(value = "/index.html", method = RequestMethod.GET)
	public String index(
			@ModelAttribute("parameter") SearchParameter<DeliveryNote> parameter,
			HttpServletRequest request, Model model) {
		try {
			List<DeliveryNote> dls=null;
			User user=(User)WebUtil.getCurrentUser(request);
			if(user.isAgent()){
				Agent agent=agentDAO.findById(user.getId());
				parameter.getEntity().setUserByApplyAgent(agent);
				dls= deliveryNoteDAO.searchEntityByPage(
						parameter, null, null);
			}else{
				dls= deliveryNoteDAO.searchEntityByPage(
						parameter, null, null);
			}
			 
			WebUtil.turnPage(parameter, request);
			model.addAttribute("dls", dls);
			model.addAttribute("status",DeliveryStatus.values());
		} catch (Exception exp) {
			exp.printStackTrace();
			LOG.error("发货申请查询失败,",exp);
		}
		return "delivery/delivery_index";
	}


	@RequestMapping(value = "/download.html")
	public void download(
			@ModelAttribute("parameter") SearchParameter<DeliveryNote> parameter,
			HttpServletResponse response) {
		List<DeliveryNote> orders=deliveryNoteDAO.findDownload(parameter);
        Map<String,Integer> sum =new HashMap<>();
		List<String> headers = new ArrayList<>();
		headers.add("业务单号");
		headers.add("寄件单位");
		headers.add("寄件人姓名");
		headers.add("寄件人电话");
		headers.add("寄件人手机");
		headers.add("寄件人省");
		headers.add("寄件人市");
		headers.add("寄件区/县");
		headers.add("寄件人地址");
		headers.add("寄件人邮编");
		headers.add("收件人姓名");
		headers.add("收件人电话");
		headers.add("收件人手机");
		headers.add("收件省");
		headers.add("收件市");
		headers.add("收件区/县");
		headers.add("收件人地址");
		headers.add("收件邮政编码");
		headers.add("运费");
		headers.add("订单金额");
		headers.add("商品名称");
		headers.add("商品编码");
		headers.add("销售属性");
		headers.add("商品金额");
		headers.add("数量");
		headers.add("备注");
		headers.add("物流费");
		headers.add("订单ID");
//        headers.add("产品名字");
//        headers.add("产品数量");
		List<Map>   datas = new ArrayList<>();
		Map m = null;
		DeliveryNote order=null;
		for(int i=0;i<orders.size();i++){
			order=orders.get(i);
			//如果下单数量超过一位  就分几次单
			m=new HashedMap();
			m.put(0,"");
			m.put(1,"");
			m.put(2,"筑美");
			m.put(3,"");
			m.put(4,"17775862960");
			m.put(5,"");
			m.put(6,"");
			m.put(8,"湖南长沙");
			m.put(9,"");
			m.put(10,order.getConsigneeName()+((Agent)order.getUserByConsignee()).getAgentCode());//收货人姓名
			m.put(11,"");
			m.put(12,order.getMobile());//收货人手机
			m.put(13,"");
			m.put(14,"");
			m.put(15,"");
			m.put(16,order.getProvince()+order.getCity()+order.getCounty()+order.getAddress());//收货人地址
			m.put(17,"");
			m.put(18,"");
			m.put(19,"");
			StringBuffer stringBuffer=new StringBuffer();
			for(DeliveryItem item:order.getDeliveryItems()){//遍历所有的item
                String gn=item.getGoods().getName();
                Integer gq=item.getQuantity();
				stringBuffer.append(gn);
				stringBuffer.append(gq);
				stringBuffer.append(item.getGoods().getSpec()+"/");
                if(sum.containsKey(gn)){
                    Integer tem=sum.get(gn);
                    sum.put(gn,tem+gq);
                }else {
                    sum.put(gn,gq);
                }
			}
			m.put(20,stringBuffer.toString());
			m.put(21,"");
			m.put(22,"");
			m.put(23,"");
			m.put(24,"");
			m.put(25,order.getRemark());
			m.put(26,order.getLogisticsFee());//物流费
			m.put(27,""+order.getId());//订单ID
//            if(i<results.size()){
//                m.put(28,""+results.get(i)[0]);//订单ID
//                m.put(29,""+results.get(i)[1]);//订单ID
//            }
			m.put(28,"");//订单ID
//            //有多少就增加多少
//            for (int id=0;id<order.getQuantity();id++){
			datas.add(m);
//            }
		}
        //总数表格
        List<String> sheaders=new ArrayList<>();
        sheaders.add("产品名字");
        sheaders.add("产品总数");
        Map ms=null;
        List<Map> sdatas=new ArrayList<>();
        for(String key:sum.keySet()){
            ms =new HashedMap();
            ms.put(0,key);
            ms.put(1,""+sum.get(key));
            sdatas.add(ms);
        }
        List<String> ss=new ArrayList<>();
        ss.add("订单详情");
        ss.add("订单总数");

        List<List> hs=new ArrayList<>();
        hs.add(headers);
        hs.add(sheaders);

        List<List<Map>> ds=new ArrayList<>();
        ds.add(datas);
        ds.add(sdatas);
//		ExcelFile.ExpExs("","特权代理商城订单",headers,datas,response);//创建表格并写入
		ExcelFile.ExpExs("",ss,hs,ds,response);//创建表格并写入
	}
	
	
	/**
	 * 确认自己下级从商城发起的发货请求
	 * @param parameter
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/agent/confirm.html", method = RequestMethod.GET)
	public String agentConfirm(
			@ModelAttribute("parameter") SearchParameter<DeliveryNote> parameter,
			HttpServletRequest request, Model model) {
		try {
			User user=(User)WebUtil.getCurrentUser(request);
			Agent agent=null;
			if(user.isAdmin()){
				agent=mutedUserDAO.findById(((User)WebUtil.getSessionAttribute(request, Constant.MUTED_USER_KEY)).getId());
			}else{
				agent=agentDAO.findById(user.getId());
			}
			parameter.getEntity().setParentByOperator(agent);
			List<DeliveryNote> dls = deliveryNoteDAO.searchAgentConfirmDeliveryNoteByPage(
					parameter, null, null);
			WebUtil.turnPage(parameter, request);
			model.addAttribute("dls", dls);
			model.addAttribute("status",DeliveryStatus.values());
		} catch (Exception exp) {
			exp.printStackTrace();
			LOG.error("发货申请查询失败,",exp);
		}
		return "delivery/agent_confirm_index";
	}

	@RequestMapping(value = "/edit.html", method = RequestMethod.GET)
	public String edit(
			@ModelAttribute("parameter") SearchParameter<DeliveryNote> parameter,
			HttpServletRequest request, Model model) {
		try {
		} catch (Exception exp) {
			LOG.error("进入发货申请失败,",exp);
			exp.printStackTrace();
		}
		return "delivery/delivery_edit";
	}
	
	@RequestMapping(value = "/mobile.html", method = RequestMethod.GET)
	public String mobile(
			@ModelAttribute("parameter") SearchParameter<DeliveryNote> parameter,
			HttpServletRequest request, Model model) {
		try {
			User user=(User)WebUtil.getCurrentUser(request);
			if(!user.isAgent()){
				return "common/403";
			}
		} catch (Exception exp) {
			LOG.error("进入发货管理失败,",exp);
			exp.printStackTrace();
		}
		return "delivery/delivery_mobile_index";
	}
	
	@RequestMapping(value = "/success.html", method = RequestMethod.GET)
	public String success(
			@ModelAttribute("parameter") SearchParameter<DeliveryNote> parameter,
			HttpServletRequest request, Model model) {
		try {
			User user=(User)WebUtil.getCurrentUser(request);
			Agent agent=agentDAO.findById(user.getId());
			List<TransferApplySuccessDTO> dtos=parameter.getEntity().getDeliveryItems().stream().map(item->{
				TransferApplySuccessDTO dto=new TransferApplySuccessDTO();
				dto.setGoodsName(item.getGoods().getName());
				Price price=agent.caculatePrice(item.getGoods(), item.getQuantity());
				dto.setPrice(price.getPrice());
				dto.setAmount(item.getQuantity()*price.getPrice());
				dto.setQuantity(item.getQuantity());
				dto.setLevelName(price.getAgentLevel().getName());
				return dto;
			}).collect(Collectors.toList());
			Double amount=dtos.stream().mapToDouble(i->i.getAmount()).sum();
			model.addAttribute("items", dtos);
			model.addAttribute("amount", amount);
            if(parameter.getEntity().getLogisticsFee()!=null)
			model.addAttribute("logistfee", parameter.getEntity().getLogisticsFee());
		} catch (Exception exp) {
			LOG.error("进入发货申请成功清单失败,",exp);
			exp.printStackTrace();
		}
		return "delivery/delivery_success";
	}
	
	@RequestMapping(value = "/print.html", method = RequestMethod.GET)
	public String print(
			@ModelAttribute("parameter") SearchParameter<DeliveryNote> parameter,
			HttpServletRequest request, Model model) {
		try {
		} catch (Exception exp) {
			LOG.error("进入发货申请失败,",exp);
			exp.printStackTrace();
		}
		return "delivery/delivery_print";
	}
	
	@RequestMapping(value = "/confirm.html", method = RequestMethod.GET)
	public String confirm(
			@ModelAttribute("parameter") SearchParameter<DeliveryNote> parameter,
			HttpServletRequest request, Model model) {
		try {
			User user=(User)WebUtil.getCurrentUser(request);
			if(!user.isAdmin() && !Objects.equals(user.getId(), 
					parameter.getEntity().getParentByOperator().getId())){
				LOG.error("非管理员身份,无发货确认权限");
				return "common/403";
			}
			if(parameter.getEntity().isMallApply()){
				return "delivery/delivery_mall_confirm";
			}
		} catch (Exception exp) {
			LOG.error("进入发货确认失败,",exp);
			exp.printStackTrace();
		}
		return "delivery/delivery_confirm";
	}
	
	@RequestMapping(value = "/delivery.html", method = RequestMethod.GET)
	public String delivery(
			@ModelAttribute("parameter") SearchParameter<DeliveryNote> parameter,
			HttpServletRequest request, Model model) {
		try {
			User user=(User)WebUtil.getCurrentUser(request);
			if(!user.isAdmin()){
				LOG.error("非管理员身份,无发货权限");
				return "common/403";
			}
		} catch (Exception exp) {
			LOG.error("进入发货确认失败,",exp);
			exp.printStackTrace();
		}
		return "delivery/delivery";
	}

	@RequestMapping(value = "/authGoods.html", method = RequestMethod.GET)
	public String selectGoodsEnter(
			@ModelAttribute("parameter") SearchParameter<DeliveryNote> parameter,
			HttpServletRequest request, Model model,int toback) {
		try {
		} catch (Exception exp) {
			LOG.error("进入发货申请货物选择界面失败,",exp);
			exp.printStackTrace();
		}
		model.addAttribute("toback",toback);
		return "delivery/delivery_authGoods";
	}
	
	@RequestMapping(value = "/authGoods.json", method = RequestMethod.GET)
	@ResponseBody
	public DatatableDTO<Goods> queryGoods(
			@ModelAttribute("parameter") SearchParameter<DeliveryNote> parameter,
			HttpServletRequest request, Model model) {
		DatatableDTO<Goods> dto=new DatatableDTO<Goods>();
		try {
			User user=(User)WebUtil.getCurrentUser(request);
			Agent agent=agentDAO.findById(user.getId());
			List<Goods> goods=agent.loadActivedAuthorizedGoodses();
			
			List<Goods> page=goods.stream().skip(parameter.getStart()).limit(parameter.getLength()).collect(Collectors.toList());
			dto.setData(page);
			dto.setRecordsFiltered(goods.size());
			dto.setRecordsTotal(goods.size());
		} catch (Exception exp) {
			LOG.error("进入发货申请货物选择界面失败,",exp);
			exp.printStackTrace();
		}
		return dto;
	}


    @RequestMapping(value="/upload.html",method=RequestMethod.GET)
    public String uploadShipping(){
        return "delivery/order_upload";
    }

	/**
	 * 获取所有需要发货的订单
	 */
	@RequestMapping(value = "/getOrders.html",method = RequestMethod.GET)
	public void getOrders(HttpServletResponse response, Model model){
		List<DeliveryNote> orders=deliveryNoteDAO.getDeliveryNoteNews();//获取没有发货的订单
		List<Object[]> results=deliveryNoteDAO.getOrdersItemCount();
		List<String> headers = new ArrayList<>();
		headers.add("业务单号");
		headers.add("寄件单位");
		headers.add("寄件人姓名");
		headers.add("寄件人电话");
		headers.add("寄件人手机");
		headers.add("寄件人省");
		headers.add("寄件人市");
		headers.add("寄件区/县");
		headers.add("寄件人地址");
		headers.add("寄件人邮编");
		headers.add("收件人姓名");
		headers.add("收件人电话");
		headers.add("收件人手机");
		headers.add("收件省");
		headers.add("收件市");
		headers.add("收件区/县");
		headers.add("收件人地址");
		headers.add("收件邮政编码");
		headers.add("运费");
		headers.add("订单金额");
		headers.add("商品名称");
		headers.add("商品编码");
		headers.add("销售属性");
		headers.add("商品金额");
		headers.add("数量");
		headers.add("备注");
        headers.add("物流费");
		headers.add("订单ID");
		headers.add("产品名字");
		headers.add("产品数量");
		List<Map> datas = new ArrayList<>();
		Map m = null;
		DeliveryNote order=null;
		for(int i=0;i<orders.size();i++){
			order=orders.get(i);
			m=new HashedMap();
			m.put(0,"");
			m.put(1,"");
			m.put(2,"筑美");
			m.put(3,"");
			m.put(4,"17775862960");
			m.put(5,"");
			m.put(6,"");
			m.put(8,"湖南长沙");
			m.put(9,"");
			m.put(10,order.getConsigneeName());//收货人姓名
			m.put(11,"");
			m.put(12,order.getMobile()+order.getUserByApplyAgent().getAgentCode());//收货人手机
			m.put(13,"");
			m.put(14,"");
			m.put(15,"");
			m.put(16,order.getProvince()+order.getCity()+order.getCounty()+order.getAddress());//收货人地址
			m.put(17,"");
			m.put(18,"");
			m.put(19,"");
			StringBuffer stringBuffer=new StringBuffer();
			for(DeliveryItem item:order.getDeliveryItems()){//遍历所有的item
				stringBuffer.append(item.getGoods().getName());
				stringBuffer.append(item.getQuantity());
				stringBuffer.append(item.getGoods().getSpec()+"/");
			}
			m.put(20,stringBuffer.toString());
			m.put(21,"");
			m.put(22,"");
			m.put(23,"");
			m.put(24,"");
			m.put(25,order.getRemark());
            m.put(26,order.getLogisticsFee());//物流费
			m.put(27,""+order.getId());//订单ID
			if(i<results.size()){
				m.put(28,""+results.get(i)[0]);//订单ID
				m.put(29,""+results.get(i)[1]);//订单ID
			}
			m.put(30,"");//订单ID
			datas.add(m);
		}
		ExcelFile.ExpExs("","特权代理商城订单",headers,datas,response);//创建表格并写入
//        model.addAttribute("order_size",orders.size());
//        return "/pmall/order/order_print";
	}



	@ModelAttribute("parameter")
	public SearchParameter<DeliveryNote> preprocess(@RequestParam("id")Optional<Integer> id) {
		SearchParameter<DeliveryNote> parameter = new SearchParameter<DeliveryNote>();
		if (id.isPresent()) {
			parameter.setEntity(deliveryNoteDAO.findById(id.get()));
		} else {
			parameter.setEntity(new DeliveryNote());
		}
		return parameter;
	}

	@Autowired
	private DeliveryNoteDAO deliveryNoteDAO;
	@Autowired
	private AgentDAO agentDAO;
	@Autowired
	private MutedUserDAO mutedUserDAO;
	@Autowired
	private GoodsDAO goodsDAO;
	
	private final Logger LOG=LoggerFactory.getLogger(getClass());

}
