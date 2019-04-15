package com.sinosoft.ops.cimp.util;

import java.util.List;
import java.util.Map;

import com.sinosoft.ops.cimp.service.word.ExportService;
import org.apache.commons.lang3.StringUtils;


/**
 * @ClassName: CodeTranslateUtil
 * @Description: 根据给出的代码类型和代码值转换为对应的中文汉字信息
 * @Author: y
 * @Date: 2017年11月15日 下午6:27:15
 * @Version 1.0.0
 */
public class CodeTranslateUtil {

    private CodeTranslateUtil() {

    }

    /**
     * 根据给出的代码类型和代码值转换为对应的中文汉字信息
     *
     * @param codeType
     * @param code
     * @param exportWordService
     * @return
     */
    public static String codeToName(String codeType, String code, ExportService exportWordService) {
        try {
            final String sql = "SELECT SYS_CODE_SET.ID FROM  SYS_CODE_SET SYS_CODE_SET  WHERE SYS_CODE_SET.NAME = '"
                    + codeType + "'";
            List list = exportWordService.findBySQL(sql);
            Integer id = Integer.parseInt(StringUtil.obj2Str(((Map) list.get(0)).get("ID")));
            final String sqlcode = "SELECT SYS_CODE_ITEM.CODE,SYS_CODE_ITEM.DESCRIPTION,BRIEF_NAME FROM SYS_CODE_ITEM SYS_CODE_ITEM  WHERE SYS_CODE_ITEM.CODE_SET_ID = "
                    + id + " AND   SYS_CODE_ITEM.CODE = '" + code + "'";
            if (StringUtils.isNotEmpty(code)) {
                List listcode = exportWordService.findBySQL(sqlcode);
                return listcode == null || listcode.size() == 0 ? ""
                        : StringUtil.obj2Str(((Map) listcode.get(0)).get("BRIEF_NAME"));
            } else {
                return "";
            }
        } catch (Exception e) {
            return "";
        }
    }
}
