package com.sinosoft.ops.cimp.util.word.pattern.xinjiang;

import com.sinosoft.ops.cimp.service.export.ExportService;
import com.sinosoft.ops.cimp.util.CodeTranslateUtil;
import com.sinosoft.ops.cimp.util.StringUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;


/**
 * 
 * @classname:  OrganizationCategoryAttrValue
 * @description: 单位机构类别
 * @author:        zhangxp
 * @date:            2017年12月4日 上午1:51:28
 * @version        1.0.0
 */
public class OrganizationCategoryAttrValue  implements AttrValue{
    private final String key = "organizationCategory";
    private final int order = 4;

    @Override
    public Object getAttrValue(Map<String, Object> attrValueContext, String depId,ExportService exportWordService){
        Map b001Map = (Map) attrValueContext.get("B001");
        final String organizationCategoryInfoSql = "SELECT B001075 FROM Dep_B001 WHERE Dep_Id = '%s'  and status = 0 ";
        if (b001Map != null) {
            String organizationCategoryCode = StringUtil.obj2Str(b001Map.get("B001075"));
            if (StringUtils.isEmpty(organizationCategoryCode)) {
                organizationCategoryCode = this.getOrganizationCategoryCode(organizationCategoryInfoSql, depId, exportWordService);
            }
            String organizationCategory = "";
            if (StringUtils.isNotEmpty(organizationCategoryCode)) {
                organizationCategory = CodeTranslateUtil.codeToName("BT0375", organizationCategoryCode, exportWordService) ;
            }
            return organizationCategory;
        } else {
            String organizationCategoryCode = this.getOrganizationCategoryCode(organizationCategoryInfoSql, depId, exportWordService);
            String organizationCategory = "";
            if (StringUtils.isNotEmpty(organizationCategoryCode)) {
               organizationCategory = CodeTranslateUtil.codeToName("BT0375", organizationCategoryCode, exportWordService) ;
            }
            return organizationCategory;
        }
    }

    private String getOrganizationCategoryCode(String sql, String depId,ExportService exportWordService){
        String attrInfoSql = String.format(sql, depId);
        List attrInfoList = exportWordService.findBySQL(attrInfoSql);
        if (attrInfoList != null && attrInfoList.size() > 0) {
            Map map = (Map) attrInfoList.get(0);
            if (map != null) {
                return StringUtil.obj2Str(map.get("B001075"));
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
        return "单位机构类别";
    }        
}
