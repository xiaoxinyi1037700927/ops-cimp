package com.sinosoft.ops.cimp.util;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sinosoft.ops.cimp.entity.oraganization.Organization;
import com.sinosoft.ops.cimp.entity.oraganization.QOrganization;
import com.sinosoft.ops.cimp.mapper.organization.OrganizationMapper;
import com.sinosoft.ops.cimp.repository.oraganization.OrganizationRepository;
import com.sinosoft.ops.cimp.repository.sys.departmentInfo.DepB001Repository;
import com.sinosoft.ops.cimp.vo.from.DepB001;
import com.sinosoft.ops.cimp.vo.from.QDepB001;
import com.vip.vjtools.vjkit.number.MathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrganizationUtil {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private DepB001Repository depB001Dao;
    @Autowired
    private OrganizationRepository organizationDao;
    @PersistenceContext
    private final EntityManager entityManager;
    private JPAQueryFactory queryFactory;

    @Autowired
    public OrganizationUtil(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @PostConstruct
    public void init() {
        queryFactory = new JPAQueryFactory(entityManager);
    }

    //每个批次执行条数
    private static final int BATCH_SIZE = 1000;

    /***
     * 导入全部数据
     */
    @Transactional
    public void importData() {
        organizationDao.deleteAll();
        System.out.println("del all organization");

        int deptCount = (int) depB001Dao.count();
        int batchNumber = MathUtil.divide(deptCount, BATCH_SIZE, RoundingMode.UP);

        for (int i = 0; i < batchNumber; i++) {
            Page<DepB001> depB001Page = depB001Dao.findAll(QDepB001.depB001.depId.isNotEmpty(), PageRequest.of(i, BATCH_SIZE));
            List<DepB001> depB001List = depB001Page.getContent();

            //转换数据
            List<Organization> organizations = depB001List.stream().map(OrganizationMapper.INSTANCE::depB001ToOrganization).collect(Collectors.toList());
            organizationDao.saveAll(organizations);
        }

        QOrganization qOrganization = QOrganization.organization;
        List<String> codes = queryFactory.select(qOrganization.code).from(qOrganization).fetch();

        //这里执行速度慢 应该是上面的添加没有加到事务里
        for (String code : codes) {
//          "001.019.114.456"
//          "001.019.929.165.123"
            List<String> parIds = queryFactory
                    .select(qOrganization.id)
                    .from(qOrganization)
                    .where(qOrganization.code.eq(code))
                    .fetch();
            queryFactory.update(qOrganization)
                    .set(qOrganization.parentId, parIds.get(0))
                    .where(qOrganization.code.like(code + ".___"))
                    .execute();
        }
    }

    /***
     * 单个DEPB001数据添加和修改
     * @param id
     */
    public void execute(String id) {
        Optional<DepB001> depB001 = depB001Dao.findById(id);
        String code = depB001.get().getTreeLevelCode();
        String parentId = null;
        if (code.length() > 3) {
            String parentCode = code.substring(0, code.length() - 4);
            DepB001 parDepB001 = depB001Dao.findByTreeLevelCode(parentCode);
            parentId = parDepB001.getDepId();
        }
        Organization organization = OrganizationMapper.INSTANCE.depB001ToOrganization(depB001.get());
        organization.setParentId(parentId);
        organizationDao.save(organization);
    }

    /***
     * 删除organization对应数据
     * @param id
     */
    public void delDepB001(String id) {
        organizationDao.deleteById(id);
    }
}
