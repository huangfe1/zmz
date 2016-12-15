package com.dreamer.service.wxchat;

import com.dreamer.domain.account.VoucherRecord;

/**
 * Created by huangfei on 16/7/18.
 * 微信通知类
 */
public interface NotcieHandler {
    /**
     * 返利通知
     * @param agentAndCode 代理姓名与编号
     * @param goodsName 产品名字
     * @param number 产品数量
     * @param voucher 返代金券数量
     * @param time  返利时间
     */
    void sendFanLiNotice(String nameAndCode,String goodsName,int number,Double voucher);

     void sendRecord();

    /**
     * 代金券纪录通知
     * @param Body
     */
    void sendVoucherRecord(VoucherRecord v);

    /**
     * 活动通知
     */
    boolean sendMessage(String openId,String url,String mid,String title,String[] keywords ,String remark);



    /**
     * 下级转货通知
     * @param nameAndCode 下级编号
     * @param orderId 转货ID
     */
    void sendTransferOrderNotice(String nameAndCode,int orderId);

    /**
     * 下级直接发货通知
     * @param nameAndCode 下级编号
     * @param orderId 订单ID
     */
    void sendTransferDeliverNotice(String nameAndCode,String orderNo);

    /**
     * 操作成功通知
     * @param noticeBody  操作内容
     */
    void sendOperationNotice(String noticeBody);
}
