package com.sinosoft.ops.cimp.common.word.processor;

import com.aspose.words.Document;

import java.util.Map;

/**
 * Created by rain chen on 2017/9/27.
 *
 * @author rain chen
 * @version 1.0
 * @since 1.0
 * Copyright (C) 2017. SinSoft All Rights Received
 */
public interface AttrValueProcessor {

    void processAttr(Document document, Map<String, Object> attrAndValue) throws Exception;
}
