package com.sinosoft.ops.cimp.repository.archive.busarch;

import com.sinosoft.ops.cimp.entity.archive.BusArchApply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Map;

@Repository
public interface BusArchApplyRepository extends JpaRepository<BusArchApply, String>, QuerydslPredicateExecutor<BusArchApply> {

    /**
     * 分页查询查看申请
     * @param userid
     * @param pageable
     * @return
     */
    @Query("select a from BusArchApply a where a.userid=?1 and a.verifyType>=0 order by a.createdTime")
    public Page<BusArchApply> findAllByUseridAndVerifyType(String userid, Pageable pageable);

    /**
     * 分页查询查看申请的总数
     * @return
     */
    @Query("select count(a.id) from BusArchApply a where a.userid=?1 and a.verifyType>=0")
    public Integer getBusArchApplyByVerifyTypeAAndUserid(String userid);

    /**
     * 分页查询查看审批的总数
     * @return
     */
    @Query("select count(a.id) from BusArchApply a where a.verifyType>0 and a.verifyType<99 order by a.createdTime")
    public Integer getBusArchApplyByVerifyType();

    /**
     * 根据id查询
     * @param userid
     * @return
     */
    @Query("select a from BusArchApply a where a.id=?1 ")
    public BusArchApply findByIdAnd(String userid);

    /**
     * 修改 id修改verifyType（删除时verifyType-1）
     * @param verifyType
     * @param id
     */
    @Modifying
    @Query("update BusArchApply a set a.verifyType=?1 where a.id=?2 and a.verifyType <> 100")
    public void updArch(Integer verifyType,String id);

    /**
     *分页查询查看审批
     * @param pageable
     * @return
     */
    @Query("select a from BusArchApply a where  a.verifyType>0 and a.verifyType<99 order by a.createdTime")
    public Page<BusArchApply> findAllByVerifyType(Pageable pageable);



    @Query(value = "select  b.name from sys_user_role  a ,sys_role b  where a.role_id=b.id and a.user_id=?1",nativeQuery = true)
    public List<String> findRoleNameByUserId(String useid);

    @Query(value = "select max(emp_a001.A01001) A01001, max(sub_id) sub_id, wm_concat(distinct A02016_A) A02016_A, min(nvl(A02025, 999)) A02025 " +
            "from emp_a02" +
            " left join emp_a001 " +
            " on emp_a02.emp_id = emp_a001.emp_id" +
            " where emp_a02.status = 0" +
            "  and emp_a001.status = 0" +
            "  and emp_a02.A02055 = '2'" +
            "  and A02001_B = ?1 " +
            " group by emp_a001.emp_id " +
            "  order by A02025 ",nativeQuery = true)
    public List<Map<String, Object>> getA02ByDepid(String Depid);

    @Modifying
    @Query(value = "update emp_a02 set A02025=?2 where sub_id=?1",nativeQuery = true)
    public int updateA02Data(String subid,int A02025);
}

