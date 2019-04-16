package com.sinosoft.ops.cimp.service.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sinosoft.ops.cimp.entity.sys.user.ProjectPosition;
import com.sinosoft.ops.cimp.entity.sys.user.QProjectPosition;
import com.sinosoft.ops.cimp.entity.sys.user.QProjectPositionCountView;
import com.sinosoft.ops.cimp.repository.user.ProjectPositionRepository;
import com.sinosoft.ops.cimp.service.user.ProjectPositionService;
import com.sinosoft.ops.cimp.vo.to.user.RelPositionViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

/**
 * Created by Jay
 * date : 2018/9/4
 * des :
 */
@Service
public class ProjectPositionServiceImpl implements ProjectPositionService {

    @PersistenceContext
    private final EntityManager entityManager;
    private JPAQueryFactory queryFactory;

    @Autowired
    public ProjectPositionServiceImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @PostConstruct
    public void init() {
        queryFactory = new JPAQueryFactory(entityManager);
    }

    @Autowired
    private ProjectPositionRepository projectPositionRepository;

    @Override
    public ProjectPosition findById(String id) {
        ProjectPosition projectPosition = null;
        Optional<ProjectPosition> options = projectPositionRepository.findById(id);
        if (options.isPresent()) projectPosition = options.get();
        return projectPosition;
    }

    @Override
    public List<ProjectPosition> findByGroupId(String groupId) {
        List<ProjectPosition> projectPositionList = projectPositionRepository.findByGroupId(groupId);
        return projectPositionList;
    }

    @Override
    public List<RelPositionViewModel> findListByProjectId(String projectId) {

        QProjectPosition qProjectPosition = QProjectPosition.projectPosition;
        QProjectPositionCountView qProjectPositionCountView = QProjectPositionCountView.projectPositionCountView;

        BooleanBuilder builder = new BooleanBuilder();
        builder = builder.and(qProjectPosition.projectId.eq(projectId));

        QueryResults<RelPositionViewModel> queryResults = queryFactory.select(
                Projections.bean(
                        RelPositionViewModel.class,
                        qProjectPosition.id,
                        qProjectPosition.projectId,
                        qProjectPosition.organizationId,
                        qProjectPosition.organizationName,
                        qProjectPosition.organizationPositionId,
                        qProjectPosition.organizationPositionName,
                        qProjectPosition.selectCondition,
                        qProjectPosition.selectProgram,
                        qProjectPosition.selectRange,
                        qProjectPosition.selectType,
                        qProjectPositionCountView.positionNum.as("positionCadreCount")
                ))
                .from(qProjectPosition)
                .leftJoin(qProjectPositionCountView)
                .on(qProjectPosition.projectId.eq(qProjectPositionCountView.projectId),
                        qProjectPosition.organizationPositionId.eq(qProjectPositionCountView.positionId))
                .where(builder)
                .orderBy(qProjectPosition.createTime.asc())
                .fetchResults();

        List<RelPositionViewModel> relPositionViewModelList = queryResults.getResults();
        return relPositionViewModelList;
    }
}
