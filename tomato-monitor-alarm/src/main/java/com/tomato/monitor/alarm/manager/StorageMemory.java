package com.tomato.monitor.alarm.manager;

import com.google.common.collect.Maps;
import com.tomato.monitor.alarm.bean.AlarmBean;
import com.tomato.monitor.alarm.bean.MessageBean;
import com.tomato.monitor.alarm.bean.StatusCode;
import com.tomato.monitor.alarm.utils.ConfigUtil;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 *  基于内存的预警信息存储
 * User: wangrun
 * Date: 2016/9/5 11:11
 */
public class StorageMemory extends StorageManager{


    private static Logger LOG = LoggerFactory.getLogger(StorageMemory.class);

    //保存需要控制频率的
    public static Map<String, AlarmBean> controlMap = Maps.newConcurrentMap();
    //需要发生报警的队列
    public static Queue<AlarmBean> queue = new LinkedList<AlarmBean>();

    @Override
    public MessageBean add(AlarmBean alarmBean) {
        MessageBean messageBean = new MessageBean();
        boolean exist = false;
        for (AlarmBean alarm : ConfigUtil.getAlarmConfig()) {
            if (alarm.getServiceTag().equals(alarmBean.getServiceTag())) {
                exist = true;
                alarmBean.setServiceName(alarm.getServiceName());
                alarmBean.setInterval(alarm.getInterval());
                alarmBean.setLevel(alarm.getLevel());
                alarmBean.setReceiveEmail(alarm.getReceiveEmail());
                alarmBean.setReceivePhone(alarm.getReceivePhone());
                alarmBean.setStatus(alarm.getStatus());
                break;
            }
        }

        if (exist) {
            String key = alarmBean.getServiceTag() + "_" + alarmBean.getIp();
            AlarmBean alarm =  controlMap.get(key);
            if ((alarm == null || !alarmBean.isIntervalValid()) && (alarmBean.getStatus() == 1)) {
                alarmBean.setTimestamp(System.currentTimeMillis());
                if (alarmBean.isIntervalValid()) {
                    controlMap.put(key, alarmBean);
                }
                queue.offer(alarmBean);
                LOG.info("{} success add to queue", key);

                messageBean.setCode(StatusCode.SUCCESS);
                messageBean.setDescribe("the service tag '" + alarmBean.getServiceTag() + "' is successful send.");
                return messageBean;
            }else {
                LOG.info("{} within session {}s, don't add queue.", alarmBean.getServiceTag(), alarmBean.getInterval());
                messageBean.setCode(StatusCode.SUCCESS_NOT_DO);
                messageBean.setDescribe("the service tag '" + alarmBean.getServiceTag() + "' have send at "
                        + DateFormatUtils.format(alarm.getTimestamp(), "yyyy-MM-dd HH:mm:ss") + ". within session " + alarmBean.getInterval() + "s.");
                return messageBean;
            }
        }else if(alarmBean.getStatus() == 0) {
            messageBean.setCode(StatusCode.NOT_EXIST);
            messageBean.setDescribe("the service tag '" + alarmBean.getServiceTag() + "' is closed.");
            return messageBean;
        }else {
            messageBean.setCode(StatusCode.NOT_EXIST);
            messageBean.setDescribe("the service tag '" + alarmBean.getServiceTag() + "' is not config.");
            return messageBean;
        }
    }

    @Override
    public AlarmBean get() {
        return queue.peek();
    }

    @Override
    public Map<String, AlarmBean> getAllInterval() {
        return controlMap;
    }

    @Override
    public Queue<AlarmBean> getAll() {
        return queue;
    }

    @Override
    public AlarmBean delete() {
        //LOG.info("alarm queue size: {}", queue.size());
        AlarmBean alarmBean = queue.poll();
        LOG.info("success remove {}", alarmBean.getServiceTag() + "_" + alarmBean.getIp());
        //LOG.info("alarm queue size: {}", queue.size());
        return alarmBean;
    }

    @Override
    public void deleteInterval() {
        //int size = controlMap.size();
        for (Map.Entry<String, AlarmBean> entry : controlMap.entrySet()) {
            String key = entry.getKey();
            AlarmBean alarmBean = entry.getValue();
            long now = System.currentTimeMillis();
            if ((now - alarmBean.getTimestamp()) / 1000 > alarmBean.getInterval()) {
                controlMap.remove(key);
                LOG.info("{} session time failure, remove queue.", key);
            }
        }
    }
}
