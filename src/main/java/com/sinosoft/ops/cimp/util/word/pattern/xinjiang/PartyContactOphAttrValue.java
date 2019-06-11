package com.sinosoft.ops.cimp.util.word.pattern.xinjiang;

import com.sinosoft.ops.cimp.service.export.ExportService;
import com.sinosoft.ops.cimp.util.StringUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;



/**
 * 
 * @classname:  PartyContactOphAttrValue
 * @description: 党组织联系人办公电话号码
 * @author:        zhangxp
 * @date:            2017年12月4日 下午6:14:44
 * @version        1.0.0
 */
public class PartyContactOphAttrValue implements AttrValue{
	private final String key = "partyContactOph";
    private final int order = 9;

    @Override
    public Object getAttrValue(Map<String, Object> attrValueContext, String partyId,ExportService exportWordService){
//    	String partyId = OrganizationFullNameAttrValue.getPartyId();
    	Map d001Map = (Map) attrValueContext.get("D001");
        final String partyContactOphInfoSql = "SELECT D001026 FROM Party_D001 WHERE PARTY_ID = '%s'  and status = 0 ";
        if (d001Map != null) {
            String partyContactOph = StringUtil.obj2Str(d001Map.get("D001026"));
            if (StringUtils.isEmpty(partyContactOph)) {
                partyContactOph = this.getPartyContactOph(partyContactOphInfoSql, partyId, exportWordService);
            }
            return partyContactOph;
        } else {
            String partyContactOph = this.getPartyContactOph(partyContactOphInfoSql, partyId, exportWordService);
            return partyContactOph;
        }
    }

    private String getPartyContactOph(String sql, String depId,ExportService exportWordService){
        String attrInfoSql = String.format(sql, depId);
        List attrInfoList = exportWordService.findBySQL(attrInfoSql);
        if (attrInfoList != null && attrInfoList.size() > 0) {
            Map map = (Map) attrInfoList.get(0);
            if (map != null) {
                return StringUtil.obj2Str(map.get("D001026"));
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
        return "党组织联系人办公电话号码";
    }    
}
