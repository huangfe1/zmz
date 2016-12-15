package com.dreamer.service.authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dreamer.domain.authorization.AuthorizationType;
import com.dreamer.repository.authorization.AuthorizationTypeDAO;

@Service
public class AuthorizationTypeHandler {

	@Transactional
	public void addAuthorizationType(AuthorizationType authType){
		authorizationTypeDAO.save(authType);
	}
	
	@Transactional
	public void updateAuthorizationType(AuthorizationType authType){
		authorizationTypeDAO.merge(authType);
	}
	
	@Transactional
	public void removeAuthorizationType(AuthorizationType authType){
		authType.getGoods().setAuthorizationType(null);
		authType.setGoods(null);
		authorizationTypeDAO.delete(authType);
	}
	
	@Autowired
	private AuthorizationTypeDAO authorizationTypeDAO;
}
