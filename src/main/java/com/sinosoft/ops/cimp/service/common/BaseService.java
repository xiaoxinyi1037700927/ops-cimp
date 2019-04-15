/**
 * @project: IIMP
 * @title: BaseService.java
 * @copyright: © 2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.service.common;

import com.sinosoft.ops.cimp.exception.CloneException;
import com.sinosoft.ops.cimp.vo.common.Treeable;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

/**
 * @classname: BaseService
 * @description: 服务基接口
 * @author: Nil
 * @date: 2017年8月1日 上午10:37:09
 * @version 1.0.0
 */
public interface BaseService {
    /**
     * 从文件读取对象
     * @param filename
     * @return
     */
    public Object readObjectFromFile(String filename);

    /**
     * 将java对象（必须实现序列化接口）写入到文件
     * @param obj
     * @param filename
     */
    public boolean writeObjectToFile(Object obj, String filename);

    /**
     * 克隆对象
     * @param obj 要克隆的对象
     * @return 克隆对象
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     */
    Object cloneObject(Object obj) throws CloneException;

    /**
     * 将可树型化的对象集构建成树型结构
     * @param collection 可树型化的对象集
     * @return 根节点集合
     */
    Collection<? extends Treeable> constructTree(Collection<? extends Treeable> collection);
}
