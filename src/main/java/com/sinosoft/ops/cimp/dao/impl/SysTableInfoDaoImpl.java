package com.sinosoft.ops.cimp.dao.impl;

import com.google.common.collect.Lists;
import com.sinosoft.ops.cimp.cache.CacheManager;
import com.sinosoft.ops.cimp.constant.OpsErrorMessage;
import com.sinosoft.ops.cimp.dao.SysTableInfoDao;
import com.sinosoft.ops.cimp.dao.domain.sys.app.SysAppTableInfo;
import com.sinosoft.ops.cimp.dao.domain.sys.table.SysTableFieldInfo;
import com.sinosoft.ops.cimp.dao.domain.sys.table.SysTableInfo;
import com.sinosoft.ops.cimp.dao.domain.sys.table.SysTableModelInfo;
import com.sinosoft.ops.cimp.dto.sys.table.SysTableFieldInfoDTO;
import com.sinosoft.ops.cimp.dto.sys.table.SysTableInfoDTO;
import com.sinosoft.ops.cimp.dto.sys.table.SysTableModelInfoDTO;
import com.sinosoft.ops.cimp.entity.sys.app.*;
import com.sinosoft.ops.cimp.entity.sys.table.*;
import com.sinosoft.ops.cimp.exception.BusinessException;
import com.sinosoft.ops.cimp.exception.SystemException;
import com.sinosoft.ops.cimp.repository.sys.app.*;
import com.sinosoft.ops.cimp.repository.table.SysTableFieldRepository;
import com.sinosoft.ops.cimp.repository.table.SysTableRepository;
import com.sinosoft.ops.cimp.repository.table.SysTableTypeRepository;
import com.vip.vjtools.vjkit.number.NumberUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class SysTableInfoDaoImpl implements SysTableInfoDao {

    //系统表结构
    private final SysTableTypeRepository sysTableTypeRepository;
    private final SysTableRepository sysTableRepository;
    private final SysTableFieldRepository sysTableFieldRepository;

    //应用表结构
    private final SysAppRepository sysAppRepository;
    private final SysAppTableGroupRepository sysAppTableGroupRepository;
    private final SysAppTableSetRepository sysAppTableSetRepository;
    private final SysAppTableFieldGroupRepository sysAppTableFieldGroupRepository;
    private final SysAppTableFieldSetRepository sysAppTableFieldSetRepository;

    @Autowired
    public SysTableInfoDaoImpl(SysTableTypeRepository sysTableTypeRepository, SysTableRepository sysTableRepository, SysTableFieldRepository sysTableFieldRepository, SysAppRepository sysAppRepository, SysAppTableGroupRepository sysAppTableGroupRepository, SysAppTableSetRepository sysAppTableSetRepository, SysAppTableFieldGroupRepository sysAppTableFieldGroupRepository, SysAppTableFieldSetRepository sysAppTableFieldSetRepository) {
        this.sysTableTypeRepository = sysTableTypeRepository;
        this.sysTableRepository = sysTableRepository;
        this.sysTableFieldRepository = sysTableFieldRepository;
        this.sysAppRepository = sysAppRepository;
        this.sysAppTableGroupRepository = sysAppTableGroupRepository;
        this.sysAppTableSetRepository = sysAppTableSetRepository;
        this.sysAppTableFieldGroupRepository = sysAppTableFieldGroupRepository;
        this.sysAppTableFieldSetRepository = sysAppTableFieldSetRepository;
    }

    @Override
    public SysTableModelInfo getTableInfo(String tableTypeName) throws BusinessException {
        tableTypeName = tableTypeName.trim();

        Object o = CacheManager.getInstance().get(SYS_TABLE_MODEL_INFO, tableTypeName);
        if (o != null) {
            return (SysTableModelInfo) o;
        }
        try {
            Optional<SysTableType> sysTableType = sysTableTypeRepository.findOne(QSysTableType.sysTableType.nameEn.equalsIgnoreCase(tableTypeName));

            if (sysTableType.isPresent()) {
                SysTableType tableType = sysTableType.get();
                String tableTypeId = tableType.getId();
                Iterable<SysTable> sysTables = sysTableRepository.findAll(QSysTable.sysTable.sysTableTypeId.eq(tableTypeId), QSysTable.sysTable.sort.asc());
                //根据表类型id获取所有系统配置表
                List<SysTable> sysTableList = Lists.newArrayList(sysTables);
                List<String> sysTableIds = sysTableList.stream().map(SysTable::getId).collect(Collectors.toList());
                //根据系统表id获取系统表字段
                Iterable<SysTableField> tableFields = sysTableFieldRepository.findAll(QSysTableField.sysTableField.sysTableId.in(sysTableIds), QSysTableField.sysTableField.sort.asc());
                List<SysTableField> sysTableFields = Lists.newArrayList(tableFields);
                SysTableModelInfo sysTableModelInfo = new SysTableModelInfo(tableType, sysTableList, sysTableFields);
                CacheManager.getInstance().put(SYS_TABLE_MODEL_INFO, tableTypeName, sysTableModelInfo);
                return sysTableModelInfo;
            }
        } catch (Exception e) {
            if (e instanceof SystemException) {
                throw e;
            }
            throw new BusinessException(OpsErrorMessage.MODULE_NAME, OpsErrorMessage.ERROR_MESSAGE, "相同英文名的表类型只允许存在一个");
        }
        return null;
    }

    @Override
    public SysTableModelInfoDTO getTableInfo(String tableTypeName, String prjCode) throws BusinessException {
        tableTypeName = tableTypeName.trim();
        prjCode = prjCode.trim();
        //缓存key
        String cacheKey = tableTypeName + prjCode;
        Object o = CacheManager.getInstance().get(SYS_TABLE_MODEL_INFO, cacheKey);

        if (o != null) {
            return (SysTableModelInfoDTO) o;
        }
        SysTableModelInfo tableInfo = this.getTableInfo(tableTypeName);
        //系统属性信息列表
        Map<String, List<SysTableFieldInfo>> tableFieldIdMap = tableInfo.getTableFields().stream().collect(Collectors.groupingBy(SysTableFieldInfo::getId));
        //系统表（信息项）列表
        Map<String, List<SysTableInfo>> tableInfoIdMap = tableInfo.getTables().stream().collect(Collectors.groupingBy(SysTableInfo::getId));

        //TODO 根据项目编号确定出表模型

        Optional<SysApp> sysAppOpt = sysAppRepository.findOne(QSysApp.sysApp.code.eq(prjCode));
        if (!sysAppOpt.isPresent()) {
            throw new BusinessException(OpsErrorMessage.MODULE_NAME, OpsErrorMessage.ERROR_MESSAGE, "项目编号对应的项目不存在");
        }
        //获取app的数据
        SysApp sysApp = sysAppOpt.get();
        String appId = sysApp.getId();
        List<SysAppTableGroup> sysAppTableGroups = Lists.newArrayList(sysAppTableGroupRepository.findAll(QSysAppTableGroup.sysAppTableGroup.sysAppId.eq(appId), QSysAppTableGroup.sysAppTableGroup.sort.asc()));
        Map<String, List<SysAppTableGroup>> sysAppTableGroupIdMap = sysAppTableGroups.stream().collect(Collectors.groupingBy(SysAppTableGroup::getId));

        List<String> sysAppTableGroupIds = sysAppTableGroups.stream().map(SysAppTableGroup::getId).collect(Collectors.toList());
        List<SysAppTableSet> sysAppTableSets = Lists.newArrayList(sysAppTableSetRepository.findAll(QSysAppTableSet.sysAppTableSet.sysAppTableGroupId.in(sysAppTableGroupIds), QSysAppTableSet.sysAppTableSet.sort.asc()));
        Map<String, List<SysAppTableSet>> sysAppTableSetIdMap = sysAppTableSets.stream().collect(Collectors.groupingBy(SysAppTableSet::getId));

        List<String> sysAppTableSetIds = sysAppTableSets.stream().map(SysAppTableSet::getId).collect(Collectors.toList());
        List<SysAppTableFieldGroup> sysAppTableFieldGroups = Lists.newArrayList(sysAppTableFieldGroupRepository.findAll(QSysAppTableFieldGroup.sysAppTableFieldGroup.sysAppTableSetId.in(sysAppTableSetIds), QSysAppTableFieldGroup.sysAppTableFieldGroup.sort.asc()));
        Map<String, List<SysAppTableFieldGroup>> sysAppTableFieldGroupIdMap = sysAppTableFieldGroups.stream().collect(Collectors.groupingBy(SysAppTableFieldGroup::getId));

        List<String> sysAppTableFieldGroupIds = sysAppTableFieldGroups.stream().map(SysAppTableFieldGroup::getId).collect(Collectors.toList());
        long count = sysAppTableFieldSetRepository.count(QSysAppTableFieldSet.sysAppTableFieldSet.sysAppTableFieldGroupId.in(sysAppTableFieldGroupIds));
        List<SysAppTableFieldSet> sysAppTableFieldSets = Lists.newArrayList(
                sysAppTableFieldSetRepository.findAll(QSysAppTableFieldSet.sysAppTableFieldSet.sysAppTableFieldGroupId.in(sysAppTableFieldGroupIds), PageRequest.of(0, NumberUtil.toInt(String.valueOf(count)), Sort.by(Sort.Order.asc(QSysAppTableFieldSet.sysAppTableFieldSet.sort.getMetadata().getName()))))
        );

        //1.将查询出app信息进行模型转换
        List<SysAppTableInfo> sysAppTableInfoList = Lists.newArrayListWithCapacity(sysAppTableFieldSets.size());
        sysAppTableFieldSets.parallelStream().forEach(appField -> {
            //需要转换的对象
            SysAppTableInfo sysAppTableInfo = new SysAppTableInfo();

            String sysAppTableFieldGroupId = appField.getSysAppTableFieldGroupId();
            List<SysAppTableFieldGroup> fieldGroups = sysAppTableFieldGroupIdMap.getOrDefault(sysAppTableFieldGroupId, Lists.newArrayList());

            fieldGroups.forEach(appGroup -> {
                String sysAppTableSetId = appGroup.getSysAppTableSetId();
                List<SysAppTableSet> tableSetList = sysAppTableSetIdMap.getOrDefault(sysAppTableSetId, Lists.newArrayList());

                tableSetList.forEach(tableSet -> {
                    String sysAppTableGroupId = tableSet.getSysAppTableGroupId();

                    List<SysAppTableGroup> appTableGroupList = sysAppTableGroupIdMap.getOrDefault(sysAppTableGroupId, Lists.newArrayList());
                    appTableGroupList.forEach(tableGroup -> {
                        sysAppTableInfo.setAppId(sysApp.getId());
                        sysAppTableInfo.setAppName(sysApp.getName());
                        sysAppTableInfo.setPrjCode(sysApp.getCode());
                        sysAppTableInfo.setAppTableGroupId(tableGroup.getId());
                        sysAppTableInfo.setAppTableGroupName(tableGroup.getName());
                        sysAppTableInfo.setAppTableGroupSort(tableGroup.getSort());
                    });
                    sysAppTableInfo.setAppTableGroupId(sysAppTableGroupId);
                    sysAppTableInfo.setAppTableSetId(tableSet.getId());
                    sysAppTableInfo.setAppTableNameCn(tableSet.getName());
                    sysAppTableInfo.setAppTableNameEn(tableSet.getNameEn());
                    sysAppTableInfo.setAppTableNameSort(tableSet.getSort());
                    sysAppTableInfo.setSysTableId(tableSet.getSysTableId());
                });
                sysAppTableInfo.setAppTableSetId(appGroup.getSysAppTableSetId());
                sysAppTableInfo.setAppTableFieldGroupId(appGroup.getId());
                sysAppTableInfo.setAppTableFieldGroupName(appGroup.getName());
                sysAppTableInfo.setAppTableFieldGroupSort(appGroup.getSort());
            });
            sysAppTableInfo.setAppTableFieldId(appField.getId());
            sysAppTableInfo.setAppTableFieldHtml(appField.getHtml());
            sysAppTableInfo.setAppTableFieldScript(appField.getScript());
            sysAppTableInfo.setAppTableFieldName(appField.getName());
            sysAppTableInfo.setAppTableFieldSort(appField.getSort());
            sysAppTableInfo.setSysTableFieldId(appField.getSysTableFieldId());
            sysAppTableInfoList.add(sysAppTableInfo);
        });

        //2.将app的字段信息关联的系统表字段信息填充
        sysAppTableInfoList.forEach(t -> {
            String sysTableFieldId = t.getSysTableFieldId();
            String sysTableId = t.getSysTableId();
            List<SysTableFieldInfo> fieldInfos = tableFieldIdMap.getOrDefault(sysTableFieldId, Lists.newArrayList());
            if (fieldInfos.size() > 0) {
                SysTableFieldInfo sysTableFieldInfo = fieldInfos.get(0);
                t.setSysTableFieldName(sysTableFieldInfo.getNameEn());
                t.setSysTableFieldHtml(sysTableFieldInfo.getDefaultHtml());
                t.setSysTableFieldScript(sysTableFieldInfo.getDefaultScript());
            }
            List<SysTableInfo> tableInfos = tableInfoIdMap.getOrDefault(sysTableId, Lists.newArrayList());
            if (tableInfos.size() > 0) {
                SysTableInfo sysTableInfo = tableInfos.get(0);
                t.setSysTableNameCn(sysTableInfo.getNameCn());
                t.setSysTableNameEn(sysTableInfo.getNameEn());
            }
        });

        // TODO: 2019/4/12 将得到的数据进行分组得到最终数据


        SysTableModelInfoDTO sysTableModelInfoDTO = this.transform2DTO(prjCode, tableInfo);

        CacheManager.getInstance().put(SYS_TABLE_MODEL_INFO, cacheKey, sysTableModelInfoDTO);
        return sysTableModelInfoDTO;
    }

    private SysTableModelInfoDTO transform2DTO(String prjCode, SysTableModelInfo sysTableModelInfo) {
        SysTableModelInfoDTO dto = new SysTableModelInfoDTO();

        //将tableModeInfo对象转换为dto对象（简单赋值操作）
        dto.setPrimaryField(sysTableModelInfo.getPrimaryKey());
        dto.setPrjCode(prjCode);
        dto.setTableTypeNameCn(sysTableModelInfo.getTableTypeNameCn());
        dto.setTableTypeNameEn(sysTableModelInfo.getTableTypeNameEn());

        //<信息集名称，属性列表>
        Map<String, List<SysTableFieldInfo>> tableNameEnFieldMap = sysTableModelInfo.getTableNameEnAndFieldListMap();
        //<信息集名称，信息集主键字段>
        Map<String, String> subKeyMap = sysTableModelInfo.getSubTableNameEnAndPrimaryKeyMap();

        List<SysTableInfoDTO> tableInfoDTOS = sysTableModelInfo.getTables().stream().map(t -> {
            SysTableInfoDTO tableDTO = new SysTableInfoDTO();
            boolean masterTable = t.isMasterTable();
            tableDTO.setId(t.getId());
            tableDTO.setMasterTable(masterTable);
            tableDTO.setTableNameCn(t.getNameCn());
            String tableNameEn = t.getNameEn();
            tableDTO.setTableNameEn(tableNameEn);
            if (masterTable) {
                tableDTO.setTableNamePK(sysTableModelInfo.getPrimaryKey());
            } else {
                tableDTO.setTableNamePK(subKeyMap.get(tableNameEn));
                tableDTO.setTableNameFK(sysTableModelInfo.getPrimaryKey());
            }

            List<SysTableFieldInfo> sysTableFieldInfos = tableNameEnFieldMap.get(tableNameEn);
            List<SysTableFieldInfoDTO> fieldInfoDTOS = sysTableFieldInfos.stream().map(f -> {
                SysTableFieldInfoDTO fieldDTO = new SysTableFieldInfoDTO();
                fieldDTO.setId(f.getId());
                fieldDTO.setFieldNameCn(f.getNameCn());
                fieldDTO.setFieldNameEn(f.getNameEn());
                fieldDTO.setDefaultHtml(f.getDefaultHtml());
                fieldDTO.setDefaultScript(f.getDefaultScript());
                return fieldDTO;
            }).collect(Collectors.toList());
            tableDTO.setFields(fieldInfoDTOS);
            return tableDTO;
        }).collect(Collectors.toList());

        dto.setTables(tableInfoDTOS);
        return dto;
    }
}
