/**
 * @project: IIMP
 * @title: BaseServiceImpl.java
 * @copyright: © 2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.common.service;

import com.sinosoft.ops.cimp.common.dao.BaseDao;
import com.sinosoft.ops.cimp.common.model.TreeNode;
import com.sinosoft.ops.cimp.exception.CloneException;
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
     *
     * @param collection 可树型化的对象集
     * @return 根节点集合
     */
    public Collection<? extends TreeNode> constructTreeOrg(Collection<? extends TreeNode> collection) {
        //根节点
        Collection<TreeNode> root = new LinkedList<TreeNode>();
        //标识和对象映射表
        Map<Object, TreeNode> id2Object = new HashMap<Object, TreeNode>(collection.size());
        //未找到父节点的对象集
        Collection<TreeNode> notFoundParent = new LinkedList<TreeNode>();

        //遍历找父节点
        for (TreeNode o : collection) {
            o.setLeaf(true);
            id2Object.put(o.getId(), o);
            Object parentId = o.getParentId();
            if (parentId == null || "".equals(parentId) || "ROOT".equals(parentId)) {
                root.add(o);
            } else {
                TreeNode parent = id2Object.get(o.getParentId());
                if (parent != null) {
                    parent.getChildren().add(o);
                    parent.setLeaf(false);
                } else {
                    notFoundParent.add(o);
                }
            }
        }
        //处理未找到父节点的
        for (TreeNode o : notFoundParent) {
            TreeNode parent = id2Object.get(o.getParentId());
            if (parent != null) {
                parent.getChildren().add(o);
                parent.setLeaf(false);
            } else {
                root.add(o);
            }
        }
        //将不必须的属性置空
        for (Map.Entry<Object, TreeNode> entry : id2Object.entrySet()) {
            if (entry.getValue().getChildren() != null && entry.getValue().getChildren().isEmpty()) {
                entry.getValue().setChildren(null);
            }
            entry.getValue().setParentId(null);
        }
        return root;
    }

    //根据集合构造树结构
    public Collection<? extends TreeNode> constructTree(Collection<? extends TreeNode> collection) {
        if (collection == null) {
            return null;
        }
        //标识和对象映射表
        Map<Object, TreeNode> id2Object = new HashMap<Object, TreeNode>(collection.size());
        //根节点
        Collection<TreeNode> root = new LinkedList<TreeNode>();
        //准备节点Map
        for (TreeNode o : collection) {
            id2Object.put(o.getId(), o);
        }
        //构造根结点集合
        for (TreeNode o : collection) {
            Object parentId = o.getParentId();
            if (parentId == null || "".equals(parentId) || "ROOT".equals(parentId) || id2Object.get(parentId) == null) {
                o.setLeaf(true);
                root.add(o);
            }
        }
        root = addChildren(root, root, collection);
        //将不必须的属性置空
        for (Map.Entry<Object, TreeNode> entry : id2Object.entrySet()) {
            if (entry.getValue().getChildren() != null && entry.getValue().getChildren().isEmpty()) {
                entry.getValue().setChildren(null);
            }
            entry.getValue().setParentId(null);
        }
        return root;
    }

    //递归查子节点
    private Collection<TreeNode> addChildren(Collection<TreeNode> root, Collection<TreeNode> thisLayer, Collection<? extends TreeNode> collection) {
        if (thisLayer != null) {
            for (TreeNode upItem : thisLayer) {
                Object upId = upItem.getId();
                if (upId != null) {
                    for (TreeNode colItem : collection) {
                        if (upId.equals(colItem.getParentId())) {
                            colItem.setLeaf(true);
                            upItem.getChildren().add(colItem);
                            upItem.setLeaf(false);
                        }
                    }
                }
                root = addChildren(root, upItem.getChildren(), collection);
            }
        }
        return root;
    }

    /**
     * 克隆对象
     *
     * @param object 要克隆的对象
     * @return 克隆对象
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     */
    public Object cloneObject(Object object) throws CloneException {
        try {
            return BeanUtils.cloneBean(object);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException
                | NoSuchMethodException e) {
            throw new CloneException(object, e);
        }
    }

    /**
     * 将java对象（必须实现序列化接口）写入到文件
     *
     * @param object
     * @param filename
     * @throws IOException
     * @throws FileNotFoundException
     */
    public boolean writeObjectToFile(Object object, String filename) throws FileNotFoundException, IOException {
        try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(new File(filename)))) {
            os.writeObject(object);
            os.flush();
            return true;
        }
    }

    /**
     * 从文件读取对象
     *
     * @param filename
     * @return
     * @throws IOException
     * @throws FileNotFoundException
     * @throws ClassNotFoundException
     */
    public Object readObjectFromFile(String filename) throws FileNotFoundException, IOException, ClassNotFoundException {
        try (ObjectInputStream is = new ObjectInputStream(new FileInputStream(new File(filename)))) {
            return is.readObject();
        }
    }
}
