package com.example.tetris20;

import android.app.Activity;

import java.util.HashSet;

public class ActivityManager {

    private static HashSet<Activity> hashSet = new HashSet<Activity>();

    private static ActivityManager instance = new ActivityManager();;

    private ActivityManager() {}

    public static ActivityManager getInstance() {
        return instance;
    }

    /**
     * 每一个Activity 在 onCreate 方法的时候，可以装入当前this
     * in onCreate() for every Activity, add addActivity(this)
     * @param activity
     */
    public void addActivity(Activity activity) {
        try {
            hashSet.add(activity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void exit() {
        try {
            for (Activity activity : hashSet) {
                if (activity != null)
                    activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }

}