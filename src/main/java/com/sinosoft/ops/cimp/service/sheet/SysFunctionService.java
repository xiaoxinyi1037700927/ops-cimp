package com.sinosoft.ops.cimp.service.sheet;

import com.sinosoft.ops.cimp.common.service.BaseEntityService;
import com.sinosoft.ops.cimp.entity.sheet.SysFunction;

import java.util.List;
import java.util.UUID;

public interface SysFunctionService extends BaseEntityService<SysFunction> {
    List<SysFunction> findAll();
}
