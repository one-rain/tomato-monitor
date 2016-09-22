package com.tomato.monitor.alarm.manager;

import com.google.common.collect.Maps;
import com.tomato.monitor.alarm.bean.AlarmBean;
import com.tomato.monitor.alarm.bean.MessageBean;
import com.tomato.monitor.alarm.bean.StatusCode;
import com.tomato.monitor.alarm.utils.ConfigUtil;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 *  基于内存的预警信息存储
 * User: wangrun
 * Date: 2016/9/5 11:11
 */
public class StorageMemory extends StorageManager{

    private static Logger LOG = LoggerFactory.getLogger(StorageMemory.class);

    public static Map<String, AlarmBean> map = Maps.newConcurrentMap();

    @Override
    public MessageBean add(AlarmBean alarmBean) {
        MessageBean messageBean = new MessageBean();
        List<AlarmBean> alarmList = ConfigUtil.getAlarmConfig();
        boolean exist = false;
        for (AlarmBean alarm : alarmList) {
            if (alarm.getServiceTag().equals(alarmBean.getServiceTag())) {
                exist = true;
                alarm.setContent(alarmBean.getContent());
                alarm.setIp(alarmBean.getIp());
                alarm.setIntervalValid(alarmBean.isIntervalValid());
                alarmBean = alarm;
                break;
            }
        }

        if (exist) {
            AlarmBean alarm =  map.get(alarmBean.getServiceTag());
            if (alarm == null || alarmBean.isIntervalValid()) {
                alarmBean.setSend(false);
                alarmBean.setTimestamp(System.currentTimeMillis());
                map.put(alarmBean.getServiceTag(), alarmBean);
                LOG.info("{} success add queue", alarmBean.getServiceTag());

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
        }else {
            messageBean.setCode(StatusCode.NOT_EXIST);
            messageBean.setDescribe("the service tag '" + alarmBean.getServiceTag() + "' is not config.");
            return messageBean;
        }
    }

    @Override
    public AlarmBean get(String tag) {
        return map.get(tag);
    }

    @Override
    public Map<String, AlarmBean> getAll() {
        return map;
    }

    @Override
    public void delete() {
        long now = System.currentTimeMillis();
        int size = map.size();
        for (Map.Entry<String, AlarmBean> entry : map.entrySet()) {
            String key = entry.getKey();
            AlarmBean alarmBean = entry.getValue();
            if (alarmBean.isIntervalValid()) {//控制发送频率
                if ((now - alarmBean.getTimestamp()) / 1000 > alarmBean.getInterval()) {
                    map.remove(key);
                    LOG.info("{} session time failure, remove queue.", alarmBean.getServiceTag());
                }
            }else {
                //int period =ConfigUtil.getAllConfig().getInt(Constants.SEND_POLL_PERIOD, Constants.DEFAULT_SEND_POLL_PERIOD);
                if (alarmBean.isSend()) {
                    map.remove(key);
                    LOG.info("{} intervalValid is close, remove queue.", alarmBean.getServiceTag());
                }
            }
        }
        LOG.info("queue size is {} before clean, now size is {}.", size, map.size());
    }
}
