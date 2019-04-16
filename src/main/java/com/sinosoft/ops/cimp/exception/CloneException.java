/**
 * @project:      IIMP
 * @title:          CloneException.java
 * @copyright: © 2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.exception;

/**
 * @classname:  CloneException
 * @description: 克隆异常
 * @author:        Nil
 * @date:            2017年11月12日 下午4:08:30
 * @version        1.0.0
 */
public class CloneException extends Exception {
    private static final long serialVersionUID = 2901077414941731109L;

    public CloneException() {
        super();
    }

    public CloneException(String message) {
        super(message);
    }
    
    public CloneException(String message,Throwable e) {
        super(message,e);
    }
    
    public CloneException(Object object,Throwable e) {
        super("克隆"+object+"["+object.getClass()+"]失败！",e);
    }
}
