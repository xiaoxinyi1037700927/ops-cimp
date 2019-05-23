package com.sinosoft.ops.cimp.util.combinedQuery.beans.codeSet;

import java.util.ArrayList;
import java.util.List;

public class CodeItem {

    public CodeItem(String code, String name) {
        this.code = code;
        this.name = name;
    }

    private String code;

    private String name;

    private List<CodeItem> children = new ArrayList<>();

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CodeItem> getChildren() {
        return children;
    }

    public void setChildren(List<CodeItem> children) {
        this.children = children;
    }
}
