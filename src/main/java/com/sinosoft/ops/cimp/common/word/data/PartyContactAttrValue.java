package com.sinosoft.ops.cimp.common.word.data;

import java.util.List;
import java.util.Map;

import com.sinosoft.ops.cimp.service.word.ExportService;
import com.sinosoft.ops.cimp.util.StringUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * @version 1.0.0
 * @classname: PartyContactAttrValue
 * @description: 党组织联系人
 * @author: zhangxp
 * @date: 2017年12月4日 下午5:57:38
 */
public class PartyContactAttrValue implements AttrValue {
    private final String key = "partyContact";
    private final int order = 8;

    @Override
    public Object getAttrValue(Map<String, Object> attrValueContext, String partyId, ExportService exportWordService) {
//    	String partyId = OrganizationFullNameAttrValue.getPartyId();
        Map d001Map = (Map) attrValueContext.get("D001");
        final String partyContactInfoSql = "SELECT D001025 FROM Party_D001 WHERE PARTY_ID = '%s'";
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

    private String getPartyContact(String sql, String depId, ExportService exportWordService) {
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
}
