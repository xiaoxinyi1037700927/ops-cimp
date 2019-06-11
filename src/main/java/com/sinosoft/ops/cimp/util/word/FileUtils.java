package com.sinosoft.ops.cimp.util.word;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileUtils {

	private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);
	
	
	/**
	 * 
	 * 将文件输出到客户端
	 * @param outPath	文件路径
	 * @param response
	 * @author sunch
	 * @date:    2018年6月4日 下午6:06:11
	 * @since JDK 1.7
	 */
	public static void writeFile(String filePath,HttpServletResponse response){
		 FileInputStream fis = null;
	     OutputStream os = null;
	        try {
	            os = response.getOutputStream();
	            fis = new FileInputStream(filePath);
	           writeFile(fis, os);
	        } catch (Exception e) {
	            e.printStackTrace();
	            throw new RuntimeException();
	        } 
	}
	
	public static void writeFile(InputStream in,OutputStream out){
		try {
			byte[] b = new byte[1024 * 10];
			int i = 0;
			while ((i = in.read(b)) > 0) {
			    out.write(b, 0, i);
			}
			out.flush();
			out.close();
		} catch (IOException e) {
			logger.error("写文件失败",e);
			throw new RuntimeException();
		}finally {
            try {
            	if (in != null){
            		in.close();
            	}
            	if(out != null){
            		 out.close();
            	}
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
	}
	
	public static void writeFile(String filePath,String outPath){
		FileInputStream fis = null;
	    OutputStream os = null;
        try {
            os = new FileOutputStream(outPath);
            fis = new FileInputStream(filePath);
            writeFile(fis, os);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        } 
	}
	
}