package com.dreamer.view.goods;

import java.util.List;
import java.util.Optional;

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

import ps.mx.otter.utils.DatatableDTO;
import ps.mx.otter.utils.SearchParameter;
import ps.mx.otter.utils.WebUtil;

import com.dreamer.domain.goods.Goods;
import com.dreamer.domain.goods.StockBlotter;
import com.dreamer.domain.goods.StockBlotter.StockBlotterView;
import com.dreamer.repository.goods.GoodsDAO;
import com.dreamer.repository.goods.StockBlotterDAO;
import com.fasterxml.jackson.annotation.JsonView;

@Controller
@RequestMapping("/stock")
public class StockBlotterQueryController {

	@RequestMapping(value = { "/index.html", "/search.html" }, method = RequestMethod.GET)
	public String index(
			@ModelAttribute("parameter") SearchParameter<Goods> param,
			Integer startStock, Integer endStock, Model model,
			HttpServletRequest request) {
		try {
			List<Goods> goods = goodsDAO.search(param, null, startStock,
					endStock, (t) -> true);
			WebUtil.turnPage(param, request);
			model.addAttribute("goods", goods);
			model.addAttribute("startStock", startStock);
			model.addAttribute("endStock", endStock);
		} catch (Exception exp) {
			exp.printStackTrace();
		}
		return "goods/stock_index";
	}

	@RequestMapping(value = { "/edit.html" }, method = RequestMethod.GET)
	public String edit_enter(
			@ModelAttribute("stockBlotter") SearchParameter<StockBlotter> param,
			Model model, HttpServletRequest request) {
		return "goods/stock_edit";
	}

	@RequestMapping(value = { "/detail.html" }, method = RequestMethod.GET)
	public String detail(
			@ModelAttribute("stockBlotter") SearchParameter<StockBlotter> param,
			Model model, HttpServletRequest request) {
		List<StockBlotter> stocks = stockBlotterDAO.searchEntityByPage(param,
				null, (t) -> true);
		model.addAttribute("stocks", stocks);
		return "goods/stock_detail";
	}

	@RequestMapping(value = { "/detail/query.json" }, method = RequestMethod.GET)
	@ResponseBody
	@JsonView(StockBlotterView.class)
	public DatatableDTO<StockBlotter> detailQuery(
			@ModelAttribute("stockBlotter") SearchParameter<StockBlotter> param,
			Model model, HttpServletRequest request) {
		DatatableDTO<StockBlotter> dts = new DatatableDTO<StockBlotter>();
		try {
			List<StockBlotter> stocks = stockBlotterDAO.searchEntityByPage(
					param, null, (t) -> true);
			dts.setData(stocks);
			WebUtil.turnPage(param, request);
			dts.setRecordsTotal(param.getTotalRows());
			dts.setRecordsFiltered(param.getTotalRows());
		} catch (Exception exp) {
			exp.printStackTrace();
			LOG.error("库存流水查询失败", exp);
		}
		return dts;
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

	@ModelAttribute("stockBlotter")
	public SearchParameter<StockBlotter> getStockBlotter(
			@RequestParam("id") Optional<Integer> id,
			@RequestParam("goodsId") Optional<Integer> goodsId) {
		SearchParameter<StockBlotter> parameter = new SearchParameter<StockBlotter>();
		StockBlotter stockBlotter = null;
		if (id.isPresent()) {
			stockBlotter = (StockBlotter) stockBlotterDAO.findById(id.get());
		} else {
			stockBlotter = new StockBlotter();
			if (goodsId.isPresent()) {
				Goods goods = goodsDAO.findById(goodsId.get());
				stockBlotter.setGoods(goods);
			}
		}
		parameter.setEntity(stockBlotter);
		return parameter;
	}

	@Autowired
	private StockBlotterDAO stockBlotterDAO;
	@Autowired
	private GoodsDAO goodsDAO;

	private final Logger LOG = LoggerFactory.getLogger(getClass());
}
