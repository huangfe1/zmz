package com.dreamer.view.goods;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import ps.mx.otter.utils.DatatableDTO;
import ps.mx.otter.utils.SearchParameter;
import ps.mx.otter.utils.WebUtil;

import com.dreamer.domain.goods.Price;
import com.dreamer.domain.user.AgentLevel;
import com.dreamer.domain.user.User;
import com.dreamer.repository.goods.PriceDAO;
import com.dreamer.repository.user.AgentLevelDAO;

@Controller
@RequestMapping("/price")
public class PriceQueryController {

	@RequestMapping(value = "/{level}/goods.json", method = RequestMethod.GET)
	@ResponseBody
	public DatatableDTO<PriceDTO> queryAgentLevelGoods(
			@ModelAttribute("parameter") SearchParameter<Price> parameter,
			@PathVariable Integer level, HttpServletRequest request, Model model) {
		DatatableDTO<PriceDTO> dto = new DatatableDTO<PriceDTO>();
		try {
			User user = (User) WebUtil.getCurrentUser(request);
			if(!user.isAdmin()){
				return dto;
			}
			AgentLevel agentLevel=agentLevelDAO.findById(level);			
			List<Price> prices=priceDAO.searchAllPriceUnderAgentLevelByPage(parameter, agentLevel);
			WebUtil.turnPage(parameter, request);
			List<PriceDTO> dtos=prices.stream().map(p->{
				PriceDTO d=new PriceDTO();
				d.setGoodsName(p.getGoods().getName());
				d.setPrice(p.getPrice());
				d.setThreshold(p.getThreshold());
				d.setLevelName(p.getAgentLevel().getName());
				return d;
			}).collect(Collectors.toList());
			dto.setData(dtos);
			dto.setRecordsFiltered(parameter.getTotalRows());
			dto.setRecordsTotal(parameter.getTotalRows());

		} catch (Exception exp) {
			LOG.error("查询等级价格失败,", exp);
			exp.printStackTrace();
		}
		return dto;
	}
	
	@Autowired
	private PriceDAO priceDAO;
	@Autowired
	private AgentLevelDAO agentLevelDAO;
	
	@ModelAttribute("parameter")
	public SearchParameter<Price> preprocess(){
		 SearchParameter<Price> parameter=new  SearchParameter<Price>();
		 Price price=new Price();
		 parameter.setEntity(price);
		 return parameter;
	}
	private final Logger LOG = LoggerFactory.getLogger(getClass());
}
