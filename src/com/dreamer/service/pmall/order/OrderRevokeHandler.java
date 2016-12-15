package com.dreamer.service.pmall.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dreamer.domain.pmall.order.Order;
import com.dreamer.repository.pmall.order.OrderDAO;

@Service
public class OrderRevokeHandler {

	@Transactional
	public void revoke(Order order){
		order.revoke();
		orderDAO.merge(order);
	}
	
	@Autowired
	private OrderDAO orderDAO;
}
