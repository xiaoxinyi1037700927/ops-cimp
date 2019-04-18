package com.sinosoft.ops.cimp.service.sys.homepagecount.impl;

import com.google.common.collect.Lists;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sinosoft.ops.cimp.entity.sys.homepagecount.HomePageCount;
import com.sinosoft.ops.cimp.entity.user.QRoleHomePageCount;
import com.sinosoft.ops.cimp.entity.user.RoleHomePageCount;
import com.sinosoft.ops.cimp.entity.user.User;
import com.sinosoft.ops.cimp.mapper.sys.homepagecount.HomePageCountMapper;
import com.sinosoft.ops.cimp.repository.sys.homepagecount.HomePageCountRepository;
import com.sinosoft.ops.cimp.repository.user.RoleHomePageCountRepository;
import com.sinosoft.ops.cimp.service.sys.homepagecount.HomePageCountService;
import com.sinosoft.ops.cimp.util.SecurityUtils;
import com.sinosoft.ops.cimp.vo.to.sys.homepagecount.HomePageCountQueryVO;
import com.sinosoft.ops.cimp.vo.to.sys.homepagecount.HomePageCountVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Service
public class HomePageCountServiceImpl implements HomePageCountService {

    @Autowired
    private HomePageCountRepository homePageCountRepository;
    @Autowired
    private RoleHomePageCountRepository roleHomePageCountRepository;

    @Resource
    private JdbcTemplate jdbc;

    @PersistenceContext
    private final EntityManager entityManager;
    private JPAQueryFactory queryFactory;

    @Autowired
    public HomePageCountServiceImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @PostConstruct
    public void init() {
        queryFactory = new JPAQueryFactory(entityManager);
    }


    @Override
    public List<HomePageCountVO> findAllHomePageCountVOList() {
        List<HomePageCount> all = homePageCountRepository.findAll();

        List<HomePageCountVO> homePageCountVOList = HomePageCountMapper.INSTANCE.poListToHomePageCountVOList(all);

        return homePageCountVOList;
    }


    @Override
    public List<HomePageCountQueryVO> findRoleHomePageCountQueryByRoleIdList(List<String> roleIdList) {
        //查询角色对应的统计信息
        List<RoleHomePageCount> roleHomePageCountList = this.findHomePageCountListByRoleIdList(roleIdList);
        //查询对应的统计项值
        List<HomePageCountQueryVO> homePageCountQueryList = this.findHomePageCountQueryList(roleHomePageCountList);

        return homePageCountQueryList;
    }

    public List<RoleHomePageCount> findHomePageCountListByRoleIdList(List<String> roleIdList) {
        QRoleHomePageCount qRoleHomePageCount = QRoleHomePageCount.roleHomePageCount;

        //查询角色对应的统计信息
        Iterable<RoleHomePageCount> roleHomePageCounts = roleHomePageCountRepository.findAll(qRoleHomePageCount.roleId.in(roleIdList));
        List<RoleHomePageCount> roleHomePageCountList = Lists.newArrayList(roleHomePageCounts);

        return roleHomePageCountList;
    }

    public List<HomePageCountQueryVO> findHomePageCountQueryList(List<RoleHomePageCount> roleHomePageCountList) {
        List<HomePageCountQueryVO> queryVOList = new ArrayList<>();

        for (RoleHomePageCount roleHomePageCount : roleHomePageCountList) {
            String countSql = roleHomePageCount.getCountSql();

            long countNumber = 0;
            countNumber = this.executeCountSql(countSql);


            HomePageCountQueryVO returnVO = new HomePageCountQueryVO();
            returnVO.setId(roleHomePageCount.getId());
            returnVO.setCountName(roleHomePageCount.getCountName());
            returnVO.setCountNumber(countNumber);
            queryVOList.add(returnVO);
        }

        return queryVOList;
    }

    public long executeCountSql(String countSql) {
        User currentUser = SecurityUtils.getSubject().getCurrentUser();
        String currentUserId = currentUser.getId();
        String currentUserOrgId = currentUser.getOrganizationId();
        String currentReserveCadreId = currentUser.getReserveCadreId();

        countSql = countSql.replace("{$我的账户编号}", currentUserId);
        countSql = countSql.replace("{$我的账户单位}", currentUserOrgId);
        if (!StringUtils.isEmpty(currentReserveCadreId)) {
            countSql = countSql.replace("{$我的账户干部编号}", currentReserveCadreId);
        }

        long count = jdbc.queryForObject(countSql,Long.class);
        return count;
    }


}
