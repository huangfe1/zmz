package com.dreamer.service.wxchat;

import com.dreamer.domain.account.VoucherRecord;
import com.dreamer.domain.wechat.WxNotcieTemplateData;
import com.dreamer.domain.wechat.WxNoticeTemplate;
import com.dreamer.repository.account.VoucherRecordDAO;
import com.dreamer.util.HttpTookit;
import com.dreamer.util.TokenInfo;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by huangfei on 16/7/18.
 */
@Service("noticeHandlerImpl")
public class NoticeHandlerImpl implements NotcieHandler {

    @Autowired
    public VoucherRecordDAO voucherRecordDAO;

    private String NOTICE_URL="https://api.weixin.qq.com/cgi-bin/message/template/send?access_token={ACCESS_TOKEN}";

    private String getNOTICE_URL(){
        UriComponents ucb = ServletUriComponentsBuilder.fromHttpUrl(
                NOTICE_URL).buildAndExpand(TokenInfo.getAccessToken());
        return ucb.toString();
    }
    @Override
    public void sendFanLiNotice(String nameAndCode, String goodsName, int number, Double voucher) {

    }
    //微信通知
    @Transactional
    public void sendRecord() {
        /**
         *代金券变动通知
         */
        List<VoucherRecord> vs=voucherRecordDAO.getNeedNoticeRecord();
        for(VoucherRecord v:vs){
            sendVoucherRecord(v);
            v.setHasNoticed(1);
            voucherRecordDAO.save(v);
        }
    }





    @Override
    public boolean sendMessage(String openId, String url, String template_id, String title, String[] keywords, String remark) {
        Map<String,WxNotcieTemplateData> m = new HashMap<>();
        WxNoticeTemplate wxNoticeTemplate = new WxNoticeTemplate();
        wxNoticeTemplate.setUrl(url);
        wxNoticeTemplate.setTouser(openId);
        wxNoticeTemplate.setTemplate_id(template_id);
        wxNoticeTemplate.setTopcolor("#173177");
        //设置标题
        WxNotcieTemplateData first = new WxNotcieTemplateData();
        first.setValue(title);
        first.setColor("#173177");
        m.put("first",first);
        WxNotcieTemplateData keyword;
        //设置字段
        for(int i=1;i<=keywords.length;i++){
            keyword = new WxNotcieTemplateData();
            keyword.setValue(keywords[i-1]);
            keyword.setColor("#173177");
            m.put("keyword"+i,keyword);
        }
        //设置remark
        WxNotcieTemplateData remarkData = new WxNotcieTemplateData();
        remarkData.setValue(remark);
        remarkData.setColor("#173177");
        m.put("remark",remarkData);
        //加入data
        wxNoticeTemplate.setData(m);
        JSONObject jsonObject = JSONObject.fromObject(wxNoticeTemplate);
        String result= HttpTookit.doPostStr(getNOTICE_URL(),jsonObject.toString());//发送消息
        if(result.contains("ok")){
            return true;
        }
        return false;
    }

    /**
     * 代金券通知通知
     * @param voucherRecord
     */
    @Override
    public void sendVoucherRecord(VoucherRecord voucherRecord) {
        String openId=voucherRecord.getAgent().getWxOpenid();
        if(openId==null||openId.equals("")){
            return;
        }
        WxNoticeTemplate wxNoticeTemplate = new WxNoticeTemplate();
        wxNoticeTemplate.setUrl("http://www.zmz365.com/dreamer/voucher/record.html");
        wxNoticeTemplate.setTouser(openId);
        wxNoticeTemplate.setTopcolor("#173177");
        String template_id;
        String firstvalue,keyword1value,keyword2value,keyword3value,remarkvalue;

        if(voucherRecord.getType()==1){//代金券增加
            template_id="PFRuQ6QleOKVx8NdXTFZU1_JhV44WQuxY5VbGrGVvoQ";
//            firstvalue=voucherRecord.getMore();
            firstvalue=voucherRecord.getMore().substring(0,9)+".....";
            keyword1value=voucherRecord.getUpdateTime().toString();
            keyword2value=String.valueOf(voucherRecord.getVoucher());//金额
            keyword3value=String.valueOf(voucherRecord.getVoucher_now());//余额
            remarkvalue="点击'查看详情'立即查看您的账户财务记录。";
        }else {
            template_id ="YNYB1-BK635d_Fstwl5IHgIJwdTTMSKt-Cbstn6JCHI";
             firstvalue="代金券消费通知!";
             keyword1value=String.valueOf(voucherRecord.getVoucher());//金额
             keyword2value="筑美商城";
             keyword3value=voucherRecord.getUpdateTime().toString();
             remarkvalue=voucherRecord.getMore()+",剩余代金券"+voucherRecord.getVoucher_now();
        }
        wxNoticeTemplate.setTemplate_id(template_id);
        Map<String,WxNotcieTemplateData> m = new HashMap<>();
        WxNotcieTemplateData first = new WxNotcieTemplateData();
        first.setValue(firstvalue);
        first.setColor("#173177");
        WxNotcieTemplateData keyword1 = new WxNotcieTemplateData();
        keyword1.setValue(keyword1value);
        keyword1.setColor("#173177");

        WxNotcieTemplateData keyword2 = new WxNotcieTemplateData();
        keyword2.setValue(keyword2value);
        keyword2.setColor("#173177");

        WxNotcieTemplateData keyword3 = new WxNotcieTemplateData();
        keyword3.setValue(keyword3value);
        keyword3.setColor("#173177");

        WxNotcieTemplateData remark = new WxNotcieTemplateData();
        remark.setValue(remarkvalue);
        remark.setColor("#173177");
        m.put("first",first);
        m.put("keyword1",keyword1);
        m.put("keyword2",keyword2);
        m.put("keyword3",keyword3);
        m.put("remark",remark);
        wxNoticeTemplate.setData(m);
        JSONObject jsonObject = JSONObject.fromObject(wxNoticeTemplate);
       String result= HttpTookit.doPostStr(getNOTICE_URL(),jsonObject.toString());//发送消息
//        System.out.println(result+"-------result-------");
    }



    @Override
    public void sendTransferOrderNotice(String nameAndCode, int orderId) {

    }

    @Override
    public void sendTransferDeliverNotice(String nameAndCode, String orderNo) {

    }

    @Override
    public void sendOperationNotice(String noticeBody) {

    }
}
