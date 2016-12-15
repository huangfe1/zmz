package com.dreamer.view.mall;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import ps.mx.otter.utils.SearchParameter;
import ps.mx.otter.utils.WebUtil;
import ps.mx.otter.utils.digest.DigestToolHMAC;

import com.dreamer.domain.account.GoodsAccount;
import com.dreamer.domain.goods.Goods;
import com.dreamer.domain.user.Agent;
import com.dreamer.domain.user.AgentLevelName;
import com.dreamer.domain.user.User;
import com.dreamer.repository.goods.GoodsDAO;
import com.dreamer.repository.user.AgentDAO;
import com.dreamer.repository.user.MutedUserDAO;
import com.dreamer.view.mall.goods.GoodsDTO;

@Controller
@RequestMapping("/dmz/gmall")
@SessionAttributes({ "agentCode", "indexUrl", "ref", "owner" })
public class GMallIndexController {

	private static final DigestToolHMAC HMAC = new DigestToolHMAC();

	
	@RequestMapping(value = "/index.html", method = RequestMethod.GET)
	public String index(
			@RequestParam(value = "ref", required = false) String ref,
			Model model, HttpServletRequest request) {
		if (Objects.nonNull(ref)) {
			model.addAttribute("ref", ref);
		}
		if (WebUtil.isLogin(request)) {
			User user = (User) WebUtil.getCurrentUser(request);
			if (!user.isAgent()) {
				return "common/403";
			}
			Agent agent = agentDAO.findById(user.getId());
			String owner="筑美";
			if(!agent.isTopAgent()){
				owner=agent.getParent().getRealName();
			}
			WebUtil.addSessionAttribute(request, "owner", owner);
			WebUtil.addSessionAttribute(request, "parent", agent.getParent());
			WebUtil.addSessionAttribute(request, "agentCode", agent.getParent()
					.getAgentCode());
			return "gmall/mall_index";
		} else {
			model.addAttribute("agentCode", ref);
			return "gmall/mall_index";
			//return "common/403";
		}
	}

	
	
	
	/**
	 * add by hf
	 * @param agentCode
	 * @param param
	 * @param request
	 * @return
	 */
	
	@RequestMapping(value = "/goods/query.json", method = RequestMethod.GET)
	@ResponseBody
	public List<GoodsDTO> queryGoods(
			@RequestParam("agentCode") String agentCode,
			@ModelAttribute("parameter") SearchParameter<Goods> param,
			HttpServletRequest request) {
		List<GoodsDTO> dtos = new ArrayList<GoodsDTO>();
		//获取当前用户
		User user = (User) WebUtil.getCurrentUser(request);
		Agent currentAgent = agentDAO.findById(user.getId());
		Set<GoodsAccount> gaces=currentAgent.getGoodsAccounts();
		gaces = gaces.stream().skip(param.getStart())
				.limit(param.getLength()).sorted((o1, o2) -> {
					return Integer.compare(o1.getGoods().getOrder(), o2.getGoods().getOrder());
				}).collect(Collectors.toSet());
		for (GoodsAccount gac  : gaces) {
			//如果没有到官方
			if(!gac.getAgentLevel().getName().contains(AgentLevelName.官方.toString())){
				continue;
			}
			Goods g=gac.getGoods();
			GoodsDTO dto = new GoodsDTO();
			dto.setId(g.getId());
			dto.setName(g.getName());
			dto.setSpec(g.getSpec());
			dto.setPrice(g.getRetailPrice());
			String imgUrl = WebUtil.getContextPath(request) + "/dmz/img/goods/"
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
