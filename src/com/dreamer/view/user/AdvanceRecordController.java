package com.dreamer.view.user;

import com.dreamer.domain.account.AdvanceRecord;
import com.dreamer.domain.user.User;
import com.dreamer.repository.account.AdvanceRecordDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ps.mx.otter.utils.SearchParameter;
import ps.mx.otter.utils.WebUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/advance")
public class AdvanceRecordController {

	@RequestMapping(value = "/record.html", method = RequestMethod.GET)
	public String index(
			@ModelAttribute("parameter") SearchParameter<AdvanceRecord> parameter,
			Model model, HttpServletRequest request) {
		try {
			List<AdvanceRecord> pts;
			User user = (User) WebUtil.getCurrentUser(request);
			if (user.isAdmin()) {
				pts = advanceRecordDao.searchEntityByPage(parameter,null);
			} else {
				pts = advanceRecordDao.searchEntityByPage(parameter, user);
			}
			WebUtil.turnPage(parameter, request);
			model.addAttribute("pts", pts);
			model.addAttribute("from", user.getId());
		} catch (Exception exp) {
			exp.printStackTrace();
		}
		return "user/advance_record";
	}
	
	@ModelAttribute("parameter")
	public SearchParameter<AdvanceRecord> preprocess(
			@RequestParam("id") Optional<Integer> id) {
		SearchParameter<AdvanceRecord> parameter = new SearchParameter<AdvanceRecord>();
		if (id.isPresent()) {
			//parameter.setEntity(voucherRecordDAO.findById(id.get()));
		} else {
			parameter.setEntity(new AdvanceRecord());
		}
		return parameter;
	}
	


	@Autowired
	private AdvanceRecordDao advanceRecordDao;
}
