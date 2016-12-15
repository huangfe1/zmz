package com.dreamer.service.user;

import com.dreamer.domain.account.GoodsAccount;
import com.dreamer.domain.authorization.Authorization;
import com.dreamer.domain.goods.Goods;
import com.dreamer.domain.goods.GoodsType;
import com.dreamer.domain.user.Agent;
import org.springframework.stereotype.Service;
import ps.mx.otter.exception.ApplicationException;
import ps.mx.otter.utils.date.DateUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

@Service
public class AuthorizationLetterHandler {

	public byte[] generateLetter(Agent agent, Authorization auth, Path path) {
		try {
			Goods goods=auth.getAuthorizationType().getGoods();
			GoodsAccount gac=agent.loadAccountForGoodsNotNull(goods);
			ByteArrayInputStream in = new ByteArrayInputStream(
					Files.readAllBytes(path));
			BufferedImage image = ImageIO.read(in);
			BufferedImage tag = new BufferedImage(614, 820,
					BufferedImage.TYPE_INT_ARGB);
			Graphics2D gp = (Graphics2D) tag.getGraphics();
			gp.drawImage(image, 0, 0, null);
			//gp.setBackground(Color.WHITE);
			gp.setColor(Color.BLACK);
			gp.setFont(new Font("宋体", Font.BOLD, 18));
			gp.drawString(agent.getRealName(), 250, 457);
			gp.drawString(replacePrivacy(agent.getWeixin()), 250, 483);
			gp.drawString(replacePrivacy(agent.getMobile()), 250, 507);
			gp.drawString(replacePrivacy(agent.getIdCard()), 250, 532);
			gp.drawString(agent.getAgentCode(), 250, 557);
//			gp.drawString(goods.getName(), 235, 607);//
			String an=goods.getAuthorizationType().getName();
			if(goods.getGoodsType()== GoodsType.TEQ) {
				an = "和之初专业线项目";
//				String str="zmz727772zmz317828zmz851070zmz147846zmz812650zmz111668zmz213821";
//				if(str.contains(agent.getAgentCode())){//特殊代理进行联盟隐藏 刘加运 zmz317828 陈禹含 zmz851070 卿佩佩 zmz147846 彭波 zmz812650 王晓兰
//					gac.getAgentLevel().setName("大区");
//				}
				//所有的联盟都设置为金董事
				if(gac.getAgentLevel().getName().contains("联盟")){
					gac.getAgentLevel().setName("金董事");
				}
			}
			gp.drawString(an, 307-an.length()*9, 607);
			gp.setFont(new Font("宋体", Font.BOLD, 20));
			gp.drawString(gac.getAgentLevel().getName(), 307-gac.getAgentLevel().getName().length()*10, 640);
			gp.setFont(new Font("宋体", Font.BOLD, 18));
			gp.drawString(DateUtil.date2string(auth.getUpdateTime(),DateUtil.DATE_FORMAT), 240, 706);
			
			gp.dispose();
			ByteArrayOutputStream out=new ByteArrayOutputStream();
			ImageIO.write(tag,"png",out);
			return out.toByteArray();
		} catch (Exception exp) {
			exp.printStackTrace();
			throw new ApplicationException("生成授权证书失败");
		}
	}
	
	private String replacePrivacy(String data){
		if(Objects.isNull(data)){
			return "";
		}
		if(data.length()>4){
			return data.substring(0, data.length()-4)+PRIVACY;
		}else{
			return PRIVACY;
		}
	}

	private final static String PRIVACY="****";
}
