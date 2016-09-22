package com.tomato.monitor.alarm.bean;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * User: wangrun
 * Date: 2016/9/5 14:16
 */
@XmlRootElement(name = "message")
public class MessageBean {

    private int code;
    private String describe;

    public MessageBean() {
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }
}
