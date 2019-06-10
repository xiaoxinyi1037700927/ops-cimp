package com.sinosoft.ops.cimp.util.word.pattern.xinjiang;

import com.sinosoft.ops.cimp.service.word.ExportService;

import java.util.Map;

/**
 * Created by rain chen on 2017/9/20.
 *
 * @author rain chen
 * @version 1.0
 * @since 1.0
 * Copyright (C) 2017. SinSoft All Rights Received
 */
public interface AttrValue {

    //SQL执行器
//    GenericDelegator DELEGATOR = GenericDelegator.getGenericDelegator("default");
//	ExportWordService BASEQUERY = null;
    /**
     * 执行属性规则
     *
     * @return 返回Map以attrName为Key属性值正确的格式为value
     */
    Object getAttrValue(Map<String, Object> attrValueContext, String empId, ExportService exportWordService) throws AttrValueException;

    /**
     * 获取属性的排序
     *
     * @return 排序值越小越靠前
     */
    int getOrder();

    /** 
     * 获取标题
     * @return 标题
     */
    String getTitle();
}
