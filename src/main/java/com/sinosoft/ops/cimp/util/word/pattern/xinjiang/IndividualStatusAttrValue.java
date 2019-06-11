package com.sinosoft.ops.cimp.util.word.pattern.xinjiang;

import com.sinosoft.ops.cimp.service.word.ExportService;
import com.sinosoft.ops.cimp.util.CodeTranslateUtil;
import com.sinosoft.ops.cimp.util.StringUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;


/**
 * @classname:  IndividualStatusAttrValue
 * @description: 个人身份
 * @author:        zhangxp
 * @date:            2017年12月2日 下午11:46:32
 * @version        1.0.0
 */
public class IndividualStatusAttrValue  implements AttrValue{

	final private String key = "individualStatus";
	final private int order = 5;

    @Override
    public Object getAttrValue(Map<String, Object> attrValueContext, String empId,ExportService exportWordService){
        Map a001Map = (Map) attrValueContext.get("A001");
        final String individualStatusInfoSql = "SELECT A001050 FROM Emp_A001 Emp_A001 WHERE EMP_ID = '%s'  and status = 0 ";

        if (a001Map != null) {
            String IndividualStatusCode = StringUtil.obj2Str(a001Map.get("A001050"));
            if (StringUtils.isNotEmpty(IndividualStatusCode)) {
                IndividualStatusCode = this.getIndividualStatusStr(individualStatusInfoSql, empId, IndividualStatusCode,exportWordService);
            } else {
                return this.getIndividualStatusStr(individualStatusInfoSql, empId, null,exportWordService);
            }
            return IndividualStatusCode;
        } else {
            return this.getIndividualStatusStr(individualStatusInfoSql, empId, null,exportWordService);
        }
    }
    
    private String getIndividualStatusStr(String sql, String empId, String IndividualStatusCode,ExportService exportWordService){
        String genderInfoSql = String.format(sql, empId);
        if (StringUtils.isNotEmpty(IndividualStatusCode)) {
        	return CodeTranslateUtil.codeToName("BT0025", IndividualStatusCode, exportWordService);
        } else {
            List genderInfoList = exportWordService.findBySQL(genderInfoSql);
            if (genderInfoList != null && genderInfoList.size() > 0) {
                Map IndividualStatusMap = (Map) genderInfoList.get(0);
                if (IndividualStatusMap != null) {
                    String IndividualStatus_Code = StringUtil.obj2Str(IndividualStatusMap.get("A001050"));
                    return CodeTranslateUtil.codeToName("BT0025", IndividualStatus_Code, exportWordService);
                }
            }
        }

        return "";
    }
    @Override
    public int getOrder() {
        return order;
    }

    public String getKey() {
        return key;
    }
    
    @Override
    public String getTitle() {
        return "个人身份";
    }    
}
