package com.sinosoft.ops.cimp.service.sheet;

/**
 * @ClassName:  SheetPhotoExportService
 * @description: 头像导出接口
 * @author:        kanglin
 * @date:            2018年6月9日 下午
 * @version        1.0.0
 * @since            JDK 1.7
 */
public interface SheetPhotoExportService {
	
	//导出头像
	int export(String outputFilePath);

}
