package com.sinosoft.ops.cimp.util.word.pattern.xinjiang;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.newskysoft.iimp.word.service.ExportService;
import com.newskysoft.iimp.word.util.StringUtil;
/**
 * 
 * @classname:  SartySecretaryAttrValue
 * @description: 党组织书记姓名
 * @author:        zhangxp
 * @date:            2017年12月4日 下午1:01:36
 * @version        1.0.0
 */
public class SartySecretaryAttrValue implements AttrValue {
    private final String key = "partySecretary";
    private final int order = 7;

    @Override
    public Object getAttrValue(Map<String, Object> attrValueContext, String partyId,ExportService exportWordService){
//    	String partyId = OrganizationFullNameAttrValue.getPartyId();
    	Map d001Map = (Map) attrValueContext.get("D001");
        final String partySecretaryInfoSql = "SELECT D001024 FROM Party_D001 WHERE PARTY_ID = '%s'  and status = 0 ";
        if (d001Map != null) {
            String partySecretary = StringUtil.obj2Str(d001Map.get("D001024"));
            if (StringUtils.isEmpty(partySecretary)) {
                partySecretary = this.getPartySecretary(partySecretaryInfoSql, partyId, exportWordService);
            }
            return partySecretary;
        } else {
            String partySecretary = this.getPartySecretary(partySecretaryInfoSql, partyId, exportWordService);
            return partySecretary;
        }
    }

    private String getPartySecretary(String sql, String depId,ExportService exportWordService){
        String attrInfoSql = String.format(sql, depId);
        List attrInfoList = exportWordService.findBySQL(attrInfoSql);
        if (attrInfoList != null && attrInfoList.size() > 0) {
            Map map = (Map) attrInfoList.get(0);
            if (map != null) {
                return StringUtil.obj2Str(map.get("D001024"));
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
        return "党组织书记姓名";
    }
}
