/**
 * @project: IIMP
 * @title: BaseServiceImpl.java
 * @copyright: © 2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.service.common.impl;

import com.sinosoft.ops.cimp.dao.BaseDao;
import com.sinosoft.ops.cimp.exception.CloneException;
import com.sinosoft.ops.cimp.service.common.BaseService;
import com.sinosoft.ops.cimp.vo.common.Treeable;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * @ClassName: BaseServiceImpl
 * @Description: 服务基实现类
 * @Author: Nil
 * @Date: 2017年9月25日 下午2:27:40
 * @Version 1.0.0
 */
@Service("baseService")
public class BaseServiceImpl implements BaseService {
    @SuppressWarnings("unused")
    @Autowired
    private BaseDao baseDao;

    /**
     * 将可树型化的对象集构建成树型结构
     * @param collection 可树型化的对象集
     * @return 根节点集合
     */
    public Collection<? extends Treeable> constructTree(Collection<? extends Treeable> collection) {
        //根节点
        Collection<Treeable> root = new LinkedList<>();
        //标识和对象映射表
        Map<Object, Treeable> id2Object = new HashMap<>(collection.size());
        //未找到父节点的对象集
        Collection<Treeable> notFoundParent = new LinkedList<>();

        //遍历找父节点
        for (Treeable o : collection) {
            o.setLeaf(true);
            id2Object.put(o.getId(), o);
            Object parentId = o.getParentId();
            if (parentId == null || "".equals(parentId) || "ROOT".equals(parentId)) {
                root.add(o);
            } else {
                Treeable parent = id2Object.get(o.getParentId());
                if (parent != null) {
                    parent.getChildren().add(o);
                    parent.setLeaf(false);
                } else {
                    notFoundParent.add(o);
                }
            }
        }
        //处理未找到父节点的
        for (Treeable o : notFoundParent) {
            Treeable parent = id2Object.get(o.getParentId());
            if (parent != null) {
                parent.getChildren().add(o);
                parent.setLeaf(false);
            } else {
                root.add(o);
            }
        }
        //将不必须的属性置空
        for (Map.Entry<Object, Treeable> entry : id2Object.entrySet()) {
            if (entry.getValue().getChildren() != null && entry.getValue().getChildren().isEmpty()) {
                entry.getValue().setChildren(null);
            }
            entry.getValue().setParentId(null);
        }
        return root;
    }

    /**
     * 克隆对象
     * @param obj 要克隆的对象
     * @return 克隆对象
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     */
    public Object cloneObject(Object object) throws CloneException {
        try {
            return BeanUtils.cloneBean(object);
        } catch (Exception e) {
            throw new CloneException(object, e);
        }
    }

    /**
     * 根据页号和每页数据行数计算起始记录
     * @param pageNo 页号
     * @param pageSize 每页数据行数
     * @return 起始记录
     */
    protected int calculateFirstResult(final int pageNo, final int pageSize) {
        return (pageNo - 1) * pageSize;
    }

    /**
     * 将java对象（必须实现序列化接口）写入到文件
     * @param obj
     * @param filename
     */
    public boolean writeObjectToFile(Object obj, String filename) {
        File file = new File(filename);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        FileOutputStream out;
        try {
            out = new FileOutputStream(file);
            ObjectOutputStream objOut = new ObjectOutputStream(out);
            objOut.writeObject(obj);
            objOut.flush();
            objOut.close();
            System.out.println("写入对象成功");
            return true;
        } catch (IOException e) {
            System.out.println("写入对象失败");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 从文件读取对象
     * @param filename
     * @return
     */
    public Object readObjectFromFile(String filename) {
        Object temp = null;
        File file = new File(filename);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        FileInputStream in;
        try {
            in = new FileInputStream(file);
            ObjectInputStream objIn = new ObjectInputStream(in);
            temp = objIn.readObject();
            objIn.close();
            System.out.println("读取对象成功");
        } catch (IOException e) {
            System.out.println("读取对象失败");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return temp;
    }
}
