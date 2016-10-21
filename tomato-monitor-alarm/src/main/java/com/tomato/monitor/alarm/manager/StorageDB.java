package com.tomato.monitor.alarm.manager;

import com.tomato.monitor.alarm.bean.AlarmBean;
import com.tomato.monitor.alarm.bean.MessageBean;

import java.util.Map;
import java.util.Queue;

/**
 * 基于数据库的预警信息存储
 * User: wangrun
 * Date: 2016/9/20 16:17
 */
public class StorageDB extends StorageManager {

    @Override
    public MessageBean add(AlarmBean alarmBean) {
        return null;
    }

    @Override
    public AlarmBean get() {
        return null;
    }

    @Override
    public Queue<AlarmBean> getAll() {
        return null;
    }

    @Override
    public Map<String, AlarmBean> getAllInterval() {
        return null;
    }

    @Override
    public void deleteInterval() {

    }

    @Override
    public AlarmBean delete() {
        return null;
    }
}
