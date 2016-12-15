package com.dreamer.view.delivery;

import com.dreamer.domain.goods.DeliveryNote;
import com.dreamer.domain.pmall.order.Order;
import com.dreamer.domain.user.Agent;
import com.dreamer.domain.user.User;
import com.dreamer.repository.goods.DeliveryNoteDAO;
import com.dreamer.repository.user.AgentDAO;
import com.dreamer.service.delivery.DeliveryNoteHandler;
import com.dreamer.util.ExcelFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ps.mx.otter.exception.DataAccessException;
import ps.mx.otter.exception.ParameterMissingException;
import ps.mx.otter.utils.WebUtil;
import ps.mx.otter.utils.message.Message;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/delivery")
public class DeliveryNoteController {


	@RequestMapping(value = "/edit.json", method = RequestMethod.POST)
	public Message edit(
			@ModelAttribute("parameter") DeliveryNote parameter,
			Integer[] goodsIds,Integer[] quantitys,String agentCode,
			HttpServletRequest request, Model model) {
		try {
			if((Objects.isNull(agentCode)||agentCode.isEmpty()) && (Objects.isNull(parameter.getConsigneeName())||parameter.getConsigneeName().isEmpty())){
				throw new ParameterMissingException("收货代理编号或收货人姓名必须填写一项");
			}
			User user=(User)WebUtil.getCurrentUser(request);
			parameter.setUserByOperator(user);
			if(parameter.getVersion()==null){
				Agent agent=agentDAO.findById(user.getId());
				if(Objects.isNull(agent)){
					throw new DataAccessException("当前发货申请代理不存在");
				}
				parameter.setUserByApplyAgent(agent);
				deliveryNoteHandler.addDeliveryNote(parameter, agentCode, quantitys, goodsIds);
			}else{
				if(user.isAgent()){
					if(!Objects.equals(user.getId(), parameter.getUserByApplyAgent().getId())){
						throw new DataAccessException("非管理员和申请人无修改权限");
					}
				}
				deliveryNoteHandler.modifyDeliveryNote(parameter,agentCode, quantitys, goodsIds);
			}
			return Message.createSuccessMessage();
		} catch (Exception exp) {
			LOG.error("发货申请失败,",exp);
			exp.printStackTrace();
			return Message.createFailedMessage(exp.getMessage());
		}
	}
	
	@RequestMapping(value = "/remove.json", method = RequestMethod.DELETE)
	public Message remove(
			@ModelAttribute("parameter") DeliveryNote parameter,
			HttpServletRequest request, Model model) {
		try {
			User user=(User)WebUtil.getCurrentUser(request);
			if(user.isAgent()){
				if(!Objects.equals(user.getId(), parameter.getUserByApplyAgent().getId())){
					throw new DataAccessException("非管理员和申请人无删除权限");
				}
			}
			deliveryNoteHandler.removeDeliveryNote(parameter);
			return Message.createSuccessMessage();
		} catch (Exception exp) {
			LOG.error("发货申请失败,",exp);
			exp.printStackTrace();
			return Message.createFailedMessage(exp.getMessage());
		}
	}
	
	@RequestMapping(value = "/confirm.json", method = RequestMethod.POST)
	public Message confirm(
			@ModelAttribute("parameter") DeliveryNote parameter,
			HttpServletRequest request, Model model) {
		try {
			User user=(User)WebUtil.getCurrentUser(request);
			if(!user.isAdmin()){
				if(!Objects.equals(user.getId(), parameter.getParentByOperator().getId())){
					throw new DataAccessException("非管理员无发货确认权限");
				}
			}
			deliveryNoteHandler.confirm(parameter);
			return Message.createSuccessMessage();
		} catch (Exception exp) {
			LOG.error("发货确认失败,",exp);
			exp.printStackTrace();
			return Message.createFailedMessage(exp.getMessage());
		}
	}

	/**
	 * 上传订单编号与ID
	 * @param imageFile
	 */
	@RequestMapping(value = "/uploadOrdersNumber.json",method = RequestMethod.POST)
	public Message uploadOrdersNumber(MultipartFile file, HttpServletRequest request){
		int i=0;
		ExcelFile excelFile = new ExcelFile();
		String[] columns=new String[]{"快递公司","业务单号","物流费","订单ID"};
		try {
			List<Map<String,Object>> lists=excelFile.read(file.getInputStream(),0,1,columns);
			for(Map<String,Object> map:lists){
				String orderNo=(String) map.get("业务单号");
                Integer orderId=Integer.parseInt(map.get("订单ID")+"");
                Double logcinsee=Double.parseDouble(map.get("物流费")+"");
//                String company="百事快递";
				String company=(String) map.get("快递公司");
				if(orderNo!=null&&!orderNo.equals("")&&orderId!=null){
					DeliveryNote order=deliveryNoteDAO.findById(orderId);//找出id
					order.setLogisticsCode(orderNo);//设置订单编号
					order.setLogistics(company);
                    deliveryNoteHandler.delivery(order,logcinsee);
					i++;
				}
			}
			return Message.createSuccessMessage();
		}catch (Exception exp){
            exp.printStackTrace();
			return Message.createFailedMessage(exp.getMessage()+"本次共处理了"+i);
		}
	}



	@RequestMapping(value = "/delivery.json", method = RequestMethod.POST)
	public Message delivery(
			@ModelAttribute("parameter") DeliveryNote parameter,
			HttpServletRequest request, Model model,Double actual_logisticsFee) {
		try {
			User user=(User)WebUtil.getCurrentUser(request);
			if(!user.isAdmin()){
					throw new DataAccessException("非管理员无发货权限");
			}
			deliveryNoteHandler.delivery(parameter,actual_logisticsFee);
			return Message.createSuccessMessage();
		} catch (Exception exp) {
			LOG.error("发货确认失败,",exp);
			exp.printStackTrace();
			return Message.createFailedMessage(exp.getMessage());
		}
	}

	@ModelAttribute("parameter")
	public DeliveryNote preprocess(@RequestParam("id")Optional<Integer> id) {
		if (id.isPresent()) {
			return deliveryNoteDAO.findById(id.get());
		} else {
			return new DeliveryNote();
		}
	}

	@Autowired
	private DeliveryNoteDAO deliveryNoteDAO;
	@Autowired
	private AgentDAO agentDAO;
	
	@Autowired
	private DeliveryNoteHandler deliveryNoteHandler;
	
	private final Logger LOG=LoggerFactory.getLogger(getClass());

}
