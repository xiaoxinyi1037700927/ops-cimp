package com.sinosoft.ops.cimp.service.export.impl;

import com.sinosoft.ops.cimp.dao.ExportDao;
import com.sinosoft.ops.cimp.service.common.impl.BaseServiceImpl;
import com.sinosoft.ops.cimp.service.export.ExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class ExportServiceImpl extends BaseServiceImpl implements ExportService {

    @Autowired
    @Qualifier("exportDao")
    private ExportDao exportWordDao;

    @Override
    @Transactional
    public List<Map<String, Object>> findBySQL(String sql) {
        try {
            return exportWordDao.findBySQL(sql);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
