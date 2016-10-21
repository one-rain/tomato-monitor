package com.tomato.monitor.alarm.manager;


import com.tomato.monitor.alarm.bean.AlarmBean;
import com.tomato.monitor.alarm.bean.MessageBean;

import java.util.Map;
import java.util.Queue;

/**
 * User: wangrun
 * Date: 2016/9/5 11:15
 */
public abstract class StorageManager {

    /**
     * 添加预警信息
     * @param alarmBean
     * @return
     */
    public abstract MessageBean add(AlarmBean alarmBean);

    /**
     * 获取头部预警信息
     * @return
     */
    public abstract AlarmBean get();

    /**
     * 获取所有待发送的预警信息
     * @return
     */
    public abstract Queue<AlarmBean> getAll();

    /**
     * 获取所有在间隔期间内的预警
     * @return
     */
    public abstract Map<String, AlarmBean> getAllInterval();

    /**
     * 删除间隔时间过期的预警信息
     */
    public abstract void deleteInterval();

    /**
     * 删除头部的预警信息
     */
    public abstract AlarmBean delete();
}
