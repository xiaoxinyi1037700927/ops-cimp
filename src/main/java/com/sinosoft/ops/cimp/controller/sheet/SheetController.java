/**
 * @Project:      IIMP
 * @Title:          SheetController.java
 * @Copyright: © 2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.controller.sheet;

import com.sinosoft.ops.cimp.controller.BaseEntityController;
import com.sinosoft.ops.cimp.entity.sheet.Sheet;
import com.sinosoft.ops.cimp.service.sheet.SheetDataService;
import com.sinosoft.ops.cimp.service.sheet.SheetParameterService;
import com.sinosoft.ops.cimp.service.sheet.SheetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


/**
 * @ClassName: SheetController
 * @Description: 表格控制器
 * @Author: Nil
 * @Date: 2017年8月18日 下午1:16:08
 * @Version 1.0.0
 */
@Controller("sheetController")
@RequestMapping("sheet")
public class SheetController extends BaseEntityController<Sheet> {
    private static final Logger logger = LoggerFactory.getLogger(SheetController.class);

    private static final int maxComplicate = 100;// 最大的并发数为100
    private static int currentLine = 0;
    private static List<String> userList = new ArrayList<String>();

    @Resource
    private SheetService sheetService = null;

    @Autowired
    private SheetDataService sheetDataService;

    @Autowired
    private SheetParameterService sheetParameterService;

//    @Autowired
//    private SystemUserService systemUserService;
//
//    @Autowired
//    private SheetDesignService sheetDesignService;
//
//    @Autowired
//    private SheetDesignDesignCategoryService sheetDesignDesignCategoryService;
//
//    @Autowired
//    private SheetTagService sheetTagService;
//
//    @Autowired
//    private SheetComService sheetComService;
//
//    @Autowired
//    private SheetGatherInfoService gatherInfoService;
//
//    @ResponseBody
//    @RequestMapping(value = MAPPING_PATH_CREATE)
//    public ResponseResult create(Sheet entity, HttpServletRequest request) {
//        try {
//            String params= request.getParameter("params");
//            UUID sheetId= UUID.randomUUID();
//        	UUID categoryId = UUID.fromString(request.getParameter("categoryId"));
//        	entity.setId(sheetId);
//        	entity.setCreatedBy(UUID.fromString(SecurityUtils.getSubject().getCurrentUser().getId()));
//            entity.setOrdinal(sheetService.getNextOrdinal());
//            sheetService.create(entity, categoryId);
//
//            JSONArray jsonArray = JSON.parseArray(params);
//            Iterator<Object> it =  jsonArray.iterator();
//            SysDataSourceTable datasourcetable = new SysDataSourceTable();
//            while(it.hasNext()){
//                String item = it.next().toString();
//                JSONObject js = (JSONObject) JSON.parse(item);
//                SheetParameter sheetParameter = new SheetParameter();
//                sheetParameter.setId(UUID.randomUUID());
//                sheetParameter.setParameterId(js.get("parameterId").toString());
//                if(js.get("parameterValue")!=null)
//                    sheetParameter.setParameterValue(js.get("parameterValue").toString());
//                sheetParameter.setDescription(js.get("description").toString());
//                sheetParameter.setSheetId(sheetId);
//                sheetParameter.setOrdinal(sheetParameterService.getNextOrdinal());
//                sheetParameter.setStatus((byte)0);
//                sheetParameter.setCreatedTime(new Timestamp(System.currentTimeMillis()));
//                sheetParameter.setLastModifiedTime(new Timestamp(System.currentTimeMillis()));
//                sheetParameterService.create(sheetParameter);
//            }
//            return ResponseResult.success(entity,1,"保存成功！");
//        } catch (Exception e) {
//            logger.error("创建失败！", e);
//            return ResponseResult.failure("保存失败！");
//        }
//    }
//
//    @ResponseBody
//    @RequestMapping("/createBatch")
//    public ResponseResult createBatch(HttpServletRequest request, HttpServletResponse response, Sheet entity,String type,String parameterId) {
//        try {
//        	entity.setCreatedBy(UUID.fromString(SecurityUtils.getSubject().getCurrentUser().getId()));
//        	UUID categoryId = UUID.fromString(request.getParameter("categoryId"));
//            UUID designCategoryId;
//        	//designIdNos格式="designId:No"
//        	String [] designIdNos = request.getParameterValues("designIdNo");
//        	if(request.getParameter("designCategoryId")!=null && request.getParameter("designCategoryId")!="")
//            {
//                designCategoryId = UUID.fromString(request.getParameter("designCategoryId"));
//            }
//            else {
//                SheetDesignDesignCategory sheetDesignDesignCategory = sheetDesignDesignCategoryService.getByDesignId(UUID.fromString(designIdNos[0].split("\\*")[0]));
//                designCategoryId=sheetDesignDesignCategory.getCategoryId();
//            }
//
//            String params= request.getParameter("params");
//        	sheetService.createBatch(entity, categoryId,designCategoryId, designIdNos,params,type,parameterId);
//            return ResponseResult.success(entity,1,"套表成功！");
//        } catch (Exception e) {
//            logger.error("套表失败！", e);
//            return ResponseResult.failure("套表失败！");
//        }
//    }
//
//    @ResponseBody
//    @RequestMapping("/createBatchInCategory")
//    public ResponseResult createBatchInCategory(HttpServletRequest request, HttpServletResponse response, Sheet entity,String type,String parameterId) {
//        try {
//            entity.setCreatedBy(UUID.fromString(SecurityUtils.getSubject().getCurrentUser().getId()));
//            UUID categoryId = UUID.fromString(request.getParameter("categoryId"));
//            UUID designCategoryId;
//            //designIdNos格式="designId:No"
//            String [] designIdNos = request.getParameterValues("designIdNo");
//            if(request.getParameter("designCategoryId")!=null && request.getParameter("designCategoryId")!="")
//            {
//                designCategoryId = UUID.fromString(request.getParameter("designCategoryId"));
//            }
//            else {
//                SheetDesignDesignCategory sheetDesignDesignCategory = sheetDesignDesignCategoryService.getByDesignId(UUID.fromString(designIdNos[0].split("\\*")[0]));
//                designCategoryId=sheetDesignDesignCategory.getCategoryId();
//            }
//
//            String params= request.getParameter("params");
//            sheetService.createBatchInCategory(entity, categoryId,designCategoryId, designIdNos,params,type,parameterId);
//            return ResponseResult.success(entity,1,"套表成功！");
//        } catch (Exception e) {
//            logger.error("套表失败！", e);
//            return ResponseResult.failure("套表失败！");
//        }
//    }
//
//    @ResponseBody
//    @RequestMapping(value = MAPPING_PATH_UPDATE)
//    public ResponseResult update(Sheet entity,HttpServletRequest request) {
//        try {
//        	entity.setLastModifiedBy(UUID.fromString(SecurityUtils.getSubject().getCurrentUser().getId()));
//        	sheetService.updateOne(entity);
//
//            String params= request.getParameter("params");
//            JSONArray jsonArray = JSON.parseArray(params);
//            Iterator<Object> it =  jsonArray.iterator();
//            SysDataSourceTable datasourcetable = new SysDataSourceTable();
//            while(it.hasNext()){
//                String item = it.next().toString();
//                JSONObject js = (JSONObject) JSON.parse(item);
//                SheetParameter sheetParameter = new SheetParameter();
//                if(js.containsKey("id"))
//                {
//                    sheetParameter.setId(UUID.fromString(js.get("id").toString()));
//                }
//                else
//                {
//                    sheetParameter.setId(UUID.randomUUID());
//                }
//
//                sheetParameter.setParameterId(js.get("parameterId").toString());
//                sheetParameter.setParameterValue(js.get("parameterValue").toString());
//                sheetParameter.setDescription(js.get("description").toString());
//                sheetParameter.setSheetId(entity.getId());
//                sheetParameter.setOrdinal(Integer.parseInt(js.get("ordinal").toString()));
//                sheetParameter.setStatus(Byte.parseByte(js.get("status").toString()));
//                sheetParameter.setCreatedTime(Timestamp.valueOf(js.get("createdTime").toString()));
//                sheetParameter.setLastModifiedTime(new Timestamp(System.currentTimeMillis()));
//                sheetParameterService.update(sheetParameter);
//            }
//
//            return ResponseResult.success(entity,1,"保存成功！");
//        } catch (Exception e) {
//            logger.error("更新失败！", e);
//            return ResponseResult.failure("保存失败！");
//        }
//    }
//
//    @ResponseBody
//    @RequestMapping(value = MAPPING_PATH_DELETE)
//    public ResponseResult delete(Sheet entity,HttpServletRequest request) {
//        try {
//        	UUID categoryId = UUID.fromString(request.getParameter("categoryId"));
//        	entity.setLastModifiedBy(UUID.fromString(SecurityUtils.getSubject().getCurrentUser().getId()));
//        	int result  = sheetService.delete(entity, categoryId);
//        	if (result == -1) {
//                return ResponseResult.failure("删除失败！该报表下有数据，不能删除！");
//        	} else {
//        		return ResponseResult.success(entity,1,"删除成功！");
//        	}
//        } catch (Exception e) {
//            logger.error("删除失败！", e);
//            return ResponseResult.failure("删除失败！");
//        }
//    }
//
//    @ResponseBody
//
//    @RequestMapping(value = MAPPING_PATH_DELETE_BY_ID)
//    public ResponseResult deleteById(HttpServletRequest request) {
//        try {
//            UUID uuid=UUID.fromString(request.getParameter("id"));
//            sheetService.deleteById(uuid);
//            return ResponseResult.success(null,1,"删除成功！");
//        } catch (OperationNotAllowedException e) {
//            return ResponseResult.failure(ResponseResult.ERROR_FAILED,e.getMessage());
//        } catch (Exception e) {
//            logger.error("删除失败！", e);
//            return ResponseResult.failure("删除失败！");
//        }
//    }
//
//    @ResponseBody
//    @RequestMapping("/deleteByIds")
//    public ResponseResult deleteByIds(String[] ids) {
//        try {
//            sheetService.deleteByIds(ids);
//            return ResponseResult.success(null,1,"删除成功！");
//        } catch (OperationNotAllowedException e) {
//            return ResponseResult.failure(ResponseResult.ERROR_FAILED,e.getMessage());
//        } catch (Exception e) {
//            logger.error("删除失败！", e);
//            return ResponseResult.failure("删除失败！");
//        }
//    }
//
//    @ResponseBody
//    @RequestMapping("/updateFlgByIds")
//    public ResponseResult updateFlgByIds(HttpServletRequest request, String[] ids,String flg) {
//        try {
//            sheetService.updateFlagByIds(ids,Integer.parseInt(flg),getStringParam(request, "notation", null),UUID.fromString(SecurityUtils.getSubject().getCurrentUser().getId()),getCurrentLoggedInUser().getOrganizationId());
//            return ResponseResult.success(null,1,"操作成功！");
//        } catch (OperationNotAllowedException e) {
//            return ResponseResult.failure(ResponseResult.ERROR_FAILED,e.getMessage());
//        } catch (Exception e) {
//            logger.error("操作失败！", e);
//            return ResponseResult.failure("操作失败！");
//        }
//    }
//
//    @ResponseBody
//    @RequestMapping("/archiveByIds")
//    public ResponseResult archiveByIds(String categoryId, String[] ids) {
//        try {
//            UUID uuid=UUID.fromString(categoryId);
//            int count=sheetService.archiveByIds(uuid,ids,UUID.fromString(SecurityUtils.getSubject().getCurrentUser().getId()),getCurrentLoggedInUser().getOrganizationId());
//            return ResponseResult.success(count,1,"归档成功！");
//        } catch (OperationNotAllowedException e) {
//            return ResponseResult.failure(ResponseResult.ERROR_FAILED,e.getMessage());
//        } catch (Exception e) {
//            logger.error("归档失败！", e);
//            return ResponseResult.failure("归档失败！");
//        }
//    }
//    @ResponseBody
//    @RequestMapping("/unarchiveByIds")
//    public ResponseResult unarchiveByIds(String categoryId,String[] ids) {
//        try {
//            UUID uuid=UUID.fromString(categoryId);
//            int count=sheetService.unarchiveByIds(uuid,ids,UUID.fromString(SecurityUtils.getSubject().getCurrentUser().getId()),getCurrentLoggedInUser().getOrganizationId());
//            return ResponseResult.success(count,1,"取消归档成功！");
//        } catch (OperationNotAllowedException e) {
//            return ResponseResult.failure(ResponseResult.ERROR_FAILED,e.getMessage());
//        } catch (Exception e) {
//            logger.error("取消归档失败！", e);
//            return ResponseResult.failure("取消归档失败！");
//        }
//    }
//
//    @ResponseBody
//
//    @RequestMapping(value = MAPPING_PATH_GET_BY_ID)
//    public ResponseResult getById(HttpServletRequest request) {
//        try {
//            Sheet entity = sheetService.getById(getUUIDParam(request, "id", null));
//            if (entity != null) {
//                entity.setParameters(sheetParameterService.getBySheetId(entity.getId()));
//                return ResponseResult.success(entity, 1, "获取成功！");
//            } else {
//            	return ResponseResult.failure("获取失败！");
//            }
//        } catch (Exception e) {
//            logger.error("获取失败！", e);
//            return ResponseResult.failure("获取失败！");
//        }
//    }
//
////
////    public ResponseResult findByPage(HttpServletRequest request, HttpServletResponse response) {
////        try {
////            PageableQueryParameter queryParameter = new PageableQueryParameter();
////            queryParameter.setPageNo(getIntegerParam(request, "page", 1));
////            queryParameter.setPageSize(getIntegerParam(request, "limit", Constants.DEFAULT_PAGE_SIZE));
////            String keyword = request.getParameter("keyword");
////            if (keyword != null) {
////                queryParameter.getParameters().put("keyword", keyword);
////            }
////            queryParameter.getParameters().put("categoryId",this.getUUIDParam(request, "categoryId", UUID.randomUUID()));
////
////            PageableQueryResult queryResult = sheetService.findByPage(queryParameter);
////            return ResponseResult.success(queryResult.getData(), queryResult.getTotalCount());
////        } catch (Exception e) {
////            logger.error("查询数据失败！", e);
////            return ResponseResult.failure("查询数据失败！");
////        }
////    }
////
//    @ResponseBody
//    @RequestMapping("/getSummaryData")
//    public ResponseResult getSummaryData(HttpServletRequest request, HttpServletResponse response) {
//        try {
//            Map queryParameter = new HashMap();
//            List<UUID> ids =  new ArrayList<>();
//            for(String temp :request.getParameterValues("id"))
//            {
//                ids.add(UUID.fromString(temp));
//            }
//
//            List<UUID> lstDesignIds =  new ArrayList<>();
//            for(String temp :request.getParameterValues("designId"))
//            {
//                lstDesignIds.add(UUID.fromString(temp));
//            }
//
//            List<UUID> lstCategoryIds =  new ArrayList<>();
//            for(String temp :request.getParameterValues("categoryId"))
//            {
//                lstCategoryIds.add(UUID.fromString(temp));
//            }
//            queryParameter.put("id",ids);
//            queryParameter.put("design_id",lstDesignIds);
//            queryParameter.put("category_id",lstCategoryIds);
//            if(request.getParameter("containSelf")!=null)
//            {
//                queryParameter.put("containSelf",request.getParameter("containSelf"));
//            }
//            else
//            {
//                queryParameter.put("containSelf","2");
//            }
//
//            List<Map> lst = sheetService.getSummaryData(queryParameter);
//            return ResponseResult.success(lst, lst.size());
//        } catch (Exception e) {
//            logger.error("查询数据失败！", e);
//            return ResponseResult.failure("查询数据失败！");
//        }
//    }
//
//    @ResponseBody
//
//    @RequestMapping(value = MAPPING_PATH_FIND_BY_PAGE)
//    public ResponseResult findByPage(HttpServletRequest request) {
//        try {
//        	String includeDown = (String)request.getParameter("includeDown");
//        	Collection<UUID> collIds = null;
//            PageableQueryParameter queryParameter = new PageableQueryParameter();
//            queryParameter.setPageNo(getIntegerParam(request, "page", 1));	//1
//            queryParameter.setPageSize(getIntegerParam(request, "limit", Constants.DEFAULT_PAGE_SIZE));	//Constants.DEFAULT_PAGE_SIZE
//            Enumeration<String> keys = request.getParameterNames();
//            if (keys != null) {
//            	while(keys.hasMoreElements()){
//            	    String name = (String)keys.nextElement();
//            	    if ("categoryId".equals(name)) {
//            	    	if ("1".equals(includeDown)) {
//            	    		//包含子目录
//            	    		collIds = sheetService.getDownCatigories(this.getUUIDParam(request, name, UUID.randomUUID()));
//                	        queryParameter.getParameters().put(name, collIds);
//            	    	} else {
//            	    		//不包含子目录
//                	        queryParameter.getParameters().put(name, this.getUUIDParam(request, name, UUID.randomUUID()));
//            	    	}
//              	    }
//              	    else if ("designId".equals(name)) {
//                        queryParameter.getParameters().put(name, this.getUUIDParam(request, name, UUID.randomUUID()));
//                    }
//              	    else if ("type".equals(name)) {
//              	    	String[] typeArray = request.getParameterValues(name);
//              	    	Collection<Byte> typeCol = new HashSet<Byte>();
//              	    	if (typeArray != null && typeArray.length > 0) {
//                  	    	for (String aType : typeArray) {
//                  	    		typeCol.add(Byte.parseByte(aType));
//                  	    	}
//              	    	}
//                        queryParameter.getParameters().put(name, typeCol);
//              	    } else if ("status".equals(name)) {
//                        queryParameter.getParameters().put(name, this.getByteParam(request, name, (byte)-1));
//              	    } else if ("dataFillType".equals(name)) {
//                        queryParameter.getParameters().put(name, this.getByteParam(request, name, (byte)-1));
//              	    } else if ("summary".equals(name)) {
//                        queryParameter.getParameters().put(name, this.getBooleanParam(request, name));
//              	    } else if ("auditStatus".equals(name)) {
//                        queryParameter.getParameters().put(name, this.getByteParam(request, name, (byte)-1));
//              	    } else if ("beingEdited".equals(name)) {
//                        queryParameter.getParameters().put(name, this.getBooleanParam(request, name));
//              	    } else if ("hasData".equals(name)) {
//                        queryParameter.getParameters().put(name, this.getIntegerParam(request, name, (int)-1));
//              	    } else if("parameterId".equals(name)){
//              	    	queryParameter.getParameters().put(name, this.getByteParam(request, name,(byte)1));
//              	    }else if ("includeDown".equals(name)) {
//              	    } else if ("page".equals(name)) {
//                    } else if ("start".equals(name)) {
//              	    } else if ("limit".equals(name)) {
//              	    } else if ("_dc".equals(name)) {
//              	    } else {
//                        queryParameter.getParameters().put(name, (String)request.getParameter(name));
//             	    }
//            	}
//            }
//            //添加用户权限条件
//            Byte param = this.getByteParam(request, "parameterId", (byte)1);
//            if(!"sa".equals(this.getCurrentLoggedInUser().getLoginName())){
//
//            	Collection<String> organizationIds=null;
//            	// 1 单位   2 党组织
//            	if(param==1){
//            		organizationIds = systemUserService.getTreeOrganizationIds(this.UUID.fromString(SecurityUtils.getSubject().getCurrentUser().getId()), "DepTree");
//
//            	}else if(param==2){
//            		organizationIds = systemUserService.getTreeOrganizationIds(this.UUID.fromString(SecurityUtils.getSubject().getCurrentUser().getId()), "PartyTree");
//            	}else{
//            		organizationIds=new ArrayList<>();
//            	}
//            	queryParameter.getParameters().put("depIds", organizationIds);
//            	logger.debug("报表列表 param："+param);
//            	logger.debug("报表列表 userId："+this.UUID.fromString(SecurityUtils.getSubject().getCurrentUser().getId()).toString());
//            	StringBuilder sb = new StringBuilder();
//            	for (String orgId : organizationIds) {
//					sb.append(orgId+"-");
//				}
//            	logger.debug("报表列表 organizationIds："+sb.toString());
//            }
//            queryParameter.getParameters().put("parameterId", param);
//            PageableQueryResult queryResult = sheetService.findByPage(queryParameter);
//            return ResponseResult.success(queryResult.getData(), queryResult.getTotalCount());
//        } catch (Exception e) {
//            logger.error("查询数据失败！用户id为："+this.UUID.fromString(SecurityUtils.getSubject().getCurrentUser().getId()), e);
//            return ResponseResult.failure("查询数据失败！");
//        }
//    }
//
//    /**
//     * 上移节点
//     * @param request
//     * @param response
//     */
//    @ResponseBody
//    @RequestMapping("/moveUp")
//    public ResponseResult moveUp(HttpServletRequest request, HttpServletResponse response, Sheet entity) {
//        try{
//        	UUID categoryId = UUID.fromString(request.getParameter("categoryId"));
//        	entity.setLastModifiedBy(UUID.fromString(SecurityUtils.getSubject().getCurrentUser().getId()));
//            boolean success = sheetService.up(entity, categoryId);
//            if (success) {
//            	return ResponseResult.success(entity,1,"上移成功！");
//            } else {
//                return ResponseResult.failure("上移失败！");
//            }
//        } catch (Exception e) {
//            logger.error("上移失败！", e);
//            return ResponseResult.failure("上移失败！");
//        }
//    }
//
//    /**
//     * 下移节点
//     * @param request
//     * @param response
//     */
//    @ResponseBody
//    @RequestMapping("/moveDown")
//    public ResponseResult moveDown(HttpServletRequest request, HttpServletResponse response, Sheet entity) {
//        try{
//        	UUID categoryId = UUID.fromString(request.getParameter("categoryId"));
//        	entity.setLastModifiedBy(UUID.fromString(SecurityUtils.getSubject().getCurrentUser().getId()));
//            boolean success = sheetService.down(entity, categoryId);
//            if (success) {
//            	return ResponseResult.success(entity,1,"下移成功！");
//            } else {
//                return ResponseResult.failure("下移失败！");
//            }
//        } catch (Exception e) {
//            logger.error("下移失败！", e);
//            return ResponseResult.failure("下移失败！");
//        }
//    }
//
//    @RequestMapping(value = "/getSheetType")
//    public void getSheetType(HttpServletRequest request, HttpServletResponse response) {
//        try {
//            Collection<Map<String,String>> collection = sheetService.getSheetType();
//            writeJson(response, ResponseResult.success(collection, collection.size()));
//        } catch (Exception e) {
//            logger.error("获取失败！", e);
//            writeJson(response, ResponseResult.failure("获取失败！"));
//        }
//    }
//
//    @RequestMapping(value = "/calculate")
//    public void calculate(HttpServletRequest request, HttpServletResponse response) {
//        UUID userId = UUID.randomUUID();
//        String sheetNo = request.getParameter("sheetNo");
//        System.out.println("-----sheetNo=" + sheetNo);
//        Map<String, Object> params = new HashMap<String, Object>();
//        params.put("OrganizationId ", "123");
//        params.put("BeginTime", "2017-01-01");
//        params.put("EndTime", "2017-12-31");
//        params.put("organizationId ", "123");
//        System.out.println("----sheetService=" + sheetService + " this=" + this + " request=" + request);
//        sheetService.calculate(sheetNo, params, userId);
//    }
//
//    /**汇总报表*/
//    @RequestMapping("/gatherSheet")
//    @ResponseBody
//    public ResponseResult gatherSheet(HttpServletRequest request,
//    		HttpServletResponse response,String id){
//    	String[] sheetIds = request.getParameterValues("sheetIds");
//    	String isAppend = request.getParameter("isAppend");
//    	try {
//    		Assert.isTrue(!ArrayUtils.isEmpty(sheetIds), "没有选择要汇总的报表");
//
//        	if("true".equals(isAppend)){
//        		List<UUID> gatherIds = gatherInfoService.getGatherIdsBySheetId(UUID.fromString(id));
//        		if(!CollectionUtils.isEmpty(gatherIds)){
//        			List<String> gatherIdsStr = new ArrayList<>();
//        			for (UUID gatherId : gatherIds) {
//        				gatherIdsStr.add(gatherId.toString());
//					}
//        			for (String sheetId : sheetIds) {
//						if(!gatherIdsStr.contains(sheetId)){
//							gatherIdsStr.add(sheetId);
//						}
//					}
//        			sheetIds =  gatherIdsStr.toArray(sheetIds);
//        		}
//        	}
//			//if(!ArrayUtils.isEmpty(sheetIds) && sheetId.length()>=1){
//				Map<String,Map<String,SheetData>> dataMap = new HashMap<>();
//
//				for (String sheetId : sheetIds) {
//					Collection<SheetData> sheetDatas = sheetDataService.getBySheetId(UUID.fromString(sheetId));
//					Map<String, SheetData> data = new HashMap<>();
//					for (SheetData sheetData : sheetDatas) {
//						data.put(sheetData.getRowNo()+"-"+sheetData.getColumnNo(),sheetData);
//					}
//					dataMap.put(sheetId,data);
//				}
//				Collection<SheetData> gatherDatas = sheetService.gatherSheet(dataMap,id,sheetIds);
//				Sheet sheet = sheetService.getById(UUID.fromString(id));
//				//添加汇总标记
//				sheetComService.addGatherSign(UUID.fromString(SecurityUtils.getSubject().getCurrentUser().getId()).toString(),id);
//				List<Map<String,Object>> list = sheetComService.formulaCaculation(id, sheet.getDesignId().toString(),UUID.fromString(SecurityUtils.getSubject().getCurrentUser().getId()), (List<SheetData>)gatherDatas);
//				List<Map<String,Object>> list2 = sheetComService.fillUnit(sheet.getDesignId(), UUID.fromString(id));
//				list.addAll(list2);
//				gatherDatas.clear();
//				for (Map<String, Object> map : list) {
//					map.put("sheetId", UUID.fromString(id));
//					SheetData sheetData = JSON.toJavaObject((JSON)map,SheetData.class);
//
//					gatherDatas.add(sheetData);
//				}
//
//				//汇总批注
//				Map<String, String> tagMap = new HashMap<>();
//				for (String sheetId : sheetIds) {
//					Collection<SheetTag> collection = sheetTagService.getBySheetId(UUID.fromString(sheetId));
//					for (SheetTag sheetTag : collection) {
//						String position = sheetTag.getRowNo()+"-"+sheetTag.getColumnNo();
//						if(StringUtils.isBlank(tagMap.get(position))){
//							tagMap.put(position,sheetTag.getName());
//						}else{
//							tagMap.put(position, tagMap.get(position)+(StringUtils.isNotBlank(sheetTag.getName())?"\r\n"+sheetTag.getName():""));
//						}
//					}
//				}
//				List<SheetTag> sheetTags = new ArrayList<>();
//				for (String key : tagMap.keySet()) {
//					SheetTag tag = new SheetTag();
//					tag.setName(tagMap.get(key));
//					tag.setId(UUID.randomUUID());
//					tag.setSheetId(UUID.fromString(id));
//					tag.setRowNo(Integer.parseInt(key.split("-")[0]));
//					tag.setColumnNo(Integer.parseInt(key.split("-")[1]));
//					Map<String, Integer> position = sheetComService.getCtrlPositionByRelativePosition(sheet.getDesignId(), Integer.parseInt(key.split("-")[0]),
//							Integer.parseInt(key.split("-")[1]));
//					tag.setCtrlRowNo(position.get("rowNo"));
//					tag.setCtrlColumnNo(position.get("columnNo"));
//					addTrackData(tag);
//					sheetTags.add(tag);
//				}
//				Map<String, Object> gather = new HashMap<>();
//				gather.put("gatherDatas", gatherDatas);
//				gather.put("sheetTags", sheetTags);
//			//}
//			//移除汇总标记
//			sheetComService.removeGatherSign(UUID.fromString(SecurityUtils.getSubject().getCurrentUser().getId()).toString(),id);
//			return ResponseResult.success(gather, gather.size(), "汇总报表成功");
//		} catch (Exception e) {
//			e.printStackTrace();
//			logger.error("汇总报表失败",e);
//			if(e instanceof IllegalArgumentException) return ResponseResult.failure(e.getMessage());
//			return ResponseResult.failure("汇总报表失败");
//		}
//    }
//
//
//
//    /**汇总报表*/
//    @RequestMapping("/gatherSheetByIds")
//    @ResponseBody
//    public ResponseResult gatherSheetByIds(HttpServletRequest request,
//                                      HttpServletResponse response,String id){
//        String[] sheetIds = request.getParameterValues("sheetIds");
//        String isAppend = request.getParameter("isAppend");
//
//        try {
//        	//添加汇总标记
//    		sheetComService.addGatherSign(UUID.fromString(SecurityUtils.getSubject().getCurrentUser().getId()).toString(),id);
//            Assert.isTrue(!ArrayUtils.isEmpty(sheetIds), "没有选择要汇总的报表");
//
//        	if("true".equals(isAppend)){
//        		List<UUID> gatherIds = gatherInfoService.getGatherIdsBySheetId(UUID.fromString(id));
//        		if(!CollectionUtils.isEmpty(gatherIds)){
//        			List<String> gatherIdsStr = new ArrayList<>();
//        			for (UUID gatherId : gatherIds) {
//        				gatherIdsStr.add(gatherId.toString());
//					}
//        			for (String sheetId : sheetIds) {
//						if(!gatherIdsStr.contains(sheetId)){
//							gatherIdsStr.add(sheetId);
//						}
//					}
//        			sheetIds =  gatherIdsStr.toArray(sheetIds);
//        		}
//        	}
//            //if(!ArrayUtils.isEmpty(sheetIds) && sheetId.length()>=1){
//            Map<String,Map<String,SheetData>> dataMap = new HashMap<>();
//
//            for (String sheetId : sheetIds) {
//                Collection<SheetData> sheetDatas = sheetDataService.getBySheetId(sheetId);
//                Map<String, SheetData> data = new HashMap<>();
//                for (SheetData sheetData : sheetDatas) {
//                    data.put(sheetData.getRowNo()+"-"+sheetData.getColumnNo(),sheetData);
//                }
//                dataMap.put(sheetId,data);
//            }
//
//            Collection<SheetData> gatherDatas = sheetService.gatherSheet(dataMap,id,sheetIds);
//            Sheet sheet = sheetService.getById(UUID.fromString(id));
//            List<Map<String,Object>> list = sheetComService.formulaCaculation(id, sheet.getDesignId().toString(),UUID.fromString(SecurityUtils.getSubject().getCurrentUser().getId()), (List<SheetData>)gatherDatas);
//			List<Map<String,Object>> list2 = sheetComService.fillUnit(sheet.getDesignId(), UUID.fromString(id));
//			list.addAll(list2);
//			gatherDatas.clear();
//			for (Map<String, Object> map : list) {
//				map.put("sheetId", UUID.fromString(id));
//				SheetData sheetData = JSON.toJavaObject((JSON)map,SheetData.class);
//
//				gatherDatas.add(sheetData);
//			}
//
//
//			List<SheetGatherInfo> sheetGatherInfos = new ArrayList<>();
//			for (String sheetId : sheetIds) {
//				SheetGatherInfo gatherInfo = new SheetGatherInfo();
//				gatherInfo.setId(UUID.randomUUID());
//				gatherInfo.setSheetId(UUID.fromString(id));
//				gatherInfo.setGatherSheetId(UUID.fromString(sheetId));
//				addTrackData(gatherInfo);
//				sheetGatherInfos.add(gatherInfo);
//			}
//
//			for (SheetData sheetData : gatherDatas) {
//				sheetData.setId(UUID.randomUUID());
//				addTrackData(sheetData);
//
//			}
//
//			//汇总批注
//			Map<String, String> tagMap = new HashMap<>();
//			for (String sheetId : sheetIds) {
//				Collection<SheetTag> collection = sheetTagService.getBySheetId(UUID.fromString(sheetId));
//				for (SheetTag sheetTag : collection) {
//					String position = sheetTag.getRowNo()+"-"+sheetTag.getColumnNo();
//					if(StringUtils.isBlank(tagMap.get(position))){
//						tagMap.put(position,sheetTag.getName());
//					}else{
//						tagMap.put(position, tagMap.get(position)+(StringUtils.isNotBlank(sheetTag.getName())?"\r\n"+sheetTag.getName():""));
//					}
//				}
//			}
//			List<SheetTag> sheetTags = new ArrayList<>();
//			for (String key : tagMap.keySet()) {
//				SheetTag tag = new SheetTag();
//				tag.setName(tagMap.get(key));
//				tag.setId(UUID.randomUUID());
//				tag.setSheetId(UUID.fromString(id));
//				tag.setRowNo(Integer.parseInt(key.split("-")[0]));
//				tag.setColumnNo(Integer.parseInt(key.split("-")[1]));
//				addTrackData(tag);
//				sheetTags.add(tag);
//			}
//
//			sheetService.saveInfosAndDatas(UUID.fromString(id), gatherDatas, sheetGatherInfos,sheetTags,false);
//            //}
//			//移除汇总标记
//			sheetComService.removeGatherSign(UUID.fromString(SecurityUtils.getSubject().getCurrentUser().getId()).toString(),id);
//            return ResponseResult.success(gatherDatas, gatherDatas.size(), "汇总报表成功");
//        } catch (Exception e) {
//            e.printStackTrace();
//            logger.error("汇总报表失败",e);
//            if(e instanceof IllegalArgumentException) return ResponseResult.failure(e.getMessage());
//            return ResponseResult.failure("汇总报表失败");
//        }
//    }
//
//    /**批量保存汇总信息和报表数据*/
//    @SuppressWarnings("unchecked")
//	@RequestMapping("/saveInfosAndDatas")
//    @ResponseBody
//    public ResponseResult saveInfosAndDatas(HttpServletRequest request,
//    		HttpServletResponse response,@RequestBody Map<String,Object> infosAndDatas){
//
//    	try {
//			List<Map<String,Object>> infos = (List)infosAndDatas.get("infos");
//			List<Map<String,Object>> datas = (List)infosAndDatas.get("datas");
//			List<Map<String,Object>> tags = (List)infosAndDatas.get("tags");
//			String id = (String)infosAndDatas.get("id");
//
//            Sheet sheet = sheetService.getById(UUID.fromString(id));
//            if(sheet!=null) {
//                boolean archived=(sheet.getArchived()==null)?false:sheet.getArchived();
//                if(archived) {//已归档
//                    return ResponseResult.failure("不允许修改已归档的报表！");
//                }
//            }
//
//			//保存汇总信息
//			List<SheetGatherInfo> sheetGatherInfos = new ArrayList<>();
//			if(!CollectionUtils.isEmpty(infos)){
//				for (Map<String,Object> map : infos) {
//					String sheetId = (String)map.get("sheetId");
//					String gatherSheetId = (String)map.get("gatherSheetId");
//					map.put("sheetId", UUID.fromString(sheetId));
//					map.put("gatherSheetId", UUID.fromString(gatherSheetId));
//					String lastModifiedBy = (String)map.get("lastModifiedBy");
//					String createdBy = (String)map.get("createdBy");
//					if(StringUtils.isNotBlank(lastModifiedBy))
//						map.put("lastModifiedBy",UUID.fromString(lastModifiedBy));
//					if(StringUtils.isNotBlank(createdBy))
//						map.put("createdBy",UUID.fromString(lastModifiedBy));
//					JSONObject json = new JSONObject(map);
//					SheetGatherInfo info = json.toJavaObject(SheetGatherInfo.class);
//					info.setId(UUID.randomUUID());
//					addTrackData(info);
//					sheetGatherInfos.add(info);
//					//sheetGatherInfoService.create(info);
//				}
//			}
//
//			//保存表格数据
//			List<SheetData> sheetDatas = new ArrayList<>();
//			for (Map<String,Object> map : datas) {
//				String sheetId = (String)map.get("sheetId");
//				map.put("sheetId", UUID.fromString(sheetId));
//				String lastModifiedBy = (String)map.get("lastModifiedBy");
//				String createdBy = (String)map.get("createdBy");
//				if(StringUtils.isNotBlank(lastModifiedBy))
//					map.put("lastModifiedBy",UUID.fromString(lastModifiedBy));
//				if(StringUtils.isNotBlank(createdBy))
//					map.put("createdBy",UUID.fromString(lastModifiedBy));
//				JSONObject json = new JSONObject(map);
//				SheetData data = json.toJavaObject(SheetData.class);
//				data.setId(UUID.randomUUID());
//				addTrackData(data);
//				sheetDatas.add(data);
//				//sheetDataService.create(data);
//			}
//
//			//保存批注信息
//			List<SheetTag> sheetTags = new ArrayList<>();
//			for (Map<String, Object> map : tags) {
//				JSONObject json = new JSONObject(map);
//				SheetTag sheetTag = json.toJavaObject(SheetTag.class);
//				sheetTag.setId(UUID.randomUUID());
//				sheetTag.setSheetId(UUID.fromString(id));
//				addTrackData(sheetTag);
//				sheetTags.add(sheetTag);
//			}
//			sheetService.saveInfosAndDatas(UUID.fromString(id),sheetDatas,sheetGatherInfos,sheetTags,false);
//			return ResponseResult.success( "保存汇总报表成功");
//		} catch (Exception e) {
//			e.printStackTrace();
//			logger.error("保存汇总报表失败",e);
//			return ResponseResult.failure("保存汇总报表失败");
//		}
//    }
//
//    /**汇总数据反查*/
//    @RequestMapping("/gatherDataCheck")
//    @ResponseBody
//    public ResponseResult gatherDataCheck(String id,Integer rowNo,Integer columnNo){
//    	try {
//			List<Map<String,Object>> gatherInfos = sheetService.getGatherInfos(UUID.fromString(id),rowNo,columnNo);
//			return ResponseResult.success(gatherInfos, gatherInfos.size(), "获取汇总反查数据成功");
//		} catch (Exception e) {
//			logger.error("获取汇总反查数据失败",e);
//			return ResponseResult.failure("获取汇总反查数据失败");
//		}
//    }
//
//    /**
//     * 获取参数项信息
//     */
//    @RequestMapping("/getParameterInfoByDesginId")
//    @ResponseBody
//    public ResponseResult getParameterInfoByDesginId(String designId){
//    	try {
//    		 Collection<Map<String,Object>> parameterInfo = sheetService.getParameterInfoByDesgignId(UUID.fromString(designId));
//			 return ResponseResult.success(parameterInfo, parameterInfo.size(),"获取参数信息成功");
//		} catch (Exception e) {
//			logger.error("获取参数信息失败",e);
//			return ResponseResult.failure("获取参数信息失败");
//		}
//    }
//
//
//    /**导出汇总反查数据*/
//    @RequestMapping("/exportGatherData")
//    public void exportGatherData(HttpServletRequest request,
//            HttpServletResponse response,String id,String designId,Integer rowNo,Integer columnNo){
//    	String sessionId = request.getSession().getId();
//    	try {
//    		if(currentLine>maxComplicate){
//    			writeJson(response, ResponseResult.failure("超过最大并发数"));
//    			return;
//    		}
//    		if(userList.contains(sessionId)){
//    			writeJson(response, ResponseResult.failure("正在下载..."));
//    			return;
//    		}
//    		userList.add(sessionId);
//    		currentLine++;
//    		Collection<Map<String,Object>> parameterInfos = sheetService.getParameterInfoByDesgignId(UUID.fromString(designId));
//			List<Map<String,Object>> gatherInfos = sheetService.getGatherInfos(UUID.fromString(id), rowNo, columnNo);
//			sheetService.exportGatherData(response,parameterInfos,gatherInfos);
//			//writeJson(response, ResponseResult.success("下载成功"));
//		} catch (Exception e) {
//			logger.error("导出汇总反查数据失败",e);
//			writeJson(response, ResponseResult.failure("下载失败"));
//		}finally {
//			currentLine--;
//			if(userList.contains(sessionId))
//				userList.remove(sessionId);
//		}
//    }
//
//    /**
//     * 根据designId获取报表
//     */
//    @RequestMapping("/getByDesignId")
//    @ResponseBody
//    public ResponseResult getByDesignId(HttpServletRequest request,
//            HttpServletResponse response,String designId){
//    	try {
//			Collection<Sheet> collection = sheetService.getByDesignId(UUID.fromString(designId));
//			return ResponseResult.success(collection, collection.size(), "根据designId获取报表成功");
//		} catch (Exception e) {
//			logger.error("根据designId获取报表失败",e);
//			return ResponseResult.failure("根据designId获取报表失败");
//		}
//    }
//
//    @ResponseBody
//    @RequestMapping("/getRefSituation")
//    public ResponseResult getRefSituation(HttpServletRequest request, HttpServletResponse response) {
//        try {
//            String id =request.getParameter("id");
//            List<Map> list = sheetService.getRefSituation(id);
//            return ResponseResult.success(list, list.size(), "执行成功！");
//        }
//        catch (Exception e) {
//            logger.error("getRefSituation error:{}", Throwables.getStackTraceAsString(e));
//            return ResponseResult.failure(0, "执行失败！");
//        }
//    }
//
//    @ResponseBody
//    @RequestMapping("/checkLevel")
//    public ResponseResult checkLevel(HttpServletRequest request, HttpServletResponse response) {
//        try {
//            String loginName = this.getCurrentLoggedInUser().getLoginName();
//            String sheetId = request.getParameter("sheetId");
//            String type= request.getParameter("type");
//            int iResult= sheetService.checkLevel(loginName,sheetId,type);
//            return ResponseResult.success(iResult, 1, "执行成功！");
//        }
//        catch (Exception e) {
//            logger.error("getRefSituation error:{}", Throwables.getStackTraceAsString(e));
//            return ResponseResult.failure(0, "执行失败！");
//        }
//    }
}
