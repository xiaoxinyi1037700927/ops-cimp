package com.sinosoft.ops.cimp.vo.to.organization;

import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.List;

public class DepVO implements Serializable {
    private static final long serialVersionUID = 6406568653570801901L;

    private List<DepFieldVO> fields = Lists.newArrayList();

    public List<DepFieldVO> getFields() {
        return fields;
    }

    public void setFields(List<DepFieldVO> fields) {
        this.fields = fields;
    }
}
