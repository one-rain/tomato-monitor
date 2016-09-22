package com.tomato.monitor.alarm;

import com.google.common.collect.Sets;
import com.tomato.monitor.alarm.web.AlarmService;
import com.tomato.monitor.alarm.work.AlarmSendWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Application;
import java.util.Set;

/**
 * User: wangrun
 * Date: 2016/9/5 15:08
 */
public class InitApplication extends Application {

    private static Logger LOG = LoggerFactory.getLogger(InitApplication.class);

    private Set<Object> singletons = Sets.newHashSet();
    private Set<Class<?>> classes = Sets.newHashSet();

    public InitApplication() {
        LOG.info("init application...");
        // 把你的rest资源添加到这个属性中
        this.singletons.add(new AlarmService());
        AlarmSendWorker.startPoll();
    }

    @Override
    public Set<Class<?>> getClasses() {
        return classes;
    }

    @Override
    public Set<Object> getSingletons() {
        return singletons;
    }
}
