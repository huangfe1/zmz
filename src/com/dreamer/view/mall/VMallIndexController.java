package com.dreamer.view.mall;

import com.dreamer.domain.goods.Goods;
import com.dreamer.domain.goods.GoodsType;
import com.dreamer.domain.user.Agent;
import com.dreamer.domain.user.User;
import com.dreamer.repository.goods.GoodsDAO;
import com.dreamer.repository.user.AgentDAO;
import com.dreamer.repository.user.MutedUserDAO;
import com.dreamer.util.TokenInfo;
import com.dreamer.view.mall.goods.GoodsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ps.mx.otter.utils.SearchParameter;
import ps.mx.otter.utils.WebUtil;
import ps.mx.otter.utils.digest.DigestToolHMAC;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/dmz/vmall")
@SessionAttributes({ "agentCode", "indexUrl", "ref", "owner" })
public class VMallIndexController {
	private static final DigestToolHMAC HMAC = new DigestToolHMAC();

	@RequestMapping(value = "/{agentCode}/index.html", method = RequestMethod.GET)
	public String index(@RequestParam("token") String token,
			@PathVariable("agentCode") String agentCode, Model model,
			HttpServletRequest request) {
		String url = request.getServletPath();
		Agent agent = agentDAO.findByAgentCode(agentCode);

		if (WebUtil.isLogin(request)) {
			User user = (User) WebUtil.getCurrentUser(request);
			Agent currentAgent = agentDAO.findById(user.getId());
//			if(currentAgent.isTopAgent()){
//				model.addAttribute("owner", "筑美");
//			}else{
//				model.addAttribute("owner", currentAgent.getParent().getRealName());
			model.addAttribute("owner", currentAgent.getRealName());//登陆的用户显示自己的小店
			WebUtil.addSessionAttribute(request, "parent",
					currentAgent.getParent());
			return "mall/mall_index";
		} else {
//			model.addAttribute("owner", "筑美");
			model.addAttribute("owner", agent.getRealName());//扫谁的二维码,显示谁的小店
		}
		if (urlValidate(token, agentCode, url)) {
			model.addAttribute("agentCode", agentCode);
			model.addAttribute("indexUrl", WebUtil.getRequstPath(request));
			model.addAttribute("queryUrl", WebUtil.getContextPath(request)
					+ "/dmz/vmall/" + agentCode + "/goods/query.json");
			return "mall/mall_index";
		} else {
			return "common/403";
		}
	}

	@RequestMapping(value = "/index.html", method = RequestMethod.GET)
	public String index(
//			@RequestParam(value = "ref", required = false) String ref,
			Model model, HttpServletRequest request) {
//		if (Objects.nonNull(ref)) {
//			model.addAttribute("ref", ref);
//		}
		if (WebUtil.isLogin(request)) {
			User user = (User) WebUtil.getCurrentUser(request);
			Agent currentAgent = agentDAO.findById(user.getId());
			if (!user.isAgent()) {
				return "common/403";
			}
			Agent agent = agentDAO.findById(user.getId());
//			="筑美";
//			if(!agent.isTopAgent()){
			String owner=currentAgent.getRealName();
//			}
			model.addAttribute("owner", owner);
			WebUtil.addSessionAttribute(request, "owner", owner);
			WebUtil.addSessionAttribute(request, "parent", agent.getParent());
			WebUtil.addSessionAttribute(request, "agentCode", agent.getParent()
					.getAgentCode());
			return "mall/mall_index";
		} else {
//			model.addAttribute("agentCode", ref);
			return "mall/mall_index";
			//return "common/403";
		}
	}

	
	
	@RequestMapping(value = "/menu.html", method = RequestMethod.GET)
	public String menu(Model model, HttpServletRequest request) {
		return "mall/mall_menu";
	}

	private boolean urlValidate(String token, String agentCode, String url) {
		String newToken = HMAC.generateDigest(url, agentCode);
		if (Objects.equals(token, newToken)) {
			return true;
		}
		return false;
	}

	public static void main(String[] args) {
		String digest = HMAC.generateDigest("/dmz/vmall/zmz000002/index.html",
				"zmz000002");
		System.out.println(digest);
	}
	/**
	 * add by hf
	 * @param agentCode
	 * @param param
	 * @param request
	 * @return
	 */
//	
//	@RequestMapping(value = "/goods/hf_query.json", method = RequestMethod.GET)
//	@ResponseBody
//	public List<GoodsDTO> hf_queryGoods(
//			@RequestParam("agentCode") String agentCode,
//			@ModelAttribute("parameter") SearchParameter<Goods> param,
//			HttpServletRequest request) {
//		Agent proxy = agentDAO.findByAgentCode(agentCode);
//		Agent parentAgent = proxy != null ? proxy : mutedUserDAO
//				.findByAgentCode(agentCode);
//		List<Goods> goods = null;
//		if (parentAgent.isMutedUser()
//				|| parentAgent.hasMainGoodsAuthorization()) {
//			goods = goodsDAO.searchEntityByPage(param, null, (t) -> true);
//		} else {
//			goods = parentAgent.loadAuthorizedGoodses();
//			goods = goods.stream().skip(param.getStart())
//					.limit(param.getLength()).sorted((o1, o2) -> {
//						return Integer.compare(o1.getOrder(), o2.getOrder());
//					}).collect(Collectors.toList());
//		}
//
//		List<GoodsDTO> dtos = new ArrayList<GoodsDTO>();
//		//获取当前用户
//		User user = (User) WebUtil.getCurrentUser(request);
//		Agent currentAgent = agentDAO.findById(user.getId());
//		for (Goods g : goods) {
//			//如果没有到官方
//			if(!currentAgent.loadAccountForGoodsNotNull(g).getAgentLevel().getName().contains(AgentLevelName.官方.toString())){
//				goods.remove(g);
//				continue;
//			}
//			GoodsDTO dto = new GoodsDTO();
//			dto.setId(g.getId());
//			dto.setName(g.getName());
//			dto.setSpec(g.getSpec());
//			dto.setPrice(g.getRetailPrice());
//			String imgUrl = WebUtil.getContextPath(request) + "/dmz/img/goods/"
//					+ g.getImgFile();
//			dto.setImgUrl(imgUrl);
//			dtos.add(dto);
//		}
//		return dtos;
//	}
//	

	@RequestMapping(value = "/goods/query.json", method = RequestMethod.GET)
	@ResponseBody
	public List<GoodsDTO> queryGoods(
			@RequestParam("agentCode") String agentCode,
			@ModelAttribute("parameter") SearchParameter<Goods> param,
			HttpServletRequest request) {
		Agent proxy = agentDAO.findByAgentCode(agentCode);
		Agent parentAgent = proxy != null ? proxy : mutedUserDAO
				.findByAgentCode(agentCode);
		List<Goods> goods = null;
//		if (parentAgent.isMutedUser()
//				|| parentAgent.hasMainGoodsAuthorization(null)) {
			goods = goodsDAO.searchEntityByPage(param, null, (t) -> true);
//		}
//		else {
//			goods = parentAgent.loadAuthorizedGoodses();
//			goods = goods.stream().skip(param.getStart())
//					.limit(param.getLength()).sorted((o1, o2) -> {
//						return Integer.compare(o1.getOrder(), o2.getOrder());
//					}).collect(Collectors.toList());
//		}
		List<GoodsDTO> dtos = new ArrayList<GoodsDTO>();
		for (Goods g : goods) {
			GoodsDTO dto = new GoodsDTO();
			dto.setId(g.getId());
			dto.setName(g.getName());
			dto.setSpec(g.getSpec());
			dto.setPrice(g.getRetailPrice());
//			String imgUrl = WebUtil.getContextPath(request) + "/dmz/img/goods/"
//					+ g.getImgFile();
			String imgUrl = TokenInfo.IMG_HEAD_PATH + "/dmz/img/goods/"
					+ g.getImgFile();
			dto.setImgUrl(imgUrl);
			dtos.add(dto);
		}
		return dtos;
	}

	@ModelAttribute("parameter")
	public SearchParameter<Goods> preprocessing() {
		SearchParameter<Goods> param = new SearchParameter<Goods>();
		Goods goods = new Goods();
		//代理商城
		goods.setGoodsType(GoodsType.MALL);
		param.setEntity(goods);
		return param;
	}

	@Autowired
	private GoodsDAO goodsDAO;
	@Autowired
	private AgentDAO agentDAO;
	@Autowired
	private MutedUserDAO mutedUserDAO;

}
