package com.sinosoft.ops.cimp.controller.sys.table;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.Serializable;

public class FileSaveResult implements Serializable {

    private static final long serialVersionUID = 5503607663987266146L;
    private String id;
    private boolean idIsDigest;
    private MultipartFile multipartFile;
    private File targetFile;
    private String key;
    private int index;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isIdIsDigest() {
        return idIsDigest;
    }

    public void setIdIsDigest(boolean idIsDigest) {
        this.idIsDigest = idIsDigest;
    }

    public MultipartFile getMultipartFile() {
        return multipartFile;
    }

    public void setMultipartFile(MultipartFile multipartFile) {
        this.multipartFile = multipartFile;
    }

    public File getTargetFile() {
        return targetFile;
    }

    public void setTargetFile(File targetFile) {
        this.targetFile = targetFile;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public String toString() {
        return "FileSaveResult{" +
                "id='" + id + '\'' +
                ", idIsDigest=" + idIsDigest +
                ", multipartFile=" + multipartFile +
                ", targetFile=" + targetFile +
                ", key='" + key + '\'' +
                ", index=" + index +
                '}';
    }
}