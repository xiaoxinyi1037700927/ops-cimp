package com.sinosoft.ops.cimp.dao.domain;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DaoParam implements Serializable {

    private static final long serialVersionUID = 7427826097662082529L;
    /**
     * 参数对象集合
     */
    private List<ExecParam> execParamList;
    /**
     * 条件对象集合
     */
    private List<Conditions> conditionsList;
    /**
     * 排序条件对象集合
     */
    private List<Sorting> sortingList;
    /**
     * 实体名称
     */
    private String tableTypeNameEn;
    /**
     * 实体属性组组名称
     */
    private String tableNameEn;
    /**
     * 分页对象
     */
    private Page page;
    /**
     * 其它参数定义
     */
    private Map<String, Object> otherParam;

    public DaoParam() {
    }

    public DaoParam(String tableTypeNameEn, String tableNameEn) {
        this.tableTypeNameEn = tableTypeNameEn;
        this.tableNameEn = tableNameEn;
    }


    public DaoParam(List<ExecParam> execParamList, List<Conditions> conditionsList, List<Sorting> sortingList, String tableTypeNameEn, Page page, Map<String, Object> otherParam) {
        this.execParamList = execParamList;
        this.conditionsList = conditionsList;
        this.sortingList = sortingList;
        this.tableTypeNameEn = tableTypeNameEn;
        this.page = page;
        this.otherParam = otherParam;
    }

    public DaoParam addTableTypeNameEn(String entityName) {
        this.tableTypeNameEn = entityName;
        return this;
    }

    public DaoParam addTableNameEn(String tableNameEn) {
        this.tableNameEn = tableNameEn;
        return this;
    }

    public DaoParam addExecParamList(List<ExecParam> execParamList) {
        if (this.execParamList != null) {
            this.execParamList.addAll(execParamList);
            return this;
        }
        this.execParamList = execParamList;
        return this;
    }

    public DaoParam addSort(String attrName, String sort) {
        if (this.sortingList == null) {
            this.sortingList = new ArrayList<>();
        }
        this.sortingList.add(new Sorting(attrName, sort));
        return this;
    }

    public DaoParam addSortingList(List<Sorting> sortingList) {
        if (this.sortingList != null) {
            this.sortingList.addAll(sortingList);
            return this;
        }
        this.sortingList = sortingList;
        return this;
    }

    public DaoParam addExecParam(ExecParam execParam) {
        if (this.execParamList == null) {
            this.execParamList = new ArrayList<>();
        }
        this.execParamList.add(execParam);
        return this;
    }

    public DaoParam addExecParam(String execParamName, Object execParamValue) {
        return this.addExecParam(new ExecParam(execParamName, execParamName));
    }

    public DaoParam addExecParam(String execParamName) {
        return addExecParam(execParamName.trim(), "");
    }

    public DaoParam addQueryParams(String... execParamNames) {
        if (execParamNames != null && execParamNames.length > 0) {
            for (String anExecParamName : execParamNames) {
                addExecParam(anExecParamName.trim());
            }
        }
        return this;
    }

    public DaoParam addQueryParams(List<String> execParamNames) {
        if (execParamNames != null && execParamNames.size() > 0) {
            for (String anExecParamName : execParamNames) {
                addExecParam(anExecParamName);
            }
        }
        return this;
    }

    /**
     * 添加条件是相等的条件
     *
     * @param attr  属性
     * @param value 值
     * @return this
     */
    public DaoParam addEqualCondition(String attr, Object value) {
        return this.addCondition(attr, Conditions.ConditionsEnum.EQUAL, String.valueOf(value));
    }

    public DaoParam addConditionsList(List<Conditions> conditionsList) {
        if (conditionsList == null) {
            return this;
        }
        if (this.conditionsList != null) {
            this.conditionsList.addAll(conditionsList);
        } else {
            this.conditionsList = conditionsList;
        }
        return this;
    }

    /**
     * 添加条件
     *
     * @param attrName  属性名称
     * @param condition 条件  =  >   <   <>
     * @param value     属性值
     * @return this
     */
    public DaoParam addCondition(String attrName, String condition, String value) {
        Conditions conditions = new Conditions(attrName, condition, value);
        return this.addCondition(conditions);
    }

    public DaoParam addCondition(String attrName, Conditions.ConditionsEnum condition, String value) {
        Conditions conditions = new Conditions(attrName, condition, value);
        return this.addCondition(conditions);
    }

    /**
     * 添加条件
     *
     * @param condition 条件对象
     * @return this
     */
    public DaoParam addCondition(Conditions condition) {
        if (condition == null) {
            return this;
        }
        if (this.conditionsList == null) {
            conditionsList = new ArrayList<>();
        }
        this.conditionsList.add(condition);
        return this;
    }

    public DaoParam addSorting(Sorting sorting) {
        if (this.sortingList == null) {
            this.sortingList = new ArrayList<>();
        }
        this.sortingList.add(sorting);
        return this;
    }

    /**
     * 添加分页信息
     *
     * @param starRow 开始行
     * @param showRow 展示行
     * @return this
     */
    public DaoParam addPage(double starRow, double showRow) {
        if (starRow == 0 && showRow == 0) {
            return this;
        }
        this.page = new Page(starRow, showRow);
        return this;
    }

    public DaoParam addParam(String key, Object value) {
        if (this.otherParam == null) {
            this.otherParam = new HashMap<>();
        }
        this.otherParam.put(key, value);
        return this;
    }

    /**
     * 获取其它参数
     *
     * @param key 参数key
     * @return 参数值
     */
    public Object getParamByKey(String key) {
        if (this.getOtherParam() == null) {
            return "";
        }
        Object retObj = otherParam.get(key);
        if (retObj == null) {
            retObj = "";
        }
        return retObj;
    }

    public List<ExecParam> getExecParamList() {
        return execParamList;
    }

    public List<Conditions> getConditionsList() {
        return conditionsList;
    }

    public List<Sorting> getSortingList() {
        return sortingList;
    }

    public String getTableTypeNameEn() {
        return tableTypeNameEn;
    }

    public String getTableNameEn() {
        return tableNameEn;
    }

    public Page getPage() {
        return page;
    }

    public Map<String, Object> getOtherParam() {
        return otherParam;
    }

    public void removeExecParams() {
        this.execParamList.clear();
    }

    public void removeSort() {
        this.sortingList.clear();
    }

    public void removeConditions() {
        this.conditionsList.clear();
    }

    public DaoParam upDateParam(String attrName, String attrValue) {
        this.removeExecParam(attrName);
        return this.addExecParam(attrName, attrValue);
    }

    public DaoParam removeExecParam(String attrName) {
        int moveIndex = -1;
        for (int i = 0; i < this.execParamList.size(); i++) {
            ExecParam execParam = this.execParamList.get(i);
            if (StringUtils.equals(execParam.getFieldName(), attrName)) {
                moveIndex = i;
                break;
            }
        }
        if (moveIndex != -1) {
            this.execParamList.remove(moveIndex);
        }
        return this;
    }

    /**
     * 分页对象
     */
    public class Page {
        /**
         * 开始行，显示多少行
         */
        private int startRow, showRow;

        Page(double startRow, double showRow) {
            this.startRow = (int) startRow;
            this.showRow = (int) showRow;
        }

        public int getStartRow() {
            return startRow;
        }

        public int getShowRow() {
            return showRow;
        }

        @Override
        public String toString() {
            return "Page{" +
                    "startRow=" + startRow +
                    ", showRow=" + showRow +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Page)) return false;

            Page page = (Page) o;

            if (showRow != page.showRow) return false;
            if (startRow != page.startRow) return false;

            return true;
        }

        @Override
        protected Page clone() {
            return new Page(startRow, showRow);
        }

        @Override
        public int hashCode() {
            int result = startRow;
            result = 31 * result + showRow;
            return result;
        }
    }

    @Override
    public String toString() {
        return "DaoParam{" +
                "tableTypeNameEn='" + tableTypeNameEn + '\'' +
                ", ExecParamList=" + execParamList +
                ", conditionsList=" + conditionsList +
                ", sortingList=" + sortingList +
                ", page=" + page +
                ", otherParam=" + otherParam +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof DaoParam)) {
            return false;
        } else {
            DaoParam daoParam = (DaoParam) o;
            String entityNameO = daoParam.getTableTypeNameEn();
            boolean entity = StringUtils.equals(entityNameO, this.tableTypeNameEn);
            List<Conditions> conditionsListO = daoParam.getConditionsList();
            boolean con = equalsConditions(conditionsListO);
            List<ExecParam> ExecParamListO = daoParam.getExecParamList();
            boolean exc = equalsExecParams(ExecParamListO);
            List<Sorting> sortingListO = daoParam.getSortingList();
            boolean sort = equalsSortings(sortingListO);
            Map<String, Object> otherParamO = daoParam.getOtherParam();
            boolean other = equalsOtherParam(otherParamO);
            Page pageO = daoParam.getPage();
            boolean page = (pageO == this.page) || (pageO != null && this.page != null && pageO.equals(this.page));
            return entity && con && exc && sort && other && page;
        }
    }

    private boolean equalsOtherParam(Map<String, Object> conditionsListO) {
        if (conditionsListO == null || otherParam == null) {
            return (conditionsListO == otherParam) || (conditionsListO == null && otherParam.size() == 0) || (otherParam == null && conditionsListO != null && conditionsListO.size() == 0);
        } else {
            int conSize = this.otherParam.size();
            int conOSize = conditionsListO.size();
            if (conSize != conOSize) {
                return false;
            } else {
                boolean ret;
                for (Map.Entry<String, Object> stringObjectEntry : otherParam.entrySet()) {
                    ret = StringUtils.equals(String.valueOf(conditionsListO.get(stringObjectEntry.getKey())), String.valueOf(stringObjectEntry.getValue()));
                    if (ret) {
                        return false;
                    }
                }
                return true;
            }
        }
    }

    private boolean equalsSortings(List<Sorting> conditionsListO) {
        if (conditionsListO == null || sortingList == null) {
            return (conditionsListO == sortingList) || (conditionsListO == null && sortingList.size() == 0) || (sortingList == null && conditionsListO != null && conditionsListO.size() == 0);
        } else {
            int conSize = this.sortingList.size();
            int conOSize = conditionsListO.size();
            if (conSize != conOSize) {
                return false;
            } else {
                boolean ret;
                for (Sorting conditions : sortingList) {
                    ret = conditionsListO.indexOf(conditions) == -1;
                    if (ret) {
                        return false;
                    }
                }
                return true;
            }
        }
    }

    private boolean equalsExecParams(List<ExecParam> conditionsListO) {
        if (conditionsListO == null || execParamList == null) {
            return (conditionsListO == execParamList) || (conditionsListO == null && execParamList.size() == 0) || (execParamList == null && conditionsListO != null && conditionsListO.size() == 0);
        } else {
            int conSize = this.execParamList.size();
            int conOSize = conditionsListO.size();
            if (conSize != conOSize) {
                return false;
            } else {
                boolean ret;
                for (ExecParam conditions : execParamList) {
                    ret = conditionsListO.indexOf(conditions) == -1;
                    if (ret) {
                        return false;
                    }
                }
                return true;
            }
        }
    }

    private boolean equalsConditions(List<Conditions> conditionsListO) {
        if (conditionsListO == null || conditionsList == null) {
            return (conditionsListO == conditionsList) || (conditionsListO == null && conditionsList.size() == 0) || (conditionsList == null && conditionsListO != null && conditionsListO.size() == 0);
        } else {
            int conSize = this.conditionsList.size();
            int conOSize = conditionsListO.size();
            if (conSize != conOSize) {
                return false;
            } else {
                boolean ret;
                for (Conditions conditions : conditionsList) {
                    ret = conditionsListO.indexOf(conditions) == -1;
                    if (ret) {
                        return false;
                    }
                }
                return true;
            }
        }
    }
}
