package com.dreamer.view.goods;

import com.dreamer.domain.goods.DeliveryNote;
import com.dreamer.domain.goods.Goods;
import com.dreamer.domain.goods.GoodsType;
import com.dreamer.domain.goods.Price;
import com.dreamer.domain.user.Agent;
import com.dreamer.domain.user.AgentLevel;
import com.dreamer.domain.user.MutedUser;
import com.dreamer.domain.user.User;
import com.dreamer.repository.goods.GoodsDAO;
import com.dreamer.repository.goods.PriceDAO;
import com.dreamer.repository.user.AgentDAO;
import com.dreamer.repository.user.AgentLevelDAO;
import com.dreamer.repository.user.MutedUserDAO;
import com.dreamer.view.mall.goods.GoodsDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ps.mx.otter.utils.Constant;
import ps.mx.otter.utils.DatatableDTO;
import ps.mx.otter.utils.SearchParameter;
import ps.mx.otter.utils.WebUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/goods")
public class GoodsQueryController {

	@RequestMapping(value = { "/index.html", "/search.html" }, method = RequestMethod.GET)
	public String index(
			@ModelAttribute("parameter") SearchParameter<Goods> parameter,
			HttpServletRequest request, Model model) {
		try {
			List<Goods> goods = goodsDAO.searchEntityByPage(parameter, null,
					(t) -> true);
			WebUtil.turnPage(parameter, request);
			model.addAttribute("goods", goods);
		} catch (Exception exp) {
			exp.printStackTrace();
			LOG.error("产品查询失败", exp);
		}
		return "goods/goods_index";
	}

	@RequestMapping(value = "/edit.html", method = RequestMethod.GET)
	public String edit_enter(
			@ModelAttribute("parameter") SearchParameter<Goods> parameter,
			HttpServletRequest request, Model model) {
		User user = (User) WebUtil.getCurrentUser(request);
		if (user.isAdmin()) {
			List<AgentLevel> levels = agentLevelDAO.findAll();
			Set<Price> prices = parameter.getEntity().getPrices();
			HashMap<Integer, Price> maps = new HashMap<Integer, Price>();
			Iterator<Price> ite = prices.iterator();
			while (ite.hasNext()) {
				Price p = ite.next();
				maps.put(p.getAgentLevel().getId(), p);
			}
			model.addAttribute("types", GoodsType.values());
			model.addAttribute("levels", levels);
			model.addAttribute("prices", maps);
			return "goods/goods_edit";
		} else {
			LOG.error("非管理员身份,无产品编辑权限");
			return "common/403";
		}

	}

	@RequestMapping(value = "/detail.html", method = RequestMethod.GET)
	public String detail(
			@ModelAttribute("parameter") SearchParameter<Goods> parameter,
			HttpServletRequest request, Model model) {
		List<AgentLevel> levels = agentLevelDAO.findAll();
		Set<Price> prices = parameter.getEntity().getPrices();
		HashMap<Integer, Price> maps = new HashMap<Integer, Price>();
		Iterator<Price> ite = prices.iterator();
		while (ite.hasNext()) {
			Price p = ite.next();
			maps.put(p.getAgentLevel().getId(), p);
		}
		model.addAttribute("levels", levels);
		model.addAttribute("prices", maps);
		return "goods/goods_detail";

	}

	@RequestMapping(value = "/addAutyType.html", method = RequestMethod.GET)
	public String addAuthType_enter(
			@ModelAttribute("parameter") SearchParameter<Goods> parameter,
			HttpServletRequest request, Model model) {
		try {
			User user = (User) WebUtil.getCurrentUser(request);
			if (user.isAdmin()) {

			} else {
				LOG.error("非管理员身份,无产品授权增加权限");
				return "common/403";
			}
		} catch (Exception exp) {
			exp.printStackTrace();
			LOG.error("进入新增授权类型失败", exp);
		}
		return "authorization/authType_edit";
	}

	@RequestMapping(value = "/authGoods.html", method = RequestMethod.GET)
	public String selectGoodsEnter(
			@ModelAttribute("parameter") SearchParameter<DeliveryNote> parameter,
			HttpServletRequest request, Model model) {
		try {
		} catch (Exception exp) {
			LOG.error("进入货物选择界面失败,", exp);
			exp.printStackTrace();
		}
		return "goods/select_authGoods";
	}

	@RequestMapping(value = "/authGoods.json", method = RequestMethod.GET)
	@ResponseBody
	public DatatableDTO<GoodsDTO> queryGoods(
			@ModelAttribute("parameter") SearchParameter<Goods> parameter,
			HttpServletRequest request, Model model,int toback) {
		DatatableDTO<GoodsDTO> dto = new DatatableDTO<GoodsDTO>();
		try {
			User user = (User) WebUtil.getCurrentUser(request);
			Agent agent = user.isAdmin() ? mutedUserDAO
					.findById(((MutedUser) WebUtil.getSessionAttribute(request,
							Constant.MUTED_USER_KEY)).getId()) : agentDAO
					.findById(user.getId());
			List<Goods> goods = null;
			if (user.isAdmin() || agent.hasMainGoodsAuthorization(null)) {
                if(toback==1){//如果是退货,只能退特权商城产品
                    parameter.getEntity().setGoodsType(GoodsType.TEQ);
                }
				goods = goodsDAO.searchEntityByPage(parameter, null, null);
				WebUtil.turnPage(parameter, request);
				
				dto.setData(goods
						.stream()
						.map(g -> {
							GoodsDTO d = new GoodsDTO();
							d.setId(g.getId());
							d.setName(g.getName());
							if (!agent.isMutedUser()) {
								Price levelPrice = agent.caculatePrice(g,0);
								int notLevel = agent.loadAccountForGoodsNotNull(g)
										.getAgentLevel()
										.myEqual(levelPrice.getAgentLevel()) ? 0
										: 1;
								d.setLevelName(levelPrice.getAgentLevel()
										.getName()
										+ (notLevel > 0 ? "(未认证)" : ""));
								d.setNotLevel(notLevel);
								d.setPrice(levelPrice.getPrice());
							} else {
								d.setPrice(g.getRetailPrice());
							}
							d.setSpec(g.getSpec());
							d.setImgUrl(WebUtil.getContextPath(request)
									+ "/dmz/img/goods/" + g.getImgFile());
							return d;
						}).collect(Collectors.toList()));
				dto.setRecordsFiltered(parameter.getTotalRows());
				dto.setRecordsTotal(parameter.getTotalRows());
			} else {
				goods = agent.loadActivedAuthorizedGoodses();
				List<Goods> page = goods.stream().skip(parameter.getStart()).sorted((Goods o1, Goods o2)->(o1.getGoodsType().compareTo(o2.getGoodsType())))
						.limit(parameter.getLength())
						.collect(Collectors.toList());
				if(toback==1){
					page=page.stream().filter(g-> g.getGoodsType()==GoodsType.TEQ).collect(Collectors.toList());
				}
				dto.setData(page
						.stream()
						.map(g -> {
                            GoodsDTO d = new GoodsDTO();
							d.setId(g.getId());
							d.setName(g.getName());
							Price levelPrice = agent.caculatePrice(g,0);
							int notLevel = agent.loadAccountForGoodsNotNull(g)
									.getAgentLevel()
									.myEqual(levelPrice.getAgentLevel()) ? 0
									: 1;
							d.setLevelName(levelPrice.getAgentLevel().getName()
									+ (notLevel > 0 ? "(未认证)" : ""));
							d.setNotLevel(notLevel);
							d.setPrice(levelPrice.getPrice());
							d.setSpec(g.getSpec());
							d.setImgUrl(WebUtil.getContextPath(request)
									+ "/dmz/img/goods/" + g.getImgFile());
							return d;
						}).collect(Collectors.toList()));
				dto.setRecordsFiltered(goods.size());
				dto.setRecordsTotal(goods.size());
			}

		} catch (Exception exp) {
			LOG.error("查询货物失败,", exp);
			exp.printStackTrace();
		}
		return dto;
	}

	@ModelAttribute("parameter")
	public SearchParameter<Goods> preprocess(
			@RequestParam("id") Optional<Integer> id) {
		SearchParameter<Goods> parameter = new SearchParameter<Goods>();
		Goods goods = null;
		if (id.isPresent()) {
			goods = (Goods) goodsDAO.findById(id.get());
		} else {
			goods = new Goods();
		}
		parameter.setEntity(goods);
		return parameter;
	}

	
	@Autowired
	private MutedUserDAO mutedUserDAO;
	@Autowired
	private GoodsDAO goodsDAO;
	@Autowired
	private AgentDAO agentDAO;
	@Autowired
	private AgentLevelDAO agentLevelDAO;
	@Autowired
	private PriceDAO priceDAO;

	private final Logger LOG = LoggerFactory.getLogger(getClass());

}
