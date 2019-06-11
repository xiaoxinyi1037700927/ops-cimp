/**
 * @project: IIMP
 * @title: SysCodeSetServiceImpl.java
 * @copyright: © 2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.service.sheet.impl;

import com.sinosoft.ops.cimp.common.service.BaseEntityServiceImpl;
import com.sinosoft.ops.cimp.entity.sheet.SysFunction;
import com.sinosoft.ops.cimp.service.sheet.SysFunctionService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @version 1.0.0
 * @classname: SysCodeSetServiceImpl
 * @description: 函数实现
 * @author: wangyg
 * @date: 20180103
 */
@Service("SysFunctionService")
public class SysFunctionServiceImpl extends BaseEntityServiceImpl<SysFunction> implements SysFunctionService {

    //	@Autowired
////	private SysFunctionDao sysFunctionDao;
//
//	@Transactional
//	@Override
    public List<SysFunction> findAll() {
//		return sysFunctionDao.findAll();
        return new ArrayList<>();
    }

}
