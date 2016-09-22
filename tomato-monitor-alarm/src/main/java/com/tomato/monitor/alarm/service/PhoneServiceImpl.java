package com.tomato.monitor.alarm.service;

import com.tomato.monitor.alarm.bean.AlarmBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: wangrun
 * Date: 2016/8/26 14:07
 */
public class PhoneServiceImpl extends AlarmService {

    private static Logger LOG = LoggerFactory.getLogger(PhoneServiceImpl.class);

    @Override
    public String send(AlarmBean alarmBean) throws Exception {
        LOG.info("success send phone message...");
        return null;
    }

}
