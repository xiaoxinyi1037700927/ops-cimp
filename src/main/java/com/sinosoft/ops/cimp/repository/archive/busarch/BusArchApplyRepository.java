package com.sinosoft.ops.cimp.repository.archive.busarch;

import com.sinosoft.ops.cimp.entity.archive.BusArchApply;
import com.sinosoft.ops.cimp.entity.user.UserRole;
import com.sinosoft.ops.cimp.service.archive.bean.bean.PersonAndPost;
import oracle.sql.RAW;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Repository
public interface BusArchApplyRepository extends JpaRepository<BusArchApply, String>, QuerydslPredicateExecutor<BusArchApply> {

    @Query("select a from BusArchApply a where a.userid=?1 and a.verifyType>=0 order by a.createdTime")
    public List<BusArchApply> findAllByUseridAndVerifyType(String userid);

    @Modifying
    @Query("update BusArchApply a set a.verifyType=?1 where a.id=?2 and a.verifyType <> 100")
    public void updArch(Integer verifyType,String id);


    @Query("select a from BusArchApply a where  a.verifyType>0 and a.verifyType<99 order by a.createdTime")
    public List<BusArchApply> findAllByVerifyType();


    @Query(value = "select t1.emp_id,t1.a01001,A02016_A from emp_a001 t1  left join (   select *  " +
            "from (select row_number() over(partition by a02_b.emp_id order by a02_b.A02025 desc) rownumber, " +
            "a02_b.*  from EMP_A02 a02_b  where a02_b.status = 0 and a02_b.A02055='2') a02_a " +
            "where a02_a.rownumber = 1  ) a02 on t1.emp_id = a02.emp_id " +
            "where t1.A01063 = '1'  and  t1.emp_id in( " +
            "select t_t1.emp_id from EMP_A001 t_t1 where t_t1.A001004_A=?1" +
            "union all select a02.emp_id emp_id from EMP_A02 a02 " +
            " where status=0 and A02055='2' and A02001_B =?1)",nativeQuery = true)
    public List<PersonAndPost> getPersonAndPostByDepid(String Depid);

    @Query(value = "select  b.name from sys_user_role  a ,sys_role b  where a.role_id=b.id and a.user_id=?1",nativeQuery = true)
    public List<String> findRoleNameByUserId(String useid);

    @Query(value = "select max(emp_a001.A01001) A01001, max(sub_id) sub_id, wm_concat(distinct A02016_A) A02016_A, min(nvl(A02025, 999)) A02025" +
            "from emp_a02" +
            " left join emp_a001 " +
            " on emp_a02.emp_id = emp_a001.emp_id" +
            " where emp_a02.status = 0" +
            "  and emp_a001.status = 0" +
            "  and emp_a02.A02055 = '2'" +
            "  and A02001_B = ?1" +
            " group by emp_a001.emp_id " +
            "  order by A02025",nativeQuery = true)
    public List<Map<String, Object>> getA02ByDepid(String Depid);

    @Modifying
    @Query(value = "update emp_a02 set A02025=?2 where sub_id=?1",nativeQuery = true)
    public int updateA02Data(String subid,int A02025);
}

