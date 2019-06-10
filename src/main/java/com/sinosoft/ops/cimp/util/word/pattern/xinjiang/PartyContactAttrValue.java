package com.sinosoft.ops.cimp.util.word.pattern.xinjiang;

import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import com.newskysoft.iimp.word.service.ExportService;
import com.newskysoft.iimp.word.util.CodeTranslateUtil;
import com.newskysoft.iimp.word.util.StringUtil;
/**
 * 
 * @classname:  PartyContactAttrValue
 * @description: 党组织联系人
 * @author:        zhangxp
 * @date:            2017年12月4日 下午5:57:38
 * @version        1.0.0
 */
public class PartyContactAttrValue implements AttrValue {
    private final String key = "partyContact";
    private final int order = 8;

    @Override
    public Object getAttrValue(Map<String, Object> attrValueContext, String partyId,ExportService exportWordService){
//    	String partyId = OrganizationFullNameAttrValue.getPartyId();
    	Map d001Map = (Map) attrValueContext.get("D001");
        final String partyContactInfoSql = "SELECT D001025 FROM Party_D001 WHERE PARTY_ID = '%s'  and status = 0 ";
        if (d001Map != null) {
            String partyContact = StringUtil.obj2Str(d001Map.get("D001025"));
            if (StringUtils.isEmpty(partyContact)) {
                partyContact = this.getPartyContact(partyContactInfoSql, partyId, exportWordService);
            }
            return partyContact;
        } else {
            String partyContact = this.getPartyContact(partyContactInfoSql, partyId, exportWordService);
            return partyContact;
        }
    }

    private String getPartyContact(String sql, String depId,ExportService exportWordService){
        String attrInfoSql = String.format(sql, depId);
        List attrInfoList = exportWordService.findBySQL(attrInfoSql);
        if (attrInfoList != null && attrInfoList.size() > 0) {
            Map map = (Map) attrInfoList.get(0);
            if (map != null) {
                return StringUtil.obj2Str(map.get("D001025"));
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
        return "党组织联系人";
    }    
}