package com.jd.jarvisdemo1.bean;

/**
 * Created by JarvisDong on 2017/8/20.
 */

public class IntentData {
    public String title;
    public int ids;
    public Class intentTarget;

    public IntentData(String title, int ids, Class intentTarget) {
        this.title = title;
        this.ids = ids;
        this.intentTarget = intentTarget;
    }
}
