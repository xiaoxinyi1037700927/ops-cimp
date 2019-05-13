package com.sinosoft.ops.cimp.service.sys.tag.impl;

import com.google.common.collect.Lists;
import com.sinosoft.ops.cimp.constant.OpsErrorMessage;
import com.sinosoft.ops.cimp.entity.sys.tag.QSysTag;
import com.sinosoft.ops.cimp.entity.sys.tag.QSysTagCategory;
import com.sinosoft.ops.cimp.entity.sys.tag.SysTagCategory;
import com.sinosoft.ops.cimp.exception.BusinessException;
import com.sinosoft.ops.cimp.repository.sys.tag.SysTagCategoryRepository;
import com.sinosoft.ops.cimp.repository.sys.tag.SysTagRepository;
import com.sinosoft.ops.cimp.service.sys.tag.SysTagCategoryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysTagCategoryServiceImpl implements SysTagCategoryService {

    private final SysTagCategoryRepository sysTagCategoryRepository;
    private final SysTagRepository sysTagRepository;

    @Autowired
    public SysTagCategoryServiceImpl(SysTagCategoryRepository sysTagCategoryRepository, SysTagRepository sysTagRepository) {
        this.sysTagCategoryRepository = sysTagCategoryRepository;
        this.sysTagRepository = sysTagRepository;
    }

    @Override
    public List<SysTagCategory> findAllByModelName(String modelName) {
        return Lists.newArrayList(sysTagCategoryRepository.findAll(QSysTagCategory.sysTagCategory.tagModel.eq(modelName.trim()), QSysTagCategory.sysTagCategory.tagCategorySort.asc()));
    }

    @Override
    public SysTagCategory save(SysTagCategory sysTagCategory) {
        if (sysTagCategory != null) {
            return sysTagCategoryRepository.save(sysTagCategory);
        }
        return null;
    }

    @Override
    public SysTagCategory update(SysTagCategory sysTagCategory) {
        if (sysTagCategory != null && sysTagCategory.getId() != null) {
            return sysTagCategoryRepository.save(sysTagCategory);
        }
        return null;
    }

    @Override
    public void delete(String sysTagCategoryId) throws BusinessException {
        if (StringUtils.isNotEmpty(sysTagCategoryId)) {
            boolean exists = sysTagRepository.exists(QSysTag.sysTag.tagCategoryId.eq(sysTagCategoryId));
            if (exists) {
                throw new BusinessException(OpsErrorMessage.MODULE_NAME, OpsErrorMessage.ERROR_MESSAGE, "标签分类下有标签项请先删除标签项");
            }
            sysTagCategoryRepository.deleteById(sysTagCategoryId);
        }
    }
}
