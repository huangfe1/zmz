package com.dreamer.view.mall.goods;

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

import com.dreamer.domain.goods.StockBlotter;
import com.dreamer.domain.mall.goods.MallGoods;
import com.dreamer.domain.mall.goods.MallGoodsStockBlotter;
import com.dreamer.domain.mall.goods.MallGoodsStockBlotter.MallGoodsStockBlotterView;
import com.dreamer.repository.mall.goods.MallGoodsDAO;
import com.dreamer.repository.mall.goods.MallGoodsStockBlotterDAO;
import com.fasterxml.jackson.annotation.JsonView;

@Controller
@RequestMapping("/stock/pm/")
public class MallGoodsStockBlotterQueryController {

	@RequestMapping(value = { "/index.html", "/search.html" }, method = RequestMethod.GET)
	public String index(
			@ModelAttribute("parameter") SearchParameter<MallGoods> param,
			Integer startStock, Integer endStock, Model model,
			HttpServletRequest request) {
		try {
			List<MallGoods> goods = goodsDAO.search(param, null, startStock,
					endStock, (t) -> true);
			WebUtil.turnPage(param, request);
			model.addAttribute("goods", goods);
			model.addAttribute("startStock", startStock);
			model.addAttribute("endStock", endStock);
		} catch (Exception exp) {
			exp.printStackTrace();
		}
		return "mall/stock_index";
	}

	@RequestMapping(value = { "/edit.html" }, method = RequestMethod.GET)
	public String edit_enter(
			@ModelAttribute("stockBlotter") SearchParameter<StockBlotter> param,
			Model model, HttpServletRequest request) {
		return "mall/stock_edit";
	}

	@RequestMapping(value = { "/detail.html" }, method = RequestMethod.GET)
	public String detail(
			@ModelAttribute("stockBlotter") SearchParameter<MallGoodsStockBlotter> param,
			Model model, HttpServletRequest request) {
		List<MallGoodsStockBlotter> stocks = stockBlotterDAO.searchEntityByPage(param,
				null, (t) -> true);
		model.addAttribute("stocks", stocks);
		return "mall/stock_detail";
	}

	@RequestMapping(value = { "/detail/query.json" }, method = RequestMethod.GET)
	@ResponseBody
	@JsonView(MallGoodsStockBlotterView.class)
	public DatatableDTO<MallGoodsStockBlotter> detailQuery(
			@ModelAttribute("stockBlotter") SearchParameter<MallGoodsStockBlotter> param,
			Model model, HttpServletRequest request) {
		DatatableDTO<MallGoodsStockBlotter> dts = new DatatableDTO<MallGoodsStockBlotter>();
		try {
			List<MallGoodsStockBlotter> stocks = stockBlotterDAO.searchEntityByPage(
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
	public SearchParameter<MallGoods> preprocess(
			@RequestParam("id") Optional<Integer> id) {
		SearchParameter<MallGoods> parameter = new SearchParameter<MallGoods>();
		MallGoods goods = null;
		if (id.isPresent()) {
			goods = (MallGoods) goodsDAO.findById(id.get());
		} else {
			goods = new MallGoods();
		}
		parameter.setEntity(goods);
		return parameter;
	}

	@ModelAttribute("stockBlotter")
	public SearchParameter<MallGoodsStockBlotter> getStockBlotter(
			@RequestParam("id") Optional<Integer> id,
			@RequestParam("goodsId") Optional<Integer> goodsId) {
		SearchParameter<MallGoodsStockBlotter> parameter = new SearchParameter<MallGoodsStockBlotter>();
		MallGoodsStockBlotter stockBlotter = null;
		if (id.isPresent()) {
			stockBlotter = (MallGoodsStockBlotter) stockBlotterDAO.findById(id.get());
		} else {
			stockBlotter = new MallGoodsStockBlotter();
			if (goodsId.isPresent()) {
				MallGoods goods = goodsDAO.findById(goodsId.get());
				stockBlotter.setGoods(goods);
			}
		}
		parameter.setEntity(stockBlotter);
		return parameter;
	}

	@Autowired
	private MallGoodsStockBlotterDAO stockBlotterDAO;
	@Autowired
	private MallGoodsDAO goodsDAO;

	private final Logger LOG = LoggerFactory.getLogger(getClass());
}
