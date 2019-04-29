package com.sinosoft.ops.cimp.schedule;

public interface Task {

    void exec();

    default int getOrder(){
        return 0;
    }
}
