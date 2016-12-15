package com.dreamer.service.pmall.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dreamer.domain.pmall.order.Order;
import com.dreamer.repository.pmall.order.OrderDAO;

@Service
public class OrderRefundHandler {

	@Transactional
	public void refund(Order order,String operator){
		order.setRefundOperator(operator);
		order.refund();
		orderDAO.merge(order);
	}
	
	@Autowired
	private OrderDAO orderDAO;
}
