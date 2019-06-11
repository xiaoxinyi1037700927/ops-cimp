package com.sinosoft.ops.cimp.util.word.pattern.xinjiang;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.newskysoft.iimp.word.service.ExportService;
import com.newskysoft.iimp.word.util.CodeTranslateUtil;
import com.newskysoft.iimp.word.util.StringUtil;
/**
 * 
 * @classname:  PartyCategoryAttrValue
 * @description: 党组织类别
 * @author:        zhangxp
 * @date:            2017年12月4日 下午12:51:36
 * @version        1.0.0
 */
public class PartyCategoryAttrValue implements AttrValue {
    private final String key = "partyCategory";
    private final int order = 6;

    @Override
    public Object getAttrValue(Map<String, Object> attrValueContext, String partyId,ExportService exportWordService){
//    	String partyId = OrganizationFullNameAttrValue.getPartyId();
    	Map d001Map = (Map) attrValueContext.get("D001");
        final String partyCategoryInfoSql = "SELECT D001010 FROM Party_D001 WHERE PARTY_ID = '%s'  and status = 0 ";
        if (d001Map != null) {
            String partyCategoryCode = StringUtil.obj2Str(d001Map.get("D001010"));
            if (StringUtils.isEmpty(partyCategoryCode)) {
                partyCategoryCode = this.getOrganizationCategoryCode(partyCategoryInfoSql, partyId, exportWordService);
            }
            String partyCategory = "";
            if (StringUtils.isNotEmpty(partyCategoryCode)) {
                partyCategory = CodeTranslateUtil.codeToName("BT0405", partyCategoryCode, exportWordService) ;
            }
            return partyCategory;
        } else {
            String partyCategoryCode = this.getOrganizationCategoryCode(partyCategoryInfoSql, partyId, exportWordService);
            String partyCategory = "";
            if (StringUtils.isNotEmpty(partyCategoryCode)) {
               partyCategory = CodeTranslateUtil.codeToName("BT0405", partyCategoryCode, exportWordService) ;
            }
            return partyCategory;
        }
    }

    private String getOrganizationCategoryCode(String sql, String depId,ExportService exportWordService){
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
    
    @Override
    public String getTitle() {
        return "党组织类别";
    }    
}
