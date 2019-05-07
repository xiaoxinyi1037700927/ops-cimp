package com.sinosoft.ops.cimp.service.sys.systable.impl;

import com.google.common.collect.Lists;
import com.querydsl.core.BooleanBuilder;
import com.sinosoft.ops.cimp.dto.PaginationViewModel;
import com.sinosoft.ops.cimp.entity.sys.systable.*;
import com.sinosoft.ops.cimp.mapper.sys.systable.SysTableModelMapper;
import com.sinosoft.ops.cimp.repository.sys.systable.SysTableRepository;
import com.sinosoft.ops.cimp.repository.sys.systable.SysTableTypeRepository;
import com.sinosoft.ops.cimp.service.sys.systable.SysTableFieldService;
import com.sinosoft.ops.cimp.service.sys.systable.SysTableService;
import com.sinosoft.ops.cimp.util.DDLUtil;
import com.sinosoft.ops.cimp.vo.from.sys.systable.SysTableAddModel;
import com.sinosoft.ops.cimp.vo.from.sys.systable.SysTableModifyModel;
import com.sinosoft.ops.cimp.vo.from.sys.systable.SysTableSearchModel;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import javax.transaction.Transactional;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SysTableServiceImpl implements SysTableService {

    @Autowired
    private SysTableRepository sysTableDao;

    @Autowired
    private SysTableFieldService sysTableFieldService;

    @Autowired
    private SysTableTypeRepository sysTableTypeRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(SysTableServiceImpl.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    @Transactional
    public boolean addSysTable(SysTableAddModel sysTableAddModel) {
        if (sysTableAddModel == null) {
            return false;
        }
        SysTable sysTable = SysTableModelMapper.INSTANCE.addModelToSysTable(sysTableAddModel);

        sysTable.setCreateTime(new Date());
        sysTableDao.save(sysTable);
        return true;
    }

    @Override
    @Transactional
    public boolean delSysTable(String id) {
        sysTableDao.deleteById(id);
        return true;
    }

    @Override
    @Transactional
    public boolean upSysTable(SysTableModifyModel sysTableModifyModel) {
        if (sysTableModifyModel.getId() == null) {
            return false;
        }
        SysTable sysTable = SysTableModelMapper.INSTANCE.modifyModelToSysTable(sysTableModifyModel);
        sysTable.setModifyTime(new Date());
        sysTableDao.save(sysTable);
        return true;
    }

    @Override
    public boolean operatingDbTable(String sysTableId) {
        Optional<SysTable> sysTableOptional = sysTableDao.findById(sysTableId);
        if (!sysTableOptional.isPresent()) {
            return false;
        }
        SysTable sysTable = sysTableOptional.get();
        List<SysTableField> sysTableFields = sysTableFieldService.getSysTableFieldBySysTableId(sysTable.getId());
        try {
            DataSource dataSource = jdbcTemplate.getDataSource();
            Connection connection = dataSource.getConnection();
            DDLUtil.getCreateTableModel(connection, sysTable.getDbTableName(), sysTableFields);
        } catch (SQLException e) {
            LOGGER.error("数据库表创建出错：" + e);
            return false;
        }
        return true;
    }

    @Override
    public List<SysTableModifyModel> findAllSysTable() {
        List<SysTable> sysTables = sysTableDao.findAll();
        return sysTables.stream().map(SysTableModelMapper.INSTANCE::toModifyModel).collect(Collectors.toList());
    }

    @Override
    public List<SysTableModifyModel> findBySysTableTypeId(String sysTableTypeId) {
        List<SysTable> sysTables = sysTableDao.findBySysTableTypeId(sysTableTypeId);
        return sysTables.stream().map(SysTableModelMapper.INSTANCE::toModifyModel).collect(Collectors.toList());
    }

    @Override
    public PaginationViewModel<SysTableModifyModel> getSysTableByPage(SysTableSearchModel searchModel) {
        int pageIndex = searchModel.getPageIndex();
        int pageSize = searchModel.getPageSize();
        if (pageIndex <= 0) pageIndex = 1;
        if (pageSize <= 0) pageSize = 10;

        QSysTable qSysTable = QSysTable.sysTable;
        PageRequest pageRequest = PageRequest.of(pageIndex - 1, pageSize, new Sort(Sort.Direction.ASC, qSysTable.sort.getMetadata().getName()));
        BooleanBuilder builder = new BooleanBuilder();
        if (StringUtils.isNotEmpty(searchModel.getSysTableTypeId())) {
            builder = builder.and(qSysTable.sysTableTypeId.eq(searchModel.getSysTableTypeId()));
        }

        if (StringUtils.isNotEmpty(searchModel.getNameCn())) {
            builder = builder.and(qSysTable.nameCn.contains(searchModel.getNameCn()));
        }
        Page<SysTable> all = sysTableDao.findAll(builder, pageRequest);
        List<SysTableModifyModel> collect = all.getContent().stream()
                .map(SysTableModelMapper.INSTANCE::toModifyModel).collect(Collectors.toList());

        PaginationViewModel<SysTableModifyModel> page = new PaginationViewModel<>();
        page.setPageIndex(searchModel.getPageIndex());
        page.setPageSize(searchModel.getPageSize());
        page.setTotalCount(all.getTotalElements());
        page.setData(collect);
        return page;
    }

    @Override
    public List<SysTableModifyModel> getSysTableByTableTypeNameEn(String tableTypeNameEn) {

        if (StringUtils.isNotEmpty(tableTypeNameEn)) {
            Optional<SysTableType> sysTableType = sysTableTypeRepository.findOne(QSysTableType.sysTableType.nameEn.eq(tableTypeNameEn));
            if (sysTableType.isPresent()) {
                SysTableType sysTableType1 = sysTableType.get();
                String sysTableTypeId = sysTableType1.getId();

                List<SysTable> sysTables = Lists.newArrayList(sysTableDao.findAll(QSysTable.sysTable.sysTableTypeId.eq(sysTableTypeId), QSysTable.sysTable.sort.asc()));
                return sysTables.stream().map(SysTableModelMapper.INSTANCE::toModifyModel).collect(Collectors.toList());
            }
        }
        return Lists.newArrayList();
    }
}
