package com.dreamer.view.goods;

import com.dreamer.domain.goods.Logistics;
import com.dreamer.service.goods.LogisticsHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ps.mx.otter.utils.message.Message;

import java.util.Optional;

@RestController
@RequestMapping("/logistics")
public class LogisticsController {

	@RequestMapping(value = "/edit.json", method = RequestMethod.POST)
	public Message edit(@ModelAttribute("parameter") Logistics logistics) {
    try {
        logisticsHandler.saveOrupdate(logistics);
    return Message.createSuccessMessage();
		} catch (Exception exp) {
			exp.printStackTrace();
			LOG.error("物流规则编辑保存失败", exp);
			return Message.createFailedMessage();
		}
	}

	@RequestMapping(value = "/remove.json", method = RequestMethod.DELETE)
	public Message remove(@ModelAttribute("parameter") Logistics logistics, Model model) {
		try {
            logisticsHandler.removeLogistics(logistics);
			return Message.createSuccessMessage();
		} catch (Exception exp) {
			exp.printStackTrace();
			LOG.error("产品删除失败", exp);
			return Message.createFailedMessage();
		}
	}

	@ModelAttribute("parameter")
	public Logistics preprocess(@RequestParam("id") Optional<Integer> id) {
		Logistics logistics = new Logistics();
		if (id.isPresent()) {
            logistics = logisticsHandler.findById(id.get());
		}
		return logistics;
	}
    @Autowired
	private LogisticsHandler logisticsHandler;



	private final Logger LOG = LoggerFactory.getLogger(getClass());

}
