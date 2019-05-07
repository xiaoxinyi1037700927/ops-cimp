package com.sinosoft.ops.cimp.export.common.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 类别 bean
 *
 * @author shixianggui
 * @Date: 20180306
 */
public class CategoryBean implements Serializable {

    private static final long serialVersionUID = 1L;

    // 父节点类别id: select g.id, g.description from SYS_INFO_SET_GROUP g where PARENT_ID=ID;
    //
    // 当前类别id:
    // SELECT
    // s.ID, s.NAME_CN
    // FROM SYS_INFO_SET s LEFT JOIN SYS_INFO_SET_USER U ON U.INFO_SET_ID = s.ID
    // WHERE s.ID IN (SELECT gr.info_set_id FROM SYS_INFO_SET_GROUP gr WHERE gr.NAME IN (
    // SELECT G.NAME FROM SYS_INFO_SET_GROUP G WHERE G.ID='113')
    // );
//    private int parentId;
//    private int currentId;
    private String sysTableNameEn;
    private List<AttributeBean> attributeBeans;

    public CategoryBean(String sysTableNameEn, List<AttributeBean> attributeBeans) {
        super();
        this.sysTableNameEn = sysTableNameEn;
        this.attributeBeans = attributeBeans;
    }


    public List<AttributeBean> getAttributeBeans() {
        return attributeBeans;
    }

    public void setAttributeBeans(List<AttributeBean> attributeBeans) {
        this.attributeBeans = attributeBeans;
    }

    public String getSysTableNameEn() {
        return sysTableNameEn;
    }

    public void setSysTableNameEn(String sysTableNameEn) {
        this.sysTableNameEn = sysTableNameEn;
    }

    @Override
    public String toString() {
        return "CategoryBean{" +
                "sysTableNameEn='" + sysTableNameEn + '\'' +
                ", attributeBeans=" + attributeBeans +
                '}';
    }
}
