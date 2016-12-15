package com.dreamer.util;

/**
 * Created by huangfei on 16/6/14.
 */
public class TokenInfo {

    public static String IMG_HEAD_PATH="http://www.zmz365.com:8081/dreamer";

    public static String ACCESS_TOKEN;

    public static String JSAPI_TICKET;

    public static String getAccessToken() {
        return ACCESS_TOKEN;
    }

    public static void setAccessToken(String accessToken) {
        ACCESS_TOKEN = accessToken;
    }

    public static String getJsapiTicket() {
        return JSAPI_TICKET;
    }

    public static void setJsapiTicket(String jsapiTicket) {
        JSAPI_TICKET = jsapiTicket;
    }
}
