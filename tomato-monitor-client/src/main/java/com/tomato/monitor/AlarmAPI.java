package com.tomato.monitor;

/**
 * User: wangrun
 * Date: 2016/9/13 13:22
 */
public interface AlarmAPI {

    public void send(String serviceId, String content);

    public void send(String serviceId, String content, int valid);
}
