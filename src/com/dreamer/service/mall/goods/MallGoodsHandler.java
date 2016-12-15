package com.dreamer.service.mall.goods;

import com.dreamer.domain.mall.goods.MallGoods;
import com.dreamer.domain.mall.goods.MallGoodsStockBlotter;
import com.dreamer.repository.mall.goods.MallGoodsDAO;
import com.dreamer.repository.mall.goods.MallGoodsStockBlotterDAO;
import com.dreamer.repository.pmall.order.OrderItemDao;
import com.dreamer.repository.system.SystemParameterDAOInf;
import com.dreamer.repository.user.MutedUserDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ps.mx.otter.exception.ApplicationException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.UUID;

@Service
public class MallGoodsHandler {

    @Transactional
    public void addMallGoods(MallGoods mallGoods, String imgType, byte[] imgBytes) {
        LOG.debug("新增积分商城商品 name:{}", mallGoods.getName());
        if (Objects.nonNull(imgType)) {
            String fileName = generateFileName(imgType);
            writeImgFile(imgBytes, fileName);
            mallGoods.setImgFile(fileName);
        }
        if (mallGoods.getActivity() == 1) {//活动
            if (Objects.isNull(mallGoods.getStartTime())) {//
                //设置当前时间为活动时间
                mallGoods.setStartTime(new Timestamp(System.currentTimeMillis()));
            }
        } else {//下架
            if (Objects.nonNull(mallGoods.getStartTime())) {
                mallGoods.setStartTime(null);
            }
        }
        if(Objects.isNull(mallGoods.getVersion())) {
            mallGoodsDAO.save(mallGoods);
        }else {
            mallGoodsDAO.merge(mallGoods);
        }
    }

    @Transactional
    public void modifyMallGoods(MallGoods mallGoods, String imgType, byte[] imgBytes) {
        LOG.debug("修改积分商城商品 id:{}", mallGoods.getId());
        if (Objects.nonNull(imgType)) {
            String fileName = mallGoods.getImgFile();
            if (Objects.isNull(fileName) || fileName.isEmpty()) {
                fileName = generateFileName(imgType);
                mallGoods.setImgFile(fileName);
            }
            writeImgFile(imgBytes, fileName);
        }
        if (mallGoods.getActivity() == 1) {//活动
            if (Objects.isNull(mallGoods.getStartTime())) {//
                //设置当前时间为活动时间
                mallGoods.setStartTime(new Timestamp(System.currentTimeMillis()));
            }
        } else {//下架
            if (Objects.nonNull(mallGoods.getStartTime())) {
                mallGoods.setStartTime(null);
            }
        }
        mallGoodsDAO.merge(mallGoods);
    }

    /**
     * 购物车的产品已经大于或者等于限制值了
     *
     * @param uid
     * @param goods
     * @return
     */
    public boolean isActivityLitmit(Integer uid, MallGoods goods, Integer quantity) {
        if (goods.getActivity() == 0) {//非活动期间
            return false;
        }
        return (orderItemDao.getOrdersDateBefore(uid, goods).size() + quantity) > goods.getLimitNumer();
    }

    @Transactional
    public void removeMallGoods(MallGoods mallGoods) {
        LOG.debug("删除积分商城商品 id:{}", mallGoods.getId());
        mallGoodsDAO.delete(mallGoods);
    }

    @Transactional
    public void addStock(MallGoodsStockBlotter stock) {
        MallGoods goods = mallGoodsDAO.findById(stock.getGoods().getId());
        goods.addStockBlotter(stock);
        mallGoodsStockBlotterDAO.save(stock);
        LOG.debug("新增库存成功");
    }

    private String generateFileName(String imgType) {
        String s = UUID.randomUUID().toString();
        StringBuilder sbd = new StringBuilder();
        sbd.append(s.substring(0, 8)).append(s.substring(9, 13)).append(s.substring(14, 18)).append(s.substring(19, 23)).append(s.substring(24)).append(imgType);
        return sbd.toString();
    }


    private void writeImgFile(byte[] imgBytes, String fileName) {
        try {
            Path path = Paths.get(systemParameterDAO.getMallGoodsImgPath());
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
            Path imgFile = path.resolve(fileName);
            if (!Files.exists(imgFile)) {
                Files.createFile(imgFile);
            }
            Files.write(imgFile, imgBytes, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException iop) {
            LOG.error("写图片文件失败", iop);
            throw new ApplicationException(iop);
        }
    }

    @Autowired
    private OrderItemDao orderItemDao;
    @Autowired
    private MallGoodsDAO mallGoodsDAO;
    @Autowired
    private SystemParameterDAOInf systemParameterDAO;
    @Autowired
    private MutedUserDAO mutedUserDAO;
    @Autowired
    private MallGoodsStockBlotterDAO mallGoodsStockBlotterDAO;

    private final Logger LOG = LoggerFactory.getLogger(getClass());
}
