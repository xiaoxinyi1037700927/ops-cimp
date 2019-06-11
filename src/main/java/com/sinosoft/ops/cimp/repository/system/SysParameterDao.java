/**
 * @Project:      IIMP
 * @Title:          SysParameterDao.java
 * @Copyright: © 2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.repository.system;


import com.sinosoft.ops.cimp.common.dao.BaseEntityDao;
import com.sinosoft.ops.cimp.entity.system.SysParameter;

import java.util.List;

/** 
 * @classname:  SysParameterDao
 * @description: 系统参数数据访问接口
 * @author:        Nil
 * @date:            2018年4月12日 下午4:39:54
 * @version        1.0.0
 */
public interface SysParameterDao extends BaseEntityDao<SysParameter> {
    List<SysParameter> getByScope(String scope);
}
