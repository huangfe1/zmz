package com.dreamer.service.goods;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Objects;
import java.util.UUID;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartResolver;

import ps.mx.otter.exception.ApplicationException;

import com.dreamer.domain.account.GoodsAccount;
import com.dreamer.domain.goods.Goods;
import com.dreamer.domain.goods.Price;
import com.dreamer.domain.goods.StockBlotter;
import com.dreamer.domain.user.AgentLevel;
import com.dreamer.domain.user.MutedUser;
import com.dreamer.repository.goods.GoodsDAO;
import com.dreamer.repository.goods.PriceDAO;
import com.dreamer.repository.goods.StockBlotterDAO;
import com.dreamer.repository.system.SystemParameterDAOInf;
import com.dreamer.repository.user.AgentLevelDAO;
import com.dreamer.repository.user.MutedUserDAO;

@Service
public class GoodsHandler {
	
	
	@Transactional
	public Goods addGoods(Goods goods, Double[] levelPrice,
			Integer[] levelThreshold, Integer[] levelId, Integer[] priceId,String imgType,byte[] imgBytes) {
		assemblePrice(goods, levelPrice, levelThreshold, levelId, priceId);
		if(Objects.nonNull(imgType)){
			String fileName=generateFileName(imgType);
			writeImgFile(imgBytes, fileName);
			goods.setImgFile(fileName);
		}
		Goods instance = goodsDAO.merge(goods);
		LOG.debug("新增产品成功");
		return instance;
	}

	@Transactional
	public void addStock(StockBlotter stock) {
		Goods goods = goodsDAO.findById(stock.getGoods().getId());
		MutedUser companyUser=mutedUserDAO.loadFirstOne();
		GoodsAccount companyAccount=companyUser.loadAccountForGoodsNotNull(goods);
		companyAccount.increaseBalance(stock.getChange());
		goods.addStockBlotter(stock);
		stockBlotterDAO.save(stock);
		LOG.debug("新增库存成功");
	}

	@Transactional
	public Goods updateGoods(Goods goods, Double[] levelPrice,
			Integer[] levelThreshold, Integer[] levelId, Integer[] priceId,String imgType,byte[] imgBytes) {
		// goods.clearPrices();
		assemblePrice(goods, levelPrice, levelThreshold, levelId, priceId);
		if(Objects.nonNull(imgType)){
			String fileName=goods.getImgFile();
			if(Objects.isNull(fileName)||fileName.isEmpty()){
				fileName=generateFileName(imgType);
				goods.setImgFile(fileName);
			}
			writeImgFile(imgBytes, fileName);
		}
		Goods instance = goodsDAO.merge(goods);
		LOG.debug("修改产品成功");
		return instance;
	}

	
	private void assemblePrice(Goods goods, Double[] levelPrice,
			Integer[] levelThreshold, Integer[] levelId, Integer[] priceId) {
		if (levelId != null && levelId.length > 0) {
			for (int index = 0; index < levelId.length; index++) {
				Price price = null;
				if (priceId[index] != null) {
					price = priceDAO.findById(priceId[index]);
				} else {
					price = new Price();
					AgentLevel agentLevel = agentLevelDAO
							.findById(levelId[index]);
					price.setAgentLevel(agentLevel);
				}
				price.setPrice(levelPrice[index]);
				price.setThreshold(levelThreshold[index]);
				goods.addPrice(price);
			}
		}
	}
	
	private String generateFileName(String imgType){
		String s=UUID.randomUUID().toString();
		StringBuilder sbd=new StringBuilder();
		sbd.append(s.substring(0,8)).append(s.substring(9,13)).append(s.substring(14,18)).append(s.substring(19,23)).append(s.substring(24)).append(imgType);
		return sbd.toString();
	}
	

	private void writeImgFile(byte[] imgBytes,String fileName) {
		try {
			Path path = Paths.get(systemParameterDAO.getGoodsImgPath());
			if (!Files.exists(path)) {
				Files.createDirectories(path);
			}
			Path imgFile=path.resolve(fileName);
			if(!Files.exists(imgFile)){
				Files.createFile(imgFile);
			}
			Files.write(imgFile, imgBytes, StandardOpenOption.TRUNCATE_EXISTING);
		} catch (IOException iop) {
			LOG.error("写图片文件失败",iop);
			throw new ApplicationException(iop);
		}
	}
	
	

	@Transactional
	public void removeGoods(Goods goods) {
		goodsDAO.delete(goods);
		LOG.debug("删除产品成功");
	}

	@Autowired
	private GoodsDAO goodsDAO;
	@Autowired
	private AgentLevelDAO agentLevelDAO;
	@Autowired
	private PriceDAO priceDAO;
	@Autowired
	private StockBlotterDAO stockBlotterDAO;
	@Autowired
	private SystemParameterDAOInf systemParameterDAO;
	@Autowired
	private MutedUserDAO mutedUserDAO;

	private final Logger LOG = LoggerFactory.getLogger(getClass());

}
