package com.tomato.monitor.commons.utils;

import ch.qos.logback.core.joran.spi.JoranException;
import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

/**
 * User: wangrun
 * Date: 2016/7/27 11:50
 */
public class GlobalConfigUtil {

    private static final Logger LOG = LoggerFactory.getLogger(GlobalConfigUtil.class);

    private static CompositeConfiguration globalConfig = null;
    public static final String CONFIG_FILE = "config" + File.separator + "config.properties";
    public static final String CONFIG_FILE_DEFAULT = "config.properties";
    public static final String LOG_FILE = "config" + File.separator + "logback.xml";

    public static String PATH = System.getProperty("user.dir") + File.separator;
    //public static String PATH_PARENT = PATH.substring(0, PATH.substring(0, PATH.length() - 1).lastIndexOf(File.separator));

    public static CompositeConfiguration getConfig() {
        return initConfig();
    }

    /**
     * 全局配置和自身配置合并，使用全局配置
     *
     * @param configuration 自身配置
     * @return
     */
    public static Configuration getConfig(Configuration configuration) {
        Iterator<String> iterator = getConfig().getKeys();
        while (iterator.hasNext()) {
            String key = iterator.next();
            configuration.setProperty(key, globalConfig.getString(key));
        }
        return configuration;
    }

    private static CompositeConfiguration initConfig() {
        if (globalConfig == null) {
            synchronized (GlobalConfigUtil.class) {
                if (globalConfig == null) {
                    globalConfig = new CompositeConfiguration();
                    try {
                        File f1 = new File(CONFIG_FILE_DEFAULT);
                        if (f1.exists()) {
                            Configuration defaultConfig = new PropertiesConfiguration(f1);
                            globalConfig.addConfiguration(defaultConfig);
                        } else {
                           /* f1 = new File(PATH_PARENT + CONFIG_FILE_DEFAULT);
                            if (f1.exists()) {
                                Configuration defaultConfig = new PropertiesConfiguration(f1);
                                globalConfig.addConfiguration(defaultConfig);
                            }*/
                        }
                        File f2 = new File(CONFIG_FILE);
                        if (f2.exists()) {
                            Configuration configuration = new PropertiesConfiguration(f2);
                            globalConfig.addConfiguration(configuration);
                        } else {
                            /*f2 = new File(PATH_PARENT + CONFIG_FILE);
                            if (f2.exists()) {
                                Configuration configuration = new PropertiesConfiguration(f2);
                                globalConfig.addConfiguration(configuration);
                            }*/
                        }
                    } catch (ConfigurationException e) {
                        LOG.error("load " + CONFIG_FILE + " error.", e);
                        System.exit(1);
                    }
                }
            }
        }
        return globalConfig;
    }

    public static void reloadLogConfig() {
        try {
            File file = new File(PATH + LOG_FILE);
            if (file.exists()) {
                LogBackConfigLoader.load(PATH + LOG_FILE);
            } else {
                //LogBackConfigLoader.load(PATH_PARENT + LOG_FILE);
            }
        } catch (IOException e) {
            LOG.error("load " + PATH + LOG_FILE + " error.", e);
            System.exit(1);
        } catch (JoranException e) {
            LOG.error("load " + PATH + LOG_FILE + " error.", e);
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        System.out.println(PATH);
        System.out.println(PATH.substring(0, PATH.substring(0, PATH.length() - 1).lastIndexOf(File.separator)));
    }
}
