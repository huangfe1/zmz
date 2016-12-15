package com.dreamer.domain.user;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.dreamer.domain.goods.GoodsType;
import ps.mx.otter.exception.DataNotFoundException;

import com.dreamer.domain.account.GoodsAccount;
import com.dreamer.domain.authorization.AuthorizationType;
import com.dreamer.domain.goods.Goods;
import com.dreamer.domain.goods.Transfer;

public class MutedUser extends Agent {

	@Override
	public void transferPoints(Agent toAgent, Double points) {
		toAgent.getAccounts().increasePoints(points);
	}

	@Override
	public void transferGoodsToAnother(Agent user, Transfer transfer) {
		// TODO Auto-generated method stub
		transfer.getItems().forEach((k,v)->{
			GoodsAccount accTo = user.loadAccountForGoodsId(k);
			if (Objects.isNull(accTo)) {
				throw new DataNotFoundException("转入方对应货物账户不存在");
			}
			GoodsAccount accFrom = loadAccountForGoodsId(k);
			//管理员转货
			accFrom.transferGoodsToAnother(accTo, v.getQuantity(),transfer.getApplyOrigin());
		});		
	}
	
	public GoodsAccount addGoodsAccount(Goods goods){
		if (!hasGoodsAccount(goods)) {
			GoodsAccount account = buildGoodsAccount(goods);
			addGoodsAccount(account);
			return account;
		}
		return null;
	}
	
	@Override
	public boolean isBalanceNotEnough(Goods goods,Integer quantity){
		return goods.getCurrentBalance()<quantity;
	}
	
	

	@Override
	public GoodsAccount buildGoodsAccount(Goods g) {
		// TODO Auto-generated method stub
		GoodsAccount account = new GoodsAccount();
		account.setGoods(g);
		account.setCurrentBalance(g.getCurrentBalance());
		account.setCurrentPoint(g.getCurrentPoint());
		account.setCumulative(0);
		account.setAgentLevel(g.getLowestPrice().getAgentLevel());
		account.setUpdateTime(new Date());
		return account;
	}

	@Override
	public GoodsAccount loadAccountForGoodsNotNull(Goods goods) {
		// TODO Auto-generated method stub
		Optional<GoodsAccount> optional = getGoodsAccounts().stream()
				.filter((g) -> Objects.equals(g.getGoods(), goods)).findFirst();
		return optional.orElseGet(()->{
			GoodsAccount acc=this.addGoodsAccount(goods);
			return acc;
		});
	}

	@Override
	public boolean hasMainGoodsAuthorization(GoodsType gt) {
		// TODO Auto-generated method stub
		return true;
	}

	private static final long serialVersionUID = -2547232979881873516L;

	@Override
	public void addAuthorizationToAgent(Agent agent,
			List<AuthorizationType> types) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isAdmin() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isAgent() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isVisitor() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isMutedUser() {
		// TODO Auto-generated method stub
		return true;
	}

}
