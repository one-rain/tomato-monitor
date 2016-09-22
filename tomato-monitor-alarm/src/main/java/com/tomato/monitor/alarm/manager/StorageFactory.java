package com.tomato.monitor.alarm.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: wangrun
 * Date: 2016/9/5 11:10
 */
public class StorageFactory {

    private static Logger LOG = LoggerFactory.getLogger(StorageFactory.class);


    public static StorageManager getStorage(String type) {
        try {
            Class<? extends StorageManager> storageManager = getClass(type);
            return storageManager.newInstance();
        } catch (Exception e) {
            LOG.error("get Storage class error.", e);
        }
        return null;
    }

    private static Class<? extends StorageManager> getClass(String type)
            throws Exception {
        String storageTypeName = type;
        StorageType srcType = StorageType.OTHER;
        try {
            srcType = StorageType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException ex) {
            LOG.debug("storage type {} is a custom type", type);
        }
        if (!srcType.equals(StorageType.OTHER)) {
            storageTypeName = srcType.getStorageTypeName();
        }
        try {
            return (Class<? extends StorageManager>) Class.forName(storageTypeName);
        } catch (Exception ex) {
            throw new Exception("Unable to load source type: " + type
                    + ", class: " + storageTypeName, ex);
        }
    }
}
