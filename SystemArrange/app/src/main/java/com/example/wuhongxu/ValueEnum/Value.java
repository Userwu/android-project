package com.example.wuhongxu.ValueEnum;

/**
 * Created by wuhongxu on 2015/10/8.
 */
public class Value {
    //UI更新指令
    public static final int ITEM_ADD = 0x7FFFFFFF;
    public static final int ITEM_REMOVE = 0x7FFFFFFD;
    public static final int ITEM_OPEN = 0x7FFFFFFC;

    public static final int CREATE_TOAST = 0x7FFF;

    //线程cpu占用指令
    public static final int LOW_LEVEL = 100;
    public static final int MIDDLE_LEVEL = 50;
    public static final int HIGH_LEVEL = 0;

    public static final int MAX_INTEGER = 0x7FFFFFFF;
    public static final int NO_EFFECT = -1;

    //动画持续时长常量
    public static final int LONG_TIME = 400;
    public static final int SHORT_TIME = 200;

    //是否启用动画效果~~~~
    public static final int ANIMATION_OK = 0x7FFFFFFF;
    public static final int ANIMATION_NO = 0x7FFFFFFE;

    //单个格子宽度
    public static final int ONE_SPACE = 50;

    //bottomFragment
    public static final int MESSAGELAYOUT_SHOW = 0;
    public static final int MESSAGELAYOUT_CLOSE = 1;
    public static final int BTNLAYOUT_SHOW = 2;
    public static final int BTNLAYOUT_CLOSE = 3;
    public static final int CANCELLAYOUT_SHOW = 4;
    public static final int CANCELLAYOUT_CLOSE = 5;
    public static final int LIST_SHOW = 6;
    public static final int LIST_CLOSE = 7;
    public static final int showCancelBtn = 8;

    //SearchResultFragment
    public static final int CLOSEE_EDT_LAYOUT = 20;
    public static final int CLOSE_PROGRESS_LAYOUT = 21;
    public static final int SHOW_EDT_LAYOUT = 22;
    public static final int SHOW_PROGRESS_LAYOUT = 23;

}
