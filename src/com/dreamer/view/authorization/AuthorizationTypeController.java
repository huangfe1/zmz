package com.dreamer.view.authorization;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ps.mx.otter.utils.message.Message;

import com.dreamer.domain.authorization.AuthorizationType;
import com.dreamer.repository.authorization.AuthorizationTypeDAO;
import com.dreamer.repository.goods.GoodsDAO;
import com.dreamer.service.authorization.AuthorizationTypeHandler;
import com.dreamer.service.goods.GoodsHandler;

@RestController
@RequestMapping("/authType")
public class AuthorizationTypeController {

	@RequestMapping(value = "/edit.json", method = RequestMethod.POST)
	public Message edit(@ModelAttribute("parameter") AuthorizationType authType, Model model) {
		try {
			if(authType.getId()!=null){
				authorizationTypeHandler.updateAuthorizationType(authType);
			}else{
				authorizationTypeHandler.addAuthorizationType(authType);
			}
			return Message.createSuccessMessage();
		} catch (Exception exp) {
			exp.printStackTrace();
			LOG.error("授权类型编辑保存失败", exp);
			return Message.createFailedMessage();
		}
	}

	@RequestMapping(value = "/remove.json", method = RequestMethod.DELETE)
	public Message remove(@ModelAttribute("parameter") AuthorizationType authType, Model model) {
		try {
			authorizationTypeHandler.removeAuthorizationType(authType);
			return Message.createSuccessMessage();
		} catch (Exception exp) {
			exp.printStackTrace();
			LOG.error("授权类型删除失败", exp);
			return Message.createFailedMessage();
		}
	}

	@ModelAttribute("parameter")
	public AuthorizationType preprocess(@RequestParam("id") Optional<Integer> id) {
		AuthorizationType authType = new AuthorizationType();
		if (id.isPresent()) {
			authType = authorizationTypeDAO.findById(id.get());
		}
		return authType;
	}

	@Autowired
	private GoodsDAO goodsDAO;
	@Autowired
	private GoodsHandler goodsHandler;
	@Autowired
	private AuthorizationTypeDAO authorizationTypeDAO;
	@Autowired
	private AuthorizationTypeHandler authorizationTypeHandler;
	

	private final Logger LOG = LoggerFactory.getLogger(getClass());

}
