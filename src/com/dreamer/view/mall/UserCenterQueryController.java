package com.dreamer.view.mall;

import com.dreamer.domain.goods.DeliveryNote;
import com.dreamer.domain.user.Agent;
import com.dreamer.repository.goods.DeliveryNoteDAO;
import com.dreamer.repository.user.AgentDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;
import ps.mx.otter.utils.WebUtil;
import ps.mx.otter.utils.digest.DigestToolHMAC;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/vmall/uc/")
@SessionAttributes({"agentCode","url","ref","purl"})
public class UserCenterQueryController {

	@RequestMapping(value = "/dmz/register.html", method = RequestMethod.GET)
	public String register(
			Model model, HttpServletRequest request) {
		model.addAttribute("agentCode", request.getSession().getAttribute("agentCode"));
		model.addAttribute("pUrl", request.getSession().getAttribute("purl"));
		return "mall/register";
	}

	/**
	 * 二维码分享过来的注册
	 * @param model
	 * @param request
	 * @param agenCode
	 * @param pUrl
     * @return
     */
	@RequestMapping(value = "/dmz/hfregister.html", method = RequestMethod.GET)
	public String hfregister(
			Model model, HttpServletRequest request,String agentCode) {
		model.addAttribute("agentCode", agentCode);
		return "mall/register";
	}

	@RequestMapping(value = {"/dmz/login.html","/dmz/slogin.html"}, method = RequestMethod.GET)
	public String login(
			@RequestParam(value = "url", required = false) String url,
            @RequestParam(value = "gid", required = false) String gid,
			Model model, HttpServletRequest request,@RequestParam(value = "myCode", required = false) String myCode) {
        if (Objects.nonNull(myCode)) {//分享过来的
            model.addAttribute("agentCode", myCode);
            String path="/dmz/pmall/detail.html?myCode={myCode}&&id={id}";
            UriComponents ucb = ServletUriComponentsBuilder.fromContextPath(request
                    ).path(path).buildAndExpand(myCode,
                     gid);
            url=ucb.toUriString();
            model.addAttribute("purl", url);//这个会放入到session中  然后注册页面获取
			if(myCode.equals("")){//如果不是代理分享的,没有上级代理链接的
			return  "mall/login_only";
			}
            return "mall/login";
        }

        if (Objects.nonNull(url)) {
			String reUrl=url;
			if(url.indexOf("/vmall/")>-1){
				UriComponents ucb = ServletUriComponentsBuilder
						.fromContextPath(request).path("/dmz/vmall/index.html").build();
				reUrl=ucb.toUriString();
			}else{
				UriComponents ucb = ServletUriComponentsBuilder
						.fromContextPath(request).path("/dmz/pmall/index.html").build();
				reUrl=ucb.toUriString();
			}
			model.addAttribute("url", reUrl);
			if(url.indexOf("ref=01")>=0||url.indexOf("ref=1")>=0){
				return "mall/login_only";
			}
		}
		if(request.getRequestURI().indexOf("slogin.html")>-1){
			return "mall/login_only";
		}
		return "mall/login";
	}
	
	@RequestMapping(value = "/index.html", method = RequestMethod.GET)
	public String index(@RequestParam(value="new",required=false) String newUser,
			Model model, HttpServletRequest request) {
		if(Objects.nonNull(newUser) && !newUser.isEmpty()){
			model.addAttribute("new",newUser);
		}
		Agent agent=(Agent)WebUtil.getCurrentUser(request);
		Agent my=agentDAO.findById(agent.getId());
		List<DeliveryNote> agents=devileryNoteDAO.findByAgent(agent);
		model.addAttribute("orders", agents);
		model.addAttribute("vip",my.isTeqVip());//添加是否是VIP
		model.addAttribute("my",my);
		return "mall/uc/uc_index";
	}

	@RequestMapping(value = "/promoCode.html", method = RequestMethod.GET)
	public String promoCode(Model model, HttpServletRequest request) {
		return "mall/uc/uc_promoCode";
	}
	@RequestMapping(value = "/dmz/promoCode.html/{agentCode}", method = RequestMethod.GET)
	public String dmzpromoCode(Model model, HttpServletRequest request, @PathVariable("agentCode") String agentCode) {
		DigestToolHMAC HMAC=new DigestToolHMAC();
		if (WebUtil.isLogin(request)) {//如果是登陆状态
			return "mall/uc/uc_promoCode";
		}
		StringBuilder sbd=new StringBuilder();
		sbd.append("/dmz/vmall/").append(agentCode).append("/index.html");
		String token=HMAC.generateDigest(sbd.toString(), agentCode);
		return "redirect:"+sbd.append("?token=").append(token).toString();
	}
	@Autowired
	private DeliveryNoteDAO devileryNoteDAO;
	@Autowired
	private AgentDAO agentDAO;
}
