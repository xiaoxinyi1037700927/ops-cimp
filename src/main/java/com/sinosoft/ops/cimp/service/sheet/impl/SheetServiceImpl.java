package com.sinosoft.ops.cimp.service.sheet.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sinosoft.ops.cimp.common.model.Config;
import com.sinosoft.ops.cimp.common.service.BaseEntityServiceImpl;
import com.sinosoft.ops.cimp.constant.OpsErrorMessage;
import com.sinosoft.ops.cimp.entity.sheet.*;
import com.sinosoft.ops.cimp.exception.BusinessException;
import com.sinosoft.ops.cimp.repository.sheet.SheetCategoryDao;
import com.sinosoft.ops.cimp.repository.sheet.SheetDao;
import com.sinosoft.ops.cimp.repository.sheet.SheetDesignDao;
import com.sinosoft.ops.cimp.repository.sheet.SheetSheetCategoryDao;
import com.sinosoft.ops.cimp.service.sheet.*;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.*;

/**
 * 表格服务
 * 
 * @author Nil
 * @version 1.0
 * @created 21-6月-2017 13:17:43
 */
@Service("sheetService")
public class SheetServiceImpl extends BaseEntityServiceImpl<Sheet> implements SheetService {
	
    private static final Logger logger = LoggerFactory.getLogger(SheetServiceImpl.class);
    
    @Autowired
    private SheetDao sheetDao = null;
    @Autowired
    private SheetCategoryDao sheetCategoryDao = null;
    @Autowired
    private SheetSheetCategoryDao sheetSheetCategoryDao = null;
    @Autowired
    private SheetDesignDao sheetDesignDao = null;
    @Autowired
    private SheetSheetCategoryService sheetSheetCategoryService = null;
    @Autowired
	private SheetDataService sheetDataService = null;
    @Autowired
    private SheetCategoryService sheetCategoryService = null;
    @Autowired
    private SheetGatherInfoService gatherInfoService;
    @Autowired
	SheetParameterService sheetParameterService;
	@Autowired
	SheetDesignParameterService sheetDesignParameterService;
	@Autowired
	private SheetDesignSectionService sheetDesignSectionService;
	@Autowired
	private SheetTagService sheetTagService;

    public SheetServiceImpl() {
    }
    
	/**
     * 根据表格分类获取表格设计
     * 
     * @param categoryId    表格分类标识
     */
	@Override
	@Transactional(readOnly = true)
    public Collection<Sheet> getByCategoryId(UUID categoryId) {
	    return sheetDao.getByCategoryId(categoryId);
    }

	@Override
	@Transactional(readOnly = true)
	public Object getOnlyByCategoryId(UUID categoryId) {
		return sheetDao.getOnlyByCategoryId(categoryId);
	}
	
	@Override
	@Transactional(readOnly = true)
	public Collection<UUID> getDownCatigories(UUID topCatigoryId) {
		Collection<UUID> coll = new HashSet<UUID>();
		coll.add(topCatigoryId);
		coll = getChildCatigories(coll, topCatigoryId);
		return coll;
	}
	
	//得到表子目录ID集合
	private Collection<UUID> getChildCatigories(Collection<UUID> coll, UUID upCatigoryId) {
		Collection<SheetCategory> catColl = sheetCategoryDao.getChildren(upCatigoryId);
		if (catColl != null && catColl.size() > 0) {
			for (SheetCategory aCat : catColl) {
				UUID aId = aCat.getId();
				coll.add(aId);
				coll = getChildCatigories(coll, aId);
			}
		}
		return coll;
	}
	
	@Override
	@Transactional
	public UUID create(Sheet entity, UUID categoryId) {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		entity.setLastModifiedBy(entity.getCreatedBy());
		entity.setCreatedTime(now);
		entity.setLastModifiedTime(now);
		entity.setOrdinal(sheetDao.getNextOrdinal());
		entity.setFlg(0);
		entity.setStatus((byte)0);
		sheetDao.save(entity);

		//增加关系表数据
		SheetSheetCategory sheetSheetCategory = new SheetSheetCategory();
		sheetSheetCategory.setId(UUID.randomUUID());
		sheetSheetCategory.setCategoryId(categoryId);
		sheetSheetCategory.setSheetId(entity.getId());
		sheetSheetCategory.setStatus((byte)0);
		sheetSheetCategory.setCreatedBy(entity.getCreatedBy());
		sheetSheetCategory.setLastModifiedBy(entity.getCreatedBy());
		sheetSheetCategory.setCreatedTime(now);
		sheetSheetCategory.setLastModifiedTime(now);
		sheetSheetCategoryDao.save(sheetSheetCategory);
		return entity.getId();
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public boolean createBatch(Sheet entity, UUID categoryId,UUID designCategoryId, String[] designIdNos,String params,String type,String parameterId) {
    	if (designIdNos != null && designIdNos.length > 0) {

    		String parameterValue="";
    		String description="";
			String valueId="";

			JSONArray jsonArray = JSON.parseArray(params);
			for(int i=0;i<jsonArray.size();i++)
			{
				JSONObject js = (JSONObject) jsonArray.get(i);
				if(js.get("parameterId").equals(parameterId))
				{
					parameterValue=js.get("parameterValue").toString();
					description=js.get("description").toString();
					valueId=js.get("valueId").toString();
					break;
				}
			}

			String [] parameterValues =parameterValue.split(",");
			String [] descriptions =description.split(",");
			String [] valueIds =valueId.split(",");

			for(int ipara=0;ipara<parameterValues.length;ipara++)
			{
				//增加分类项
				SheetCategory category = new SheetCategory();
				category.setName(descriptions[ipara]);
				category.setType(Byte.parseByte(type));
				category.setParentId(categoryId);
				category.setDesignCategoryId(designCategoryId);
				category.setReportType(Integer.parseInt(parameterId));
				category.setReportOrg(valueIds[ipara]);
				category.setCreatedBy(entity.getCreatedBy());
				category.setLastModifiedBy(entity.getCreatedBy());
				category.setOrdinal(sheetCategoryService.getNextOrdinal());
				UUID newCategoryId = sheetCategoryService.create(category);

				//批量处理,在最后加
				for (int i = 1; i <= designIdNos.length; i++) {
					//处理报表顺序
					for (String aIdItem : designIdNos) {
						String[] idItems =  aIdItem.split("\\*");
						String aId = idItems[0];
						String aIndex = idItems[1];
						if (i == Integer.parseInt(aIndex)) {
							UUID designId = UUID.fromString(aId);
							SheetDesign design = sheetDesignDao.getById(designId);
							Sheet newItem = new Sheet();
							BeanUtils.copyProperties(entity, newItem);
							newItem.setId(UUID.randomUUID());
							newItem.setDesignId(designId);
							if (design != null) {
								newItem.setName(design.getName());
							}
							UUID sheetId = this.create(newItem, newCategoryId);

							Collection<SheetDesignParameter> sheetDesignParameters = sheetDesignParameterService.getByDesignId(designId);
							List<String> lst =  new ArrayList<>();
							sheetDesignParameters.forEach(item -> lst.add(item.getParameterId()));

							Iterator<Object> it =  jsonArray.iterator();
							while(it.hasNext()){
								String item = it.next().toString();
								JSONObject js = (JSONObject) JSON.parse(item);
								if(lst.contains(js.getString("parameterId")))
								{

									SheetParameter sheetParameter = new SheetParameter();
									sheetParameter.setId(UUID.randomUUID());
									sheetParameter.setParameterId(js.get("parameterId").toString());
									if(js.get("parameterId").equals(parameterId))
									{
										sheetParameter.setParameterValue(parameterValues[ipara]);
										sheetParameter.setDescription(descriptions[ipara]);
									}
									else
									{
										sheetParameter.setParameterValue(js.get("parameterValue").toString());
										sheetParameter.setDescription(js.get("description").toString());
									}

									sheetParameter.setSheetId(sheetId);
									sheetParameter.setOrdinal(sheetParameterService.getNextOrdinal());
									sheetParameter.setStatus((byte)0);
									sheetParameter.setCreatedTime(new Timestamp(System.currentTimeMillis()));
									sheetParameter.setLastModifiedTime(new Timestamp(System.currentTimeMillis()));
									sheetParameterService.create(sheetParameter);
								}
							}
						}
					}
				}
			}
    	}
    	return true;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public boolean createBatchInCategory(Sheet entity, UUID categoryId,UUID designCategoryId, String[] designIdNos,String params,String type,String parameterId) {
		if (designIdNos != null && designIdNos.length > 0) {

			String parameterValue="";
			String description="";
			String valueId="";

			JSONArray jsonArray = JSON.parseArray(params);
			for(int i=0;i<jsonArray.size();i++)
			{
				JSONObject js = (JSONObject) jsonArray.get(i);
				if(js.get("parameterId").equals(parameterId))
				{
					parameterValue=js.get("parameterValue").toString();
					description=js.get("description").toString();
					valueId=js.get("valueId").toString();
					break;
				}
			}

			String [] parameterValues =parameterValue.split(",");
			String [] descriptions =description.split(",");
			String [] valueIds =valueId.split(",");

			for(int ipara=0;ipara<parameterValues.length;ipara++)
			{
				//批量处理,在最后加
				for (int i = 1; i <= designIdNos.length; i++) {
					//处理报表顺序
					for (String aIdItem : designIdNos) {
						String[] idItems =  aIdItem.split("\\*");
						String aId = idItems[0];
						String aIndex = idItems[1];
						if (i == Integer.parseInt(aIndex)) {
							UUID designId = UUID.fromString(aId);
							SheetDesign design = sheetDesignDao.getById(designId);
							Sheet newItem = new Sheet();
							BeanUtils.copyProperties(entity, newItem);
							newItem.setId(UUID.randomUUID());
							newItem.setDesignId(designId);
							if (design != null) {
								newItem.setName(design.getName());
							}
							UUID sheetId = this.create(newItem, categoryId);

							Collection<SheetDesignParameter> sheetDesignParameters = sheetDesignParameterService.getByDesignId(designId);
							List<String> lst =  new ArrayList<>();
							sheetDesignParameters.forEach(item -> lst.add(item.getParameterId()));

							Iterator<Object> it =  jsonArray.iterator();
							while(it.hasNext()){
								String item = it.next().toString();
								JSONObject js = (JSONObject) JSON.parse(item);
								if(lst.contains(js.getString("parameterId")))
								{

									SheetParameter sheetParameter = new SheetParameter();
									sheetParameter.setId(UUID.randomUUID());
									sheetParameter.setParameterId(js.get("parameterId").toString());
									if(js.get("parameterId").equals(parameterId))
									{
										sheetParameter.setParameterValue(parameterValues[ipara]);
										sheetParameter.setDescription(descriptions[ipara]);
									}
									else
									{
										sheetParameter.setParameterValue(js.get("parameterValue").toString());
										sheetParameter.setDescription(js.get("description").toString());
									}

									sheetParameter.setSheetId(sheetId);
									sheetParameter.setOrdinal(sheetParameterService.getNextOrdinal());
									sheetParameter.setStatus((byte)0);
									sheetParameter.setCreatedTime(new Timestamp(System.currentTimeMillis()));
									sheetParameter.setLastModifiedTime(new Timestamp(System.currentTimeMillis()));
									sheetParameterService.create(sheetParameter);
								}
							}
						}
					}
				}
			}
		}
		return true;
	}
	
	@Override
	@Transactional
	public int updateOne(Sheet entity) {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		UUID oldId = entity.getId();
		Sheet oldItem = this.getById(oldId);
		Timestamp createTime = oldItem.getCreatedTime();
		UUID createBy = oldItem.getCreatedBy();
		int ordinal = oldItem.getOrdinal();
		BeanUtils.copyProperties(entity, oldItem);
		oldItem.setLastModifiedTime(now);
		oldItem.setCreatedTime(createTime);
		oldItem.setCreatedBy(createBy);
		oldItem.setOrdinal(ordinal);
		this.update(oldItem);
		return 1;
	}
	
	@Override
	@Transactional
	public int delete(Sheet entity, UUID categoryId) {
		UUID id = entity.getId();
		Collection<SheetData> list = sheetDataService.getBySheetId(id);
		if (list != null && list.size() > 0) {
			//有sheetDate记录，不能删除
			logger.info("Sheet的id=" + id.toString() + "，有data子记录，不能删除！");
			return -1;
		}
		Sheet orgItem = this.getById(id);
		int orgOrdinal = orgItem.getOrdinal();
		//后面的节点ordinal减1
		sheetDao.minusOrdinals(orgOrdinal, categoryId, entity.getLastModifiedBy());
		//节点删除
		sheetDao.deleteById(id);
		//删除关系表数据
		sheetSheetCategoryService.deleteBySheetIdAndCategoryId(id, categoryId);
		return 1;
	}
	
	@Override
	@Transactional
	public boolean up(Sheet entity, UUID categoryId) {
		UUID id = entity.getId();
		Sheet curr = sheetDao.getById(entity.getId());
		int ordinal = curr.getOrdinal();
		UUID userName = entity.getLastModifiedBy();
		Sheet previous = sheetDao.findPrevious(id, categoryId);
		if (previous != null) {
			UUID preId = previous.getId();
			int preOrdinal = previous.getOrdinal();
			int cnt = sheetDao.updateOrdinal(preId, ordinal, userName);
			if (cnt > 0) {
				cnt = sheetDao.updateOrdinal(id, preOrdinal, userName);
				if (cnt > 0) {
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	@Transactional
	public boolean down(Sheet entity, UUID categoryId) {
		UUID id = entity.getId();
		Sheet curr = sheetDao.getById(entity.getId());
		int ordinal = curr.getOrdinal();
		UUID userName = entity.getLastModifiedBy();
		Sheet nextvious = sheetDao.findNext(id, categoryId);
		if (nextvious != null) {
			UUID nextId = nextvious.getId();
			int nextOrdinal = nextvious.getOrdinal();
			int cnt = sheetDao.updateOrdinal(nextId, ordinal, userName);
			if (cnt > 0) {
				cnt = sheetDao.updateOrdinal(id, nextOrdinal, userName);
				if (cnt > 0) {
					return true;
				}
			}
		}
		return false;
	}
    
    @Override
    @Transactional(readOnly = true)
    public Collection<Sheet> getByDesignId(UUID designId, Map<String, Object> params) {
        return sheetDao.getByDesignId(designId, params);
    }
    @Override
    @Transactional(readOnly = true)
    public Collection<Sheet> getByReportNo(String sheetNo, int version, Map<String, Object> params) {
        return sheetDao.getByReportNo(sheetNo, version, params);
    }
    @Override
    @Transactional(readOnly = true)
    public Collection<Sheet> getByReportNo(String sheetNo, Map<String, Object> params) {
        return sheetDao.getByReportNo(sheetNo, SheetDesign.INITIAL_VERSION, params);
    }
    
    @Override
    @Transactional
    public Sheet calculate(UUID designId, Map<String, Object> params, UUID userId) {
        return null;
    }
    @Override
    @Transactional
    public Sheet calculate(String sheetNo, int version, Map<String, Object> params, UUID userId) {
        System.out.println("---------fileupload.maxUploadSize="+Config.getConfiguration().getString("fileupload.maxUploadSize"));
        return null;
    }
    @Override
    @Transactional
    public Sheet calculate(String sheetNo, Map<String, Object> params, UUID userId) {
        return calculate(sheetNo,SheetDesign.INITIAL_VERSION,params,userId);
    }
    
    @Override
    @Transactional
    public Sheet sum(String sheetNo, int version, Map<String, Object> params, UUID userId) {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    @Transactional
    public Sheet sum(String sheetNo, Map<String, Object> params, UUID userId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Transactional
    public boolean archive(UUID id, UUID userId) throws BusinessException {
        Sheet sheet = sheetDao.getById(id);
        if (sheet == null) {
            throw new BusinessException(OpsErrorMessage.MODULE_NAME,OpsErrorMessage.ERROR_MESSAGE,"标识为“"+id+"”的表格不存在！");
        }
        sheet.setArchived(true);
        sheet.setLastModifiedBy(userId);
        sheet.setLastModifiedTime(new Timestamp(System.currentTimeMillis()));
        sheetDao.update(sheet);
        return true;
    }
    @Override
    @Transactional
    public boolean unarchive(UUID id, UUID userId) throws BusinessException {
        Sheet sheet = sheetDao.getById(id);
        if (sheet == null) {
            throw new BusinessException(OpsErrorMessage.MODULE_NAME,OpsErrorMessage.ERROR_MESSAGE,"标识为“"+id+"”的表格不存在！");
        }
        sheet.setArchived(false);
        sheet.setLastModifiedBy(userId);
        sheet.setLastModifiedTime(new Timestamp(System.currentTimeMillis()));
        sheetDao.update(sheet);
        return true;
    }

    @Override
    public boolean check(UUID id, UUID userId) {
        // TODO Auto-generated method stub
        return false;
    }
    @Override
    public boolean check(String sheetNo, int version, Map<String, Object> params, UUID userId) {
        // TODO Auto-generated method stub
        return false;
    }
    @Override
    public boolean check(String sheetNo, Map<String, Object> params, UUID userId) {
        // TODO Auto-generated method stub
        return false;
    }
    
    @Override
    @Transactional
    public boolean exportToFile(UUID id, String filename) throws FileNotFoundException, IOException, BusinessException {
        Sheet sheet = sheetDao.getById(id);
        if (sheet == null) {
            throw new BusinessException(OpsErrorMessage.MODULE_NAME,OpsErrorMessage.ERROR_MESSAGE,"标识为“"+id+"”的表格不存在！");
        }
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))){
            oos.writeObject(sheet);
        }
        return true;
    }
    @Override
    @Transactional
    public Sheet importFromFile(String filename, UUID userId) throws ClassNotFoundException, IOException {
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))){
            Sheet entity = (Sheet) ois.readObject();
            entity.setLastModifiedBy(userId);
            entity.setLastModifiedTime(new Timestamp(System.currentTimeMillis()));
            sheetDao.saveOrUpdate(entity);
            return entity;
        }
    }
    
    @Override
    public Map<Byte, String> getSheetTypeMap() {
        return SheetType.toMap();
    }

    @Override
    public Collection<Map<String, String>> getSheetType() {
        Collection<Map<String, String>> collection = new LinkedList<Map<String, String>>();
        for (Map.Entry<Byte, String> entry : getSheetTypeMap().entrySet()) {
            HashMap<String,String> map = new HashMap<String,String>();
            map.put("id", String.valueOf(entry.getKey()));
            map.put("text", entry.getValue());
            collection.add(map);
        }
        return collection;
    }

	@Override
	@Transactional(readOnly=true)
	public Collection<SheetData> gatherSheet(Map<String,Map<String,SheetData>> dataMap, String id, 
			 String[] sheetIds) {
		List<SheetData> sheetDatas = new ArrayList<>();
		Sheet sheet = getById(UUID.fromString(id));
		Collection<SheetDesignSection> sections = sheetDesignSectionService.getByDesignId(sheet.getDesignId());
		for (SheetDesignSection sheetDesignSection : sections) {
			
			if(sheetDesignSection!=null && BooleanUtils.isTrue(sheetDesignSection.getSummaryable())){
				
				int betweenRow = sheetDesignSection.getCtrlRowStart()-sheetDesignSection.getStartRowNo()-1;
				int betweenColumn = sheetDesignSection.getCtrlColumnStart()-sheetDesignSection.getStartColumnNo()-1;
				
				for(int i = sheetDesignSection.getStartRowNo();i <= sheetDesignSection.getEndRowNo();i++){
					
					for(int j = sheetDesignSection.getStartColumnNo();j <= sheetDesignSection.getEndColumnNo();j++){
						SheetData sheetData = new SheetData();
						sheetData.setSheetId(UUID.fromString(id));
						sheetData.setBeingEdited(false);
						sheetData.setRowNo(i);
						sheetData.setColumnNo(j);
						sheetData.setCtrlRowNo(i+betweenRow);
						sheetData.setCtrlColumnNo(j+betweenColumn);
						try {
							
							BigDecimal bigDecimal = new BigDecimal("0");
							for (String sheetId : sheetIds) {
								SheetData data = dataMap.get(sheetId).get(sheetData.getRowNo()+"-"+sheetData.getColumnNo());
								if(data != null){
									Double.parseDouble(StringUtils.isNotBlank(data.getStringValue())?data.getStringValue():"0");
									BigDecimal decimal = NumberUtils.createBigDecimal(StringUtils.isBlank(data.getStringValue())?"0":data.getStringValue());
									bigDecimal = bigDecimal.add(decimal);
								}
							}
							sheetData.setStringValue(bigDecimal.stripTrailingZeros().toPlainString());
						}
						catch (Exception e)
						{
							
						}
						sheetDatas.add(sheetData);
					}
				}
			}
		}

		return sheetDatas;
	}
	
	@Transactional
	public List<Map> getSummaryData(Map queryParameter)
	{
		List<Map> tempList = sheetDao.getSummaryData(queryParameter);
		for(Map temp : tempList)
		{
			String ids=temp.get("IDS").toString();
			logger.info("汇总符合条件的报表ids="+ids);
			for(String id : ids.split(","))
			{
				ids=ids.replace(id,getUUIDString(id));
			}
			temp.put("IDS",ids);
		}
		return tempList;
	}

	private String getUUIDString(String strUUID)
	{
		//ac9325ef-3026-4ccf-a800-83b77e627337
//		return strUUID.substring(0,8).toLowerCase()+ "-" + strUUID.substring(8,12).toLowerCase()+ "-" + strUUID.substring(12,16).toLowerCase()+ "-" + strUUID.substring(16,20).toLowerCase()+ "-" + strUUID.substring(20,32).toLowerCase();
		if(strUUID.length()<32) return "";
		String[] components = {strUUID.substring(0,8).toLowerCase(),strUUID.substring(8,12).toLowerCase(),strUUID.substring(12,16).toLowerCase(),strUUID.substring(16,20).toLowerCase(),strUUID.substring(20,32).toLowerCase()};

		for (int i=0; i<5; i++)
			components[i] = "0x"+components[i];

		long mostSigBits = Long.decode(components[0]).longValue();
		mostSigBits <<= 16;
		mostSigBits |= Long.decode(components[1]).longValue();
		mostSigBits <<= 16;
		mostSigBits |= Long.decode(components[2]).longValue();

		long leastSigBits = Long.decode(components[3]).longValue();
		leastSigBits <<= 48;
		leastSigBits |= Long.decode(components[4]).longValue();

		return new UUID(mostSigBits, leastSigBits).toString();
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly=true)
	public List<Map<String, Object>> getGatherInfos(UUID id, Integer rowNo, Integer columnNo) {
		Collection<SheetGatherParameter> gatherParameterValues = sheetDao.getGatherParameterValue(id);
		List<GatherInfo> gatherInfos = sheetDao.getGatherInfos(id,rowNo,columnNo);
		List<Map<String, Object>> gatherInfoMaps = new ArrayList<>();
		for (GatherInfo gatherInfo : gatherInfos) {
			
			Map<String,Object> info = (Map<String, Object>) JSON.toJSON(gatherInfo);
			List<SheetGatherParameter> deleteList = new ArrayList<>();
			for (SheetGatherParameter  gatherParameter : gatherParameterValues) {
				if(StringUtils.equals(gatherInfo.getGatherSheetId().toString(), 
						gatherParameter.getGatherSheetId().toString())){
					info.put(gatherParameter.getName(), gatherParameter.getDescription());
					deleteList.add(gatherParameter);
				}
			}
			if(deleteList.size()>0){
				gatherParameterValues.removeAll(deleteList);
			}
			gatherInfoMaps.add(info);
		}
		return gatherInfoMaps;
		
	}

	@Override
	public void exportGatherData(HttpServletResponse response,Collection<Map<String, Object>> parameterInfos, List<Map<String, Object>> gatherInfos) throws Exception {
		response.setHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode("汇总反查数据.xls", "UTF-8"));  
		response.setHeader("Connection", "close");  
		response.setHeader("Content-Type", "application/x-download"); 
		Workbook workbook = new HSSFWorkbook();
		org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet();
		Row row = sheet.createRow(0);
		CellStyle style = workbook.createCellStyle();
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
		
		Cell cell = row.createCell(0);
		cell.setCellValue("报表分类(套表)");
		cell.setCellStyle(style);
		Cell cell1 = row.createCell(1);
		cell1.setCellValue("表名");
		cell1.setCellStyle(style);
		int i = 2;
		for (Map<String, Object> parameterInfo : parameterInfos) {
			Cell createCell = row.createCell(i);
			createCell.setCellValue((String)parameterInfo.get("NAMECN"));
			createCell.setCellStyle(style);
			i++;
		}
//		Cell cell2 = row.createCell(2);
//		cell2.setCellValue("填报机构");
//		cell2.setCellStyle(style);
//		Cell cell3 = row.createCell(3);
//		cell3.setCellValue("报告期");
//		cell3.setCellStyle(style);
		Cell cell4 = row.createCell(i);
		cell4.setCellValue("数据");
		cell4.setCellStyle(style);
		
		
		int j = 1;
		for (Map<String, Object> data : gatherInfos) {
			Row row2 = sheet.createRow(j);
			Cell cell00 = row2.createCell(0);
			cell00.setCellValue(data.get("categoryName")==null?"":data.get("categoryName").toString());
			Cell cell33 = row2.createCell(1);
			cell33.setCellValue(data.get("sheetName")==null?"":data.get("sheetName").toString());
			int k = 2;
			for (Map<String, Object> parameterInfo : parameterInfos) {
				Cell createCell = row2.createCell(k);
				createCell.setCellValue((String)data.get(parameterInfo.get("NAME")));
				k++;
			}
			Cell cell44 = row2.createCell(k);
			cell44.setCellValue(data.get("stringValue")==null?"":data.get("stringValue").toString());
			j++;
		}
		workbook.write(response.getOutputStream());
		
	}

	/**
	 * 
	 * 保存汇总信息和报表数据，放到一个事务中
	 * @param sheetDatas
	 * @param sheetGatherInfos
	 * @param bAutoOrWord 是否自动统计或word统计
	 * @author sunch
	 * @date:    2018年7月19日 下午1:52:18
	 * @since JDK 1.7
	 */
	@Override
	@Transactional
	public void saveInfosAndDatas(UUID id,Collection<SheetData> sheetDatas, Collection<SheetGatherInfo> sheetGatherInfos,Collection<SheetTag> sheetTags,boolean bAutoOrWord) {
		//删除之前数据
		//TODO 这种处理方式不好，后续修改
		
		try {
			sheetDataService.deleteBySheetId(id);
			if(!CollectionUtils.isEmpty(sheetDatas)){
				sheetDataService.createAll(sheetDatas);
			}
			
			gatherInfoService.deleteBySheetId(id);
			if(!CollectionUtils.isEmpty(sheetGatherInfos)){
				gatherInfoService.createAll(sheetGatherInfos);
			}
			if(!bAutoOrWord) {
				sheetTagService.deleteBySheetId(id);
				if (!CollectionUtils.isEmpty(sheetTags)) {
					sheetTagService.createAll(sheetTags);
				}
			}
		} catch (Exception e) {
			logger.error("保存表格数据失败",e);
			logger.info("保存表格数据失败数据追踪id："+(id==null?"kong":id.toString()));
			Object json = JSON.toJSON(sheetDatas);
			logger.info("保存表格数据失败数据追踪sheetDatas："+(json.toString()));
			throw new RuntimeException(e);
		}
	}

	@Override
	@Transactional(readOnly=true)
	public Collection<Map<String, Object>> getParameterInfoByDesgignId(UUID designId) {
		return sheetDao.getParameterInfoByDesignId(designId);
		
	}
	
	@Override
	@Transactional
	public void deleteById(UUID id) throws BusinessException {
	    Sheet sheet = sheetDao.getById(id);
	    if(sheet!=null) {
	        boolean archived=(sheet.getArchived()==null)?false:sheet.getArchived();
            if(archived) {//已归档
                throw new BusinessException(OpsErrorMessage.MODULE_NAME,OpsErrorMessage.ERROR_MESSAGE,"不允许删除已归档的报表！");
            }
	    }
        sheetDao.deleteById(id);
        sheetDataService.deleteBySheetId((UUID)id);
        gatherInfoService.deleteBySheetId((UUID)id);
        sheetParameterService.deleteBySheetId((UUID)id);
        sheetSheetCategoryService.deleteBySheetId((UUID)id);	    
	}

	@Override
	@Transactional
	public void deleteByIds(String[] ids) throws BusinessException {
		for (String id : ids) {
			deleteById(UUID.fromString(id));
		}
	}

	@Override
	@Transactional
	public void updateFlagByIds(String[] ids,int newFlag,String notation,UUID userId,String organizationId)  throws BusinessException{
		if(ids != null){
			for (String id : ids) {
				Sheet sheet = sheetDao.getById(UUID.fromString(id));
				if(sheet!=null) {
				    int oldFlag=(sheet.getFlg()==null)?-1:sheet.getFlg();
				    boolean archived=(sheet.getArchived()==null)?false:sheet.getArchived();
				    if(archived) {//已归档
				        throw new BusinessException(OpsErrorMessage.MODULE_NAME,OpsErrorMessage.ERROR_MESSAGE,"不允许操作已归档的报表！");
				    }
				    //打回已上报
				    if(2==oldFlag&&3==newFlag&&organizationId.equalsIgnoreCase(sheet.getOperatedOrganizationId())) {
				        throw new BusinessException(OpsErrorMessage.MODULE_NAME,OpsErrorMessage.ERROR_MESSAGE,"不允许打回已上报的报表！");
				    }
				    //审核通过已上报
				    if(2==oldFlag&&5==newFlag&&organizationId.equalsIgnoreCase(sheet.getOperatedOrganizationId())) {
                        throw new BusinessException(OpsErrorMessage.MODULE_NAME,OpsErrorMessage.ERROR_MESSAGE,"不允许审核通过已上报的报表！");
                    }
				  //审核中已上报
                    if(2==oldFlag&&6==newFlag&&organizationId.equalsIgnoreCase(sheet.getOperatedOrganizationId())) {
                        throw new BusinessException(OpsErrorMessage.MODULE_NAME,OpsErrorMessage.ERROR_MESSAGE,"不允许将已上报的报表置为审核中！");
                    }
    				sheet.setFlg(newFlag);
    				sheet.setNotation(notation);
    				sheet.setLastModifiedBy(userId);
    				sheet.setLastModifiedTime(new Timestamp(System.currentTimeMillis()));
    				sheet.setOperatedOrganizationId(organizationId);
    				sheetDao.update(sheet);
				}
			}
		}
	}

	@Override
	@Transactional(readOnly=true)
	public Collection<Sheet> getByDesignId(UUID designId) {
		return sheetDao.getByDesignId(designId);
	}

	@Override
	@Transactional
	public List<Map> getRefSituation(String id)
	{
		return sheetDao.getRefSituation(id);
	}

	@Override
	@Transactional
	public int checkLevel(String loginName,String sheetId,String type)
	{
		return sheetDao.checkLevel(loginName,sheetId,type);
	}

	@Transactional
    @Override
    public UUID create(Sheet sheet, Collection<SheetData> sheetDatas) {
	    UUID id = (UUID) this.create(sheet);
	    sheetDataService.createAll(sheetDatas);
        return id;
    }

    @Override
    @Transactional
    public int archiveByIds(UUID categoryId,String[] ids, UUID userId, String organizationId) throws BusinessException {
        int count=0;
        if(ids != null){
            for (String id : ids) {
                UUID sheetId=UUID.fromString(id);
                Sheet sheet = sheetDao.getById(sheetId);
                if(sheet!=null) {
                    //验证是否被上级机构归档
                    String archivedOrganizationId=sheet.getArchivedOrganizationId();
                    if(archivedOrganizationId!=null&&!archivedOrganizationId.equalsIgnoreCase(organizationId)) {
                        String archivedOrganizationName=sheetDao.getAncestorOrganizationName(organizationId, archivedOrganizationId);
                        if(archivedOrganizationName!=null) {
                            throw new BusinessException(OpsErrorMessage.MODULE_NAME,OpsErrorMessage.ERROR_MESSAGE,"不允许归档上级机构 "+archivedOrganizationName+" 归档的报表 " + sheet.getName()+"！");
                        }                        
                    }
                    count+=sheetDao.archiveById(categoryId,sheet.getDesignId(),userId,organizationId);
                }
            }
        }
        return count;
    }
    @Override
    @Transactional
    public int unarchiveByIds(UUID categoryId,String[] ids, UUID userId, String organizationId) throws BusinessException {
        int count=0;
        if(ids != null){
            for (String id : ids) {
                UUID sheetId=UUID.fromString(id);
                Sheet sheet = sheetDao.getById(sheetId);
                if(sheet!=null) {
                    //验证是否被上级机构归档
                    String archivedOrganizationId=sheet.getArchivedOrganizationId();
                    if(archivedOrganizationId!=null&&!archivedOrganizationId.equalsIgnoreCase(organizationId)) {
                        String archivedOrganizationName=sheetDao.getAncestorOrganizationName(organizationId, archivedOrganizationId);
                        if(archivedOrganizationName!=null) {
                            throw new BusinessException(OpsErrorMessage.MODULE_NAME,OpsErrorMessage.ERROR_MESSAGE,"不允许取消上级机构 "+archivedOrganizationName+" 归档的报表 " + sheet.getName()+"！");
                        }
                    }
                    count+=sheetDao.unarchiveById(categoryId,sheet.getDesignId(),userId);
                }
            }
        }
        return count;
    }
}