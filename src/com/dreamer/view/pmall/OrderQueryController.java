package com.dreamer.view.pmall;

import com.dreamer.domain.pmall.order.Order;
import com.dreamer.domain.pmall.order.OrderItem;
import com.dreamer.domain.pmall.order.OrderStatus;
import com.dreamer.domain.pmall.order.PaymentStatus;
import com.dreamer.domain.user.Agent;
import com.dreamer.repository.pmall.order.OrderDAO;
import com.dreamer.service.pay.GetOpenIdHandler;
import com.dreamer.service.pay.PayConfig;
import com.dreamer.util.ExcelFile;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;
import ps.mx.otter.utils.SearchParameter;
import ps.mx.otter.utils.WebUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Controller
@RequestMapping("/pm/order")
public class OrderQueryController {

	@RequestMapping(value="/index.html",method=RequestMethod.GET)
	public String orderIndex(
			@ModelAttribute("parameter") SearchParameter<Order> param,
			HttpServletRequest request, HttpServletResponse response,
			Model model) {
		try{
			List<Order> orders=orderDAO.searchEntityByPage(param, null, (t)->true);
			WebUtil.turnPage(param, request);
			model.addAttribute("orders",orders);
			model.addAttribute("status",OrderStatus.values());
			model.addAttribute("paymentStatus",PaymentStatus.values());
		}catch(Exception exp){
			LOG.error("进入订单查询错误",exp);
			exp.printStackTrace();
		}
		return "pmall/order/order_index";
	}


	@RequestMapping(value="/download.html")
	public void download(
			@ModelAttribute("parameter") SearchParameter<Order> param,
			HttpServletRequest request, HttpServletResponse response,
			Model model) {
		try{
			List<Order> orders=orderDAO.findDownload(param);
			Map<String,Integer> sum=new HashedMap();
//			List<Object[]> results=orderDAO.getOrdersItemCount();
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
			headers.add("订单ID");
			headers.add("产品名字");
			headers.add("产品数量");
			List<Map> datas = new ArrayList<>();
			Map m = null;
			Order order=null;
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
				m.put(10,order.getConsignee());//收货人姓名
				m.put(11,"");
				m.put(12,order.getMobile());//收货人手机
				m.put(13,"");
				m.put(14,"");
				m.put(15,"");
				m.put(16,order.getShippingAddress());//收货人地址
				m.put(17,"");
				m.put(18,"");
				m.put(19,"");
				StringBuffer stringBuffer=new StringBuffer();
				for(OrderItem item:order.getItems().values()){//遍历所有的item
					String gn=item.getGoodsName();
					Integer gq=item.getQuantity();
					stringBuffer.append(gn);
					stringBuffer.append(gq);
					stringBuffer.append(item.getGoodsSpec()+"/");
					if(sum.containsKey(gn)){
						sum.put(gn,sum.get(gn)+gq);
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
				m.put(26,""+order.getId());//订单ID
//				if(i<results.size()){
//					m.put(27,""+results.get(i)[0]);//订单ID
//					m.put(28,""+results.get(i)[1]);//订单ID
//				}
				m.put(29,"");//订单ID
				datas.add(m);
			}

			List<String> sheaders=new ArrayList<>();
			sheaders.add("名字");
			sheaders.add("数量");
			List<Map> sdatas=new ArrayList<>();
			Map mm;
			for(String key:sum.keySet()){
			mm=new HashedMap();
				mm.put(0,key);
				mm.put(1,sum.get(key)+"");
			sdatas.add(mm);
			}

			List<List> sh=new ArrayList<>();
			sh.add(headers);
			sh.add(sheaders);
			List<List<Map>> ds=new ArrayList<>();
			ds.add(datas);
			ds.add(sdatas);

			List<String> ss=new ArrayList<>();
			ss.add("订单详情");
			ss.add("订单总数");
			ExcelFile.ExpExs("",ss,sh,ds,response);

		}catch(Exception exp){
			LOG.error("进入订单查询错误",exp);
			exp.printStackTrace();
		}
	}



	@RequestMapping(value="/pay.html",method=RequestMethod.GET)
	public String payIndex(@ModelAttribute("parameter") SearchParameter<Order> param,
			HttpServletRequest request, HttpServletResponse response,
			Model model){
		try{
			if(param.getEntity().isUnpaid()){
				model.addAttribute("paid", "1");
			}
		}catch(Exception exp){
			exp.printStackTrace();
			LOG.error("进入订单付款确认失败");
		}
		return "pmall/order/order_pay";
	}
	
	@RequestMapping(value="/onlinepay.html",method=RequestMethod.GET)
	public String onlinePayIndex(@ModelAttribute("parameter") SearchParameter<Order> param,
			HttpServletRequest request, HttpServletResponse response,
			Model model){
		try{
			Order order=param.getEntity();
			Agent agent=order.getUser();
			UriComponents backUrl=null;
			if(Objects.nonNull(agent.getWxOpenid()) && !agent.getWxOpenid().isEmpty()){
				LOG.debug("weixin openid already get,direct ro pay.html");
				backUrl = ServletUriComponentsBuilder
						.fromContextPath(request).path("/pay/pay.html").queryParam("order", order.getId()).build();
			}else{
				backUrl = ServletUriComponentsBuilder
						.fromContextPath(request).path("/pay/callback.html").queryParam("order", order.getId()).build();
				LOG.debug("Get OpenId CallBack URL:{}",backUrl.toUriString());
			}
			String uri=GetOpenIdHandler.createGetBaseOpenIdCallbackUrl(payConfig, backUrl.toUriString(), order.getId());
			LOG.debug("To Pay URL:{}",uri);
			response.setHeader("Location", uri);
			return "redirect:"+uri;
		}catch(Exception exp){
			exp.printStackTrace();
			LOG.error("进入订单在线付款失败");
		}
		return "pmall/index.html";
	}
	
	@RequestMapping(value="/revoke.html",method=RequestMethod.GET)
	public String revokeIndex(@ModelAttribute("parameter") SearchParameter<Order> param,
			HttpServletRequest request, HttpServletResponse response,
			Model model){
		try{
			if(param.getEntity().isUnpaid()){
				model.addAttribute("paid", "1");
			}
		}catch(Exception exp){
			exp.printStackTrace();
			LOG.error("进入订单撤销操作失败");
		}
		return "pmall/order/order_revoke";
	}
	
	@RequestMapping(value="/refund.html",method=RequestMethod.GET)
	public String refundIndex(@ModelAttribute("parameter") SearchParameter<Order> param,
			HttpServletRequest request, HttpServletResponse response,
			Model model){
		try{
			if(param.getEntity().isUnpaid()){
				model.addAttribute("paid", "1");
			}
		}catch(Exception exp){
			exp.printStackTrace();
			LOG.error("进入订单退款操作失败");
		}
		return "pmall/order/order_refund";
	}
	
	@RequestMapping(value="/return.html",method=RequestMethod.GET)
	public String returnIndex(@ModelAttribute("parameter") SearchParameter<Order> param,
			HttpServletRequest request, HttpServletResponse response,
			Model model){
		try{
			if(param.getEntity().isUnpaid()){
				model.addAttribute("paid", "1");
			}
		}catch(Exception exp){
			exp.printStackTrace();
			LOG.error("进入订单退货操作失败");
		}
		return "pmall/order/order_return";
	}
	@RequestMapping(value="/detail.html",method=RequestMethod.GET)
	public String detail(@ModelAttribute("parameter") SearchParameter<Order> param,
			HttpServletRequest request, HttpServletResponse response,
			Model model){
		return "pmall/order/order_detail";
	}
	
	@RequestMapping(value="/shipping.html",method=RequestMethod.GET)
	public String shippingIndex(@ModelAttribute("parameter") SearchParameter<Order> param,
			HttpServletRequest request, HttpServletResponse response,
			Model model){
		try{
			List<Order> orders=orderDAO.searchShippingEntityByPage(param, null, (t)->true);
			WebUtil.turnPage(param, request);
			model.addAttribute("orders",orders);
			model.addAttribute("status",OrderStatus.values());
			model.addAttribute("paymentStatus",PaymentStatus.values());
		}catch(Exception exp){
			LOG.error("进入订单发货管理错误",exp);
			exp.printStackTrace();
		}
		return "pmall/order/order_shipping";
	}
	
	@RequestMapping(value="/shipping/confirm.html",method=RequestMethod.GET)
	public String shippingConfirm(@ModelAttribute("parameter") SearchParameter<Order> param,
			HttpServletRequest request, HttpServletResponse response,
			Model model){
		try{
		}catch(Exception exp){
			LOG.error("进入订单发货确认错误",exp);
			exp.printStackTrace();
		}
		return "pmall/order/shipping_confirm";
	}
	
	@RequestMapping(value="/shipping/print.html",method=RequestMethod.GET)
	public String printShipping(@ModelAttribute("parameter") SearchParameter<Order> param,
			HttpServletRequest request, HttpServletResponse response,
			Model model){
		return "pmall/order/shipping_print";
	}

    @RequestMapping(value="/shipping/upload.html",method=RequestMethod.GET)
    public String uploadShipping(){
        return "pmall/order/order_upload";
    }

	/**
	 * 获取所有需要发货的订单
	 */
	@RequestMapping(value = "/getOrders.html",method = RequestMethod.GET)
	public void getOrders(HttpServletResponse response, Model model){
        List<Order> orders=orderDAO.getOrdersNews();//获取没有发货的订单
        List<Object[]> results=orderDAO.getOrdersItemCount();
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
        headers.add("订单ID");
        headers.add("产品名字");
        headers.add("产品数量");
        List<Map> datas = new ArrayList<>();
        Map m = null;
        Order order=null;
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
            m.put(10,order.getConsignee());//收货人姓名
            m.put(11,"");
            m.put(12,order.getMobile());//收货人手机
            m.put(13,"");
            m.put(14,"");
            m.put(15,"");
            m.put(16,order.getShippingAddress());//收货人地址
            m.put(17,"");
            m.put(18,"");
            m.put(19,"");
            StringBuffer stringBuffer=new StringBuffer();
            for(OrderItem item:order.getItems().values()){//遍历所有的item
                stringBuffer.append(item.getGoodsName());
                stringBuffer.append(item.getQuantity());
                stringBuffer.append(item.getGoodsSpec()+"/");
            }
            m.put(20,stringBuffer.toString());
            m.put(21,"");
            m.put(22,"");
            m.put(23,"");
            m.put(24,"");
            m.put(25,order.getRemark());
            m.put(26,""+order.getId());//订单ID
            if(i<results.size()){
                m.put(27,""+results.get(i)[0]);//订单ID
                m.put(28,""+results.get(i)[1]);//订单ID
            }
            m.put(29,"");//订单ID
            datas.add(m);
        }
        ExcelFile.ExpExs("","积分商城订单",headers,datas,response);//创建表格并写入
//        model.addAttribute("order_size",orders.size());
//        return "/pmall/order/order_print";
	}


    public static void main(String[] args) {
        ExcelFile excelFile = new ExcelFile();
        String path="/Users/huangfei/Desktop/datas.xlsx";
        String[] columns=new String[]{"业务单号","订单ID"};
        List<Map<String,Object>> lists=excelFile.read(path,0,1,columns);
        for(Map<String,Object> map:lists){
            System.out.println(map.get("业务单号"));
            System.out.println(map.get("订单ID"));
        }
    }


    @ModelAttribute("parameter")
	public SearchParameter<Order> orderPreprocess(
			@RequestParam(required = false) Optional<Integer> id) {
		SearchParameter<Order> parameter = new SearchParameter<Order>();
		if (id.isPresent()) {
			parameter.setEntity(orderDAO.findById(id.get()));
		} else {
			parameter.setEntity(new Order());
		}
		return parameter;
	}

	@Autowired
	private OrderDAO orderDAO;
	@Autowired
	private PayConfig payConfig;
	@Autowired
	private GetOpenIdHandler getOpenIdHandler;
	
	private final Logger LOG=LoggerFactory.getLogger(getClass());
}
