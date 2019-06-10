package com.sinosoft.ops.cimp.util.word;

import com.aspose.words.*;
import com.sinosoft.ops.cimp.util.word.wordTemplate.WordTemplate;
import com.sinosoft.ops.cimp.common.model.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.zip.ZipOutputStream;



public class WordUtil {
	
	
	private static final Logger logger = LoggerFactory.getLogger(WordUtil.class);
	
	private static String rootPath;
	
	private static String path = "/resources/temp";
	
	static{
		String path = WordUtil.class.getClassLoader().getResource("").getPath();
		rootPath = path.substring(0, path.indexOf("/iimp-gradle")+12);
		System.out.println("-------------"+rootPath+"----------");
	}

	public static boolean getLicense(){
		boolean result = false;
		
		try {
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			InputStream in = loader.getResourceAsStream("license.xml");// 凭证文件
			License license = new License();
			license.setLicense(in);
			result = true;
		} catch (Exception e) {
			logger.error("aspose凭证认证失败", e);
		}
		
		return result; 
	}
	
	/**
	 * word 转 pdf （不加密）
	 */
	public static void doc2pdf(String inPath,String outPath){
		doc2pdf(inPath, outPath, false, null,null);
	}
	
	/**
	 * word 转 pdf  （为pdf加密码）
	 * 
	 * @param inPath
	 * @param outPath
	 * @param needPassword
	 * @param password
	 * @author sunch
	 * @date:    2018年6月4日 下午4:19:37
	 * @since JDK 1.7
	 */
	@SuppressWarnings("resource")
	public static void doc2pdf(String inPath,String outPath,boolean needPassword,String userPassword,String ownerPassword){
		if(!getLicense()){
			return;
		}
		FileInputStream fileInputStream = null;
		try {
			fileInputStream = new FileInputStream(inPath);
			Document document = new Document(fileInputStream);
			
			//加密
			PdfSaveOptions saveOption = new PdfSaveOptions();
			saveOption.setSaveFormat(SaveFormat.PDF);
			//user pass 设置了打开时，需要密码   
			//owner pass 控件编辑等权限  
            if(needPassword){
            	PdfEncryptionDetails encryptionDetails = new PdfEncryptionDetails(userPassword, ownerPassword, PdfEncryptionAlgorithm.RC_4_128);  
                encryptionDetails.setPermissions(PdfPermissions.DISALLOW_ALL);
                saveOption.setEncryptionDetails(encryptionDetails);
            }
			document.save(outPath, saveOption);
			
			//document.save(outPath, SaveFormat.PDF);
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("word转pdf失败",e);
		}finally {
			if(fileInputStream!=null){
				try {
					fileInputStream.close();
				} catch (IOException e) {
					//TODO Auto-generated catch block
					logger.error("TODO:异常描述",e);
				}
			}
		}
		
	}
	
	/**
	 * 
	 * word文档加密
	 * @param in
	 * @param out
	 * @param passWord
	 * @author sunch
	 * @date:    2018年9月5日 下午5:40:20
	 * @since JDK 1.7
	 */
	public static void word2encryption(InputStream in,OutputStream out,String passWord){
		try {
			Document document = new Document(in);
			DocSaveOptions saveOptions = new DocSaveOptions();
			saveOptions.setPassword(passWord);
			saveOptions.setSaveFormat(SaveFormat.DOC);
			document.save(out, saveOptions);
		} catch (Exception e) {
			logger.error("word加密失败",e);
		}
	}
	
	
	
	public  static String fillData2Templement(byte[] content,Collection<Map<String,Object>> collection) throws Exception{
		String filePath = rootPath+path+"/"+UUID.randomUUID().toString()+".docx";
		String zipFilePath = rootPath+path+"/"+UUID.randomUUID().toString()+".docx";
		ZipOutputStream zipout = null;
		try {
			File dir = new File(rootPath+path);
			if(!dir.exists())dir.mkdirs();
			zipout = new ZipOutputStream(new FileOutputStream(zipFilePath));
			
			WordTemplate temp = new WordTemplate(content, filePath, zipout);
			temp.fillData(collection);
			
	       return zipFilePath;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("解析word失败",e);
			throw new Exception();
		}finally {
			if(new File(filePath).exists())
				new File(filePath).delete();
			if(zipout != null)
				zipout.close();
		}
	}
	
	public static Collection<TreeNode> analyzeWord2Tree(byte[] content,Object rootId) throws Exception{
		String filePath = rootPath+path+"/"+UUID.randomUUID().toString()+".docx";
		File file = new File(filePath);
		try {
			File dir = new File(rootPath+path);
			if(!dir.exists())dir.mkdirs();
			
			WordTemplate temp = new WordTemplate(content, filePath);
			return temp.analyzeWord2Tree(content, rootId);

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("解析word失败",e);
			throw new Exception("解析word成树失败", e);
		}finally {
			if(file.exists())
				file.delete();
		}
	}
	
	
	
	public static void fillDataForEach() throws  Exception{
		ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream("C:\\Users\\sunch\\Desktop\\会议记录\\gbrmb_template222.docx"));
		String filePath = "C:\\Users\\sunch\\Desktop\\会议记录\\gbrmb_template111.docx";
		
		WordTemplate temp = new WordTemplate(filePath, zipOut);
		temp.fillDataForEach();
		zipOut.close();
	}

	public static void main(String[] args) throws Exception{
//		ZipOutputStream zipout = new ZipOutputStream(new FileOutputStream("C:\\Users\\sunch\\Desktop\\会议记录\\gbrmb_template222.docx"));
//		ZipFile zipFile = new ZipFile("C:\\Users\\sunch\\Desktop\\会议记录\\gbrmb_template111.docx");
//		
//		WordTemplate temp = new WordTemplate("C:\\Users\\sunch\\Desktop\\会议记录\\gbrmb_template111.docx", zipout);
//		temp.fillDataForEach();
		WordUtil.doc2pdf("C:\\software\\eclipseWorkSpace\\.metadata\\.plugins\\org.eclipse.wst.server.core\\\\tmp0\\wtpwebapps\\iimp-gradle\\\\resources\\\\temp\\59153ca5-d8be-4174-8504-88370bdf41b7.docx", 
				"C:\\software\\eclipseWorkSpace\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp0\\wtpwebapps\\iimp-gradle\\resources\\temp\\pdf\\59153ca5-d8be-4174-8504-88370bdf41b2.pdf");
	}
}
