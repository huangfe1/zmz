package com.dreamer.view.mall.goods;

import com.dreamer.domain.goods.Goods;
import com.dreamer.domain.mall.shopcart.CartItem;
import com.dreamer.domain.mall.shopcart.ShopCart;
import com.dreamer.repository.goods.GoodsDAO;
import com.dreamer.repository.user.AgentDAO;
import com.dreamer.util.TokenInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ps.mx.otter.utils.WebUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping("/dmz/tmall")
public class TmallGoodsDetailQueryController {
	private static final String CART = "tshopcart";

	@RequestMapping(value="/detail.html",method=RequestMethod.GET)
	public String goodsDetail(@ModelAttribute("goods")Goods goods,HttpServletRequest request,Model model){
		GoodsDTO dto = new GoodsDTO();
		dto.setId(goods.getId());
		dto.setName(goods.getName());
		dto.setSpec(goods.getSpec());
		try {
			if(WebUtil.isLogin(request)){
				//Agent user=(Agent)WebUtil.getCurrentUser(request);
				//Agent agent=agentDAO.findById(user.getId());
				//dto.setPrice(agent.caculatePrice(goods).getPrice());
				dto.setPrice(goods.getRetailPrice());
			}else{
				dto.setPrice(goods.getRetailPrice());
			}
		} catch (Exception exp) {
			dto.setPrice(goods.getRetailPrice());
		}
//		String imgUrl = WebUtil.getContextPath(request) + "/dmz/img/goods/"
//				+ goods.getImgFile();
		String imgUrl = TokenInfo.IMG_HEAD_PATH + "/dmz/img/goods/"
				+ goods.getImgFile();
		dto.setImgUrl(imgUrl);
		List<String> list =new ArrayList<>();
		if(goods.getDetailImg()!=null){
			for (String str : goods.getDetailImg().split("\\+")){
				list.add(TokenInfo.IMG_HEAD_PATH + "/dmz/img/goods/"+str);
			}
		}
		dto.setDetailImgUrls(list);//添加详情页
		Object ob = WebUtil.getSessionAttribute(request, CART);
		Integer quantity=1;
		if (Objects.nonNull(ob)) {
			ShopCart cart = (ShopCart) ob;
			CartItem item=cart.getItem(goods.getId());
			if(Objects.nonNull(item)){
				quantity=item.getQuantity();
			}
		}
		model.addAttribute("goods", dto);
		model.addAttribute("quantity", quantity);
		return "tmall/goods_detail";
	}

	@ModelAttribute("goods")
	public Goods preprocess(@RequestParam("id") Optional<Integer> id,Model model){
		if(id.isPresent()){
			return goodsDAO.findById(id.get());
		}else{
			return new Goods();
		}
	}

	@Autowired
	private GoodsDAO goodsDAO;
	@Autowired
	private AgentDAO agentDAO;
}
