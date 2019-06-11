/**
 * @project:     IIMP
 * @title:          SysParameterServiceImpl.java
 * @copyright: © 2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.service.system.impl;


import com.sinosoft.ops.cimp.common.service.BaseEntityServiceImpl;
import com.sinosoft.ops.cimp.entity.system.SysParameter;
import com.sinosoft.ops.cimp.repository.system.SysParameterDao;
import com.sinosoft.ops.cimp.service.system.SysParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @classname:  SysParameterServiceImpl
 * @description: 系统参数服务实现类
 * @author:        Nil
 * @date:            2018年4月12日 上午9:15:26
 * @version        1.0.0
 */
@Service("sysParameterService")
public class SysParameterServiceImpl extends BaseEntityServiceImpl<SysParameter> implements SysParameterService {

    @Autowired
    SysParameterDao sysParameter=null;

    @Transactional
    public List<SysParameter> getByScope(String scope)
    {
        return sysParameter.getByScope(scope);
    }
}
