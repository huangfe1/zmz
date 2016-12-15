package com.dreamer.domain.account;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Objects;


import com.sun.javafx.tools.packager.Log;
import org.junit.Test;
import ps.mx.otter.exception.ApplicationException;

import com.dreamer.domain.goods.Goods;
import com.dreamer.domain.goods.GoodsType;
import com.dreamer.domain.goods.Price;
import com.dreamer.domain.goods.TransferApplyOrigin;
import com.dreamer.domain.user.Agent;
import com.dreamer.domain.user.AgentLevel;
import com.dreamer.domain.user.AgentLevelName;
import com.dreamer.domain.user.User;
import com.dreamer.repository.user.AgentLevelDAO;


public class GoodsAccount implements java.io.Serializable {

    private static final long serialVersionUID = -1216038804344510293L;
    private Integer id;
    private User user;
    private Goods goods;
    private Double currentPoint;
    private Integer currentBalance;
    private Date updateTime;
    private Integer version;
    //累积购买量
    private Integer cumulative;
    //代理针对货物所处的级别
    private AgentLevel agentLevel;
    //交易记录

    public boolean isMainGoodsAccount() {

        return goods.isMainGoods();
    }

    public Price caculatePrice() {
        return goods.getPrice(agentLevel);
    }

    /**
     * 等级比我低
     *
     * @param GoodsAccount 参与比较的账户,传入的账户等级比我低时返回 true
     * @return
     */
    public boolean lowerThanMe(GoodsAccount gac) {
        return agentLevel.lowerThanMe(gac.getAgentLevel());
    }

    /**
     * 等级比我高
     *
     * @param GoodsAccount 参与比较的账户,传入的账户等级比高低时返回 true
     * @return
     */
    public boolean higherThanMe(GoodsAccount gac) {
        return agentLevel.higherThanMe(gac.getAgentLevel());
    }

    /**
     * 相同等级
     *
     * @param GoodsAccount
     * @return
     */
    public boolean myEqual(GoodsAccount gac) {
        return agentLevel.myEqual(gac.getAgentLevel());
    }

    public boolean currentBalanceNotEnough(Integer quantity) {
        return currentBalance < quantity;
    }

    /**
     * 增加
     *
     * @param another
     * @param amount
     */
    public void transferGoodsToAnother(GoodsAccount another, Integer amount, TransferApplyOrigin applyOrigin) {
        deductBalance(amount);
        /*if(!user.isMutedUser()){
            Double points=goods.caculatePoints(amount);
			deductPoints(points);
			user.deductPoints(points);
		}*/
//		String more= user.getRealName()+"购买"+goods.getName();//转账备注

        //官方返利接收
        if (applyOrigin == TransferApplyOrigin.GMALL) {
            //another.hf_accept(amount);
            //官方返利不需要了
        } else if (applyOrigin == TransferApplyOrigin.TMALL) {//特权商城返利接收
//            another.teq_accept(amount);  这里需要修改
            another.teq_accept(amount);
        }else if (applyOrigin == TransferApplyOrigin.OUT){//主动转出
            another.accept(amount);//不用返利
        }
        else if (applyOrigin==TransferApplyOrigin.MALL){//代理商城返利接受
            another.hf_accept(amount);//不管是不是官方都按照这个方式三级返利润
        }

        //another.accept(amount);
        if (user.isMutedUser()) {
            goods.deductCurrentBalance(amount);
        }
    }


    public void transferGoodsToAnother(GoodsAccount another, Integer amount) {
        deductBalance(amount);
		/*if(!user.isMutedUser()){
			Double points=goods.caculatePoints(amount);
			deductPoints(points);
			user.deductPoints(points);
		}*/

        //官方返利接收
        another.hf_accept(amount);
        //another.accept(amount);
        if (user.isMutedUser()) {
            goods.deductCurrentBalance(amount);
        }
    }

    public void transferGoodsToBack(GoodsAccount me,GoodsAccount another, Integer amount) {
        deductBalance(amount);
        deductCumulative(amount);//减少累计
        if (!user.isMutedUser()) {
            Double points = goods.caculatePoints(amount);
            //deductPoints(points);
            //user.deductPoints(points);
        }
        another.acceptBack(amount,me);
        //然后这个人的的上级要扣减

    }

    /**
     * 主打产品账户价格等级比本产品账户价格等级高
     *
     * @param mainGoodsAgentLevel
     * @return
     */
    public boolean mainGoodsAgentLevelHigherThanMe(AgentLevel mainGoodsAgentLevel) {
        return agentLevel.higherThanMe(mainGoodsAgentLevel);
    }

    public void hf_accept(Integer amount) {
        String more = user.getRealName() + "进货" + goods.getName() + "货物" + amount + "" + goods.getSpec();//转账备注
        //不管是不是官方
        increaseBalance(amount);
        increaseCumulative(amount);
        double points = goods.caculatePoints(amount);
        increasePoints(points);
        user.increasePoints(points);
        /**
         * 这里增加判断如果是官方、发起者、联盟就按照官方三级返回、
         */
        if (getAgentLevel().getName().contains(AgentLevelName.官方.toString()) || getAgentLevel().getName().contains(AgentLevelName.发起者.toString())) {
            int f_index = 0;//发起者计数
            int g_index = 0;//官方计数
            Agent faqizhe_agent = null;
            Agent fengongsi_agent = null;
            Agent guanfang1_agent = null;
            Agent guanfang2_agent = null;
            Agent guanfang3_agent = null;

            //分公司价格
            Double fenngongsi_price = null;
            Double faqizhe_price = null;
            Double guanfang_price;
            //递归找出发起者
            Agent parent = user.getParent();
            GoodsAccount gac = parent.loadAccountForGoodsNotNull(goods);//获取上家的
            while (!gac.getAgentLevel().getName().contains(AgentLevelName.联盟单位.toString()) && !parent.isMutedUser()) {//如果不是联盟单位，一直递归
                //如果是官方
                if (gac.getAgentLevel().getName().contains(AgentLevelName.官方.toString())) {
                    if (g_index == 0) {
                        guanfang1_agent = parent;
                    }

                    if (g_index == 1) {
                        guanfang2_agent = parent;
                    }

                    if (g_index == 2) {
                        guanfang3_agent = parent;
                    }

                    g_index++;
                }
                //如果是发起者
                if (gac.getAgentLevel().getName().contains(AgentLevelName.发起者.toString())) {
                    //找出第一个发起者
                    if (f_index == 0) {
                        faqizhe_price = gac.getPrice();//获取价格
                        faqizhe_agent = parent;
                    }
                    f_index++;
                }
                //如果包含分公司的字样，或者到了顶端
                //如果到了顶端

                parent = parent.getParent();
                gac = parent.loadAccountForGoodsNotNull(goods);//获取上家的;
            }

            //如果包含联盟单位的字样
            if (gac.getAgentLevel().getName().contains(AgentLevelName.联盟单位.toString())) {
                fenngongsi_price = gac.getPrice();
                fengongsi_agent = parent;
            }

            //如果自己是官方等级
            if (getAgentLevel().getName().contains(AgentLevelName.官方.toString())&&!parent.isMutedUser()) {
                guanfang_price = getPrice();
                Double chajia_price = (guanfang_price - faqizhe_price) * amount;//官方与发起者的差价
//                Double fandian_price = chajia_price * 0.4 * amount;//拿出其中的40%作为返点
                Double fandian_price = chajia_price * 0.4 ;//拿出其中的40%作为返点
                if (guanfang1_agent != null) {//返回给1 50%代
                    guanfang1_agent.getAccounts().increaseVoucher(fandian_price * 0.5, more);
                    chajia_price = chajia_price - fandian_price * 0.5;
                }

                if (guanfang2_agent != null) {//返回给2 0%代
//                guanfang2_agent.getAccounts().increaseVoucher(fandian_price*0,more);
//                chajia_price=chajia_price-fandian_price*0;
                }

                if (guanfang3_agent != null) {//返回给3 0%代
//                guanfang3_agent.getAccounts().increaseVoucher(fandian_price*0.3,more);
//                chajia_price=chajia_price-fandian_price*3;
                }

                if (faqizhe_agent != null) {//发起者节流
                    faqizhe_agent.getAccounts().increaseVoucher(chajia_price, more);//剩下的给发起者
                }

            } else {//发起者
                faqizhe_price = getPrice();
            }


            if (fengongsi_agent != null) {//联盟单位获利
                fengongsi_agent.getAccounts().increaseVoucher((faqizhe_price - fenngongsi_price)*amount, more);//发起者与分公司的差价

            }


//		while(index<voucherLevel){
//			Agent parent=temp_user.getParent();
//			GoodsAccount tgac=parent.loadAccountForGoodsNotNull(goods);
//			if(tgac.getAgentLevel().getName().equals("官方")){//如果上级是官方
//				index++;
//			}else if(tgac.getAgentLevel().getName().equals("发起者")){//发起者节流
//				
//				parent.getAccounts().increaseVoucher(voucher);
//				break;//循环结束
//			}
//			else{
//				temp_user=parent;//开始递归
//			}
//			
//		}

        }

    }

    /**
     * 特权商城
     *
     * @param amount
     */
    public void teq_accept(Integer amount) {
        //不管是不是官方
        increaseBalance(amount);
        increaseCumulative(amount);
        double points = goods.caculatePoints(amount);
        increasePoints(points);
        user.increasePoints(points);
        teqFanLi(amount,0);//返利
    }

    /**
     * 对大区,董事,金董,联盟的上级代金券操作
     * @param goodsAccount
     * @param amount
     * @param more
     * @param toback 1代表是退回货物
     */
    private void teqFanLi(Integer amount,int toback){
        String more;
        StringBuilder stringBuilder =new StringBuilder();
        if(toback==1){//如果是退回货物
//            more = user.getRealName() + "退回" + goods.getName() + "货物" + amount + "" + goods.getSpec();//转账备注

            stringBuilder.append(user.getRealName()).append("退回").append(goods.getName()).append("货物").append(amount).append(goods.getSpec());
        }else {
//            more = user.getRealName() + "购买" + goods.getName() + "货物" + amount + "" + goods.getSpec();//转账备注
            stringBuilder.append(user.getRealName()).append("购买").append(goods.getName()).append("货物").append(amount).append(goods.getSpec());
        }
        more=stringBuilder.toString();
        /**
         * 这里增加判断如果是特权大区,董事,金董,联盟单位
         */
        if (getAgentLevel().getName().contains(AgentLevelName.大区.toString())||getAgentLevel().getName().contains(AgentLevelName.董事.toString())||getAgentLevel().getName().contains(AgentLevelName.金董.toString())||getAgentLevel().getName().contains(AgentLevelName.联盟单位.toString())) {
            int g_index = 0;//大区计数
            int d_index = 0;//董事计数
            int j_index = 0;//金董事计数
            int l_index = 0;//联盟计数
//            if(getAgentLevel().getName().contains("董事")){
//                 d_index = 1;
//            }
//            if(getAgentLevel().getName().contains("金董")){
//                j_index = 1;
//            }
//            if(getAgentLevel().getName().contains("联盟单位")){
//                l_index = 1;
//            }
            //返利模式
            Double[] backVouchers = getbackVouchers();
            //递归整条线
            Agent parent = user.getTeqparent();
            GoodsAccount gac = parent.loadAccountForGoodsNotNull(goods);//获取上家的
            while (!parent.isMutedUser()) {//如果不是公司
                //如果是特权大区
                if (gac.getAgentLevel().getName().contains(AgentLevelName.大区.toString())) {
                    if (g_index < backVouchers.length-3) {//反几代
                        Double voucher_temp = getbackVoucher(g_index+3, amount);//返回第一个大区价格
                        if(toback==1){//退回货物就是减
                            parent.getAccounts().deductVoucherForBack(voucher_temp, more);
                        }else {
                            parent.getAccounts().increaseVoucher(voucher_temp, more);
                        }
                    }
                    g_index++;//大区计数
                }
                //如果是董事 只判断一次
                if (gac.getAgentLevel().getName().contains(AgentLevelName.董事.toString())) {
                    Double voucher_temp = getbackVoucher(g_index + 3, amount);//大区的返利会有
                    if(l_index!=0){         //如果有联盟

                    }else if(j_index!=0){  //如果有金董

                    }else if(d_index!=0){   //如果有董事

                    }else {
                        voucher_temp += getbackVoucher(2, amount);//加上董事的返利
                    }
//                    if (d_index == 0) {//如果是第一个董事
//                        voucher_temp += getbackVoucher(2, amount);//加上董事的返利
//                    }
                    if(toback==1){//退回货物
                        parent.getAccounts().deductVoucherForBack(voucher_temp, more);
                    }else {
                        parent.getAccounts().increaseVoucher(voucher_temp, more);
                    }
                    g_index++;//增加一次大区
                    d_index++;//增加一次董事
                }
                //如果是金董
                if (gac.getAgentLevel().getName().contains(AgentLevelName.金董.toString())) {
                    Double voucher_temp = getbackVoucher(g_index + 3, amount);//大区的返利会有
                    if(l_index!=0){         //如果有联盟

                    }else if(j_index!=0){  //如果有金董

                    }else if(d_index!=0){   //如果有董事  金董事的返利减去董事的返利
                        voucher_temp+=(getbackVoucher(1, amount)-getbackVoucher(2,amount));
                    }else {
                        voucher_temp += getbackVoucher(1, amount);//加上金董事的返利
                    }

//                    if (j_index == 0) {//如果是第一个金董
//                        voucher_temp += getbackVoucher(1, amount);//加上金董事的返利
//                    }
                    if(toback==1){//退回货物
                        parent.getAccounts().deductVoucherForBack(voucher_temp, more);
                    }else {
                        parent.getAccounts().increaseVoucher(voucher_temp, more);
                    }
                    g_index++;//增加一次大区
                    j_index++;//增加一次金董
                }
                //如果是联盟
                if (gac.getAgentLevel().getName().contains(AgentLevelName.联盟单位.toString())) {
                    Double voucher_temp = getbackVoucher(g_index + 3, amount);//大区的返利会有
                    Double getbackVoucher=getbackVoucher(0, amount);
                    //如果是下面几个人 联盟之返利20
                    if(parent.getAgentCode().equals("zmz213821")||parent.getAgentCode().equals("zmz126786")){
                        String gn=goods.getName();
                        if(gn.equals("和之初雪莲护理贴小礼盒")||gn.equals("和之初雪莲护理贴大礼盒")||gn.equals("特权铭颜精致小微脸定格液")||gn.equals("特权铭颜玻尿酸精华液套")){
                            getbackVoucher=20.0*amount;
                        }
                    }
                    if(l_index!=0){         //如果有联盟

                    }else if(j_index!=0){  //如果有金董
                        voucher_temp+=(getbackVoucher-getbackVoucher(1,amount));
                    }else if(d_index!=0){   //如果有董事  金董事的返利减去董事的返利
                        voucher_temp+=(getbackVoucher-getbackVoucher(2,amount));
                    }else {
                        voucher_temp += getbackVoucher;//加上联盟的返利
                    }
//
//                    if (l_index == 0) {//如果是第一个联盟
//                        voucher_temp += getbackVoucher(0, amount);//加上联盟的返利
//                    }
                    if(toback==1){//退回货物
                        parent.getAccounts().deductVoucherForBack(voucher_temp, more);
                    }else {
                        parent.getAccounts().increaseVoucher(voucher_temp, more);
                    }
                    g_index++;//增加一次大区
                    l_index++;//增加一次联盟
                }
                parent = parent.getTeqparent();
                gac = parent.loadAccountForGoodsNotNull(goods);//获取上家的;
            }
            backVouchers=null;//清空对象


//            //如果不是上级
//            if (!parent.isMutedUser()) {
//                Double voucher_temp = (double) (getbackVoucher(0, amount) + getbackVoucher(g_index + 1, amount));//返回第一个大区价格跟联盟价格
//                if(toback==1){//退回货物
//                    parent.getAccounts().deductVoucherForBack(voucher_temp, more);
//                }else {
//                    parent.getAccounts().increaseVoucher(voucher_temp, more);
//                }
//
//            }
        }

    }

//    /**
//     * 对大区的上级代金券操作
//     * @param goodsAccount
//     * @param amount
//     * @param more
//     * @param toback 1代表是退回货物
//     */
//    private void teqFanLi(Integer amount,int toback){
//        String more;
//        if(toback==1){//如果是退回货物
//            more = user.getRealName() + "退回" + goods.getName() + "货物" + amount + "" + goods.getSpec();//转账备注
//        }else {
//             more = user.getRealName() + "购买" + goods.getName() + "货物" + amount + "" + goods.getSpec();//转账备注
//        }
//        /**
//         * 这里增加判断如果是特权大区
//         */
//        if (goods.getGoodsType() == GoodsType.TEQ && getAgentLevel().getName().contains(AgentLevelName.大区.toString())) {
//
//
//            int g_index = 0;//大区计数
//            //返利模式
//            Double[] backVouchers = getbackVouchers();
//            //递归找出发起者
//            Agent parent = user.getTeqparent();
//            GoodsAccount gac = parent.loadAccountForGoodsNotNull(goods);//获取上家的
//            while (!gac.getAgentLevel().getName().contains(AgentLevelName.联盟单位.toString()) && !parent.isMutedUser()) {//如果不是特权联盟单位，也没到公司一直递归
//                //如果是特权大区
//                if (gac.getAgentLevel().getName().contains(AgentLevelName.大区.toString())) {
//                    g_index++;//大区计数
//                    if (g_index < backVouchers.length) {//反几代
//                        Double voucher_temp = (double) (getbackVoucher(g_index, amount));//返回第一个大区价格
//                      if(toback==1){//退回货物就是减
//                          parent.getAccounts().deductVoucherForBack(voucher_temp, more);
//                      }else {
//                          parent.getAccounts().increaseVoucher(voucher_temp, more);
//                      }
//
//                    }
//                }
//                parent = parent.getTeqparent();
//                gac = parent.loadAccountForGoodsNotNull(goods);//获取上家的;
//            }
//
//            //如果包含联盟的字样
//            if (gac.getAgentLevel().getName().contains(AgentLevelName.联盟单位.toString()) && gac.getGoods().getGoodsType() == GoodsType.TEQ) {
//                Double voucher_temp = (double) (getbackVoucher(0, amount) + getbackVoucher(g_index + 1, amount));//返回第一个大区价格跟联盟价格
//               if(toback==1){//退回货物
//                   parent.getAccounts().deductVoucherForBack(voucher_temp, more);
//               }else {
//                    parent.getAccounts().increaseVoucher(voucher_temp, more);
//                }
//
//            }
//        }
//
//    }


    /**
     * 获取返利模式
     *
     * @return
     */
    public Double[] getbackVouchers() {
        String[] strs = getGoods().getVoucher().trim().split("_");
        Double[] temps = new Double[strs.length];
        for (int i = 0; i < temps.length; i++) {
            temps[i] = Double.parseDouble(strs[i]);
            strs[i]=null;//释放内存
        }
        return temps;
    }


    /**
     * 获取返利
     *
     * @param i
     * @return 没有的为0
     */
    public Double getbackVoucher(int i, int amount) {
        Double[] vous = getbackVouchers();
        if (i < vous.length) {
            return vous[i] * amount;
        }
        return 0.0;
    }


    public void accept(Integer amount) {
        increaseBalance(amount);
        increaseCumulative(amount);
        double points = goods.caculatePoints(amount);
        increasePoints(points);
        user.increasePoints(points);

        //autoPromotion();
    }

    /**
     * 账户等级自动晋升
     */
    public void autoPromotion() {
        AgentLevel parentLevel = this.agentLevel.getParent();
        while (Objects.nonNull(parentLevel)) {
            Price priceLevel = goods.getPrice(parentLevel);
            if (Objects.isNull(priceLevel)) {
                parentLevel = parentLevel.getParent();
                continue;
            }
            if (cumulativeExceedsThreshold(priceLevel.getThreshold()) && parentLevel.canAutoPromotion()) {
                this.agentLevel = parentLevel;
            }
            parentLevel = parentLevel.getParent();
        }
    }

    public boolean cumulativeExceedsThreshold(Integer threshold) {
        return threshold <= this.getCumulative();
    }

    public void acceptBack(Integer amount,GoodsAccount from) {
        increaseBalance(amount);
        if (user.isMutedUser()) {
            goods.increaseCurrentBalance(amount);
        }
        //扣除from的上级的利润
        from.teqFanLi(amount,1);
    }

    /**
     * 增加积分
     *
     * @param points
     * @return
     */
    public Double increasePoints(Double points) {
        if (getCurrentPoint() + points < 0) {
            throw new ApplicationException("积分增加值非法");
        }
        setCurrentPoint(getCurrentPoint() + points);
        return getCurrentPoint();
    }

    /**
     * 积分扣减
     *
     * @param points
     * @return
     */
    public Double deductPoints(Double points) {
        if (points < 0) {
            throw new ApplicationException("积分扣减值不能为负数");
        }
        setCurrentPoint(getCurrentPoint() - points);
        return getCurrentPoint();
    }

    /**
     * 累积购买量增加
     *
     * @param cumulative
     * @return
     */
    public Integer increaseCumulative(Integer cumulative) {
        setCumulative(getCumulative() + cumulative);
        return getCumulative();
    }

    /**
     * 累积购买量减少
     *
     * @param cumulative
     * @return
     */
    public Integer deductCumulative(Integer cumulative) {
        setCumulative(getCumulative() - cumulative);
        if (this.getCumulative() < 0) {
            this.setCumulative(0);
        }
        return getCumulative();
    }

    /**
     * 增加账户余额
     *
     * @param balance
     * @return
     */
    public Integer increaseBalance(Integer balance) {
        if (getCurrentBalance() + balance < 0) {
            throw new ApplicationException("货物账户余额增加值非法");
        }
        setCurrentBalance(getCurrentBalance() + balance);
        return getCurrentBalance();
    }

    /**
     * 扣减账户余额
     *
     * @param balance
     * @return
     */
    public Integer deductBalance(Integer balance) {
        if (balance < 0) {
            throw new ApplicationException("账户余额扣减值不能为负数");
        }
        if (balance > getCurrentBalance()) {
            throw new ApplicationException("需扣减的[" + goods.getName() + "]账户余额不足");
        }
        setCurrentBalance(getCurrentBalance() - balance);
        return getCurrentBalance();
    }

    public Double getPrice() {
        Price price = goods.getPrice(agentLevel);
        return price == null ? 0.0D : price.getPrice();

    }

    // Constructors

    public Integer getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(Integer currentBalance) {
        this.currentBalance = currentBalance;
    }

    /**
     * default constructor
     */
    public GoodsAccount() {
    }

    /**
     * minimal constructor
     */
    public GoodsAccount(Agent user, Goods goods, Double currentPoint) {
        this.user = user;
        this.goods = goods;
        this.currentPoint = currentPoint;
    }

    /**
     * full constructor
     */
    public GoodsAccount(Agent user, Goods goods, Double currentPoint,
                        Timestamp updateTime, Integer version) {
        this.user = user;
        this.goods = goods;
        this.currentPoint = currentPoint;
        this.updateTime = updateTime;
        this.version = version;
    }

    // Property accessors

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Goods getGoods() {
        return this.goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;

    }

    public Double getCurrentPoint() {
        return this.currentPoint;
    }

    public void setCurrentPoint(Double currentPoint) {
        this.currentPoint = currentPoint;
    }

    public Date getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getVersion() {
        return this.version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Integer getCumulative() {
        return cumulative;
    }

    public void setCumulative(Integer cumulative) {
        this.cumulative = cumulative;
    }

    public AgentLevel getAgentLevel() {
        return agentLevel;
    }

    public void setAgentLevel(AgentLevel agentLevel) {
        this.agentLevel = agentLevel;
    }

    @Override
    public int hashCode() {
        // TODO Auto-generated method stub
        return Objects.hash(goods);
    }

    @Override
    public boolean equals(Object obj) {
        // TODO Auto-generated method stub
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof GoodsAccount)) {
            return false;
        }
        GoodsAccount other = (GoodsAccount) obj;
        return Objects.equals(goods, other.getGoods());
    }


}