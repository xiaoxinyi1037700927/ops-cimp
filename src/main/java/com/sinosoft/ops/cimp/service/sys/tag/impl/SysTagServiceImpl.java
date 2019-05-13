package com.sinosoft.ops.cimp.service.sys.tag.impl;

import com.google.common.collect.Lists;
import com.sinosoft.ops.cimp.entity.sys.tag.CadreTag;
import com.sinosoft.ops.cimp.entity.sys.tag.QCadreTag;
import com.sinosoft.ops.cimp.entity.sys.tag.QSysTag;
import com.sinosoft.ops.cimp.entity.sys.tag.SysTag;
import com.sinosoft.ops.cimp.repository.sys.tag.CadreTagRepository;
import com.sinosoft.ops.cimp.repository.sys.tag.SysTagRepository;
import com.sinosoft.ops.cimp.service.sys.tag.SysTagService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SysTagServiceImpl implements SysTagService {
    private final SysTagRepository sysTagRepository;
    private final CadreTagRepository cadreTagRepository;

    @Autowired
    public SysTagServiceImpl(SysTagRepository sysTagRepository, CadreTagRepository cadreTagRepository) {
        this.sysTagRepository = sysTagRepository;
        this.cadreTagRepository = cadreTagRepository;
    }

    @Override
    public List<SysTag> findAll() {
        return sysTagRepository.findAll();
    }

    @Override
    public List<SysTag> findAll(List<String> tagCategoryIds) {
        return Lists.newArrayList(sysTagRepository.findAll(QSysTag.sysTag.tagCategoryId.in(tagCategoryIds), QSysTag.sysTag.tagSort.asc()));
    }

    public Optional<SysTag> findSysTag(String sysTagId) {
        return sysTagRepository.findOne(QSysTag.sysTag.id.eq(sysTagId));
    }

    @Override
    public SysTag save(SysTag sysTag) {
        return sysTagRepository.save(sysTag);
    }

    @Override
    public SysTag update(SysTag sysTag) {
        return sysTagRepository.save(sysTag);
    }

    @Override
    public void delete(String sysTagId) {
        if (StringUtils.isNotEmpty(sysTagId)) {
            sysTagRepository.deleteById(sysTagId);
            Iterable<CadreTag> tagIterable = cadreTagRepository.findAll(QCadreTag.cadreTag.tagId.eq(sysTagId));
            cadreTagRepository.deleteInBatch(tagIterable);
        }
    }


}
