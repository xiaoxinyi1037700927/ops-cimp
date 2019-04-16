package com.sinosoft.ops.cimp.service.user.homePageCount.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sinosoft.ops.cimp.entity.sys.user.QRole;
import com.sinosoft.ops.cimp.entity.sys.user.QRoleHomePageCount;
import com.sinosoft.ops.cimp.entity.sys.user.RoleHomePageCount;
import com.sinosoft.ops.cimp.repository.user.RoleHomePageCountRepository;
import com.sinosoft.ops.cimp.service.user.homePageCount.RoleHomePageCountService;
import com.sinosoft.ops.cimp.vo.from.user.homePageCount.RoleHomePageCountModel;
import com.sinosoft.ops.cimp.vo.to.homePageCount.RoleHomePageCountVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Service
public class RoleHomePageCountServiceImpl implements RoleHomePageCountService {

    @Autowired
    private RoleHomePageCountRepository roleHomePageCountRepository;

    @PersistenceContext
    private final EntityManager entityManager;
    private JPAQueryFactory queryFactory;

    @Autowired
    public RoleHomePageCountServiceImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @PostConstruct
    public void init() {
        queryFactory = new JPAQueryFactory(entityManager);
    }


    @Override
    @Transactional
    public RoleHomePageCount addOrUpdateRoleCount(RoleHomePageCountModel model) {
        RoleHomePageCount po = new RoleHomePageCount();
        if (StringUtils.isEmpty(model.getId())) {
            BeanUtils.copyProperties(model, po);
        } else {
            Optional<RoleHomePageCount> one = roleHomePageCountRepository.findById(model.getId());
            if (one.isPresent()) {
                po = one.get();
                po.setCountName(model.getCountName());
                po.setCountSql(model.getCountSql());
            } else {
                return null;
            }
        }

        return roleHomePageCountRepository.save(po);
    }

    @Override
    @Transactional
    public boolean deleteRoleCountByRoleCountIdList(List<String> roleCountIdList) {
        roleHomePageCountRepository.deleteByIdIn(roleCountIdList);
        return true;
    }

    @Override
    public List<RoleHomePageCountVO> findRoleCountByRoleId(String roleId) {
        QRoleHomePageCount qRoleHomePageCount = QRoleHomePageCount.roleHomePageCount;
        QRole qRole = QRole.role;

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qRoleHomePageCount.roleId.eq(roleId));

        List<RoleHomePageCountVO> roleHomePageCountVOList = queryFactory.select(
                Projections.bean(
                        RoleHomePageCountVO.class,
                        qRoleHomePageCount.id,
                        qRoleHomePageCount.roleId,
                        qRoleHomePageCount.countName,
                        qRoleHomePageCount.countSql,
                        qRole.name.as("roleName")
                ))
                .from(qRoleHomePageCount)
                .leftJoin(qRole).on(qRole.id.eq(qRoleHomePageCount.roleId))
                .where(builder)
                .fetch();


        return roleHomePageCountVOList;
    }


}
