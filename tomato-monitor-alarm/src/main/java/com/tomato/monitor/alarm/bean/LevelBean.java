package com.tomato.monitor.alarm.bean;

/**
 * User: wangrun
 * Date: 2016/8/25 15:46
 */
public class LevelBean {

    public static final int LEVEL_1 = 1;//1级，最高
    public static final int LEVEL_2 = 2;//2级
    public static final int LEVEL_3 = 3;//3级

    private int level;//等级
    private int interval;//间隔时长，单位秒
    private String explain;//说明

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
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
}
