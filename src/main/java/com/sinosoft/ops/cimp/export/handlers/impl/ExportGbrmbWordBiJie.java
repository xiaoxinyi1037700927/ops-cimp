package com.sinosoft.ops.cimp.export.handlers.impl;

import com.aspose.words.Document;
import com.sinosoft.ops.cimp.context.ExecuteContext;
import com.sinosoft.ops.cimp.export.common.ExportConstant;
import com.sinosoft.ops.cimp.export.handlers.AbstractExportGbrmbBiJie;

import java.util.List;
import java.util.Map;

/**
 * 生成毕节市干部任免表word文件
 */
public class ExportGbrmbWordBiJie extends AbstractExportGbrmbBiJie {

    private String filePath = null;

    public ExportGbrmbWordBiJie(String empId) {
        super(empId);
    }

    @Override
    protected void saveFile(Document doc) throws Exception {
        doc.save(getFilePath());
    }


    /**
     * 获取生成的文件名
     */
    @Override
    public String getFilePath() throws Exception {
        if (filePath == null) {
            List<Map<String, Object>> list = ExportConstant.exportService.findBySQL("select A01001 as \"name\",A001003 as \"cardNo\" from EMP_A001 where EMP_ID = '" + empId + "'");
            if (list == null || list.size() == 0) {
                throw new Exception("获取干部失败！");
            }
            Map<String, Object> map = list.get(0);
            String name = map.get("name").toString();
            String cardNo = map.get("cardNo").toString();

            String baseDir = "";
            Object isTmp = ExecuteContext.getVariable(ExportConstant.IS_TMP);
            if (isTmp == null) {
                baseDir = ExportConstant.EXPORT_BASE_PATH + ExportConstant.EXPORT_GBRMB_WORD;
            } else {
                baseDir = ExportConstant.EXPORT_BASE_PATH + ExportConstant.EXPORT_GBRMB_TMP;
            }

            filePath = baseDir + name + cardNo + ExportConstant.RMB_SUFFIX_WORD;
        }

        return filePath;
    }
}
