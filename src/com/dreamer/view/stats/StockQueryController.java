package com.dreamer.view.stats;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

import com.dreamer.domain.account.GoodsAccount;
import com.dreamer.domain.goods.Goods;
import com.dreamer.domain.user.Agent;
import com.dreamer.repository.account.GoodsAccountDAO;
import com.dreamer.repository.goods.GoodsDAO;
import com.dreamer.view.goods.GoodsAccountDTO;

@Controller
@RequestMapping("/stats")
public class StockQueryController {

	@RequestMapping(value = "/stock.html")
	public String queryStockstatistics(
			@RequestParam(value = "goodsName", required = false) String goodsName,
			HttpServletRequest request, Model model) {
		try {
			List<Object[]> result = goodsDAO.queryStockstatistics(goodsName);
			model.addAttribute("result", result);
		} catch (Exception exp) {
			exp.printStackTrace();
		}
		return "stats/stock_stats";
	}

	 @RequestMapping(value = "/goodsBalance.html")
	public String goodsBalance(
			@RequestParam(value = "goodsId", required = false) Integer goodsId,
			HttpServletRequest request, Model model) {
		try{
			Goods goods=goodsDAO.findById(goodsId);
			model.addAttribute("goods", goods);
		}catch(Exception exp){
			exp.printStackTrace();
		}		
		return "stats/stock_balance_detail";
	}

	@RequestMapping(value = "/goodsBalance.json", method = RequestMethod.GET)
	@ResponseBody
	public DatatableDTO<GoodsAccountDTO> queryGoodsAccountBalance(
			@ModelAttribute("parameter") SearchParameter<GoodsAccount> parameter,
			@RequestParam(value = "goodsId") Integer goodsId,
			HttpServletRequest request, Model model) {
		DatatableDTO<GoodsAccountDTO> dtos = new DatatableDTO<GoodsAccountDTO>();
		try {
			List<GoodsAccount> acs = goodsAccountDAO.findAccountsByGoodsId(
					parameter, goodsId);
			WebUtil.turnPage(parameter, request);
			dtos.setData(acs.stream().map(g -> {
				GoodsAccountDTO dto = new GoodsAccountDTO();
				dto.setGoods(g.getGoods());
				Agent agent =(Agent)g.getUser();
				dto.setAgentCode(agent.getAgentCode());
				dto.setAgentName(g.getUser().getRealName());
				dto.setCurrentBalance(g.getCurrentBalance());
				dto.setCumulative(g.getCumulative());
				return dto;
			}).collect(Collectors.toList()));
			dtos.setRecordsFiltered(parameter.getTotalRows());
			dtos.setRecordsTotal(parameter.getTotalRows());
		} catch (Exception exp) {
			LOG.error("查询指定商品的所有代理商余额异常", exp);
			exp.printStackTrace();
		}
		return dtos;
	}

	@ModelAttribute("parameter")
	public SearchParameter<GoodsAccount> preprocess(
			@RequestParam("id") Optional<Integer> id) {
		SearchParameter<GoodsAccount> parameter = new SearchParameter<GoodsAccount>();
		parameter.setEntity(new GoodsAccount());
		return parameter;
	}

	@Autowired
	private GoodsDAO goodsDAO;

	@Autowired
	private GoodsAccountDAO goodsAccountDAO;

	private final Logger LOG = LoggerFactory.getLogger(getClass());
}
