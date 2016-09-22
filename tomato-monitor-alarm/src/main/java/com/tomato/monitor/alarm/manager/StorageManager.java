package com.tomato.monitor.alarm.manager;


import com.tomato.monitor.alarm.bean.AlarmBean;
import com.tomato.monitor.alarm.bean.MessageBean;

import java.util.Map;

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
     * 获取预警信息
     * @param tag
     * @return
     */
    public abstract AlarmBean get(String tag);

    /**
     * 获取所有的预警信息
     * @return
     */
    public abstract Map<String, AlarmBean> getAll();

    /**
     * 删除预警信息
     */
    public abstract void delete();
}
