package com.boha.proximity.cms;

import java.io.Serializable;
import java.util.List;

/**
 * Created by aubreyM on 2014/08/13.
 */
public class FileNames implements Serializable {
    public FileNames(List<String> list) {
        this.fileNames = list;
    }
    private List<String> fileNames;

    public List<String> getFileNames() {
        return fileNames;
    }

    public void setFileNames(List<String> fileNames) {
        this.fileNames = fileNames;
    }
}
