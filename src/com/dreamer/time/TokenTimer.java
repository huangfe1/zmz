package com.dreamer.time;

import com.dreamer.service.pay.GetOpenIdHandler;
import com.dreamer.service.pay.PayConfig;
import com.dreamer.util.TokenInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by huangfei on 16/6/14.
 */
@Component("tokenTimer")
public class TokenTimer {

    @Autowired
    private GetOpenIdHandler getOpenIdHandler;

    @Autowired
    private PayConfig payConfig;

    public void  doIt(){
        //每隔7000秒获取一次token与ticket
        String access_token=getOpenIdHandler.getToken(payConfig).get("access_token");
        TokenInfo.setAccessToken(access_token);
        String ticket=getOpenIdHandler.getTicket(access_token).get("ticket");
        TokenInfo.setJsapiTicket(ticket);
    }
}
