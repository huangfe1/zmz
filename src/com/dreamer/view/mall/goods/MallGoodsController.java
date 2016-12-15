package com.dreamer.view.mall.goods;

import com.dreamer.domain.mall.goods.MallGoods;
import com.dreamer.domain.user.User;
import com.dreamer.repository.mall.goods.MallGoodsDAO;
import com.dreamer.service.mall.goods.MallGoodsHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ps.mx.otter.exception.NotAuthorizationException;
import ps.mx.otter.utils.WebUtil;
import ps.mx.otter.utils.message.Message;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/mall/goods")
public class MallGoodsController {

	

	@RequestMapping(value = "/edit.json", method = {RequestMethod.POST,RequestMethod.PUT})
	public Message edit(
			@ModelAttribute("parameter") MallGoods parameter,@RequestParam(value="img",required=false) MultipartFile file,
			HttpServletRequest request, Model model) {

		try{
			User user = (User) WebUtil.getCurrentUser(request);
			if (!user.isAdmin()) {
				LOG.error("非管理员身份,无积分商城商品编辑权限");
				throw new NotAuthorizationException(); 
			} 
				if(Objects.isNull(file)||file.isEmpty()){
					mallGoodsHandler.addMallGoods(parameter,null,null);
				}else{
					String imgType =getImgType( file.getOriginalFilename());
					mallGoodsHandler.addMallGoods(parameter,imgType,file.getBytes());
				}
//				if(Objects.isNull(file)||file.isEmpty()){
//					mallGoodsHandler.modifyMallGoods(parameter,null,null);
//				}else{
//					String imgType =getImgType(file.getOriginalFilename());
//					mallGoodsHandler.modifyMallGoods(parameter,imgType,file.getBytes());
//				}
			return Message.createSuccessMessage();
		}catch(Exception exp){
			exp.printStackTrace();
			LOG.error("积分商城商品维护失败",exp);
			return Message.createFailedMessage(exp.getMessage());
		}
	}
	
	private String getImgType(String name){
		return name.substring(name.indexOf("."));
	}
	
	@RequestMapping(value = "/remove.json", method = {RequestMethod.DELETE})
	public Message delete(
			@ModelAttribute("parameter") MallGoods parameter,
			HttpServletRequest request, Model model) {		
		try{
			User user = (User) WebUtil.getCurrentUser(request);
			if (!user.isAdmin()) {
				LOG.error("非管理员身份,无积分商城商品编辑权限");
				throw new NotAuthorizationException(); 
			} 
			mallGoodsHandler.removeMallGoods(parameter);
			return Message.createSuccessMessage();
		}catch(Exception exp){
			exp.printStackTrace();
			LOG.error("积分商城商品删除失败",exp);
			return Message.createFailedMessage(exp.getMessage());
		}
	}
	


	@ModelAttribute("parameter")
	public MallGoods preprocess(
			@RequestParam("id") Optional<Integer> id) {
		MallGoods goods = null;
		if (id.isPresent()) {
			goods = (MallGoods) mallGoodsDAO.findById(id.get());
		} else {
			goods = new MallGoods();
		}
		return goods;
	}

	@Autowired
	private MallGoodsDAO mallGoodsDAO;
	@Autowired
	private MallGoodsHandler mallGoodsHandler;

	private final Logger LOG = LoggerFactory.getLogger(getClass());

}
