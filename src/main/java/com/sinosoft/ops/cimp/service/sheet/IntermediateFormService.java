package com.sinosoft.ops.cimp.service.sheet;

import com.sinosoft.ops.cimp.common.service.BaseService;
import com.sinosoft.ops.cimp.exception.CloneException;

import java.util.Map;



public interface IntermediateFormService extends BaseService {

	/**生成中间表
	 * @throws CloneException */
	void generatorIntermediateForm(String tableName, String[] columns) throws CloneException;

	/**
	 * @param fieldDecMap 
	 * @throws CloneException */
	void generatorIntermediateForm(String setInfoName, String description, Map<String, String> itemMap, Map<String, String> fieldDecMap) throws CloneException;

}
