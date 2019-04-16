package com.sinosoft.ops.cimp.export.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class DaemonThread implements Runnable{
	
	  private final static Logger logger = LogManager.getLogger(DaemonThread.class);

		@Override
		public void run() {
			if(!Thread.currentThread().isAlive()){
				System.out.println("所有pdf导出完毕");
			}
		}

}
