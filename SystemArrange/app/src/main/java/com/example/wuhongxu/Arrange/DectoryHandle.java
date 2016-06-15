package com.example.wuhongxu.Arrange;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuhongxu on 2015/10/8.
 */
public class DectoryHandle {
    private OnDectoryChange context;
    private File sdcard;
    private List<DectoryValue> dectoryValueList;
    private String TAG = "DectoryHandle";

    public DectoryHandle() {
        init();
    }

    public DectoryHandle(OnDectoryChange c) {
        context = c;
        init();
    }

    public void init() {
        sdcard = Environment.getExternalStorageDirectory();
        dectoryValueList = new ArrayList<DectoryValue>();
    }

    public void startDHandle() {
        init();
        DectoryValue d = new DectoryValue();
        d.name = sdcard.getName();
        d.path = sdcard.getPath();
        d.level = 0;
        d.tag = 0;
        dectoryValueList.add(d);
        context.addItem(d, 0);
    }

    public void openDectory(File f, int level, int tag) {
        int position = tag;
        if (f.isFile())
            return;
        if (position + 1 < dectoryValueList.size() && dectoryValueList.get(position + 1).level > level) {
            closeDectory(tag);
            return;
        }

        File[] fs = f.listFiles();
        Log.e(TAG, "openDectory " + f.getName());
        for (File fi : fs) {
            DectoryValue d = new DectoryValue();
            d.name = fi.getName();
            d.path = fi.getPath();
            d.level = level + 1;
            dectoryValueList.add(++position, d);
            context.addItem(d, position);
        }
    }

    public void closeDectory(int position) {
        int tag = position;
        DectoryValue dv = dectoryValueList.get(tag);
        Log.e(TAG, "closeDectory " + dv.name);
        int level = dv.level;
        dv = dectoryValueList.get(++tag);
        while (dv.level > level) {
            context.removeItem(tag);
            dectoryValueList.remove(tag);
            if (tag >= dectoryValueList.size())
                return;
            dv = dectoryValueList.get(tag);
        }
    }

    public void chooseDectory(int tag) {
        DectoryValue dv = dectoryValueList.get(tag);
        File f = new File(dv.path);
        openDectory(f, dv.level, tag);

        if (f.isDirectory()) {
            context.choosePath(dv.path);
            return;
        }
        int level = dv.level;
        for (int i = tag; i >= 0; i--) {
            dv = dectoryValueList.get(tag);
            if (dv.level < level) {
                context.choosePath(dv.path);
                return;
            }
        }
    }
}
