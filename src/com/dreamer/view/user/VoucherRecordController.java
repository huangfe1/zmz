package com.dreamer.view.user;

import com.dreamer.domain.account.VoucherRecord;
import com.dreamer.domain.user.User;
import com.dreamer.repository.account.VoucherRecordDAO;
import com.dreamer.repository.user.MutedUserDAO;
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
@RequestMapping("/voucher")
public class VoucherRecordController {

	@RequestMapping(value = "/record.html", method = RequestMethod.GET)
	public String index(
			@ModelAttribute("parameter") SearchParameter<VoucherRecord> parameter,
			Model model, HttpServletRequest request) {
		try {
			List<VoucherRecord> pts;
			User user = (User) WebUtil.getCurrentUser(request);
			if (user.isAdmin()) {
				pts = voucherRecordDAO.searchEntityByPage(parameter);
			} else {
				pts = voucherRecordDAO.searchEntityByPage(parameter, user);
			}
			WebUtil.turnPage(parameter, request);
			model.addAttribute("pts", pts);
			model.addAttribute("from", user.getId());
		} catch (Exception exp) {
			exp.printStackTrace();
		}
		return "user/voucher_record";
	}
	
	@ModelAttribute("parameter")
	public SearchParameter<VoucherRecord> preprocess(
			@RequestParam("id") Optional<Integer> id) {
		SearchParameter<VoucherRecord> parameter = new SearchParameter<VoucherRecord>();
		if (id.isPresent()) {
			//parameter.setEntity(voucherRecordDAO.findById(id.get()));
		} else {
			parameter.setEntity(new VoucherRecord());
		}
		return parameter;
	}
	

	@Autowired
	private MutedUserDAO mutedUserDAO;
	@Autowired
	private VoucherRecordDAO voucherRecordDAO; 
}
