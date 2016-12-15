package com.dreamer.view.goods;

import com.dreamer.repository.system.SystemParameterDAOInf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ps.mx.otter.utils.WebUtil;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ConcurrentHashMap;

@Controller
@RequestMapping("/dmz/img")
public class GoodsImageQueryController {
    private static ConcurrentHashMap<String, byte[]> goodsImgFiles = new ConcurrentHashMap<String, byte[]>();
    private static ConcurrentHashMap<String, byte[]> pointsImgFiles = new ConcurrentHashMap<String, byte[]>();
    private static ConcurrentHashMap<String, byte[]> defImgFiles = new ConcurrentHashMap<String, byte[]>();
    @Value("${img.goods.dir}")
    private String goodsImgDir;
    @Value("${img.mallgoods.dir}")
    private String pointsGoodsImgDir;

    @RequestMapping(value = "/goods/{code}.{suffix}", method = RequestMethod.GET)
    public void goodsImg(@PathVariable("code") String code, @PathVariable("suffix") String suffix,
                         HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("image/jpeg");
        ServletOutputStream out = null;
        try {
            /*Path path=Paths.get(goodsImgDir,code+"."+suffix);
			if(!Files.exists(path)){
				String defaultImgDir=WebUtil.getRealPath(request, "/assets/images");
				LOG.debug("/assets/images 绝对路径：{}",defaultImgDir);
				path=Paths.get(defaultImgDir,"default.jpg");
			}
			byte[] bts=Files.readAllBytes(path);*/
            Path imgFile = this.getImgFile(getGoodsImgDir(), code + "." + suffix, request);
            byte[] bts = null;
            if (goodsImgFiles.containsKey(imgFile.getFileName().toString())) {
                bts = goodsImgFiles.get(imgFile.getFileName().toString());
            } else {
                bts = Files.readAllBytes(imgFile);
                if(bts==null)return;
                goodsImgFiles.put(imgFile.getFileName().toString(), bts);
            }
            System.out.println("goodsImgFiles"+goodsImgFiles.size());
            out = response.getOutputStream();
            out.write(bts);
            out.flush();
        } catch (Exception exp) {
            exp.printStackTrace();
            LOG.error("获取产品图片失败", exp);
        } finally {
            try {
                if(out!=null) {
                    out.close();
                    out = null;
                }
            } catch (Exception e) {
                LOG.error("关闭流异常" + e);
            }
        }
    }

    @RequestMapping(value = "/pmall/{code}.{suffix}", method = RequestMethod.GET)
    public void pointsGoodsImg(@PathVariable("code") String code, @PathVariable("suffix") String suffix,
                               HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("image/jpeg");
        ServletOutputStream out = null;
        try {
            Path imgFile = this.getImgFile(getPointsGoodsImgDir(), code + "." + suffix, request);
            byte[] bts = null;
            if (pointsImgFiles.containsKey(imgFile.getFileName().toString())) {
                bts = pointsImgFiles.get(imgFile.getFileName().toString());
            } else {
                bts = Files.readAllBytes(imgFile);
                if(bts==null)return;
                pointsImgFiles.put(imgFile.getFileName().toString(), bts);
            }
            System.out.println("pointsImgFiles"+pointsImgFiles.size());
            out = response.getOutputStream();
            out.write(bts);
            out.flush();
        } catch (Exception exp) {
            exp.printStackTrace();
            LOG.error("获取积分产品图片失败", exp);
        } finally {
            try {
                if(out!=null) {
                    out.close();
                    out = null;
                }
            } catch (Exception e) {
                LOG.error("关闭流异常" + e);
            }
        }
    }

    @RequestMapping(value = {"/goods/null", "/pmall/null", "/goods/", "/pmall/"}, method = RequestMethod.GET)
    public void goodsNullImg(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("image/jpeg");
        ServletOutputStream out = null;
        try {
            Path imgFile = Paths.get(getGoodsImgDir(), "default.jpg");
            byte[] bts =null;
            if (defImgFiles.containsKey(imgFile.getFileName().toString())) {
                bts = defImgFiles.get(imgFile.getFileName().toString());
            } else {
                bts = Files.readAllBytes(imgFile);
                if(bts==null)return;
                defImgFiles.put(imgFile.getFileName().toString(), bts);
            }
            out = response.getOutputStream();
            out.write(bts);
            out.flush();
        } catch (Exception exp) {
            exp.printStackTrace();
            LOG.error("获取产品图片失败", exp);
        } finally {
            try {
                if(out!=null) {
                    out.close();
                    out = null;
                }
            } catch (Exception e) {
                LOG.error("关闭流异常" + e);
            }
        }
    }

    private Path getImgFile(String imgDir, String fileName, HttpServletRequest request) {
        Path path = Paths.get(imgDir, fileName);
        if (!Files.exists(path)) {
            String defaultImgDir = WebUtil.getRealPath(request, "/assets/images");
            LOG.debug("/assets/images 绝对路径：{}", defaultImgDir);
            path = Paths.get(defaultImgDir, "default.jpg");
        }
        return path;
    }


    public String getGoodsImgDir() {
        return systemParameterDAO.getGoodsImgPath();
    }

    public void setGoodsImgDir(String goodsImgDir) {
        this.goodsImgDir = goodsImgDir;
    }

    public String getPointsGoodsImgDir() {
        return systemParameterDAO.getMallGoodsImgPath();
    }

    public void setPointsGoodsImgDir(String pointsGoodsImgDir) {
        this.pointsGoodsImgDir = pointsGoodsImgDir;
    }

    @Autowired
    private SystemParameterDAOInf systemParameterDAO;
    private final Logger LOG = LoggerFactory.getLogger(getClass());
}
