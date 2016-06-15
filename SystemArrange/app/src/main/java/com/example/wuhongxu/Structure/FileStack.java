package com.example.wuhongxu.Structure;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuhongxu on 2015/10/8.
 */


public class FileStack {
    private List<File> files;

    public FileStack() {
        files = new ArrayList<File>();
    }

    public boolean push(File file) {
        return files.add(file);
    }

    public File pop() {
        return files.remove(files.size() - 1);
    }

    public int getLength() {
        return files.size();
    }

    public boolean isEmpty() {
        return files.size() == 0;
    }
}
