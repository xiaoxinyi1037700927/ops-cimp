package com.sinosoft.ops.cimp.common.word.data;

import com.sinosoft.ops.cimp.service.word.ExportService;
import com.sinosoft.ops.cimp.util.StringUtil;

import java.util.List;
import java.util.Map;


/**
 * @version 1.0.0
 * @classname: DanweiOfPartyAttrValue
 * @description: 党组织所在单位名称(所有)
 * @author: zhangxp
 * @date: 2017年12月4日 下午7:40:16
 */
public class DanweiOfPartyAttrValue implements AttrValue {

    private final String key = "danweiOfParty";
    private final int order = 12;

    @Override
    public Object getAttrValue(Map<String, Object> attrValueContext, String partyId, ExportService exportWordService) {
//	    	String partyId = OrganizationFullNameAttrValue.getPartyId();
        final String danweiOfPartyInfoSql = "SELECT D001005_A FROM Party_D001 WHERE PARTY_ID = '%s'";
        String danweiOfPartyIds = this.getPartyContact(danweiOfPartyInfoSql, partyId, exportWordService);
        String[] depIds = danweiOfPartyIds.split(",");
        String depSql = "SELECT B001001_B,DESCRIPTION FROM DEP_B001 WHERE DEP_ID = '%s'";
        StringBuffer danweiOfParty = new StringBuffer();
        String tempName = "";
        for (int i = 0; i < depIds.length; i++) {
            tempName = this.getDepNameByDepId(depSql, depIds[i], exportWordService);
            danweiOfParty.append(tempName + "，");
        }
        String name = danweiOfParty.toString();
        name = name.substring(0, name.length() - 1);
        return name;
    }


    private String getPartyContact(String sql, String depId, ExportService exportWordService) {
        String attrInfoSql = String.format(sql, depId);
        List attrInfoList = exportWordService.findBySQL(attrInfoSql);
        if (attrInfoList != null && attrInfoList.size() > 0) {
            Map map = (Map) attrInfoList.get(0);
            if (map != null) {
                return StringUtil.obj2Str(map.get("D001005_A"));
            }
        }
        return "";
    }

    private String getDepNameByDepId(String sql, String depId, ExportService exportWordService) {
        String attrInfoSql = String.format(sql, depId);
        List attrInfoList = exportWordService.findBySQL(attrInfoSql);
        if (attrInfoList != null && attrInfoList.size() > 0) {
            Map map = (Map) attrInfoList.get(0);
            if (map != null) {
                String name = StringUtil.obj2Str(map.get("B001001_B"));
                if (StringUtil.isEmptyOrNull(name)) {
                    name = StringUtil.obj2Str(map.get("DESCRIPTION"));
                }
                return name;
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
