package com.sinosoft.ops.cimp.service.word;

import com.sinosoft.ops.cimp.common.service.BaseService;
import com.sinosoft.ops.cimp.entity.sys.syscode.SysCodeItem;
import com.sinosoft.ops.cimp.repository.mongodb.ex.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;



/**
 * 
 * @ClassName:  BaseQueryService
 * @Description: 用于word导出sql查询
 * @Author:        zhangxp
 * @Date:            2017年11月13日 下午12:35:39
 * @Version        1.0.0
 */
public interface ExportService extends BaseService {

	public List<Map<String, Object>> findBySQL(String sql);
	public void downLoadPhotoFile(String id, File file) throws DownloadResourceFromMongoDbError, CannotFindMongoDbResourceById;
	public void downloadFileToStreamDecryptWithAES(String id, OutputStream os) throws DownloadResourceFromMongoDbError, CannotFindMongoDbResourceById;
	public List<String> getAllEmpIds();
	public boolean saveResumeByEmpId(String empId, String resume);
	public void generatorAndExportLiangWeiPDF(HttpServletRequest request, HttpServletResponse response) throws Exception;
	public List<String> getLWEmpIds();
	public String getStringEmps(String type);
	
	/** 
	 * 更新公务员简历
	 * @return 执行信息
	 * @author Ni
	 * @date:    2018年4月15日 下午12:24:56
	 * @since JDK 1.7
	 */
	String updateResume();
	
    /** 
     * 更新连队两委人员简历
     * @return 执行信息
     * @author Ni
     * @date:    2018年4月15日 下午12:24:56
     * @since JDK 1.7
     */	
	String updateLWResume();
	
    /**
     * 根据代码集名称和代码获取代码项
     * @param codeSetName 代码集名称
     * @param code 代码
     * @return 代码项
     */
	SysCodeItem getCodeItemByCode(String codeSetName, String code);
}
