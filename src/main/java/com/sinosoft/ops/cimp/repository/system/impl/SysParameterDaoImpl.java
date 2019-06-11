/**
 * @Project:      IIMP
 * @Title:          SysParameterDaoImpl.java
 * @Copyright: © 2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.repository.system.impl;


import com.sinosoft.ops.cimp.common.dao.BaseEntityDaoImpl;
import com.sinosoft.ops.cimp.entity.system.SysParameter;
import com.sinosoft.ops.cimp.repository.system.SysParameterDao;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.util.List;

/** 
 * @classname:  SysParameterDaoImpl
 * @description: 系统参数数据访问实现类
 * @author:        Nil
 * @date:            2018年4月12日 下午4:41:48
 * @version        1.0.0
 */
@Repository("sysParameterDao")
public class SysParameterDaoImpl extends BaseEntityDaoImpl<SysParameter> implements SysParameterDao {
    public SysParameterDaoImpl(EntityManagerFactory factory) {
        super(factory);
    }

    public List<SysParameter> getByScope(String scope)
    {
        return sessionFactory.getCurrentSession().createQuery("from SysParameter where scope=:scope").setParameter("scope",Byte.parseByte(scope)).list();
    }
}
