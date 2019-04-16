package com.sinosoft.ops.cimp.service.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sinosoft.ops.cimp.dto.PaginationViewModel;
import com.sinosoft.ops.cimp.entity.sys.oraganization.Organization;
import com.sinosoft.ops.cimp.entity.sys.oraganization.QOrganization;
import com.sinosoft.ops.cimp.entity.sys.user.QSecretaryMember;
import com.sinosoft.ops.cimp.entity.sys.user.SecretaryMember;
import com.sinosoft.ops.cimp.entity.sys.user.cadre.QCadreInfo;
import com.sinosoft.ops.cimp.repository.user.SecretaryMemberRepository;
import com.sinosoft.ops.cimp.service.user.SecretaryMemberService;
import com.sinosoft.ops.cimp.util.CachePackage.OrganizationCacheManager;
import com.sinosoft.ops.cimp.util.SecurityUtils;
import com.sinosoft.ops.cimp.vo.from.user.organization.SecretaryMemberAddViewModel;
import com.sinosoft.ops.cimp.vo.from.user.organization.SecretaryMemberModifyViewModel;
import com.sinosoft.ops.cimp.vo.from.user.organization.SecretaryMemberSearchViewModel;
import com.sinosoft.ops.cimp.vo.user.organization.SecretaryMemberViewModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Service
public class SecretaryMemberServiceImpl implements SecretaryMemberService {

    @PersistenceContext
    private final EntityManager entityManager;
    private JPAQueryFactory queryFactory;

    @Autowired
    public SecretaryMemberServiceImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @PostConstruct
    public void init() {
        queryFactory = new JPAQueryFactory(entityManager);
    }

    @Autowired
    private SecretaryMemberRepository secretaryMemberRepository;

    @Override
    public PaginationViewModel<SecretaryMemberViewModel> findByPageData(SecretaryMemberSearchViewModel searchViewModel) {
        PaginationViewModel<SecretaryMemberViewModel> page = new PaginationViewModel<>();
        int pageIndex = searchViewModel.getPageIndex();
        int pageSize = searchViewModel.getPageSize();
        if (pageIndex <= 0) pageIndex = 1;
        if (pageSize <= 0) pageSize = 10;

        QSecretaryMember qSecretaryMember = QSecretaryMember.secretaryMember;
        QCadreInfo qCadreInfo = QCadreInfo.cadreInfo;
        QOrganization qOrganization = QOrganization.organization;
        BooleanBuilder builder = new BooleanBuilder();
        if (StringUtils.isNotBlank(searchViewModel.getOrganizationName())) {
            builder = builder.and(qOrganization.name.contains(searchViewModel.getOrganizationName()));
        }
        if (StringUtils.isNotBlank(searchViewModel.getCadreInfoName())) {
            builder = builder.and(qCadreInfo.name.contains(searchViewModel.getCadreInfoName()));
        }

        Organization organization = OrganizationCacheManager.getSubject().getOrganizationById(searchViewModel.getOrganizationId());
        if (organization != null) {
            if ("1".equals(searchViewModel.getContain())) {
                builder = builder.and(qOrganization.code.startsWith(organization.getCode()));
            } else {
                builder = builder.and(qOrganization.code.eq(organization.getCode()));
            }
        }
        QueryResults<SecretaryMemberViewModel> queryResults = queryFactory.select(Projections.bean(
                SecretaryMemberViewModel.class,
                qSecretaryMember.id,
                qSecretaryMember.organizationId,
                qSecretaryMember.cadreInfoId,
                qCadreInfo.name.as("cadreInfoName"),
                qOrganization.name.as("organizationName"),
                qCadreInfo.duty
        )).from(qSecretaryMember)
                .leftJoin(qCadreInfo).on(qSecretaryMember.cadreInfoId.eq(qCadreInfo.id))
                .leftJoin(qOrganization).on(qSecretaryMember.organizationId.eq(qOrganization.id))
                .where(builder)
                .orderBy(qSecretaryMember.organizationId.asc())
                .offset((pageIndex - 1) * pageSize)
                .limit(pageSize).fetchResults();
        long totalNum = queryResults.getTotal();
        List<SecretaryMemberViewModel> secretaryMemberViewModelList = queryResults.getResults();
        page.setData(secretaryMemberViewModelList);
        page.setTotalCount(totalNum);
        page.setPageIndex(pageIndex);
        page.setPageSize(pageSize);
        return page;
    }

    @Override
    public SecretaryMember findById(String secretaryMemberId) {
        SecretaryMember secretaryMember = null;
        Optional<SecretaryMember> options = secretaryMemberRepository.findById(secretaryMemberId);
        if (options.isPresent()) {
            secretaryMember = options.get();
        }
        return secretaryMember;
    }

    @Override
    @Transactional
    public boolean addSecretaryMember(SecretaryMemberAddViewModel addViewModel) {
        SecretaryMember secretaryMember = new SecretaryMember();
        secretaryMember.setCadreInfoId(addViewModel.getCadreInfoId());
        secretaryMember.setOrganizationId(addViewModel.getOrganizationId());
        secretaryMemberRepository.save(secretaryMember);
        return true;
    }

    @Override
    @Transactional
    public boolean modifySecretaryMember(SecretaryMemberModifyViewModel modifyViewModel) {
        SecretaryMember secretaryMember = this.findById(modifyViewModel.getId());
        secretaryMember.setOrganizationId(modifyViewModel.getOrganizationId());
        secretaryMember.setCadreInfoId(modifyViewModel.getCadreInfoId());
        return true;
    }

    @Override
    @Transactional
    public boolean deleteById(String secretaryMemberId) {
        secretaryMemberRepository.deleteById(secretaryMemberId);
        return true;
    }

    @Override
    public List<SecretaryMemberViewModel> findByOrganizationId() {
        String organizationId = SecurityUtils.getSubject().getCurrentUser().getDataOrganizationId();
        QSecretaryMember qSecretaryMember = QSecretaryMember.secretaryMember;
        QCadreInfo qCadreInfo = QCadreInfo.cadreInfo;

        List<SecretaryMemberViewModel> secretaryMemberViewModelList = queryFactory.select(Projections.bean(
                SecretaryMemberViewModel.class,
                qCadreInfo.id,
                qSecretaryMember.cadreInfoId,
                qCadreInfo.name.as("cadreInfoName"),
                qCadreInfo.duty
        ))
        .from(qSecretaryMember)
        .leftJoin(qCadreInfo).on(qSecretaryMember.cadreInfoId.eq(qCadreInfo.id))
        .where(qSecretaryMember.organizationId.eq(organizationId))
        .fetch();
        return secretaryMemberViewModelList;
    }
}
