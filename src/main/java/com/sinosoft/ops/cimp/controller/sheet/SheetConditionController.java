package com.sinosoft.ops.cimp.controller.sheet;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Throwables;
import com.sinosoft.ops.cimp.common.model.DataStatus;
import com.sinosoft.ops.cimp.common.model.PageableQueryParameter;
import com.sinosoft.ops.cimp.common.model.PageableQueryResult;
import com.sinosoft.ops.cimp.common.model.ResponseResult;
import com.sinosoft.ops.cimp.controller.BaseController;
import com.sinosoft.ops.cimp.entity.sheet.SheetCondition;
import com.sinosoft.ops.cimp.entity.sheet.SheetConditionItem;
import com.sinosoft.ops.cimp.entity.sheet.SysFunction;
import com.sinosoft.ops.cimp.entity.user.User;
import com.sinosoft.ops.cimp.exception.BusinessException;
import com.sinosoft.ops.cimp.service.sheet.SheetConditionItemService;
import com.sinosoft.ops.cimp.service.sheet.SheetConditionService;
import com.sinosoft.ops.cimp.service.sheet.SysFunctionService;
import com.sinosoft.ops.cimp.util.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.*;

/**
 * @ClassName: SheetConditionItemGroupController
 * @description: 条件项控制类
 */
@Controller("sheetConditionController")
@RequestMapping("sheet/sheetCondition")
public class SheetConditionController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(SheetConditionController.class);

    @Autowired
    private SheetConditionService sheetConditionService;

    @Autowired
    private SheetConditionItemService sheetConditionItemService;
    @Resource
    private SysFunctionService sysFunctionService = null;

    @ResponseBody
    @RequestMapping("/getFunctionList")
    public ResponseResult getFunctionList(HttpServletRequest request, HttpServletResponse response) {
        try {
            List<SysFunction> sysFunctions = sysFunctionService.findAll();
            return ResponseResult.success(sysFunctions, sysFunctions.size(), "执行成功！");
        } catch (Exception e) {
            logger.error("getFunctionList error:{}", Throwables.getStackTraceAsString(e));
            return ResponseResult.failure(0, "执行失败！");
        }
    }

    @ResponseBody
    @RequestMapping("/getConditionByCategoryId")
    public ResponseResult getConditionByCategoryId(HttpServletRequest request, HttpServletResponse response) {
        try {
            String categoryId = request.getParameter("categoryId");
            List<SheetCondition> sheetConditions = sheetConditionService.getConditionByCategoryId(categoryId);
            return ResponseResult.success(sheetConditions, sheetConditions.size(), "执行成功！");
        } catch (Exception e) {
            logger.error("getConditionByCategoryId error:{}", Throwables.getStackTraceAsString(e));
            return ResponseResult.failure(0, "执行失败！");
        }
    }

    @ResponseBody
    @RequestMapping("/getConditionByDesignId")
    public ResponseResult getConditionByDesignId(HttpServletRequest request, HttpServletResponse response) {
        try {
            String designId = request.getParameter("designId");
            List<SheetCondition> sheetConditions = sheetConditionService.getConditionByDesignId(designId);
            return ResponseResult.success(sheetConditions, sheetConditions.size(), "执行成功！");
        } catch (Exception e) {
            logger.error("getConditionByCategoryId error:{}", Throwables.getStackTraceAsString(e));
            return ResponseResult.failure(0, "执行失败！");
        }
    }

    @ResponseBody
    @RequestMapping(value = MAPPING_PATH_CREATE)//SpringMVC这个类的前段请求路径
    public ResponseResult create(SheetCondition entity) {
        try {
            entity.setCreatedBy(UUID.fromString(SecurityUtils.getSubject().getCurrentUser().getId()));
            addTrackData(entity);
            entity.setOrdinal(sheetConditionService.getNextOrdinal());
            sheetConditionService.save(entity);
            return ResponseResult.success(entity, 1, "保存成功！");
        } catch (Exception e) {
            logger.error("sheetConditionController create error:{}", Throwables.getStackTraceAsString(e));
            return ResponseResult.failure(0, "保存失败！");
        }
    }


    @ResponseBody
    @RequestMapping("/ResolveAndSave")
    public ResponseResult ResolveAndSave(HttpServletRequest request, HttpServletResponse response) {
        try {
            sheetConditionService.ResolveAndSave(request, UUID.fromString(getCurrentLoggedInUser().getId()));
            return ResponseResult.success(null, 1, "修改成功！");
        } catch (BusinessException e) {
            return ResponseResult.failure(0, e.getMessage());
        } catch (Exception e) {
            logger.error("ResolveAndSave error:{}", Throwables.getStackTraceAsString(e));
            return ResponseResult.failure(0, "修改失败！");
        }
    }

    @ResponseBody
    @RequestMapping("/getRefSituation")
    public ResponseResult getRefSituation(HttpServletRequest request, HttpServletResponse response) {
        try {
            String id = request.getParameter("id");
            List<Map> list = sheetConditionService.getRefSituation(id);
            return ResponseResult.success(list, list.size(), "执行成功！");
        } catch (Exception e) {
            logger.error("getRefSituation error:{}", Throwables.getStackTraceAsString(e));
            return ResponseResult.failure(0, "执行失败！");
        }
    }

    @ResponseBody
    @RequestMapping("/getOpeType")
    public ResponseResult getOpeType(HttpServletRequest request, HttpServletResponse response, String conditionId) {
        try {
            SheetCondition entity = sheetConditionService.getById(UUID.fromString(conditionId));
            if (getCurrentLoggedInUser().getId().equals(entity.getCreatedBy()) || entity.getCreatedBy() == null) {
                return ResponseResult.success(1, 1);
            } else {
                return ResponseResult.success(0, 1);
            }
        } catch (Exception e) {
            logger.error("获取失败！", e);
            return ResponseResult.failure("获取失败！");
        }
    }

    @ResponseBody
    @RequestMapping(value = MAPPING_PATH_UPDATE)
    public ResponseResult update(SheetCondition entity) {
        try {

            SheetCondition entityold = sheetConditionService.getById(entity.getId());
            entity.setCategoryId(entityold.getCategoryId());
            entity.setCreatedBy(entityold.getCreatedBy());
            entity.setCreatedTime(entityold.getCreatedTime());
            entity.setLastModifiedTime(new Timestamp(System.currentTimeMillis()));
            entity.setOrdinal(entityold.getOrdinal());
            entity.setStatus(entityold.getStatus());
            sheetConditionService.setSqlTables(entity);
            sheetConditionService.update(entity);
            return ResponseResult.success(entity, 1, "修改成功！");
        } catch (Exception e) {
            logger.error("sheetConditionController update error:{}", Throwables.getStackTraceAsString(e));
            return ResponseResult.failure(0, "修改失败！");
        }
    }

    @ResponseBody
    @RequestMapping(value = MAPPING_PATH_DELETE_BY_ID)
    public ResponseResult deleteById(HttpServletRequest request) {
        try {
            String id = request.getParameter("id");
            sheetConditionService.deleteById(id);
            return ResponseResult.success(null, 1, "删除成功！");
        } catch (Exception e) {
            logger.error("sheetConditionController deleteById error:{}", Throwables.getStackTraceAsString(e));
            return ResponseResult.failure(0, "删除失败！");
        }
    }

    @ResponseBody
    @RequestMapping("/deleteByIds")
    public ResponseResult deleteByIds(HttpServletRequest request, HttpServletResponse respons) {
        try {
            String[] ids = request.getParameterValues("ids");
            for (String id : ids) {
                sheetConditionService.deleteById(id);
            }
            return ResponseResult.success(null, 1, "删除成功！");
        } catch (Exception e) {
            logger.error("sheetConditionController deleteById error:{}", Throwables.getStackTraceAsString(e));
            return ResponseResult.failure(0, "删除失败！");
        }
    }

    @ResponseBody
    @RequestMapping("/GetConditionById")
    public ResponseResult GetConditionById(HttpServletRequest request, HttpServletResponse response, String strConditionId) {
        try {
            SheetCondition sheetCondition = sheetConditionService.GetConditionDataById(strConditionId);
            List<HashMap<String, Object>> root = new ArrayList<HashMap<String, Object>>();
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("categoryId", sheetCondition.getCategoryId());
            map.put("conditionId", sheetCondition.getId());
            map.put("conditionName", sheetCondition.getConditionName());
            map.put("conditionRelation", sheetCondition.getConditionRelation());

            String conditionRelationDes = sheetCondition.getConditionRelation();
            if (conditionRelationDes != null) {
                UUID conditionID = sheetCondition.getId();
                List<SheetConditionItem> sheetConditionItems = sheetConditionItemService
                        .GetDataByConditionID(conditionID);
                conditionRelationDes = conditionRelationDes.replaceAll("//*", " AND ").replaceAll("//+", " OR ");
                ArrayList<JSONObject> JsonDatas = new ArrayList<JSONObject>();
                for (SheetConditionItem sheetConditionItem : sheetConditionItems) {
                    String Jsondata = sheetConditionItem.getJsonData();
                    JSONObject jsonitem = (JSONObject) JSONObject.parse(Jsondata);
                    jsonitem.put("itemId", sheetConditionItem.getId());
                    JsonDatas.add(jsonitem);
                    conditionRelationDes = conditionRelationDes.replace(sheetConditionItem.getConditionNum().toString(), "[" + jsonitem.get("name") + "]");
                }

                map.put("conditionRelationDes", conditionRelationDes);
                map.put("conditionsJson", JsonDatas);
            } else {
                map.put("sql", sheetCondition.getSql());
            }

            root.add(map);

            return ResponseResult.success(root, 1);
        } catch (Exception e) {
            logger.error("ResolveAndSave error:{}", Throwables.getStackTraceAsString(e));
            return ResponseResult.failure(0, "修改失败！");
        }
    }

    /**
     * 上移节点
     *
     * @param request
     * @param response
     */
    @ResponseBody
    @RequestMapping("/moveUp")
    public ResponseResult moveUp(HttpServletRequest request, HttpServletResponse response, SheetCondition entity) {
        try {
            String categoryId = request.getParameter("categoryId");
            entity.setLastModifiedBy(UUID.fromString(getCurrentLoggedInUser().getId()));
            boolean success = sheetConditionService.moveUp(entity, UUID.fromString(categoryId));
            if (success) {
                return ResponseResult.success(entity, 1, "上移成功！");
            } else {
                return ResponseResult.failure("上移失败！");
            }
        } catch (Exception e) {
            logger.error("上移失败！", e);
            return ResponseResult.failure("上移失败！");
        }
    }

    /**
     * 下移节点
     */
    @ResponseBody
    @RequestMapping("/moveDown")
    public ResponseResult moveDown(HttpServletRequest request, HttpServletResponse response, SheetCondition entity) {
        try {
            String categoryId = request.getParameter("categoryId");
            entity.setLastModifiedBy(UUID.fromString(getCurrentLoggedInUser().getId()));
            boolean success = sheetConditionService.moveDown(entity, UUID.fromString(categoryId));
            if (success) {
                return ResponseResult.success(entity, 1, "下移成功！");
            } else {
                return ResponseResult.failure("下移失败！");
            }
        } catch (Exception e) {
            logger.error("下移失败！", e);
            return ResponseResult.failure("下移失败！");
        }
    }

    /**
     * 移动
     */
    @ResponseBody
    @RequestMapping("/moveCategory")
    public ResponseResult moveCategory(HttpServletRequest request, HttpServletResponse response, String[] ids, String categoryId) {
        try {
            ids = request.getParameterValues("id");
            for (String id : ids) {
                SheetCondition entity = sheetConditionService.getById(UUID.fromString(id));
                entity.setCategoryId(UUID.fromString(categoryId));
                sheetConditionService.update(entity);
            }
            return ResponseResult.success();
        } catch (Exception e) {
            logger.error("moveCategory:", e);
            return ResponseResult.failure("移动失败！");
        }
    }

    @ResponseBody
    @RequestMapping(MAPPING_PATH_FIND_BY_PAGE)
    public ResponseResult findByPage(HttpServletRequest request) {
        try {
            PageableQueryParameter queryParameter = new PageableQueryParameter();
            Enumeration<String> keys = request.getParameterNames();
            if (keys != null) {
                while (keys.hasMoreElements()) {
                    String name = (String) keys.nextElement();
                    queryParameter.getParameters().put(name, (String) request.getParameter(name));
                }
            }
            PageableQueryResult queryResult = sheetConditionService.findByPage(queryParameter);
            return ResponseResult.success(queryResult.getData(), queryResult.getTotalCount());
        } catch (Exception e) {
            logger.error("查询数据失败！", e);
            return ResponseResult.failure("查询数据失败！");
        }
    }


    @ResponseBody
    @RequestMapping("/copyConditionById")
    public ResponseResult copyConditionById(HttpServletRequest request, HttpServletResponse response, String strConditionId) {
        try {
            SheetCondition sheetCondition = sheetConditionService.GetConditionDataById(strConditionId);
            List<HashMap<String, Object>> root = new ArrayList<HashMap<String, Object>>();
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("categoryId", sheetCondition.getCategoryId());
            map.put("conditionName", sheetCondition.getConditionName());
            map.put("conditionRelation", sheetCondition.getConditionRelation());

            String conditionRelationDes = sheetCondition.getConditionRelation();

            if (conditionRelationDes != null) {
                UUID conditionID = sheetCondition.getId();
                List<SheetConditionItem> sheetConditionItems = sheetConditionItemService
                        .GetDataByConditionID(conditionID);
                conditionRelationDes = conditionRelationDes.replaceAll("//*", " AND ").replaceAll("//+", " OR ");
                ArrayList<JSONObject> JsonDatas = new ArrayList<JSONObject>();
                for (SheetConditionItem sheetConditionItem : sheetConditionItems) {
                    String Jsondata = sheetConditionItem.getJsonData();
                    JSONObject jsonitem = (JSONObject) JSONObject.parse(Jsondata);
                    jsonitem.remove("itemId");
                    JsonDatas.add(jsonitem);
                    conditionRelationDes = conditionRelationDes.replace(sheetConditionItem.getConditionNum().toString(), "[" + jsonitem.get("name") + "]");
                }

                map.put("conditionRelationDes", conditionRelationDes);
                map.put("conditionsJson", JsonDatas);
            } else {
                map.put("sql", sheetCondition.getSql());
            }
            root.add(map);

            return ResponseResult.success(root, 1);
        } catch (Exception e) {
            logger.error("ResolveAndSave error:{}", Throwables.getStackTraceAsString(e));
            return ResponseResult.failure(0, "修改失败！");
        }
    }


    @ResponseBody
    @RequestMapping("/getConditonItems")
    public ResponseResult getConditonItems(HttpServletRequest request, HttpServletResponse response, String[] ids) {
        try {
            ArrayList<JSONObject> JsonDatas = new ArrayList<JSONObject>();
            for (String id : ids) {
                SheetCondition sheetCondition = sheetConditionService.GetConditionDataById(id);
                List<HashMap<String, Object>> root = new ArrayList<HashMap<String, Object>>();
                String conditionRelationDes = sheetCondition.getConditionRelation();
                if (conditionRelationDes != null) {
                    UUID conditionID = sheetCondition.getId();
                    List<SheetConditionItem> sheetConditionItems = sheetConditionItemService
                            .GetDataByConditionID(conditionID);
                    conditionRelationDes = conditionRelationDes.replaceAll("//*", " AND ").replaceAll("//+", " OR ");

                    for (SheetConditionItem sheetConditionItem : sheetConditionItems) {
                        String Jsondata = sheetConditionItem.getJsonData();
                        JSONObject jsonitem = (JSONObject) JSONObject.parse(Jsondata);
                        jsonitem.remove("itemId");
                        JsonDatas.add(jsonitem);
                    }
                } else {
                    return ResponseResult.failure(0, "不能取得系统维护的条件！");
                }
            }
            return ResponseResult.success(JsonDatas, JsonDatas.size(), "执行成功");
        } catch (Exception e) {
            logger.error("ResolveAndSave error:{}", Throwables.getStackTraceAsString(e));
            return ResponseResult.failure(0, "执行失败！");
        }
    }

    private void addTrackData(SheetCondition entity) {
        UUID id = UUID.randomUUID();
        if (entity.getId() == null) {
            entity.setId(id);
        }
        if (entity.getStatus() == null) {
            entity.setStatus(DataStatus.NORMAL.getValue());
        }
        if (entity.getCreatedTime() == null) {
            entity.setCreatedTime(new Timestamp(System.currentTimeMillis()));
            ;
        }
        if (entity.getCreatedBy() == null) {
            entity.setCreatedBy(UUID.fromString(getCurrentLoggedInUser().getId()));
        }
        if (entity.getLastModifiedTime() == null) {
            entity.setLastModifiedTime(new Timestamp(System.currentTimeMillis()));
        }
        if (entity.getLastModifiedBy() == null) {
            entity.setLastModifiedBy(id);
        }
    }

    private User getCurrentLoggedInUser() {
        return SecurityUtils.getSubject().getCurrentUser();
    }

}
