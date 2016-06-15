package com.example.wuhongxu.Arrange;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChangeFile implements Runnable {
    private String path;
    private List<File> files;
    private boolean isMove;
    private OnSearchChange c;
    private String TAG = "OOOO";

    public ChangeFile() {

    }

    public ChangeFile(String path, List<File> files, boolean isMove, OnSearchChange context) {
        this.path = path;
        this.files = new ArrayList<File>(files);
        this.isMove = isMove;
        this.c = context;
        init();
    }

    private boolean init() {
        new Thread(this).start();

        return true;
    }

    @Override
    public void run() {
        FileInputStream fis;
        FileOutputStream fos;
        c.loading(isMove ? "移动" : "复制" + "中");
        Log.e(TAG, "FileList " + (isMove ? "移动" : "复制") + "  " + files.size() + "->" + path);
        for (int i = 0; i < files.size(); i++) {

            File f = files.get(i);

            File mf = new File(path + "/" + f.getName());

            try {
                fis = new FileInputStream(f);
                if (f.length() < 2147483630) {
                    byte[] b = new byte[(int) f.length()];
                    fis.read(b);
                    mf.createNewFile();
                    fos = new FileOutputStream(mf);
                    fos.write(b);
                } else {
                    //文件过大处理


                    break;
                }

                fos.close();
                ;
                fis.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (isMove)
                f.delete();
        }
        //完成处理
        c.closeLoading();
        c.sendFToast((isMove ? "移动" : "复制") + "完成");


    }
}
