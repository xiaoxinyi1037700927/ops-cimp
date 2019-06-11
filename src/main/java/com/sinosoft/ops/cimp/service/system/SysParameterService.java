/**
 * @project:     IIMP
 * @title:          SysParameterService.java
 * @copyright: © 2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.service.system;


import com.sinosoft.ops.cimp.common.service.BaseEntityService;
import com.sinosoft.ops.cimp.entity.system.SysParameter;

import java.util.List;

/**
 * @classname:  SysParameterService
 * @description: 系统参数服务接口
 * @author:        Nil
 * @date:            2018年4月12日 上午9:15:03
 * @version        1.0.0
 */
public interface SysParameterService extends BaseEntityService<SysParameter> {
    List<SysParameter> getByScope(String scope);
}
