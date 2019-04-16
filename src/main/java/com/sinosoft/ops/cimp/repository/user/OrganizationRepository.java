package com.sinosoft.ops.cimp.repository.user;

import com.sinosoft.ops.cimp.entity.sys.oraganization.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Collection;
import java.util.List;

public interface OrganizationRepository extends JpaRepository<Organization, String>, QuerydslPredicateExecutor<Organization> {

    /**
     * 根据父ID查询
     *
     * @param organizationId
     * @return
     */
    List<Organization> findByParentId(String organizationId);

    List<Organization> findByParentIdOrderBySortNumber(String organizationId);

    /**
     * 根据父 CODE 模糊查询
     *
     * @param organizationCode
     * @return
     */
    List<Organization> findByParentCodeStartingWith(String organizationCode);

    /**
     * 根据ID IN查询
     *
     * @param organizationIdLst
     * @return
     */
    List<Organization> findByIdIn(Collection<String> organizationIdLst);

    /**
     * 根据父节点查询
     *
     * @param parentCode
     * @return
     */
    List<Organization> findByParentCode(String parentCode);

    List<Organization> findByParentCodeAndNameLike(String parentCode, String name);

    /**
     * 根据父节点查询
     *
     * @param codeList
     * @return
     */
    List<Organization> findByParentCodeIn(Collection<String> codeList);

    List<Organization> findByParentCodeInOrderBySortNumber(Collection<String> codeList);


    /**
     * 查询所有
     *
     * @return
     */
    @Query("select new Organization(id,name,code,parentCode,briefName,parentId,orgType,subOrdination,sortNumber) from Organization")
    List<Organization> findData();

    /**
     * 根据code查询
     *
     * @param code
     * @return
     */
    List<Organization> findByCode(String code);

    /**
     * 根据orgType查询
     * @param orgType
     * @return
     */
    List<Organization> findByOrgType(String orgType);
}
