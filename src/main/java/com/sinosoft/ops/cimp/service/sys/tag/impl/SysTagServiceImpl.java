package com.sinosoft.ops.cimp.service.sys.tag.impl;

import com.google.common.collect.Lists;
import com.sinosoft.ops.cimp.entity.sys.tag.QSysTag;
import com.sinosoft.ops.cimp.entity.sys.tag.SysTag;
import com.sinosoft.ops.cimp.repository.sys.tag.SysTagRepository;
import com.sinosoft.ops.cimp.service.sys.tag.SysTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysTagServiceImpl implements SysTagService {
    private final SysTagRepository sysTagRepository;

    @Autowired
    public SysTagServiceImpl(SysTagRepository sysTagRepository) {
        this.sysTagRepository = sysTagRepository;
    }

    @Override
    public List<SysTag> findAll() {
        return sysTagRepository.findAll();
    }

    @Override
    public List<SysTag> findAll(List<String> tagCategoryIds) {
        return Lists.newArrayList(sysTagRepository.findAll(QSysTag.sysTag.tagCategoryId.in(tagCategoryIds), QSysTag.sysTag.tagSort.asc()));
    }
}
