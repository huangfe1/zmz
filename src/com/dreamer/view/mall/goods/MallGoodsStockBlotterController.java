package com.dreamer.view.mall.goods;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ps.mx.otter.utils.WebUtil;
import ps.mx.otter.utils.message.Message;

import com.dreamer.domain.mall.goods.MallGoodsStockBlotter;
import com.dreamer.domain.user.Admin;
import com.dreamer.repository.mall.goods.MallGoodsDAO;
import com.dreamer.repository.mall.goods.MallGoodsStockBlotterDAO;
import com.dreamer.service.mall.goods.MallGoodsHandler;

@RestController
@RequestMapping("/stock/pm")
public class MallGoodsStockBlotterController {

	
	@RequestMapping(value={"/edit.json"},method=RequestMethod.POST)
	public Message edit_enter(@ModelAttribute("stockBlotter")MallGoodsStockBlotter stock,Model model,HttpServletRequest request){
		try{
			Admin user=(Admin)WebUtil.getCurrentUser(request);
			stock.setUser(user);
			goodsHandler.addStock(stock);
			return Message.createSuccessMessage("新增库存成功");
		}catch(Exception exp){
			exp.printStackTrace();
			return Message.createFailedMessage(exp.getMessage());
		}
	}
	
	
	@ModelAttribute("stockBlotter")
	public MallGoodsStockBlotter preprocess(
			@RequestParam("id") Optional<Integer> id) {
		
		MallGoodsStockBlotter stockBlotter = null;
		if (id.isPresent()) {
			stockBlotter = (MallGoodsStockBlotter) stockBlotterDAO.findById(id.get());
		} else {
			stockBlotter = new MallGoodsStockBlotter();
		}
		return stockBlotter;
	}

	@Autowired
	private MallGoodsStockBlotterDAO stockBlotterDAO;
	@Autowired
	private MallGoodsDAO goodsDAO;
	@Autowired
	private MallGoodsHandler goodsHandler;
}
