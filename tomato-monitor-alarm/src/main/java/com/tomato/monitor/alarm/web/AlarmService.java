package com.tomato.monitor.alarm.web;

import com.tomato.monitor.alarm.bean.AlarmBean;
import com.tomato.monitor.alarm.bean.MessageBean;
import com.tomato.monitor.alarm.manager.StorageFactory;
import com.tomato.monitor.alarm.manager.StorageManager;
import com.tomato.monitor.alarm.utils.NetworkUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

/**
 * User: wangrun
 * Date: 2016/9/5 10:57
 */
@Path("/alarm")
public class AlarmService {

    private static Logger LOG = LoggerFactory.getLogger(AlarmService.class);

    @POST
    @Path("/send")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public MessageBean add(@QueryParam("tag") String tag, @QueryParam("valid") String valid, @Context HttpServletRequest request) {
        String ip = "";
        try {
            ip = NetworkUtil.getIpAddress(request);
        } catch (IOException e) {
            LOG.error("get ip error!", e);
        }

        LOG.info("send alarm message. ip: {}, service tag: {}, valid: {}", ip, tag, (valid == null ? "" : valid));

        AlarmBean alarmBean = new AlarmBean();
        String content = request.getParameter("content");
        StorageManager storageManager = StorageFactory.getStorage("default");
        alarmBean.setServiceTag(tag);
        alarmBean.setContent(content);
        alarmBean.setIp(ip);
        if (valid != null && "1".equals(valid)) {//打开频率控制
            alarmBean.setIntervalValid(true);
        }
        return storageManager.add(alarmBean);
    }

}
