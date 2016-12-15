package com.dreamer.view.goods;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ps.mx.otter.utils.SearchParameter;
import ps.mx.otter.utils.WebUtil;

import com.dreamer.domain.goods.Goods;
import com.dreamer.domain.goods.Price;
import com.dreamer.domain.user.AgentLevel;
import com.dreamer.repository.goods.GoodsDAO;
import com.dreamer.repository.user.AgentLevelDAO;

@Controller
@RequestMapping("/dmz/goods")
public class GoodsDMZQueryController {

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
		return "/goods/goods_index";
	}

	
	@RequestMapping(value="/names.json",method=RequestMethod.GET)
	@ResponseBody
	public List<String> searchGoodsNames(@ModelAttribute("parameter") SearchParameter<Goods> parameter,
			HttpServletRequest request, Model model){
		List<Goods> goods=goodsDAO.findByExample(parameter.getEntity());
		List<String> names=new ArrayList<String>();
		goods.forEach((g)->{
			names.add(g.getName());
		});
		return names;
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
			return "/goods/goods_detail";

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
	private GoodsDAO goodsDAO;
	@Autowired
	private AgentLevelDAO agentLevelDAO;

	private final Logger LOG = LoggerFactory.getLogger(getClass());

}
