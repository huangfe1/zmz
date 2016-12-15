package com.dreamer.domain.account;

import java.util.Objects;

import com.dreamer.domain.goods.Goods;
import com.dreamer.domain.goods.Price;
import com.dreamer.domain.user.Agent;
import com.dreamer.domain.user.AgentLevel;

public interface GoodsPriceStrategy {
	
	public default Price calculatePrice(Agent agent,Goods goods){
		AgentLevel priceLevel=agent.loadAccountForGoodsNotNull(goods).getAgentLevel();
		Price price= goods.getPrice(priceLevel);
		if(Objects.isNull(price)){
			price=goods.getLowestPrice();
		}
		return price;
	}

}
