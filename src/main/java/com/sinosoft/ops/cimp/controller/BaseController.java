package com.sinosoft.ops.cimp.controller;

import com.sinosoft.ops.cimp.common.BaseResult;
import com.sinosoft.ops.cimp.common.BaseResultHttpStatus;
import com.sinosoft.ops.cimp.constant.OpsErrorMessage;
import com.sinosoft.ops.cimp.controller.sys.table.FileSaveResult;
import com.sinosoft.ops.cimp.controller.sys.table.FileUpload;
import com.sinosoft.ops.cimp.controller.sys.table.UploadResults;
import com.sinosoft.ops.cimp.exception.BusinessException;
import com.sinosoft.ops.cimp.exception.SystemException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public abstract class BaseController {

    @Value("${cimp.file-save-dir}")
    private String fileSaveDir;

    public static final Logger LOGGER = LoggerFactory.getLogger(BaseController.class);

    public ResponseEntity ok(Object data, BaseResult.Page page) throws BusinessException, SystemException {
        return ResponseEntity.ok(BaseResult.ok(data, page));
    }

    public ResponseEntity ok(Object data) throws BusinessException, SystemException {
        return ResponseEntity.ok(BaseResult.ok(data));
    }

    public ResponseEntity fail(String message) throws BusinessException, SystemException {
        return new ResponseEntity(BaseResult.fail(message), HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity fail(String message, BaseResultHttpStatus status) throws BusinessException, SystemException {
        if (status.getStatus() == BaseResultHttpStatus.FAIL.getStatus()) {
            if (message != null && StringUtils.isNotEmpty(message)) {
                return new ResponseEntity(BaseResult.fail(status, message), HttpStatus.BAD_REQUEST);
            } else {
                return new ResponseEntity(BaseResult.fail(status, BaseResultHttpStatus.FAIL.getDesc()), HttpStatus.BAD_REQUEST);
            }
        }
        if (status.getStatus() == BaseResultHttpStatus.NOT_FOUND.getStatus()) {
            if (message != null && StringUtils.isNotEmpty(message)) {
                return new ResponseEntity(BaseResult.fail(status, message), HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity(BaseResult.fail(status, BaseResultHttpStatus.FAIL.getDesc()), HttpStatus.NOT_FOUND);
            }
        }
        if (status.getStatus() == BaseResultHttpStatus.UNAUTHORIZED.getStatus()) {
            if (message != null && StringUtils.isNotEmpty(message)) {
                return new ResponseEntity(BaseResult.fail(status, message), HttpStatus.UNAUTHORIZED);
            } else {
                return new ResponseEntity(BaseResult.fail(status, BaseResultHttpStatus.FAIL.getDesc()), HttpStatus.UNAUTHORIZED);
            }
        }
        if (status.getStatus() == BaseResultHttpStatus.INTERNAL_SERVER_ERROR.getStatus()) {
            if (message != null && StringUtils.isNotEmpty(message)) {
                return new ResponseEntity(BaseResult.fail(status, message), HttpStatus.INTERNAL_SERVER_ERROR);
            } else {
                return new ResponseEntity(BaseResult.fail(status, BaseResultHttpStatus.FAIL.getDesc()), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity(BaseResult.fail(message), HttpStatus.BAD_REQUEST);
    }


    @ResponseBody
    @RequestMapping(value = "/fileUp", method = RequestMethod.POST)
    public ResponseEntity bigFileUpload(
                                        @RequestParam("file1") @FileUpload MultipartFile[] files, UploadResults result) throws BusinessException, SystemException {
        try {
          //  List<FileSaveResult> list1 = result.getGroup("file1");
            Map<String, Object> res = new HashMap<>();
            Map<String, String> params = new HashMap<>();
            res.put("params", params);

            return ok(res);
        } catch (Exception e) {

        }
        return null;
    }


    @ResponseBody
    @FileUpload(digest = true)
    @RequestMapping(value = "/fileUp1", method = RequestMethod.POST)
    public ResponseEntity bigFileUpload1(
            @RequestPart("files1") @FileUpload(digest = false) MultipartFile[] files1,
            @RequestPart("files2") MultipartFile[] files2, UploadResults result
    ) throws BusinessException, SystemException {
        try {
            List<FileSaveResult> list1 = result.getGroup("files1");
            List<FileSaveResult> list2 = result.getGroup("files2");

            Map<String, Object> res = new HashMap<>();
            Map<String, String> params = new HashMap<>();
            res.put("params", params);
            if (files1 != null) {
                LOGGER.debug("-------------------文件数量:  " + files1.length + "个文件------------");
            } else {
                LOGGER.debug("-------------------文件数量:  " + 0 + "个文件------------");
            }
            if (files2 != null) {
                LOGGER.debug("-------------------文件数量:  " + files2.length + "个文件------------");
            } else {
                LOGGER.debug("-------------------文件数量:  " + 0 + "个文件------------");
            }
            return ok(res);
        } catch (Exception e) {
        }
        return null;
    }

    @RequestMapping(value = "/download/{fileId}", method = RequestMethod.GET)
    public void download(
            @PathVariable("fileId") String fileId,
            HttpServletResponse response
    ) throws BusinessException, SystemException {
        String dirPath = fileSaveDir + File.separator + fileId;
        File dir = new File(dirPath);
        if (!dir.exists()) {
            throw new SystemException(OpsErrorMessage.MODULE_NAME, OpsErrorMessage.FILE_IO_ERROR);
        }
        String[] filenames = dir.list();
        if (filenames == null || filenames.length <= 0) {
            LOGGER.error("文件不存在{}", dirPath);
            throw new SystemException(OpsErrorMessage.MODULE_NAME, OpsErrorMessage.FILE_NOT_FOUND_ERROR, fileId);
        }
        File file = new File(dirPath + File.separator + filenames[0]);
        try {
            // 设置文件mimeType
            String mimeType = Files.probeContentType(file.toPath());
            if (mimeType == null) {
                mimeType = "application/octet-stream";
            }
            // 设置文件名字
            response.setContentType(mimeType);
            response.setHeader("Content-Disposition",
                    "attachment; filename=" + URLEncoder.encode(file.getName(), "UTF-8"));
            response.setContentLength((int) file.length());

            // 写入文件到response
            InputStream in = new FileInputStream(file);
            InputStream inputStream = new BufferedInputStream(in);
            FileCopyUtils.copy(inputStream, response.getOutputStream());
            in.close();
            inputStream.close();
        } catch (IOException e) {
            throw new SystemException(OpsErrorMessage.MODULE_NAME, OpsErrorMessage.FILE_IO_ERROR);
        }
    }


}
