package com.sinosoft.ops.cimp.controller.tabledata;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sinosoft.ops.cimp.annotation.SystemApiGroup;
import com.sinosoft.ops.cimp.cache.CacheManager;
import com.sinosoft.ops.cimp.constant.Constants;
import com.sinosoft.ops.cimp.constant.RolePermissionPageSqlEnum;
import com.sinosoft.ops.cimp.controller.BaseController;
import com.sinosoft.ops.cimp.dao.SysTableInfoDao;
import com.sinosoft.ops.cimp.dao.domain.sys.table.SysTableFieldInfo;
import com.sinosoft.ops.cimp.dao.domain.sys.table.SysTableInfo;
import com.sinosoft.ops.cimp.dao.domain.sys.table.SysTableModelInfo;
import com.sinosoft.ops.cimp.dto.QueryDataParamBuilder;
import com.sinosoft.ops.cimp.dto.sys.table.SysTableFieldInfoDTO;
import com.sinosoft.ops.cimp.dto.sys.table.SysTableInfoDTO;
import com.sinosoft.ops.cimp.dto.sys.table.SysTableModelInfoDTO;
import com.sinosoft.ops.cimp.entity.user.User;
import com.sinosoft.ops.cimp.entity.user.UserRole;
import com.sinosoft.ops.cimp.exception.BusinessException;
import com.sinosoft.ops.cimp.export.ExportManager;
import com.sinosoft.ops.cimp.export.handlers.impl.ExportGbrmbHtmlBiJie;
import com.sinosoft.ops.cimp.service.oraganization.OrganizationService;
import com.sinosoft.ops.cimp.service.sys.sysapp.acess.SysAppFieldAccessService;
import com.sinosoft.ops.cimp.service.sys.sysapp.acess.SysAppTableAccessService;
import com.sinosoft.ops.cimp.service.sys.systable.SysTableTypeService;
import com.sinosoft.ops.cimp.service.tabledata.SysTableModelInfoService;
import com.sinosoft.ops.cimp.service.user.RolePermissionPageSqlService;
import com.sinosoft.ops.cimp.service.user.RolePermissionTableService;
import com.sinosoft.ops.cimp.service.user.UserCollectionTableService;
import com.sinosoft.ops.cimp.util.JsonUtil;
import com.sinosoft.ops.cimp.util.OrganizationUtil;
import com.sinosoft.ops.cimp.util.SecurityUtils;
import com.sinosoft.ops.cimp.vo.from.user.rolePermissionPageSql.RPPageSqlSearchModel;
import com.sinosoft.ops.cimp.vo.to.organization.OrganizationSearchViewModel;
import com.sinosoft.ops.cimp.vo.to.organization.OrganizationViewModel;
import com.sinosoft.ops.cimp.vo.to.sys.sysapp.access.SysAppFieldAccessModel;
import com.sinosoft.ops.cimp.vo.to.sys.sysapp.access.SysAppTableAccessModel;
import com.sinosoft.ops.cimp.vo.to.sys.systable.SysTableTypeModel;
import com.sinosoft.ops.cimp.vo.to.user.rolePermissionPageSql.RPPageSqlViewModel;
import com.sinosoft.ops.cimp.vo.to.user.rolePermissionTable.RPTableViewModel;
import com.sinosoft.ops.cimp.vo.to.user.userCollectionTable.UCTableViewModel;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@SystemApiGroup
@Api(description = "系统模型操作")
@RestController
@RequestMapping(value = "/sys/")
@SuppressWarnings("unchecked")
public class SysTableDataController extends BaseController {
    private final SysTableModelInfoService sysTableModelInfoService;
    private final SysTableInfoDao sysTableInfoDao;
    private final SysTableTypeService sysTableTypeService;
    private final RolePermissionTableService rolePermissionTableService;
    private final UserCollectionTableService userCollectionTableService;
    private final SysAppTableAccessService tableAccessService;
    private final SysAppFieldAccessService fieldAccessService;
    private final OrganizationService organizationService;
    private final RolePermissionPageSqlService rolePermissionPageSqlService;
    private final JdbcTemplate jdbcTemplate;
    private final OrganizationUtil organizationUtil;
    private ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);

    @Autowired
    public SysTableDataController(SysTableModelInfoService sysTableModelInfoService,
                                  SysTableInfoDao sysTableInfoDao,
                                  SysTableTypeService sysTableTypeService,
                                  RolePermissionTableService rolePermissionTableService,
                                  UserCollectionTableService userCollectionTableService, SysAppTableAccessService tableAccessService, SysAppFieldAccessService fieldAccessService, OrganizationService organizationService, RolePermissionPageSqlService rolePermissionPageSqlService, JdbcTemplate jdbcTemplate, OrganizationUtil organizationUtil) {
        this.sysTableModelInfoService = sysTableModelInfoService;
        this.sysTableInfoDao = sysTableInfoDao;
        this.sysTableTypeService = sysTableTypeService;
        this.rolePermissionTableService = rolePermissionTableService;
        this.userCollectionTableService = userCollectionTableService;
        this.tableAccessService = tableAccessService;
        this.fieldAccessService = fieldAccessService;
        this.organizationService = organizationService;
        this.rolePermissionPageSqlService = rolePermissionPageSqlService;
        this.jdbcTemplate = jdbcTemplate;
        this.organizationUtil = organizationUtil;
    }

    @RequestMapping(value = "/getSysTableTypes", method = RequestMethod.GET)
    public ResponseEntity getSysTableTypes(
            @RequestParam("appCode") String prjCode) throws BusinessException {

        if (StringUtils.isEmpty(prjCode)) {
            return fail("查询表结构必须传递项目编号");
        }
        List<SysTableTypeModel> allSysTableType = sysTableTypeService.getAllSysTableType();
        return ok(allSysTableType);
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
        List<Map<String, Object>> result = Lists.newArrayList();

        //获取当前用户在app中的表访问权限
        Map<String, SysAppTableAccessModel> perMap = tableAccessService.getTableAccess(prjCode);

        //用户收藏的表id
        String userId = SecurityUtils.getSubject().getCurrentUser().getId();
        List<String> ucSysTableIds = userCollectionTableService.findUCTableListByUserId(userId).stream().map(UCTableViewModel::getTableId).collect(Collectors.toList());

        tableInfo.getTables().forEach(table -> {
            if (perMap.containsKey(table.getId())) {
                Map<String, Object> map = Maps.newHashMap();
                map.put("tableNameEn", table.getTableNameEn());
                map.put("tableNameCn", table.getTableNameCn());
                map.put("appGroupName", table.getAppTableGroupName());
                map.put("isMasterTable", table.isMasterTable());
                map.put("tableNamePK", table.getTableNamePK());
                map.put("tableNameFK", table.getTableNameFK());
                map.put("sysTableId", table.getId());
                map.put("isCollect", ucSysTableIds.contains(table.getId()));
                result.add(map);
            }
        });
        return ok(result);
    }

    @RequestMapping(value = "/getRoutineTableNames", method = RequestMethod.GET)
    public ResponseEntity getRoutineTableNames(
            @RequestParam("appCode") String prjCode,
            @RequestParam("tableTypeName") String tableTypeName) throws BusinessException {


        List<Map<String, Object>> result = Lists.newArrayList();
        List<RPTableViewModel> rpTableListByRoleId = rolePermissionTableService.findRPTableListByUserId();
        SysTableModelInfoDTO tableInfo = sysTableInfoDao.getTableInfo(tableTypeName, prjCode);
        List<SysTableInfoDTO> tables = tableInfo.getTables();
        Map<String, SysTableInfoDTO> collect = tables.stream().collect(Collectors.toMap(SysTableInfoDTO::getId, a -> a, (k1, k2) -> k1));

        //用户收藏的表id
        String userId = SecurityUtils.getSubject().getCurrentUser().getId();
        List<String> ucSysTableIds = userCollectionTableService.findUCTableListByUserId(userId).stream().map(UCTableViewModel::getTableId).collect(Collectors.toList());


        for (RPTableViewModel viewModel : rpTableListByRoleId) {
            SysTableInfoDTO sysTableInfoDTO = collect.get(viewModel.getTableId());
            Map<String, Object> map = Maps.newHashMap();
            if (sysTableInfoDTO != null) {
                map.put("tableNameEn", sysTableInfoDTO.getTableNameEn());
                map.put("tableNameCn", viewModel.getName());
                map.put("appGroupName", sysTableInfoDTO.getAppTableGroupName());
                map.put("isMasterTable", sysTableInfoDTO.isMasterTable());
                map.put("tableNamePK", sysTableInfoDTO.getTableNamePK());
                map.put("tableNameFK", sysTableInfoDTO.getTableNameFK());
                map.put("sysTableId", sysTableInfoDTO.getId());
                map.put("isCollect", ucSysTableIds.contains(sysTableInfoDTO.getId()));
                result.add(map);
            }
        }


        return ok(result);
    }

    @RequestMapping(value = "/getCollectionTableNames", method = RequestMethod.GET)
    public ResponseEntity getCollectionTableNames(
            @RequestParam("appCode") String prjCode,
            @RequestParam("tableTypeName") String tableTypeName) throws BusinessException {
        String userId = SecurityUtils.getSubject().getCurrentUser().getId();

        List<Map<String, Object>> result = Lists.newArrayList();
        List<UCTableViewModel> ucTableListByUserId = userCollectionTableService.findUCTableListByUserId(userId);
        SysTableModelInfoDTO tableInfo = sysTableInfoDao.getTableInfo(tableTypeName, prjCode);
        List<SysTableInfoDTO> tables = tableInfo.getTables();
        Map<String, SysTableInfoDTO> collect = tables.stream().collect(Collectors.toMap(SysTableInfoDTO::getId, a -> a, (k1, k2) -> k1));
        for (UCTableViewModel viewModel : ucTableListByUserId) {
            SysTableInfoDTO sysTableInfoDTO = collect.get(viewModel.getTableId());
            Map<String, Object> map = Maps.newHashMap();
            if (sysTableInfoDTO != null) {
                map.put("tableNameEn", sysTableInfoDTO.getTableNameEn());
                map.put("tableNameCn", viewModel.getName());
                map.put("appGroupName", sysTableInfoDTO.getAppTableGroupName());
                map.put("isMasterTable", sysTableInfoDTO.isMasterTable());
                map.put("tableNamePK", sysTableInfoDTO.getTableNamePK());
                map.put("tableNameFK", sysTableInfoDTO.getTableNameFK());
                map.put("sysTableId", sysTableInfoDTO.getId());
                result.add(map);
            }
        }


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

        //权限判断
        Map<String, SysAppTableAccessModel> tableAccessMap;
        Object o = CacheManager.getInstance().get(Constants.SYS_TABLE_ACCESS_CACHE, prjCode);
        if (o != null) {
            tableAccessMap = (Map<String, SysAppTableAccessModel>) o;
        } else {
            tableAccessMap = tableAccessService.getTableAccess(prjCode);
        }
        for (Iterator<SysTableInfoDTO> itTable = tableInfo.getTables().iterator(); itTable.hasNext(); ) {
            SysTableInfoDTO table = itTable.next();
            //没有该表的访问权限,过滤掉
            if (!tableAccessMap.containsKey(table.getId())) {
                itTable.remove();
                continue;
            }

            boolean canWriteAll = tableAccessMap.get(table.getId()).getCanWriteAll();
            if (!canWriteAll) {
                Map<String, SysAppFieldAccessModel> fieldAccessMap = fieldAccessService.getFieldAccess(prjCode, table.getId());
                for (Iterator<SysTableFieldInfoDTO> itField = table.getFields().iterator(); itField.hasNext(); ) {
                    SysTableFieldInfoDTO field = itField.next();
                    //没有该字段的访问权限,过滤掉
                    if (!fieldAccessMap.containsKey(field.getId())) {
                        itField.remove();
                        continue;
                    }

                    SysAppFieldAccessModel fieldAccessModel = fieldAccessMap.get(field.getId());
                    //没有该字段的读写权限,过滤掉
                    if (!fieldAccessModel.getCanWrite() && !fieldAccessModel.getCanRead()) {
                        itField.remove();
                        continue;
                    }
                    field.setReadOnly(!fieldAccessModel.getCanWrite());
                }
            }
        }


        SysTableModelInfoDTO resultTableInfo = new SysTableModelInfoDTO();
        if (StringUtils.isNotEmpty(tableName)) {
            Map<String, List<SysTableInfoDTO>> tableNameMap = tableInfo.getTables().stream().collect(Collectors.groupingBy(SysTableInfoDTO::getTableNameEn));
            List<SysTableInfoDTO> tableInfoDTOList = tableNameMap.getOrDefault(tableName, Lists.newArrayList());
            resultTableInfo.setTables(tableInfoDTOList);
            resultTableInfo.setTableTypeNameEn(tableInfo.getTableTypeNameEn());
            resultTableInfo.setTableTypeNameCn(tableInfo.getTableTypeNameCn());
            resultTableInfo.setPrjCode(tableInfo.getPrjCode());
            resultTableInfo.setPrimaryField(tableInfo.getPrimaryField());
            return ok(resultTableInfo);
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

        return this.saveSysData(appCode, tableTypeName, tableName, tableNamePK, tableNameFK, tableNameFKValue, form);
    }

    @RequestMapping(value = "batchSaveSysTableData", method = RequestMethod.POST)
    public ResponseEntity batchSaveSysTableData(
            @RequestParam("appCode") String appCode,
            @RequestParam("tableTypeName") String tableTypeName,
            @RequestParam("tableName") String tableName,
            @RequestParam("tableNamePK") String tableNamePK,
            @RequestParam("tableNameFK") String tableNameFK,
            @RequestParam("tableNameFKValue") String tableNameFKValue,
            @RequestParam("form") String form) throws BusinessException {

        if (StringUtils.isNotEmpty(tableNameFKValue)) {
            String[] tableNameFKValues = tableNameFKValue.split(",");

            for (String table_name_FK_value : tableNameFKValues) {
                this.saveSysData(appCode, tableTypeName, tableName, tableNamePK, tableNameFK, table_name_FK_value, form);
            }
        }
        return ok("批量新增成功");
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
        if (formMap == null || formMap.size() == 0) {
            return ok("修改成功");
        }
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

    @RequestMapping(value = "deleteSysTableData", method = RequestMethod.POST)
    public ResponseEntity deleteSysTableData(
            @RequestParam("appCode") String appCode,
            @RequestParam("tableTypeName") String tableTypeName,
            @RequestParam("tableName") String tableName,
            @RequestParam("form") String form) throws BusinessException {

        if (StringUtils.isEmpty(tableTypeName)) {
            return fail("删除信息集必须指定表类型");
        }
        if (StringUtils.isEmpty(appCode)) {
            return fail("删除信息集必须指定项目编号");
        }
        if (StringUtils.isEmpty(tableName)) {
            return fail("删除信息集必须指定表名");
        }

        Map formMap = JsonUtil.parseStringToObject(form, HashMap.class);
        QueryDataParamBuilder queryDataParam = new QueryDataParamBuilder();

        queryDataParam.setPrjCode(appCode)
                .setTableTypeNameEn(tableTypeName)
                .setTableNameEn(tableName)
                .setSaveOrUpdateFormData(formMap);

        sysTableModelInfoService.deleteData(queryDataParam);
        return ok("删除成功");
    }

    @RequestMapping(value = "deleteSysTableDataRecover", method = RequestMethod.POST)
    public ResponseEntity deleteSysTableDataRecover(
            @RequestParam("appCode") String appCode,
            @RequestParam("tableTypeName") String tableTypeName,
            @RequestParam("tableName") String tableName,
            @RequestParam("form") String form) throws BusinessException {

        if (StringUtils.isEmpty(tableTypeName)) {
            return fail("恢复信息集必须指定表类型");
        }
        if (StringUtils.isEmpty(appCode)) {
            return fail("恢复信息集必须指定项目编号");
        }
        if (StringUtils.isEmpty(tableName)) {
            return fail("恢复信息集必须指定表名");
        }

        Map formMap = JsonUtil.parseStringToObject(form, HashMap.class);
        QueryDataParamBuilder queryDataParam = new QueryDataParamBuilder();

        queryDataParam.setPrjCode(appCode)
                .setTableTypeNameEn(tableTypeName)
                .setTableNameEn(tableName)
                .setSaveOrUpdateFormData(formMap);

        sysTableModelInfoService.deleteDataRecover(queryDataParam);
        return ok("恢复成功");
    }

    @RequestMapping(value = "deleteSysTableDataFinal", method = RequestMethod.POST)
    public ResponseEntity deleteSysTableDataFinal(
            @RequestParam("appCode") String appCode,
            @RequestParam("tableTypeName") String tableTypeName,
            @RequestParam("tableName") String tableName,
            @RequestParam("form") String form) throws BusinessException {

        if (StringUtils.isEmpty(tableTypeName)) {
            return fail("清空信息集必须指定表类型");
        }
        if (StringUtils.isEmpty(appCode)) {
            return fail("清空信息集必须指定项目编号");
        }
        if (StringUtils.isEmpty(tableName)) {
            return fail("清空信息集必须指定表名");
        }

        SysTableModelInfo tableInfo = sysTableInfoDao.getTableInfo(tableTypeName);
        if (tableInfo == null) {
            return fail("请确认实体名称和项目编号存在");
        }
        String tableNameDB = tableInfo.getTableNameEnAndSaveTableMap().get(tableName);
        if (StringUtils.isEmpty(tableNameDB)) {
            return fail("请确认要删除的信息集信息存在");
        }
        List<SysTableFieldInfo> sysTableFieldInfos = tableInfo.getTableNameEnAndFieldListMap().get(tableName);
        Optional<SysTableFieldInfo> primaryKeyInfo = sysTableFieldInfos.stream().filter(x -> x.isPK()).findFirst();
        String primaryKey = new String("");
        if (primaryKeyInfo.isPresent()) {
            SysTableFieldInfo sysTableFieldInfo = primaryKeyInfo.get();
            primaryKey = sysTableFieldInfo.getNameEn();
        } else {
            return fail("当前删除的信息项未设置主键属性");
        }

        Map formMap = JsonUtil.parseStringToObject(form, HashMap.class);
        Object keyObject = formMap.get(primaryKey);
        if (keyObject == null) {
            return fail("当前传递的信息项未找到主键信息");
        }
        QueryDataParamBuilder queryDataParam = new QueryDataParamBuilder();

        queryDataParam.setPrjCode(appCode)
                .setTableTypeNameEn(tableTypeName)
                .setTableNameEn(tableName)
                .setSaveOrUpdateFormData(formMap);

        sysTableModelInfoService.deleteDataFinal(queryDataParam);
        return ok("回收站删除成功");
    }

    @RequestMapping(value = "getSysTableCount")
    public ResponseEntity getSysTableCount() throws BusinessException {
        Map<String, Object> resultMap = Maps.newHashMap();

        User currentUser = SecurityUtils.getSubject().getCurrentUser();
        String dataOrganizationId = currentUser.getDataOrganizationId();
        List<UserRole> currentUserRole = SecurityUtils.getSubject().getCurrentUserRole();
        List<String> roleIds = currentUserRole.stream().map(UserRole::getRoleId).collect(Collectors.toList());

        OrganizationSearchViewModel searchViewModel = new OrganizationSearchViewModel();
        searchViewModel.setOrganizationId(dataOrganizationId);
        OrganizationViewModel organizationViewModel = organizationService.lstTreeNode(searchViewModel);

        int organizationCount = organizationViewModel.getSubTreeNode().size();
        resultMap.put("organizationCount", organizationCount);

        RPPageSqlSearchModel searchModel = new RPPageSqlSearchModel();
        searchModel.setRoleIds(roleIds);
        List<RPPageSqlViewModel> pageSqlListByRoleIds = rolePermissionPageSqlService.findRPPageSqlListByRoleIds(searchModel);
        Optional<RPPageSqlViewModel> pageSqlViewModel = pageSqlListByRoleIds.stream().filter(s -> StringUtils.equals(s.getIncludeSubNode(), "1")).filter(s -> StringUtils.equals(s.getSqlNameEn(), RolePermissionPageSqlEnum.NAME_EN.干部集合.value)).findFirst();

        if (pageSqlViewModel.isPresent()) {
            RPPageSqlViewModel viewModel = pageSqlViewModel.get();
            String execCountSql = viewModel.getExecCountSql();
            String selectCountFieldEn = viewModel.getSelectCountFieldEn();
            String execCadreCountSql = execCountSql.replaceAll("\\$\\{deptId\\}", dataOrganizationId);

            Map<String, Object> countMap = jdbcTemplate.queryForMap(execCadreCountSql);
            Object cadreCount = countMap.get(selectCountFieldEn);
            if (cadreCount == null) {
                resultMap.put("cadreCount", 0L);
            } else {
                resultMap.put("cadreCount", Long.parseLong(String.valueOf(cadreCount)));
            }
        }
        return ok(resultMap);
    }


    private ResponseEntity saveSysData(String appCode, String tableTypeName, String tableName, String
            tableNamePK, String tableNameFK, String tableNameFKValue, String form) throws BusinessException {
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

        //权限判断
        Map<String, SysAppTableAccessModel> tableAccessMap = tableAccessService.getTableAccess(appCode);
        Map<String, SysTableInfo> tableNameEnMap = tableInfo.getTables().stream().collect(Collectors.toMap(SysTableInfo::getNameEn, (v) -> v, (s1, s2) -> s1));
        SysTableInfo sysTableInfo = tableNameEnMap.get(tableName);
        if (sysTableInfo == null) {
            return fail("操作的信息集未配置");
        }
        SysAppTableAccessModel sysAppTableAccessModel = tableAccessMap.get(sysTableInfo.getId());
        if (sysAppTableAccessModel == null) {
            return fail("没有操作信息集的权限");
        }
        Map formMap;
        if (sysAppTableAccessModel.getCanWriteAll()) {
            formMap = JsonUtil.parseStringToObject(form, HashMap.class);
        } else {
            HashMap<String, Object> saveFormData = JsonUtil.parseStringToObject(form, HashMap.class);
            Map<String, SysAppFieldAccessModel> fieldAccessMap = fieldAccessService.getFieldAccess(appCode, sysTableInfo.getId());
            List<String> nameEnList = fieldAccessMap.values().stream().map(SysAppFieldAccessModel::getNameEn).collect(Collectors.toList());

            ArrayList formDataKeys = Lists.newArrayList(saveFormData.keySet());
            for (Object formDataKey : formDataKeys) {
                if (!nameEnList.contains(formDataKey)) {
                    saveFormData.entrySet().removeIf(e -> StringUtils.equals(e.getKey(), String.valueOf(formDataKey)));
                }
            }
            formMap = saveFormData;
        }

        //保存单位
        if (StringUtils.equalsIgnoreCase(tableName, "DepB001")) {
            String code = formMap.get("PPTR").toString() + "." + formMap.get("treeLevelCode").toString();
            formMap.put("treeLevelCode", code);
            formMap.put("ORDINAL", jdbcTemplate.queryForMap("select nvl(max(nvl(ORDINAL,0)),0)+1 as ordinal from DEP_B001").get("ordinal"));
        }

        QueryDataParamBuilder queryDataParam = new QueryDataParamBuilder();

        queryDataParam.setPrjCode(appCode)
                .setTableTypeNameEn(tableTypeName)
                .setTableNameEn(tableName)
                .setTableNameEnPK(tableNamePK)
                .setTableNameEnFK(tableNameFK)
                .setTableNameEnFKValue(tableNameFKValue)
                .setSaveOrUpdateFormData(formMap);

        QueryDataParamBuilder dataParamBuilder = sysTableModelInfoService.saveData(queryDataParam);

        if (StringUtils.equalsIgnoreCase(tableTypeName, "CadreInfo")) {
            //如果primaryKey和tableNamePK不一致则表明保存的是子集
            if (!StringUtils.equals(primaryKey, tableNamePK)) {
                if (StringUtils.isNotEmpty(tableNameFKValue)) {
                    executorService.submit(() -> {
                        try {
                            ExportManager.generate(new ExportGbrmbHtmlBiJie(tableNameFKValue));
                        } catch (Exception e) {
                            LOGGER.error("异步生成干部任免表html失败", e);
                        }
                    });
                }
            } else {
                Object tableNameEnPKValue = dataParamBuilder.getTableNameEnPKValue();
                executorService.submit(() -> {
                    try {
                        ExportManager.generate(new ExportGbrmbHtmlBiJie(String.valueOf(tableNameEnPKValue)));
                    } catch (Exception e) {
                        LOGGER.error("异步生成干部任免表html失败", e);
                    }
                });
            }
        }

        if (StringUtils.equalsIgnoreCase(tableName, "DepB001")) {
            //新增单位时，同步到organization表
            String tableNameEnPKValue = dataParamBuilder.getTableNameEnPKValue().toString();
            executorService.submit(() -> organizationUtil.execute(tableNameEnPKValue));
        }
        return ok("保存成功");
    }
}
