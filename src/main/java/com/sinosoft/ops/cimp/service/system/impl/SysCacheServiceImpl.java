/**
 * @project:     iimp-gradle
 * @title:          SysCacheServiceImpl.java
 * @copyright: ©2018 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.service.system.impl;


import com.sinosoft.ops.cimp.common.service.BaseServiceImpl;
import com.sinosoft.ops.cimp.service.system.SysCacheService;
import com.sinosoft.ops.cimp.service.system.SysReentryService;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName: SysCacheServiceImpl
 * @description: 系统缓存同步服务实现类
 * @author:        Ni
 * @date:            2018年7月10日 上午9:57:23
 * @version        1.0.0
 * @since            JDK 1.7
 */
@Service("sysCacheService")
public class SysCacheServiceImpl extends BaseServiceImpl implements SysCacheService {
    public static final String MQ_TOPIC_NAME = "iimp.sysCache.topic";
    private static AtomicInteger instanceNum = new AtomicInteger(0);

    private final EhCacheCacheManager cacheManager;
    
    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    private SysReentryService sysReentryService;
    
    @Autowired
    public SysCacheServiceImpl(EhCacheCacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }
    
    @Override
    public Cache getByName(String cacheName) {
        return cacheManager.getCache(cacheName);
    }
    
    @Async
    @Override
    public void reset(String cacheName) {
        reset(cacheName,null);
    }
    @Async
    @Override
    public void reset(String cacheName, Object key) {
        resetLocal(cacheName,key);
        restOther(cacheName,key);
    }
    
    public void restOther(String cacheName, Object key) {
        jmsTemplate.send(new ActiveMQTopic(MQ_TOPIC_NAME), new MessageCreator() {  
            @Override
            public Message createMessage(Session session) throws JMSException {
                MapMessage message = session.createMapMessage();
                message.setString("cacheName", cacheName);
                if(key!=null) {
                    message.setString("key", key.toString());
                }
                return message;
            }
        });
    }
    
    @Override
    public void resetLocal(String cacheName) {
        resetLocal(cacheName,null);
    }
    @Override
    public void resetLocal(String cacheName, Object key) {
        Cache cache = getByName(cacheName);
        if(cache!=null) {
            if(key!=null) {
                cache.evict(key);
            }else {
                cache.clear();
            }
        }
    }
    
    @Override
    @JmsListener(destination=MQ_TOPIC_NAME, containerFactory="jmsTopicListenerContainerFactory")
    public void onTopicMessage(MapMessage message) throws JMSException {
        if(!sysReentryService.isReentry(message.getJMSMessageID())) {
            resetLocal(message.getString("cacheName"),message.getString("key"));
        }
    }

    @PostConstruct
    public void postConstruct(){
        instanceNum.incrementAndGet();
    }

    @PreDestroy
    public void preDestroy(){
        if(0==instanceNum.decrementAndGet()) {//最一个实例负责清空缓存
            for (String name : cacheManager.getCacheNames()) {
                cacheManager.getCache(name).clear();
            }
        }
    }

    @Override
    public Collection<String> getAllNames() {
        return cacheManager.getCacheNames();
    }
}
