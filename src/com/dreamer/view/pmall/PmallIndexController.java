package com.dreamer.view.pmall;

import com.dreamer.domain.mall.goods.MallGoods;
import com.dreamer.domain.user.Agent;
import com.dreamer.domain.user.User;
import com.dreamer.repository.mall.goods.MallGoodsDAO;
import com.dreamer.repository.user.AgentDAO;
import com.dreamer.service.pay.GetOpenIdHandler;
import com.dreamer.service.pay.JsApiParameterFactory;
import com.dreamer.service.pay.PayConfig;
import com.dreamer.util.TokenInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ps.mx.otter.utils.SearchParameter;
import ps.mx.otter.utils.WebUtil;
import ps.mx.otter.utils.json.JsonUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping(value={"/pmall","/dmz/pmall"})
public class PmallIndexController {

	@RequestMapping(value = { "/index.html", "/", "/index" }, method = RequestMethod.GET)
	public String index(
			@ModelAttribute("parameter") SearchParameter<MallGoods> param,
			@RequestParam(value="ref",required=false) String ref,
			HttpServletRequest request, HttpServletResponse response,
			Model model) {
		try {
			if(WebUtil.isLogin(request)){
				User user=(User)WebUtil.getCurrentUser(request);
				Agent agent=agentDAO.findById(user.getId());
				model.addAttribute("account_points", agent.getAccounts().getPointsBalance());
                model.addAttribute("myCode", agent.getAgentCode());//加入自己的编号
			}
			if(Objects.nonNull(ref)){
				model.addAttribute("agentCode", ref);
			}
		} catch (Exception exp) {
			LOG.error("进入积分商城首页异常", exp);
			exp.printStackTrace();
		}
		return "pmall/pmall_index";
	}
	
	@RequestMapping(value="/detail.html",method=RequestMethod.GET)
	public String detail(@RequestParam("id") Integer id,HttpServletRequest request,String myCode,HttpServletResponse response,Model model){
		try{
			MallGoods g=mallGoodsDAO.findById(id);
			PointsGoodsDTO dto=new PointsGoodsDTO();
			dto.setId(g.getId());
			dto.setBenefitPoints(g.getBenefitPoints());
			dto.setMoneyPrice(g.getMoneyPrice());
			dto.setName(g.getName());
			dto.setPointPrice(g.getPointPrice());
			dto.setPrice(g.getPrice());
			dto.setSpec(g.getSpec());
			dto.setStockQuantity(g.getStockQuantity());
            dto.setShareDetail(g.getShareDetail());
            dto.setShareName(g.getShareName());
			dto.setVoucher(g.getVoucher());
//			String imgUrl = WebUtil.getContextPath(request) + "/dmz/img/pmall/"
//					+ g.getImgFile();
			String imgUrl = TokenInfo.IMG_HEAD_PATH + "/dmz/img/pmall/"
					+ g.getImgFile();
			dto.setImgUrl(imgUrl);
			List<String> list =new ArrayList<>();
			if(g.getDetailImg()!=null){
				for (String str : g.getDetailImg().split("\\+")){
//					list.add(WebUtil.getContextPath(request) + "/dmz/img/goods/"+str);
					list.add(TokenInfo.IMG_HEAD_PATH + "/dmz/img/goods/"+str);
				}
			}
			dto.setDetailImgUrls(list);//添加详情页
           String url=ServletUriComponentsBuilder.fromRequest(request).toUriString();
			HashMap jspmap=jsApiParameterFactory.buildShare(payConfig,url, TokenInfo.getJsapiTicket());
            String jsonParam= JsonUtil.mapToJsonStr(jspmap);
//            LOG.debug("JSAPI Edit address Param：{}",jsonParam);
            if(WebUtil.isLogin(request)){
                User user=(User) WebUtil.getCurrentUser(request);
                Agent agent = agentDAO.findById(user.getId());
                myCode=agent.getAgentCode();
            }
            model.addAttribute("myCode",myCode);//传获取parent的
//			System.out.println("--------hfCode"+myCode);
			model.addAttribute("jsapiParamJson",jsonParam);
            model.addAttribute("goods", dto);
		}catch(Exception exp){
			LOG.error("进入积分商城商品详情页异常",exp);
			exp.printStackTrace();
		}
		return "pmall/goods_detail";
	}




	@RequestMapping(value = "/goods/query.json", method = RequestMethod.GET)
	@ResponseBody
	public List<PointsGoodsDTO> queryGoods(
			@ModelAttribute("parameter") SearchParameter<MallGoods> param,
			HttpServletRequest request) {
		param.getEntity().setShelf(true);
		List<MallGoods> goods = mallGoodsDAO.searchEntityByPage(param, null,
				(t) -> true);
		List<PointsGoodsDTO> dtos=new ArrayList<PointsGoodsDTO>();
		goods.forEach(g->{
			PointsGoodsDTO dto=new PointsGoodsDTO();
			dto.setId(g.getId());
			dto.setBenefitPoints(g.getBenefitPoints());
			dto.setMoneyPrice(g.getMoneyPrice());
			dto.setName(g.getName());
			dto.setPointPrice(g.getPointPrice());
			dto.setPrice(g.getPrice());
			dto.setSpec(g.getSpec());
			dto.setStockQuantity(g.getStockQuantity());
			dto.setVoucher(g.getVoucher());
			dto.setVouchers(g.getVouchers());
//            String imgUrl = WebUtil.getContextPath(request) + "/dmz/img/pmall/"
//					+ g.getImgFile();
			String imgUrl = TokenInfo.IMG_HEAD_PATH + "/dmz/img/pmall/"
					+ g.getImgFile();
			dto.setImgUrl(imgUrl);
			dtos.add(dto);
		});
		return dtos;
	}
	
	@ModelAttribute("parameter")
	public SearchParameter<MallGoods> preprocessing() {
		SearchParameter<MallGoods> param = new SearchParameter<MallGoods>();
		MallGoods goods = new MallGoods();
		param.setEntity(goods);
		return param;
	}

    @Autowired
    private JsApiParameterFactory jsApiParameterFactory;

	@Autowired
	private MallGoodsDAO mallGoodsDAO;

    @Autowired
    private PayConfig payConfig;

    @Autowired
    private GetOpenIdHandler getOpenIdHandler;
	@Autowired
	private AgentDAO agentDAO;

	private final Logger LOG = LoggerFactory.getLogger(getClass());
}
