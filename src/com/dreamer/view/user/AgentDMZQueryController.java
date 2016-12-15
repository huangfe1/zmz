package com.dreamer.view.user;

import com.dreamer.domain.user.Agent;
import com.dreamer.repository.authorization.AuthorizationTypeDAO;
import com.dreamer.repository.user.AgentDAO;
import com.dreamer.repository.user.AgentLevelDAO;
import com.dreamer.service.user.AgentDomainGenerateStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;
import ps.mx.otter.utils.SearchParameter;
import ps.mx.otter.utils.img.QRCodeGenerater;
import ps.mx.otter.utils.img.QRCodeGenerater.CODE_TYPE;
import ps.mx.otter.utils.img.QRCodeGenerater.FORMAT;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/dmz/agent")
public class AgentDMZQueryController {

	private static final QRCodeGenerater QRCODE = QRCodeGenerater.getInstance();

	@Autowired
	private AgentDomainGenerateStrategy agentDomainGenerateStrategy;

	@RequestMapping(value = { "/index.html", "/search.html" }, method = RequestMethod.GET)
	public String index(
			@ModelAttribute("parameter") SearchParameter<Agent> parameter,
			@RequestParam(value = "f", required = false) String f, Model model,
			HttpServletRequest request) {
		try {
			Agent agent = agentDAO.findByDmz(parameter.getEntity()
					.getAgentCode());
			/*
			 * List<Agent> agents = agentDAO.searchEntityByPage(parameter, null,
			 * ( t) -> true); WebUtil.turnPage(parameter, request);
			 */
            List auths;
			if(Objects.nonNull(agent)){
				if(agent.hasMainGoodsAuthorization(null)){
                         auths= agent.getAuthorizations().stream().filter(au->{
						return au.getAuthorizedGoods().isMainGoods()&&au.isActive();
					}).collect(Collectors.toList());
                }else{
					auths=agent.getAuthorizations().stream().collect(Collectors.toList());
				}
				model.addAttribute("auths", auths);
			}
			
			model.addAttribute("f", f);
			model.addAttribute("agent", agent);
		} catch (Exception exp) {
			exp.printStackTrace();
			LOG.error("代理查询失败", exp);
		}
		return "user/dmz/agent_search";
	}



	@RequestMapping(value = "/domain.html", method = RequestMethod.GET)
	public void qrCodeForDomain(
			@ModelAttribute("parameter") SearchParameter<Agent> parameter,
			HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("image/jpeg");
		try {
            UriComponents uri=ServletUriComponentsBuilder.fromContextPath(request).path("/vmall/uc/dmz/hfregister.html?agentCode="+parameter.getEntity().getAgentCode()).build();
            byte[] bts = QRCODE.generateAsBytes(uri.toString(), 200, 200, 0,
					CODE_TYPE.QR, FORMAT.PNG);
			
			ByteArrayInputStream in = new ByteArrayInputStream(bts);
			BufferedImage image = ImageIO.read(in);
			BufferedImage tag = new BufferedImage(200, 245, BufferedImage.TYPE_INT_ARGB);
			Graphics2D gp=(Graphics2D)tag.getGraphics();			
			gp.drawImage(image, 0, 0, null);
			gp.dispose();
			//背景缓冲图
			BufferedImage bg = new BufferedImage(600, 800, BufferedImage.TYPE_INT_ARGB);
			//背景图
			ImageIcon ic=new ImageIcon(request.getSession().getServletContext().getRealPath("/assets")+File.separator+"images"+File.separator+"zs.jpg");
			
			Graphics2D gbg=(Graphics2D) bg.getGraphics();
			gbg.drawImage(ic.getImage(),0,0,null);
			gbg.drawImage(tag, 320, 330, null);
			
			gbg.setBackground(Color.WHITE);
			gbg.setColor(Color.ORANGE);
			gbg.setFont(new Font("Microsoft Yahei", Font.BOLD, 50));
			gbg.setColor(Color.YELLOW);
			gbg.drawString(parameter.getEntity().getRealName(), 290-parameter.getEntity().getRealName().length()*50, 145);
			gbg.setFont(new Font("小宋", Font.BOLD, 20));
			gbg.drawString(parameter.getEntity().getRealName(), 150, 522);
			gbg.drawString(parameter.getEntity().getAgentCode(), 150, 558);
			ServletOutputStream out = response.getOutputStream();
			//out.write(bts);
			ImageIO.write(bg,"png",out);
			out.flush();
			out.close();
		} catch (Exception exp) {
			exp.printStackTrace();
			LOG.error("生成商域二维码失败", exp);
		}
	}
	
	public static void main(String[] args){
		try {
			byte[] bts = QRCODE.generateAsBytes("http://m.jd.com/", 200, 200, 0,
					CODE_TYPE.QR, FORMAT.PNG);
			ByteArrayInputStream in = new ByteArrayInputStream(bts);
			BufferedImage image = ImageIO.read(in);
			BufferedImage tag = new BufferedImage(200, 245, BufferedImage.TYPE_INT_ARGB);
			Graphics2D gp=(Graphics2D)tag.getGraphics();			
			gp.drawImage(image, 0, 0, null);
			gp.setBackground(Color.WHITE);
			gp.setColor(Color.ORANGE);
			gp.setFont(new Font("Microsoft Yahei", Font.PLAIN, 14));
			gp.drawString("代理编号: zmz000002", 34, 215);
			gp.drawString("代理姓名: 张三丰", 34, 235);
			gp.dispose();
			ImageIO.write(tag,"png",new File("D:/qr.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/exists.json", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Boolean> queryByAgentCode(@RequestParam("agentCode") String agentCode,
			HttpServletRequest request, HttpServletResponse response,
			Model model) {
		Agent agent = agentDAO.findByAgentCode(agentCode.trim());
		if (Objects.nonNull(agent)) {
			return new ResponseEntity<Boolean>(true,HttpStatus.OK);
		} else {
			return new ResponseEntity<Boolean>(false,HttpStatus.OK);
		}
	}

	@ModelAttribute("parameter")
	public SearchParameter<Agent> preprocess(
			@RequestParam("id") Optional<Integer> id) {
		Agent agent = null;
		if (id.isPresent()) {
			agent = (Agent) agentDAO.findById(id.get());
		} else {
			agent = new Agent();
		}
		return new SearchParameter<Agent>(agent);
	}

	@Autowired
	private AgentLevelDAO agentLevelDAO;
	@Autowired
	private AgentDAO agentDAO;
	@Autowired
	private AuthorizationTypeDAO authorizationTypeDAO;
	

	private final Logger LOG = LoggerFactory.getLogger(getClass());

}
