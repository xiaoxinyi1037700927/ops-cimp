package com.sinosoft.ops.cimp.controller.archive;


import com.sinosoft.ops.cimp.controller.BaseController;
import com.sinosoft.ops.cimp.entity.archive.ArchiveMaterialFile;
import com.sinosoft.ops.cimp.exception.BusinessException;
import com.sinosoft.ops.cimp.service.archive.ArchiveMaterialFileService;
import com.sinosoft.ops.cimp.service.archive.ArchiveMaterialService;
import com.sinosoft.ops.cimp.service.archive.MongoDbService;
import com.sinosoft.ops.cimp.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller("archiveMaterialFileController")
@RequestMapping("/archiveMaterialFile")
public class ArchiveMaterialFileController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(ArchiveMaterialFileController.class);
	@Resource
	private MongoDbService mongoDbService;
	@Resource 
	private ArchiveMaterialFileService archiveMaterialFileService;
	@Resource 
	private ArchiveMaterialService archiveMaterialService;



	/**
	 * 根据人员ID+档案分类ID 获取 ArchiveMaterialFile集合
	 * @param request->empId 人员ID; 
	 * @param request->categoryId 档案分类ID;
	 * @author zhaizf
	 * @return ArchiveMaterialFile  保存路径、文件名信息
	 */
//	@RequestMapping("/findbyArchiveIDs")
	public void findbyArchiveIDS(HttpServletRequest request, HttpServletResponse response) throws BusinessException {
		try {
			String empId = request.getParameter("empId");
			String categoryId = request.getParameter("categoryId");
			//idList 指定人员+指定档案分类的 ArchiveMaterial ID集合
			List<String> idList = archiveMaterialService.getArchiveMaterialIDs(empId, categoryId);
			//archiveMaterialFileList  ID in idList 的ArchiveMaterial集合
			List<ArchiveMaterialFile> archiveMaterialFileList = (List<ArchiveMaterialFile>) archiveMaterialFileService.getArchiveMaterialFilebyAchiveMaterialIDs(idList);
			if(archiveMaterialFileList!=null&&archiveMaterialFileList.size()>0){
				String relPath = request.getSession().getServletContext().getRealPath("resources/download/");	//保存路径
				Map<String,Object> map = new HashMap<String,Object>() ;//返回 保存路径 和 文件名列表
				List<String> fileNameList = new ArrayList<String>();//文件名List

				String  id="";
				Path path=null;
				String fileName="";
				for (ArchiveMaterialFile entity : archiveMaterialFileList) {
					id =entity.getFileStorageRef();
					fileName=id+".jpg";
					fileNameList.add(fileName);//文件名List
					path = Paths.get(relPath,fileName);
					mongoDbService.downloadToFileDecryptWithAES(id, path);
				}
				//返回获取的文件名称List 和文件保存相对路径
				map.put("fileNameList",fileNameList);
				map.put("location", "resources/download/");
				writeJson(response, ok(map));
			}else{
				writeJson(response, fail("不存在Archive_Material_ID="+idList+"的记录"));
			}
		} catch (Exception e) {
			logger.error("查询失败！", e);
			writeJson(response, fail("查询失败"));
		}
	}

	/**
	 * 根据指定档案ID 和pageNo获取
	 * @param request->archiveMaterialId: ArchiveMaterialFile archiveMaterialId;
	 * @param request->pageNo: ArchiveMaterialFile pageNumber
	 * @author zhaizf
	 */
	@RequestMapping("/findbyArchiveMaterialIDAndPageNo")
	public void findbyArchiveMaterialIDAndPageNo(HttpServletRequest request, HttpServletResponse response) throws BusinessException {
		try {
			String archiveMaterialId =request.getParameter("archiveMaterialId");			
			String pageNo =request.getParameter("pageNo");
			String type =request.getParameter("type");

			if(StringUtil.isEmptyOrNull(pageNo)){
				pageNo="1";
			}
			ArchiveMaterialFile archiveMaterialFile = archiveMaterialFileService.findbypageNo(archiveMaterialId, pageNo,type);
			if(archiveMaterialFile!=null){
				//保存路径
				String relPath = request.getSession().getServletContext().getRealPath("resources/download/");
				//返回 保存路径 和 文件名列表
				Map<String,Object> map = new HashMap<String,Object>() ;
				String id = archiveMaterialFile.getFileStorageRef();
				String fileName=id+".jpg";
				Path path = Paths.get(relPath,fileName);
				mongoDbService.downloadToFileDecryptWithAES(id, path);
				map.put("location", "resources/download/" + fileName);
				writeJson(response, ok(map));
			}else{
				writeJson(response, ok("不存在Archive_Material_ID="+archiveMaterialId+"的记录"));
			}
		} catch (Exception e) {
			logger.error("查询失败！", e);
			writeJson(response, fail("查询失败"));
		}
	}

	/**
	 * 根据指定 ArchiveMaterialFile ID 获取单个ArchiveMaterialFile
	 * @param request->archiveMaterialId :ArchiveMaterialFile ID
	 * @return ArchiveMaterialFile
	 * @author zhaizf
	 */
	@RequestMapping("/getArchiveMaterialFileByID")
	public void getArchiveMaterialFileByID(HttpServletRequest request, HttpServletResponse response) throws BusinessException {
		try {
			String archiveMaterialId =request.getParameter("archiveMaterialId");
			ArchiveMaterialFile archiveMaterialFile = archiveMaterialFileService.getById(archiveMaterialId);
			if(archiveMaterialFile!=null){
				writeJson(response, ok(archiveMaterialFile));
			}else{
				writeJson(response,fail("不存在archiveMaterialId="+archiveMaterialId+"的记录"));
			}
		} catch (Exception e) {
			logger.error("查询失败！", e);
			writeJson(response, fail("查询失败"));
		}
	}

	/**
	 * 根据指定 Archive_Material_ID 获取 ArchiveMaterialFile集合 List<ArchiveMaterialFile>
	 * 
	 * @param request->archiveMaterialId  Archive_Material_ID
	 * @return ArchiveMaterialFile集合  List<ArchiveMaterialFile>
	 * @author zhaizf
	 */
	@RequestMapping("/getAMFileListByAMID")
	public void getArchiveMaterialFileByAchiveMaterialID(HttpServletRequest request, HttpServletResponse response) throws BusinessException {
		try {
			String archiveMaterialId =request.getParameter("archiveMaterialId");
			List<ArchiveMaterialFile> archiveMaterialFileList = (List<ArchiveMaterialFile>)archiveMaterialFileService.getArchiveMaterialFilebyAchiveMaterialID(archiveMaterialId);
			if(archiveMaterialFileList!=null&&archiveMaterialFileList.size()!=0){
				String relPath = request.getSession().getServletContext().getRealPath("resources/download/");
				Map<String,Object> map = new HashMap<String,Object>() ;
				List<String> fileNameList = new ArrayList<String>();
				String  id="";
				Path path=null;
				String fileName="";

				for (ArchiveMaterialFile amfile : archiveMaterialFileList) {
					id = amfile.getFileStorageRef();
					fileName=id+".jpg";
					fileNameList.add(fileName);//文件名List

					path = Paths.get(relPath,fileName);
					mongoDbService.downloadToFileDecryptWithAES(id, path);
				}
				map.put("fileNameList",fileNameList);
				map.put("location", "resources/download/");
				writeJson(response, ok(map));
			}else{
				writeJson(response, ok("不存在Archive_Material_ID="+archiveMaterialId+"的记录"));
			}
		} catch (Exception e) {
			logger.error("查询失败！", e);
			writeJson(response, fail("查询失败"));
		}
	}

	/**
	 * 根据多个Archive_Material_ID   获取ArchiveMaterialFile Map集合 
	 *
	 * @param request  Archive_Material_ID集
	 * @return ArchiveMaterialFile集合Map<String,  List<ArchiveMaterialFile>>
	 * @author zhaizf
	 */
	//	@RequestMapping("/getMapByAchiveMaterialIDs")
	public void getMapByAchiveMaterialIDs(HttpServletRequest request, HttpServletResponse response) throws BusinessException {
		try {
			String[] archiveMaterialIds =request.getParameterValues("archiveMaterialIds");
			List<String> idList = java.util.Arrays.asList(archiveMaterialIds); 
			Map<String,  List<ArchiveMaterialFile>> map = 
					archiveMaterialFileService.getMapByAchiveMaterialIDs(idList);
			if(map!=null){
				writeJson(response, ok(map));
			}else{
				writeJson(response, fail("不存在Archive_Material_ID IN"+archiveMaterialIds+"的记录"));
			}
		} catch (Exception e) {
			logger.error("查询失败！", e);
			writeJson(response, fail("查询失败"));
		}
	}

}
