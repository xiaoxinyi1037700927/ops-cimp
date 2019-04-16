package com.sinosoft.ops.cimp.repository.user;

import com.sinosoft.ops.cimp.entity.sys.oraganization.BusinessUnitOrg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface BusinessUnitOrgRepository extends JpaRepository<BusinessUnitOrg,String>
        , QuerydslPredicateExecutor<BusinessUnitOrg> {

    /**
     * 根据业部门id查询数量
     * @param businessUnitId
     * @return
     */
    long countByBusinessUnitId(String businessUnitId);

    /**
     * 根据业务部门删除
     * @param businessUnitId
     */
    void deleteByBusinessUnitId(String businessUnitId);

    /**
     * 查询所有需配置的业务部门
     * @return
     */
    @Query("select organizationId from BusinessUnitOrg")
    List<String> findOrganizationIdList();

    /**
     * 根据业务ID查询关联单位列表
     * @param businessUnitId
     * @return
     */
    List<BusinessUnitOrg> findByBusinessUnitId(String businessUnitId);
}
