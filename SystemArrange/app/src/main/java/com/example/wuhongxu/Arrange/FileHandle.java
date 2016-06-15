package com.example.wuhongxu.Arrange;

import android.os.Environment;
import android.util.Log;

import com.example.wuhongxu.Structure.FileStack;
import com.example.wuhongxu.ValueEnum.Value;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuhongxu on 2015/10/8.
 */
public class FileHandle implements Runnable {
    private String attribute;
    private boolean isPause;
    private boolean isClose;

    private int sleepTime;//sleepTime用于当处于后台运行是，使用较少的资源
    private String TAG = "FileHandle";
    private List<File> fileList;
    private List<String> pathList;
    private OnSearchChange c;

    //各种搜索设置
    private boolean isFront, isDectory, isFile;
    private int minSize, maxSize;
    private long changeTime;

    public FileHandle() {
        init();
    }

    public FileHandle(OnSearchChange context) {
        c = context;
        init();

    }

    public void init() {
        isPause = false;
        isClose = false;
        sleepTime = 500;

        isFront = false;
        isDectory = false;
        isFile = true;
        minSize = Value.NO_EFFECT;
        maxSize = Value.MAX_INTEGER;
        changeTime = Value.LOW_LEVEL;


    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public boolean getIsPause() {
        return isPause;
    }

    public void setIsPause(boolean isPause) {
        this.isPause = isPause;
    }

    public void closeSearch() {
        isClose = true;
    }

    private void setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime;
    }

    public List<File> getFiles() {
        return fileList;
    }

    public void startSearch() {
        Log.e(TAG, "startSearch ");
        isClose = false;
        fileList = new ArrayList<File>();//用于存储被选中的文件
        pathList = new ArrayList<String>();//用于存储所有搜索结果路径
        new Thread(this).start();
    }

    //清楚所有已经选中的文件
    public void clearCheck() {
        fileList.clear();
    }

    //设置搜索条件
    public void setSearchCondition(boolean isFront, boolean isDectory, boolean isFile,
                                   int minSize, int maxSize,
                                   long changeTime) {
        this.isFront = isFront;
        this.isDectory = isDectory;
        this.isFile = isFile;
        this.minSize = minSize;
        this.maxSize = maxSize;
        this.changeTime = changeTime;
    }

    public void setSearchCondition(boolean isFront, boolean isDectory, boolean isFile) {
        setSearchCondition(isFront, isDectory, isFile, Value.NO_EFFECT, Value.MAX_INTEGER, Value.NO_EFFECT);
    }

    public void setSearchCondition(int minSize, int maxSize) {
        setSearchCondition(true, true, true, minSize, maxSize, Value.NO_EFFECT);
    }

    public void setSearchCondition(boolean isFront) {
        setSearchCondition(isFront, true, true, Value.NO_EFFECT, Value.MAX_INTEGER, Value.NO_EFFECT);
    }

    public boolean checkName(String name, String attribute) {
        if (isFront) {
            if (name.startsWith(attribute))
                return true;
            return false;
        }
        if (name.endsWith(attribute))
            return true;
        return false;
    }

    public void changeFileName(String name, int tag) {
        File oldf = new File(pathList.get(tag));
        if (oldf.getName().equals(name))
            return;
        File f = new File(handlePath(oldf.getPath()) + name);
        if (f.exists()) {
            c.sendFToast("文件名已存在！");
            return;
        }


        //刷新UI
        c.reFreshItem(name, f.getPath(), f.length(), tag, f.isFile());

        //路径列表中更新
        pathList.set(tag, f.getPath());

        //在已选中文件列表中更新
        for (int i = 0; i < fileList.size(); i++) {
            if (oldf.getPath().equals(fileList.get(i).getPath())) {
                fileList.set(i, f);
                break;
            }
        }

        oldf.renameTo(f);
    }

    //路径处理函数，用于去除最后一层
    public String handlePath(String p) {
        String path = new String();
        path = p;
        int i = path.length() - 1;
        for (; i >= 0; i++) {
            if (path.charAt(i) == '/')
                break;
        }
        if (i == 0)
            return null;
        path.substring(0, ++i);
        if (path.equals(""))
            return null;
        return path;
    }

    public void chooseFile(int tag) {
        File f = new File(pathList.get(tag));
        if (c.chooseItem(tag)) {
            Log.e(TAG, "chooseFile add  " + f.getName() + "   " + pathList.size());
            if (fileList.indexOf(f) == -1)
                fileList.add(f);
        } else {
            Log.e(TAG, "chooseFile remove  " + f.getName() + "   " + pathList.size());
            if (fileList.indexOf(f) != -1)
                fileList.remove(f);
            if (fileList.isEmpty())
                c.onChooseEmpty();
        }

    }

    public void change(boolean isMove, String path) {
        Log.e(TAG, "change FileList ===" + fileList.size());
        new ChangeFile(path, fileList, isMove, c);
    }

    @Override
    public void run() {
        File sdcard = Environment.getExternalStorageDirectory();
        if (!sdcard.exists()) {
            c.sendFToast("没有sd卡");
            return;
        }
        c.loading("搜索中");
        int tagg = 0;
        FileStack files = new FileStack();
        files.push(sdcard);
        int rootLength = 0, tm = 0;
        while (!isClose && !files.isEmpty()) {
            if (isPause) {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }
            tm++;
            if (files.getLength() <= rootLength) {
                rootLength--;
                c.addProgress();
            }
            File root = files.pop();
            if (root.isDirectory()) {
                File[] fs = root.listFiles();
                for (int i = 0; i < fs.length; i++) {
                    files.push(fs[i]);
                }
                if (tm == 1) {
                    rootLength = fs.length;
                }
            }

            if (isFile && root.isFile())
                if (checkName(root.getName(), attribute) && root.length() > minSize && root.length() < maxSize && root.lastModified() > changeTime) {
                    c.addItem(root.getName(), root.getPath(), root.length(), tagg++, isFile);
                    pathList.add(root.getPath());
                } else if (isDectory && root.isDirectory())
                    if (checkName(root.getName(), attribute) && root.length() > minSize && root.length() < maxSize && root.lastModified() > changeTime) {
                        c.addItem(root.getName(), root.getPath(), root.length(), tagg++, isFile);
                        pathList.add(root.getPath());
                    }

        }
        init();
        c.sendFToast("扫描完成");
        c.closeLoading();
    }

}
