package com.sinosoft.ops.cimp.common.word.data;

import java.util.List;
import java.util.Map;

import com.sinosoft.ops.cimp.service.word.ExportService;
import com.sinosoft.ops.cimp.util.CodeTranslateUtil;
import com.sinosoft.ops.cimp.util.StringUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * @version 1.0.0
 * @classname: PartyCategoryAttrValue
 * @description: 党组织类别
 * @author: zhangxp
 * @date: 2017年12月4日 下午12:51:36
 */
public class PartyCategoryAttrValue implements AttrValue {
    private final String key = "partyCategory";
    private final int order = 6;

    @Override
    public Object getAttrValue(Map<String, Object> attrValueContext, String partyId, ExportService exportWordService) {
//    	String partyId = OrganizationFullNameAttrValue.getPartyId();
        Map d001Map = (Map) attrValueContext.get("D001");
        final String partyCategoryInfoSql = "SELECT D001010 FROM Party_D001 WHERE PARTY_ID = '%s'";
        if (d001Map != null) {
            String partyCategoryCode = StringUtil.obj2Str(d001Map.get("D001010"));
            if (StringUtils.isEmpty(partyCategoryCode)) {
                partyCategoryCode = this.getOrganizationCategoryCode(partyCategoryInfoSql, partyId, exportWordService);
            }
            String partyCategory = "";
            if (StringUtils.isNotEmpty(partyCategoryCode)) {
                partyCategory = CodeTranslateUtil.codeToName("BT0405", partyCategoryCode, exportWordService);
            }
            return partyCategory;
        } else {
            String partyCategoryCode = this.getOrganizationCategoryCode(partyCategoryInfoSql, partyId, exportWordService);
            String partyCategory = "";
            if (StringUtils.isNotEmpty(partyCategoryCode)) {
                partyCategory = CodeTranslateUtil.codeToName("BT0405", partyCategoryCode, exportWordService);
            }
            return partyCategory;
        }
    }

    private String getOrganizationCategoryCode(String sql, String depId, ExportService exportWordService) {
        String attrInfoSql = String.format(sql, depId);
        List attrInfoList = exportWordService.findBySQL(attrInfoSql);
        if (attrInfoList != null && attrInfoList.size() > 0) {
            Map map = (Map) attrInfoList.get(0);
            if (map != null) {
                return StringUtil.obj2Str(map.get("D001010"));
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
}
