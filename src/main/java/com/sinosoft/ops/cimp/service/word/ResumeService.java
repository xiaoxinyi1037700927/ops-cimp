/**
 * @project:     iimp-gradle
 * @title:          ResumeService.java
 * @copyright: ©2019 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.service.word;

/**
 * @ClassName: ResumeService
 * @description: 简历服务
 * @author:        Ni
 * @date:            2019年4月15日 下午1:24:11
 * @version        1.0.0
 * @since            JDK 1.7
 */
public interface ResumeService {
    /** 
     * 生成人员简历
     * @param empId 人员标识
     * @return 执行信息
     * @author Ni
     * @throws Exception 
     * @date:    2018年4月15日 下午12:24:56
     * @since JDK 1.7
     */
    String generateResume(String empId) throws Exception;
    
    /** 
     * 更新人员简历
     * @param empId 人员标识
     * @param resume 简历
     * @return 影响记录数
     * @author Ni
     * @date:    2019年4月15日 下午1:40:50
     * @since JDK 1.7
     */
    boolean updateResume(String empId, String resume);
}
