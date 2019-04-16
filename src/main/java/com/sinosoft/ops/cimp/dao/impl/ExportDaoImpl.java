package com.sinosoft.ops.cimp.dao.impl;

import com.sinosoft.ops.cimp.dao.ExportDao;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: BaseQueryDaoImpl
 * @Description: 用于sql查询，服务于word导出功能
 * @Author: zhangxp
 * @Date: 2017年11月13日 下午12:32:03
 * @Version 1.0.0
 */
@Repository("exportDao")
public class ExportDaoImpl extends BaseDaoImpl implements ExportDao {

    private static final Logger logger = LoggerFactory.getLogger(ExportDaoImpl.class);

    @SuppressWarnings("unchecked")
    @Override
    @Transactional
    public List<Map<String, Object>> findBySQL(String sql) {
        List<Map<String, Object>> list = entityManager.createNativeQuery(sql).unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).getResultList();
        return list;
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional
    public List<Map<String, Object>> findBySQL1(String sql, String empId) {
        return entityManager.createQuery(sql)
                .setParameter("empId", empId)
                .getResultList();
    }

    @Override
    public String getEmpsInUnitExportGbrmb(String[] depIds) {
        String sql = "select Emp_id from EMP_A001 t1 where ( 1=1 ) and t1.STATUS='0' and t1.A001004_A in :depIds ";
        List list = entityManager.createNativeQuery(sql).setParameter("depIds", depIds).getResultList();
        StringBuilder emps = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            emps.append(list.get(i));
            if (i != list.size() - 1) {
                emps.append(",");
            }
        }
        return emps.toString();
    }

    /**
     * 获取所有人的emp_id
     *
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public String getStringEmps(String type) {
        StringBuilder sb = new StringBuilder(" ");
        boolean flag = false;
        if ("part".equalsIgnoreCase(type)) {//县处级以上的干部
            sb.append(" select distinct  t.emp_id from emp_a001 t inner join emp_a02 t2 on t.emp_id=t2.emp_id and t2.status = 0   and t2.A02055 = '2' ");
            sb.append(" inner join emp_a05 t5 on t.emp_id=t5.emp_id and t5.A05024='1' and (t5.a05002_b like '10%' or t5.a05002_b like '11%' or t5.a05002_b like '12%' or t5.a05002_b like '13%' ");
            sb.append("  or t5.a05002_b like '21%' or t5.a05002_b like '22%' or t5.a05002_b like '23%') ");
            sb.append("  and t5.status='0'   and t.status='0' and t.status = 0 and t.A01063 = '1' ");
        } else if ("partType".equalsIgnoreCase(type)) {
            //管理类别为23的干部
            sb.append(" select distinct  t.emp_id from emp_a001 t where (t.A01065 = '2' or t.A01065 = '3')");
            sb.append("  and  t.status='0' and t.status = 0 and t.A01063 = '1' ");
        } else if ("all".equalsIgnoreCase(type)) {//全库干部
            sb.append(" select emp_id from emp_a001 t1 where t1.status='0' ");
        } else {//查一个单独县区的，科级及以下
            flag = true;
            sb.append(" select distinct t.emp_id from emp_a001 t inner join dep_b001 d on t.A001004_A=d.dep_id inner join emp_a02 t2 on t.emp_id=t2.emp_id ");
            sb.append(" and t2.status = 0   and t2.A02055 = '2'  inner join pkreference p on t.emp_id=p.A0100 and d.code=p.b0110 inner join emp_a05 t5 on t.emp_id=t5.emp_id and t5.A05024='1' ");
            sb.append(" and (t5.a05002_b like '14%' or t5.a05002_b like '15%' or t5.a05002_b like '16%' or t5.a05002_b like '19%'  or t5.a05002_b like '24%' or t5.a05002_b like '25%' ");
            sb.append("  or t5.a05002_b like '26%'  or t5.a05002_b like '29%' or t5.a05002_b like '4%')  and t5.status='0'  and d.status='0' and t.status='0' and t.A01063 = '1' and d.tree_level_code like :treeLevelCode");
        }
        List list = null;
        if (flag) {
            list = entityManager.createNativeQuery(sb.toString()).setParameter("treeLevelCode", type + '%').getResultList();
        } else {
            list = entityManager.createNativeQuery(sb.toString()).getResultList();
        }
        StringBuilder emps = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            emps.append(list.get(i));
            if (i != list.size() - 1) {
                emps.append(",");
            }
        }
        return emps.toString();
    }

    /**
     * 获取所有人的emp_id
     *
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public List<String> getEmps() {
        String sql = "select emp_id from emp_a001 t1 where t1.status='0' and rownum < 30";
        List<String> list = entityManager.createNativeQuery(sql).getResultList();
        return list;
    }

//    @Override
//    public void exportPdf(List<String> empIds) {
//        Long startTime = System.currentTimeMillis();
//
//        try {
//            // 创建线程池
//            ThreadFactory threadFactory = Executors.defaultThreadFactory();
//            ThreadPoolExecutor tPoolExecutor = new ThreadPoolExecutor(5, 10, 1, TimeUnit.SECONDS,
//                    new ArrayBlockingQueue<>(12000), threadFactory);
//            //守护线程
//            Runnable daemon = new DaemonThread();
//            Thread monitor = new Thread(daemon);
///*        ExecutorService exec = Executors.newCachedThreadPool();
//        Thread monitor = new Thread(new DaemonThread());
//        exec.execute(monitor); */
//            monitor.setDaemon(true);
//            monitor.setName("daemonThread");
//            monitor.start();
//            //exec.shutdown();
//
//            String outputFilePath = PathConfig.getOutputfilePath();
//            for (int i = 0; i < empIds.size(); i++) {
//                PDFForPadThread padThread = new PDFForPadThread(outputFilePath, empIds.get(i));
//                tPoolExecutor.execute(padThread);
//                System.out.println(i + "===" + tPoolExecutor.getQueue().size());
//            }
//            tPoolExecutor.shutdown();
//
//            // 等待线程结束
//            tPoolExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
//        } catch (Exception ex) {
//
//        }
//        //monitor.
//        Long endTime = System.currentTimeMillis();
//        logger.error((endTime - startTime) / 100 + "秒");
//        System.out.println((endTime - startTime) / 100 + "秒");
//    }
}
