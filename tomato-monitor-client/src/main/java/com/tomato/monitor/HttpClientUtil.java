package com.tomato.monitor;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * User: wangrun
 * Date: 2016/9/2 17:53
 */
public class HttpClientUtil {

    private static final Logger LOG = LoggerFactory.getLogger(HttpClientUtil.class);

    private static int SOCKET_TIME_OUT = 2000;//通信超时时间，毫秒
    private static int CONNECTION_TIME_OUT = 2000;//连接超时时间，毫秒

    public static String ALARM_URL = "http://127.0.0.1/tomato-monitor-alarm/alarm";

    /**
     * 发送HTTP get请求。 如果失败，返回null
     * @param url
     * @return
     */
    public static String httpGet(String url) {
        HttpClient httpClient = HttpClients.createDefault();
        String message = null;
        HttpGet httpGet = null;
        try {
            httpGet = new HttpGet(url);
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(SOCKET_TIME_OUT).
                    setConnectTimeout(CONNECTION_TIME_OUT).build();
            httpGet.setConfig(requestConfig);
            HttpResponse response = httpClient.execute(httpGet);//执行请求
            int code = response.getStatusLine().getStatusCode();
            if(code == HttpStatus.SC_OK) {
                message = EntityUtils.toString(response.getEntity());
            }else {
                LOG.info("can't get service info, status code: {}", code);
                message = "status code: " + code;
            }
        } catch (Exception e) {
            LOG.error("send " + url + " failed.", e);
        } finally {
            if(httpGet != null) {
                httpGet.releaseConnection();
            }
        }
        return message;
    }

    /**
     * 发送HTTP post 请求。如果失败，返回null
     * @param url
     * @param map request body 参数
     * @return
     * @throws Exception
     */
    public static String httpPost(String url, Map<String, String> map) {
        HttpClient httpClient = HttpClients.createDefault();
        String message = null;
        HttpPost httpPost = null;
        try {
            httpPost = new HttpPost(url);
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(SOCKET_TIME_OUT).
                    setConnectTimeout(CONNECTION_TIME_OUT).build();//设置请求和传输超时时间
            httpPost.setConfig(requestConfig);

            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            Set<String> keySet = map.keySet();
            for(String key : keySet) {
                nvps.add(new BasicNameValuePair(key, map.get(key)));
            }
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
            HttpResponse response = httpClient.execute(httpPost);
            int code = response.getStatusLine().getStatusCode();
            if(code == HttpStatus.SC_OK) {
                return EntityUtils.toString(response.getEntity());
            }else {
                LOG.info("can't get service info, status code: {}", code);
                message = "status code: " + code;
            }
        } catch (Exception e) {
            LOG.error("send " + url + " failed.", e);
        } finally {
            if(httpPost != null) {
                httpPost.releaseConnection();
            }
        }
        return message;
    }

    public static void setSocketTimeOut(int timeOut) {
        SOCKET_TIME_OUT = timeOut;
    }

    public static void setConnectionTimeOut(int connectionTimeOut) {
        CONNECTION_TIME_OUT = connectionTimeOut;
    }

    public static String getAlarmUrl() {
        return ALARM_URL;
    }

    public static void setAlarmUrl(String alarmUrl) {
        ALARM_URL = alarmUrl;
    }
}
