package com.sinosoft.ops.cimp.util.word.pattern.xinjiang;

import com.sinosoft.ops.cimp.service.export.ExportService;
import com.sinosoft.ops.cimp.util.StringUtil;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;



/**
 * 
 * @ClassName: BecomePartyDateAttrValue
 * @description: 转正时间
 * @author: zhangxp
 * @date: 2017年12月3日 下午5:36:02
 * @version 1.0.0
 */
public class BecomePartyDateAttrValue implements AttrValue {
    private static final Logger logger = LoggerFactory.getLogger(BecomePartyDateAttrValue.class);

    private final String key = "becomePartyDate";
    private final int order = 23;

    @Override
    public Object getAttrValue(Map<String, Object> attrValueContext, String empId, ExportService exportWordService) {
        boolean isShowZhengZhiMianMiaoTime = (boolean) (attrValueContext.get("isShowZhengZhiMianMiaoTime") == null
                ? false
                : attrValueContext.get("isShowZhengZhiMianMiaoTime"));
        if (!isShowZhengZhiMianMiaoTime) {
            return "";
        }
        Map a030Map = (Map) attrValueContext.get("A030");
        final String becomePartyDateInfoSql = "SELECT A030020 FROM Emp_A030  WHERE EMP_ID = '%s' and status = 0 order by A030001 ASC";
        if (a030Map != null) {
            Object becomePartyDate = a030Map.get("A030020");
            if (!StringUtil.isNotEmptyOrNull(becomePartyDate)) {
                becomePartyDate = getBecomePartyDate(becomePartyDateInfoSql, empId, exportWordService);
            }
            if (StringUtil.isNotEmptyOrNull(becomePartyDate)) {
                try {
                    return new DateTime(becomePartyDate).toString("yyyy.MM");
                } catch (Exception e) {
                    logger.error("转正日期有误！", e);
                    return "";
                }
            } else {
                return "";
            }
        } else {
            Object becomePartyDate = getBecomePartyDate(becomePartyDateInfoSql, empId, exportWordService);
            if (StringUtil.isNotEmptyOrNull(becomePartyDate)) {
                try {
                    return new DateTime(becomePartyDate).toString("yyyy.MM");
                } catch (Exception e) {
                    logger.error("转正日期有误！", e);
                    return "";
                }
            } else {
                return "";
            }
        }
    }

    private Object getBecomePartyDate(String sql, String empId, ExportService exportWordService) {
        String attrInfoSql = String.format(sql, empId);
        List attrInfoList = exportWordService.findBySQL(attrInfoSql);
        if (attrInfoList != null && attrInfoList.size() > 0) {
            Map map = (Map) attrInfoList.get(0);
            if (map != null) {
                return map.get("A030020");
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
        return "转正时间";
    }
}
