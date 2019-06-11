package com.sinosoft.ops.cimp.service.sheet.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sinosoft.ops.cimp.common.model.*;
import com.sinosoft.ops.cimp.common.service.BaseServiceImpl;
import com.sinosoft.ops.cimp.constant.OpsErrorMessage;
import com.sinosoft.ops.cimp.entity.infostruct.SysInfoItem;
import com.sinosoft.ops.cimp.entity.infostruct.SysInfoSet;
import com.sinosoft.ops.cimp.entity.sheet.*;
import com.sinosoft.ops.cimp.entity.sys.syscode.SysCodeItem;
import com.sinosoft.ops.cimp.entity.sys.syscode.SysCodeSet;
import com.sinosoft.ops.cimp.esl1.ESLLexer;
import com.sinosoft.ops.cimp.esl1.ESLParser;
import com.sinosoft.ops.cimp.esl1.SheetEvalueateListener;
import com.sinosoft.ops.cimp.exception.BusinessException;
import com.sinosoft.ops.cimp.framework.spring.datasource.DynamicDataSourceContextHolder;
import com.sinosoft.ops.cimp.repository.sheet.SheetComDao;
import com.sinosoft.ops.cimp.service.infostruct.SysInfoItemService;
import com.sinosoft.ops.cimp.service.infostruct.SysInfoSetService;
import com.sinosoft.ops.cimp.service.sheet.*;
import com.sinosoft.ops.cimp.service.sys.syscode.SysCodeItemService;
import com.sinosoft.ops.cimp.service.sys.syscode.SysCodeSetService;
import com.sinosoft.ops.cimp.util.word.FileUtils;
import com.sinosoft.ops.cimp.util.word.WordUtil;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @version 1.0.0
 * @ClassName: SheetComServiceImpl
 * @description: Sheet 通用服务实现类
 * @author: sunch
 * @date: 2018年6月7日 下午3:56:58
 * @since JDK 1.7
 */
@Service("sheetComService")
public class SheetComServiceImpl extends BaseServiceImpl implements SheetComService {

    private static final Logger logger = LoggerFactory.getLogger(SheetComServiceImpl.class);
    //当前统计执行器
    private static Map<String, LinkedBlockingQueue<SheetBuildDataExcutor1>> concurrentHashMap1 = new ConcurrentHashMap<>();
    private static LinkedBlockingQueue<SheetBuildDataExcutor1> priorityQueue = new LinkedBlockingQueue<>();
    private static ConcurrentHashMap<String, SheetStatisticsStatus> concurrentStatisticsStatus = new ConcurrentHashMap<>();
    //汇总公式运算标记 防止表间计算时 需要用到的表还没有计算完成
    private static Map<String, Object> gatherSign = new ConcurrentHashMap<>();
    /**
     * 求和
     */
    private static final String SUM = "sum";
    /**
     * 取值
     */
    private static final String VALUE = "value";
    /**
     * 计数
     */
    private static final String COUNT = "count";
    /**
     * 其它函数
     */
    private static final String OTHER = "other";

    private static final String Map = null;
    
    @Autowired
    private ThreadPoolTaskExecutor statisticsTaskExecutor;
    
//    private static ExecutorService excutor;
//	static {
//		excutor = Executors.newFixedThreadPool(10);
//	}

    @Autowired
    private SheetComDao sheetComDao;

    @Autowired
    private SheetService sheetService;

    @Autowired
    private SysInfoItemService sysInfoItemService;

    @Autowired
    private SysInfoSetService sysInfoSetService;

    @Autowired
    private SysCodeItemService sysCodeItemService;

    @Autowired
    private SysCodeSetService sysCodeSetService;

    @Autowired
    private SheetConditionService sheetConditionService;

    @Autowired
    private SheetConditionItemService sheetConditionItemService;

    @Autowired
    private SheetDesignConditionService sheetDesignConditionService;
    
    @Autowired
    private SheetDesignFieldBindingService sheetDesignFieldBindingService;

    @Autowired
    private SheetDesignFieldService sheetDesignFieldService;

    @Autowired
    private SheetDefitemService sheetDefitemService;
    
    @Autowired
    private SheetDesignSectionService sheetDesignSectionService;
    
    @Autowired
    private SheetDesignDataSourceService sheetDesignDataSourceService;
    
    @Autowired
    private SheetDesignExpressionService sheetDesignExpressionService;
    
    @Autowired
    private SheetParameterService sheetParameterService;
    
    @Autowired
    private SheetDataService sheetDataService;
    
    @Autowired 
    private SheetDesignService sheetDesignService;
    
    @Autowired
    private SheetComService sheetComService;
    
    @Autowired
    private SheetSheetCategoryService sheetSheetCategoryService;
    
//    @Value("${mainTaskExecutor.corePoolSize}")
//    private Integer maxPoolSize;
    
    @Autowired
    private ThreadPoolTaskExecutor controlTaskExecutor;
    @Autowired
    private ThreadPoolTaskExecutor storageTaskExecutor;
    
    @Override
    @Transactional
    public List<SysCodeItem> getChildCodeItemList(Integer codeSetId, String parentCode) {

        return sheetComDao.getChildCodeItemList(codeSetId, parentCode);
    }


    /**
     * 构建代码表树形结构，一次性把整个树加载出来
     */
    @Override
    public Collection<TreeNode> generateItemTree(Collection<SysCodeItem> itemList, String codeSetName) {

        //构建树形
        Collection<TreeNode> list = loadChildCodeItemList(itemList, codeSetName);

        return list;
    }

    private Collection<TreeNode> loadChildCodeItemList(Collection<SysCodeItem> itemList, String parentCode) {
        long i = System.currentTimeMillis();
        Collection<TreeNode> childTree = new ArrayList<TreeNode>();
        for (SysCodeItem sysCodeItem : itemList) {
            if (StringUtils.equals(parentCode, sysCodeItem.getParentCode())) {

                DefaultTreeNode treeModel = conversionTreeOne(sysCodeItem);

                //利用递归层级加载子节点
                Collection<TreeNode> childCodeItemList = loadChildCodeItemList(itemList, treeModel.getCode());
                treeModel.setChildren(childCodeItemList);
                childTree.add(treeModel);
            }
        }
        long j = System.currentTimeMillis();
        logger.debug("构建树形耗时---" + (j - i) + "-毫秒");
        return childTree;
    }


    @Override
    public List<DefaultTreeNode> conversionTree(List<SysCodeItem> list) {
        List<DefaultTreeNode> treeList = new ArrayList<DefaultTreeNode>();
        for (SysCodeItem sysCodeItem : list) {
            DefaultTreeNode tree = conversionTreeOne(sysCodeItem);
            treeList.add(tree);
        }
        return treeList;
    }


    private DefaultTreeNode conversionTreeOne(SysCodeItem sysCodeItem) {
        DefaultTreeNode treeModel = new DefaultTreeNode();
        treeModel.setChecked(false);
        treeModel.setId(sysCodeItem.getId());
        treeModel.setCode(sysCodeItem.getCode());
        treeModel.setParentId(sysCodeItem.getParentCode());
        treeModel.setExpanded(false);
//        treeModel.setLeaf(sysCodeItem.getLeaf());
        treeModel.setText(sysCodeItem.getName());
        return treeModel;
    }


    @Override
    @Transactional(readOnly = true)
    public Collection<SysInfoSet> getInfoSet() {

        return sheetComDao.getInfoSet();
    }


    @Override
    @Transactional(readOnly = true)
    public Collection<SysInfoItem> getByInfoSetId(Integer infoSetId) {

        return sheetComDao.getByInfoSetId(infoSetId);
    }


    @Override
    @Transactional
    public SheetData mergeEachOption(SheetDesignDataSource dataSource, SheetDesignField designField,
                                     Collection<SheetDesignCondition> conditions, int row, int column, int betweenRow, int betweenColumn, JSONArray paramArray) throws BusinessException {
        Map mapformat= new LinkedHashMap();
        String querySql = getQuerySql(dataSource, designField, conditions, paramArray,mapformat);
        long a = System.currentTimeMillis();
        Object obj = null;
		try {
			obj = sheetComDao.getValueBySql(querySql.toString());
		} catch (Exception e) {
			logger.error("错误SQL："+querySql);
			logger.error("TODO:异常描述",e);
			throw new BusinessException(OpsErrorMessage.MODULE_NAME,OpsErrorMessage.ERROR_MESSAGE,"错误SQL："+querySql);
		}
        long b = System.currentTimeMillis();
        
        SheetData sheetData = new SheetData();
        if (b-a > 60000) {
        	logger.info("耗时SQL："+querySql);
            logger.info("行列号："+row+"--"+column);
            logger.info("查询耗时："+(b-a));
            //return sheetData;
        }
        sheetData.setRowNo(row);
        sheetData.setColumnNo(column);
        sheetData.setCtrlRowNo(row + betweenRow - 1);
        sheetData.setCtrlColumnNo(column + betweenColumn - 1);
        sheetData.setStringValue(obj == null ? "" : obj.toString());

        return sheetData;
    }
    
    @Override
    @Transactional
    public SheetData queryDataBySql(String querySql,int row, int column, int betweenRow, int betweenColumn) throws BusinessException {
        
        long a = System.currentTimeMillis();
        Object obj = null;
		try {
			obj = sheetComDao.getValueBySql(querySql.toString());
		} catch (Exception e) {
			String realDataSource = DynamicDataSourceContextHolder.getRealDataSource();
			logger.error("数据源："+realDataSource);
			logger.error("错误SQL："+querySql);
			logger.error("TODO:异常描述",e);
			throw new BusinessException(OpsErrorMessage.MODULE_NAME,OpsErrorMessage.ERROR_MESSAGE,"数据源："+realDataSource+"错误SQL："+querySql);
		}
        long b = System.currentTimeMillis();
        
        SheetData sheetData = new SheetData();
        if (b-a > 60000) {
        	logger.info("耗时SQL："+querySql);
            logger.info("行列号："+row+"--"+column);
            logger.info("查询耗时："+(b-a));
            //return sheetData;
        }
        sheetData.setRowNo(row);
        sheetData.setColumnNo(column);
        sheetData.setCtrlRowNo(row + betweenRow - 1);
        sheetData.setCtrlColumnNo(column + betweenColumn - 1);
        sheetData.setStringValue(obj == null ? "" : obj.toString());

        return sheetData;
    }


    @Override
    @Transactional
    public List<SheetData> mergeListData(SheetDesignSection designSection, SheetDesignDataSource dataSource, SheetDesignField designField,
                                         Collection<SheetDesignCondition> conditions, JSONArray paramArray) throws BusinessException,Exception {
        List<SheetData> sheetDatas = new ArrayList<>();
        try
        {
            int ExpandDirection = designSection.getExpandDirection();
            int CtrlRowStart = designSection.getCtrlRowStart();
//            int CtrlRowEnd = designSection.getCtrlRowEnd();
            int CtrlColumnStart = designSection.getCtrlColumnStart();
//            int CtrlColumnEnd = designSection.getCtrlColumnEnd();

            int StartRowNo = designSection.getStartRowNo();
//            int EndRowNo = designSection.getEndRowNo();
            int StartColumnNo = designSection.getStartColumnNo();
//            int EndColumnNo = designSection.getEndColumnNo();

            Map mapformat= new LinkedHashMap();
            String querySql = getQuerySql(dataSource, designField, conditions, paramArray,mapformat);

            List<Map> lstmap = sheetComDao.getListBySql(querySql.toString());

            if (ExpandDirection == 1) {
                int row = StartRowNo;
                int ctrlrow = CtrlRowStart-1;
                for (Map map : lstmap) {
                    int column = StartColumnNo;
                    int ctrlcolumn = CtrlColumnStart-1;
                    int i=0;
                    for (Object ob : mapformat.keySet()) {
                        SheetData sheetData = new SheetData();
                        sheetData.setRowNo(row);
                        sheetData.setColumnNo(column++);
                        sheetData.setCtrlRowNo(ctrlrow);
                        sheetData.setCtrlColumnNo(ctrlcolumn++);
                        String strformat = mapformat.get(ob).toString();
                        if(strformat.startsWith("dateformat"))
                        {
                            if(map.get(ob)==null)
                            {
                                sheetData.setStringValue("");
                            }
                            else
                            {
                                SimpleDateFormat dateFormatformat = new SimpleDateFormat(strformat.replace("dateformat:",""));
                                SimpleDateFormat dateFormatformat1 = new SimpleDateFormat("yyyy-MM-dd");
                                sheetData.setStringValue(dateFormatformat.format(dateFormatformat1.parse(map.get(ob).toString().split(" ")[0])));
                            }
                        }
                        else if(strformat.startsWith("numformat"))
                        {
                            if(map.get(ob)==null)
                            {
                                sheetData.setStringValue("");
                            }
                            else
                            {
                                sheetData.setStringValue(new DecimalFormat(strformat.replace("numformat:","")).format(map.get(ob)));
                            }
                        }
                        else if(strformat.startsWith("code"))
                        {
                            if(map.get(ob)==null)
                            {
                                sheetData.setStringValue("");
                            }
                            else
                            {
                                if(strformat.startsWith("code1"))
                                {
                                    String getNameEx="";
                                    for(String temp : map.get(ob).toString().split(","))
                                    {
                                        SysCodeSet sysCodeSet = sysCodeSetService.getByName(strformat.replace("code1:",""));
                                        getNameEx += sysCodeItemService.getNameEx(sysCodeSet.getId(), temp)+",";
                                    }
                                    sheetData.setStringValue(getNameEx.substring(0,getNameEx.length()-1));
                                }
                                else
                                {
                                    String getName="";
                                    for(String temp : map.get(ob).toString().split(",")) {
                                        SysCodeSet sysCodeSet = sysCodeSetService.getByName(strformat.replace("code0:", ""));
                                        getName += sysCodeItemService.getDescription(sysCodeSet.getId(), temp)+",";
                                    }
                                    sheetData.setStringValue(getName.substring(0,getName.length()-1));
                                }

                            }
                        }
                        else
                        {
                            sheetData.setStringValue(map.get(ob)==null?"":map.get(ob).toString());
                        }
                        sheetDatas.add(sheetData);
                    }
                    row++;
                    ctrlrow++;
                }
            } else {
                int column = StartColumnNo;
                int ctrlcolumn = CtrlColumnStart-1;
                for (Map map : lstmap) {
                    int row = StartRowNo;
                    int ctrlrow = CtrlRowStart-1;
                    for (Object ob : map.keySet()) {
                        SheetData sheetData = new SheetData();
                        sheetData.setRowNo(row++);
                        sheetData.setColumnNo(column);
                        sheetData.setCtrlRowNo(ctrlrow++);
                        sheetData.setCtrlColumnNo(ctrlcolumn);
                        sheetData.setStringValue(map.get(ob).toString());
                        sheetDatas.add(sheetData);
                    }
                    column++;
                    ctrlcolumn++;
                }
            }
        }
        catch (BusinessException e)
        {
            throw e;
        }
        catch (Exception ex)
        {
            throw ex;
        }
        return sheetDatas;
    }

    @Override
    @Transactional
    public List<HashMap<String, Object>> getItemTreeByDataPosition(String designId,int rowNo ,int columnNo)
    {
        Collection<SysInfoItem> infoItems = new ArrayList<>();
        List<SheetDesignCondition> lst = sheetDesignConditionService.getByDesignId(UUID.fromString(designId));
        Collection<SheetDesignCondition> sheetDesignConditions= getDesignConditions(lst, rowNo, columnNo);
        Collection<SheetDesignField> designFields = sheetDesignFieldService.getByDesignId(UUID.fromString(designId));

        SheetDesignField designField = getUniqueDesignField(designFields,rowNo,columnNo);

        List<Integer> ids= new ArrayList();
        if(designField.getJsonData()!=null)
        {
            JSONArray ja= JSON.parseArray(designField.getJsonData());
            for(int i=0;i<ja.size();i++)
            {
                JSONObject json=(JSONObject) ja.get(i);
                SysInfoItem sysInfoItem = sysInfoItemService.getByColumnName(json.get("referenceTable").toString(), json.get("referenceValueColumn").toString());

            }
        }
        else
        {
            SysInfoItem sysInfoItem = sysInfoItemService.getByColumnName(designField.getReferenceTable().toUpperCase(), designField.getReferenceValueColumn());
            if (!ids.contains(sysInfoItem.getId())) ids.add(sysInfoItem.getId());
        }

        for (SheetDesignCondition entity : sheetDesignConditions) {
            for(SheetConditionItem sheetConditionItem : sheetConditionItemService.GetDataByConditionID(entity.getConditionId()))
            {
                ids = setInfoItems(infoItems,JSONObject.parseObject(sheetConditionItem.getJsonData()),ids);
            }
        }

        List<SheetDefitem> sheetDefitems = sheetDefitemService.findAll();

        Collection<SysInfoItem> sysInfoItems = sysInfoItemService.getByIds(ids);
        for(SysInfoItem sysInfoItem : sysInfoItems)
        {
            for (SheetDefitem sheetDefitem :sheetDefitems)
            {
                if(sheetDefitem.getInfoSetId().equals(sysInfoItem.getInfoSetId()))
                {
                    SysInfoItem tmp = sysInfoItemService.getById(sheetDefitem.getInfoItemId());
                    if(!infoItems.contains(tmp)) infoItems.add(tmp);
                }
            }
            if(!infoItems.contains(sysInfoItem) && !sysInfoItem.getInputType().equals("hidden")) infoItems.add(sysInfoItem);
        }

        List<HashMap<String, Object>> root = new ArrayList<HashMap<String, Object>>();

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("treeid","XX");
        map.put("text", "条件项");
        map.put("expanded", false);
        map.put("leaf", false);
        map.put("checked", false);
        List<HashMap<String, Object>> children1 = new ArrayList<HashMap<String, Object>>();

        for(SysInfoItem infoItem: infoItems)
        {
            SysInfoSet sysInfoSet = sysInfoSetService.getById(infoItem.getInfoSetId());
            HashMap<String, Object> map1 = new HashMap<String, Object>();
            map1.put("treeid", infoItem.getId());
            map1.put("tablename", sysInfoSet.getTableName());
            map1.put("columnname", infoItem.getColumnName());
            map1.put("text", infoItem.getNameCn());
            map1.put("expanded", false);
            map1.put("leaf", true);
            map1.put("xtype", infoItem.getInputType());
            map1.put("jdbctype", infoItem.getJdbcType());
            map1.put("checked", false);
            if ("combox".equals(infoItem.getInputType())) {
                map1.put("codeType", infoItem.getReferenceCodeSet());
            } else if ("code".equals(infoItem.getInputType())) {
                map1.put("codeType", infoItem.getReferenceCodeSet());
            } else if ("comboxtree".equals(infoItem.getInputType())) {
                map1.put("codeType", infoItem.getReferenceCodeSet());
            }
            children1.add(map1);
        }
        map.put("children", children1);
        root.add(map);

        return root;
    }
    
    @Override
    @Transactional
    public List<HashMap<String, Object>> getItemTreeBySectionNo(String designId,String sectionNo)
    {
        Collection<SysInfoItem> infoItems = new ArrayList<>();
        List<SheetDesignCondition> lst = sheetDesignConditionService.getByDesignId(UUID.fromString(designId));
        Collection<SheetDesignCondition> sheetDesignConditions= getDesignConditions(lst,sectionNo);

        SheetDesignField designField = sheetDesignFieldService.getBingData(UUID.fromString(designId), sectionNo);

        List<Integer> ids= new ArrayList();
        if(designField.getJsonData()!=null)
        {
            JSONArray ja= JSON.parseArray(designField.getJsonData());
            for(int i=0;i<ja.size();i++)
            {
                JSONObject json=(JSONObject) ja.get(i);
                SysInfoItem sysInfoItem = sysInfoItemService.getByColumnName(json.get("referenceTable").toString(), json.get("referenceValueColumn").toString());

            }
        }
        else
        {
            SysInfoItem sysInfoItem = sysInfoItemService.getByColumnName(designField.getReferenceTable().toUpperCase(), designField.getReferenceValueColumn());
            if (!ids.contains(sysInfoItem.getId())) ids.add(sysInfoItem.getId());
        }

        for (SheetDesignCondition entity : sheetDesignConditions) {
            for(SheetConditionItem sheetConditionItem : sheetConditionItemService.GetDataByConditionID(entity.getConditionId()))
            {
                ids = setInfoItems(infoItems,JSONObject.parseObject(sheetConditionItem.getJsonData()),ids);
            }
        }

        List<SheetDefitem> sheetDefitems = sheetDefitemService.findAll();

        Collection<SysInfoItem> sysInfoItems = sysInfoItemService.getByIds(ids);
        for(SysInfoItem sysInfoItem : sysInfoItems)
        {
            for (SheetDefitem sheetDefitem :sheetDefitems)
            {
                if(sheetDefitem.getInfoSetId()==sysInfoItem.getInfoSetId())
                {
                    SysInfoItem tmp = sysInfoItemService.getById(sheetDefitem.getInfoItemId());
                    if(!infoItems.contains(tmp)) infoItems.add(tmp);
                }
            }
            if(!infoItems.contains(sysInfoItem)) infoItems.add(sysInfoItem);
        }

        List<HashMap<String, Object>> root = new ArrayList<HashMap<String, Object>>();

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("treeid","XX");
        map.put("text", "条件项");
        map.put("expanded", false);
        map.put("leaf", false);
        map.put("checked", false);
        List<HashMap<String, Object>> children1 = new ArrayList<HashMap<String, Object>>();

        for(SysInfoItem infoItem: infoItems)
        {
            SysInfoSet sysInfoSet = sysInfoSetService.getById(infoItem.getInfoSetId());
            HashMap<String, Object> map1 = new HashMap<String, Object>();
            map1.put("treeid", infoItem.getId());
            map1.put("tablename", sysInfoSet.getTableName());
            map1.put("columnname", infoItem.getColumnName());
            map1.put("text", infoItem.getNameCn());
            map1.put("expanded", false);
            map1.put("leaf", true);
            map1.put("xtype", infoItem.getInputType());
            map1.put("jdbctype", infoItem.getJdbcType());
            map1.put("checked", false);
            if ("combox".equals(infoItem.getInputType())) {
                map1.put("codeType", infoItem.getReferenceCodeSet());
            } else if ("code".equals(infoItem.getInputType())) {
                map1.put("codeType", infoItem.getReferenceCodeSet());
            } else if ("comboxtree".equals(infoItem.getInputType())) {
                map1.put("codeType", infoItem.getReferenceCodeSet());
            }
            children1.add(map1);
        }
        map.put("children", children1);
        root.add(map);

        return root;
    }


    private List setInfoItems(Collection<SysInfoItem> infoItems,JSONObject json,List ids) {
        String itemIds="";
        String conditiontype = json.getString("conditionType");
        String InputType = json.getString("inputType");
        if(InputType.equals("SetItem"))
        {
            itemIds+=json.getString("informationSetId")+",";
        }
        else
        {
            if(json.containsKey("paramId1"))
            {
                itemIds+=json.getString("paramId1")+",";
            }
            if(json.containsKey("paramId2"))
            {
                itemIds+=json.getString("paramId2")+",";
            }
            if(json.containsKey("paramId3"))
            {
                itemIds+=json.getString("paramId3")+",";
            }
        }
        JSONObject jsonright1 = JSONObject.parseObject(json.get("rightJson1").toString());
        if(jsonright1.containsKey("paramId1"))
        {
            itemIds+=jsonright1.getString("paramId1")+",";
        }
        if(jsonright1.containsKey("paramId2"))
        {
            itemIds+=jsonright1.getString("paramId2")+",";
        }
        if(jsonright1.containsKey("paramId3"))
        {
            itemIds+=jsonright1.getString("paramId3")+",";
        }

        if ("BETWEEN".equals(conditiontype)) {
            JSONObject jsonright2 = JSONObject.parseObject(json.get("rightJson2").toString());
            if(jsonright2.containsKey("paramId1"))
            {
                itemIds+=jsonright2.getString("paramId1")+",";
            }
            if(jsonright2.containsKey("paramId2"))
            {
                itemIds+=jsonright2.getString("paramId2")+",";
            }
            if(jsonright2.containsKey("paramId3"))
            {
                itemIds+=jsonright2.getString("paramId3")+",";
            }
        }
        for (String temp : itemIds.split(",")) {
            if (!temp.isEmpty() && !ids.contains(Integer.parseInt(temp))) {
                ids.add(Integer.parseInt(temp));
            }
        }
        return ids;
    }

    @Transactional(readOnly=true)
    private String getQuerySql(SheetDesignDataSource dataSource,SheetDesignField designField, String[] fields, Collection<SheetDesignCondition> conditions, JSONArray paramArray) throws BusinessException {
        String sql = "";
        if (dataSource == null) {
            sql = getDataSource(designField, conditions);
        } else {
            sql = dataSource.getSql();
        }
        String fieldsSql = getFieldsSql(fields);
        String conditionSql = getConditionSql(conditions);
        sql = sql.replace("*", fieldsSql);

        //构建返回sql
        String querySql = sql + conditionSql;
        if (querySql.contains("@")) {
            Iterator<Object> it = paramArray.iterator();
            while (it.hasNext()) {
                String item = it.next().toString();
                JSONObject js = (JSONObject) JSON.parse(item);
                if (js.get("value") == null) continue;
                querySql = querySql.replace("@" + js.getString("name") + "@", js.getString("value"));
            }
        }

        return querySql;
    }

    @Override
    @Transactional(readOnly=true)
    public String getQuerySql(SheetDesignDataSource dataSource, SheetDesignField designField, Collection<SheetDesignCondition> conditions, JSONArray paramArray,Map mapformat) throws BusinessException {

    	//如果数据项函数为其他默认SQL
    	if("3".equals(designField.getFunctionName())){
    		return "SELECT DISTINCT 0 FROM SYS_PARAMETER";
    	}
    	
        String sql = "";

        if (dataSource == null) {
            sql = getDataSource(designField, conditions);
        } else {
            sql = dataSource.getSql();
        }

        String fieldSql = getFieldSql(designField,mapformat);

        String conditionSql = getConditionSql(conditions);
        sql = sql.replace("*", fieldSql);

        //构建返回sql
        String querySql = sql + conditionSql;
        if (querySql.contains("@")) {
            Iterator<Object> it = paramArray.iterator();
            while (it.hasNext()) {
                String item = it.next().toString();
                JSONObject js = (JSONObject) JSON.parse(item);
                if (js.get("value") == null) continue;
                querySql = querySql.replace("@" + js.getString("name") + "@", js.getString("value"));
            }
        }

        return querySql;
    }

    @Override
    @Transactional(readOnly=true)
    public Object getValueBySql(String querySql) {
        return sheetComDao.getValueBySql(querySql.toString());
    }

    @Transactional(readOnly=true)
    private String getDataSource(SheetDesignField designField, Collection<SheetDesignCondition> conditions) throws BusinessException {
        try {
            String strSql = "select * from ";
            String tableIds = "";
            for (SheetDesignCondition entity : conditions) {
                SheetCondition sheetCondition = sheetConditionService.getById(entity.getConditionId());
                if (sheetCondition != null && sheetCondition.getDescription() != null) {
                    tableIds += sheetCondition.getDescription();
                }
            }

            Collection<Integer> SetIds = new ArrayList<>();
            for (String temp : tableIds.split(",")) {
                if (!temp.isEmpty() && !SetIds.contains(Integer.parseInt(temp))) {
                    SetIds.add(Integer.parseInt(temp));
                }
            }

            if (designField != null) {

                if(designField.getJsonData()!=null)
                {
                    JSONArray ja= JSON.parseArray(designField.getJsonData());
                    for(int i=0;i<ja.size();i++)
                    {
                        JSONObject json=(JSONObject) ja.get(i);
                        if(json.get("functionName").equals("max") || json.get("functionName").equals("min") || json.get("functionName").equals("concat"))
                        {
                            continue;
                        }
                        SysInfoItem sysInfoItem = sysInfoItemService.getByColumnName(json.get("referenceTable").toString(), json.get("referenceValueColumn").toString());
                        if (!SetIds.contains(sysInfoItem.getInfoSetId())) SetIds.add(sysInfoItem.getInfoSetId());
                    }
                }
                else
                {
                    SysInfoItem sysInfoItem = sysInfoItemService.getByColumnName(designField.getReferenceTable().toUpperCase(), designField.getReferenceValueColumn());
                    if (!SetIds.contains(sysInfoItem.getInfoSetId())) SetIds.add(sysInfoItem.getInfoSetId());
                }
            }

            Collection<SysInfoItem> sysInfoItemsWithSetId = new ArrayList<>();
            for (Integer setid : SetIds) {
                sysInfoItemsWithSetId.addAll(sysInfoItemService.getBySetId(setid));
            }

            Collection<SysInfoItem> sysInfoItemsRefTable = sysInfoItemsWithSetId.stream().filter(item -> item.getReferenceTable() != null).collect(Collectors.toList());

            String strTables = "";
            String strJoins = "";
            for (Integer setid : SetIds) {
                SysInfoSet sysInfoSet = sysInfoSetService.getById(setid);
                if (sysInfoSet != null && !strTables.contains(sysInfoSet.getTableName())) {
                    strTables += sysInfoSet.getTableName() + ",";
                    strJoins += " and " + sysInfoSet.getTableName() + ".status=0 ";  //暂时所有表都加这个条件
                }

            }


            for (SysInfoItem temp : sysInfoItemsRefTable) {
                if (!strTables.toUpperCase().contains(temp.getReferenceTable().toUpperCase())) continue;
                ;
                SysInfoSet sysInfoSet = sysInfoSetService.getById(temp.getInfoSetId());
                strJoins += " and " + sysInfoSet.getTableName() + "." + temp.getColumnName() + "=" + temp.getReferenceTable() + "." + temp.getReferenceColumn();
            }

            strTables = strTables.substring(0, strTables.length() - 1);
            strSql = strSql + strTables + " where 1=1" + strJoins;
            return strSql;
        } catch (Exception e) {
        	logger.error("数据源构建失败",e);
            throw new BusinessException(OpsErrorMessage.MODULE_NAME,OpsErrorMessage.ERROR_MESSAGE,"数据源构建失败");
        }
    }

    private String getConditionSql(Collection<SheetDesignCondition> conditions) {
        StringBuilder sql = new StringBuilder();
        if (!CollectionUtils.isEmpty(conditions)) {

            for (SheetDesignCondition sheetDesignCondition : conditions) {
                sql.append(" and (").append(sheetDesignCondition.getSql()).append(") ");
            }

        }
        return sql.toString();
    }

    @Transactional(readOnly=true)
    private String getFieldSql(SheetDesignField designField,Map mapformat) throws BusinessException {
        try {
            String field = "";
            if(designField.getJsonData()!=null)
            {
                JSONArray ja= JSON.parseArray(designField.getJsonData());
                for(int i=0;i<ja.size();i++)
                {
                    JSONObject json = (JSONObject) ja.get(i);
                    if(json.containsKey("formatStr") && json.get("formatStr")!=null)
                    {
                        //可尝试sql查询和后台转换效率问题
                        if(json.get("jdbctype").equals("TIMESTAMP"))
                        {
//                            field+="to_char(" + json.get("referenceTable")+ "." + json.get("referenceValueColumn") + ",'" +  json.get("formatStr")+"'),";
                            mapformat.put(json.get("referenceValueColumn"),"dateformat:"+json.get("formatStr"));
                        }
                        if(json.get("jdbctype").equals("DECIMAL"))
                        {
//                            field+="to_char(" + json.get("referenceTable")+ "." + json.get("referenceValueColumn") + ",'" +  json.get("formatStr")+"'),";
                            mapformat.put(json.get("referenceValueColumn"),"numformat:"+json.get("formatStr"));
                        }
                        if(json.containsKey("codeType"))
                        {
                            if(json.get("formatStr").equals("1"))
                            {
                                mapformat.put(json.get("referenceValueColumn"),"code1:"+ json.get("codeType"));
                            }
                            else
                            {
                                mapformat.put(json.get("referenceValueColumn"),"code0:"+ json.get("codeType"));
                            }
                        }
                    }
                    else if(json.containsKey("codeType"))
                    {
//                        field+="(select sys_code_item.name from sys_code_set left join sys_code_item on sys_code_set.id= sys_code_item.code_set_id where sys_code_set.name ='" + json.get("codeType") + "' and sys_code_item.code= " + json.get("referenceTable")+ "." + json.get("referenceValueColumn") + ") " + json.get("referenceValueColumn") + ",";
                        mapformat.put(json.get("referenceValueColumn"),"code0:"+ json.get("codeType"));
                    }
                    else
                    {
                        mapformat.put(json.get("referenceValueColumn"),"");
                    }
                    if(json.get("functionName").equals("max") || json.get("functionName").equals("min") || json.get("functionName").equals("concat"))
                    {
                        SysInfoItem column= sysInfoItemService.getByColumnName(json.get("referenceTable").toString(),json.get("referenceValueColumn").toString());
                        Collection<SysInfoItem> sysInfoItems=sysInfoItemService.getBySetId(column.getInfoSetId());

                        List<SysInfoItem> sysInfoItemsRefTable = sysInfoItems.stream().filter(item -> item.getReferenceTable() != null).collect(Collectors.toList());
                        SysInfoItem sysInfoItem = sysInfoItemsRefTable.get(0);

                        String function =json.get("functionName").toString();
                        if(json.get("functionName").equals("concat"))
                        {
                            function="wm_concat";
                        }

                        field+=String.format("(select %s(%s) from %s where %s.%s=%s.%s) %s,",function,json.get("referenceValueColumn").toString(),json.get("referenceTable").toString(),json.get("referenceTable").toString(),sysInfoItem.getColumnName(),sysInfoItem.getReferenceTable(),sysInfoItem.getReferenceColumn(),json.get("referenceValueColumn").toString());
                    }
                    else
                    {
                        field+=json.get("referenceTable")+ "." +json.get("referenceValueColumn")+",";
                    }

                }
                field=field.substring(0,field.length()-1);
            }
            else
            {
                switch (designField.getFunctionName()) {
                    case SUM:
                        field = String.format("sum( %s.%s)",designField.getReferenceTable(),designField.getReferenceValueColumn());
                        break;
                    case COUNT:
                        field = String.format("count(distinct %s.%s)",designField.getReferenceTable(),designField.getReferenceValueColumn());
                        break;
                    case VALUE:
                        field = String.format("distinct %s.%s",designField.getReferenceTable(),designField.getReferenceValueColumn());
                        break;
                    case OTHER:
                        //TODO 暂时不考虑
                        field = "1";
                        break;
                    case "3":
                    	field = "distinct 1";
                    default:
                        break;
                }
            }
            return field;
        } catch (Exception e) {
            throw new BusinessException(OpsErrorMessage.MODULE_NAME,OpsErrorMessage.ERROR_MESSAGE,"构建失败,请检查是否绑定了数据项");
        }
    }


    @Override
    public SheetDesignDataSource getUniqueDataSource(Collection<SheetDesignDataSource> designDataSources, int row,int column) {
        List<SheetDesignDataSource> dataSources = new ArrayList<>();
        for (SheetDesignDataSource sheetDesignDataSource : designDataSources) {
            boolean flag = matchingRange(sheetDesignDataSource.getStartRowNo(), sheetDesignDataSource.getEndRowNo(),
                    sheetDesignDataSource.getStartColumnNo(), sheetDesignDataSource.getEndColumnNo(), row, column);
            if (flag) {
                dataSources.add(sheetDesignDataSource);
            }
        }
        Assert.isTrue(dataSources.size() < 2, "数据源绑定错误，对同一区域重复绑定");
        if (!CollectionUtils.isEmpty(dataSources)) {
            return dataSources.get(0);
        }
        return null;
    }

    @Override
    public SheetDesignDataSource getUniqueDataSource(Collection<SheetDesignDataSource> designDataSources,
                                                     String sectionNo) {
        List<SheetDesignDataSource> dataSources = new ArrayList<>();
        for (SheetDesignDataSource dataSource : designDataSources) {
            if (StringUtils.equals(sectionNo, dataSource.getSectionNo())) {
                dataSources.add(dataSource);
            }
        }
        Assert.isTrue(dataSources.size() < 2, "数据源绑定错误，对同一区域重复绑定");
        if (!CollectionUtils.isEmpty(dataSources)) {
            return dataSources.get(0);
        }
        return null;
    }


    private boolean matchingRange(Integer startRowNo, Integer endRowNo, Integer startColumnNo, Integer endColumnNo,
                                  int row, int column) {
//		if(((row>=startRowNo && row <= endRowNo) || startRowNo == -1)&&
//				((column>=startColumnNo && column <= endColumnNo) || startColumnNo == -1)){
//			
//		}
        boolean flag = false;
        if (startRowNo == null || endRowNo == null ||
                startColumnNo == null || endColumnNo == null) {
            return false;
        }
        if (((row >= startRowNo && row <= endRowNo)) &&
                ((column >= startColumnNo && column <= endColumnNo))) {
            flag = true;
        } else if (startRowNo == -1 && endRowNo == -1 && (column >= startColumnNo && column <= endColumnNo)) {
            flag = true;
        } else if (startColumnNo == -1 && endColumnNo == -1 && (row >= startRowNo && row <= endRowNo)) {
            flag = true;
        } else if (startRowNo == -1 && endRowNo == -1 && startColumnNo == -1 && endColumnNo == -1) {
            flag = true;
        }
        return flag;
    }


    @Override
    @Transactional(readOnly=true)
    public SheetDesignField getUniqueDesignField(Collection<SheetDesignField> designFields, int row, int column) {
        List<SheetDesignField> fields = new ArrayList<>();
        for (SheetDesignField designField : designFields) {
        	Collection<SheetDesignFieldBinding> collection = sheetDesignFieldBindingService.getByFieldId(designField.getId());
        	for (SheetDesignFieldBinding sheetDesignFieldBinding : collection) {
        		boolean flag = matchingRange(sheetDesignFieldBinding.getStartRowNo(), sheetDesignFieldBinding.getEndRowNo(),
        				sheetDesignFieldBinding.getStartColumnNo(), sheetDesignFieldBinding.getEndColumnNo(), row, column);
        		if (flag) {
        			if(!fields.contains(designField)){
        				fields.add(designField);
        			}
        			
        		}
			}
        }
        Assert.isTrue(fields.size() < 2, "数据项绑定错误，对同一区域重复绑定");
        if (!CollectionUtils.isEmpty(fields)) {
            return fields.get(0);
        }
        return null;
    }

    @Override
    public SheetDesignField getUniqueDesignField(Collection<SheetDesignField> designFields, String sectionNo) {
        List<SheetDesignField> fields = new ArrayList<>();
        for (SheetDesignField designField : designFields) {
            if (StringUtils.equals(sectionNo, designField.getSectionNo())) {
                fields.add(designField);
            }
        }
        Assert.isTrue(fields.size() < 2, "数据项绑定错误，对同一区域重复绑定");
        if (!CollectionUtils.isEmpty(fields)) {
            return fields.get(0);
        }
        return null;
    }


    @Override
    public Collection<SheetDesignCondition> getDesignConditions(List<SheetDesignCondition> designConditions, int row,
                                                                int column) {
        List<SheetDesignCondition> conditions = new ArrayList<>();
        for (SheetDesignCondition designCondition : designConditions) {
            boolean flag = matchingRange(designCondition.getStartRowNo(), designCondition.getEndRowNo(),
                    designCondition.getStartColumnNo(), designCondition.getEndColumnNo(), row, column);
            if (flag) {
                conditions.add(designCondition);
            }
        }
        return conditions;
    }

    @Override
    public Collection<SheetDesignCondition> getDesignConditions(List<SheetDesignCondition> designConditions,
                                                                String sectionNo) {
        List<SheetDesignCondition> conditions = new ArrayList<>();
        for (SheetDesignCondition condition : designConditions) {
            if (StringUtils.equals(sectionNo, condition.getSectionNo())||"-1".equals(condition.getSectionNo())) {
                conditions.add(condition);
            }
        }
        return conditions;
    }


    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getSqlDescription(Collection<SheetDesignDataSource> designDataSources,
                                                 Collection<SheetDesignField> designFields, List<SheetDesignCondition> designConditions, Integer rowNo,
                                                 Integer columnNo, JSONArray paramArray) throws BusinessException {
        Map<String, Object> sqlDesc = new HashMap<String, Object>();
        SheetDesignDataSource dataSource = getUniqueDataSource(designDataSources, rowNo, columnNo);
        SheetDesignField designField = getUniqueDesignField(designFields, rowNo, columnNo);
        Assert.notNull(designField, "该单元格没有绑定数据项");
        Collection<SheetDesignCondition> conditions = getDesignConditions(designConditions, rowNo, columnNo);
        Map mapformat= new LinkedHashMap();
        String querySql = getQuerySql(dataSource, designField, conditions, paramArray,mapformat);
        StringBuilder sqlDes = new StringBuilder();
        if (dataSource != null) {
            sqlDes.append("数据源：" + dataSource.getName() + "\n");
        }
        sqlDes.append("数据项： " + designField.getName() + "\n");

        int i = 1;
        for (SheetDesignCondition condition : conditions) {
        	String position = "(行："+condition.getStartRowNo()+" — "+condition.getEndRowNo()+
        			";列："+condition.getStartColumnNo()+" — "+condition.getEndColumnNo()+")";
            sqlDes.append("条件项" + i + ": " + condition.getConditionName());
            if(condition.getStartRowNo() == -1 && condition.getEndRowNo() == -1 && 
            		condition.getStartColumnNo() == -1 && condition.getEndColumnNo() == -1){
            	sqlDes.append(" (全表绑定) "+position);
            }else if(condition.getStartRowNo() == -1 && condition.getEndRowNo() == -1){
            	sqlDes.append(" (列绑定) "+position);
            }else if(condition.getStartColumnNo() == -1 && condition.getEndColumnNo() == -1){
            	sqlDes.append(" (行绑定) "+position);
            }else{
            	sqlDes.append(" (块绑定) "+position);
            }
            sqlDes.append("\n");
            i++;
        }

        sqlDes = sqlDes.delete(sqlDes.lastIndexOf("\n"), sqlDes.length());

        sqlDesc.put("querySql", querySql);

        sqlDesc.put("sqlDes", sqlDes.toString());

        return sqlDesc;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getSqlDescription(Collection<SheetDesignDataSource> designDataSources,
                                                  List<SheetDesignCondition> designConditions,
                                                 UUID designId,String sectionNo, JSONArray paramArray) throws BusinessException {
        Map<String, Object> sqlDesc = new HashMap<String, Object>();
        SheetDesignDataSource dataSource = getUniqueDataSource(designDataSources,sectionNo);
        SheetDesignField designField = sheetDesignFieldService.getBingData(designId, sectionNo);
        Assert.notNull(designField, "该单元格没有绑定数据项");
        Collection<SheetDesignCondition> conditions = getDesignConditions(designConditions,sectionNo);
        Map mapformat= new LinkedHashMap();
        String querySql = getQuerySql(dataSource, designField, conditions, paramArray,mapformat);
        StringBuilder sqlDes = new StringBuilder();
        if (dataSource != null) {
            sqlDes.append("数据源：" + dataSource.getName() + "\n");
        }
        sqlDes.append("数据项： " + designField.getName() + "\n");

        int i = 1;
        for (SheetDesignCondition condition : conditions) {
            sqlDes.append("条件项" + i + ": " + condition.getConditionName());
            sqlDes.append("\n");
            i++;
        }

        sqlDes = sqlDes.delete(sqlDes.lastIndexOf("\n"), sqlDes.length());

        sqlDesc.put("querySql", querySql);

        sqlDesc.put("sqlDes", sqlDes.toString());

        return sqlDesc;
    }


    @SuppressWarnings("unchecked")
    @Override
    public List<Map<String, Object>> formulaCaculation(List<SheetData> sheetDatas, Map<String,Integer> range, Collection<SheetDesignExpression> expressions) throws BusinessException {
        try {
            Map<String, Map<String, Object>> dataMap = new HashMap<>();
            List<Map<String, Object>> datas = new ArrayList<>();
            if(!CollectionUtils.isEmpty(sheetDatas)){
            	for (SheetData sheetData : sheetDatas) {
                    Map data = (Map) JSON.toJSON(sheetData);
                    dataMap.put("0-" + sheetData.getRowNo() + "-" + sheetData.getColumnNo(), data);
                    datas.add(data);
                }
                if(!CollectionUtils.isEmpty(expressions)){
                	//String formula = getFormulaCaculation(expressions);
                	for (SheetDesignExpression expression : expressions) {
                		try {
    						analyzeFormula(expression.getContent()+";", dataMap, range.get("startRowNo"), range.get("endRowNo"),
    						        range.get("startColumnNo"), range.get("endColumnNo"));
    					} catch (Exception e) {
    						logger.error("公式："+expression.getName()+" 有误，请检查！",e);
    						throw new BusinessException(OpsErrorMessage.MODULE_NAME,OpsErrorMessage.ERROR_MESSAGE,"公式："+expression.getName()+" 有误，请检查！");
    					}
    				}
                    
                }
            }
            
            return datas;
        }catch(BusinessException e){
        	logger.error(e.getMessage(),e);
        	throw new BusinessException(OpsErrorMessage.MODULE_NAME,OpsErrorMessage.ERROR_MESSAGE,e.getMessage());
        } catch (Exception e) {
            logger.error("公式运算失败", e);
            throw new BusinessException(OpsErrorMessage.MODULE_NAME,OpsErrorMessage.ERROR_MESSAGE,"公式运算失败");
        }
    }
    
    @SuppressWarnings("unchecked")
    @Override
    @Transactional(readOnly=true)
    public List<Map<String, Object>> formulaCaculation(String sheetId,String designId,UUID userId,List<SheetData> sheetDatas) throws BusinessException {
        try {
        	// 获取模板下所有计算表达式
        	Collection<SheetDesignExpression> expressions = sheetDesignExpressionService
        			.getCaculationFormulaByDesignId(UUID.fromString(designId));
        	List<Map<String, Object>> datas = new ArrayList<>();
        	if (!CollectionUtils.isEmpty(expressions)) {
    			// 把表号解析出来
    			Map<String, String> noMap = analyzeSheetNoByExpression(expressions);
    			
    			// 根据sheetId获取参数，并根据参数和表号获取需要校核的报表数据
    			Collection<SheetParameter> parameters = sheetParameterService
    					.getBySheetId(UUID.fromString(sheetId));
    			Map<String, Map<String, Object>> dataMap = new HashMap<>();
    			
    			if (!CollectionUtils.isEmpty(noMap.keySet())) {
    				// 根据sheetId获取参数，并根据参数和表号获取需要校核的报表数据
    				SheetSheetCategory sheetSheetCategory = sheetSheetCategoryService.getBySheetId(UUID.fromString(sheetId));
    				Collection<SheetInfo> sheetInfos = getSheetInfos(noMap.keySet(), parameters,sheetSheetCategory.getCategoryId());
    				
    				for (SheetInfo sheetInfo : sheetInfos) {
    					if (StringUtils.equals(sheetId, sheetInfo.getSheetId().toString())) {
							for(SheetData sheetData : sheetDatas){
								Map<String, Object> data = (Map<String, Object>) JSON.toJSON(sheetData);
    							data.put("sheetNo", sheetInfo.getSheetNo());
    							data.put("sheetName", sheetInfo.getSheetName());
    							dataMap.put("0-" + sheetData.getRowNo() + "-" + sheetData.getColumnNo(), data);
    							String sheetNo = "";
    							for (String no : noMap.keySet()) {
    								if(StringUtils.equals(sheetInfo.getSheetNo(), no.trim())){
    									dataMap.put(noMap.get(no) + "-" + sheetData.getRowNo() + "-" + sheetData.getColumnNo(), data);
    								}
    							}
    							datas.add(data);
							}
							
						}else{
							while(true){
								boolean flag = true;
								if(gatherSign.containsKey(userId.toString()+"_"+sheetInfo.getSheetId().toString())){
									flag = false;
								}
								
								if(flag){
									break;
								}else{
									logger.debug("表号："+sheetInfo.getSheetNo()+" 报表数据没有计算完成阻塞500毫秒");
									Thread.sleep(500);
								}
							}
							Collection<SheetData> infoDatas = sheetDataService.getBySheetId(sheetInfo.getSheetId());

	    					if(!CollectionUtils.isEmpty(infoDatas)){
	    						for (SheetData sheetData : infoDatas) {
	    							Map<String, Object> data = (Map<String, Object>) JSON.toJSON(sheetData);
	    							data.put("sheetNo", sheetInfo.getSheetNo());
	    							data.put("sheetName", sheetInfo.getSheetName());
	    							// key 表号-行号-列号 value 单元格map数据
	    							String sheetNo = "";
	    							for (String no : noMap.keySet()) {
	    								if(StringUtils.equals(sheetInfo.getSheetNo(), no.trim())){
	    									dataMap.put(noMap.get(no) + "-" + sheetData.getRowNo() + "-" + sheetData.getColumnNo(), data);
	    								}
	    							}
	    							
	    						}
	    					}
						}
    					
    				}
    			} else {
    				SheetDesign sheetDesign = sheetDesignService.getById(UUID.fromString(designId));
    				com.sinosoft.ops.cimp.entity.sheet.Sheet sheet = sheetService.getById(UUID.fromString(sheetId));
    				for (SheetData sheetData : sheetDatas) {
    					Map<String, Object> data = (Map<String, Object>) JSON.toJSON(sheetData);
    					data.put("sheetNo", sheetDesign.getSheetNo());
    					data.put("sheetName", sheet.getName());
    					dataMap.put("0-" + sheetData.getRowNo() + "-" + sheetData.getColumnNo(), data);
    					datas.add(data);
    				}
    			}
    			
    			Map<String, Integer> range = sheetDesignSectionService.getRangeByDesignId(UUID.fromString(designId));
    			
    			for (SheetDesignExpression expression : expressions) {
    				SheetEvalueateListener listener = null;
    				String regex = "T(\\s*[a-zA-Z0-9\\.]*\\s*)R|T(\\s*[a-zA-Z0-9\\.]*\\s*)C";
    				Pattern pattern = Pattern.compile(regex);
    				try {
    					
    					Matcher matcher = pattern.matcher(expression.getContent());
    					String content = expression.getContent();
    					while (matcher.find()) {
    						int ordinal;
    						if(matcher.group(1) == null){
    							ordinal = 2;
    							String group = matcher.group(ordinal);
        						String string = noMap.get(group);
        						content = content.replaceAll("T"+group+"C", "T"+string+"C");
    						}else{
    							ordinal = 1;
    							String group = matcher.group(ordinal);
        						String string = noMap.get(group);
        						content = content.replaceAll("T"+group+"R", "T"+string+"R");
    						}
    						
    					}
    					
    					listener = analyzeFormula(content + ";", dataMap,
    							range.get("startRowNo"), range.get("endRowNo"), range.get("startColumnNo"),
    							range.get("endColumnNo"), expression.getType());
    				} catch (Exception e) {
    					logger.error("公式：" + expression.getName() + " 有误，请检查！", e);
    					throw new BusinessException(OpsErrorMessage.MODULE_NAME,OpsErrorMessage.ERROR_MESSAGE,"公式：" + expression.getName() + " 有误，请检查！");
    				}
    			}
    		}else{
    			for(SheetData sheetData : sheetDatas){
					Map<String, Object> data = (Map<String, Object>) JSON.toJSON(sheetData);
					datas.add(data);
				}
    		}
            return datas;
        }catch(BusinessException e){
        	logger.error(e.getMessage(),e);
        	throw new BusinessException(OpsErrorMessage.MODULE_NAME,OpsErrorMessage.ERROR_MESSAGE,e.getMessage());
        } catch (Exception e) {
            logger.error("公式运算失败", e);
            throw new BusinessException(OpsErrorMessage.MODULE_NAME,OpsErrorMessage.ERROR_MESSAGE,"公式运算失败");
        }
    }
    
    @Override
    @Transactional
    public List<Map<String, Object>> formulaCheck(String sheetId,String designId,String formula) throws BusinessException{
    	List<Map<String, Object>> result = new ArrayList<>();
    	// 获取模板下所有校核表达式
    	Collection<SheetDesignExpression> expressions = sheetDesignExpressionService
    			.getCheckFormulaByDesignId(UUID.fromString(designId));
    	if (!CollectionUtils.isEmpty(expressions)) {
			// 把表号解析出来
			Map<String, String> noMap = analyzeSheetNoByExpression(expressions);
			
			// 根据sheetId获取参数，并根据参数和表号获取需要校核的报表数据
			Collection<SheetParameter> parameters = sheetParameterService
					.getBySheetId(UUID.fromString(sheetId));
			Map<String, Map<String, Object>> dataMap = new HashMap<>();
			if (!CollectionUtils.isEmpty(noMap.keySet())) {
				// 根据sheetId获取参数，并根据参数和表号获取需要校核的报表数据
				SheetSheetCategory sheetSheetCategory = sheetSheetCategoryService.getBySheetId(UUID.fromString(sheetId));
				Collection<SheetInfo> sheetInfos = getSheetInfos(noMap.keySet(), parameters,sheetSheetCategory.getCategoryId());
				dataMap = generateDataMap(sheetInfos, sheetId,noMap);
			} else {
				Collection<SheetData> datas = sheetDataService.getBySheetId(sheetId);
				SheetDesign sheetDesign = sheetDesignService.getById(UUID.fromString(designId));
                com.sinosoft.ops.cimp.entity.sheet.Sheet sheet = sheetService.getById(UUID.fromString(sheetId));
				for (SheetData sheetData : datas) {
					Map<String, Object> data = (Map<String, Object>) JSON.toJSON(sheetData);
					data.put("sheetNo", sheetDesign.getSheetNo());
					data.put("sheetName", sheet.getName());
					dataMap.put("0-" + sheetData.getRowNo() + "-" + sheetData.getColumnNo(), data);
				}
			}
			
			Map<String, Integer> range = sheetDesignSectionService.getRangeByDesignId(UUID.fromString(designId));
			
			for (SheetDesignExpression expression : expressions) {
				SheetEvalueateListener listener = null;
				String regex = "T(\\s*[a-zA-Z0-9\\.]*\\s*)R|T(\\s*[a-zA-Z0-9\\.]*\\s*)C";
				Pattern pattern = Pattern.compile(regex);
				try {
					
					Matcher matcher = pattern.matcher(expression.getContent());
					String content = expression.getContent();
					while (matcher.find()) {
						String group ;
						if(matcher.group(1) == null){
							group = matcher.group(2);
							String string = noMap.get(group);
							content = content.replaceAll("T"+group+"C", "T"+string+"C");
						}else{
							group = matcher.group(1);
							String string = noMap.get(group);
							content = content.replaceAll("T"+group+"R", "T"+string+"R");
						}
						
					}
					
					listener = analyzeFormula(content + ";", dataMap,
							range.get("startRowNo"), range.get("endRowNo"), range.get("startColumnNo"),
							range.get("endColumnNo"), expression.getType());
				} catch (Exception e) {
					logger.error("公式：" + expression.getName() + " 有误，请检查！", e);
					throw new BusinessException(OpsErrorMessage.MODULE_NAME,OpsErrorMessage.ERROR_MESSAGE,"公式：" + expression.getName() + " 有误，请检查！");
				}
				for (Map<String, Object> map : listener.getLogicResult()) {
					map.put("expressionId", expression.getId());
					String statement = (String)map.get("statement");
					Matcher matcher = pattern.matcher(statement);
					while(matcher.find()){
						String group;
						if(matcher.group(1) == null){
							group = matcher.group(2);
							for (String string : noMap.keySet()) {
								if(StringUtils.equals(noMap.get(string), group)){
									statement = statement.replaceAll("T"+group+"C", "T"+string+"C");
									map.put("statement",statement);
								}
							}
						}else{
							group = matcher.group(1);
							for (String string : noMap.keySet()) {
								if(StringUtils.equals(noMap.get(string), group)){
									statement = statement.replaceAll("T"+group+"R", "T"+string+"R");
									map.put("statement",statement);
								}
							}
						}
						
						
					}
				}
				result.addAll(listener.getLogicResult());
			}
			if(formula != null){
				return getFormulaCheckData(formula, result, dataMap,noMap);
			}
			
    	}
    	return result;
    }
    
    /**
	 * 构建表格数据map 格式：key：t-r-c value：data
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	private Map<String, Map<String, Object>> generateDataMap(Collection<SheetInfo> sheetInfos, String sheetId,Map<String, String> nos) {
		Map<String, Map<String, Object>> dataMap = new HashMap<>();

		for (SheetInfo sheetInfo : sheetInfos) {
			Collection<SheetData> sheetDatas = sheetDataService.getBySheetId(sheetInfo.getSheetId());

			if(!CollectionUtils.isEmpty(sheetDatas)){
				for (SheetData sheetData : sheetDatas) {
					Map<String, Object> data = (Map<String, Object>) JSON.toJSON(sheetData);
					data.put("sheetNo", sheetInfo.getSheetNo());
					data.put("sheetName", sheetInfo.getSheetName());
					// key 表号-行号-列号 value 单元格map数据
					String sheetNo = "";
					for (String no : nos.keySet()) {
						if(StringUtils.equals(sheetInfo.getSheetNo(), no.trim())){
							dataMap.put(nos.get(no) + "-" + sheetData.getRowNo() + "-" + sheetData.getColumnNo(), data);
						}
					}
					
					if (StringUtils.equals(sheetId, sheetInfo.getSheetId().toString())) {
						// 表内校核表号默认为0
						dataMap.put("0-" + sheetData.getRowNo() + "-" + sheetData.getColumnNo(), data);
					}
				}
			}
		}
		return dataMap;
	}
    
    /**
	 * 
	 * 根据表达式集合解析出表号
	 * 
	 * @param expressions
	 *            表达式集合
	 * @return 不重复的表号集合
	 */
	private Map<String, String> analyzeSheetNoByExpression(Collection<SheetDesignExpression> expressions) {
		Map<String, String> map = new HashMap<>();// 表号集合
		String regex = "T(\\s*[a-zA-Z0-9\\.]*\\s*)R|T(\\s*[a-zA-Z0-9\\.]*\\s*)C";
		int i = 1;
		for (SheetDesignExpression sheetDesignExpression : expressions) {
			String content = sheetDesignExpression.getContent();
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(content);
			while (matcher.find()) {
				String tableNo;
				if(matcher.group(1) == null){
					tableNo = matcher.group(2);
				}else{
					tableNo = matcher.group(1);
				}
				map.put(tableNo, i+"");
				i++;
			}
		}
		return map;
	}
	
	@Override
    @Transactional
    public Map<String, Object> getFormulaCaculationData(String sheetId,String designId,List<Map<String, Object>> sheetDatas,Integer rowNo, Integer columnNo) throws BusinessException {
		 //////////////////
		try {
        	// 获取模板下所有计算表达式
        	Collection<SheetDesignExpression> expressions = sheetDesignExpressionService
        			.getCaculationFormulaByDesignId(UUID.fromString(designId));
        	
        	if (!CollectionUtils.isEmpty(expressions)) {
    			// 把表号解析出来
    			Map<String, String> noMap = analyzeSheetNoByExpression(expressions);
    			
    			// 根据sheetId获取参数，并根据参数和表号获取需要校核的报表数据
    			Collection<SheetParameter> parameters = sheetParameterService
    					.getBySheetId(UUID.fromString(sheetId));
    			Map<String, Map<String, Object>> dataMap = new HashMap<>();
    			String sheetNo = "";
    			if (!CollectionUtils.isEmpty(noMap.keySet())) {
    				// 根据sheetId获取参数，并根据参数和表号获取需要校核的报表数据
    				SheetSheetCategory sheetSheetCategory = sheetSheetCategoryService.getBySheetId(UUID.fromString(sheetId));
    				Collection<SheetInfo> sheetInfos = getSheetInfos(noMap.keySet(), parameters,sheetSheetCategory.getCategoryId());
    				
    				
    				for (SheetInfo sheetInfo : sheetInfos) {
    					if(StringUtils.equals(sheetId, sheetInfo.getSheetId().toString())){
							sheetNo = sheetInfo.getSheetNo();
						}
						Collection<SheetData> infoDatas = sheetDataService.getBySheetId(sheetInfo.getSheetId());

    					if(!CollectionUtils.isEmpty(infoDatas)){
    						for (SheetData sheetData : infoDatas) {
    							Map<String, Object> data = (Map<String, Object>) JSON.toJSON(sheetData);
    							data.put("sheetNo", sheetInfo.getSheetNo());
    							data.put("sheetName", sheetInfo.getSheetName());
    							// key 表号-行号-列号 value 单元格map数据
    							if(StringUtils.equals(sheetId, sheetInfo.getSheetId().toString())){
    								dataMap.put("0-" + sheetData.getRowNo() + "-" + sheetData.getColumnNo(), data);
    							}
    							
    							for (String no : noMap.keySet()) {
    								if(StringUtils.equals(sheetInfo.getSheetNo(), no.trim())){
    									dataMap.put(noMap.get(no) + "-" + sheetData.getRowNo() + "-" + sheetData.getColumnNo(), data);
    								}
    							}
    							
    						}
    					}
					}
    					
    			} else {
    				SheetDesign sheetDesign = sheetDesignService.getById(UUID.fromString(designId));
                    com.sinosoft.ops.cimp.entity.sheet.Sheet sheet = sheetService.getById(UUID.fromString(sheetId));
    				for (Map<String, Object> sheetData : sheetDatas) {
    					Map<String, Object> data = (Map<String, Object>) JSON.toJSON(sheetData);
    					data.put("sheetNo", sheetDesign.getSheetNo());
    					data.put("sheetName", sheet.getName());
    					dataMap.put("0-" + sheetData.get("rowNo") + "-" + sheetData.get("columnNo"), data);
    				}
    			}
    			
    			Map<String, Integer> range = sheetDesignSectionService.getRangeByDesignId(UUID.fromString(designId));
    			
    			for (SheetDesignExpression expression : expressions) {
    				SheetEvalueateListener listener = null;
    				String regex = "T(\\s*[a-zA-Z0-9\\.]*\\s*)R|T(\\s*[a-zA-Z0-9\\.]*\\s*)C";
    				Pattern pattern = Pattern.compile(regex);
    				try {
    					
    					Matcher matcher = pattern.matcher(expression.getContent());
    					String content = expression.getContent();
    					while (matcher.find()) {
    						int ordinal;
    						if(matcher.group(1) == null){
    							ordinal = 2;
    							String group = matcher.group(ordinal);
        						String string = noMap.get(group);
        						content = content.replaceAll("T"+group+"C", "T"+string+"C");
    						}else{
    							ordinal = 1;
    							String group = matcher.group(ordinal);
        						String string = noMap.get(group);
        						content = content.replaceAll("T"+group+"R", "T"+string+"R");
    						}
    						
    					}
    					
    					listener = analyzeFormula(content + ";", dataMap,
    							range.get("startRowNo"), range.get("endRowNo"), range.get("startColumnNo"),
    							range.get("endColumnNo"), expression.getType());
    					Map<String, Object> formulaData = new HashMap<>();
    			        Map<String, String> calculationFormula = listener.getCalculationFormula();
    			        Map<String, String> result = listener.getCalculationResult();
    			        String rule = calculationFormula.get("0-" + rowNo + "-" + columnNo);
    			        if(StringUtils.isNotBlank(rule)){
    			        	String regex1 = "(T\\[(\\d*)\\]R\\[(\\d*)\\]C\\[(\\d*)\\])";
    			        	formulaData.put("calculationResult", result.get("0-" + rowNo + "-" + columnNo));
    			            formulaData.put("rule", rule.replace("T[0]", "").replace("[", "").replace("]", ""));
    			            String ruleDes = rule.split("=")[1];
    			            Pattern pattern1 = Pattern.compile(regex1);
    			            Matcher matcher1 = pattern1.matcher(ruleDes);
    			            List<Map<String, Object>> datas1 = new ArrayList<>();
    			            while (matcher1.find()) {
    			                String t = matcher1.group(2);
    			                String r = matcher1.group(3);
    			                String c = matcher1.group(4);
    			                Map<String, Object> sheetData = dataMap.get(t + "-" + r + "-" + c);
                                com.sinosoft.ops.cimp.entity.sheet.Sheet sheet = sheetService.getById(UUID.fromString((String) sheetData.get("sheetId")));
    			                sheetData.put("sheetName", sheet.getName());
    			                datas1.add(sheetData);
    			            }
    			            formulaData.put("datas", datas1);
    			        }else{
    			        	if(StringUtils.isNotBlank(sheetNo)){
    			        		for (String no : noMap.keySet()) {
									if(StringUtils.equals(sheetNo, no.trim())){
										String tableNo = noMap.get(no);
										String rule1 = calculationFormula.get(tableNo+"-"+rowNo+"-"+columnNo);
										if(StringUtils.isNotBlank(rule1)){
											String rule2 = rule1.replace("[", "").replace("]", "");
											formulaData.put("calculationResult", result.get(tableNo+"-" + rowNo + "-" + columnNo));
											String regex2 = "(T\\[(\\d*)\\]R\\[(\\d*)\\]C\\[(\\d*)\\])";
											Pattern pattern2 = Pattern.compile(regex2);
											Matcher matcher2 = pattern2.matcher(rule1);
											List<Map<String, Object>> datas2 = new ArrayList<>();
											while(matcher2.find()){
												String t = matcher2.group(2);
				    			                for(String sheetNo1 : noMap.keySet()){
				    			                	if(StringUtils.equals(noMap.get(sheetNo1), t)){
				    			                		rule2 = rule2.replace("T"+t+"R", "T"+sheetNo1+"R");
				    			                		
				    			                	}
				    			                }
				    			                
											}
											matcher2 = pattern2.matcher(rule1.split("=")[1]);
											while(matcher2.find()){
												String t = matcher2.group(2);
												String r = matcher2.group(3);
								                String c = matcher2.group(4);
				    			                for(String sheetNo1 : noMap.keySet()){
				    			                	if(StringUtils.equals(noMap.get(sheetNo1), t)){
				    			                		rule2 = rule2.replace("T"+t+"R", "T"+sheetNo1+"R");
				    			                		
				    			                	}
				    			                }
				    			                Map<String, Object> sheetData = dataMap.get(t + "-" + r + "-" + c);
                                                com.sinosoft.ops.cimp.entity.sheet.Sheet sheet = sheetService.getById(UUID.fromString((String) sheetData.get("sheetId")));
				    			                sheetData.put("sheetName", sheet.getName());
				    			                datas2.add(sheetData);
											}
											formulaData.put("datas", datas2);
											formulaData.put("rule", rule2);
											
										}
									}
								}
    			        	}
    			        }
    			        return formulaData;
    				} catch (Exception e) {
    					logger.error("公式：" + expression.getName() + " 有误，请检查！", e);
    					throw new BusinessException(OpsErrorMessage.MODULE_NAME,OpsErrorMessage.ERROR_MESSAGE,"公式：" + expression.getName() + " 有误，请检查！");
    				}
    			}
    		}
            return null;
        }catch(BusinessException e){
        	logger.error(e.getMessage(),e);
        	throw new BusinessException(OpsErrorMessage.MODULE_NAME,OpsErrorMessage.ERROR_MESSAGE,e.getMessage());
        } catch (Exception e) {
            logger.error("公式运算失败", e);
            throw new BusinessException(OpsErrorMessage.MODULE_NAME,OpsErrorMessage.ERROR_MESSAGE,"公式运算失败");
        }

		
    }

    @Override
    @Transactional
    public Map<String, Object> getFormulaCaculationData(List<Map<String, Object>> sheetDatas, Map<String,Integer> range,
                                                        Collection<SheetDesignExpression> expressions, Integer rowNo, Integer columnNo) {
        String regex = "(T\\[(\\d*)\\]R\\[(\\d*)\\]C\\[(\\d*)\\])";
        Map<String, Map<String, Object>> dataMap = new HashMap<>();
        for (Map<String, Object> sheetData : sheetDatas) {
            dataMap.put("0-" + sheetData.get("rowNo") + "-" + sheetData.get("columnNo"), sheetData);
        }
        String formula = getFormulaCaculation(expressions);
        SheetEvalueateListener listener = analyzeFormula(formula, dataMap, range.get("startRowNo"), range.get("endRowNo"),
                range.get("startColumnNo"), range.get("endColumnNo"));
        Map<String, Object> formulaData = new HashMap<>();
        Map<String, String> calculationFormula = listener.getCalculationFormula();
        Map<String, String> result = listener.getCalculationResult();

        String rule = calculationFormula.get("0-" + rowNo + "-" + columnNo);
        if (StringUtils.isNotBlank(rule)) {
            formulaData.put("calculationResult", result.get("0-" + rowNo + "-" + columnNo));
            formulaData.put("rule", rule.replace("T[0]", "").replace("[", "").replace("]", ""));
            String ruleDes = rule.split("=")[1];
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(ruleDes);
            List<Map<String, Object>> datas = new ArrayList<>();
            while (matcher.find()) {
                String t = matcher.group(2);
                String r = matcher.group(3);
                String c = matcher.group(4);
                Map<String, Object> sheetData = dataMap.get(t + "-" + r + "-" + c);
                com.sinosoft.ops.cimp.entity.sheet.Sheet sheet = sheetService.getById(UUID.fromString((String) sheetData.get("sheetId")));
                sheetData.put("sheetName", sheet.getName());
                datas.add(sheetData);
            }
            formulaData.put("datas", datas);
        }

        return formulaData;
    }

    @Override
	public SheetEvalueateListener analyzeFormula(String formula,
			java.util.Map<String, java.util.Map<String, Object>> dataMap, Integer startRowNo, Integer endRowNo,
			Integer startColumnNo, Integer endColumnNo, Byte type) {
    	CharStream input = CharStreams.fromString(formula);
        // 词法分析
        ESLLexer lexer = new ESLLexer(input);
        // 获得Token流
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        // 语法分析
        ESLParser parser = new ESLParser(tokens);
        // 生成语法树，从规则 program开始
        ParseTree tree = parser.program();
        // 创建遍历器
        ParseTreeWalker walker = new ParseTreeWalker();
        // 调用监听器
        SheetEvalueateListener listener = new SheetEvalueateListener(parser, startRowNo, startColumnNo, endRowNo, endColumnNo, dataMap,type);
        // 开始遍历语法树
        walker.walk(listener, tree);

        return listener;
	}

    @Override
    @Transactional(readOnly=true)
    public SheetEvalueateListener analyzeFormula(String formula, Map<String, Map<String, Object>> dataMap, Integer startRowNo, Integer endRowNo,
                                                  Integer startColumnNo, Integer endColumnNo) {
    	return analyzeFormula(formula, dataMap, startRowNo, endRowNo, startColumnNo, endColumnNo, null);
    }

    @Override
    public String getFormulaCaculation(Collection<SheetDesignExpression> expressions) {
        StringBuilder expression = new StringBuilder();
        for (SheetDesignExpression sheetDesignExpression : expressions) {
            expression.append(sheetDesignExpression.getContent() + ";");
        }
        return expression.toString();
    }


    @Override
    @Transactional(readOnly=true)
    public PageableQueryResult getQueryDataByPage(Collection<SheetDesignDataSource> designDataSources, Collection<SheetDesignField> designFields, List<SheetDesignCondition> designConditions,
                                                  PageableQueryParameter queryParameter, Integer rowNo, Integer columnNo, String[] fields, JSONArray paramArray) throws BusinessException {

        SheetDesignDataSource dataSource = getUniqueDataSource(designDataSources, rowNo, columnNo);
        Collection<SheetDesignCondition> conditions = getDesignConditions(designConditions, rowNo, columnNo);
        SheetDesignField designField =getUniqueDesignField(designFields,rowNo,columnNo);
        String querySql = getQuerySql(dataSource,designField, fields, conditions, paramArray);

        PageableQueryResult result = sheetComDao.getQueryDataByPage(querySql, queryParameter);
        return result;
    }
    
    @Override
    @Transactional(readOnly=true)
    public PageableQueryResult getQueryDataByPage(Collection<SheetDesignDataSource> designDataSources,Collection<SheetDesignField> designFields, List<SheetDesignCondition> designConditions,
                     PageableQueryParameter queryParameter,UUID designId, String sectionNo, String[] fields, JSONArray paramArray) throws BusinessException {

        SheetDesignDataSource dataSource = getUniqueDataSource(designDataSources,sectionNo);
        Collection<SheetDesignCondition> conditions = getDesignConditions(designConditions,sectionNo);
        SheetDesignField designField = sheetDesignFieldService.getBingData(designId, sectionNo);
        String querySql = getQuerySql(dataSource,designField, fields, conditions, paramArray);

        PageableQueryResult result;
		try {
			result = sheetComDao.getQueryDataByPage(querySql, queryParameter);
		} catch (Exception e) {
			logger.error("获取查询反查数据失败",e);
			logger.error("SQL:"+querySql);
			throw new BusinessException(OpsErrorMessage.MODULE_NAME,OpsErrorMessage.ERROR_MESSAGE,"获取查询反查数据失败");
		}
        return result;
    }

    private String getFieldsSql(String[] fields) {
        int i = 1;
        StringBuilder fieldsSql = new StringBuilder();
        fieldsSql.append(" distinct ");
        for (String field : fields) {
            fieldsSql.append(field + " f" + i + ",");
            i++;
        }
        return fieldsSql.deleteCharAt(fieldsSql.lastIndexOf(",")).toString();

    }


    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getQueryData(Collection<SheetDesignDataSource> designDataSources,
                                                  Collection<SheetDesignField> designFields, List<SheetDesignCondition> designConditions, Integer rowNo, Integer columnNo, String[] fields, JSONArray paramArray) throws BusinessException {

        SheetDesignDataSource dataSource = getUniqueDataSource(designDataSources, rowNo, columnNo);
        Collection<SheetDesignCondition> conditions = getDesignConditions(designConditions, rowNo, columnNo);
        SheetDesignField designField =getUniqueDesignField(designFields,rowNo,columnNo);
        String querySql = getQuerySql(dataSource, designField,fields, conditions, paramArray);

        List<Map<String, Object>> result = sheetComDao.getQueryData(querySql);
        return result;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getQueryData(Collection<SheetDesignDataSource> designDataSources,
                                                  Collection<SheetDesignField> designFields, List<SheetDesignCondition> designConditions,UUID designId, String sectionNo, String[] fields, JSONArray paramArray) throws BusinessException {

        SheetDesignDataSource dataSource = getUniqueDataSource(designDataSources,sectionNo);
        Collection<SheetDesignCondition> conditions = getDesignConditions(designConditions,sectionNo);
        SheetDesignField designField = sheetDesignFieldService.getBingData(designId, sectionNo);
        String querySql = getQuerySql(dataSource, designField,fields, conditions, paramArray);

        List<Map<String, Object>> result = sheetComDao.getQueryData(querySql);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getQueryDataSql(Collection<SheetDesignDataSource> designDataSources,
                                               Collection<SheetDesignField> designFields,  List<SheetDesignCondition> designConditions, Integer rowNo, Integer columnNo, String[] fields, String[] fieldsDes, JSONArray paramArray) throws BusinessException {

        SheetDesignDataSource dataSource = getUniqueDataSource(designDataSources, rowNo, columnNo);
        Collection<SheetDesignCondition> conditions = getDesignConditions(designConditions, rowNo, columnNo);

        SheetDesignField designField =getUniqueDesignField(designFields,rowNo,columnNo);
        String querySql = getQuerySql(dataSource, designField,fields, conditions, paramArray);

        Map<String, Object> sqlDesc = new HashMap<String, Object>();
        StringBuilder sqlDes = new StringBuilder();
        if (dataSource != null) {
            sqlDes.append("数据源：" + dataSource.getName() + "\n");
        }

        if (fieldsDes != null) {
            sqlDes.append("数据项： " + fieldsDes == null ? "" : String.join(",", fieldsDes) + "\n");
        }

        int i = 1;
        for (SheetDesignCondition condition : conditions) {
            sqlDes.append("条件项" + i + ": " + condition.getConditionName() + "\n");
            i++;
        }

        sqlDes = sqlDes.delete(sqlDes.lastIndexOf("\n"), sqlDes.length());

        sqlDesc.put("querySql", querySql);

        sqlDesc.put("sqlDes", sqlDes.toString());

        return sqlDesc;
    }


    @Override
    public void exportQueryData(String[] fieldsDes, List<Map<String, Object>> result, HttpServletResponse response) throws Exception {
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("查询反查数据.xls", "UTF-8"));
        response.setHeader("Connection", "close");
        response.setHeader("Content-Type", "application/x-download");
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);

        for (int i = 0; i < fieldsDes.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(fieldsDes[i]);
            cell.setCellStyle(style);
        }
        int i = 1;
        for (Map<String, Object> data : result) {
            Row row2 = sheet.createRow(i);
            for (int j = 0; j < fieldsDes.length; j++) {
                Cell cell = row2.createCell(j);
                cell.setCellValue(data.get("F" + (j + 1)) == null ? "" : data.get("F" + (j + 1)).toString());
            }
            i++;
        }
        workbook.write(response.getOutputStream());
    }


    @Override
    public void fillData2Pdf(byte[] content, Collection<Map<String,Object>> collection, HttpServletResponse response) throws Exception {
        String wordFilePath = WordUtil.fillData2Templement(content, collection);
        String pdfFilePath = wordFilePath.substring(0, wordFilePath.lastIndexOf("/")) + "/pdf/" + UUID.randomUUID().toString() + ".pdf";
        WordUtil.doc2pdf(wordFilePath, pdfFilePath);
        FileUtils.writeFile(pdfFilePath, response);
        if (new File(wordFilePath).exists())
            new File(wordFilePath).delete();
        if (new File(pdfFilePath).exists()) {
            new File(pdfFilePath).delete();
        }
    }
    
    @Override
	public void fillData2Word(byte[] content, Collection<java.util.Map<String, Object>> datas,
			HttpServletResponse response,String name) throws Exception {
    	  response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(name+".doc", "UTF-8"));
    	  response.setHeader("Connection", "close");
    	  response.setHeader("Content-Type", "application/x-download");
    	  String wordFilePath = WordUtil.fillData2Templement(content, datas);
          FileUtils.writeFile(wordFilePath, response);
          if (new File(wordFilePath).exists())
              new File(wordFilePath).delete();
	}


    @Override
    public SheetDesignSection getUniqueSection(Collection<SheetDesignSection> sections, Integer rowNo,
                                               Integer columnNo) {
        List<SheetDesignSection> designSections = new ArrayList<>();
        for (SheetDesignSection section : sections) {
            boolean flag = matchingRange(section.getStartRowNo(), section.getEndRowNo(),
                    section.getStartColumnNo(), section.getEndColumnNo(), rowNo, columnNo);
            if (flag) {
                designSections.add(section);
            }
        }
        Assert.isTrue(designSections.size() < 2, "数据块绑定错误，对同一区域重复绑定,行列号："+rowNo+"-"+columnNo);
        if (!CollectionUtils.isEmpty(designSections)) {
            return designSections.get(0);
        }
        return null;
    }

    @Override
    public SheetDesignSection getUniqueSectionByCtrl(Collection<SheetDesignSection> sections, Integer ctrlRowNo,
                                                     Integer ctrlColumnNo) {
        List<SheetDesignSection> designSections = new ArrayList<>();
        for (SheetDesignSection section : sections) {
            boolean flag = matchingRange(section.getCtrlRowStart(), section.getCtrlRowEnd(),
                    section.getCtrlColumnStart(), section.getCtrlColumnEnd(), ctrlRowNo, ctrlColumnNo);
            if (flag) {
                designSections.add(section);
            }
        }
        Assert.isTrue(designSections.size() < 2, "数据源绑定错误，对同一区域重复绑定");
        if (!CollectionUtils.isEmpty(designSections)) {
            return designSections.get(0);
        }
        return null;
    }
    
    @Override
    @Transactional(readOnly=true)
    public Map<String,Integer> getCtrlPositionByRelativePosition(UUID designId,Integer rowNo,Integer columnNo){
    	Collection<SheetDesignSection> collection = sheetDesignSectionService.getByDesignId(designId);
    	SheetDesignSection section = getUniqueSection(collection, rowNo, columnNo);
    	//数据块绝对位置和相对位置的差值，确定单元格的绝对位置
		int betweenRow = section.getCtrlRowStart()-section.getStartRowNo();
		int betweenColumn = section.getCtrlColumnStart()-section.getStartColumnNo();
		
		Map<String,Integer> position = new HashMap<String, Integer>();
		position.put("rowNo", rowNo+betweenRow-1);
		position.put("columnNo", columnNo+betweenColumn-1);
		return position;
    }


    @Override
    public void exportFormulaCaculationData(List<Map<String,Object>> list, HttpServletResponse response) throws IOException {
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("校核反查数据.xls", "UTF-8"));
        response.setHeader("Connection", "close");
        response.setHeader("Content-Type", "application/x-download");
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);

        Cell cell = row.createCell(0);
        cell.setCellValue("表号");
        cell.setCellStyle(style);
        Cell cell1 = row.createCell(1);
        cell1.setCellValue("表名");
        cell1.setCellStyle(style);
        Cell cell2 = row.createCell(2);
        cell2.setCellValue("行号");
        cell2.setCellStyle(style);
        Cell cell3 = row.createCell(3);
        cell3.setCellValue("列号");
        cell3.setCellStyle(style);
        Cell cell4 = row.createCell(4);
        cell4.setCellValue("数值");
        cell4.setCellStyle(style);

        int i = 1;
        for (Map<String,Object> data : list) {
            Row row2 = sheet.createRow(i);
            Cell c0 = row2.createCell(0);
            c0.setCellValue((String)data.get("sheetNo"));
            Cell c1 = row2.createCell(1);
            c1.setCellValue((String)data.get("sheetName"));
            Cell c2 = row2.createCell(2);
            c2.setCellValue(data.get("rowNo") + "");
            Cell c3 = row2.createCell(3);
            c3.setCellValue(data.get("columnNo") + "");
            Cell c4 = row2.createCell(4);
            c4.setCellValue(data.get("stringValue")+"");
            i++;
        }
        workbook.write(response.getOutputStream());

    }


    /**
     * 
     * 获取参数包装信息
     * @param sheetNos
     * @param parameterId
     * @param parameterValue
     * @return
     */
	@Override
	@Transactional(readOnly=true)
	public Collection<SheetInfo> getSheetInfos(Set<String> sheetNos, String parameterId,
			String parameterValue) {
		return sheetComDao.getSheetInfos(sheetNos,parameterId,parameterValue);
	}


	@Override
	@Transactional(readOnly=true)
	public Collection<SheetInfo> getSheetInfos(Set<String> sheetNos, Collection<SheetParameter> parameters,UUID categoryId) {
		StringBuilder parameterSql = new StringBuilder();
		Set<String> nos = new HashSet<>();
		for (String string : sheetNos) {
			nos.add(string.trim());
		}
		parameterSql.append("select DISTINCT s.id sheetId,s.design_id designId,sd.SHEET_NO sheetNo,s.name sheetName from sheet_design sd,sheet s,sheet_sheet_category ssc,");
		parameterSql.append("(select t1.sheet_id from sheet_parameter t1 ");
		//String parameterSql = "select t1.sheet_id from sheet_parameter t1 ";
		int i = 1;
		Map<String, Object> parameter = new HashMap<>();
		for (SheetParameter sheetParameter : parameters) {
			if(i==1){
				parameter.put("parameterId1", sheetParameter.getParameterId());
				if(sheetParameter.getParameterValue()!=null){
					parameter.put("parameterValue1", sheetParameter.getParameterValue());
				}
				
			}else{
				parameterSql.append(" join sheet_parameter t"+i+" on t"+i+".sheet_id=t1.sheet_id and (t"+i+".parameter_id=:parameterId"+i+" and t"+i+".parameter_value");
				parameterSql.append(sheetParameter.getParameterValue()==null?" is null )":"=:parameterValue"+i+")");
				//parameterSql.append(" join sheet_parameter t"+i+" on t"+i+".sheet_id=t1.sheet_id and (t"+i+".parameter_id=:parameterId"+i+" and t"+i+".parameter_value"+sheetParameter.getParameterValue()==null?" is null ":"=:parameterValue"+i+")");
				parameter.put("parameterId"+i,sheetParameter.getParameterId());
				if(sheetParameter.getParameterValue()!=null){
					parameter.put("parameterValue"+i, sheetParameter.getParameterValue());
				}
			}
			i++;
		}
		parameterSql.append(" where t1.parameter_id=:parameterId1 and t1.parameter_value");
		parameterSql.append(parameter.get("parameterValue1")==null?" is null":"=:parameterValue1");
		//parameterSql.append(" where t1.parameter_id=:parameterId1 and t1.parameter_value"+parameter.get("parameterValue1")==null?" is null":"=:parameterValue1");
		parameterSql.append(") sp where sd.SHEET_NO in (:sheetNos) and sd.id=s.DESIGN_ID and  s.id=sp.SHEET_ID and s.id=ssc.sheet_id and ssc.category_id=:categoryId");
		parameter.put("categoryId", categoryId);
		return sheetComDao.getSheetInfos(nos, parameter, parameterSql.toString());
	}



	public List<Map<String, Object>> getFormulaCheckData(String formula, List<Map<String, Object>> result,Map<String,Map<String, Object>> dataMap,Map<String, String> noMap) {
		
		String regex = "(T\\[(\\d*)\\]R\\[(\\d*)\\]C\\[(\\d*)\\])";
		String regex2 = "T(\\s*[a-zA-Z0-9\\.]*\\s*)R|T(\\s*[a-zA-Z0-9\\.]*\\s*)C";
		for (Map<String, Object> map : result) {
			if(StringUtils.equals(formula, (String)map.get("statement"))){
				List<Map<String,Object>> childs = (List<Map<String, Object>>) map.get("childs");
				for (Map<String, Object> child : childs) {
					String rule = (String) child.get("rule");
					Pattern pattern = Pattern.compile(regex);
					Matcher matcher = pattern.matcher(rule.split("==|>|<|>=|<=")[1]);
					List<Map<String, Object>> checkDatas = new ArrayList<>();
					while(matcher.find()){
						String t = matcher.group(2);
						String r = matcher.group(3);
						String c = matcher.group(4);
						Map<String,Object> data = (Map<String, Object>) dataMap.get(t+"-"+r+"-"+c);
						if(data != null){
							Map<String, Object> data1 = new HashMap<>(data);
							checkDatas.add(data1);
						}
						
					}
					//表内校核
					if(1==(Byte)map.get("type")){
						rule = rule.replace("T[0]", "").replace("[", "").replace("]", "");
						
					}else if(3==(Byte)map.get("type")){//表间校核
						rule = rule.replace("[", "").replace("]", "");
					}
					Pattern pattern2 = Pattern.compile(regex2);
					Matcher matcher2 = pattern2.matcher(rule);
					while(matcher2.find()){
						String value;
						if(matcher2.group(1) == null){
							value = matcher2.group(2);
							for (String key : noMap.keySet()) {
								if(StringUtils.equals(noMap.get(key),value)){
									rule = rule.replace("T"+value+"C","T"+key+"C");
									break;
								}
							}
						}else{
							value = matcher2.group(1);
							for (String key : noMap.keySet()) {
								if(StringUtils.equals(noMap.get(key),value)){
									rule = rule.replace("T"+value+"R","T"+key+"R");
									break;
								}
							}
						}
						
					}
					child.put("rule", rule);
					child.put("checkDatas", checkDatas);
				}
				return childs;
			}
		}
		return null;
	}
	
	@Transactional
	@Override
	public List<SheetData> getComputeDatas4Excel(UUID userId,UUID sheetId,UUID designId,JSONArray paramArray,SheetComService sheetComService,Integer priority) throws BusinessException, Exception{
		UUID key;
		if(sheetId == null){
			key = designId;
		}else{
			key = sheetId;
		}
		try {
			
			// 查出同一designId的数据块、数据源、数据项、条件项
			Collection<SheetDesignSection> sections = sheetDesignSectionService
					.getByDesignId(designId);
			Collection<SheetDesignDataSource> designDataSources = sheetDesignDataSourceService
					.getByDesignId(designId);
			Collection<SheetDesignField> designFields = sheetDesignFieldService
					.getByDesignId(designId);
			List<SheetDesignCondition> designConditions = sheetDesignConditionService
					.getByDesignId(designId);

			List<SheetData> sheetDatas = new ArrayList<>();

			for (SheetDesignSection sheetDesignSection : sections.stream().filter(item -> item.getAutoExpand())
					.collect(Collectors.toList())) {

				// 唯一数据源
				SheetDesignDataSource dataSource = getUniqueDataSource(designDataSources,
						sheetDesignSection.getStartRowNo(), sheetDesignSection.getStartColumnNo());

				// 唯一数据项
				SheetDesignField designField = getUniqueDesignField(designFields,
						sheetDesignSection.getStartRowNo(), sheetDesignSection.getStartColumnNo());
				// 符合条件的条件项集合
				Collection<SheetDesignCondition> conditions = getDesignConditions(designConditions,
						sheetDesignSection.getStartRowNo(), sheetDesignSection.getStartColumnNo());

				List<SheetData> lstData = mergeListData(sheetDesignSection, dataSource, designField,
						conditions, paramArray);

				sheetDatas.addAll(lstData);
			}
			long a = System.currentTimeMillis();
			List<Future<SheetData>> futures = new ArrayList<>();
			List<SheetBuildDataExcutor1> sheetBuildDataExcutors = new LinkedList<>();
			LinkedBlockingQueue<SheetBuildDataExcutor1> sheetBuildDataExcutors1 = new LinkedBlockingQueue<>();
			for (SheetDesignSection sheetDesignSection : sections.stream().filter(item -> !item.getAutoExpand())
					.collect(Collectors.toList())) {

				// 数据块绝对位置和相对位置的差值，确定单元格的绝对位置
				int betweenRow = sheetDesignSection.getCtrlRowStart() - sheetDesignSection.getStartRowNo();
				int betweenColumn = sheetDesignSection.getCtrlColumnStart() - sheetDesignSection.getStartColumnNo();

				for (int i = sheetDesignSection.getStartRowNo(); i <= sheetDesignSection.getEndRowNo(); i++) {

					for (int j = sheetDesignSection.getStartColumnNo(); j <= sheetDesignSection.getEndColumnNo(); j++) {

						// 唯一数据源
						SheetDesignDataSource dataSource = getUniqueDataSource(designDataSources, i, j);

						// 唯一数据项
						SheetDesignField designField = getUniqueDesignField(designFields, i, j);
						// 符合条件的条件项集合
						Collection<SheetDesignCondition> conditions = getDesignConditions(designConditions, i, j);
						
						if (designField != null) {
							//int nextInt = RandomUtils.nextInt(0, 10);
							String querySql = getQuerySql(dataSource, designField, conditions, paramArray,new LinkedHashMap<>());
							SheetBuildDataExcutor1 dataExcutor = new SheetBuildDataExcutor1(dataSource, designField, conditions, i, j, betweenRow, betweenColumn, paramArray,priority,key,userId,sheetId,designId,querySql);
							sheetBuildDataExcutors.add(dataExcutor);
							sheetBuildDataExcutors1.add(dataExcutor);
							//Future<SheetData> future = mainTaskExecutor.submit(dataExcutor);
							//sheetDatas.add(future.get());
							//futures.add(future);
							//ThreadPoolExecutor pool = (ThreadPoolExecutor) excutor;
							//System.out.println("线程池中得线程数目："+pool.getPoolSize()+";队列中数目："+pool.getQueue().size()+";已执行完成得数目："+pool.getCompletedTaskCount()+";活跃得数目："+pool.getActiveCount());
						}

					}
				}
			}
			//priorityQueue.addAll(sheetBuildDataExcutors);
			concurrentStatisticsStatus.put(userId.toString()+"_"+key.toString(), new SheetStatisticsStatus(sheetBuildDataExcutors.size()));
			concurrentHashMap1.put(userId.toString()+"_"+key.toString(), sheetBuildDataExcutors1);
			if(!CollectionUtils.isEmpty(sheetBuildDataExcutors)){
				gatherSign.put(userId.toString()+"_"+key.toString(), "1");
			}
			
			
			controlTaskExecutor.submit(new Runnable() {
				public void run() {
					logger.debug("提交统计任务线程开始："+Thread.currentThread().getName());
					Set<String> keySet;
					while(!CollectionUtils.isEmpty(keySet = concurrentHashMap1.keySet())){
						logger.debug("当前统计报表数："+concurrentHashMap1.size());
						synchronized (priorityQueue) {
							while(statisticsTaskExecutor.getThreadPoolExecutor().getQueue().size()>=16){
								try {
									logger.debug(Thread.currentThread().getName()+":任务线程池线程数："+statisticsTaskExecutor.getThreadPoolExecutor().getQueue().size());
									//if(mainTaskExecutor.getThreadPoolExecutor().getQueue().size()>=16){
										logger.debug(Thread.currentThread().getName()+"：等待。。。");
										priorityQueue.wait();
										logger.debug(Thread.currentThread().getName()+"：被唤醒。。。");
									//}
								} catch (InterruptedException e) {
									logger.error("阻塞线程失败",e);
								}
							}
						}
						for (String string : keySet) {
							LinkedBlockingQueue<SheetBuildDataExcutor1> queue = concurrentHashMap1.get(string);
							SheetBuildDataExcutor1 poll = queue.poll();
							if(poll != null){
								statisticsTaskExecutor.submit(poll);
							}
							
						}
					}
					logger.debug("提交统计任务线程结束："+Thread.currentThread().getName());
//					SheetBuildDataExcutor1 dataExcutor;
//					while((dataExcutor = priorityQueue.poll())!=null){
//						synchronized (priorityQueue) {
//							
//							
//							while(mainTaskExecutor.getThreadPoolExecutor().getQueue().size()>=16){
//								try {
//									int activeCount = mainTaskExecutor.getActiveCount();
//									logger.info("当前活跃数："+activeCount+" 最大数："+maxPoolSize);
//									logger.info("线程池队列数："+mainTaskExecutor.getThreadPoolExecutor().getQueue().size());
//									logger.info(Thread.currentThread().getName()+"等待");
//									priorityQueue.wait();
//									logger.info(Thread.currentThread().getName()+"被唤醒");
//									
//								} catch (InterruptedException e) {
//									logger.error("阻塞线程失败",e);
//								}
//							}
//							logger.info(Thread.currentThread().getName()+"提交任务");
//							mainTaskExecutor.submit(dataExcutor);
//						}
//						
//						
//					}
					
				}
			});
			
			
			long b = System.currentTimeMillis();
			logger.debug("计算耗时："+(b-a));
			return sheetDatas;
		} catch (Exception e) {
			cancelStatistics(userId, key);
			logger.error("TODO:异常描述",e);
			throw new RuntimeException(e);
		} 
	}


	@Override
	@Transactional
	public void batchDirectStatistic(UUID sheetId,UUID designId, JSONArray jsonArray, SheetComService sheetComService, UUID userId) throws BusinessException, Exception {
		List<SheetData> list = getComputeDatas4Excel(userId,sheetId,designId, jsonArray, sheetComService,0);
		if(!CollectionUtils.isEmpty(list)){
			Map<String, Integer> range1 = sheetDesignSectionService.getRangeByDesignId(designId);
			//公式计算
			Collection<SheetDesignExpression> expressions = sheetDesignExpressionService
					.getCaculationFormulaByDesignId(designId);
			List<Map<String, Object>> list1 = sheetComService.formulaCaculation(list, range1, expressions);
			List<SheetData> dataList1 = new LinkedList<>();
			for (Map<String, Object> sheetData : list1) {
				JSONObject jsonObj = new JSONObject(sheetData);
				SheetData data = jsonObj.toJavaObject(SheetData.class);
				data.setId(UUID.randomUUID());
				data.setSheetId(sheetId);
				data.setBeingEdited(false);
				data.setStatus(DataStatus.NORMAL.getValue());
				data.setCreatedBy(userId);
				data.setCreatedTime(new Timestamp(System.currentTimeMillis()));
				data.setLastModifiedBy(userId);
				data.setLastModifiedTime(new Timestamp(System.currentTimeMillis()));
				dataList1.add(data);
			}
			sheetService.saveInfosAndDatas(sheetId, dataList1, null, null,true);
		}
	}
	
	@Override
	@Transactional
	public void saveFormulaSheetData(UUID sheetId,UUID designId, UUID userId,SheetStatisticsStatus status) throws BusinessException, Exception {
		//SheetStatisticsStatus status = concurrentStatisticsStatus.get(userId.toString()+"_"+sheetId.toString());
		Collection<SheetData> collection = sheetDataService.getBySheetId(sheetId);
		Map<String, SheetData> dataMap = new HashMap<String, SheetData>();
		if(!CollectionUtils.isEmpty(collection)){
			for (SheetData sheetData : collection) {
				SheetData cloneData = (SheetData) this.cloneObject(sheetData);
				cloneData.setId(null);
				cloneData.setCreatedBy(null);
				cloneData.setLastModifiedBy(null);
				cloneData.setSheetId(null);
				dataMap.put(sheetData.getRowNo()+"-"+sheetData.getColumnNo(), cloneData);
			}
		}
		List<SheetData> list = new ArrayList<>(status.getDatas());
		
		if(!CollectionUtils.isEmpty(list)){
			List<SheetData> dataList1 = new LinkedList<>();
			for (SheetData data : list) {
				if(dataMap.containsKey(data.getRowNo()+"-"+data.getColumnNo())){
					SheetData sd = dataMap.get(data.getRowNo()+"-"+data.getColumnNo());
					sd.setStringValue(data.getStringValue());
					dataList1.add(sd);
					dataMap.remove(data.getRowNo()+"-"+data.getColumnNo());
				}else{
					dataList1.add(data);
				}
				//dataList1.add(data);
			}
			dataList1.addAll(dataMap.values());
			List<Map<String, Object>> list1 = sheetComService.formulaCaculation(sheetId.toString(), designId.toString(),userId, dataList1);
			List<Map<String,Object>> fillUnit = sheetComService.fillUnit(designId, sheetId);
			list1.addAll(fillUnit);
			List<SheetData> list2 = new ArrayList<>();
			for (Map<String, Object> sheetData : list1) {
				JSONObject jsonObj = new JSONObject(sheetData);
				SheetData data = jsonObj.toJavaObject(SheetData.class);
				data.setId(UUID.randomUUID());
				data.setSheetId(sheetId);
				data.setBeingEdited(false);
				data.setStatus(DataStatus.NORMAL.getValue());
				data.setCreatedBy(userId);
				data.setCreatedTime(new Timestamp(System.currentTimeMillis()));
				data.setLastModifiedBy(userId);
				data.setLastModifiedTime(new Timestamp(System.currentTimeMillis()));
				
				list2.add(data);
			}
			sheetService.saveInfosAndDatas(sheetId, list2, null, null,true);
		}
		
		
		
//		if(!CollectionUtils.isEmpty(list)){
//			
//			List<Map<String, Object>> list1 = sheetComService.formulaCaculation(sheetId.toString(), designId.toString(),userId, list);
//			List<Map<String,Object>> fillUnit = sheetComService.fillUnit(designId, sheetId);
//			list1.addAll(fillUnit);
//			List<SheetData> dataList1 = new LinkedList<>();
//			for (Map<String, Object> sheetData : list1) {
//				JSONObject jsonObj = new JSONObject(sheetData);
//				SheetData data = jsonObj.toJavaObject(SheetData.class);
//				data.setId(UUID.randomUUID());
//				data.setSheetId(sheetId);
//				data.setBeingEdited(false);
//				data.setStatus(DataStatus.NORMAL.getValue());
//				data.setCreatedBy(userId);
//				data.setCreatedTime(new Timestamp(System.currentTimeMillis()));
//				data.setLastModifiedBy(userId);
//				data.setLastModifiedTime(new Timestamp(System.currentTimeMillis()));
//				if(dataMap.containsKey(data.getRowNo()+"-"+data.getColumnNo())){
//					SheetData sd = dataMap.get(data.getRowNo()+"-"+data.getColumnNo());
//					sd.setStringValue(data.getStringValue());
//				}else{
//					dataList1.add(data);
//				}
//				dataList1.add(data);
//			}
//			dataList1.addAll(dataMap.values());
			
//			sheetService.saveInfosAndDatas(sheetId, dataList1, null, null);
			
		//}
	}


	@Override
	public void cancelStatistics(UUID userId,UUID key) {
		
		concurrentStatisticsStatus.remove(userId.toString()+"_"+key.toString());
		concurrentHashMap1.remove(userId.toString()+"_"+key.toString());
		gatherSign.remove(userId.toString()+"_"+key.toString());
	}
	
	
	@Override
	public SheetStatisticsStatus pollingGetStatus(UUID userId,UUID key){
		return concurrentStatisticsStatus.get(userId.toString()+"_"+key.toString());
	}
	
	@Transactional
	@Override
	public List<Map<String, Object>> fillUnit(UUID designId,UUID sheetId){
		Collection<SheetDesignExpression> expressions = sheetDesignExpressionService.getFillUnitFormulaByDesignId(designId);
		List<Map<String,Object>> list = new ArrayList<>();
		if(!CollectionUtils.isEmpty(expressions)){
			
			Map<String, String> noByExpression = analyzeSheetNoByExpression(expressions);
			Collection<SheetParameter> parameters = sheetParameterService.getBySheetId(sheetId);
			SheetSheetCategory sheetSheetCategory = sheetSheetCategoryService.getBySheetId(sheetId);
			Collection<SheetInfo> sheetInfos = getSheetInfos(noByExpression.keySet(), parameters,sheetSheetCategory.getCategoryId());
			if(!CollectionUtils.isEmpty(sheetInfos)){
				Map<String, SheetData> dataMap = new HashMap<>();
				for (SheetInfo sheetInfo : sheetInfos) {
					if(sheetInfo.getSheetId().equals(sheetId))continue;
					Collection<SheetData> sheetDatas = sheetDataService.getBySheetId(sheetInfo.getSheetId());
					if(!CollectionUtils.isEmpty(sheetDatas)){
						for (SheetData sheetData : sheetDatas) {
							dataMap.put(sheetInfo.getSheetNo()+"-"+sheetData.getRowNo()+"-"+sheetData.getColumnNo(), sheetData);
						}
					}
					
				}
				String regex = "T\\s*([a-zA-Z0-9\\.]*)\\s*R(\\d*)C(\\d*)";
				String regex1 = "\\$\\{([a-zA-Z0-9\\.\\u4e00-\\u9fa5\\p{P}\\s]*)\\}";
				Pattern pattern = Pattern.compile(regex);
				Pattern pattern1 = Pattern.compile(regex1);
				
				List<Map<String, Object>> maps = new ArrayList<>();
				for (SheetDesignExpression expression : expressions) {
					//String[] split = expression.getContent().split("=");
					Matcher matcher1 = pattern1.matcher(expression.getContent());
					while(matcher1.find()){
						Map<String, Object> map = new HashMap<>();
						map.put("start", matcher1.start());
						map.put("end", matcher1.end());
						map.put("val", matcher1.group(1));
						maps.add(map);
					}
					Matcher matcher = pattern.matcher(expression.getContent());
					int i = 1;
					Integer row=-1,col=-1;
					
					String value = "";
					while(matcher.find()){
						String sheetNo = matcher.group(1);
						String rowNo = matcher.group(2);
						String colNo = matcher.group(3);
						if(i==1){
							row = Integer.parseInt(rowNo);
							col = Integer.parseInt(colNo);
						}else if(i==2){
							SheetData sheetData = dataMap.get(sheetNo+"-"+rowNo+"-"+colNo);
							if(sheetData != null){
								value = dataMap.get(sheetNo+"-"+rowNo+"-"+colNo).getStringValue();
								int start = matcher.start();
								for (Map<String, Object> map : maps) {
									int mapStart = (int) map.get("start");
									if(mapStart<start){
										value = map.get("val")+value;
									}else if(mapStart>start){
										value += map.get("val");
									}
								}
							}
							
						}
						i++;
					}
					if(row != -1 && col != -1){
						java.util.Map<String, Integer> position = getCtrlPositionByRelativePosition(designId, row, col);
						SheetData sheetData = new SheetData();
						sheetData.setBeingEdited(false);
						sheetData.setRowNo(row);
						sheetData.setColumnNo(col);
						sheetData.setCtrlRowNo(position.get("rowNo"));
						sheetData.setCtrlColumnNo(position.get("columnNo"));
						sheetData.setStringValue(value);
						list.add((Map<String,Object>)JSON.toJSON(sheetData));
					}
				}
			}
			
		}
		return list;
	}
	
	public class SheetStatisticsStatus{
		//当前主线程所有task
		private int total;
		//当前主线程计算出的data
		private ConcurrentLinkedQueue<SheetData> datas = new ConcurrentLinkedQueue<>();
		//计数器
		private AtomicInteger num = new AtomicInteger(0);
		//状态	0 未完成 1完成 -1 错误
		private byte status;
		//信息
		private String message;
		
		public SheetStatisticsStatus(int total) {
			super();
			this.total = total;
		}
		
		
		public SheetStatisticsStatus(byte status, String message) {
			super();
			this.status = status;
			this.message = message;
		}


		public int getTotal() {
			return total;
		}
		public void setTotal(int total) {
			this.total = total;
		}
		public ConcurrentLinkedQueue<SheetData> getDatas() {
			return datas;
		}
		public void setDatas(ConcurrentLinkedQueue<SheetData> datas) {
			this.datas = datas;
		}
		public AtomicInteger getNum() {
			return num;
		}
		public void setNum(AtomicInteger num) {
			this.num = num;
		}
		public byte getStatus() {
			return status;
		}
		public void setStatus(byte status) {
			this.status = status;
		}
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
		
		
	}

	class SheetBuildDataExcutor1 implements Callable<SheetData>,Comparable<SheetBuildDataExcutor1>{
		
		
		private static final int DEFAULT_PRIORIT = 0;

		private SheetDesignDataSource dataSource;
		
		private SheetDesignField designField;
		
		private Collection<SheetDesignCondition> conditions;
		
		private int row;
		
		private int column;
		
		private int betweenRow;
		
		private int betweenColumn;

		private JSONArray paramArray;
		
		private int priority = DEFAULT_PRIORIT;

		private UUID key;
		
		private UUID userId;
		
		private UUID sheetId;
		
		private UUID designId;
		
		private String sql;

		public SheetBuildDataExcutor1(SheetDesignDataSource dataSource, SheetDesignField designField,
				Collection<SheetDesignCondition> conditions, int row, int column, int betweenRow, int betweenColumn,
				JSONArray paramArray, int priority, UUID key, UUID userId,UUID sheetId,UUID designId,String sql) {
			super();
			this.dataSource = dataSource;
			this.designField = designField;
			this.conditions = conditions;
			this.row = row;
			this.column = column;
			this.betweenRow = betweenRow;
			this.betweenColumn = betweenColumn;
			this.paramArray = paramArray;
			this.priority = priority;
			this.key = key;
			this.userId = userId;
			this.sheetId = sheetId;
			this.designId = designId;
			this.sql = sql;
		}

		@Override
		@Transactional
		public SheetData call() throws Exception {
			try {
				logger.debug("统计线程："+Thread.currentThread().getName()+"启动");
				long i = System.currentTimeMillis();
				//DynamicDataSourceContextHolder.setDataSource("master");
				//String querySql = sheetComService.getQuerySql(dataSource, designField, conditions, paramArray, new LinkedHashMap<>());
				//logger.info("统计线程："+Thread.currentThread().getName()+"从主库获取查询sql："+sql);
				DynamicDataSourceContextHolder.setDataSource("slave");
				long ii = System.currentTimeMillis();
				SheetData data = sheetComService.queryDataBySql(sql, row, column, betweenRow, betweenColumn);
				//SheetData data = sheetComService.mergeEachOption(dataSource, designField, conditions, row, column,betweenRow,betweenColumn,paramArray);
				long jj = System.currentTimeMillis();
				logger.debug(Thread.currentThread().getName()+"查询耗时："+(jj-ii));
				synchronized (priorityQueue) {
					int activeCount = statisticsTaskExecutor.getActiveCount();
//					logger.debug("统计线程池当前活跃数："+activeCount+" 最大数："+maxPoolSize);
					logger.debug("统计线程池队列数："+statisticsTaskExecutor.getThreadPoolExecutor().getQueue().size());
					logger.debug(Thread.currentThread().getName()+"唤醒提交任务线程");
					priorityQueue.notifyAll();
					
				}
				SheetStatisticsStatus status = concurrentStatisticsStatus.get(this.userId.toString()+"_"+this.key.toString());
				if(status != null){
					status.getDatas().add(data);
					status.getNum().incrementAndGet();
					
					if(status.getTotal() == status.getNum().get() && priority == 1){
						storageTaskExecutor.submit(new Runnable() {
							
							@Override
							public void run() {
								DynamicDataSourceContextHolder.setDataSource("master");
								try {
									sheetComService.saveFormulaSheetData(sheetId, designId, userId,status);
								} catch (Exception e) {
									logger.error("统计后保存报错sheetId:"+sheetId.toString()+"-designId:"+designId.toString()+"-userId:"+userId.toString(),e);
								}
								gatherSign.remove(userId.toString()+"_"+key.toString());
								
							}
						});
						
					}
				}
				
				long j = System.currentTimeMillis();
				//logger.info(Thread.currentThread().getName()+"耗时："+(j-i));
				logger.debug("统计线程："+Thread.currentThread().getName()+"结束");
				return data;
			} catch (Exception e) {
				sheetComService.cancelStatistics(userId, key);
				concurrentStatisticsStatus.put(userId.toString()+"_"+key.toString(), new SheetStatisticsStatus((byte)-1, "表格构建异常:第"+row+"行，第"+column+"列数据计算失败"));
				logger.error("表格构建异常:第"+row+"行，第"+column+"列数据计算失败",e);
				throw new BusinessException(OpsErrorMessage.MODULE_NAME,OpsErrorMessage.ERROR_MESSAGE,"表格构建异常:第"+row+"行，第"+column+"列数据计算失败");
			}
		}

		@Override
		public int compareTo(SheetBuildDataExcutor1 o) {
			if(this.priority>o.priority){
				return -1;
			}else if(this.priority<o.priority){
				return 1;
			}
			return 0;
		}
	}

	@Override
	public void addGatherSign(String userId, String sheetId) {
		gatherSign.put(userId+"_"+sheetId,"1");
		
	}


	@Override
	public void removeGatherSign(String userId, String sheetId) {
		gatherSign.remove(userId+"_"+sheetId);
		
	}


	@Override
	public void cancelStatisticsByUserId(UUID userId) {
		Set<String> keySet = concurrentHashMap1.keySet();
		if(!CollectionUtils.isEmpty(keySet)){
			for (String key : keySet) {
				if(key.indexOf(userId.toString()) != -1){
					
					concurrentStatisticsStatus.remove(key);
					concurrentHashMap1.remove(key);
				}
			}
		}
	}


	@Override
	public void removeGatherSignByUserId(UUID userId) {
		Set<String> keySet = gatherSign.keySet();
		if(!CollectionUtils.isEmpty(keySet)){
			for (String key : keySet) {
				if(key.indexOf(userId.toString()) != -1){
					gatherSign.remove(key);
				}
			}
		}
		
	}


	@Override
	public java.util.Map<String, Object> viewSheetSize() {
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("sheetSize", concurrentHashMap1.size());
		int cellSize = 0;
		for (String key : concurrentHashMap1.keySet()) {
			cellSize += concurrentHashMap1.get(key).size();
		}
		map.put("cellSize", cellSize);
		return map;
	} 
}
