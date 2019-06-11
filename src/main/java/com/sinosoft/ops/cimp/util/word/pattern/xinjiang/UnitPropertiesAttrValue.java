package com.sinosoft.ops.cimp.util.word.pattern.xinjiang;

import com.sinosoft.ops.cimp.service.word.ExportService;
import com.sinosoft.ops.cimp.util.CodeTranslateUtil;
import com.sinosoft.ops.cimp.util.StringUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;


/**
 * 
 * @classname:  UnitPropertiesAttrValue
 * @description: 单位性质
 * @author:        zhangxp
 * @date:            2017年12月4日 上午1:40:48
 * @version        1.0.0
 */
public class UnitPropertiesAttrValue  implements AttrValue{
    private final String key = "unitProperties";
    private final int order = 3;

    @Override
    public Object getAttrValue(Map<String, Object> attrValueContext, String depId,ExportService exportWordService){
        Map b001Map = (Map) attrValueContext.get("B001");
        final String unitPropertiesInfoSql = "SELECT B001060 FROM Dep_B001 WHERE Dep_Id = '%s'  and status = 0 ";
        if (b001Map != null) {
            String unitPropertiesCode = StringUtil.obj2Str(b001Map.get("B001060"));
            if (StringUtils.isEmpty(unitPropertiesCode)) {
                unitPropertiesCode = this.getUnitPropertiesCode(unitPropertiesInfoSql, depId, exportWordService);
            }
            String unitProperties = "";
            if (StringUtils.isNotEmpty(unitPropertiesCode)) {
                unitProperties = CodeTranslateUtil.codeToName("BT0365", unitPropertiesCode, exportWordService) ;
            }
            return unitProperties;
        } else {
            String unitPropertiesCode = this.getUnitPropertiesCode(unitPropertiesInfoSql, depId, exportWordService);
            String unitProperties = "";
            if (StringUtils.isNotEmpty(unitPropertiesCode)) {
               unitProperties = CodeTranslateUtil.codeToName("BT0365", unitPropertiesCode, exportWordService) ;
            }
            return unitProperties;
        }
    }

    private String getUnitPropertiesCode(String sql, String depId,ExportService exportWordService){
        String attrInfoSql = String.format(sql, depId);
        List attrInfoList = exportWordService.findBySQL(attrInfoSql);
        if (attrInfoList != null && attrInfoList.size() > 0) {
            Map map = (Map) attrInfoList.get(0);
            if (map != null) {
                return StringUtil.obj2Str(map.get("B001060"));
            }
        }
        return "";
    }

    public String getKey() {
        return key;
    }

    @Override
    public int getOrder() {
        return order;
    }

    @Override
    public String getTitle() {
        return "单位性质";
    }
}
