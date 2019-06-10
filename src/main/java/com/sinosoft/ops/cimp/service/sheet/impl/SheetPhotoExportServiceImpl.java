package com.sinosoft.ops.cimp.service.sheet.impl;


import com.sinosoft.ops.cimp.common.service.BaseServiceImpl;
import com.sinosoft.ops.cimp.service.word.ExportService;
import com.sinosoft.ops.cimp.service.sheet.SheetPhotoExportService;
import com.sinosoft.ops.cimp.util.StringUtil;
import org.hibernate.type.descriptor.java.UUIDTypeDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @ClassName:  SheetPhotoExportServiceImpl
 * @description: 头像导出接口
 * @author:        kanglin
 * @date:            2018年6月9日 下午
 * @version        1.0.0
 * @since            JDK 1.7
 */
@Service("SheetPhotoExportService")
public class SheetPhotoExportServiceImpl extends BaseServiceImpl implements SheetPhotoExportService {
	
    private static final Logger logger = LoggerFactory.getLogger(SheetPhotoExportServiceImpl.class);
    
	@Autowired
	ExportService exportWordService;

	@Autowired
	private ExportService exportService;

	//导出头像
	@Transactional(readOnly=true)
	public int export(String outputFilePath) {
		File photePathFile = new File(outputFilePath);
		if (!photePathFile.exists()) {
			photePathFile.mkdirs();
		}
		int cnt = 0;
		List<String> empIds = exportService.getLWEmpIds();
		if(CollectionUtils.isEmpty(empIds)) return 0;

		//生成对应照片
		for (String empId : empIds) {
			try
			{
				if (exportPhotoFile(empId, outputFilePath)) {
					logger.info("成功导出头像EMPID=" + empId);
					cnt++;
				}
			} catch(Exception e) {
				continue;
			}
		}

		File[] files = photePathFile.listFiles();
		if (files != null && files.length > 0) {
			for (File aFile : files) {
				if (aFile.exists() && aFile.length() == 0) {
					aFile.delete();
				}
			}
		}
		return cnt;
	}
	
	private boolean exportPhotoFile(String empId, String photoNewPath) {
		String A092010 = "04";
		try {
			String photoId = getPhotoId(empId, A092010);
			if (StringUtil.isEmptyOrNull(photoId)) {
				photoId = getOtherPhotoId(empId);
			}
			if(StringUtil.isEmptyOrNull(photoId)){
				return false;
			}
			File photeFile = new File(photoNewPath + "/" + empId.toLowerCase() + ".jpg");
			if(!photeFile.exists())
			{
				exportWordService.downLoadPhotoFile(photoId.toLowerCase(), photeFile);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("下载头像失败！", e);
		}
		return false;
	}

	private String getPhotoId(String empId, String photoType) throws Exception {
		// 根据字段查询字段的info_item_id
		String infoItemIdSql = "SELECT ID FROM SYS_INFO_ITEM WHERE NAME = 'a092015'";
		List infoItemIdList = exportWordService.findBySQL(infoItemIdSql);
		String infoItemId = "";
		if (infoItemIdList != null && infoItemIdList.size() > 0) {
			Map infoItemIdMap = (Map) infoItemIdList.get(0);
			infoItemId = StringUtil.obj2Str(infoItemIdMap.get("ID"));
			String subjectIdSql = "SELECT  SUB_ID  FROM EMP_A092 WHERE EMP_ID = '" + empId + "' and  A092010 = '"
					+ photoType + "'  and status = 0 ";
			List subjectIdList = exportWordService.findBySQL(subjectIdSql);
			String subjectId = "";
			if (subjectIdList != null && subjectIdList.size() > 0) {
				Map subjectIdMap = (Map) subjectIdList.get(0);
				subjectId = StringUtil.obj2Str(subjectIdMap.get("SUB_ID"));
				String photoIdSql = "SELECT  ID FROM SYS_IMAGE WHERE INFO_ITEM_ID = '" + infoItemId
						+ "' AND SUBJECT_ID = '" + subjectId + "'  and status = 0 ";
				List photoIdList = exportWordService.findBySQL(photoIdSql);
				String photoId = "";
				if (photoIdList != null && photoIdList.size() > 0) {
					Map photoIdMap = (Map) photoIdList.get(0);
					Object photoIdObj = photoIdMap.get("ID");
					if (photoIdObj instanceof byte[]) {
						byte[] ba = (byte[]) photoIdObj;
						if (ba.length == 16) {// UUID，使用hibernate的UUID解析器处理
							try {
								photoId = UUIDTypeDescriptor.ToBytesTransformer.INSTANCE.parse(photoIdObj).toString()
										.toUpperCase();
								if (StringUtil.isNotEmptyOrNull(photoId)) {
									return photoId;
								}
							} catch (Exception e) {
								return "";
							}
						} else {

						}
					}
				}
			}
		}
		return "";
	}

	private String getOtherPhotoId(String empId) throws Exception {
		// 根据字段查询字段的info_item_id
		String infoItemIdSql = "SELECT ID FROM SYS_INFO_ITEM WHERE NAME = 'a092015'";
		List infoItemIdList = exportWordService.findBySQL(infoItemIdSql);
		String infoItemId = "";
		if (infoItemIdList != null && infoItemIdList.size() > 0) {
			Map infoItemIdMap = (Map) infoItemIdList.get(0);
			infoItemId = StringUtil.obj2Str(infoItemIdMap.get("ID"));
			String subjectIdSql = "SELECT  SUB_ID  FROM EMP_A092 WHERE EMP_ID = '" + empId
					+ "'  and A092010 in('01','02','03','04')  and status = 0 order by A092005 desc";
			List subjectIdList = exportWordService.findBySQL(subjectIdSql);
			String subjectId = "";
			if (subjectIdList != null && subjectIdList.size() > 0) {
				for (Object temp : subjectIdList) {
					Map subjectIdMap = (Map) temp;
					subjectId = StringUtil.obj2Str(subjectIdMap.get("SUB_ID"));
					String photoIdSql = "SELECT  ID FROM SYS_IMAGE WHERE INFO_ITEM_ID = '" + infoItemId
							+ "' AND SUBJECT_ID = '" + subjectId + "'  and status = 0 ";
					List photoIdList = exportWordService.findBySQL(photoIdSql);
					String photoId = "";
					if (photoIdList != null && photoIdList.size() > 0) {
						Map photoIdMap = (Map) photoIdList.get(0);
						Object photoIdObj = photoIdMap.get("ID");
						if (photoIdObj instanceof byte[]) {
							byte[] ba = (byte[]) photoIdObj;
							if (ba.length == 16) {// UUID，使用hibernate的UUID解析器处理
								try {
									photoId = UUIDTypeDescriptor.ToBytesTransformer.INSTANCE.parse(photoIdObj)
											.toString().toUpperCase();
									if (StringUtil.isNotEmptyOrNull(photoId)) {
										return photoId;
									}
								} catch (Exception e) {
									return "";
								}
							} else {
							}
						}
					}
				}
			}
		}
		return "";
	}
}
