package com.sinosoft.ops.cimp.service.sys.homepagecount.impl;

import com.google.common.collect.Lists;
import com.sinosoft.ops.cimp.entity.sys.homepagecount.ChartsStatisticsSql;
import com.sinosoft.ops.cimp.entity.sys.homepagecount.QChartsStatisticsSql;
import com.sinosoft.ops.cimp.repository.sys.homepagecount.ChartsStatisticsSqlRepository;
import com.sinosoft.ops.cimp.service.sys.homepagecount.ChartsService;
import com.sinosoft.ops.cimp.util.SecurityUtils;
import com.sinosoft.ops.cimp.vo.to.sys.homepagecount.ChartsDataModel;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Service
public class ChartsServiceImpl implements ChartsService {

    private final ChartsStatisticsSqlRepository chartsRepository;
    private final JdbcTemplate jdbcTemplate;
    private ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);

    public ChartsServiceImpl(ChartsStatisticsSqlRepository chartsRepository, JdbcTemplate jdbcTemplate) {
        this.chartsRepository = chartsRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * 获取干部统计图表数据
     */
    @Override
    public List<ChartsDataModel> getCadreStatisticsData() {
        QChartsStatisticsSql qChartsStatisticsSql = QChartsStatisticsSql.chartsStatisticsSql;
        Iterable<ChartsStatisticsSql> iterable = chartsRepository.findAll(qChartsStatisticsSql.sortNumber.asc().nullsLast());
        List<ChartsStatisticsSql> charts = Lists.newArrayList(iterable);

        String dataOrganizationId = SecurityUtils.getSubject().getCurrentUser().getDataOrganizationId();

        dataOrganizationId = Arrays.stream(dataOrganizationId.split(",")).collect(Collectors.joining("','", "('", "')"));

        List<ChartsDataModel> modelList = new ArrayList<>(charts.size());
        List<QueryTask> tasks = new ArrayList<>();
        for (ChartsStatisticsSql chart : charts) {
            tasks.add(new QueryTask(jdbcTemplate, chart, dataOrganizationId));
        }

        try {
            List<Future<ChartsDataModel>> futures = executorService.invokeAll(tasks);
            for (Future<ChartsDataModel> future : futures) {
                modelList.add(future.get());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return modelList;
    }


    class QueryTask implements Callable<ChartsDataModel> {
        private JdbcTemplate jdbcTemplate;
        private ChartsStatisticsSql charts;
        private String orgId;

        QueryTask(JdbcTemplate jdbcTemplate, ChartsStatisticsSql charts, String orgId) {
            this.jdbcTemplate = jdbcTemplate;
            this.charts = charts;
            this.orgId = orgId;
        }

        @Override
        public ChartsDataModel call() {
            ChartsDataModel model = new ChartsDataModel();
            model.setName(charts.getName());

            model.setData(jdbcTemplate.queryForList(charts.getSql().replaceAll("\\$\\{deptId}", orgId)).stream().map(map -> {
                Map<String, Object> item = new HashMap<>();
                item.put("name", map.get("name"));
                item.put("num", map.get("num"));
                return item;
            }).collect(Collectors.toList()));

            return model;
        }
    }

}
