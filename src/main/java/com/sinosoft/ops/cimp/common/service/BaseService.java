/**
 * @project: IIMP
 * @title: BaseService.java
 * @copyright: © 2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.common.service;

import com.sinosoft.ops.cimp.common.model.TreeNode;
import com.sinosoft.ops.cimp.exception.CloneException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

/**
 * @version 1.0.0
 * @ClassName: BaseService
 * @description: 服务基接口
 * @author: Nil
 * @date: 2017年8月1日 上午10:37:09
 */
public interface BaseService {
    /**
     * 从文件读取对象
     *
     * @param filename
     * @return
     * @throws IOException
     * @throws FileNotFoundException
     * @throws ClassNotFoundException
     */
    public Object readObjectFromFile(String filename) throws FileNotFoundException, IOException, ClassNotFoundException;

    /**
     * 将java对象（必须实现序列化接口）写入到文件
     *
     * @param obj
     * @param filename
     * @throws IOException
     * @throws FileNotFoundException
     */
    public boolean writeObjectToFile(Object obj, String filename) throws FileNotFoundException, IOException;

    /**
     * 克隆对象
     *
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
     *
     * @param collection 可树型化的对象集
     * @return 根节点集合
     */
    Collection<? extends TreeNode> constructTree(Collection<? extends TreeNode> collection);
}
