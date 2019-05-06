package com.sinosoft.ops.cimp.schedule.syscheck;

/**
 * 查错统计结果表，用2套是为了保证在定时任务生成统计数据时的可用性
 */
public enum SysCheckResultTables {

    TABLES1("1", "SYS_CHECK_RESULTS", "SYS_CHECK_RESULTTEMP"),
    TABLES2("2", "SYS_CHECK_RESULTS2", "SYS_CHECK_RESULTTEMP2");

    private String id;
    private String resultsName;//总人数结果集
    private String resultsTempName;//查错人数结果集

    SysCheckResultTables(String id, String resultsName, String resultsTempName) {
        this.id = id;
        this.resultsName = resultsName;
        this.resultsTempName = resultsTempName;
    }

    public String getId() {
        return id;
    }

    public String getResultsName() {
        return resultsName;
    }

    public String getResultsTempName() {
        return resultsTempName;
    }

    public static SysCheckResultTables getTables(String id) {
        switch (id) {
            case "1":
                return TABLES1;
            case "2":
                return TABLES2;
            default:
                return null;
        }
    }

    public static SysCheckResultTables getAnotherTables(String id) {
        switch (id) {
            case "1":
                return TABLES2;
            case "2":
                return TABLES1;
            default:
                return null;
        }
    }

}
