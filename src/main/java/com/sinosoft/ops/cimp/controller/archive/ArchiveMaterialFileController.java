package com.sinosoft.ops.cimp.controller.archive;


import com.mongodb.gridfs.GridFSDBFile;
import com.sinosoft.ops.cimp.annotation.ArchiveApiGroup;
import com.sinosoft.ops.cimp.controller.BaseController;
import com.sinosoft.ops.cimp.entity.archive.ArchiveMaterialFile;
import com.sinosoft.ops.cimp.exception.BusinessException;
import com.sinosoft.ops.cimp.repository.archive.ex.CannotFindMongoDbResourceById;
import com.sinosoft.ops.cimp.repository.archive.ex.DownloadResourceFromMongoDbError;
import com.sinosoft.ops.cimp.repository.archive.ex.UploadResourceToMongoDbError;
import com.sinosoft.ops.cimp.repository.archive.impl.MongoDbDaoImpl;
import com.sinosoft.ops.cimp.service.archive.ArchiveMaterialFileService;
import com.sinosoft.ops.cimp.service.archive.ArchiveMaterialService;
import com.sinosoft.ops.cimp.service.archive.MongoDbService;
import com.sinosoft.ops.cimp.util.StringUtil;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@ArchiveApiGroup
@Api(description = "干部档案文件控制器")
@Controller("archiveMaterialFileController")
@RequestMapping("/archiveMaterialFile")
public class ArchiveMaterialFileController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(ArchiveMaterialFileController.class);
    @Resource
    private MongoDbService mongoDbService;
    @Resource
    private ArchiveMaterialFileService archiveMaterialFileService;
    @Resource
    private ArchiveMaterialService archiveMaterialService;
    @Resource
    private MongoDbDaoImpl mongoDbDao;


    @ApiOperation("添加图片")
    @RequestMapping(value = "/upde", consumes = "multipart/*", headers = "content-type=multipart/form-date", method = RequestMethod.POST)
    public void upde(@ApiParam(value = "上传的文件", required = true) MultipartFile file) throws IOException {


        InputStream inputStream1 = file.getInputStream();
        String originalFilename = file.getOriginalFilename();
        mongoDbService.uploadFileFromStreamEncryptAES(mongoDbService.genMongoDbId(), originalFilename, inputStream1);
    }


    /**
     * 根据人员ID+档案分类ID 获取 ArchiveMaterialFile集合
     *
     * @param request->empId      人员ID;
     * @param request->categoryId 档案分类ID;
     * @return ArchiveMaterialFile  保存路径、文件名信息
     */
//	@RequestMapping("/findbyArchiveIDs")
    public void findbyArchiveIDS(HttpServletRequest request, HttpServletResponse response) throws BusinessException {
        try {
            String empId = request.getParameter("empId");
            String categoryId = request.getParameter("categoryId");
            //idList 指定人员+指定档案分类的 ArchiveMaterial ID集合
            List<String> idList = archiveMaterialService.getArchiveMaterialIDs(empId, categoryId);
            //archiveMaterialFileList  ID in idList 的ArchiveMaterial集合
            List<ArchiveMaterialFile> archiveMaterialFileList = (List<ArchiveMaterialFile>) archiveMaterialFileService.getArchiveMaterialFilebyAchiveMaterialIDs(idList);
            if (archiveMaterialFileList != null && archiveMaterialFileList.size() > 0) {
                String relPath = request.getSession().getServletContext().getRealPath("resources/download/");    //保存路径
                Map<String, Object> map = new HashMap<String, Object>();//返回 保存路径 和 文件名列表
                List<String> fileNameList = new ArrayList<String>();//文件名List

                String id = "";
                Path path = null;
                String fileName = "";
                for (ArchiveMaterialFile entity : archiveMaterialFileList) {
                    id = entity.getFileStorageRef();
                    fileName = id + ".jpg";
                    fileNameList.add(fileName);//文件名List
                    path = Paths.get(relPath, fileName);
                    mongoDbService.downloadToFileDecryptWithAES(id, path);
                }
                //返回获取的文件名称List 和文件保存相对路径
                map.put("fileNameList", fileNameList);
                map.put("location", "resources/download/");
                ok(map);
            } else {
                fail("不存在Archive_Material_ID=" + idList + "的记录");
            }
        } catch (Exception e) {
            logger.error("查询失败！", e);
            fail("查询失败");
        }
    }

    @ApiOperation("根据指定imgID获取图片")
    @ApiImplicitParam(name = "id", value = "图片id", dataType = "String", required = true, paramType = "query")
    @RequestMapping(value = "/imgbyid", method = RequestMethod.GET)
    public void imgbyid(HttpServletRequest request, HttpServletResponse response) throws BusinessException {
        try {
            String id = request.getParameter("id");
            String relPath = request.getSession().getServletContext().getRealPath("\\");
            //返回 保存路径 和 文件名列表
            Map<String, Object> map = new HashMap<String, Object>();
            String fileName = id + ".jpg";
            Path path = Paths.get(relPath, id);
            File file = mongoDbService.downloadToFileDecryptWithAES(id, path);
            //设置向浏览器端传送的文件格式
            response.setContentType("application/x-download");
            response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
            BufferedImage bi = ImageIO.read(file);
            ServletOutputStream out = response.getOutputStream();
            ImageIO.write(bi, "jpg", out);
        } catch (Exception e) {
            logger.error("查询失败！", e);
        }
    }

    /**
     * 根据指定档案ID 和pageNo获取
     *
     * @param request->archiveMaterialId: ArchiveMaterialFile archiveMaterialId;
     * @param request->pageNo:            ArchiveMaterialFile pageNumbe
     */
    @ApiOperation("根据指定档案ID 和pageNo获取")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "页数", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "archiveMaterialId", value = "档案id", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "type", value = "文件类型", dataType = "String", required = true, paramType = "query")
    })
    @RequestMapping(value = "/findbyArchiveMaterialIDAndPageNo", method = RequestMethod.POST)
    public ResponseEntity findbyArchiveMaterialIDAndPageNo(HttpServletRequest request, HttpServletResponse response) throws BusinessException {
        try {
            String archiveMaterialId = request.getParameter("archiveMaterialId");
            String pageNo = request.getParameter("pageNo");
            String type = request.getParameter("type");

            if (StringUtil.isEmptyOrNull(pageNo)) {
                pageNo = "1";
            }
            Integer pageNumber = Integer.valueOf(pageNo);
            ArchiveMaterialFile archiveMaterialFile = archiveMaterialFileService.findbypageNo(archiveMaterialId, pageNumber, type);
            if (archiveMaterialFile != null) {
                //返回 保存路径 和 文件名列表
                Map<String, Object> map = new HashMap<String, Object>();
                String id = archiveMaterialFile.getFileStorageRef();
                map.put("img", id);
                map.put("PageCount", archiveMaterialFile.getPageCount());
                map.put("PageNumber", archiveMaterialFile.getPageNumber());

                return ok(map);
            } else {
                return fail("不存在Archive_Material_ID=" + archiveMaterialId + "的记录");
            }
        } catch (Exception e) {
            logger.error("查询失败！", e);
            return fail("查询失败");
        }
    }

    /**
     * 根据指定 ArchiveMaterialFile ID 获取单个ArchiveMaterialFile
     *
     * @param request->archiveMaterialId :ArchiveMaterialFile ID
     * @return ArchiveMaterialFile
     */
    @ApiOperation("根据指定 干部档案文件 ID 获取单个干部档案文件")
    @ApiImplicitParam(name = "archiveMaterialId",
            value = "干部档案文件 ID",
            dataType = "String",
            required = true,
            paramType = "query")
    @RequestMapping(value = "/getArchiveMaterialFileByID", method = RequestMethod.POST)
    public ResponseEntity getArchiveMaterialFileByID(HttpServletRequest request, HttpServletResponse response) throws BusinessException {
        try {
            String archiveMaterialId = request.getParameter("archiveMaterialId");
            ArchiveMaterialFile archiveMaterialFile = archiveMaterialFileService.getById(archiveMaterialId);
            if (archiveMaterialFile != null) {
                return ok(archiveMaterialFile);
            } else {
                return fail("不存在archiveMaterialId=" + archiveMaterialId + "的记录");
            }
        } catch (Exception e) {
            logger.error("查询失败！", e);
            return fail("查询失败");
        }
    }

    /**
     * 根据指定 Archive_Material_ID 获取 ArchiveMaterialFile集合 List<ArchiveMaterialFile>
     *
     * @param request->archiveMaterialId Archive_Material_ID
     * @return ArchiveMaterialFile集合  List<ArchiveMaterialFile>
     */
    @ApiOperation("根据指定 Archive_Material_ID 获取 档案文件集合 List<ArchiveMaterialFile>")
    @ApiImplicitParam(name = "archiveMaterialId",
            value = "档案id",
            dataType = "String",
            required = true,
            paramType = "query")
    @RequestMapping(value = "/getAMFileListByAMID", method = RequestMethod.POST)
    public ResponseEntity getArchiveMaterialFileByAchiveMaterialID(HttpServletRequest request, HttpServletResponse response) throws BusinessException {
        try {
            String archiveMaterialId = request.getParameter("archiveMaterialId");
            List<ArchiveMaterialFile> archiveMaterialFileList = (List<ArchiveMaterialFile>) archiveMaterialFileService.getArchiveMaterialFilebyAchiveMaterialID(archiveMaterialId);
            if (archiveMaterialFileList != null && archiveMaterialFileList.size() != 0) {
                String relPath = request.getSession().getServletContext().getRealPath("resources/download/");
                Map<String, Object> map = new HashMap<String, Object>();
                List<String> fileNameList = new ArrayList<String>();
                String id = "";
                Path path = null;
                String fileName = "";

                for (ArchiveMaterialFile amfile : archiveMaterialFileList) {
                    id = amfile.getFileStorageRef();
                    fileName = id + ".jpg";
                    fileNameList.add(fileName);//文件名List

                    path = Paths.get(relPath, fileName);
                    mongoDbService.downloadToFileDecryptWithAES(id, path);
                }
                map.put("fileNameList", fileNameList);
                map.put("location", "resources/download/");
                return ok(map);
            } else {
                return fail("不存在Archive_Material_ID=" + archiveMaterialId + "的记录");
            }
        } catch (Exception e) {
            logger.error("查询失败！", e);
            return fail("查询失败");
        }
    }

    /**
     * 根据多个Archive_Material_ID   获取ArchiveMaterialFile Map集合
     *
     * @param request Archive_Material_ID集
     * @return ArchiveMaterialFile集合Map<String, List < ArchiveMaterialFile>>
     */
    //	@RequestMapping("/getMapByAchiveMaterialIDs")
    public void getMapByAchiveMaterialIDs(HttpServletRequest request, HttpServletResponse response) throws BusinessException {
        try {
            String[] archiveMaterialIds = request.getParameterValues("archiveMaterialIds");
            List<String> idList = java.util.Arrays.asList(archiveMaterialIds);
            Map<String, List<ArchiveMaterialFile>> map =
                    archiveMaterialFileService.getMapByAchiveMaterialIDs(idList);
            if (map != null) {
                ok(map);
            } else {
                fail("不存在Archive_Material_ID IN" + archiveMaterialIds + "的记录");
            }
        } catch (Exception e) {
            logger.error("查询失败！", e);
            fail("查询失败");
        }
    }

}
