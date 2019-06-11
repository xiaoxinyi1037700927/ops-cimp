/**
 * @Project: IIMP
 * @Title: SheetDesignController.java
 * @Copyright: © 2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.controller.sheet;

import com.google.common.base.Throwables;
import com.sinosoft.ops.cimp.common.model.Constants;
import com.sinosoft.ops.cimp.common.model.PageableQueryParameter;
import com.sinosoft.ops.cimp.common.model.PageableQueryResult;
import com.sinosoft.ops.cimp.common.model.ResponseResult;
import com.sinosoft.ops.cimp.controller.BaseEntityController;
import com.sinosoft.ops.cimp.entity.sheet.SheetDesign;
import com.sinosoft.ops.cimp.entity.sheet.SheetDesignCarrier;
import com.sinosoft.ops.cimp.entity.sheet.SheetDesignDesignCategory;
import com.sinosoft.ops.cimp.service.sheet.SheetDesignCarrierService;
import com.sinosoft.ops.cimp.service.sheet.SheetDesignDesignCategoryService;
import com.sinosoft.ops.cimp.service.sheet.SheetDesignSectionService;
import com.sinosoft.ops.cimp.service.sheet.SheetDesignService;
import com.sinosoft.ops.cimp.util.SecurityUtils;
import com.sinosoft.ops.cimp.util.word.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.*;

/**
 * @ClassName: SheetDesignController
 * @Description: 表格设计控制器
 * @Author: Nil
 * @Date: 2017年8月18日 下午1:15:20
 * @Version 1.0.0
 */
@Controller("sheetDesignController")
@RequestMapping("sheet/design")
public class SheetDesignController extends BaseEntityController<SheetDesign> {
    private static final Logger logger = LoggerFactory.getLogger(SheetDesignController.class);

    @Resource
    private SheetDesignService sheetDesignService = null;
    @Resource
    private SheetDesignCarrierService sheetDesignCarrierService = null;
    @Autowired
    private SheetDesignSectionService sheetDesignSectionService;
    @Autowired
    private SheetDesignDesignCategoryService sheetDesignDesignCategoryService;
    @Autowired
    private SystemUserService systemUserService;

    @ResponseBody
    @RequestMapping(value = MAPPING_PATH_CREATE)
    public ResponseResult create(SheetDesign entity, HttpServletRequest request) {
        try {
            UUID categoryId = UUID.fromString(request.getParameter("categoryId"));
            entity.setCreatedBy(UUID.fromString(SecurityUtils.getSubject().getCurrentUser().getId()));
            if(!sheetDesignService.checkSheetNo(entity.getSheetNo()))
            {
                return ResponseResult.failure("模板号不能重复！");
            }
            sheetDesignService.create(entity, categoryId);
            return ResponseResult.success(entity, 1, "保存成功！");
        } catch (Exception e) {
            logger.error("创建失败！", e);
            return ResponseResult.failure("保存失败！");
        }
    }

    @ResponseBody
    @RequestMapping("/outputCondition")
    public ResponseResult outputCondition(HttpServletRequest request, HttpServletResponse response,String designId) {
        try {
            String templateFilePath = request.getSession().getServletContext().getRealPath("/resources/download/");

            String tempPath = sheetDesignService.ouputCondition(UUID.fromString(designId),templateFilePath);

            String encodeFileName = URLEncoder.encode(tempPath.replace(templateFilePath,""), "UTF-8");
            response.reset();
            response.setHeader("Content-Disposition", "attachment; filename=\"" + encodeFileName);
            response.setContentType("application/octet-stream;charset=UTF-8");
            FileUtils.writeFile(tempPath, response);
            if (new File(tempPath).exists()) {
                new File(tempPath).delete();
            }
            return ResponseResult.success();
        } catch (Exception e) {
            logger.error("创建失败！", e);
            return ResponseResult.failure("保存失败！");
        }
    }

    @ResponseBody
    @RequestMapping("/getSecCondition")
    public ResponseResult getSecCondition(HttpServletRequest request, HttpServletResponse response,String designId,String sectionNo) {
        try {

            Collection clt = sheetDesignService.getSecCondition(designId,sectionNo);
            return ResponseResult.success(clt,clt.size(),"执行成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.failure("执行失败！");
        }
    }

    @ResponseBody
    @RequestMapping("/getSecField")
    public ResponseResult getSecField(HttpServletRequest request, HttpServletResponse response,String designId,String sectionNo) {
        try {

            Collection clt = sheetDesignService.getSecField(designId,sectionNo);
            return ResponseResult.success(clt,clt.size(),"执行成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.failure("执行失败！");
        }
    }

    @ResponseBody
    @RequestMapping("/write2File")
    public ResponseResult write2File(HttpServletRequest request, HttpServletResponse response,String id) {
        try {
            UUID uuid = UUID.fromString(id);
            String templateFilePath = request.getSession().getServletContext().getRealPath("/resources/download/");
            String tempPath = sheetDesignService.write2File(uuid,templateFilePath);

            String encodeFileName = URLEncoder.encode(tempPath, "UTF-8");
            response.reset();
            response.setHeader("Content-Disposition", "attachment; filename=\"" + encodeFileName + "\"");
            response.setContentType("application/octet-stream;charset=UTF-8");
            FileUtils.writeFile(templateFilePath+tempPath, response);
            if (new File(templateFilePath+tempPath).exists()) {
                new File(templateFilePath+tempPath).delete();
            }
            return ResponseResult.success();
        } catch (Exception e) {
            logger.error("执行失败！", e);
            return ResponseResult.failure("执行失败！");
        }
    }

    @ResponseBody
    @RequestMapping("/checkModelExist")
    public ResponseResult checkModelExist(HttpServletRequest request, HttpServletResponse response) {
        try {
            String categoryId = request.getParameter("categoryId");
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            String fileName = "";
            Iterator<String> fileNames = multipartRequest.getFileNames();
            while (fileNames.hasNext()) {
                fileName = (String) fileNames.next();
            }
            MultipartFile file = multipartRequest.getFile(fileName);
            byte[] bytes = file.getBytes();
            InputStream input = new ByteArrayInputStream(bytes);
            LinkedHashMap serializableMap=null;
            ObjectInputStream ois = new ObjectInputStream(input);
            try {
                serializableMap = (LinkedHashMap)ois.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            SheetDesign sheetDesign=(SheetDesign)serializableMap.get("SheetDesign-0");
            if(sheetDesignService.getById(sheetDesign.getId())!=null)
            {
                return ResponseResult.success(-1, 0, "执行成功！");
            }
            else
            {
                sheetDesignService.readAndSave(serializableMap,categoryId);
                return ResponseResult.success(1, 0, "执行成功！");
            }
        } catch (Exception e) {
            logger.error("执行失败！", e);
            return ResponseResult.failure("执行失败！");
        }
    }

    @ResponseBody
    @RequestMapping("/readModel")
    public ResponseResult readModel(HttpServletRequest request, HttpServletResponse response) {
        try {
            String categoryId = request.getParameter("categoryId");
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            String fileName = "";
            Iterator<String> fileNames = multipartRequest.getFileNames();
            while (fileNames.hasNext()) {
                fileName = (String) fileNames.next();
            }
            MultipartFile file = multipartRequest.getFile(fileName);
            byte[] bytes = file.getBytes();
            InputStream input = new ByteArrayInputStream(bytes);
            LinkedHashMap serializableMap=null;
            ObjectInputStream ois = new ObjectInputStream(input);
            try {
                serializableMap = (LinkedHashMap)ois.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            sheetDesignService.readAndSave(serializableMap,categoryId);
            return ResponseResult.success(null, 0, "执行成功！");
        } catch (Exception e) {
            logger.error("执行失败！", e);
            return ResponseResult.failure("执行失败！");
        }
    }

    @ResponseBody
    @RequestMapping("/saveAs")
    public ResponseResult saveAs(HttpServletRequest request, HttpServletResponse response, SheetDesign entity) {
        try {
            UUID categoryId = UUID.fromString(request.getParameter("categoryId"));
            entity.setCreatedBy(UUID.fromString(SecurityUtils.getSubject().getCurrentUser().getId()));
            if(!sheetDesignService.checkSheetNo(entity.getSheetNo()))
            {
                return ResponseResult.failure("模板号不能重复！");
            }
            sheetDesignService.saveAs(entity, categoryId);
            return ResponseResult.success(entity, 1, "保存成功！");
        } catch (Exception e) {
            logger.error("创建失败！", e);
            return ResponseResult.failure("保存失败！");
        }
    }

    /**
     * 移动
     *
     * @param request
     * @param response
     */
    @ResponseBody
    @RequestMapping("/moveCategory")
    public ResponseResult moveCategory(HttpServletRequest request, HttpServletResponse response, String id,String oldCategoryId,String newCategoryId) {
        try {
            SheetDesignDesignCategory entity =sheetDesignDesignCategoryService.getByDesignId(UUID.fromString(id),UUID.fromString(oldCategoryId));
            entity.setCategoryId(UUID.fromString(newCategoryId));
            sheetDesignDesignCategoryService.update(entity);
            return ResponseResult.success();
        } catch (Exception e) {
            logger.error("moveCategory:", e);
            return ResponseResult.failure("移动失败！");
        }
    }

    @ResponseBody
    
    @RequestMapping(value = MAPPING_PATH_UPDATE)
    public ResponseResult update(SheetDesign entity) {
        try {
            SheetDesign sd = sheetDesignService.getById(entity.getId());

            if(entity.getIsReleased())
            {
                Timestamp now = new Timestamp(System.currentTimeMillis());
                entity.setReleaseTime(now);
            }
            if(!sheetDesignService.checkSheetNo(entity.getSheetNo(),entity.getId()))
            {
                return ResponseResult.failure("模板号不能重复！");
            }
            entity.setOrdinal(sd.getOrdinal());
            entity.setCreatedBy(sd.getCreatedBy());
            entity.setCreatedTime(sd.getCreatedTime());
            entity.setLastModifiedBy(UUID.fromString(SecurityUtils.getSubject().getCurrentUser().getId()));
            entity.setLastModifiedTime(new Timestamp(System.currentTimeMillis()));
            sheetDesignService.update(entity);
            return ResponseResult.success(entity, 1, "保存成功！");
        } catch (Exception e) {
            logger.error("更新失败！", e);
            return ResponseResult.failure("保存失败！");
        }
    }

    @ResponseBody
    @RequestMapping("/output")
    public ResponseResult output(HttpServletRequest request, HttpServletResponse response, SheetDesign entity) {
        try {
            SheetDesign sd = sheetDesignService.getById(entity.getId());
            Timestamp now = new Timestamp(System.currentTimeMillis());
            sd.setReleaseTime(now);
            if(!sheetDesignService.checkSheetNo(entity.getSheetNo(),entity.getId()))
            {
                return ResponseResult.failure("模板号不能重复！");
            }
            entity.setOrdinal(sd.getOrdinal());
            sd.setLastModifiedBy(UUID.fromString(SecurityUtils.getSubject().getCurrentUser().getId()));
            sd.setLastModifiedTime(new Timestamp(System.currentTimeMillis()));
            sheetDesignService.output(entity);
            return ResponseResult.success(entity, 1, "发布成功！");
        } catch (Exception e) {
            logger.error("发布失败！", e);
            return ResponseResult.failure("发布失败！");
        }
    }

    @ResponseBody
    @RequestMapping("/outputCancel")
    public ResponseResult outputCancel(HttpServletRequest request, HttpServletResponse response, SheetDesign entity) {
        try {
            SheetDesign sd = sheetDesignService.getById(entity.getId());
            entity.setReleaseTime(null);
            if(!sheetDesignService.checkSheetNo(entity.getSheetNo(),entity.getId()))
            {
                return ResponseResult.failure("模板号不能重复！");
            }
            entity.setOrdinal(sd.getOrdinal());
            entity.setLastModifiedBy(UUID.fromString(SecurityUtils.getSubject().getCurrentUser().getId()));
            entity.setLastModifiedTime(new Timestamp(System.currentTimeMillis()));
            sheetDesignService.update(entity);
            return ResponseResult.success(entity, 1, "取消发布成功！");
        } catch (Exception e) {
            logger.error("取消发布失败！", e);
            return ResponseResult.failure("取消发布失败！");
        }
    }

    @ResponseBody
    @RequestMapping(value = MAPPING_PATH_DELETE)
    public ResponseResult delete(SheetDesign entity,HttpServletRequest request) {
        try {
            UUID categoryId = UUID.fromString(request.getParameter("categoryId"));
            entity.setLastModifiedBy(UUID.fromString(SecurityUtils.getSubject().getCurrentUser().getId()));
            sheetDesignService.delete(entity, categoryId);
            return ResponseResult.success(entity, 1, "删除成功！");
        } catch (Exception e) {
            logger.error("删除失败！", e);
            return ResponseResult.failure("删除失败！");
        }
    }

    @ResponseBody
    
    @RequestMapping(value = MAPPING_PATH_DELETE_BY_ID)
    public ResponseResult deleteById(HttpServletRequest request) {
        try {
            UUID id = getUUIDParam(request, "id", UUID.randomUUID());
            //sheetDesignSectionService.deleteByDesignId(id);
            sheetDesignService.deleteById(id);
            return ResponseResult.success(id, 1, "删除成功！");
        } catch (Exception e) {
            logger.error("删除失败！", e);
            return ResponseResult.failure("删除失败！");
        }
    }

    @ResponseBody
    
    @RequestMapping(value = MAPPING_PATH_GET_BY_ID)
    public ResponseResult getById(HttpServletRequest request) {
        try {
            SheetDesign entity = sheetDesignService.getById(getUUIDParam(request, "id", UUID.randomUUID()));
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

//    @ResponseBody
//    
//    @RequestMapping(value = MAPPING_PATH_FIND_BY_PAGE)
//    public ResponseResult findByPage(HttpServletRequest request, HttpServletResponse response) {
//        try {
//            PageableQueryParameter queryParameter = new PageableQueryParameter();
//            queryParameter.setPageNo(getIntegerParam(request, "page", 1));
//            queryParameter.setPageSize(getIntegerParam(request, "limit", Constants.DEFAULT_PAGE_SIZE));
//            String keyword = request.getParameter("keyword");
//            if (keyword != null) {
//                queryParameter.getParameters().put("keyword", keyword);
//            }
//            queryParameter.getParameters().put("categoryId", this.getUUIDParam(request, "categoryId", UUID.randomUUID()));
//            //queryParameter.getParameters().put("type",this.getByteParam(request, "type", (byte)-1));
//
//            Map<String, String> orderMap = new HashMap<String, String>();
//            orderMap.put("d.ordinal", "ASC");
//            queryParameter.setOrderBys(orderMap);
//            PageableQueryResult queryResult = sheetDesignService.findByPage(queryParameter);
//            return ResponseResult.success(queryResult.getData(), queryResult.getTotalCount());
//        } catch (Exception e) {
//            logger.error("查询数据失败！", e);
//            return ResponseResult.failure("查询数据失败！");
//        }
//    }

    @ResponseBody
    
    @RequestMapping(value = MAPPING_PATH_FIND_BY_PAGE)
    public ResponseResult findByPage(HttpServletRequest request) {
        try {
            String includeDown = (String) request.getParameter("includeDown");
            Collection<UUID> collIds = null;
            PageableQueryParameter queryParameter = new PageableQueryParameter();
            queryParameter.setPageNo(getIntegerParam(request, "page", 1));    //1
            queryParameter.setPageSize(getIntegerParam(request, "limit", Constants.DEFAULT_PAGE_SIZE));    //Constants.DEFAULT_PAGE_SIZE
            Enumeration<String> keys = request.getParameterNames();
            if (keys != null) {
                while (keys.hasMoreElements()) {
                    String name = (String) keys.nextElement();
                    if ("categoryId".equals(name)) {
                        if ("1".equals(includeDown)) {
                            //包含子目录
                            collIds = sheetDesignService.getDownCatigories(this.getUUIDParam(request, name, UUID.randomUUID()));
                            queryParameter.getParameters().put(name, collIds);
                        } else {
                            //不包含子目录
                            queryParameter.getParameters().put(name, this.getUUIDParam(request, name, UUID.randomUUID()));
                        }
                    } else if ("type".equals(name)) {
                        String[] typeArray = request.getParameterValues(name);
                        Collection<Byte> typeCol = new HashSet<Byte>();
                        if (typeArray != null && typeArray.length > 0) {
                            for (String aType : typeArray) {
                            	if (aType != null && !"".equals(aType)) {
                            		typeCol.add(Byte.parseByte(aType));
                            	}
                            }
                        }
                        if (typeCol.size() > 0) {
                        	queryParameter.getParameters().put(name, typeCol);
                        }
                    } else if ("status".equals(name)) {
                        queryParameter.getParameters().put(name, this.getByteParam(request, name, (byte) -1));
                    } else if ("output".equals(name)) {
                        queryParameter.getParameters().put(name, request.getParameter(name));
                    } else if ("dataFillType".equals(name)) {
                        queryParameter.getParameters().put(name, this.getByteParam(request, name, (byte) -1));
                    } else if ("includeDown".equals(name)) {
                    } else if ("page".equals(name)) {
                    } else if ("start".equals(name)) {
                    } else if ("limit".equals(name)) {
                    } else if ("_dc".equals(name)) {
                    } else if("sheetCategoryId".equals(name)){
                        queryParameter.getParameters().put(name, UUID.fromString(request.getParameter(name)));
                    } else {
                        queryParameter.getParameters().put(name, (String) request.getParameter(name));
                    }
                }
            }
            if(!"sa".equals(SecurityUtils.getSubject().getCurrentUser().getLoginName())){
            	Collection<String> organizationIds = systemUserService.getTreeOrganizationIds(UUID.fromString(SecurityUtils.getSubject().getCurrentUser().getId()), "DepTree");
            	//Collection<String> depIds = sheetDesignService.getAllOrganizationByParent(organizationIds);
            	queryParameter.getParameters().put("depIds", organizationIds);
            }
            Map<String, String> orderMap = new HashMap<String, String>();
            orderMap.put("c.categoryId", "ASC");
            orderMap.put("d.ordinal", "ASC");
            queryParameter.setOrderBys(orderMap);
            //PageableQueryResult queryResult = sheetDesignService.findByPage(queryParameter, collIds);
            PageableQueryResult queryResult = sheetDesignService.findByPage(queryParameter);
            sheetDesignService.setRefNum(queryResult.getData());
            return ResponseResult.success(queryResult.getData(), queryResult.getTotalCount());
        } catch (Exception e) {
            logger.error("查询数据失败！", e);
            return ResponseResult.failure("查询数据失败！");
        }
    }

    @ResponseBody
    @RequestMapping("/copy")
    public ResponseResult copy(HttpServletRequest request, HttpServletResponse response, SheetDesign entity) {
        try {
            UUID categoryId = UUID.fromString(request.getParameter("categoryId"));
            entity.setCreatedBy(UUID.fromString(SecurityUtils.getSubject().getCurrentUser().getId()));
            sheetDesignService.copy(entity, categoryId);
            return ResponseResult.success(entity, 1, "复制成功！");
        } catch (Exception e) {
            logger.error("复制失败！", e);
            return ResponseResult.failure("复制失败！");
        }
    }

    /**
     * 上移节点
     * @param request
     * @param response
     */
    @ResponseBody
    @RequestMapping("/moveUp")
    public ResponseResult moveUp(HttpServletRequest request, HttpServletResponse response, SheetDesign entity) {
        try {
            UUID categoryId = UUID.fromString(request.getParameter("categoryId"));
            entity.setLastModifiedBy(UUID.fromString(SecurityUtils.getSubject().getCurrentUser().getId()));
            boolean success = sheetDesignService.moveUp(entity, categoryId);
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
     * @param request
     * @param response
     */
    @ResponseBody
    @RequestMapping("/moveDown")
    public ResponseResult moveDown(HttpServletRequest request, HttpServletResponse response, SheetDesign entity) {
        try {
            UUID categoryId = UUID.fromString(request.getParameter("categoryId"));
            entity.setLastModifiedBy(UUID.fromString(SecurityUtils.getSubject().getCurrentUser().getId()));
            boolean success = sheetDesignService.moveDown(entity, categoryId);
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
     * 上传word文件
     *
     * @param request
     * @param response
     */
    @ResponseBody
    @RequestMapping("/wordUpload")
    public ResponseResult wordUpload(HttpServletRequest request, HttpServletResponse response) {
        try {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            String fileName = "";
            Iterator<String> fileNames = multipartRequest.getFileNames();
            while (fileNames.hasNext()) {
                fileName = (String) fileNames.next();
            }
            MultipartFile file = multipartRequest.getFile(fileName);
            String orgName = file.getOriginalFilename();
            byte[] bytes = file.getBytes();
            UUID designId = UUID.fromString((String) request.getParameter("designId"));
            //UUID designId = UUID.fromString("E9D1852F-1379-4FB0-877C-800E7BD16EBF");
            SheetDesignCarrier oldItem = sheetDesignCarrierService.getByDesignId(designId);
            Timestamp now = new Timestamp(System.currentTimeMillis());
            if (oldItem == null) {
                // 生成新记录
                SheetDesignCarrier newItem = new SheetDesignCarrier();
                newItem.setId(UUID.randomUUID());
                newItem.setCreatedBy(UUID.fromString(SecurityUtils.getSubject().getCurrentUser().getId()));
                newItem.setLastModifiedBy(UUID.fromString(SecurityUtils.getSubject().getCurrentUser().getId()));
                newItem.setCreatedTime(now);
                newItem.setLastModifiedTime(now);
                newItem.setType((byte) 3);
                newItem.setStatus((byte) 0);
                newItem.setOrdinal(1);
                newItem.setContent(bytes);
                newItem.setContentLength((long) bytes.length);
                newItem.setDesignId(designId);
                newItem.setFileName(orgName);
                sheetDesignCarrierService.create(newItem);
            } else {
                // 修改原记录
                oldItem.setLastModifiedBy(UUID.fromString(SecurityUtils.getSubject().getCurrentUser().getId()));
                oldItem.setLastModifiedTime(now);
                oldItem.setType((byte) 3);
                oldItem.setStatus((byte) 0);
                oldItem.setContent(bytes);
                oldItem.setContentLength((long) bytes.length);
                oldItem.setFileName(orgName);
                sheetDesignCarrierService.update(oldItem);
            }
            sheetDesignSectionService.deleteByDesignId(designId);
            sheetDesignCarrierService.analyzeWord2Tree(bytes, designId);
            return ResponseResult.success(designId, 1, "上传成功！");
        } catch (Exception e) {
            logger.error("上传失败！", e);
            return ResponseResult.failure("上传失败！");
        }
    }

    /**
     * 下载word文件
     *
     * @param request
     * @param response
     */
    @ResponseBody
    @RequestMapping("/wordDownload")
    public void wordDownload(HttpServletRequest request, HttpServletResponse response) {
        OutputStream outputStream = null;
        try {
            UUID designId = UUID.fromString((String) request.getParameter("designId"));
            SheetDesignCarrier oldItem = sheetDesignCarrierService.getByDesignId(designId);
            if (oldItem != null) {
                byte[] data = oldItem.getContent();
                String fileName = oldItem.getFileName();
                String encodeFileName = URLEncoder.encode(fileName, "UTF-8");
                response.reset();
                response.setHeader("Content-Disposition", "attachment; filename=\"" + encodeFileName + "\"");
                response.addHeader("Content-Length", "" + data.length);
                response.setContentType("application/octet-stream;charset=UTF-8");
                outputStream = new BufferedOutputStream(response.getOutputStream());
                outputStream.write(data);
                outputStream.flush();
                outputStream.close();
                logger.info("下载成功,designId=" + designId + ",fileName=" + fileName);
            } else {
                logger.error("下载失败，模板还没上传，designId=" + designId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("下载失败！", e);
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    logger.error("关闭对象失败！", e);
                }
            }
        }
    }


    /**
     * 下载word文件
     *
     * @param request
     * @param response
     */
    @ResponseBody
    @RequestMapping("/word2html")
    public ResponseResult word2html(HttpServletRequest request, HttpServletResponse response) {
        OutputStream outputStream = null;
        try {
            UUID designId = UUID.fromString((String) request.getParameter("designId"));
            String tempPath = "/resources/temp/" + UUID.randomUUID().toString() + ".pdf";
            String templateFilePath = request.getSession().getServletContext()
                    .getRealPath(tempPath);
            Integer num = sheetDesignCarrierService.word2html(designId, templateFilePath, tempPath);

            if (num > 0) {
                FileUtils.writeFile(templateFilePath, response);
                if (new File(templateFilePath).exists()) {
                    new File(templateFilePath).delete();
                }
                return ResponseResult.success();
            } else {
                return ResponseResult.success(null, 0, "OK");
            }

        } catch (Exception e) {
            logger.error("下载失败！", e);
            return ResponseResult.failure();
        }
    }

    @ResponseBody
    @RequestMapping("/getRefSituation")
    public ResponseResult getRefSituation(HttpServletRequest request, HttpServletResponse response) {
        try {
            String id =request.getParameter("id");
            List<Map> list = sheetDesignService.getRefSituation(id);
            return ResponseResult.success(list, list.size(), "执行成功！");
        }
        catch (Exception e) {
            logger.error("getRefSituation error:{}", Throwables.getStackTraceAsString(e));
            return ResponseResult.failure(0, "执行失败！");
        }
    }

}
