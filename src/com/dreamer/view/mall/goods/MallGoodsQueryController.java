package com.dreamer.view.mall.goods;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;
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

import com.dreamer.domain.goods.GoodsType;
import com.dreamer.domain.mall.goods.MallGoods;
import com.dreamer.domain.user.User;
import com.dreamer.repository.mall.goods.MallGoodsDAO;
import com.fasterxml.jackson.annotation.JsonView;

@Controller
@RequestMapping("/mall/goods")
public class MallGoodsQueryController {

	@RequestMapping(value = { "/index.html", "/search.html" }, method = RequestMethod.GET)
	public String index(
			@ModelAttribute("parameter") SearchParameter<MallGoods> parameter,
			HttpServletRequest request, Model model) {
		try {
			/*List<MallGoods> goods = mallGoodsDAO.searchEntityByPage(parameter, null,
					(t) -> true);
			WebUtil.turnPage(parameter, request);
			model.addAttribute("goods", goods);*/
		} catch (Exception exp) {
			exp.printStackTrace();
			LOG.error("产品查询失败", exp);
		}
		return "/mall/mallGoods_index";
	}
	
	@RequestMapping(value = { "/query.json", "/search.json" }, method = RequestMethod.GET)
	@ResponseBody
	@JsonView(MallGoods.MallGoodsView.class)
	public DatatableDTO<MallGoods> queryASJson(
			@ModelAttribute("parameter") SearchParameter<MallGoods> parameter,
			HttpServletRequest request, Model model) {
		DatatableDTO<MallGoods> dts=new DatatableDTO<MallGoods>();
		try {
			List<MallGoods> goods = mallGoodsDAO.searchEntityByPage(parameter, null,
					(t) -> true);
			WebUtil.turnPage(parameter, request);
			dts.setData(goods);
			dts.setRecordsFiltered(parameter.getTotalRows());
			dts.setRecordsTotal(parameter.getTotalRows());
		} catch (Exception exp) {
			exp.printStackTrace();
			LOG.error("产品查询失败", exp);
		}
		return dts;
	}
	

	@RequestMapping(value = "/edit.html", method = RequestMethod.GET)
	public String edit_enter(
			@ModelAttribute("parameter") SearchParameter<MallGoods> parameter,
			HttpServletRequest request, Model model) {
		User user = (User) WebUtil.getCurrentUser(request);
		if (user.isAdmin()) {
			return "/mall/mallGoods_edit";
		} else {
			LOG.error("非管理员身份,无积分商城商品编辑权限");
			return "/common/403";
		}

	}
	


	@ModelAttribute("parameter")
	public SearchParameter<MallGoods> preprocess(
			@RequestParam("id") Optional<Integer> id) {
		SearchParameter<MallGoods> parameter = new SearchParameter<MallGoods>();
		MallGoods goods = null;
		if (id.isPresent()) {
			goods = (MallGoods) mallGoodsDAO.findById(id.get());
		} else {
			goods = new MallGoods();
		}
		parameter.setEntity(goods);
		return parameter;
	}

	@Autowired
	private MallGoodsDAO mallGoodsDAO;

	private final Logger LOG = LoggerFactory.getLogger(getClass());

}
