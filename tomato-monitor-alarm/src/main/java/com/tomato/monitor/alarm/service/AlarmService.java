package com.tomato.monitor.alarm.service;


import com.tomato.monitor.alarm.bean.AlarmBean;

/**
 * User: wangrun
 * Date: 2016/8/26 14:18
 */
public abstract class AlarmService {

    /**
     * 发送预警
     * @param alarmBean
     * @return
     * @throws Exception
     */
    public abstract String send(AlarmBean alarmBean) throws Exception;

}
