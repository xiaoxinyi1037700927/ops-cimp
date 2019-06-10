/**
 * @project:     iimp-gradle
 * @title:          ConcurrentTestService.java
 * @copyright: ©2018 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.service.sheet;

import com.sinosoft.ops.cimp.common.service.BaseService;

import javax.jms.JMSException;
import javax.jms.MapMessage;



/**
 * @ClassName: ConcurrentTestService
 * @description: 并行计算测试服务接口
 * @author:        Nil
 * @date:            2018年10月21日 下午10:11:51
 * @version        1.0.0
 * @since            JDK 1.7
 */
public interface ConcurrentTestService extends BaseService {
    /**
     * 启动并行任务（通过消息发布的方式）
     * @param reportNum 报表数
     * @param rowNum 行数
     * @param columnNum 列数
     * @author Nil
     * @date:    2018年10月21日 下午10:27:45
     * @since JDK 1.7
     */	
	void startConcurrentTask(int reportNum, int rowNum, int columnNum);
	
    /**
     * 启动非并行任务
     * @param reportNum 报表数
     * @param rowNum 行数
     * @param columnNum 列数
     * @author Nil
     * @date:    2018年10月21日 下午10:27:45
     * @since JDK 1.7
     */ 
    void startNonconcurrentTask(int reportNum, int rowNum, int columnNum);
    
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
