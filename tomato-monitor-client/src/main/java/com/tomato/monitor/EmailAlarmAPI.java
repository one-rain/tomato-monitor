package com.tomato.monitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * User: wangrun
 * Date: 2016/9/2 18:20
 */
public class EmailAlarmAPI implements AlarmAPI {

    private static final Logger LOG = LoggerFactory.getLogger(EmailAlarmAPI.class);

    /**
     * 发送报警
     * @param serviceId  服务id，从后台配置获取
     * @param content    邮件内容
     * @return
     */
    @Override
    public void send(String serviceId, String content) {
        this.send(serviceId, content, 0);
    }

    /**
     *
     * @param serviceId  服务id，从后台配置获取
     * @param content    邮件内容
     * @param valid      控制频率标识，1：控制，默认为不控制
     */
    @Override
    public void send(String serviceId, String content, int valid) {
        String url = HttpClientUtil.getAlarmUrl() + "/send?tag=" + serviceId + "&valid=" + valid;
        Map<String, String> map = new HashMap<String, String>();
        map.put("content", content);
        HttpClientManager http = new HttpClientManager();
        http.httpPost(url, map);
    }

    public static void main(String[] args) {
        AlarmAPI alarmAPI = new EmailAlarmAPI();
        //alarmAPI.send("emr-job-day", "这个一个客户端测试");
        alarmAPI.send("emr-job-day", "这个一个客户端测试", 1);
        //System.exit(1);
        LOG.info("over...");
    }
}
