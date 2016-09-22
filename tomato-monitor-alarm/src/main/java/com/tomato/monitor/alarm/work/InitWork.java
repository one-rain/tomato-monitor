package com.tomato.monitor.alarm.work;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * User: wangrun
 * Date: 2016/9/9 18:28
 */
public class InitWork implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
        //ServletContext servletContext = event.getServletContext();
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
    }
}
