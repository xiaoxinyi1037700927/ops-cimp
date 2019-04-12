package com.sinosoft.ops.cimp.dao.domain;

import java.io.Serializable;

public class Sorting implements Serializable {
    private static final long serialVersionUID = 6864149187050771505L;
    /**
     * 排序的字段名字 attrEn
     */
    private String sortName;
    /**
     * asc或desc  升序或降序
     */
    private String sortValue;

    public Sorting(String sortName, String sortValue) {
        this.sortName = sortName;
        this.sortValue = sortValue;
    }

    public Sorting() {
    }

    @Override
    public Sorting clone() {
        Sorting sorting = new Sorting();
        sorting.setSortName(this.sortName);
        sorting.setSortValue(this.sortValue);
        return sorting;
    }

    @Override
    public String toString() {
        return sortName + ":" + sortValue;
    }

    public void setSortName(String sortName) {
        this.sortName = sortName;
    }

    public void setSortValue(String sortValue) {
        this.sortValue = sortValue;
    }

    public String getSortName() {
        return sortName;
    }

    public String getSortValue() {
        return sortValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Sorting)) return false;

        Sorting sorting = (Sorting) o;

        if (!sortName.equals(sorting.sortName)) return false;
        if (!sortValue.equals(sorting.sortValue)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = sortName.hashCode();
        result = 31 * result + sortValue.hashCode();
        return result;
    }
}
