package com.tomato.monitor.alarm.bean;

/**
 * User: wangrun
 * Date: 2016/8/25 18:40
 */
public class ReceivePhone {

    private String[] recipients;
    private boolean send;

    public String[] getRecipients() {
        return recipients;
    }

    public void setRecipients(String[] recipients) {
        this.recipients = recipients;
    }

    public boolean isSend() {
        return send;
    }

    public void setSend(boolean send) {
        this.send = send;
    }
}
