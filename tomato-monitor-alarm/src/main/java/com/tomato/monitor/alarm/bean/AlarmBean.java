package com.tomato.monitor.alarm.bean;

/**
 * User: wangrun
 * Date: 2016/8/24 17:45
 */
public class AlarmBean {

    private String serviceTag;//服务标识
    private String serviceName;//服务名称
    private ReceiveEmail receiveEmail;//Email接收
    private ReceivePhone receivePhone;//手机短信接收
    private String content;//内容
    private int level;//报警级别
    private int interval;//连续报警的间隔时间，单位秒
    private int status = 1;//0:停用预警服务，1：启用预警服务
    private boolean intervalValid = false;//频率控制开关，如果为false，interval将失效
    private long timestamp;
    private boolean send = false; //是否发送
    private String ip="";


    public String getServiceTag() {
        return serviceTag;
    }

    public void setServiceTag(String serviceTag) {
        this.serviceTag = serviceTag;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public ReceiveEmail getReceiveEmail() {
        return receiveEmail;
    }

    public void setReceiveEmail(ReceiveEmail receiveEmail) {
        this.receiveEmail = receiveEmail;
    }

    public ReceivePhone getReceivePhone() {
        return this.receivePhone;
    }

    public void setReceivePhone(ReceivePhone receivePhone) {
        this.receivePhone = receivePhone;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isIntervalValid() {
        return intervalValid;
    }

    public void setIntervalValid(boolean intervalValid) {
        this.intervalValid = intervalValid;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isSend() {
        return send;
    }

    public void setSend(boolean send) {
        this.send = send;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
