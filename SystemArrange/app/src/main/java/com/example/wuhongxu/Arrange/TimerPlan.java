package com.example.wuhongxu.Arrange;

/**
 * Created by wuhongxu on 2015/10/11.
 */
public class TimerPlan implements Runnable {
    private int time;
    private OnTimeFinish of;

    public TimerPlan(int time, OnTimeFinish plan) {
        this.time = time;
        this.of = plan;
        new Thread(this).start();
    }

    @Override
    public void run() {
        try {
            Thread.sleep(time);
            of.plan();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
