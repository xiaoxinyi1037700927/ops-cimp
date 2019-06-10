package com.sinosoft.ops.cimp.service.word.impl;

import com.sinosoft.ops.cimp.common.service.BaseServiceImpl;
import com.sinosoft.ops.cimp.entity.sys.syscode.SysCodeItem;
import com.sinosoft.ops.cimp.repository.export.ExportDao;
import com.sinosoft.ops.cimp.repository.mongodb.MongoDbDao;
import com.sinosoft.ops.cimp.repository.mongodb.ex.CannotFindMongoDbResourceById;
import com.sinosoft.ops.cimp.repository.mongodb.ex.DownloadResourceFromMongoDbError;
import com.sinosoft.ops.cimp.service.archive.MongoDbService;
import com.sinosoft.ops.cimp.service.sys.syscode.SysCodeItemService;
import com.sinosoft.ops.cimp.service.word.ExportService;
import com.sinosoft.ops.cimp.service.word.ResumeService;
import com.sinosoft.ops.cimp.util.CryptoUtil;
import com.sinosoft.ops.cimp.util.MultiZipUtil;
import com.sinosoft.ops.cimp.util.word.pattern.AbstractExportWordXinJiang;
import net.coobird.thumbnailator.Thumbnails;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 
 * @ClassName: BaseQueryServiceImpl
 * @Description: 用于wordsql查询
 * @Author: zhangxp
 * @Date: 2017年11月13日 下午12:35:10
 * @Version 1.0.0
 */
@Service("exportWordService")
public class ExportServiceImpl extends BaseServiceImpl implements ExportService {
	private static final Logger logger = LoggerFactory.getLogger(ExportServiceImpl.class);

	private static final byte[] pwdBytes = "0123456789ABCDEF".getBytes();
	@Autowired
	@Qualifier("exportWordDao")
	private ExportDao exportWordDao = null;
	@Autowired
	@Qualifier("mongoDbDao")
	private MongoDbDao mongoDbDao = null;

	@Autowired
	private MongoDbService mongoDbService=null;
	@Autowired
	private ResumeService resumeService=null;
    @Autowired
    private SysCodeItemService sysCodeItemService;
    @Autowired
    private ThreadPoolTaskExecutor mainTaskExecutor;	

	@Override
	@Transactional(readOnly = true)
	public List<Map<String, Object>> findBySQL(String sql) {
		try {
			return exportWordDao.findBySQL(sql);
		} catch (Exception e) {
			System.out.println("执行查询sql错误：" + e);
			System.out.println("错误sql：" + sql);
			return null;
		}
	}

//	@Override
//	@Transactional
//	public void downLoadPhotoFile(String id, File file)
//			throws DownloadResourceFromMongoDbError, CannotFindMongoDbResourceById {
//		FileOutputStream fos = null;
//		try {
//			fos = new FileOutputStream(file);
//			downloadFileToStreamDecryptWithAES(id, fos);
//		} catch (IOException e) {
//			logger.error("创建文件输出流失败", e);
//			throw new DownloadResourceFromMongoDbError(
//					String.format("create file output stream error. file path:%s", file.getPath()));
//		}
//	}

	//陈处要求照片按处理过的那种方式 所以此处根据之前继红说的按照.85方式处理
	@Override
	@Transactional
	public void downLoadPhotoFile(String id, File file)
			throws DownloadResourceFromMongoDbError, CannotFindMongoDbResourceById {

		try(FileOutputStream fos=new FileOutputStream(file)){
			mongoDbService.downloadFileToStreamDecryptWithAES(id);
			Thumbnails.of(file.getAbsolutePath())
					.scale(1f)
					.outputQuality(0.85d)
					.outputFormat("jpg")
					.toFile(file);
		}catch(Exception e){
		    logger.error("下载照片失败！",e);
		}
	}

	@Override
	@Transactional
	public void downloadFileToStreamDecryptWithAES(String id, OutputStream os)
			throws DownloadResourceFromMongoDbError, CannotFindMongoDbResourceById {

		ByteArrayOutputStream baos = null;

		try {

			baos = new ByteArrayOutputStream();
			mongoDbDao.downloadFileToStream(id, baos);
			byte[] unDecryptBytes = baos.toByteArray();
			byte[] decryptedBytes = CryptoUtil.decryptAes(unDecryptBytes, pwdBytes);
			IOUtils.write(decryptedBytes, os);
			// TODO MD5 验证

		} catch (IOException | InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException
				| IllegalBlockSizeException | BadPaddingException e) {
			logger.error("下载文件解密失败", e);
			throw new DownloadResourceFromMongoDbError();
		} finally {
			IOUtils.closeQuietly(os);
			IOUtils.closeQuietly(baos);
		}
	}

	@Override
	@Transactional
	public boolean saveResumeByEmpId(String empId,String resume) {
		return exportWordDao.saveResumeByEmpId(empId, resume);
	}

	@Override
	@Transactional(readOnly = true)
	public List<String> getAllEmpIds() {
		return exportWordDao.getAllEmps();
	}

	
	@Override
	@Transactional(readOnly = true)
	public void generatorAndExportLiangWeiPDF(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//模板位置
		String templateFilePath = request.getSession().getServletContext()
                .getRealPath("resources/template/word/bingtuan/lianduiliangwei_template_bt.docx");
		//文件导出目录
		String outputFileRealPath = request.getSession().getServletContext()
                .getRealPath("resources/download/word/gbrmb/");

		//文件导出目录
		String outputPhotoPath = request.getSession().getServletContext()
				.getRealPath("resources/download/photo/");
		File file = new File(outputPhotoPath);
		if (file.exists()) {
			if (!file.isDirectory()) {
				file.mkdir();
			}
		} else {
			file.mkdir();
		}


		//取得两委对应数据
		List<String> empIds = getLWEmpIds();
		if(CollectionUtils.isEmpty(empIds)) return;
		
		//生成对应Word
		for (String empId : empIds) {
			try
			{
				AbstractExportWordXinJiang exportWord = new AbstractExportWordXinJiang("liangwei");
				String photoPath = outputPhotoPath + "\\" + empId + ".jpg";
				exportWord.setPhotoFilePath(photoPath);
				Map<String, Object> allAttrValue = exportWord.getAllAttrValue(empId, this);
				String outputFilePath = outputFileRealPath + "/LW" + empId + ".pdf";
				if(!new File(outputFilePath).exists())
				{
					exportWord.processAttrValueAndToPDF(templateFilePath, allAttrValue, outputFilePath);
				}
			}
			catch (Exception e)
			{
				logger.error("两委生成失败" + empId);
				logger.error(e.toString());
				continue;
			}
		}
		logger.error("两委生成完毕");
//
//		File file = new File(outputPdfFileRealPath);
//		if(!file.exists()){
//			file.mkdirs();
//		}
//
//		//Word 转 PDF 并加密
//		if(!CollectionUtils.isEmpty(outputFileLit)){
//			int i = outputFileLit.size()/100==0?10:outputFileLit.size();
//			for(int j=0;j<i;j++){
//				Thread thread = new Thread(){
//					@Override
//					public void run() {
//						boolean flag = true;
//						while(flag){
//							if(outputFileLit.size()>0){
//								String outputFileName = "";
//								synchronized (outputFileLit) {
//									if(outputFileLit.size()>0){
//										outputFileName = outputFileLit.get(outputFileLit.size()-1);
//										outputFileLit.remove(outputFileName);
//										//System.out.println("当前线程---"+Thread.currentThread().getName()+"==="+outputFileName);
//									}else{
//										flag = false;
//									}
//								}
//								if(StringUtils.isNotBlank(outputFileName)){
//									try {
//										WordUtil.doc2pdf(outputFileName, fileNames.get(outputFileName), true, "123456q",null);
//									} catch (Exception e) {
//										logger.error("两委生成失败" + outputFileName);
//										logger.error(e.toString());
//									}
//								}
//							}else{
//								flag = false;
//							}
//						}
//					}
//				};
//				thread.start();
//			}
//		}
//
//		ArrayList<String> pdfFileNames = new ArrayList<String>();
//		for (String fileName : fileNames.keySet()) {
//			try
//			{
//				pdfFileNames.add(fileNames.get(fileName));
//				WordUtil.doc2pdf(fileName, fileNames.get(fileName),true,"123456q","123456q");
//			}
//			catch (Exception e)
//			{
//				logger.error("两委生成失败" + fileName);
//				logger.error(e.toString());
//				continue;
//			}
//		}
	}

	private String[] compressedFile(ArrayList<String> pdfFileNames,String outputZipFilePath) throws ZipException {
		//判断是否为多个pdf 如果为多个 合并到一个压缩文件
		String[] strs = new String[2];
		String outputZipFileName = pdfFileNames.size() > 0 ? pdfFileNames.get(0) : "";
		
		String zipName = "";
		if(pdfFileNames.size()>1){
			 outputZipFileName = outputZipFilePath + "/" + "连队“两委”基本信息表_" + new Date().getTime();
			 //压缩文件
             MultiZipUtil.zip(outputZipFileName, pdfFileNames);
             zipName = outputZipFileName.substring(outputZipFileName.lastIndexOf("/") + 1) + ".zip";
             outputZipFileName = outputZipFileName.replaceAll("\\\\", new String("\\\\\\\\")).replaceAll("/",
                     new String("\\\\\\\\")) + ".zip";
		}else if(pdfFileNames.size()==1){
			zipName = pdfFileNames.get(0).substring(outputZipFileName.lastIndexOf("/") + 1);
			outputZipFileName = pdfFileNames.get(0).replaceAll("\\\\", new String("\\\\\\\\")).replaceAll("/",
                    new String("\\\\\\\\"));
		}
		strs[0] = outputZipFileName;
		strs[1] = zipName;
		return strs;
	}

	@Override
	@Transactional(readOnly = true)
	public List<String> getLWEmpIds() {
	    return exportWordDao.getLWEmpIds();
	}

    @Override
    @Transactional(readOnly = true)
    public String getStringEmps(String type) {
        return exportWordDao.getStringEmps(type);
    }

    @Override
    @Transactional
    public String updateResume() {
        return ResumeTask.startAll("公务员", this.getAllEmpIds(), resumeService, mainTaskExecutor);
    }

    @Override
    @Transactional
    public String updateLWResume() {
        return ResumeTask.startAll("“两委”", this.getLWEmpIds(), resumeService, mainTaskExecutor);
    }

    @Override
    @Transactional(readOnly = true)
    public SysCodeItem getCodeItemByCode(String codeSetName, String code) {
        return sysCodeItemService.getByCode(codeSetName, code);
    }
}

//简历任务
class ResumeTask implements Runnable{
    private static final Logger logger = LoggerFactory.getLogger(ResumeTask.class);
    private static ConcurrentHashMap<String,AtomicInteger> errorNumMap=new ConcurrentHashMap<String,AtomicInteger>();
    private static ConcurrentHashMap<String,AtomicInteger> successNumMap=new ConcurrentHashMap<String,AtomicInteger>();
    private static ConcurrentHashMap<String,AtomicInteger> toDoNumMap=new ConcurrentHashMap<String,AtomicInteger>();    
    protected ResumeService resumeService=null;//简历服务
    protected String empId=null;//人员标识
    protected String type="公务员";//类型
    
    public ResumeTask(String type,ResumeService resumeService,String empId) {
        this.type=type;
        this.resumeService=resumeService;
        this.empId=empId;
    }
    
    public static AtomicInteger getErrorNumByType(String type) {
        if(!errorNumMap.containsKey(type)) {
            errorNumMap.put(type, new AtomicInteger(0));
        }
        return errorNumMap.get(type);
    }
    public static AtomicInteger getSuccessNumByType(String type) {
        if(!successNumMap.containsKey(type)) {
            successNumMap.put(type, new AtomicInteger(0));
        }
        return successNumMap.get(type);
    }
    public static AtomicInteger getToDoNumByType(String type) {
        if(!toDoNumMap.containsKey(type)) {
            toDoNumMap.put(type, new AtomicInteger(0));
        }
        return toDoNumMap.get(type);
    }     

    @Override
    public void run() {
        try {
            resumeService.updateResume(empId, resumeService.generateResume(empId));
            getSuccessNumByType(this.type).incrementAndGet();
        } catch (Exception e) {
            getErrorNumByType(this.type).incrementAndGet();
            logger.error("生成"+type+"简历异常--empId="+empId,e);
        }finally {
            int finished = getSuccessNumByType(this.type).intValue()+getErrorNumByType(this.type).intValue();
            if( finished>=getToDoNumByType(this.type).intValue() ){
                logger.info("生成"+type+"简历完成--失败 "+getErrorNumByType(this.type).intValue()+" 人，共 "+getToDoNumByType(this.type).intValue()+" 人");
                getToDoNumByType(this.type).set(0);
            }else {
                if(finished%1000==0) {
                    logger.info("生成"+type+"简历--"+finished);
                }
            }
        }
    }
    
    public static String startAll(String type,Collection<String> empIds,ResumeService resumeService,ThreadPoolTaskExecutor taskExecutor) {
        if (getToDoNumByType(type).compareAndSet(0, -1)) {
            getToDoNumByType(type).set(empIds.size());
            getErrorNumByType(type).set(0);
            getSuccessNumByType(type).set(0);
            String info=new StringBuilder("开始生成")
                    .append(type)
                    .append("人员简历...，共 ")
                    .append(getToDoNumByType(type).get())
                    .append(" 人")
                    .toString();
            logger.info(info);
            for (String empId : empIds) {
                taskExecutor.execute(new ResumeTask(type,resumeService,empId));
            }
            return info;
        }
        return new StringBuilder("正在生成")
                .append(type)
                .append("公务员简历...，共 ")
                .append(getToDoNumByType(type).get())
                .append(" 人，成功 ")
                .append(getSuccessNumByType(type).intValue())
                .append(" 人，失败 ")
                .append(getErrorNumByType(type).intValue())
                .append(" 人")
                .toString();
    }
}
