package com.sinosoft.ops.cimp.controller.sys;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sinosoft.ops.cimp.config.swagger2.SystemApiGroup;
import com.sinosoft.ops.cimp.controller.BaseController;
import com.sinosoft.ops.cimp.dao.SysTableInfoDao;
import com.sinosoft.ops.cimp.dao.domain.sys.table.SysTableModelInfo;
import com.sinosoft.ops.cimp.dto.QueryDataParamBuilder;
import com.sinosoft.ops.cimp.dto.sys.table.SysTableInfoDTO;
import com.sinosoft.ops.cimp.dto.sys.table.SysTableModelInfoDTO;
import com.sinosoft.ops.cimp.exception.BusinessException;
import com.sinosoft.ops.cimp.service.SysTableModelInfoService;
import com.sinosoft.ops.cimp.util.JsonUtil;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SystemApiGroup
@Api(description = "系统模型操作")
@RestController
@RequestMapping(value = "/sys/")
@SuppressWarnings("unchecked")
public class SysTableModelInfoController extends BaseController {

    private final SysTableModelInfoService sysTableModelInfoService;
    private final SysTableInfoDao sysTableInfoDao;

    @Autowired
    public SysTableModelInfoController(SysTableModelInfoService sysTableModelInfoService, SysTableInfoDao sysTableInfoDao) {
        this.sysTableModelInfoService = sysTableModelInfoService;
        this.sysTableInfoDao = sysTableInfoDao;
    }

    @RequestMapping(value = "/getSysTableNames", method = RequestMethod.GET)
    public ResponseEntity getSysTableNames(
            @RequestParam("appCode") String prjCode,
            @RequestParam("tableTypeName") String tableTypeName) throws BusinessException {

        if (StringUtils.isEmpty(prjCode)) {
            return fail("查询表结构必须传递项目编号");
        }
        if (StringUtils.isEmpty(tableTypeName)) {
            return fail("查询表结构必须传递表类型名称");
        }
        SysTableModelInfoDTO tableInfo = sysTableInfoDao.getTableInfo(tableTypeName, prjCode);
        List<Map<String, String>> result = Lists.newArrayList();

        tableInfo.getTables().forEach(table -> {
            Map<String, String> map = Maps.newHashMap();
            map.put("tableNameEn", table.getTableNameEn());
            map.put("tableNameCn", table.getTableNameCn());
            map.put("appGroupName", table.getAppTableGroupName());
            result.add(map);
        });
        return ok(result);
    }

    @RequestMapping(value = "/getSysTableStructure", method = RequestMethod.GET)
    public ResponseEntity<SysTableModelInfoDTO> getSysTableStructure(
            @RequestParam("prjCode") String prjCode,
            @RequestParam("tableTypeName") String tableTypeName,
            @RequestParam(value = "tableName", required = false) String tableName) throws BusinessException {

        if (StringUtils.isEmpty(prjCode)) {
            return fail("查询表结构必须传递项目编号");
        }
        if (StringUtils.isEmpty(tableTypeName)) {
            return fail("查询表结构必须传递表类型名称");
        }
        SysTableModelInfoDTO tableInfo = sysTableInfoDao.getTableInfo(tableTypeName, prjCode);
        if (StringUtils.isNotEmpty(tableName)) {
            Map<String, List<SysTableInfoDTO>> tableNameMap = tableInfo.getTables().stream().collect(Collectors.groupingBy(SysTableInfoDTO::getTableNameEn));
            List<SysTableInfoDTO> tableInfoDTOList = tableNameMap.getOrDefault(tableName, Lists.newArrayList());
            tableInfo.setTables(tableInfoDTOList);
            return ok(tableInfo);
        }
        return ok(tableInfo);
    }

    @RequestMapping(value = "saveSysTableData", method = RequestMethod.POST)
    public ResponseEntity saveSysTableData(
            @RequestParam("appCode") String appCode,
            @RequestParam("tableTypeName") String tableTypeName,
            @RequestParam("tableName") String tableName,
            @RequestParam("tableNamePK") String tableNamePK,
            @RequestParam("tableNameFK") String tableNameFK,
            @RequestParam("tableNameFKValue") String tableNameFKValue,
            @RequestParam("form") String form) throws BusinessException {

        if (StringUtils.isEmpty(tableTypeName)) {
            return fail("保存信息集数据必须指定表名类型");
        }
        if (StringUtils.isEmpty(tableName)) {
            return fail("保存信息集必须指定表名");
        }
        if (StringUtils.isEmpty(tableNamePK)) {
            return fail("保存信息集必须指定信息集主键字段");
        }

        SysTableModelInfo tableInfo = sysTableInfoDao.getTableInfo(tableTypeName);
        if (tableInfo == null) {
            return fail("请确认实体名称和项目编号存在");
        }

        String primaryKey = tableInfo.getPrimaryKey();
        if (!StringUtils.equals(primaryKey, tableNamePK)) {
            if (StringUtils.isEmpty(tableNameFK)) {
                return fail("当前保存的信息项非主集则必须传递tableNameFK属性");
            }
        }

        Map formMap = JsonUtil.parseStringToObject(form, HashMap.class);
        QueryDataParamBuilder queryDataParam = new QueryDataParamBuilder();

        queryDataParam.setPrjCode(appCode)
                .setTableTypeNameEn(tableTypeName)
                .setTableNameEn(tableName)
                .setTableNameEnPK(tableNamePK)
                .setTableNameEnFK(tableNameFK)
                .setTableNameEnFKValue(tableNameFKValue)
                .setSaveOrUpdateFormData(formMap);

        sysTableModelInfoService.saveData(queryDataParam);
        return ok("保存成功");
    }

    @RequestMapping(value = "updateSysTableData", method = RequestMethod.POST)
    public ResponseEntity updateSysTableData(
            @RequestParam("appCode") String appCode,
            @RequestParam("tableTypeName") String tableTypeName,
            @RequestParam("tableName") String tableName,
            @RequestParam("tableNamePK") String tableNamePK,
            @RequestParam("tableNamePKValue") String tableNamePKValue,
            @RequestParam("form") String form) throws BusinessException {

        if (StringUtils.isEmpty(tableTypeName)) {
            return fail("修改信息集必须指定表类型");
        }
        if (StringUtils.isEmpty(appCode)) {
            return fail("修改信息集必须指定项目编号");
        }
        if (StringUtils.isEmpty(tableName)) {
            return fail("修改信息集必须指定表名");
        }
        if (StringUtils.isEmpty(tableNamePK)) {
            return fail("修改信息集必须指定信息集主键字段");
        }
        if (StringUtils.isEmpty(tableNamePKValue)) {
            return fail("修改信息集必须指定信息集主键字段的值");
        }

        Map formMap = JsonUtil.parseStringToObject(form, HashMap.class);
        QueryDataParamBuilder queryDataParam = new QueryDataParamBuilder();

        queryDataParam.setPrjCode(appCode)
                .setTableTypeNameEn(tableTypeName)
                .setTableNameEn(tableName)
                .setTableNameEnPK(tableNamePK)
                .setTableNameEnPKValue(tableNamePKValue)
                .setSaveOrUpdateFormData(formMap);

        sysTableModelInfoService.updateData(queryDataParam);
        return ok("修改成功");
    }

    @RequestMapping(value = "getSysTableData")
    public ResponseEntity<QueryDataParamBuilder> getSysTableData(
            @RequestParam("appCode") String appCode,
            @RequestParam("tableTypeName") String tableTypeName,
            @RequestParam("tableName") String tableName,
            @RequestParam("tableNamePK") String tableNamePK,
            @RequestParam("tableNamePKValue") String tableNamePKValue,
            @RequestParam("tableNameFK") String tableNameFK,
            @RequestParam("tableNameFKValue") String tableNameFKValue) throws BusinessException {

        if (StringUtils.isEmpty(appCode)) {
            return fail("查询信息集必须指定项目编号");
        }
        if (StringUtils.isEmpty(tableTypeName)) {
            return fail("查询信息集必须指定表类型名称");
        }
        if (StringUtils.isEmpty(tableName)) {
            return fail("查询信息集必须指定表名");
        }
        if (StringUtils.isEmpty(tableNamePK)) {
            return fail("查询信息集必须指定表名主键");
        }

        QueryDataParamBuilder dataParamBuilder = new QueryDataParamBuilder();

        dataParamBuilder.setPrjCode(appCode)
                .setTableTypeNameEn(tableTypeName)
                .setTableNameEn(tableName)
                .setTableNameEnPK(tableNamePK)
                .setTableNameEnPKValue(tableNamePKValue)
                .setTableNameEnFK(tableNameFK)
                .setTableNameEnFKValue(tableNameFKValue);

        QueryDataParamBuilder resultDataParam = sysTableModelInfoService.queryData(dataParamBuilder);
        return ok(resultDataParam);
    }
}
