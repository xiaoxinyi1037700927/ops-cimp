package com.sinosoft.ops.cimp.util.word.pattern.xinjiang;


import com.sinosoft.ops.cimp.service.word.ExportService;
import com.sinosoft.ops.cimp.util.StringUtil;
import org.hibernate.type.descriptor.java.UUIDTypeDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * Created by SML 照片 date : 2017/11/8 des :
 */
public class PhotoAttrValue implements AttrValue {
	// 属性与属性之间的排序，越小越靠前
	private final int order = 0;

	private final String key = "photo";

	private static final Logger logger = LoggerFactory.getLogger(PhotoAttrValue.class);

	@Override
	public Object getAttrValue(Map<String, Object> attrValueContext, String empId, ExportService exportWordService)
			throws AttrValueException {
		String wordType = (String) attrValueContext.get("wordType");
		String A092010 = "";
		// 根据不同类型的word选择不同类型的照片
		switch (wordType) {
		case "xxcjb":
			A092010 = "02";
			break;
		case "gbrmb":
			A092010 = "04";
			break;
		}
		byte[] buffer = null;
		String photoId = getPhotoId(empId, A092010, exportWordService);
		if (StringUtil.isEmptyOrNull(photoId)) {
			photoId = getOtherPhotoId(empId, exportWordService);
		}
		if(StringUtil.isEmptyOrNull(photoId)){
			logger.error("无照片:" + empId);
			return buffer;
		}
		String photoFilePath = StringUtil.obj2Str(attrValueContext.get("photoFilePath"));
		File photeFile = new File(photoFilePath);
		try {
			if(!photeFile.exists())
			{
				exportWordService.downLoadPhotoFile(photoId.toLowerCase(), photeFile);
			}
			try(FileInputStream fis = new FileInputStream(photeFile);
    			ByteArrayOutputStream bos = new ByteArrayOutputStream(1000)){
    
    			byte[] b = new byte[1000];
    			int n;
    			// 每次从fis读1000个长度到b中，fis中读完就会返回-1
    			while ((n = fis.read(b)) != -1) {
    				bos.write(b, 0, n);
    			}
    			buffer = bos.toByteArray();
			}
		} catch (FileNotFoundException e) {
			logger.error("照片生成失败" + empId,e);
		} catch (IOException e) {
			logger.error("照片生成失败" + empId,e);
		} catch (Exception e) {
			logger.error("照片生成失败:" + empId,e);
		}
		return buffer;
	}

	public String getPhotoId(String empId, String photoType, ExportService exportWordService) {
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

	public String getOtherPhotoId(String empId, ExportService exportWordService) {
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

	public String getKey() {
		return key;
	}

	@Override
	public int getOrder() {
		return order;
	}

    @Override
    public String getTitle() {
        return "照片";
    }
}
