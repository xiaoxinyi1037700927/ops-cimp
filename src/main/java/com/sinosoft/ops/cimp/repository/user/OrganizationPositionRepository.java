package com.sinosoft.ops.cimp.repository.user;


import com.sinosoft.ops.cimp.entity.sys.user.organization.OrganizationPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Collection;
import java.util.List;

public interface OrganizationPositionRepository extends JpaRepository<OrganizationPosition,String>
        , QuerydslPredicateExecutor<OrganizationPosition> {

    List<OrganizationPosition> findByOrganizationId(String organizationId);

    List<OrganizationPosition> findByIdIn(List<String> positionIdLst);


    List<OrganizationPosition> findByOrganizationIdInAndCodeIn(Collection<String> organizationIds, Collection<String> codes);

    List<OrganizationPosition> findByCodeIn(Collection<String> codes);

}
