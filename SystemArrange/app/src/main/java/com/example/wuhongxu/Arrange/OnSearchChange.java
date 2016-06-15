package com.example.wuhongxu.Arrange;

/**
 * Created by wuhongxu on 2015/10/8.
 */
public interface OnSearchChange {
    public void addItem(String name, String path, long size, int tag, boolean isFile);

    public void removeItem(int tag);

    public void sendFToast(String content);

    public boolean chooseItem(int tag);

    public void loading(String reason);

    public void closeLoading();

    public void reFreshItem(String name, String path, long size, int tag, boolean isFile);

    public void onChooseEmpty();

    public void addProgress();
}
