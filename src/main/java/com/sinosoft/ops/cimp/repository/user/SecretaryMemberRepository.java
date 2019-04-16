package com.sinosoft.ops.cimp.repository.user;


import com.sinosoft.ops.cimp.entity.sys.user.SecretaryMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface SecretaryMemberRepository extends JpaRepository<SecretaryMember,String>,
        QuerydslPredicateExecutor<SecretaryMember> {

    List<SecretaryMember> findByOrOrganizationId(String organizationId);
}
