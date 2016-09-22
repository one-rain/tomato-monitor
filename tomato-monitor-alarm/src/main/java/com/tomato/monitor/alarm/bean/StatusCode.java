package com.tomato.monitor.alarm.bean;

/**
 * User: wangrun
 * Date: 2016/9/5 14:19
 */
public class StatusCode {

    /**
     * 成功
     */
    public static int SUCCESS = 200;

    /**
     * 成功，未作任何事情
     */
    public static int SUCCESS_NOT_DO = 201;

    /**
     * 不存在
     */
    public static int NOT_EXIST = 400;

    /**
     * 数据格式错误
     */
    public static int ERROR_DATA = 500;

    /**
     * 服务器程序错误
     */
    public static int ERROR_SERVICE = 501;

}
