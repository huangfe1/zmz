package com.dreamer.view.goods;

import com.dreamer.domain.goods.Logistics;
import com.dreamer.domain.user.User;
import com.dreamer.service.goods.LogisticsHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ps.mx.otter.utils.SearchParameter;
import ps.mx.otter.utils.WebUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/logistics")
public class LogisticsQueryController {
	@RequestMapping("/index.html")
	public String index(
			@ModelAttribute("parameter") SearchParameter<Logistics> parameter,
			HttpServletRequest request, Model model) {
		try {
            List<Logistics> logisticses = logisticsHandler.searchAllByPage(parameter);
			WebUtil.turnPage(parameter, request);
			model.addAttribute("logisticses", logisticses);
		} catch (Exception exp) {
			exp.printStackTrace();
			LOG.error("产品查询失败", exp);
		}
		return "goods/logistics_index";
	}

    @RequestMapping(value = "/edit.html", method = RequestMethod.GET)
    public String edit_enter(
            @ModelAttribute("parameter") SearchParameter<Logistics> parameter,
            HttpServletRequest request, Model model) {
        User user = (User) WebUtil.getCurrentUser(request);
        if (user.isAdmin()) {
            return "goods/logistics_edit";
        } else {
            LOG.error("非管理员身份,无产品编辑权限");
            return "common/403";
        }

    }


	@ModelAttribute("parameter")
	public SearchParameter<Logistics> preprocess(@RequestParam("id") Optional<Integer> id) {
		SearchParameter<Logistics> parameter = new SearchParameter<>();
        Logistics  logistics;
		if (id.isPresent()) {
			  logistics = logisticsHandler.findById(id.get());
        }else {
            logistics=new Logistics();
        }
        parameter.setEntity(logistics);
		return parameter;
	}
    @Autowired
	private LogisticsHandler logisticsHandler;



	private final Logger LOG = LoggerFactory.getLogger(getClass());

}
