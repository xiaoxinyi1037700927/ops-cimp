package com.sinosoft.ops.cimp.util;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import org.apache.commons.lang3.StringUtils;


import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by rain chen on 2017/8/14.
 *
 * @author rain chen
 * @version 1.0
 * @since 1.0
 * Copyright (C) 2017. SinSoft All Rights Received
 */
public class MultiZipUtil {

    //zip文件后缀名
    public static final String ZIP_FILE_SUFFIX = ".zip";

    public static final int FILE_NAME = 0;
    public static final int FOLDER_NAME = 1;
    private static ZipParameters parameters = new ZipParameters();

    public static void zip(String zip, ArrayList<String> files) throws ZipException {
        zip(zip, files, true);
    }

    public static void zip(String zip, ArrayList<String> files, boolean coverFlag) throws ZipException {
        zip = getZipPath(zip, coverFlag);
        ArrayList<ArrayList<File>> fileArrays = getFileToZip(files);
        ArrayList<File> fileList = (ArrayList) fileArrays.get(0);
        ZipFile zipFile = addZipFile(zip, fileList, parameters);
        ArrayList<File> fileFolderList = (ArrayList) fileArrays.get(1);
        if (fileFolderList.size() > 0) {
            addZipFolders(zipFile, fileFolderList);
        }
    }

    private static ZipFile addZipFolders(ZipFile zipFile, ArrayList<File> files) {
        if (files != null) {
            for (File file : files) {
                try {
                    zipFile.addFolder((File) file, parameters);
                } catch (ZipException var5) {
                    throw new RuntimeException("添加到Zip文件失败！");
                }
            }
        }
        return zipFile;
    }

    public static void addFileToZip(String zipPath, ArrayList<String> filePaths, String toPath) {
        ArrayList<File> files = (ArrayList) getFileToZip(filePaths).get(0);
        ZipParameters zipParameters = new ZipParameters();
        zipParameters.setCompressionMethod(8);
        zipParameters.setCompressionLevel(5);
        zipParameters.setRootFolderInZip(toPath);
        addZipFile(zipPath, files, zipParameters);
    }

    public static void addFilesToZip(String zipPath, Map<String, ArrayList<String>> fileMaps) {
        if (fileMaps != null && fileMaps.size() != 0) {
            try {
                ZipFile zipFile = new ZipFile(getZipPath(zipPath, false));
                ZipParameters zipParameters = new ZipParameters();
                zipParameters.setCompressionMethod(8);
                zipParameters.setCompressionLevel(5);

                for (Object o : fileMaps.entrySet()) {
                    Map.Entry entry = (Map.Entry) o;
                    String toPath = StringUtil.obj2Str(entry.getKey());
                    ArrayList<String> filePaths = (ArrayList) entry.getValue();
                    zipParameters.setRootFolderInZip(toPath);
                    zipFile.addFiles((ArrayList) getFileToZip(filePaths).get(0), zipParameters);
                }

            } catch (ZipException e) {
                throw new RuntimeException("添加文件到Zip文件失败", e);
            }
        } else {
            throw new RuntimeException("添加到Zip包文件为空");
        }
    }

    public static void addFileToZip(String zip, String filePath, String toZipFile) {
        try {
            zipStream(zip, new FileInputStream(filePath), toZipFile);
        } catch (Exception e) {
            throw new RuntimeException("添加压缩文件到文件流失败");
        }
    }

    public static void zipStream(String zip, InputStream inputStream, String toFile) throws ZipException {
        ZipFile zipFile = new ZipFile(zip);
        ZipParameters parameters = new ZipParameters();
        parameters.setCompressionMethod(8);
        parameters.setFileNameInZip(toFile);
        parameters.setSourceExternalStream(true);
        zipFile.addStream(inputStream, parameters);
    }

    public static void addToZip(String zip, ArrayList<String> files) {
        try {
            ZipFile zipFile = new ZipFile(zip);
            addZipFolders(zipFile, (ArrayList<File>) getFileToZip(files).get(1));
        } catch (ZipException e) {
            throw new RuntimeException("添加文件到Zip文件失败");
        }
    }

    private static ArrayList<ArrayList<File>> getFileToZip(List<String> files) {
        ArrayList<ArrayList<File>> arrayLists = new ArrayList<ArrayList<File>>();
        if (files != null && files.size() != 0) {
            int fileCount = files.size();
            ArrayList<File> filesToAdd = new ArrayList<File>(fileCount);
            ArrayList<File> foldersToAdd = new ArrayList<File>(fileCount);

            for (String file1 : files) {
                File file = new File(file1);
                if (file.isDirectory()) {
                    foldersToAdd.add(file);
                } else {
                    filesToAdd.add(file);
                }
            }

            arrayLists.add(0, filesToAdd);
            arrayLists.add(1, foldersToAdd);
            return arrayLists;
        } else {
            throw new RuntimeException("添加压缩文件为空");
        }
    }

    private static ZipFile addZipFile(String zip, ArrayList<File> files, ZipParameters zipParameters) {
        try {
            if (zipParameters == null) {
                zipParameters = parameters;
            }

            ZipFile zipFile = new ZipFile(zip);
            zipFile.addFiles(files, zipParameters);
            return zipFile;
        } catch (ZipException e) {
        	e.printStackTrace();
        	throw new RuntimeException("");
        }
    }

    public static void unzip(String zip, String path) {
        try {
            ZipFile zipFile = new ZipFile(zip);
            zipFile.extractAll(path);
        } catch (ZipException e) {
            throw new RuntimeException("解压文件失败");
        }
    }

    public static void unzip(String zip) {
        String unZipPath = zip.substring(0, zip.lastIndexOf("."));
        unzip(zip, unZipPath);
    }

    private static String getZipPath(String zip, boolean coverFlag) {
        if (!zip.endsWith(".zip")) {
            zip = zip + ".zip";
        }

        int lastIndex = zip.lastIndexOf("\\");
        String zipName = zip.substring(lastIndex + 1);
        boolean dateZip = StringUtils.equals(".zip", zipName);
        if (dateZip) {
            zipName = new Date() + zipName;
            zip = zip.substring(0, lastIndex + 1) + zipName;
        }

        boolean b = zip.indexOf("\\") == lastIndex;
        if (!b) {
            String dir = zip.substring(0, lastIndex);
            File zipFile = new File(dir);
            if (!zipFile.exists()) {
                zipFile.mkdirs();
            }
        }

        if (!dateZip) {
            File fileE = new File(zip);
            if (fileE.exists()) {
                if (coverFlag) {
                    fileE.delete();
                } else {
                    int zipSize = zip.length();
                    if (lastIndex < zipSize) {
                        zip = zip.substring(0, lastIndex + 1) + new Date().getTime() + zipName;
                    }
                }
            }
        }

        return zip;
    }

    static {
        parameters.setCompressionMethod(8);
        parameters.setCompressionLevel(5);
    }
}
