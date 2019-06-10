package com.sinosoft.ops.cimp.util.word.pattern.xinjiang;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.newskysoft.iimp.word.service.ExportService;
import com.newskysoft.iimp.word.util.StringUtil;
/**
 * 
 * @classname:  PartyContactCphAttrValue
 * @description: 党组织联系人手机号码
 * @author:        zhangxp
 * @date:            2017年12月4日 下午6:28:57
 * @version        1.0.0
 */
public class PartyContactCphAttrValue implements AttrValue{
    private final String key = "partyContactCph";
    private final int order = 10;

    @Override
    public Object getAttrValue(Map<String, Object> attrValueContext, String partyId,ExportService exportWordService){
//    	String partyId = OrganizationFullNameAttrValue.getPartyId();
    	Map d001Map = (Map) attrValueContext.get("D001");
        final String partyContactCphInfoSql = "SELECT D001027 FROM Party_D001 WHERE PARTY_ID = '%s'  and status = 0 ";
        if (d001Map != null) {
            String partyContactCph = StringUtil.obj2Str(d001Map.get("D001027"));
            if (StringUtils.isEmpty(partyContactCph)) {
                partyContactCph = this.getPartyContactCph(partyContactCphInfoSql, partyId, exportWordService);
            }
            return partyContactCph;
        } else {
            String partyContactCph = this.getPartyContactCph(partyContactCphInfoSql, partyId, exportWordService);
            return partyContactCph;
        }
    }

    private String getPartyContactCph(String sql, String depId,ExportService exportWordService){
        String attrInfoSql = String.format(sql, depId);
        List attrInfoList = exportWordService.findBySQL(attrInfoSql);
        if (attrInfoList != null && attrInfoList.size() > 0) {
            Map map = (Map) attrInfoList.get(0);
            if (map != null) {
                return StringUtil.obj2Str(map.get("D001027"));
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
        return "党组织联系人手机号码";
    }
}
