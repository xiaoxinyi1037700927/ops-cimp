package com.sinosoft.ops.cimp.schedule;

/**
 * 定时任务接口
 */
public interface Task {

    void exec();

    default int getOrder(){
        return 0;
    }
}
