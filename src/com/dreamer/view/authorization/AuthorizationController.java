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

import com.dreamer.domain.authorization.Authorization;
import com.dreamer.domain.authorization.AuthorizationType;
import com.dreamer.repository.authorization.AuthorizationDAO;
import com.dreamer.repository.goods.GoodsDAO;
import com.dreamer.service.goods.GoodsHandler;

@RestController
@RequestMapping("/auth")
public class AuthorizationController {

	@RequestMapping(value = "/active.json", method = RequestMethod.POST)
	public Message edit(@ModelAttribute("parameter") Authorization auth, Model model) {
		try {
			if(auth.getId()!=null){
			}else{
			}
			return Message.createSuccessMessage();
		} catch (Exception exp) {
			exp.printStackTrace();
			LOG.error("授权编辑保存失败", exp);
			return Message.createFailedMessage();
		}
	}

	@RequestMapping(value = "/remove.json", method = RequestMethod.DELETE)
	public Message remove(@ModelAttribute("parameter") AuthorizationType auth, Model model) {
		try {
			return Message.createSuccessMessage();
		} catch (Exception exp) {
			exp.printStackTrace();
			LOG.error("授权删除失败", exp);
			return Message.createFailedMessage();
		}
	}

	@ModelAttribute("parameter")
	public Authorization preprocess(@RequestParam("id") Optional<Integer> id) {
		Authorization auth = new Authorization();
		if (id.isPresent()) {
			auth = authorizationDAO.findById(id.get());
		}
		return auth;
	}

	@Autowired
	private GoodsDAO goodsDAO;
	@Autowired
	private GoodsHandler goodsHandler;
	@Autowired
	private AuthorizationDAO authorizationDAO;

	private final Logger LOG = LoggerFactory.getLogger(getClass());

}
