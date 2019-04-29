package com.sinosoft.ops.cimp.service.sys.check.impl;

import com.sinosoft.ops.cimp.repository.sys.check.SysCheckConditionRepository;
import com.sinosoft.ops.cimp.service.sys.check.SysCheckService;
import com.sinosoft.ops.cimp.util.SecurityUtils;
import com.sinosoft.ops.cimp.vo.to.sys.check.SysCheckConditionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SysCheckServiceImpl implements SysCheckService {

    private final SysCheckConditionRepository sysCheckConditionRepository;

    @Autowired
    public SysCheckServiceImpl(SysCheckConditionRepository sysCheckConditionRepository) {
        this.sysCheckConditionRepository = sysCheckConditionRepository;
    }

    /**
     * 系统查错条件列表
     */
    @Override
    public List<SysCheckConditionModel> listSysCheckCondition() {
        return sysCheckConditionRepository.findAll(new Sort(Sort.Direction.ASC, "sort"))
                .stream().map(condition -> {
                    SysCheckConditionModel model = new SysCheckConditionModel();
                    model.setId(condition.getId());
                    model.setName(condition.getName());

                    return model;
                }).collect(Collectors.toList());
    }
}
