package com.sinosoft.ops.cimp.schedule.tasks;

import com.sinosoft.ops.cimp.entity.sys.check.SysCheckCondition;
import com.sinosoft.ops.cimp.entity.sys.check.SysCheckItem;
import com.sinosoft.ops.cimp.repository.sys.check.SysCheckConditionRepository;
import com.sinosoft.ops.cimp.repository.sys.check.SysCheckItemRepository;
import com.sinosoft.ops.cimp.schedule.Task;
import com.sinosoft.ops.cimp.schedule.beans.SysCheckResultTables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 数据统计
 */
@Component
public class DataStatisticsTask implements Task {

    private static final Logger logger = LoggerFactory.getLogger(DataStatisticsTask.class);

    private final JdbcTemplate jdbcTemplate;
    private final SysCheckConditionRepository sysCheckConditionRepository;
    private final SysCheckItemRepository sysCheckItemRepository;

    private static SysCheckResultTables resultTables;

    public DataStatisticsTask(JdbcTemplate jdbcTemplate, SysCheckConditionRepository sysCheckConditionRepository, SysCheckItemRepository sysCheckItemRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.sysCheckConditionRepository = sysCheckConditionRepository;
        this.sysCheckItemRepository = sysCheckItemRepository;
    }

    @Override
    public void exec() {
        long beginTime = System.currentTimeMillis();
        logger.info("DataStatisticsTask begin...");
        try {
            init();

            //统计A B各类总数
            statisticsEmpNumOfDep();
            statisticsNumOfDep();

            //统计查错数
            statisticsWrongNum();

            //统计查错总数
            statisticsTotalWrongNum();

            //统计完成后切换使用的结果表
            switchResultTables();

            long endTime = System.currentTimeMillis();
            logger.info("DataStatisticsTask end. totalTime = " + (endTime - beginTime) + "ms");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 切换使用的结果表
     */
    private void switchResultTables() {
        logger.info("switchResultTables begin...");

        String sql = "UPDATE SYS_CHECK_RESULT_TABLES SET RESULT_TABLES = " + resultTables.getId() + ",UPDATE_DATE = sysdate";
        jdbcTemplate.update(sql);

        logger.info("switchResultTables success");
    }

    /**
     * 统计查错数
     */
    private void statisticsWrongNum() {
        logger.info("statisticsWrongNum begin...");

        //获取所有的查错条件
        List<SysCheckCondition> checkConditions = sysCheckConditionRepository.findAll();

        List<SysCheckItem> sysCheckItems;
        for (SysCheckCondition condition : checkConditions) {
            sysCheckItems = sysCheckItemRepository.findBySysCheckConditionId(condition.getId());
            for (SysCheckItem item : sysCheckItems) {
                switch (item.getType()) {
                    case "A":
                        doStatisticsWrongNumA(item);
                        break;
                    case "B":
                        doStatisticsWrongNumB(item);
                        break;
                }
            }
        }
        logger.info("statisticsWrongNum success");
    }

    /**
     * 统计查错数 -- A
     */
    private void doStatisticsWrongNumA(SysCheckItem item) {
        String sql = "insert into RESULT_WRONG_NUMBER(org_id, tree_level_code, pptr, num, Type, check_condition_id) " +
                " select b001.dep_id,max(b001.B001001_A),max(b001.pptr), count(1), 'A', '" + item.getSysCheckConditionId() +
                "' from dep_b001 b001 left join emp_a001 t1 on t1.A001004_A = b001.dep_id " +
                " where b001.status=0 and t1.status = 0 and " + item.getWherePart() +
                " group by b001.dep_id  ";

        int count = jdbcTemplate.update(sql);

        logger.info("doStatisticsA:insert " + count + "rows into RESULT_WRONG_NUMBER table ");
    }

    /**
     * 统计查错数 -- B
     */
    private void doStatisticsWrongNumB(SysCheckItem item) {
        String sql = "insert into RESULT_WRONG_NUMBER(org_id, tree_level_code, pptr, num, Type, check_condition_id) " +
                " select b001.dep_id,max(b001.B001001_A),max(b001.pptr), count(1), 'B', '" + item.getSysCheckConditionId() +
                "' from dep_b001 b001 " +
                " where b001.status=0 and " + item.getWherePart() +
                " group by b001.dep_id  ";

        int count = jdbcTemplate.update(sql);

        logger.info("doStatisticsB:insert " + count + "rows into RESULT_WRONG_NUMBER table ");
    }

    /**
     * 统计查错总数
     */
    private void statisticsTotalWrongNum() {
        //获取所有的查错条件
        List<SysCheckCondition> checkConditions = sysCheckConditionRepository.findAll();

        List<SysCheckItem> sysCheckItems;
        String type;
        for (SysCheckCondition condition : checkConditions) {
            sysCheckItems = sysCheckItemRepository.findBySysCheckConditionId(condition.getId());
            for (SysCheckItem item : sysCheckItems) {
                switch (item.getType()) {
                    case "A":
                        doStatisticsTotalWrongNumA(item);
                        break;
                    case "B":
                        doStatisticsTotalWrongNumB(item);
                        break;
                }

            }

        }

    }

    /**
     * 统计查错总数 -- A
     */
    private void doStatisticsTotalWrongNumA(SysCheckItem item) {
        String sql = " insert into " + resultTables.getResultsTempName() + "(Tree_Level_Code,num,Type,check_condition_id) " +
                " select dl.root, nvl(sum(rwn.num), 0), 'A', '" + item.getSysCheckConditionId() +
                "' from RESULT_WRONG_NUMBER rwn " +
                " inner join DEP_LEVEL dl on dl.DEP_ID = rwn.org_id " +
                " where rwn.Type = 'A' and rwn.check_condition_id = '" + item.getSysCheckConditionId() + "'" +
                " group by dl.root ";

        int count = jdbcTemplate.update(sql);

        logger.info("doStatisticsTotalWrongNumA:insert " + count + "rows into " + resultTables.getResultsTempName() + " table ");
    }

    /**
     * 统计查错总数 -- B
     */
    private void doStatisticsTotalWrongNumB(SysCheckItem item) {
        String sql = " insert into " + resultTables.getResultsTempName() + "(Tree_Level_Code,num,Type,check_condition_id) " +
                " select dl.root, nvl(sum(rwn.num), 0), 'B', '" + item.getSysCheckConditionId() +
                "' from RESULT_WRONG_NUMBER rwn " +
                " inner join DEP_LEVEL dl on dl.DEP_ID = rwn.org_id " +
                " where rwn.Type = 'B' and rwn.check_condition_id = '" + item.getSysCheckConditionId() + "'" +
                " group by dl.root ";

        int count = jdbcTemplate.update(sql);

        logger.info("doStatisticsTotalWrongNumB:insert " + count + "rows into " + resultTables.getResultsTempName() + " table ");
    }

    /**
     * 初始化
     */
    private void init() {
        logger.info("init begin...");

        //获取存储统计数据的表名
        String id = jdbcTemplate.queryForMap("SELECT RESULT_TABLES FROM SYS_CHECK_RESULT_TABLES WHERE ROWNUM = 1").get("RESULT_TABLES").toString();
        resultTables = SysCheckResultTables.getAnotherTables(id);

        //统计单位权限 -- DEP_LEVEL
        statisticsDepLevel();

        //清空单位错误数
        deleteTableData("RESULT_WRONG_NUMBER");

        //清空结果表数据
        deleteTableData(resultTables.getResultsName());
        deleteTableData(resultTables.getResultsTempName());

        logger.info("init success");
    }


    /**
     * 统计单位权限
     */
    private void statisticsDepLevel() {
        logger.info("statisticsDepLevel begin...");

        //清空单位权限表的数据
        deleteTableData("DEP_LEVEL");

        String sql = "INSERT INTO DEP_LEVEL(dep_id, description, tree_level_code,pptr,LEVEL_NUM, root) " +
                " SELECT dep_id, description, B001001_A,pptr,LEVEL, CONNECT_BY_ROOT(B001001_A) root FROM dep_b001 " +
                " START WITH B001001_A IN (SELECT B001001_A FROM dep_b001) " +
                " CONNECT BY NOCYCLE PRIOR B001001_A = PPTR AND status = 0 ";
        int count = jdbcTemplate.update(sql);
        logger.info("insert " + count + "rows into DEP_LEVEL table ");
        logger.info("statisticsDepLevel success");
    }


    /**
     * 统计单位总人数 --A
     */
    private void statisticsEmpNumOfDep() {
        logger.info("statisticsEmpNumOfDep begin...");

        String sql = "INSERT INTO " + resultTables.getResultsName() + "(Tree_Level_Code,Total,type) " +
                "WITH a AS(" +
                " SELECT dep_id, description, tree_level_code,pptr,LEVEL_NUM, root FROM DEP_LEVEL " +
                ")," +
                "b AS(SELECT A001004_A dep_id, count(*) pn FROM emp_a001 WHERE status = 0 GROUP BY A001004_A) " +
                " SELECT a.root, nvl(sum(b.pn), 0), 'A' FROM a LEFT JOIN b ON a.dep_id = b.dep_id " +
                " GROUP BY root";

        int count = jdbcTemplate.update(sql);
        logger.info("insert " + count + "rows into SYS_CHECK_RESULTS table ");
        logger.info("statisticsEmpNumOfDep success");
    }

    /**
     * 统计单位数量 --B
     */
    private void statisticsNumOfDep() {
        logger.info("statisticsNumOfDep begin...");

        String sql = "INSERT INTO " + resultTables.getResultsName() + "(Tree_Level_Code,Total,type) " +
                "WITH a AS(" +
                " SELECT dep_id, description, tree_level_code,pptr,LEVEL_NUM, root FROM DEP_LEVEL " +
                ")," +
                "b AS(SELECT dep_id, count(1) pn FROM dep_b001 WHERE status = 0 GROUP BY dep_id)" +
                " SELECT a.root, sum(nvl(b.pn, 0)), 'B' FROM a LEFT JOIN b ON a.dep_id = b.dep_id " +
                " GROUP BY root";

        int count = jdbcTemplate.update(sql);
        logger.info("insert " + count + "rows into SYS_CHECK_RESULTS table ");
        logger.info("statisticsNumOfDep success");
    }


    /**
     * 清空表数据
     */
    private void deleteTableData(String tableName) {
        int count = jdbcTemplate.update("delete from " + tableName);
        logger.info("delete " + count + "rows from " + tableName + " table ");
    }

}
