package com.sinosoft.ops.cimp.vo.to.cadre;

import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.List;

public class CadreVO implements Serializable {
    private static final long serialVersionUID = 6406568653570801901L;

    private List<CadreFieldVO> fields = Lists.newArrayList();

    public List<CadreFieldVO> getFields() {
        return fields;
    }

    public void setFields(List<CadreFieldVO> fields) {
        this.fields = fields;
    }
}
