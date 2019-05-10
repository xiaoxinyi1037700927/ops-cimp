package com.sinosoft.ops.cimp.service.cadre.impl;

import com.sinosoft.ops.cimp.export.ExportManager;
import com.sinosoft.ops.cimp.export.handlers.impl.ExportGbrmbHtmlBiJie;
import com.sinosoft.ops.cimp.export.handlers.impl.ExportGbrmbWordBiJie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AsyncGenerateGbrmb {
    private static final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);
    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncGenerateGbrmb.class);

    public static void execute(String... empIds) {
        if (empIds != null) {
            for (String empId : empIds) {
                executorService.submit(() -> {
                    try {
                        ExportManager.generate(new ExportGbrmbHtmlBiJie(String.valueOf(empId)), true);
                        ExportManager.generate(new ExportGbrmbWordBiJie(String.valueOf(empId)), true);
                    } catch (Exception e) {
                        LOGGER.error("异步生成干部任免表html失败", e);
                    }
                });
            }
        }
    }

}
