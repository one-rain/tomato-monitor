package com.tomato.monitor.alarm.utils;

import com.google.common.collect.Lists;
import com.tomato.monitor.alarm.bean.AlarmBean;
import com.tomato.monitor.alarm.bean.LevelBean;
import com.tomato.monitor.alarm.bean.ReceiveEmail;
import com.tomato.monitor.alarm.bean.ReceivePhone;
import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Iterator;
import java.util.List;


/**
 *
 * User: wangrun
 * Date: 2016/7/27 11:50
 */
public class ConfigUtil {

    private static final Logger LOG = LoggerFactory.getLogger(ConfigUtil.class);

    public static CompositeConfiguration config = null;
    public static final String CONFIG_FILE = "config.properties";
    public static final String CONFIG_FILE_ALARM = "alarm-config.xml";
    public static final String CONFIG_FILE_LEVEL = "level-config.xml";
    //public static final String LOG_FILE = "logback.xml";

    private static String PATH = ConfigUtil.class.getResource("/").getPath();
    //private static String PATH = System.getProperty("user.dir") + File.separator;

    private static List<LevelBean> levelList = null;
    private static List<AlarmBean> alarmList = null;


    public static CompositeConfiguration getAllConfig() {
        if (config == null) {
            synchronized (ConfigUtil.class) {
                if (config == null) {
                    config = new CompositeConfiguration();
                    try {
                        System.out.println(PATH);
                        config.addConfiguration(new PropertiesConfiguration(new File(PATH + CONFIG_FILE)));
                        config.addConfiguration(new XMLConfiguration(new File(PATH + CONFIG_FILE_LEVEL)));
                        config.addConfiguration(new XMLConfiguration(new File(PATH + CONFIG_FILE_ALARM)));

                        Iterator<String> ite = config.getKeys();
                        LOG.info("All config info: ");
                        while (ite.hasNext()) {
                            String key = ite.next();
                            LOG.debug("{} = {}", key, config.getString(key));
                        }
                    } catch (ConfigurationException e) {
                        LOG.error("load " + CONFIG_FILE + " error.", e);
                        System.exit(1);
                    }
                }
            }
        }
        return config;
    }

    public  static List<LevelBean> getLevelConfig() {
        if (levelList == null) {
            List list = getAllConfig().getList("levelBean.level");
            levelList = Lists.newArrayList();
            for (int index = 0; index < list.size(); index++) {
                LevelBean levelBean = new LevelBean();
                levelBean.setLevel(config.getInt("levelBean(" + index + ").level"));
                levelBean.setInterval(config.getInt("levelBean(" + index + ").interval"));
                levelBean.setExplain(config.getString("levelBean(" + index + ").explain"));
                levelList.add(levelBean);
            }
        }
        return levelList;
    }

    public  static List<AlarmBean> getAlarmConfig() {
        if (alarmList == null) {
            List list = getAllConfig().getList("alarmBean.serviceTag");
            alarmList = Lists.newArrayList();
            for (int index = 0; index < list.size(); index++) {
                AlarmBean alarmBean = new AlarmBean();
                alarmBean.setLevel(config.getInt("alarmBean(" + index + ").level"));
                String interval = config.getString("alarmBean(" + index + ").interval");
                if (interval == null || "".equals(interval.trim())) {
                    List<LevelBean> levelList = getLevelConfig();
                    for (LevelBean levelBean : levelList) {
                        if (alarmBean.getLevel() == levelBean.getLevel()) {
                            alarmBean.setInterval(levelBean.getInterval());
                            break;
                        }
                    }
                }else {
                    alarmBean.setInterval(Integer.valueOf(interval));
                }
                alarmBean.setServiceTag(config.getString("alarmBean(" + index + ").serviceTag"));
                alarmBean.setServiceName(config.getString("alarmBean(" + index + ").serviceName"));

                String[] emailRec = config.getStringArray("alarmBean(" + index + ").email.recipients");
                LOG.debug("{} email to {}", alarmBean.getServiceTag(), emailRec);
                if (emailRec != null  && emailRec.length > 0) {
                    ReceiveEmail receiveEmail = new ReceiveEmail();
                    receiveEmail.setRecipients(emailRec);
                    String cc = config.getString("alarmBean(" + index + ").email.cc");
                    receiveEmail.setCc(cc);
                    alarmBean.setReceiveEmail(receiveEmail);
                }
                String[] phoneRec = config.getStringArray("alarmBean(" + index + ").phone.recipients");
                if (phoneRec != null && phoneRec.length > 0) {
                    ReceivePhone receivePhone = new ReceivePhone();
                    receivePhone.setRecipients(phoneRec);
                    alarmBean.setReceivePhone(receivePhone);
                }

                String status = config.getString("alarmBean(" + index + ").status");
                String intervalValid = config.getString("alarmBean(" + index + ").intervalValid");
                if (status != null && !"".equals(status.trim())) {
                    alarmBean.setStatus(Integer.valueOf(status));
                }
                if (intervalValid != null && !"".equals(intervalValid.trim())) {
                    alarmBean.setIntervalValid(Boolean.valueOf(intervalValid));
                }
                alarmList.add(alarmBean);
            }
        }
        return alarmList;
    }

    public static void main(String[] args) throws Exception {
        //getLevelConfig();
        List<AlarmBean> list = getAlarmConfig();
        for (AlarmBean alarmBean : list) {
            System.out.println(alarmBean.getServiceTag() + ", " + alarmBean.getLevel() + ", send to " + alarmBean.getReceiveEmail().getRecipients());
        }
    }
}
