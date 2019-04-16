package com.sinosoft.ops.cimp.common.word.data;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.sinosoft.ops.cimp.service.word.ExportService;
import com.sinosoft.ops.cimp.util.StringUtil;
import org.hibernate.type.descriptor.java.UUIDTypeDescriptor;

/**
 * Created by SML
 * 照片
 * date : 2017/11/8
 * des :
 */
public class PhotoAttrValue implements AttrValue {
    //属性与属性之间的排序，越小越靠前
    private final int order = 0;

    private final String key = "photo";

    @Override
    public Object getAttrValue(Map<String, Object> attrValueContext, String empId, ExportService exportWordService) throws Exception {
        String wordType = (String) attrValueContext.get("wordType");
        String A092010 = "";
        //根据不同类型的word选择不同类型的照片
        switch (wordType) {
            case "xxcjb":
                A092010 = "02";
                break;
            case "gbrmb":
                A092010 = "04";
                break;
        }
        //根据字段查询字段的info_item_id
        String infoItemIdSql = "SELECT ID FROM SYS_INFO_ITEM WHERE NAME = 'a092015'";
        List infoItemIdList = exportWordService.findBySQL(infoItemIdSql);
        String infoItemId = "";
        byte[] buffer = null;
        if (infoItemIdList != null && infoItemIdList.size() > 0) {
            Map infoItemIdMap = (Map) infoItemIdList.get(0);
            infoItemId = StringUtil.obj2Str(infoItemIdMap.get("ID"));
            String subjectIdSql = "SELECT  SUB_ID  FROM EMP_A092 WHERE EMP_ID = '" + empId + "' and  A092010 = '" + A092010 + "'";
            List subjectIdList = exportWordService.findBySQL(subjectIdSql);
            String subjectId = "";
            if (subjectIdList != null && subjectIdList.size() > 0) {
                Map subjectIdMap = (Map) subjectIdList.get(0);
                subjectId = StringUtil.obj2Str(subjectIdMap.get("SUB_ID"));
                String photoIdSql = "SELECT  ID FROM SYS_IMAGE WHERE INFO_ITEM_ID = '" + infoItemId + "' AND SUBJECT_ID = '" + subjectId + "'";
                List photoIdList = exportWordService.findBySQL(photoIdSql);
                String photoId = "";
                if (photoIdList != null && photoIdList.size() > 0) {
                    Map photoIdMap = (Map) photoIdList.get(0);
                    Object photoIdObj = photoIdMap.get("ID");
                    if (photoIdObj instanceof byte[]) {
                        byte[] ba = (byte[]) photoIdObj;
                        if (ba.length == 16) {//UUID，使用hibernate的UUID解析器处理
                            try {
                                photoId = UUIDTypeDescriptor.ToBytesTransformer.INSTANCE.parse(photoIdObj).toString().toUpperCase();
                            } catch (Exception e) {

                            }
                        } else {

                        }
                    }
                    String photoFilePath = StringUtil.obj2Str(attrValueContext.get("photoFilePath"));
                    File photeFile = new File(photoFilePath);


//                    byte[] buffer = null;
                    try {
//                        exportWordService.downLoadPhotoFile(photoId.toLowerCase(), photeFile);
                        FileInputStream fis = new FileInputStream(photeFile);

                        ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);

                        byte[] b = new byte[1000];
                        int n;
                        //每次从fis读1000个长度到b中，fis中读完就会返回-1
                        while ((n = fis.read(b)) != -1) {
                            bos.write(b, 0, n);
                        }
                        fis.close();
                        bos.close();
                        buffer = bos.toByteArray();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        return buffer;
                    }

                }
            }
        }

        return buffer;




    	/*
    	String photoTableSql = "SELECT * FROM emp_photo photo WHERE EMP_ID = '%s'";
        photoTableSql = String.format(photoTableSql, empId);
        List photoTableList = exportWordService.findBySQL(photoTableSql);
        if (photoTableList != null && photoTableList.size() > 0) {
            Map map = (Map) photoTableList.get(0);
            //将photo表的记录放到上下文中
            attrValueContext.put("photo", map);
            //获取图片并返回
            Object file = map.get("PHOTO_FILE");
            return StringUtil.blobToBytes((Blob) file);
        }
        return new byte[0];
        */
    }

    public String getKey() {
        return key;
    }

    @Override
    public int getOrder() {
        return order;
    }
}
