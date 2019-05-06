package com.sinosoft.ops.cimp.schedule.tasks;

import com.sinosoft.ops.cimp.entity.sys.check.SysCheckCondition;
import com.sinosoft.ops.cimp.entity.sys.check.SysCheckType;
import com.sinosoft.ops.cimp.repository.sys.check.SysCheckConditionRepository;
import com.sinosoft.ops.cimp.repository.sys.check.SysCheckTypeRepository;
import com.sinosoft.ops.cimp.schedule.Task;
import com.sinosoft.ops.cimp.schedule.syscheck.SysCheckResultTables;
import com.sinosoft.ops.cimp.schedule.syscheck.SysCheckTypeAdapter;
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
    private final SysCheckTypeRepository sysCheckTypeRepository;
    private final SysCheckTypeAdapter[] typeAdapters;

    private static SysCheckResultTables resultTables;

    public DataStatisticsTask(JdbcTemplate jdbcTemplate, SysCheckConditionRepository sysCheckConditionRepository, SysCheckTypeRepository sysCheckTypeRepository, SysCheckTypeAdapter[] typeAdapters) {
        this.jdbcTemplate = jdbcTemplate;
        this.sysCheckConditionRepository = sysCheckConditionRepository;
        this.sysCheckTypeRepository = sysCheckTypeRepository;
        this.typeAdapters = typeAdapters;
    }

    @Override
    public boolean exec() {
        long beginTime = System.currentTimeMillis();
        logger.info("DataStatisticsTask begin...");
        try {
            init();

            //统计各类总数
            statisticsTotalNum();

            //统计查错数
            statisticsWrongNum();

            //统计查错总数
            statisticsTotalWrongNum();

            //统计完成后切换使用的结果表
            switchResultTables();

            long endTime = System.currentTimeMillis();
            logger.info("DataStatisticsTask end. totalTime = " + (endTime - beginTime) + "ms");
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return false;
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
     * 统计各类总数
     */
    private void statisticsTotalNum() throws Exception {
        for (SysCheckType type : sysCheckTypeRepository.findAll()) {
            SysCheckTypeAdapter typeAdapter = getTypeAdapter(type.getId());
            typeAdapter.statisticsTotalNum(jdbcTemplate, resultTables.getResultsName());
        }
    }


    /**
     * 统计查错数
     */
    private void statisticsWrongNum() throws Exception {
        logger.info("statisticsWrongNum begin...");

        //获取所有的查错条件
        List<SysCheckCondition> checkConditions = sysCheckConditionRepository.findAll();

        for (SysCheckCondition condition : checkConditions) {
            SysCheckTypeAdapter typeAdapter = getTypeAdapter(condition.getTypeId());
            typeAdapter.statisticsWrongNum(jdbcTemplate, condition);
        }
        logger.info("statisticsWrongNum success");
    }

    /**
     * 统计查错总数
     */
    private void statisticsTotalWrongNum() throws Exception {
        //获取所有的查错条件
        List<SysCheckCondition> checkConditions = sysCheckConditionRepository.findAll();

        for (SysCheckCondition condition : checkConditions) {
            SysCheckTypeAdapter typeAdapter = getTypeAdapter(condition.getTypeId());
            typeAdapter.statisticsTotalWrongNum(jdbcTemplate, condition, resultTables.getResultsTempName());
        }

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
     * 根据查错类型获取适配器
     */
    private SysCheckTypeAdapter getTypeAdapter(String typeId) throws Exception {
        for (SysCheckTypeAdapter typeAdapter : typeAdapters) {
            if (typeAdapter.support(typeId)) {
                return typeAdapter;
            }
        }

        throw new Exception("no adapter for type " + typeId);
    }


    /**
     * 清空表数据
     */
    private void deleteTableData(String tableName) {
        int count = jdbcTemplate.update("delete from " + tableName);
        logger.info("delete " + count + "rows from " + tableName + " table ");
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

}
