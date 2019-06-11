/**
 * @project:     iimp-gradle
 * @title:          SysCacheService.java
 * @copyright: ©2018 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.service.system;

import com.sinosoft.ops.cimp.common.service.BaseService;
import org.springframework.cache.Cache;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import java.util.Collection;



/**
 * @ClassName: SysCacheService
 * @description: 系统缓存服务接口
 * @author:        Ni
 * @date:            2018年7月10日 上午9:37:56
 * @version        1.0.0
 * @since            JDK 1.7
 */
public interface SysCacheService extends BaseService {
    /** 
     * 获取缓存
     * @param cacheName 缓存名称
     * @return 缓存对象
     * @author Ni
     * @date:    2018年7月11日 下午4:13:03
     * @since JDK 1.7
     */
    Cache getByName(String cacheName);
    
    /** 
     * 获取全部缓存名
     * @return 缓存名集
     * @author Ni
     * @date:    2018年11月27日 上午10:15:21
     * @since JDK 1.7
     */
    Collection<String> getAllNames();
    
    /** 
     * 重置缓存
     * @param cacheName 缓存名
     * @author Ni
     * @date:    2018年7月10日 上午11:38:33
     * @since JDK 1.7
     */
    void reset(String cacheName);
    /**
     * 重置缓存项
     * @param cacheName 缓存名
     * @param key 键
     * @author Ni
     * @date:    2018年7月10日 上午11:38:53
     * @since JDK 1.7
     */
    void reset(String cacheName, Object key);
    
    /** 
     * 重置本地缓存
     * @param cacheName 缓存名
     * @author Ni
     * @date:    2018年7月11日 上午11:34:24
     * @since JDK 1.7
     */
    void resetLocal(String cacheName);
    /** 
     * 重置本地缓存项
     * @param cacheName 缓存名
     * @param key 键
     * @author Ni
     * @date:    2018年7月11日 上午11:34:24
     * @since JDK 1.7
     */
    void resetLocal(String cacheName, Object key);
    
    /** 
     * 主题消息处理方法
     * @param message 消息
     * @throws JMSException
     * @author Ni
     * @date:    2018年12月5日 上午9:53:32
     * @since JDK 1.7
     */
    void onTopicMessage(MapMessage message) throws JMSException;
}
