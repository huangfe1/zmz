package com.dreamer.service.pmall.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dreamer.domain.mall.goods.MallGoods;
import com.dreamer.domain.pmall.order.Order;
import com.dreamer.repository.mall.goods.MallGoodsDAO;
import com.dreamer.repository.pmall.order.OrderDAO;

@Service
public class OrderReturnHandler {

	@Transactional
	public void returns(Order order,String operator){
		order.returnGoods();
		increaseGoodsStock(order);
		orderDAO.merge(order);
	}
	
	@Transactional
	public void increaseGoodsStock(Order order){
		order.getItems().entrySet().stream().forEach(entry->{
			MallGoods goods=mallGoodsDAO.findById(entry.getKey());
			goods.increaseCurrentStock(entry.getValue().getQuantity());
		});
	}
	
	@Autowired
	private MallGoodsDAO mallGoodsDAO;
	@Autowired
	private OrderDAO orderDAO;
}
