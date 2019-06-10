package com.sinosoft.ops.cimp.controller.sheet;


import com.sinosoft.ops.cimp.common.model.ResponseResult;
import com.sinosoft.ops.cimp.controller.BaseController;
import com.sinosoft.ops.cimp.entity.sheet.IntermediateFormAnalyze;
import com.sinosoft.ops.cimp.report.excel.ExcelReader;
import com.sinosoft.ops.cimp.service.sheet.IntermediateFormService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.util.Iterator;

/**
 * 中间表生成控制器
 * 上传文件的静态页面地址：
 * 			/sheet/intermediateForm/intermediate.html
 */

@RequestMapping("/sheet/intermediateForm")
@Controller
public class IntermediateFormController extends BaseController {
	
	private static final Logger logger = LoggerFactory.getLogger(IntermediateFormController.class);
	

	@Autowired 
	private IntermediateFormService intermediateFormService;
	
	
	
	@RequestMapping("/generatorIntermediateForm")
	@ResponseBody
	public ResponseResult generatorIntermediateForm(HttpServletRequest request, HttpServletResponse response){
		String filePath = "C:\\Users\\sunch\\Desktop\\中间表\\column3423s.txt";
		File file = new File(filePath);
		logger.info("文件名称："+file.getName().substring(0, file.getName().indexOf(".")));
		String fileName = file.getName().substring(0, file.getName().indexOf("."));
		try {
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			StringBuilder sb = new StringBuilder();
			String s = "";
			while((s=bufferedReader.readLine())!=null){
				logger.info("读取行："+s);
				sb.append(s+" ");
			}
			bufferedReader.close();
			logger.info("文件内容："+sb.toString());
			String[] split = sb.toString().split("\t");
			
			//插入infoset表
			//StringBuilder iis = new StringBuilder();
			//intermediateFormService.generatorIntermediateForm(fileName,split);
			
			
			return ResponseResult.success();
		} catch (Exception e) {
			logger.error("TODO:异常描述",e);
			return ResponseResult.failure();
		}
		
	}
	
	/**
	 * 
	 * 上传excel生成中间表
	 * 	sheet页名称命名方式：中文名称_英文名称   
	 * 		例： 测试_test
	 * 			
	 */
	@RequestMapping("/generatorIntermediateForm1")
	@ResponseBody
	public ResponseResult generatorIntermediateForm1(HttpServletRequest request, HttpServletResponse response){
		
		try {
			CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getServletContext());
			//检查form中是否有enctype="multipart/form-data"
			if(multipartResolver.isMultipart(request)){
				//将request变成多部分request
	            MultipartHttpServletRequest multiRequest=(MultipartHttpServletRequest)request;
	            //获取multiRequest 中所有的文件名
	            Iterator<String> iter=multiRequest.getFileNames();
	            while(iter.hasNext()){
	            	//一次遍历所有文件
	            	String fileName = iter.next().toString();
	                MultipartFile file=multiRequest.getFile(fileName);
	                InputStream inputStream = file.getInputStream();
	                ExcelReader excelReader = new ExcelReader(inputStream, file.getOriginalFilename());
	                
	                int i = 0;
	    			while(excelReader.nextSheet()){
	    				String sheetName = excelReader.getSheetName(i);
	    				logger.info("sheetname:"+sheetName);
	    				String[] readData = excelReader.readData(0);
	    				for (String string : readData) {
	    					System.out.println(string);
	    				}
	    				String[] readData2 = excelReader.readData(1);
	    				for (String string : readData2) {
	    					System.out.println(string);
	    				}
	    				i++;
	    				intermediateFormService.generatorIntermediateForm(sheetName,readData2);
	    			}
	            }
			}
			return ResponseResult.success();
		} catch (Exception e) {
			logger.error("TODO:异常描述",e);
			return ResponseResult.failure();
		}
		
	}
	
	/**
	 * 
	 * 上传excel生成中间表
	 * 	sheet页名称命名方式：中文名称_英文名称   
	 * 		例： 测试_test
	 * 			
	 */
	@RequestMapping("/generatorIntermediateForm2")
	@ResponseBody
	public ResponseResult generatorIntermediateForm2(HttpServletRequest request, HttpServletResponse response){
		
		try {
			CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getServletContext());
			//检查form中是否有enctype="multipart/form-data"
			if(multipartResolver.isMultipart(request)){
				//将request变成多部分request
	            MultipartHttpServletRequest multiRequest=(MultipartHttpServletRequest)request;
	            //获取multiRequest 中所有的文件名
	            Iterator<String> iter=multiRequest.getFileNames();
	            while(iter.hasNext()){
	            	//一次遍历所有文件
	            	String fileName = iter.next().toString();
	                MultipartFile file=multiRequest.getFile(fileName);
	                InputStream inputStream = file.getInputStream();
	                IntermediateFormAnalyze analyze = new IntermediateFormAnalyze(inputStream, file.getOriginalFilename());
	                
	                intermediateFormService.generatorIntermediateForm(analyze.getSetInfoName(),analyze.getDescription(),analyze.getItemMap(),analyze.getFieldDecMap());
	                while(analyze.next()){
	                	intermediateFormService.generatorIntermediateForm(analyze.getSetInfoName(),analyze.getDescription(),analyze.getItemMap(),analyze.getFieldDecMap());
	                }
	            }
			}
			return ResponseResult.success();
		} catch (Exception e) {
			logger.error("TODO:异常描述",e);
			return ResponseResult.failure();
		}
		
	}
}
