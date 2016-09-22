package com.tomato.monitor.alarm.work;

import com.tomato.monitor.alarm.bean.AlarmBean;
import com.tomato.monitor.alarm.bean.ReceiveEmail;
import com.tomato.monitor.alarm.bean.ReceivePhone;
import com.tomato.monitor.alarm.manager.StorageFactory;
import com.tomato.monitor.alarm.manager.StorageManager;
import com.tomato.monitor.alarm.service.AlarmService;
import com.tomato.monitor.alarm.service.EmailServiceImpl;
import com.tomato.monitor.alarm.service.PhoneServiceImpl;
import com.tomato.monitor.alarm.utils.ConfigUtil;
import com.tomato.monitor.alarm.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * User: wangrun
 * Date: 2016/8/31 15:50
 */
public class AlarmSendWorker {

    private static Logger LOG = LoggerFactory.getLogger(AlarmSendWorker.class);

    private static boolean start;

    /**
     * 启动轮询任务
     */
    public static void startPoll() {
        if (!start) {
            synchronized (AlarmSendWorker.class) {
                if (!start) {
                    ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
                    executorService.scheduleAtFixedRate(new SendWork(), 2,
                            ConfigUtil.getAllConfig().getInt(Constants.SEND_POLL_PERIOD), TimeUnit.SECONDS);

                    executorService.scheduleAtFixedRate(new CleanWork(), 5,
                            ConfigUtil.getAllConfig().getInt(Constants.CLEAN_POLL_PERIOD), TimeUnit.SECONDS);
                    start = true;
                }
            }
        }
    }

    private static class SendWork implements Runnable {

        public void run() {
            StorageManager storageManager = StorageFactory.getStorage("default");
            Map<String, AlarmBean> map = storageManager.getAll();
            if (map.size() > 0) {
                LOG.info("start poll send...");
                for (Map.Entry<String, AlarmBean> entry : map.entrySet()) {
                    AlarmBean alarmBean = entry.getValue();
                    if (!alarmBean.isSend()) {

                        this.setReceive(alarmBean);

                        if (alarmBean.isSend()) {
                            LOG.info("success send alarm.");
                            storageManager.getAll().put(alarmBean.getServiceTag(), alarmBean);
                        }else {
                            LOG.error("send alarm failed!");
                            storageManager.getAll().remove(alarmBean.getServiceTag());
                        }
                    }
                }
                LOG.info("end poll send...");
            }
        }

        private void setReceive(AlarmBean alarmBean) {
            boolean flagEmail = false, flagPhone = false;
            ReceiveEmail receiveEmail = alarmBean.getReceiveEmail();
            if (receiveEmail != null) {
                AlarmService alarmService = new EmailServiceImpl();
                try {
                    alarmService.send(alarmBean);
                    receiveEmail.setSend(true);
                    alarmBean.setReceiveEmail(receiveEmail);
                    flagEmail = true;
                } catch (Exception e) {
                    alarmBean.setSend(false);
                    LOG.error("send email alarm error!, service tag: " + alarmBean.getServiceTag(), e);
                }
            }

            ReceivePhone receivePhone = alarmBean.getReceivePhone();
            if (receivePhone != null) {
                AlarmService alarmService = new PhoneServiceImpl();
                try {
                    alarmService.send(alarmBean);
                    receivePhone.setSend(true);
                    alarmBean.setReceivePhone(receivePhone);
                    flagPhone = true;
                } catch (Exception e) {
                    alarmBean.setSend(false);
                    LOG.error("send phone alarm error!, service tag: " + alarmBean.getServiceTag(), e);
                }
            }

            if (flagEmail || flagPhone) {
                alarmBean.setSend(true);
            }
        }
    }

    private static class CleanWork implements Runnable {

        @Override
        public void run() {
            LOG.info("start poll clean...");
            StorageManager storageManager = StorageFactory.getStorage("default");
            storageManager.delete();
            LOG.info("end poll clean...");
        }
    }

}
