package com.sinosoft.ops.cimp.schedule;

/**
 * 定时任务接口
 */
public interface Task {

    boolean exec();

    default int getOrder() {
        return 0;
    }
}
