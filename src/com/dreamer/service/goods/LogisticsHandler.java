package com.dreamer.service.goods;

import com.dreamer.domain.goods.Logistics;
import com.dreamer.repository.goods.LogisticsDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ps.mx.otter.utils.SearchParameter;

import java.util.List;

@Service
public class LogisticsHandler {
	
	
	@Transactional
	public Logistics saveOrupdate(Logistics logistics){

		Logistics instance = logisticsDAO.merge(logistics);
		LOG.debug("新增物流规则成功");
		return logistics;
	}


	

	@Transactional
	public void removeLogistics(Logistics logistics) {
		logisticsDAO.delete(logistics);
		LOG.debug("删除物流规则成功");
	}

	@Autowired
	private LogisticsDAO logisticsDAO;


	private final Logger LOG = LoggerFactory.getLogger(getClass());

	public Logistics findById(Integer integer) {
		return  logisticsDAO.findById(integer);
	}

    public List<Logistics> searchAllByPage(SearchParameter<Logistics> parameter) {
        return logisticsDAO.searchAllByPage(parameter);
    }
}
