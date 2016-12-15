package com.dreamer.view.mall.shopcart;

import com.dreamer.domain.goods.DeliveryNote;
import com.dreamer.domain.goods.Goods;
import com.dreamer.domain.mall.shopcart.ShopCart;
import com.dreamer.domain.user.Agent;
import com.dreamer.repository.goods.GoodsDAO;
import com.dreamer.repository.user.AgentDAO;
import com.dreamer.service.delivery.DeliveryNoteHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;
import ps.mx.otter.exception.DataNotFoundException;
import ps.mx.otter.utils.WebUtil;
import ps.mx.otter.utils.message.Message;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@RestController
@RequestMapping("/tmall/shopcart")
@SessionAttributes({ "indexUrl", "ref" })
public class TmallShopCartController {

	private static final String CART = "tshopcart";

	@RequestMapping(value = "/add.json", method = RequestMethod.POST)
	public Message addGoodsToShopcart(
			@RequestParam("goodsId") Integer goodsId,
			@RequestParam(value = "quantity", required = false) Integer quantity,
			HttpServletRequest request) {
		try {
			Agent agentUser = (Agent) WebUtil.getCurrentUser(request);
			Agent agent = agentDAO.findById(agentUser.getId());
			Goods goods = goodsDAO.findById(goodsId);
			if (Objects.isNull(goods)) {
				throw new DataNotFoundException("商品不存在");
			}
			Object ob = WebUtil.getSessionAttribute(request, CART);
			Integer addQuantity = Objects.nonNull(quantity)?quantity:1,result=0;
			if (Objects.nonNull(ob)) {
				ShopCart cart = (ShopCart) ob;
				cart.addGoods(goods, addQuantity, agent.caculatePrice(goods).getPrice());
				result=cart.getQuantity();
			} else {
				ShopCart cart = new ShopCart();
				cart.addGoods(goods, addQuantity, agent.caculatePrice(goods).getPrice());
				WebUtil.addSessionAttribute(request, CART, cart);
				result=cart.getQuantity();
			}
			return Message.createSuccessMessage("OK", String.valueOf(result));
		} catch (Exception exp) {
			exp.printStackTrace();
			return Message.createFailedMessage(exp.getMessage());
		}
	}
	
	@RequestMapping(value = "/put.json", method = {RequestMethod.POST,RequestMethod.PUT})
	public Message putGoodsToShopcart(
			@RequestParam("goodsId") Integer goodsId,
			@RequestParam(value = "quantity", required = false) Integer quantity,
			HttpServletRequest request) {
		try {
			Agent agentUser = (Agent) WebUtil.getCurrentUser(request);
			Agent agent = agentDAO.findById(agentUser.getId());
			Goods goods = goodsDAO.findById(goodsId);
			if (Objects.isNull(goods)) {
				throw new DataNotFoundException("商品不存在");
			}
			Object ob = WebUtil.getSessionAttribute(request, CART);
			Integer addQuantity = Objects.nonNull(quantity)?quantity:1,result=0;
			if (Objects.nonNull(ob)) {
				ShopCart cart = (ShopCart) ob;
				cart.removeItems(goods);
				cart.addGoods(goods, addQuantity, agent.caculatePrice(goods).getPrice());
				result=cart.getQuantity();
			} else {
				ShopCart cart = new ShopCart();
				cart.addGoods(goods, addQuantity, agent.caculatePrice(goods).getPrice());
				WebUtil.addSessionAttribute(request, CART, cart);
				result=cart.getQuantity();
			}
			return Message.createSuccessMessage("OK", String.valueOf(result));
		} catch (Exception exp) {
			exp.printStackTrace();
			return Message.createFailedMessage(exp.getMessage());
		}
	}

	@RequestMapping(value = "/mins.json", method = RequestMethod.POST)
	public Message minsGoodsToShopcart(
			@RequestParam("goodsId") Integer goodsId, HttpServletRequest request) {
		try {
			Goods goods = goodsDAO.findById(goodsId);
			if (Objects.isNull(goods)) {
				throw new DataNotFoundException("商品不存在");
			}
			Object ob = WebUtil.getSessionAttribute(request, CART);
			if (Objects.nonNull(ob)) {
				ShopCart cart = (ShopCart) ob;
				cart.removeGoods(goods, 1);
			}
			return Message.createSuccessMessage();
		} catch (Exception exp) {
			exp.printStackTrace();
			return Message.createFailedMessage(exp.getMessage());
		}
	}

	@RequestMapping(value = "/remove.json", method = RequestMethod.POST)
	public Message removeGoodsToShopcart(
			@RequestParam("goodsId") Integer goodsId, HttpServletRequest request) {
		try {
			Goods goods = goodsDAO.findById(goodsId);
			if (Objects.isNull(goods)) {
				throw new DataNotFoundException("商品不存在");
			}
			Object ob = WebUtil.getSessionAttribute(request, CART);
			if (Objects.nonNull(ob)) {
				ShopCart cart = (ShopCart) ob;
				cart.removeItems(goods);
			}
			return Message.createSuccessMessage();
		} catch (Exception exp) {
			exp.printStackTrace();
			return Message.createFailedMessage(exp.getMessage());
		}
	}
/**
	 * add by hf 特权商城直接发货
	 * @param parameter
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/pay.json", method = RequestMethod.POST)
	public Message pay(@ModelAttribute("parameter") DeliveryNote parameter,
			/*@ModelAttribute("indexUrl") String indexUrl,*/
			HttpServletRequest request, HttpServletResponse response,Integer isUseAdvance,Integer isUseVoucher) {
		try {
			Agent agentUser = (Agent) WebUtil.getCurrentUser(request);
			Agent agent = agentDAO.findById(agentUser.getId());
			ShopCart cart = (ShopCart) WebUtil.getSessionAttribute(request,
					CART);
			parameter.setUserByApplyAgent(agent);
			parameter.setUserByOperator(agent);
			if(agent.isTeqVip()){//如果是特权vip
				parameter.setParentByOperator(agentDAO.findById(3));//处理人公司
			}else {
				parameter.setParentByOperator(agent.getTeqparent());//处理人为他的特权上家
			}
			if(!Objects.isNull(isUseAdvance)&&isUseAdvance!=0){//使用预存款
                Boolean b=true;
                if(!Objects.isNull(isUseVoucher)&&isUseVoucher==0){
                    b=false;
                }
 				//建立特权商城发货订单
				deliveryNoteHandler.buildDeliveryNoteByTmallAndConfirm(parameter,
						agentUser.getAgentCode(), cart,b);

			}else {//不使用预存款,不是大区
                //建立特权商城发货订单
                deliveryNoteHandler.buildDeliveryNoteByTmall(parameter,
                        agentUser.getAgentCode(), cart);
            }
			UriComponents ucb = ServletUriComponentsBuilder
					.fromContextPath(request).path("/dmz/tmall/index.html")
					.build();
			response.setHeader("Location", ucb.toUriString());
			WebUtil.removeSessionAttribute(request, CART);
			return Message.createSuccessMessage("ok",String.valueOf(parameter.getId()));
		} catch (Exception exp) {
			exp.printStackTrace();
			LOG.error("提交失败",exp);
			return Message.createFailedMessage(exp.getMessage());
		}
	}
	
	@Autowired
	private GoodsDAO goodsDAO;

	@Autowired
	private AgentDAO agentDAO;

	@Autowired
	private DeliveryNoteHandler deliveryNoteHandler;
	
	private Logger LOG=LoggerFactory.getLogger(getClass());
}
