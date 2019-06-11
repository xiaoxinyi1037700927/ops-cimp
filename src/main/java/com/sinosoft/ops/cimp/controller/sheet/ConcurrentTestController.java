/**
 * @project:     iimp-gradle
 * @title:          ConcurrentTestController.java
 * @copyright: ©2018 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.controller.sheet;

import com.sinosoft.ops.cimp.common.model.ResponseResult;
import com.sinosoft.ops.cimp.controller.BaseController;
import com.sinosoft.ops.cimp.service.sheet.ConcurrentTestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;



/**
 * @ClassName:  ConcurrentTestController
 * @description: 并发计算测试
 * @author:        Ni
 * @date:            2018年10月17日 下午5:33:11
 * @version        1.0.0
 * @since            JDK 1.7
 */
@Controller("concurrentTestController")
@RequestMapping("sheet/concurrentTest")
public class ConcurrentTestController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(ConcurrentTestController.class);
    
    @Autowired
    private ConcurrentTestService concurrentTestService = null;
    
    @ResponseBody
    @RequestMapping("/startConcurrentTask")
    public ResponseResult startConcurrentTask(@RequestParam int reportNum, @RequestParam int rowNum, @RequestParam int columnNum) {
        try {
        	concurrentTestService.startConcurrentTask(reportNum, rowNum, columnNum);
        	String message = "已提交"+reportNum+"个报表待计算，每个报表有"+rowNum+"行、"+columnNum+"列，共"+(rowNum*columnNum)+"个SQL";
        	return ResponseResult.success(message);
        }catch(Exception e) {
        	logger.error("任务提交失败！",e);
        	return ResponseResult.failure("任务提交失败！"+e.getMessage());
        }
    }
    
    @ResponseBody
    @RequestMapping("/startNonconcurrentTask")
    public ResponseResult startNonconcurrentTask(@RequestParam int reportNum,@RequestParam int rowNum,@RequestParam int columnNum) {
        try {
            concurrentTestService.startNonconcurrentTask(reportNum, rowNum, columnNum);
            String message = "已提交"+reportNum+"个报表待计算，每个报表有"+rowNum+"行、"+columnNum+"列，共"+(rowNum*columnNum)+"个SQL";
            return ResponseResult.success(message);
        }catch(Exception e) {
            logger.error("任务提交失败！",e);
            return ResponseResult.failure("任务提交失败！"+e.getMessage());
        }
    }
    
    @ResponseBody
    @RequestMapping("/blockTest")
    public ResponseResult blockTest(@RequestParam int seconds) {
    	try {
    		logger.debug(Thread.currentThread().getName()+" 阻塞开始--"+System.currentTimeMillis());
    		Thread.sleep(seconds*1000);
    		logger.debug(Thread.currentThread().getName()+" 阻塞结束--"+System.currentTimeMillis());
            return ResponseResult.success("阻塞测试完成--"+Thread.currentThread().getName());
        }catch(Exception e) {
            logger.error("阻塞测试异常！",e);
            return ResponseResult.failure("阻塞测试失败--"+Thread.currentThread().getName());
        }
	}
}
