package com.sinosoft.ops.cimp.service.user;


import com.sinosoft.ops.cimp.entity.sys.user.ProjectPosition;
import com.sinosoft.ops.cimp.vo.to.user.RelPositionViewModel;

import java.util.List;

/**
 * Created by Jay
 * date : 2018/9/4
 * des :
 */

public interface ProjectPositionService {
    /**
     * 根据方案Id查询方案职数
     * @param projectId
     * @return
     */
    List<RelPositionViewModel> findListByProjectId(String projectId);

    ProjectPosition findById(String id);

    List<ProjectPosition> findByGroupId(String groupId);
}
