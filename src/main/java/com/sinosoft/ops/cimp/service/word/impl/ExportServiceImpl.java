package com.sinosoft.ops.cimp.service.word.impl;

import com.sinosoft.ops.cimp.dao.ExportDao;
import com.sinosoft.ops.cimp.service.code.SysCodeItemService;
import com.sinosoft.ops.cimp.service.common.impl.BaseServiceImpl;
import com.sinosoft.ops.cimp.service.word.ExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: BaseQueryServiceImpl
 * @ClassName: BaseQueryServiceImpl
 * @Description: 用于wordsql查询
 * @Author: zhangxp
 * @Date: 2017年11月13日 下午12:35:10
 * @Version 1.0.0
 */
@Service("exportWordService")
public class ExportServiceImpl extends BaseServiceImpl implements ExportService {
    private static final byte[] pwdBytes = "0123456789ABCDEF".getBytes();
    @Autowired
    @Qualifier("exportWordDao")
    private ExportDao exportWordDao = null;

    @Resource
    private SysCodeItemService sysCodeItemService;


    @Override
    @Transactional
    public List<Map<String, Object>> findBySQL(String sql) {
        try {

            return exportWordDao.findBySQL(sql);
        } catch (Exception e) {
            System.out.println("执行查询sql错误：" + e);
            System.out.println("错误sql：" + sql);
            return null;
        }
    }

    @Override
    @Transactional
    public List<Map<String, Object>> findBySQL1(String sql, String empId) {
        try {
            return exportWordDao.findBySQL1(sql, empId);
        } catch (Exception e) {
            System.out.println("执行查询sql错误：" + e);
            System.out.println("错误sql：" + sql);
            return null;
        }
    }


}
