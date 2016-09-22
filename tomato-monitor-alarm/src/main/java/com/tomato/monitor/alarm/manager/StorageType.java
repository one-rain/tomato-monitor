package com.tomato.monitor.alarm.manager;

/**
 * User: wangrun
 * Date: 2016/9/5 11:21
 */
public enum StorageType {

    /**
     * 没有任何匹配的监听类型
     */
    OTHER(null),

    /**
     * 默认为内存.
     *
     * @see com.tomato.monitor.alarm.manager.StorageMemory
     */
    DEFAULT("com.tomato.monitor.alarm.manager.StorageMemory"),

    /**
     * 内存存储.
     *
     * @see com.tomato.monitor.alarm.manager.StorageMemory
     */
    MEMORY("com.tomato.monitor.alarm.manager.StorageMemory"),

    /**
     * 数据库存储
     *
     * @see com.tomato.monitor.alarm.manager.StorageDB
     */
    DB("com.tomato.monitor.alarm.manager.StorageDB");

    private final String storageTypeName;

    public String getStorageTypeName() {
        return storageTypeName;
    }

    private StorageType(String storageTypeName) {
        this.storageTypeName = storageTypeName;
    }
}
