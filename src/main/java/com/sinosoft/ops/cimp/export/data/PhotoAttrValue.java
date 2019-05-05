package com.sinosoft.ops.cimp.export.data;


import com.sinosoft.ops.cimp.export.common.ExportConstant;
import com.sinosoft.ops.cimp.util.StringUtil;

import java.sql.Blob;
import java.util.List;
import java.util.Map;

/**
 * Created by SML
 * date : 2017/11/8
 * des :
 */
public class PhotoAttrValue implements AttrValue {
    //属性与属性之间的排序，越小越靠前
    public static final int ORDER = 0;

    public static final String KEY = "photo";

    @Override
    public Object getAttrValue(Map<String, Object> attrValueContext, String empId) throws Exception {
        try {
            String photoTableSql = "SELECT * FROM emp_photo WHERE EMP_ID = '%s'";
            photoTableSql = String.format(photoTableSql, empId);
            List photoTableList = ExportConstant.exportService.findBySQL(photoTableSql);

            if (photoTableList != null && photoTableList.size() > 0) {
                Map map = (Map) photoTableList.get(0);
                //将photo表的记录放到上下文中
                attrValueContext.put("photo", map);
                //获取图片并返回
                Object file = map.get("PHOTO_FILE");
                return StringUtil.blobToBytes((Blob) file);
            }
            return new byte[0];
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("任免表生成失败:[emp_photo照片]");
        }
    }

    @Override
    public int getOrder() {
        return ORDER;
    }

}
