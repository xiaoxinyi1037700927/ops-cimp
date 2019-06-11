/**
 * @Project:      IIMP
 * @Title:          SheetDesignCarrierController.java
 * @Copyright: © 2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.controller.sheet;


import com.sinosoft.ops.cimp.common.model.DataStatus;
import com.sinosoft.ops.cimp.common.model.ResponseResult;
import com.sinosoft.ops.cimp.controller.BaseEntityController;
import com.sinosoft.ops.cimp.entity.sheet.SheetDesign;
import com.sinosoft.ops.cimp.entity.sheet.SheetDesignParameter;
import com.sinosoft.ops.cimp.entity.system.SysParameter;
import com.sinosoft.ops.cimp.service.sheet.SheetDesignParameterService;
import com.sinosoft.ops.cimp.service.sheet.SheetDesignService;
import com.sinosoft.ops.cimp.service.system.SysParameterService;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @ClassName: SheetDesignParameterController
 * @Description: 表格设计参数控制器
 * @Version 1.0.0
 */
@Controller("sheetDesignParameterController")
@RequestMapping("sheet/designParameter")
public class SheetDesignParameterController extends BaseEntityController<SheetDesignParameter> {
    private static final Logger logger = LoggerFactory.getLogger(SheetDesignParameterController.class);

    @Resource
    private SheetDesignParameterService sheetDesignParameterService = null;
    @Autowired
    private SysParameterService sysParameterService;
    @Autowired
    private SheetDesignService sheetDesignService;

    @ResponseBody
    
    @RequestMapping(value = MAPPING_PATH_CREATE)
    public ResponseResult create(SheetDesignParameter entity) {
        try {
            entity.setCreatedBy(UUID.fromString(SecurityUtils.getSubject().getCurrentUser().getId()));
            entity.setCreatedTime(new Timestamp(System.currentTimeMillis()));
            entity.setLastModifiedTime(new Timestamp(System.currentTimeMillis()));
            entity.setId(UUID.fromString(SecurityUtils.getSubject().getCurrentUser().getId()));
            entity.setStatus(DataStatus.NORMAL.getValue());
            entity.setOrdinal(sheetDesignParameterService.getNextOrdinal());
            sheetDesignParameterService.create(entity);
            return ResponseResult.success(entity,1,"保存成功！");
        } catch (Exception e) {
            logger.error("创建失败！", e);
            return ResponseResult.failure("保存失败！");
        }
    }

    @ResponseBody
    
    @RequestMapping(value = MAPPING_PATH_UPDATE)
    public ResponseResult update(SheetDesignParameter entity) {
        try {
            sheetDesignParameterService.update(entity);
            return ResponseResult.success(null,1,"更新成功！");
        } catch (Exception e) {
            logger.error("更新失败！", e);
            return ResponseResult.failure("更新失败！");
        }
    }


    @ResponseBody
    
    @RequestMapping(value = MAPPING_PATH_DELETE_BY_ID)
    public ResponseResult deleteById(HttpServletRequest request) {
        try {
            sheetDesignParameterService.deleteById(UUID.fromString(request.getParameter("id")));
            return ResponseResult.success(null,1,"删除成功！");
        } catch (Exception e) {
            logger.error("删除失败！", e);
            return ResponseResult.failure("删除失败！");
        }
    }

    @ResponseBody
    
    @RequestMapping(value = MAPPING_PATH_GET_BY_ID)
    public ResponseResult getById(HttpServletRequest request) {
        try {
            SheetDesignParameter entity = sheetDesignParameterService.getById(UUID.fromString(request.getParameter("id")));
            if (entity != null) {
                return ResponseResult.success(entity, 1, "获取成功！");
            } else {
                return ResponseResult.failure("获取失败！");
            }
        } catch (Exception e) {
            logger.error("获取失败！", e);
            return ResponseResult.failure("获取失败！");
        }
    }

    @ResponseBody
    @RequestMapping("/getByDesignId")
    public ResponseResult getByDesignId(HttpServletRequest request, HttpServletResponse response) {
        try {
            Collection<SheetDesignParameter> entitys = sheetDesignParameterService.getByDesignId(UUID.fromString(request.getParameter("designId")));
            for(SheetDesignParameter entity : entitys)
            {
                SysParameter sysParameter = sysParameterService.getById(Integer.parseInt(entity.getParameterId()));
                entity.setReferenceCodeSet(sysParameter.getReferenceCodeSet());
                entity.setParameterName(sysParameter.getNameCn());
            }
            return ResponseResult.success(entitys, entitys.size(), "获取成功！");
        } catch (Exception e) {
            logger.error("获取失败！", e);
            return ResponseResult.failure("获取失败！");
        }
    }


    @ResponseBody
    @RequestMapping("/getParamByDesignId")
    public ResponseResult getParamByDesignId(HttpServletRequest request, HttpServletResponse response) {
        try {
            List<SysParameter> sysParameters = sysParameterService.getByScope("1");

            Collection<SheetDesignParameter> entitys = sheetDesignParameterService.getByDesignId(UUID.fromString(request.getParameter("designId")));
            List<SysParameter> dellist= new ArrayList<>();
            for(SheetDesignParameter entity : entitys)
            {
                for(SysParameter sysParameter :sysParameters)
                {
                    if(entity.getParameterId().equals(sysParameter.getId().toString()))
                    {
                        dellist.add(sysParameter);
                    }
                }
            }
            sysParameters.removeAll(dellist);
            return ResponseResult.success(sysParameters, sysParameters.size(), "获取成功！");
        } catch (Exception e) {
            logger.error("获取失败！", e);
            return ResponseResult.failure("获取失败！");
        }
    }

    @ResponseBody
    @RequestMapping("/getByDesignIds2Stat")
    public ResponseResult getByDesignIds2Stat(HttpServletRequest request, HttpServletResponse response) {
        try {
            Collection<SheetDesignParameter> entityresult = new ArrayList<SheetDesignParameter>();
            String[] arrids= request.getParameterValues("ids");
            String paraType= request.getParameter("paraType");
            for(String designId : arrids)
            {
                Collection<SheetDesignParameter> entitys = sheetDesignParameterService.getByDesignId(UUID.fromString(designId));
                    if(entitys.stream().filter(item-> paraType.equals(item.getParameterId())).collect(Collectors.toList()).size()==0)
                    {
                        SheetDesign sheetDesign = sheetDesignService.getById(UUID.fromString(designId));
                        return ResponseResult.failure("模板:" + sheetDesign.getName() + "缺失参数,不能下发报表");
                    }
                for(SheetDesignParameter entity : entitys)
                {
                    SysParameter sysParameter = sysParameterService.getById(Integer.parseInt(entity.getParameterId()));
                    entity.setReferenceCodeSet(sysParameter.getReferenceCodeSet());
                    entity.setParameterName(sysParameter.getNameCn());
                }
                entityresult.addAll(entitys);
            }
            return ResponseResult.success(entityresult, entityresult.size(), "获取成功！");
        } catch (Exception e) {
            logger.error("获取失败！", e);
            return ResponseResult.failure("获取失败！");
        }
    }

    @ResponseBody
    @RequestMapping("/getByDesignIds")
    public ResponseResult getByDesignIds(HttpServletRequest request, HttpServletResponse response) {
        try {
            Collection<SheetDesignParameter> entityresult = new ArrayList<SheetDesignParameter>();
            String[] arrids= request.getParameterValues("ids");
            for(String designId : arrids)
            {
                Collection<SheetDesignParameter> entitys = sheetDesignParameterService.getByDesignId(UUID.fromString(designId));
                for(SheetDesignParameter entity : entitys)
                {
                    SysParameter sysParameter = sysParameterService.getById(Integer.parseInt(entity.getParameterId()));
                    entity.setReferenceCodeSet(sysParameter.getReferenceCodeSet());
                    entity.setParameterName(sysParameter.getNameCn());
                }
                entityresult.addAll(entitys);
            }
            return ResponseResult.success(entityresult, entityresult.size(), "获取成功！");
        } catch (Exception e) {
            logger.error("获取失败！", e);
            return ResponseResult.failure("获取失败！");
        }
    }

    /**
     * 上移节点
     * @param request
     * @param response
     */
    @ResponseBody
    @RequestMapping("/moveUp")
    public ResponseResult moveUp(HttpServletRequest request, HttpServletResponse response, SheetDesignParameter entity) {
        try{
        	UUID designId = UUID.fromString(request.getParameter("designId"));
        	entity.setLastModifiedBy(UUID.fromString(SecurityUtils.getSubject().getCurrentUser().getId()));
            boolean success = sheetDesignParameterService.moveUp(entity, designId);
            if (success) {
            	return ResponseResult.success(entity,1,"上移成功！");
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
     * @param request
     * @param response
     */
    @ResponseBody
    @RequestMapping("/moveDown")
    public ResponseResult moveDown(HttpServletRequest request, HttpServletResponse response, SheetDesignParameter entity) {
        try{
        	UUID designId = UUID.fromString(request.getParameter("designId"));
        	entity.setLastModifiedBy(UUID.fromString(SecurityUtils.getSubject().getCurrentUser().getId()));
            boolean success = sheetDesignParameterService.moveDown(entity, designId);
            if (success) {
            	return ResponseResult.success(entity,1,"下移成功！");
            } else {
                return ResponseResult.failure("下移失败！");
            }
        } catch (Exception e) {
            logger.error("下移失败！", e);
            return ResponseResult.failure("下移失败！");
        }
    }

	
	public ResponseResult delete(SheetDesignParameter entity) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public ResponseResult findByPage(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

 
}
