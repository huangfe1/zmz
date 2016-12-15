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
@RequestMapping("/dmz/tmall")
@SessionAttributes({ "agentCode", "indexUrl", "ref", "owner" })
public class TMallIndexController {
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
			return "tmall/mall_index";
		} else {
//			model.addAttribute("agentCode", ref);
			return "tmall/mall_index";
			//return "common/403";
		}
	}

	
	
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
//		} else {
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
			String imgUrl = TokenInfo.IMG_HEAD_PATH+ "/dmz/img/goods/"
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
		//特权商城
		goods.setGoodsType(GoodsType.TEQ);
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
