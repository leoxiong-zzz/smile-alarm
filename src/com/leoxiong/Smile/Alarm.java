package com.leoxiong.Smile;

/**
 * Created by Leo on 6/8/2014.
 */
public class Alarm {
    private int hour;
    private int minute;
    private String name;
    private boolean enabled;

    public Alarm(int hour, int minute, String name, boolean enabled) {
        this.hour = hour;
        this.minute = minute;
        this.name = name;
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public String getName() {
        return name;
    }
}
