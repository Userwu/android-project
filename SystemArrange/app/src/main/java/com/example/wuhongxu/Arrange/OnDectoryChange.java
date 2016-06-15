package com.example.wuhongxu.Arrange;

/**
 * Created by wuhongxu on 2015/10/8.
 */
public interface OnDectoryChange {
    public void addItem(DectoryValue dv, int position);

    public void removeItem(int tag);

    public void choosePath(String dectoryName);

    public void sendDToast(String s);
}
