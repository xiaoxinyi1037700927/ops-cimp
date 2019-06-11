package com.sinosoft.ops.cimp.service.sheet.impl;


import com.sinosoft.ops.cimp.common.service.BaseEntityServiceImpl;
import com.sinosoft.ops.cimp.constant.OpsErrorMessage;
import com.sinosoft.ops.cimp.entity.sheet.*;
import com.sinosoft.ops.cimp.exception.BusinessException;
import com.sinosoft.ops.cimp.repository.sheet.SheetDesignCategoryDao;
import com.sinosoft.ops.cimp.repository.sheet.SheetDesignDao;
import com.sinosoft.ops.cimp.service.sheet.*;
import com.sinosoft.ops.cimp.util.ParticularUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 表格设计服务
 * @author Nil
 * @version 1.0
 * @created 21-6月-2017 13:17:37
 */
@Service("sheetDesignService")
public class SheetDesignServiceImpl extends BaseEntityServiceImpl<SheetDesign> implements SheetDesignService {
	private static final Logger logger = LoggerFactory.getLogger(SheetDesignServiceImpl.class);

    @Autowired
    private SheetDesignDao sheetDesignDao = null;
    @Autowired
    private SheetDesignCategoryDao sheetDesignCategoryDao = null;
    @Autowired
    private SheetDesignDesignCategoryService sheetDesignDesignCategoryService = null;
	@Autowired
	private SheetDesignCarrierService sheetDesignCarrierService;

	@Autowired
	private SheetService sheetService;

	@Autowired
	private SheetConditionService sheetConditionService;
	@Autowired
	private SheetConditionItemService sheetConditionItemService;
	@Autowired
	private SheetConditionCategoryService sheetConditionCategoryService;

	@Autowired
	private SheetDataSourceService sheetDataSourceService;

	@Autowired
	private SheetDataSourceCategoryService sheetDataSourceCategoryService;

	@Autowired
	private SheetParameterService sheetParameterService;

	@Autowired
	private SheetDesignCellService sheetDesignCellService;

	@Autowired
	private SheetDesignConditionService sheetDesignConditionService;

	@Autowired
	private SheetDesignDataSourceService sheetDesignDataSourceService;

	@Autowired
	private SheetDesignExpressionService sheetDesignExpressionService;

	@Autowired
	private SheetDesignFieldService sheetDesignFieldService;
	@Autowired
	private SheetDesignFieldBindingService sheetDesignFieldBindingService;

	@Autowired
	private SheetDesignParameterService sheetDesignParameterService;
	@Autowired
	private SheetDesignPageSetupService sheetDesignPageSetupService;
	@Autowired
	private SheetDesignSectionService sheetDesignSectionService;
	@Autowired
	private SheetDesignVariableService sheetDesignVariableService;
	@Autowired
	SheetComService sheetComService;

	public SheetDesignServiceImpl(){

	}
    /**
     * 根据Id删除
     * @param id 标识
     */
	@Override
	@Transactional
    public void deleteById(Serializable id){
		sheetDesignDao.deleteById(id);
		sheetDesignCarrierService.deleteByDesignId((UUID) id);
		sheetDesignSectionService.deleteByDesignId((UUID)id);
		sheetDesignDataSourceService.deleteByDesignId((UUID)id);
		sheetDesignFieldService.deleteByDesignId((UUID)id);
		sheetDesignParameterService.deleteByDesignId((UUID)id);
		sheetDesignExpressionService.deleteByDesignId((UUID)id);
		sheetDesignConditionService.deleteByDesignId((UUID)id);
		sheetDesignDesignCategoryService.deleteByDesignId((UUID)id);
		if(sheetDesignPageSetupService.getByDesignId((UUID)id)!=null){
			sheetDesignPageSetupService.deleteByDesignId((UUID)id);
		}
		
	};
	/**
     * 根据表格分类获取表格设计
     * 
     * @param categoryId    表格分类标识
     */
	@Override
	@Transactional(readOnly = true)
    public Collection<SheetDesign> getByCategoryId(UUID categoryId) {
	    return sheetDesignDao.getByCategoryId(categoryId);
    }

    public void setRefNum(Object o)
	{
		Collection<SheetDesign> sheetDesigns = (Collection<SheetDesign>) o;
	}
    
	@Override
	@Transactional
    public boolean lock(UUID id, UUID userId) throws BusinessException {
	    SheetDesign sheetDesign = sheetDesignDao.getById(id);
        if (sheetDesign == null) {
            throw new BusinessException(OpsErrorMessage.MODULE_NAME,OpsErrorMessage.ERROR_MESSAGE,"标识为“"+id+"”的表格不存在！");
        }
	    sheetDesign.setLocked(true);
	    sheetDesign.setLastModifiedBy(userId);
	    sheetDesign.setLastModifiedTime(new Timestamp(System.currentTimeMillis()));
	    sheetDesignDao.update(sheetDesign);
	    return true;
    }

	@Override
	@Transactional
    public boolean unlock(UUID id, UUID userId) throws BusinessException {
	    SheetDesign sheetDesign = sheetDesignDao.getById(id);
        if (sheetDesign == null) {
			throw new BusinessException(OpsErrorMessage.MODULE_NAME,OpsErrorMessage.ERROR_MESSAGE,"标识为“"+id+"”的表格不存在！");
        }
        sheetDesign.setLocked(false);
        sheetDesign.setLastModifiedBy(userId);
        sheetDesign.setLastModifiedTime(new Timestamp(System.currentTimeMillis()));
        sheetDesignDao.update(sheetDesign);
        return true;
    }
    
	@Override
	@Transactional(readOnly = true)
    public boolean exportToFile(UUID id, String filename) throws FileNotFoundException, IOException, BusinessException  {
	    SheetDesign sheetDesign = sheetDesignDao.getById(id);
	    if (sheetDesign == null) {
			throw new BusinessException(OpsErrorMessage.MODULE_NAME,OpsErrorMessage.ERROR_MESSAGE,"标识为“"+id+"”的表格不存在！");
        }
	    try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))){
	        oos.writeObject(sheetDesign);
	    }
	    return true;
	}
	@Override
	@Transactional
    public SheetDesign importFromFile(String filename, UUID userId) throws FileNotFoundException, IOException, ClassNotFoundException  {
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))){
            SheetDesign entity = (SheetDesign) ois.readObject();
            entity.setLastModifiedBy(userId);
            entity.setLastModifiedTime(new Timestamp(System.currentTimeMillis()));
            sheetDesignDao.saveOrUpdate(entity);
            return entity;
        }
    }

	@Override
	@Transactional
    public boolean increaseVersion(UUID id, UUID userId) throws BusinessException {
        SheetDesign sheetDesign = sheetDesignDao.getById(id);
        if (sheetDesign == null) {
			throw new BusinessException(OpsErrorMessage.MODULE_NAME,OpsErrorMessage.ERROR_MESSAGE,"标识为“"+id+"”的表格不存在！");
        }
        sheetDesign.setVersion(sheetDesign.getVersion() + 1);
        sheetDesign.setLastModifiedBy(userId);
        sheetDesign.setLastModifiedTime(new Timestamp(System.currentTimeMillis()));
        sheetDesignDao.update(sheetDesign);
        return true;
    }

    @Override
    @Transactional
    public boolean archive(UUID id, UUID userId) throws BusinessException {
        SheetDesign sheetDesign = sheetDesignDao.getById(id);
        if (sheetDesign == null) {
			throw new BusinessException(OpsErrorMessage.MODULE_NAME,OpsErrorMessage.ERROR_MESSAGE,"标识为“"+id+"”的表格不存在！");
        }
        sheetDesign.setArchived(true);
        sheetDesign.setLastModifiedBy(userId);
        sheetDesign.setLastModifiedTime(new Timestamp(System.currentTimeMillis()));
        sheetDesignDao.update(sheetDesign);
        return true;
    }
    @Override
    @Transactional
    public boolean unarchive(UUID id, UUID userId) throws BusinessException {
        SheetDesign sheetDesign = sheetDesignDao.getById(id);
        if (sheetDesign == null) {
			throw new BusinessException(OpsErrorMessage.MODULE_NAME,OpsErrorMessage.ERROR_MESSAGE,"标识为“"+id+"”的表格不存在！");
        }
        sheetDesign.setArchived(false);
        sheetDesign.setLastModifiedBy(userId);
        sheetDesign.setLastModifiedTime(new Timestamp(System.currentTimeMillis()));
        sheetDesignDao.update(sheetDesign);
        return true;
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
		Collection<SheetDesignCategory> catColl = sheetDesignCategoryDao.getChildren(upCatigoryId);
		if (catColl != null && catColl.size() > 0) {
			for (SheetDesignCategory aCat : catColl) {
				UUID aId = aCat.getId();
				coll.add(aId);
				coll = getChildCatigories(coll, aId);
			}
		}
		return coll;
	}

	@Override
	@Transactional
	public boolean checkSheetNo(String SheetNo)
	{
		return sheetDesignDao.checkSheetNo(SheetNo);
	}

	@Override
	@Transactional
	public boolean checkSheetNo(String SheetNo,UUID id)
	{
		return sheetDesignDao.checkSheetNo(SheetNo,id);
	}


	@Override
	@Transactional
	public void output(SheetDesign entity)
	{
		try
		{
			update(entity);
			Collection<UUID> sheetCategoryIds = sheetDesignDao.getSheetCategoryId(entity.getId());
			for(Object ob : sheetCategoryIds)
			{
				UUID sheetCategoryId =UUID.fromString(sheetDesignDao.toUUIDStringByRaw(ob));
				Object obid = sheetService.getOnlyByCategoryId(sheetCategoryId);
				if(obid!=null)
				{
					Collection<SheetParameter> sheetParameters = sheetParameterService.getBySheetId((UUID)obid);
					Sheet sheet = new Sheet();
					sheet.setDesignId(entity.getId());
					sheet.setId(UUID.randomUUID());
					sheet.setName(entity.getName());
					sheetService.create(sheet,sheetCategoryId);
					for(SheetParameter sheetParameter :sheetParameters)
					{
						SheetParameter newSheetParameter = ParticularUtils.objectCopy(sheetParameter);
						newSheetParameter.setId(UUID.randomUUID());
						newSheetParameter.setSheetId(sheet.getId());
						sheetParameterService.create(newSheetParameter);
					}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	@Transactional
	public UUID create(SheetDesign entity, UUID categoryId) {
		UUID upId = entity.getId();
		UUID newId = UUID.randomUUID();
		Timestamp now = new Timestamp(System.currentTimeMillis());
		entity.setLastModifiedBy(entity.getCreatedBy());
		entity.setCreatedTime(now);
		entity.setLastModifiedTime(now);
		if (upId == null) {
			int upOrdinal = sheetDesignDao.getMaxOrdinal(categoryId);
			entity.setId(newId);
			entity.setOrdinal(upOrdinal + 1);
			this.create(entity);
		} else {
			int upOrdinal = sheetDesignDao.getMaxOrdinal(categoryId);
			entity.setOrdinal(upOrdinal + 1);
			this.create(entity);
		}
		//增加关系表数据
		SheetDesignDesignCategory designDesignDesignCategory = new SheetDesignDesignCategory();
		designDesignDesignCategory.setId(UUID.randomUUID());
		designDesignDesignCategory.setCategoryId(categoryId);
		designDesignDesignCategory.setDesignId(newId);
		designDesignDesignCategory.setStatus((byte)0);
		designDesignDesignCategory.setCreatedBy(entity.getCreatedBy());
		designDesignDesignCategory.setLastModifiedBy(entity.getCreatedBy());
		designDesignDesignCategory.setCreatedTime(now);
		designDesignDesignCategory.setLastModifiedTime(now);
		sheetDesignDesignCategoryService.create(designDesignDesignCategory);
		return newId;
	}


	@Transactional
	public UUID Sercreate(SheetDesign entity, UUID categoryId) {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		entity.setLastModifiedBy(entity.getCreatedBy());
		entity.setCreatedTime(now);
		entity.setLastModifiedTime(now);
		int upOrdinal = sheetDesignDao.getMaxOrdinal(categoryId);
		entity.setOrdinal(upOrdinal + 1);
		this.create(entity);
		//增加关系表数据
		SheetDesignDesignCategory designDesignDesignCategory = new SheetDesignDesignCategory();
		designDesignDesignCategory.setId(UUID.randomUUID());
		designDesignDesignCategory.setCategoryId(categoryId);
		designDesignDesignCategory.setDesignId(entity.getId());
		designDesignDesignCategory.setStatus((byte)0);
		designDesignDesignCategory.setCreatedBy(entity.getCreatedBy());
		designDesignDesignCategory.setLastModifiedBy(entity.getCreatedBy());
		designDesignDesignCategory.setCreatedTime(now);
		designDesignDesignCategory.setLastModifiedTime(now);
		sheetDesignDesignCategoryService.create(designDesignDesignCategory);
		return entity.getId();
	}

	@Override
	@Transactional
	public UUID saveAs(SheetDesign entity, UUID categoryId) throws Exception {

		UUID oldid  = entity.getId();
		SheetDesign oldSheetDesign = getById(oldid);

		UUID newid = UUID.randomUUID();
		entity.setId(newid);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		entity.setLastModifiedBy(entity.getCreatedBy());
		entity.setReleaseTime(null);
		entity.setCreatedTime(now);
		entity.setLastModifiedTime(now);
		entity.setOrdinal(getNextOrdinal());
		this.create(entity);

		//复制关系表数据
		SheetDesignDesignCategory designDesignDesignCategory = new SheetDesignDesignCategory();
		designDesignDesignCategory.setId(UUID.randomUUID());
		designDesignDesignCategory.setCategoryId(categoryId);
		designDesignDesignCategory.setDesignId(entity.getId());
		designDesignDesignCategory.setStatus((byte)0);
		designDesignDesignCategory.setCreatedBy(entity.getCreatedBy());
		designDesignDesignCategory.setLastModifiedBy(entity.getCreatedBy());
		designDesignDesignCategory.setCreatedTime(now);
		designDesignDesignCategory.setLastModifiedTime(now);
		sheetDesignDesignCategoryService.create(designDesignDesignCategory);

		//复制内容数据
		SheetDesignCarrier sheetDesignCarrier = sheetDesignCarrierService.getByDesignId(oldid);
		SheetDesignCarrier newSheetDesignCarrier = ParticularUtils.objectCopy(sheetDesignCarrier);
		newSheetDesignCarrier.setDesignId(newid);
		newSheetDesignCarrier.setId(UUID.randomUUID());
		sheetDesignCarrierService.create(newSheetDesignCarrier);

		//复制条件数据
		for(SheetDesignCondition temp : sheetDesignConditionService.getByDesignId(oldid))
		{
			SheetDesignCondition sheetDesignCondition = ParticularUtils.objectCopy(temp);
			sheetDesignCondition.setDesignId(newid);
			sheetDesignCondition.setId(UUID.randomUUID());
			sheetDesignCondition.setCreatedTime(now);
			sheetDesignCondition.setLastModifiedTime(now);
			sheetDesignCondition.setStatus((byte)0);
			sheetDesignCondition.setOrdinal(sheetDesignConditionService.getNextOrdinal());
			sheetDesignConditionService.create(sheetDesignCondition);
		}

		//复制数据源数据
		for(SheetDesignDataSource temp : sheetDesignDataSourceService.getByDesignId(oldid))
		{
			SheetDesignDataSource sheetDesignDataSource = ParticularUtils.objectCopy(temp);
			sheetDesignDataSource.setDesignId(newid);
			sheetDesignDataSource.setId(UUID.randomUUID());
			sheetDesignDataSourceService.create(sheetDesignDataSource);
		}

		//复制表达式数据
		for(SheetDesignExpression temp : sheetDesignExpressionService.getByDesignId(oldid))
		{
			SheetDesignExpression sheetDesignExpression = ParticularUtils.objectCopy(temp);
			sheetDesignExpression.setDesignId(newid);
			sheetDesignExpression.setId(UUID.randomUUID());
			sheetDesignExpression.setContent(sheetDesignExpression.getContent().replace(String.format("T%sR",oldSheetDesign.getSheetNo()),String.format("T%sR",entity.getSheetNo())));
			sheetDesignExpressionService.create(sheetDesignExpression);
		}

		//复制数据项数据
		for(SheetDesignField temp : sheetDesignFieldService.getByDesignId(oldid))
		{
			SheetDesignField sheetDesignField = ParticularUtils.objectCopy(temp);
			sheetDesignField.setDesignId(newid);
			UUID fieldid= UUID.randomUUID();
			sheetDesignField.setId(fieldid);
			sheetDesignFieldService.create(sheetDesignField);

			for(SheetDesignFieldBinding binding:sheetDesignFieldBindingService.getByFieldId(temp.getId()))
			{
				SheetDesignFieldBinding sheetDesignFieldBinding = ParticularUtils.objectCopy(binding);
				sheetDesignFieldBinding.setDesignId(newid);
				sheetDesignFieldBinding.setFieldId(fieldid);
				sheetDesignFieldBinding.setId(UUID.randomUUID());
				sheetDesignFieldBindingService.create(sheetDesignFieldBinding);
			}
		}

		//复制参数数据
		for(SheetDesignParameter temp : sheetDesignParameterService.getByDesignId(oldid))
		{
			SheetDesignParameter sheetDesignParameter = ParticularUtils.objectCopy(temp);
			sheetDesignParameter.setDesignId(newid);
			sheetDesignParameter.setId(UUID.randomUUID());
			sheetDesignParameterService.create(sheetDesignParameter);
		}

		//复制数据区数据
		for(SheetDesignSection temp : sheetDesignSectionService.getByDesignId(oldid))
		{
			SheetDesignSection sheetDesignSection = ParticularUtils.objectCopy(temp);
			sheetDesignSection.setDesignId(newid);
			sheetDesignSection.setId(UUID.randomUUID());
			sheetDesignSectionService.create(sheetDesignSection);
		}

		//复制变量数据
		for(SheetDesignVariable temp : sheetDesignVariableService.getByDesignId(oldid))
		{
			SheetDesignVariable sheetDesignVariable = ParticularUtils.objectCopy(temp);
			sheetDesignVariable.setDesignId(newid);
			sheetDesignVariable.setId(UUID.randomUUID());
			sheetDesignVariableService.create(sheetDesignVariable);
		}

		//复制界面设置数据
		SheetDesignPageSetup sheetDesignPageSetup = sheetDesignPageSetupService.getByDesignId(oldid);
		if(sheetDesignPageSetup!=null)
		{
			SheetDesignPageSetup newSheetDesignPageSetup = ParticularUtils.objectCopy(sheetDesignPageSetup);
			newSheetDesignPageSetup.setDesignId(newid);
			sheetDesignPageSetupService.create(newSheetDesignPageSetup);
		}

		return entity.getId();
	}

	@Override
	@Transactional
	public Collection<String> getSecCondition(String designId,String sectionNo)
	{
		return sheetDesignDao.getSecCondition(designId,sectionNo);
	}

	@Override
	@Transactional
	public Collection<String> getSecField(String designId,String sectionNo)
	{
		return sheetDesignDao.getSecField(designId,sectionNo);
	}

	@Override
	@Transactional
	public String ouputCondition(UUID designId,String FilePath) throws Exception
	{

		SheetDesign sheetDesign = getById(designId);
		String fileName = FilePath + sheetDesign.getName() + ".txt";
		createFile(fileName);

		Collection<SheetDesignSection> sections = sheetDesignSectionService.
				getByDesignId(designId);
		List<SheetDesignCondition> designConditions = sheetDesignConditionService.getByDesignId(designId);

		AddWrite(fileName,"(" + sheetDesign.getSheetNo()+")"+sheetDesign.getName()+"条件明细如下:");
		AddWrite(fileName,"");

		if("23".contains(sheetDesign.getType().toString()))
		{
			for(SheetDesignSection section: sections)
			{
				AddWrite(fileName,"数据块--" + section.getName() + " 绑定条件如下:");
				for(SheetDesignCondition designCondition: designConditions.stream().filter(item -> item.getSectionNo()!=null && item.getSectionNo().equals(section.getSectionNo())).collect(Collectors.toList()))
				{
					AddWrite(fileName,designCondition.getConditionName());
				}
				AddWrite(fileName,"");
			}
		}
		else
		{
			int iMinRow=1,iMaxRow=1,iMinColumn=1,iMaxColumn=1;
			for(SheetDesignSection section: sections)
			{
				iMinRow=iMinRow>section.getStartRowNo()?section.getStartRowNo():iMinRow;
				iMaxRow=iMaxRow<section.getEndRowNo()?section.getEndRowNo():iMaxRow;
				iMinColumn=iMinColumn>section.getStartColumnNo()?section.getStartColumnNo():iMinColumn;
				iMaxColumn=iMaxColumn<section.getEndColumnNo()?section.getEndColumnNo():iMaxColumn;
			}

			for(int i=iMinRow;i<=iMaxRow;i++)
			{
				for(int j=iMinColumn;j<=iMaxColumn;j++)
				{
					AddWrite(fileName,"第" + i + "行 第" + j +"列 绑定条件如下:");
					for(SheetDesignCondition designCondition: designConditions.stream().filter(item -> item.getStartColumnNo() !=null).collect(Collectors.toList()))
					{
						if(designCondition.getStartRowNo()<=i && designCondition.getEndRowNo()>=i &&designCondition.getStartColumnNo()<=j && designCondition.getEndColumnNo()>=j)
						{
							AddWrite(fileName,designCondition.getConditionName());
						}
						if(designCondition.getStartRowNo()<=i && designCondition.getEndRowNo()>=i && designCondition.getStartColumnNo()==-1)
						{
							AddWrite(fileName,designCondition.getConditionName());
						}
						if(designCondition.getStartRowNo()==-1 &&designCondition.getStartColumnNo()<=j && designCondition.getEndColumnNo()>=j)
						{
							AddWrite(fileName,designCondition.getConditionName());
						}
						if(designCondition.getStartRowNo()==-1 && designCondition.getEndColumnNo()==-1)
						{
							AddWrite(fileName,designCondition.getConditionName());
						}
					}
					AddWrite(fileName,"");
				}
			}
		}

		return  fileName;
	}

	private boolean createFile(String filePath) throws Exception{
		try{
			File file = new File(filePath);
			if (!file.exists()) {
				file.createNewFile();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return true;
	}

	private void AddWrite(String filePath, String content) {
		try {
			//构造函数中的第二个参数true表示以追加形式写文件
			FileWriter fw = new FileWriter(filePath,true);
			fw.write(content);
			fw.write("\r\n");
			fw.close();
		} catch (IOException e) {
			System.out.println("文件写入失败！" + e);
		}
	}

	@Override
	@Transactional
	public String write2File(UUID id,String FilePath)
	{

		SheetDesign sheetDesign = getById(id);
		LinkedHashMap sheetDesignMap= new LinkedHashMap();

		String fileName = sheetDesign.getName()+sheetDesign.getId().toString();

		int i=0;
		sheetDesignMap.put(String.format("SheetDesign-%s",i++),sheetDesign);

		//内容数据
		SheetDesignCarrier sheetDesignCarrier = sheetDesignCarrierService.getByDesignId(id);
		sheetDesignMap.put(String.format("SheetDesignCarrier-%s",i++),sheetDesignCarrier);

		//条件数据
		for(SheetDesignCondition temp : sheetDesignConditionService.getByDesignId(id))
		{
			sheetDesignMap.put(String.format("SheetDesignCondition-%s",i++),sheetDesignConditionService.getById(temp.getId()));
			SheetCondition sheetCondition =sheetConditionService.getById(temp.getConditionId());
			sheetDesignMap.put(String.format("SheetCondition-%s",i++),sheetCondition);
			for(SheetConditionItem sheetConditionItem:sheetConditionItemService.GetDataByConditionID(temp.getConditionId().toString()))
			{
				sheetDesignMap.put(String.format("SheetConditionItem-%s",i++),sheetConditionItem);
			}
		}

		//数据源数据
		for(SheetDesignDataSource temp : sheetDesignDataSourceService.getByDesignId(id))
		{
			sheetDesignMap.put(String.format("SheetDesignDataSource-%s",i++),sheetDesignDataSourceService.getById(temp.getId()));
			if(temp.getDatasourceId()!=null)
			{
				SheetDataSource sheetDataSource =sheetDataSourceService.getById(temp.getDatasourceId());
				sheetDesignMap.put(String.format("SheetDataSource-%s",i++),sheetDataSource);
			}
		}

		//数据项数据
		for(SheetDesignField temp : sheetDesignFieldService.getByDesignId(id))
		{
			sheetDesignMap.put(String.format("SheetDesignField-%s",i++),temp);
			for(SheetDesignFieldBinding sheetDesignFieldBinding:sheetDesignFieldBindingService.getByFieldId(temp.getId()))
			{
				sheetDesignMap.put(String.format("SheetDesignFieldBinding-%s",i++),sheetDesignFieldBinding);
			}
		}

		//表达式数据
		for(SheetDesignExpression temp : sheetDesignExpressionService.getByDesignId(id))
		{
			sheetDesignMap.put(String.format("SheetDesignExpression-%s",i++),temp);
		}

		//参数数据
		for(SheetDesignParameter temp : sheetDesignParameterService.getByDesignId(id))
		{
			sheetDesignMap.put(String.format("SheetDesignParameter-%s",i++),temp);
		}

		//数据块数据
		for(SheetDesignSection temp : sheetDesignSectionService.getByDesignId(id))
		{
			sheetDesignMap.put(String.format("SheetDesignSection-%s",i++),temp);
		}

		//变量数据
		for(SheetDesignVariable temp : sheetDesignVariableService.getByDesignId(id))
		{
			sheetDesignMap.put(String.format("SheetDesignVariable-%s",i++),temp);
		}

		//界面设置数据
		SheetDesignPageSetup sheetDesignPageSetup = sheetDesignPageSetupService.getByDesignId(id);
		if(sheetDesignPageSetup!=null)
		{
			sheetDesignMap.put(String.format("SheetDesignPageSetup-%s",i++),sheetDesignPageSetup);
		}

		writeObject(FilePath+fileName,sheetDesignMap);
		return  fileName;
	}

	@Override
	@Transactional
	public void readAndSave(LinkedHashMap serializableMap,String categoryId) throws  Exception
	{
		for (Object model : serializableMap.keySet())
		{
			String strModel = model.toString().split("-")[0];
			if(strModel.equals("SheetDesign"))
			{
				SheetDesign sheetDesign=(SheetDesign)serializableMap.get(model);
				sheetDesignFieldService.deleteByDesignId(sheetDesign.getId());
				sheetDesignConditionService.deleteByDesignId(sheetDesign.getId());
				sheetDesignDataSourceService.deleteByDesignId(sheetDesign.getId());
				sheetDesignFieldBindingService.deleteByDesignId(sheetDesign.getId());
				sheetDesignExpressionService.deleteByDesignId(sheetDesign.getId());
				sheetDesignParameterService.deleteByDesignId(sheetDesign.getId());
				sheetDesignSectionService.deleteByDesignId(sheetDesign.getId());
				if(getById(sheetDesign.getId())!=null)
				{
					merge(sheetDesign);
				}
				else
				{
					Sercreate(sheetDesign,UUID.fromString(categoryId));
				}
			}
			else if(strModel.equals("SheetDesignCarrier"))
			{
				SheetDesignCarrier sheetDesignCarrier=(SheetDesignCarrier)serializableMap.get(model);
				if(sheetDesignCarrierService.getById(sheetDesignCarrier.getId())!=null)
				{
					sheetDesignCarrierService.merge(sheetDesignCarrier);
				}
				else
				{
					sheetDesignCarrierService.create(sheetDesignCarrier);
				}
			}
			else if(strModel.equals("SheetDesignCondition"))
			{
				SheetDesignCondition sheetDesignCondition=(SheetDesignCondition)serializableMap.get(model);
				if(sheetDesignConditionService.getById(sheetDesignCondition.getId())!=null)
				{
					sheetDesignConditionService.merge(sheetDesignCondition);
				}
				else
				{
					sheetDesignConditionService.create(sheetDesignCondition);
				}
			}
			else if(strModel.equals("SheetCondition"))
			{
				SheetCondition sheetCondition=(SheetCondition)serializableMap.get(model);
				SheetCondition sheetConditionold=sheetConditionService.getById(sheetCondition.getId());
				if(sheetConditionold!=null)
				{
					sheetCondition.setCategoryId(sheetConditionold.getCategoryId());
					sheetConditionService.merge(sheetCondition);
				}
				else
				{
					if(sheetCondition.getDesignId()==null)
					{
						if(sheetConditionCategoryService.getById(sheetCondition.getCategoryId())==null)
						{
							sheetCondition.setCategoryId(sheetConditionCategoryService.getFisrtId());
						}
					}
					sheetConditionService.create(sheetCondition);
				}
			}
			else if(strModel.equals("SheetConditionItem"))
			{
				SheetConditionItem sheetConditionItem=(SheetConditionItem)serializableMap.get(model);
				if(sheetConditionItemService.getById(sheetConditionItem.getId())!=null)
				{
					sheetConditionItemService.merge(sheetConditionItem);
				}
				else
				{
					sheetConditionItemService.create(sheetConditionItem);
				}
			}
			else if(strModel.equals("SheetDesignDataSource"))
			{
				SheetDesignDataSource sheetDesignDataSource=(SheetDesignDataSource)serializableMap.get(model);
				if(sheetDesignDataSourceService.getById(sheetDesignDataSource.getId())!=null)
				{
					sheetDesignDataSourceService.merge(sheetDesignDataSource);
				}
				else
				{
					sheetDesignDataSourceService.create(sheetDesignDataSource);
				}
			}
			else if(strModel.equals("SheetDataSource"))
			{
				SheetDataSource sheetDataSource=(SheetDataSource)serializableMap.get(model);
				SheetDataSource sheetDataSourceold = sheetDataSourceService.getById(sheetDataSource.getId());
				if(sheetDataSourceold!=null)
				{
					sheetDataSource.setCategoryId(sheetDataSourceold.getCategoryId());
					sheetDataSourceService.merge(sheetDataSource);
				}
				else
				{
					if(sheetDataSourceCategoryService.getById(sheetDataSource.getCategoryId())==null)
					{
						sheetDataSource.setCategoryId(sheetDataSourceCategoryService.getFisrtId());
					}
					sheetDataSourceService.create(sheetDataSource);
				}
			}
			else if(strModel.equals("SheetDesignField"))
			{
				SheetDesignField sheetDesignField=(SheetDesignField)serializableMap.get(model);
				if(sheetDesignFieldService.getById(sheetDesignField.getId())!=null)
				{
					sheetDesignFieldService.merge(sheetDesignField);
				}
				else
				{
					sheetDesignFieldService.create(sheetDesignField);
				}
			}
			else if(strModel.equals("SheetDesignFieldBinding"))
			{
				SheetDesignFieldBinding sheetDesignFieldBinding=(SheetDesignFieldBinding)serializableMap.get(model);
				if(sheetDesignFieldBindingService.getById(sheetDesignFieldBinding.getId())!=null)
				{
					sheetDesignFieldBindingService.merge(sheetDesignFieldBinding);
				}
				else
				{
					sheetDesignFieldBindingService.create(sheetDesignFieldBinding);
				}
			}
			else if(strModel.equals("SheetDesignExpression"))
			{
				SheetDesignExpression sheetDesignExpression=(SheetDesignExpression)serializableMap.get(model);
				if(sheetDesignExpressionService.getById(sheetDesignExpression.getId())!=null)
				{
					sheetDesignExpressionService.merge(sheetDesignExpression);
				}
				else
				{
					sheetDesignExpressionService.create(sheetDesignExpression);
				}
			}
			else if(strModel.equals("SheetDesignParameter"))
			{
				SheetDesignParameter sheetDesignParameter=(SheetDesignParameter)serializableMap.get(model);
				if(sheetDesignParameterService.getById(sheetDesignParameter.getId())!=null)
				{
					sheetDesignParameterService.merge(sheetDesignParameter);
				}
				else
				{
					sheetDesignParameterService.create(sheetDesignParameter);
				}
			}
			else if(strModel.equals("SheetDesignSection"))
			{
				SheetDesignSection sheetDesignSection=(SheetDesignSection)serializableMap.get(model);
				if(sheetDesignSectionService.getById(sheetDesignSection.getId())!=null)
				{
					sheetDesignSectionService.merge(sheetDesignSection);
				}
				else
				{
					sheetDesignSectionService.create(sheetDesignSection);
				}
			}
			else if(strModel.equals("SheetDesignVariable"))
			{
				SheetDesignVariable sheetDesignVariable=(SheetDesignVariable)serializableMap.get(model);
				if(sheetDesignVariableService.getById(sheetDesignVariable.getId())!=null)
				{
					sheetDesignVariableService.merge(sheetDesignVariable);
				}
				else
				{
					sheetDesignVariableService.create(sheetDesignVariable);
				}
			}
			else if(strModel.equals("SheetDesignPageSetup"))
			{
				SheetDesignPageSetup sheetDesignPageSetup=(SheetDesignPageSetup)serializableMap.get(model);
				if(sheetDesignPageSetupService.getById(sheetDesignPageSetup.getId())!=null)
				{
					sheetDesignPageSetupService.merge(sheetDesignPageSetup);
				}
				else
				{
					sheetDesignPageSetupService.create(sheetDesignPageSetup);
				}
			}
		}
	}

	@Override
	@Transactional
	public UUID copy(SheetDesign entity, UUID categoryId) {
		UUID newId = UUID.randomUUID();
		Timestamp now = new Timestamp(System.currentTimeMillis());
		UUID oldId = entity.getId();
		SheetDesign oldItem = this.getById(oldId);
		int oldOrdinal = oldItem.getOrdinal();
		UUID userId = entity.getCreatedBy();
		String name = oldItem.getName();
		sheetDesignDao.addOrdinals(oldOrdinal, categoryId, entity.getCreatedBy());
		BeanUtils.copyProperties(oldItem, entity);
		entity.setCreatedTime(now);
		entity.setCreatedBy(userId);
		entity.setLastModifiedTime(now);
		entity.setLastModifiedBy(userId);
		entity.setOrdinal(oldOrdinal + 1);
		entity.setId(newId);
		entity.setName(name + "-复制");
		/*
		entity.setId(newId);
		entity.setAppId(oldItem.getAppId());
		entity.setArchived(oldItem.getArchived());
		entity.setBottom(oldItem.getBottom());
		entity.setCarriers(oldItem.getCarriers());
		entity.setCells(oldItem.getCells());
		entity.setConditions(oldItem.getConditions());
		entity.setCreatedTime(now);
		entity.setDataFillType(oldItem.getDataFillType());
		entity.setDataSources(oldItem.getDataSources());
		entity.setDescription(oldItem.getDescription());
		entity.setEditable(oldItem.getEditable());
		entity.setExpressions(oldItem.getExpressions());
		entity.setFields(oldItem.getFields());
		entity.setInstructions(oldItem.getInstructions());
		entity.setLastModifiedBy(entity.getCreatedBy());
		entity.setLastModifiedTime(now);
		entity.setLeft(oldItem.getLeft());
		entity.setLocked(oldItem.getLocked());
		entity.setName(oldItem.getName());
		entity.setOrdinal(oldOrdinal + 1);
		entity.setPageSetup(oldItem.getPageSetup());
		entity.setParameters(oldItem.getParameters());
		entity.setReleaseComId(oldItem.getReleaseComId());
		entity.setReleaseComName(oldItem.getReleaseComName());
		entity.setReleaseTime(now);
		entity.setRight(oldItem.getRight());
		entity.setSections(oldItem.getSections());
		entity.setSheetNo(oldItem.getSheetNo());
		entity.setSqls(oldItem.getSqls());
		entity.setStatus(oldItem.getStatus());
		entity.setTags(oldItem.getTags());
		entity.setTop(oldItem.getTop());
		entity.setType(oldItem.getType());
		entity.setVariables(oldItem.getVariables());
		entity.setVersion(oldItem.getVersion());
		*/
		this.create(entity);
		//增加关系表数据
		SheetDesignDesignCategory designDesignDesignCategory = new SheetDesignDesignCategory();
		designDesignDesignCategory.setId(UUID.randomUUID());
		designDesignDesignCategory.setCategoryId(categoryId);
		designDesignDesignCategory.setDesignId(newId);
		designDesignDesignCategory.setStatus((byte)0);
		designDesignDesignCategory.setCreatedBy(userId);
		designDesignDesignCategory.setLastModifiedBy(userId);
		designDesignDesignCategory.setCreatedTime(now);
		designDesignDesignCategory.setLastModifiedTime(now);
		sheetDesignDesignCategoryService.create(designDesignDesignCategory);
		return newId;
	}
	
	@Override
	@Transactional
	public int updateOne(SheetDesign entity) {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		UUID oldId = entity.getId();
		SheetDesign oldItem = this.getById(oldId);
		Timestamp createTime = oldItem.getCreatedTime();
		UUID createBy = oldItem.getCreatedBy();
		int ordinal = oldItem.getOrdinal();
		BeanUtils.copyProperties(entity, oldItem);
		oldItem.setLastModifiedTime(now);
		oldItem.setCreatedTime(createTime);
		oldItem.setCreatedBy(createBy);
		oldItem.setOrdinal(ordinal);
		if (entity.getReleaseTime() != null) {
			oldItem.setReleaseTime(now);
		}
		/*
		oldItem.setAppId(entity.getAppId());
		oldItem.setArchived(entity.getArchived());
		oldItem.setBottom(entity.getBottom());
		oldItem.setCarriers(entity.getCarriers());
		oldItem.setCells(entity.getCells());
		oldItem.setConditions(entity.getConditions());
		oldItem.setDataFillType(entity.getDataFillType());
		oldItem.setDataSources(entity.getDataSources());
		oldItem.setDescription(entity.getDescription());
		oldItem.setEditable(entity.getEditable());
		oldItem.setExpressions(entity.getExpressions());
		oldItem.setFields(entity.getFields());
		oldItem.setInstructions(entity.getInstructions());
		oldItem.setLastModifiedBy(entity.getLastModifiedBy());
		oldItem.setLastModifiedTime(now);
		oldItem.setLeft(entity.getLeft());
		oldItem.setLocked(entity.getLocked());
		oldItem.setName(entity.getName());
		oldItem.setPageSetup(entity.getPageSetup());
		oldItem.setParameters(entity.getParameters());
		oldItem.setReleaseComId(entity.getReleaseComId());
		oldItem.setReleaseComName(entity.getReleaseComName());
		oldItem.setReleaseTime(entity.getReleaseTime());
		oldItem.setRight(entity.getRight());
		oldItem.setSections(entity.getSections());
		oldItem.setSheetNo(entity.getSheetNo());
		oldItem.setSqls(entity.getSqls());
		oldItem.setStatus(entity.getStatus());
		oldItem.setTags(entity.getTags());
		oldItem.setTop(entity.getTop());
		oldItem.setType(entity.getType());
		oldItem.setVariables(entity.getVariables());
		oldItem.setVersion(entity.getVersion());
		*/
		this.update(oldItem);
		return 1;
	}
	
	@Override
	@Transactional
	public boolean delete(SheetDesign entity, UUID categoryId) {
		UUID id = entity.getId();
		SheetDesignCarrier carrier = sheetDesignCarrierService.getByDesignId(id);
		if (carrier != null) {
			//有carrier记录，不能删除
			logger.info("SheetDesign的id=" + id.toString() + "，有carrier子记录，不能删除！");
			return false;
		}
		SheetDesign orgItem = this.getById(id);
		int orgOrdinal = orgItem.getOrdinal();
		//后面的节点ordinal减1
		sheetDesignDao.minusOrdinals(orgOrdinal, categoryId, entity.getLastModifiedBy());
		//节点删除
		deleteById(id);
		//删除关系表数据
		sheetDesignDesignCategoryService.deleteByDesignIdAndCategoryId(id, categoryId);
		return true;
	}
	
	@Override
	@Transactional
	public boolean moveUp(SheetDesign entity, UUID categoryId) {
		UUID id = entity.getId();
		SheetDesign curr = sheetDesignDao.getById(entity.getId());
		int ordinal = curr.getOrdinal();
		UUID userName = entity.getLastModifiedBy();
		SheetDesign previous = sheetDesignDao.findPrevious(id, categoryId);
		if (previous != null) {
			UUID preId = previous.getId();
			int preOrdinal = previous.getOrdinal();
			int cnt = sheetDesignDao.updateOrdinal(preId, ordinal, userName);
			if (cnt > 0) {
				cnt = sheetDesignDao.updateOrdinal(id, preOrdinal, userName);
				if (cnt > 0) {
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	@Transactional
	public boolean moveDown(SheetDesign entity, UUID categoryId) {
		UUID id = entity.getId();
		SheetDesign curr = sheetDesignDao.getById(entity.getId());
		int ordinal = curr.getOrdinal();
		UUID userName = entity.getLastModifiedBy();
		SheetDesign nextvious = sheetDesignDao.findNext(id, categoryId);
		if (nextvious != null) {
			UUID nextId = nextvious.getId();
			int nextOrdinal = nextvious.getOrdinal();
			int cnt = sheetDesignDao.updateOrdinal(nextId, ordinal, userName);
			if (cnt > 0) {
				cnt = sheetDesignDao.updateOrdinal(id, nextOrdinal, userName);
				if (cnt > 0) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	@Transactional
	public List<Map> getRefSituation(String id)
	{
		return sheetDesignDao.getRefSituation(id);
	}
	
	@Override
	@Transactional(readOnly=true)
	public Collection<String> getAllOrganizationByParent(Collection<String> organizationIds) {
		return sheetDesignDao.getAllOrganizationByParent(organizationIds);
	}


	private void writeObject(String filename, LinkedHashMap serializableMap) {
		try {
			File file = new File(filename);
			if (!file.exists()) {
				file.createNewFile();
			}
			// The value "false" for FileOutputStream means that overwrite this file,
			// if it is "true",append the new data to this file.
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file,false));
			oos.writeObject(serializableMap);
			oos.flush();
			oos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private LinkedHashMap readObject(String path, String filename) throws  Exception{
		File file = new File(path, filename);
		ObjectInputStream ois = null;
		LinkedHashMap serializableMap = null;

		try {
			ois = new ObjectInputStream(new FileInputStream(file));
			try {
				serializableMap = (LinkedHashMap)ois.readObject();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				ois.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return serializableMap;
	}

}