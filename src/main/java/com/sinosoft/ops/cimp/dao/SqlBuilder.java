package com.sinosoft.ops.cimp.dao;

import com.google.common.collect.Maps;
import com.sinosoft.ops.cimp.constant.OpsErrorMessage;
import com.sinosoft.ops.cimp.dao.domain.DaoParam;
import com.sinosoft.ops.cimp.dao.domain.ResultSql;
import com.sinosoft.ops.cimp.exception.BusinessException;
import com.vip.vjtools.vjkit.number.NumberUtil;
import com.vip.vjtools.vjkit.time.DateFormatUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface SqlBuilder {

    Logger LOGGER_FACTORY = LoggerFactory.getLogger(SqlBuilder.class);

    /**
     * 根据实体名和实体属性信息拼出sql
     *
     * @return 待执行的sql
     */
    ResultSql getExecuteSql(DaoParam daoParam) throws BusinessException;

    default Map<String, String> selectField(List<String> execTableField, List<List<String>> tableFieldList) throws BusinessException {
        Map<String, String> tableFields = Maps.newHashMap();
        for (List<String> list : tableFieldList) {
            String tableNameEn = list.get(0);
            String saveField = list.get(1);
            tableFields.put(tableNameEn, saveField);
        }

        Map<String, String> result = Maps.newHashMap();
        for (String tableField : execTableField) {
            String saveField = tableFields.get(tableField);
            if (StringUtils.isEmpty(saveField)) {
                throw new BusinessException(OpsErrorMessage.MODULE_NAME, OpsErrorMessage.ERROR_MESSAGE, "属性[" + tableField + "]无对应的存储列");
            }
            result.put(tableField, saveField);
        }

        return result;
    }

    default Object convertValue(Object data, String attrSaveType) throws BusinessException {
        if (attrSaveType.toUpperCase().startsWith("VARCHAR") || attrSaveType.toUpperCase().startsWith("VARCHAR2")) {
            if (data != null) {
                return String.valueOf(data);
            }
            return "";
        }
        if (StringUtils.equalsIgnoreCase("DATE", attrSaveType)) {
            try {
                if (data instanceof Timestamp) {
                    return new SimpleDateFormat(DateFormatUtil.PATTERN_DEFAULT_ON_SECOND).format(data);
                }
                if (data instanceof Date) {
                    return DateFormatUtil.formatDate(DateFormatUtil.PATTERN_DEFAULT_ON_SECOND, (Date) data);
                }
                if (data == null) {
                    return "";
                }
                return DateFormatUtil.parseDate(DateFormatUtil.PATTERN_DEFAULT_ON_SECOND, (String) data);
            } catch (ParseException e) {
                throw new BusinessException(OpsErrorMessage.MODULE_NAME, OpsErrorMessage.ERROR_MESSAGE, "格式化日期错误");
            }
        }
        if (attrSaveType.toUpperCase().startsWith("NUMBER")) {
            if (attrSaveType.contains(",")) {
                if (data == null) {
                    return null;
                }
                if (data instanceof BigDecimal) {
                    return ((BigDecimal) data).doubleValue();
                }
                return NumberUtil.toDouble((String) data);
            } else {
                if (data == null) {
                    return null;
                }
                if (data instanceof BigDecimal) {
                    return ((BigDecimal) data).intValue();
                }
                return NumberUtil.toIntObject((String) data);
            }
        }
        if (attrSaveType.toUpperCase().startsWith("BLOB")) {
            return ((String) data).toCharArray();
        }
        return "";
    }

    default Object parseParam(String fieldType, Object paramValue) {
        if (StringUtils.equalsIgnoreCase("DATE", fieldType)) {
            if (paramValue != null) {
                int length = String.valueOf(paramValue).length();
                if (length <= 7) {
                    try {
                        return DateFormatUtil.parseDate("yyyy-MM", String.valueOf(paramValue));
                    } catch (ParseException e) {
                        return null;
                    }
                }
                if (length <= 10) {//yyyy-MM-dd格式
                    try {
                        return DateFormatUtil.parseDate(DateFormatUtil.PATTERN_ISO_ON_DATE, String.valueOf(paramValue));
                    } catch (ParseException e) {
                        return null;
                    }
                } else if (length <= 19) { //yyyy-MM-dd HH:mm:ss
                    try {
                        return DateFormatUtil.parseDate(DateFormatUtil.PATTERN_DEFAULT_ON_SECOND, String.valueOf(paramValue));
                    } catch (ParseException e) {
                        return null;
                    }
                }
            }
        } else {
            return String.valueOf(paramValue);
        }
        return null;
    }
}
