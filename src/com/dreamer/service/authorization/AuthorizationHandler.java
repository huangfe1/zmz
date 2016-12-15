package com.dreamer.service.authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dreamer.repository.authorization.AuthorizationDAO;

@Service
public class AuthorizationHandler {

	
	@Autowired
	private AuthorizationDAO authorizationDAO;
}
