package com.dreamer.view.goods;

import com.dreamer.domain.goods.Goods;
import com.dreamer.repository.goods.GoodsDAO;
import com.dreamer.service.goods.GoodsHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import ps.mx.otter.utils.message.Message;

import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/goods")
public class GoodsController {


	@RequestMapping(value = "/edit.json", method = RequestMethod.POST)
	public Message edit(@ModelAttribute("parameter") Goods goods,MultipartHttpServletRequest request,
						Double[] levelPrice, Integer[] levelThreshold,
						Integer[] levelId,Integer[]priceId, Model model) {
		try {
			if(goods.getAuthorizationType()!=null){
				goods.getAuthorizationType().setGoods(goods);
			}
			MultipartFile file=request.getFile("img");
			if(goods.getId()!=null){
				if(Objects.isNull(file) ||file.isEmpty()){
					goodsHandler.updateGoods(goods,levelPrice,levelThreshold,levelId,priceId,null,null);
				}else{
					String imgType = file.getOriginalFilename().substring(file.getOriginalFilename().indexOf("."));
					goodsHandler.updateGoods(goods,levelPrice,levelThreshold,levelId,priceId,imgType,file.getBytes());
				}
			}else{
				if(Objects.isNull(file) || file.isEmpty()){
					goodsHandler.addGoods(goods,levelPrice,levelThreshold,levelId,priceId,null,null);
				}else{
					String imgType = file.getOriginalFilename().substring(file.getOriginalFilename().indexOf("."));
					goodsHandler.addGoods(goods,levelPrice,levelThreshold,levelId,priceId,imgType,file.getBytes());
				}
			}
			return Message.createSuccessMessage();
		} catch (Exception exp) {
			exp.printStackTrace();
			LOG.error("产品编辑保存失败", exp);
			return Message.createFailedMessage();
		}
	}

	@RequestMapping(value = "/remove.json", method = RequestMethod.DELETE)
	public Message remove(@ModelAttribute("parameter") Goods goods, Model model) {
		try {
			goodsHandler.removeGoods(goods);
			return Message.createSuccessMessage();
		} catch (Exception exp) {
			exp.printStackTrace();
			LOG.error("产品删除失败", exp);
			return Message.createFailedMessage();
		}
	}

	@ModelAttribute("parameter")
	public Goods preprocess(@RequestParam("id") Optional<Integer> id) {
		Goods goods = new Goods();
		if (id.isPresent()) {
			goods = goodsDAO.findById(id.get());
		}
		return goods;
	}

	@Autowired
	private GoodsDAO goodsDAO;
	@Autowired
	private GoodsHandler goodsHandler;

	private final Logger LOG = LoggerFactory.getLogger(getClass());

}
