/**
 * @project:     IIMP
 * @title:          BaseEntityController.java
 * @copyright: © 2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.controller;


import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

/**
 * @classname:  BaseEntityController
 * @description: 实体控制器基类
 * @author:        Nil
 * @date:            2017年8月1日 上午10:33:24
 * @param <T>
 * @version        1.0.0
 */
public abstract class BaseEntityController<T extends Serializable> extends BaseController {
    /**映射路径--创建*/
    public static final String MAPPING_PATH_CREATE="/create";
    /**映射路径--修改*/   
    public static final String MAPPING_PATH_UPDATE="/update";
    /**映射路径--删除*/
    public static final String MAPPING_PATH_DELETE="/delete";
    /**映射路径--根据Id修改*/
    public static final String MAPPING_PATH_DELETE_BY_ID="/deleteById";
    /**映射路径--根据Id获取*/
    public static final String MAPPING_PATH_GET_BY_ID="/getById";
    /**映射路径--分页查询*/
    public static final String MAPPING_PATH_FIND_BY_PAGE="/findByPage";
    
    /*** 泛型实体类 */
	protected Class<T> clazz;
	
	public Class<T> getClazz() {
		return clazz;
	}

	public void setClazz(Class<T> clazz) {
		this.clazz = clazz;
	}

	@SuppressWarnings("unchecked")
	public BaseEntityController() {
		ParameterizedType pt=(ParameterizedType)this.getClass().getGenericSuperclass();
		this.clazz=(Class<T>)pt.getActualTypeArguments()[0];
	}
	

}
