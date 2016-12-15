package com.dreamer.service.pay;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ps.mx.otter.utils.digest.DigestToolSHA1;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class Signature {
	
	private static final Logger LOG=LoggerFactory.getLogger(Signature.class);
	
	
	private static final DigestToolSHA1 digestToolSHA1=new DigestToolSHA1();
    /**
     * 签名算法
     * @param o 要参与签名的数据对象
     * @return 签名
     * @throws IllegalAccessException
     */
    public static String getSign(Object o,String key) throws IllegalAccessException {
        ArrayList<String> list = new ArrayList<String>();
        Class<?> cls = o.getClass();
        Field[] fields = cls.getDeclaredFields();
        for (Field f : fields) {
            f.setAccessible(true);
            if (f.get(o) != null && f.get(o) != "") {
                list.add(f.getName() + "=" + f.get(o) + "&");
            }
        }
        int size = list.size();
        String [] arrayToSort = list.toArray(new String[size]);
        Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < size; i ++) {
            sb.append(arrayToSort[i]);
        }
        String result = sb.toString();
        result += "key=" + key;
        LOG.debug("Sign Before MD5:{}", result);
        result = MD5.MD5Encode(result,PayConfig.CHARSET).toUpperCase();
        LOG.debug("Sign Result:{}", result);
        return result;
    }

    public static String getSign(Map<String,Object> map,String key){
        ArrayList<String> list = new ArrayList<String>();
        for(Map.Entry<String,Object> entry:map.entrySet()){
            if(entry.getValue()!=""){
                list.add(entry.getKey() + "=" + entry.getValue() + "&");
            }
        }
        int size = list.size();
        String [] arrayToSort = list.toArray(new String[size]);
        Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < size; i ++) {
            sb.append(arrayToSort[i]);
        }
        String result = sb.toString();
        result += "key=" + key;
        LOG.debug("Sign Before MD5:{}", result);
        result = MD5.MD5Encode(result,PayConfig.CHARSET).toUpperCase();
        LOG.debug("Sign Result:{}",result);
        return result;
    }
    
    public static String getSHA1Sign(Map<String,Object> map){
        ArrayList<String> list = new ArrayList<String>();
        for(Map.Entry<String,Object> entry:map.entrySet()){
            if(entry.getValue()!=""){
                list.add(entry.getKey() + "=" + entry.getValue() + "&");
            }
        }
        int size = list.size();
        String [] arrayToSort = list.toArray(new String[size]);
        Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < size; i ++) {
            sb.append(arrayToSort[i]);
        }
        String result = sb.toString().substring(0,sb.length()-1);
        LOG.debug("Sign Before SHA1:{}", result);
        result=digestToolSHA1.generateDigest(result, null);
        LOG.debug("Sign SHA1 Result:{}",result);
        return result;
    }


}
