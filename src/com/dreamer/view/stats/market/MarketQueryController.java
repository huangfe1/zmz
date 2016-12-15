package com.dreamer.view.stats.market;

import java.util.List;
import java.util.Objects;
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

import ps.mx.otter.utils.Constant;
import ps.mx.otter.utils.DatatableDTO;
import ps.mx.otter.utils.SearchParameter;
import ps.mx.otter.utils.WebUtil;

import com.dreamer.domain.account.GoodsAccount;
import com.dreamer.domain.goods.Goods;
import com.dreamer.domain.user.Agent;
import com.dreamer.domain.user.AgentLevel;
import com.dreamer.domain.user.User;
import com.dreamer.repository.account.GoodsAccountDAO;
import com.dreamer.repository.goods.GoodsDAO;
import com.dreamer.repository.user.AgentDAO;
import com.dreamer.repository.user.AgentLevelDAO;
import com.dreamer.repository.user.MutedUserDAO;
import com.dreamer.view.goods.GoodsAccountDTO;

@Controller
@RequestMapping("/stats/market")
public class MarketQueryController {

	@RequestMapping(value = "/index.html")
	public String queryStockstatistics(
			@RequestParam(value = "agentCode", required = false) String agentCode,
			HttpServletRequest request, Model model) {
		try {
			Agent agent=null;
			if(Objects.isNull(agentCode) || agentCode.trim().isEmpty()){
				User mutedUser=(User)WebUtil.getSessionAttribute(request, Constant.MUTED_USER_KEY);
				agent=mutedUserDAO.findById(mutedUser.getId());
			}else{
				agent=agentDAO.findByAgentCode(agentCode);
			}
			Integer parentId=agent.getId();
			List<AgentLevel> levels=agentLevelDAO.findAll();
			Integer maxRow=0;
			TableDTO table=new TableDTO();
			table.setOwner(agent.getRealName());
			for(AgentLevel l:levels){
				ColumnDTO col=new ColumnDTO();
				col.setName(l.getName());
				col.setId(l.getId());
				List<Object[]> rs=agentDAO.findMarketByParentAgent(parentId, l.getLevel());
				if(rs.size()>maxRow){
					maxRow=rs.size();
				}
				for(Object[] obs:rs){
					AgentDTO agentDTO=new AgentDTO();
					agentDTO.setId((Integer)obs[0]);
					agentDTO.setAgentName((String)obs[1]);
					agentDTO.setAgentCode((String)obs[2]);
					agentDTO.setStatus((String)obs[3]);
					col.getAgents().add(agentDTO);
				}
				table.getColumns().add(col);
			}
			table.setMaxRow(maxRow);
			model.addAttribute("table", table);
		} catch (Exception exp) {
			exp.printStackTrace();
		}
		return "stats/market/market_index";
	}

	@RequestMapping(value = "/children.html")
	public String goodsBalance(
			@RequestParam(value = "goodsId", required = false) Integer goodsId,
			HttpServletRequest request, Model model) {
		try{
			Goods goods=goodsDAO.findById(goodsId);
			model.addAttribute("goods", goods);
		}catch(Exception exp){
			exp.printStackTrace();
		}		
		return "stats/stats_market_children";
	}

	@RequestMapping(value = "/market/detail.json", method = RequestMethod.GET)
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
	public SearchParameter<Agent> preprocess(
			@RequestParam("id") Optional<Integer> id) {
		SearchParameter<Agent> parameter = new SearchParameter<Agent>();
		parameter.setEntity(new Agent());
		return parameter;
	}

	@Autowired
	private GoodsDAO goodsDAO;

	@Autowired
	private GoodsAccountDAO goodsAccountDAO;
	
	@Autowired
	private AgentLevelDAO agentLevelDAO;

	@Autowired
	private AgentDAO agentDAO;
	
	@Autowired
	private MutedUserDAO mutedUserDAO;
	
	private final Logger LOG = LoggerFactory.getLogger(getClass());
}
