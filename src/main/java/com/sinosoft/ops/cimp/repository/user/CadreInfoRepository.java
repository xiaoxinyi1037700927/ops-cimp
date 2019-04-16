package com.sinosoft.ops.cimp.repository.user;


import com.sinosoft.ops.cimp.entity.sys.user.cadre.CadreInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface CadreInfoRepository extends JpaRepository<CadreInfo,String>, QuerydslPredicateExecutor<CadreInfo> {

    List<CadreInfo> findByIdentityNumber(String identityNum);

    List<CadreInfo> findByOrganizationId(String organizationId);
}
