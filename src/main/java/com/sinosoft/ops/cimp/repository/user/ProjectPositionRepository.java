package com.sinosoft.ops.cimp.repository.user;


import com.sinosoft.ops.cimp.entity.sys.user.ProjectPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Collection;
import java.util.List;

/**
 * Created by Jay
 * date : 2018/9/4
 * des :
 */
public interface ProjectPositionRepository extends JpaRepository<ProjectPosition,String>, QuerydslPredicateExecutor<ProjectPosition> {

    /**
     * 根据方案ID 查询关联职务的单位
     * @param projectId
     * @return
     */
    List<ProjectPosition> findByProjectId(String projectId);

    /**
     * 根据方案ID 和 机构ID查询
     * @param projectId
     * @param organizationId
     * @return
     */
    List<ProjectPosition> findByProjectIdAndOrganizationId(String projectId, String organizationId);

    /**
     * 根据方案ID统计
     * @param projectId
     * @return
     */
    long countByProjectId(String projectId);

    /**
     * 根据方案ID删除
     * @param projectId
     */
    void deleteByProjectId(String projectId);

    void deleteByGroupId(String groupId);

    List<ProjectPosition> findByGroupId(String groupId);

    List<ProjectPosition> findByGroupIdInOrderByCreateTimeAsc(List<String> groupIdList);

    List<ProjectPosition> findByIdIn(Collection groupIdList);

    List<ProjectPosition> findByProjectIdOrderByCreateTimeAsc(String projectId);
}
