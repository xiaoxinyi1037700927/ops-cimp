/**
 * @project: IIMP
 * @title: InvalidConditionException.java
 * @copyright: © 2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.common.model;

/**
 * @version 1.0.0
 * @classname: InvalidConditionException
 * @description: 无效条件异常
 * @author: Nil
 * @date: 2017年11月12日 下午4:08:30
 */
public class InvalidConditionException extends Exception {
    private static final long serialVersionUID = 2901077414941731109L;

    public InvalidConditionException() {
        super();
    }

    public InvalidConditionException(String message) {
        super(message);
    }

    public InvalidConditionException(String message, Throwable e) {
        super(message, e);
    }
}
