package com.sinosoft.ops.cimp.service.sys.tag.impl;

import com.google.common.collect.Lists;
import com.sinosoft.ops.cimp.entity.sys.tag.QSysTagCategory;
import com.sinosoft.ops.cimp.entity.sys.tag.SysTagCategory;
import com.sinosoft.ops.cimp.repository.sys.tag.SysTagCategoryRepository;
import com.sinosoft.ops.cimp.service.sys.tag.SysTagCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysTagCategoryServiceImpl implements SysTagCategoryService {

    private final SysTagCategoryRepository sysTagCategoryRepository;

    @Autowired
    public SysTagCategoryServiceImpl(SysTagCategoryRepository sysTagCategoryRepository) {
        this.sysTagCategoryRepository = sysTagCategoryRepository;
    }

    @Override
    public List<SysTagCategory> findAllByModelName(String modelName) {
        return Lists.newArrayList(sysTagCategoryRepository.findAll(QSysTagCategory.sysTagCategory.tagModel.eq(modelName.trim()), QSysTagCategory.sysTagCategory.tagCategorySort.asc()));
    }
}
