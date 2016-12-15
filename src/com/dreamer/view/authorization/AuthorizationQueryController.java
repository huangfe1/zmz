package com.dreamer.view.authorization;

import com.dreamer.domain.authorization.Authorization;
import com.dreamer.domain.user.Agent;
import com.dreamer.repository.authorization.AuthorizationDAO;
import com.dreamer.service.user.AuthorizationLetterHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.util.Optional;

@Controller
@RequestMapping("/auth")
public class AuthorizationQueryController {

	@RequestMapping(value = "/letter.html", method = RequestMethod.GET)
	public String myLetter(@ModelAttribute("parameter") Authorization auth, Model model) {
		try {
			/*Agent agent=auth.getAgent();
			Goods goods=auth.getAuthorizationType().getGoods();
			model.addAttribute("name", agent.getRealName());
			String wx= agent.getWeixin();
			model.addAttribute("wx",replacePrivacy(wx));
			String mobile=agent.getMobile();
			model.addAttribute("mobile",replacePrivacy(mobile));
			String idcard=agent.getIdCard();
			model.addAttribute("idcard",replacePrivacy(idcard));
			model.addAttribute("agentCode",agent.getAgentCode());
			model.addAttribute("goods",goods.getName());
			GoodsAccount gac=agent.loadAccountForGoodsNotNull(goods);
			model.addAttribute("level", gac.getAgentLevel().getName());
			model.addAttribute("date", DateUtil.date2string(gac.getUpdateTime(),DateUtil.DATE_FORMAT));
*/
			model.addAttribute("code", auth.getAgent().getAgentCode());
			model.addAttribute("id", auth.getId());
		} catch (Exception exp) {
			exp.printStackTrace();
			LOG.error("进入授权证书失败", exp);
		}
		return "authorization/letter";
	}
	
	@RequestMapping(value = "/dmz/letterimg/{agentCode}.html", method = RequestMethod.GET)
	public void letterJpg(@ModelAttribute("parameter") Authorization auth,
			@PathVariable("agentCode") String agentCode,
			Model model,HttpServletRequest request,HttpServletResponse response) {
		OutputStream out=null;
		try {
			response.setContentType("image/jpeg");
			Agent agent=auth.getAgent();
			String dir=request.getServletContext().getRealPath("/WEB-INF/view/letter");
			byte[] letterImg=authorizationLetterHandler.generateLetter(agent, auth, Paths.get(dir, "letter.jpg"));
			 out=response.getOutputStream();
			out.write(letterImg);
			out.flush();
		} catch (Exception exp) {
			exp.printStackTrace();
			LOG.error("授权证书生成失败", exp);
		}finally {
			try{
				out.close();
			}catch(Exception e){
				LOG.error("关闭流异常"+e);
			}
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
	private AuthorizationDAO authorizationDAO;
	
	@Autowired
	private AuthorizationLetterHandler authorizationLetterHandler;
	

	private final Logger LOG = LoggerFactory.getLogger(getClass());

}
