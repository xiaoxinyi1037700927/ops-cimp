/**
 * @project:     iimp-gradle
 * @title:          AttrValueException.java
 * @copyright: ©2019 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.util.word.pattern.xinjiang;

/**
 * @ClassName: AttrValueException
 * @description: 属性值异常
 * @author:        Ni
 * @date:            2019年5月21日 下午3:10:11
 * @version        1.0.0
 * @since            JDK 1.7
 */
public class AttrValueException extends Exception {
    private static final long serialVersionUID = -8670626684254061063L;

    public AttrValueException() {
        super();
    }
    
    public AttrValueException(String message) {
        super(message);
    }
    
    public AttrValueException(String message,Throwable e) {
        super(message,e);
    }
}
