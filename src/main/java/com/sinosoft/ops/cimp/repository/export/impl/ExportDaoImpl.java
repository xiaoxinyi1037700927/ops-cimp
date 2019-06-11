package com.sinosoft.ops.cimp.repository.export.impl;


import com.sinosoft.ops.cimp.common.dao.BaseDaoImpl;
import com.sinosoft.ops.cimp.repository.export.ExportDao;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.util.List;
import java.util.Map;

/**
 * 
 * @ClassName:  BaseQueryDaoImpl
 * @Description: 用于sql查询，服务于word导出功能
 * @Author:        zhangxp
 * @Date:            2017年11月13日 下午12:32:03
 * @Version        1.0.0
 */
@Repository("exportWordDao")
public class ExportDaoImpl extends BaseDaoImpl implements ExportDao {

    public ExportDaoImpl(EntityManagerFactory factory) {
        super(factory);
    }

	@SuppressWarnings("unchecked")
    @Override
	public List<Map<String, Object>> findBySQL(String sql) {
    	return sessionFactory.getCurrentSession().createSQLQuery(sql).setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP).list();
	}

    @Override
    public boolean saveResumeByEmpId(String empId, String resume) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.setFlushMode(FlushMode.MANUAL);
        boolean result = session.createNativeQuery("update EMP_A001 set resume = :resumeValue where emp_id = :empId")
                .setParameter("resumeValue", resume)
                .setParameter("empId", empId).executeUpdate() > 0;
        session.flush();
        session.getTransaction().commit();
        return result;
    }

	@SuppressWarnings("unchecked")
    @Override
	public List<String> getAllEmps() {
		String sql = "select a1.emp_id from EMP_A001 a1 where a1.status = 0 and a1.A001050 like '5%'" + 
		        " and exists (select 1 from EMP_A070  a7 where a7.emp_id = a1.emp_id and a7.status = 0)"; 
		return sessionFactory.getCurrentSession().createSQLQuery(sql).list();
	}

	/* (non-Javadoc)
	 * @see com.newskysoft.iimp.word.dao.ExportDao#getStringEmps(java.lang.String)
	 */
	@Override
	public String getStringEmps(String type) {
    	String sql="";
    	if("part".equalsIgnoreCase(type)){
//    		sql="select t.emp_id from emp_a001 t inner join emp_a05 t5 on t.emp_id=t5.emp_id and t5.A05024='1' ";
//    	    sql+=" and t.status='0' and (t5.a05002_b like '10%' or t5.a05002_b like '11%' or t5.a05002_b like '12%' or t5.a05002_b like '13%') and rownum<20";
    		sql = "select emp_id from emp_a001 t1 where t1.status='0' and rownum<10 ";    		
    	}else if("all".equalsIgnoreCase(type)){
    		sql = "select emp_id from emp_a001 t1 where t1.status='0' and rownum<10 ";    		
    	}
        List list = sessionFactory.getCurrentSession().createSQLQuery(sql).list();
        StringBuilder emps = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            emps.append(list.get(i));
            if (i != list.size() - 1) {
                emps.append(",");
            }
        }
        return emps.toString();
	}

    @SuppressWarnings("unchecked")
    @Override
    public List<String> getLWEmpIds() {
        StringBuilder sb=new StringBuilder("select t1.EMP_ID from EMP_A001 t1 where t1.STATUS=0");
        sb.append(" AND ( t1.A001050 like '1%' or t1.A001050 like '4%' )");//个人身份
        sb.append(" AND exists (select 1 from DEP_B001 b1 where t1.a001004_a = b1.dep_id and b1.status = 0");
        sb.append("   and b1.B001060 = '1' and b1.B001045 like '5A%' and (b1.B001065 between '11' and '129' or b1.B001065 in ('21', '22', '31', '32'))");
        sb.append("   and (b1.B001070 like 'A%' and b1.B001070 not like 'A05%') )");
        sb.append(" AND (exists  (select 1 from EMP_A010 a10 where a10.status = 0 and a10.EMP_ID = t1.EMP_ID");
        sb.append("  and a10.A010002 like '863%' and a10.A010045 = '2' and a10.A010005 = (select max(x.A010005)");//行政职务
        sb.append("  from EMP_A010 x where x.status = 0 and x.emp_id = t1.emp_id))");
        sb.append(" OR exists (select 1 from EMP_C015 c15 where c15.status = 0 and c15.EMP_ID = t1.EMP_ID");
        sb.append("  and c15.C015002 like '861%' and c15.C015045 = '2' and c15.C015005 = (select max(x.C015005)");//党内职务
        sb.append(" from EMP_C015 x where x.status = 0 and x.emp_id = t1.emp_id)))");
        sb.append(" AND not exists ( select 1 from EMP_A020 a2 where a2.status=0 and ");
        sb.append("  t1.emp_id=a2.emp_id and (a2.A020001>='2' and a2.A020001<'3') and ");
        sb.append("  a2.A020005=(select max(x.A020005) from EMP_A020 x where x.status=0 and x.emp_id=t1.emp_id) )");//非减员
        sb.append(" AND not exists ( select 1 from EMP_A231 a2 where a2.status=0 and t1.emp_id=a2.emp_id and (a2.A231005>='1' and a2.A231005<='2') and a2.A231010=(select max(x.A231010) from EMP_A231 x where x.status=0 and x.emp_id=t1.emp_id) )");//非离退

        return sessionFactory.getCurrentSession().createNativeQuery(sb.toString()).list();
    }   
}
