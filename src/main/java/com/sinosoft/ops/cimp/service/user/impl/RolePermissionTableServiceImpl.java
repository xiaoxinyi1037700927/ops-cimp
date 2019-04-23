package com.sinosoft.ops.cimp.service.user.impl;

import com.google.common.collect.Lists;
import com.querydsl.core.BooleanBuilder;
import com.sinosoft.ops.cimp.dto.PaginationViewModel;
import com.sinosoft.ops.cimp.entity.user.QRolePermissionTable;
import com.sinosoft.ops.cimp.entity.user.RolePermissionTable;
import com.sinosoft.ops.cimp.entity.user.UserRole;
import com.sinosoft.ops.cimp.mapper.user.RolePermissionTableMapper;
import com.sinosoft.ops.cimp.repository.user.RolePermissionTableRepository;
import com.sinosoft.ops.cimp.service.user.RolePermissionTableService;
import com.sinosoft.ops.cimp.util.SecurityUtils;
import com.sinosoft.ops.cimp.vo.from.user.rolePermissionTable.RPTableAddModel;
import com.sinosoft.ops.cimp.vo.from.user.rolePermissionTable.RPTableSearchModel;
import com.sinosoft.ops.cimp.vo.to.user.rolePermissionTable.RPTableModifyModel;
import com.sinosoft.ops.cimp.vo.to.user.rolePermissionTable.RPTableViewModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class RolePermissionTableServiceImpl implements RolePermissionTableService {
    @Autowired
    private RolePermissionTableRepository rolePermissionTableRepository;


    @Override
    public PaginationViewModel<RPTableViewModel> findRPTablePageList(RPTableSearchModel searchModel) {
        int pageIndex = searchModel.getPageIndex();
        int pageSize = searchModel.getPageSize();
        if (pageIndex <= 0) pageIndex = 1;
        if (pageSize <= 0) pageSize = 10;

        QRolePermissionTable qRolePermissionTable = QRolePermissionTable.rolePermissionTable;
        PageRequest pageRequest = PageRequest.of(pageIndex - 1, pageSize, new Sort(Sort.Direction.ASC, qRolePermissionTable.sortNumber.getMetadata().getName()));
        BooleanBuilder builder = new BooleanBuilder();
        builder = builder.and(qRolePermissionTable.roleId.eq(searchModel.getRoleId()));
        Page<RolePermissionTable> all = rolePermissionTableRepository.findAll(builder, pageRequest);

        List<RPTableViewModel> collect = all.getContent().stream().map(x -> {
            RPTableViewModel rpTableViewModel = RolePermissionTableMapper.INSTANCE.rPTableAddModelToViewModel(x);
            if (StringUtils.isEmpty(rpTableViewModel.getName())) {
                rpTableViewModel.setName(x.getNameCN());
            }
            return rpTableViewModel;
        }).collect(Collectors.toList());

        PaginationViewModel<RPTableViewModel> page = new PaginationViewModel<RPTableViewModel>();
        page.setPageIndex(searchModel.getPageIndex());
        page.setPageSize(searchModel.getPageSize());
        page.setTotalCount(all.getTotalElements());
        page.setData(collect);
        return page;
    }

    @Override
    public List<RPTableViewModel> findRPTableListByUserId() {
        List<UserRole> roles = SecurityUtils.getSubject().getCurrentUserRole();
        List<String> roleIds = roles.stream().map(x -> x.getRoleId()).collect(Collectors.toList());

        List<RolePermissionTable> all = rolePermissionTableRepository.findAllByRoleIdInOrderBySortNumberAsc(roleIds);
        LinkedHashMap<String, List<RolePermissionTable>> map = all.stream().collect(Collectors.groupingBy(RolePermissionTable::getTableId, LinkedHashMap::new, Collectors.toList()));
        List<RolePermissionTable> groupList = new ArrayList<>();

        for (String key : map.keySet()) {
            RolePermissionTable rolePermissionTable = map.get(key).get(0);
            groupList.add(rolePermissionTable);
        }

        List<RPTableViewModel> collect = groupList.stream().map(x -> {
            RPTableViewModel rpTableViewModel = RolePermissionTableMapper.INSTANCE.rPTableAddModelToViewModel(x);
            if (StringUtils.isEmpty(rpTableViewModel.getName())) {
                rpTableViewModel.setName(x.getNameCN());
            }
            return rpTableViewModel;
        }).collect(Collectors.toList());
        return collect;
    }

    @Override
    public List<RPTableViewModel> findRPTableListByRoleId(String roleId) {
        List<RolePermissionTable> all = Lists.newArrayList(rolePermissionTableRepository.findAll(QRolePermissionTable.rolePermissionTable.roleId.eq(roleId)));
        List<RPTableViewModel> collect = all.stream().map(x -> RolePermissionTableMapper.INSTANCE.rPTableAddModelToViewModel(x)).collect(Collectors.toList());
        return collect;
    }

    @Override
    public RPTableModifyModel findRPTableById(String id) {
        Optional<RolePermissionTable> rolePermissionTableOptional = rolePermissionTableRepository.findById(id);
        if (!rolePermissionTableOptional.isPresent()) {
            return null;
        }
        RolePermissionTable rolePermissionTable = rolePermissionTableOptional.get();
        RPTableModifyModel rpTableModifyModel = RolePermissionTableMapper.INSTANCE.rPTableAddModelToModifyModel(rolePermissionTable);
        return rpTableModifyModel;
    }

    @Override
    public Boolean saveRPTable(RPTableAddModel addModel) {
        RolePermissionTable rolePermissionTable = RolePermissionTableMapper.INSTANCE.addModelToRPTableAddModel(addModel);
        Integer sortNumber = rolePermissionTableRepository.getSortNumberByRoleId(addModel.getRoleId());
        if (sortNumber != null) {
            sortNumber++;
        } else {
            sortNumber = 0;
        }
        rolePermissionTable.setSortNumber(sortNumber);
        rolePermissionTableRepository.save(rolePermissionTable);
        return true;
    }

    @Override
    public Boolean modifyRPTable(RPTableViewModel modifyModel) {
        Optional<RolePermissionTable> rolePermissionTableOptional = rolePermissionTableRepository.findById(modifyModel.getId());
        if (!rolePermissionTableOptional.isPresent()) {
            return false;
        }
        RolePermissionTable rolePermissionTable = rolePermissionTableOptional.get();
        RolePermissionTableMapper.INSTANCE.modifyRolePermissionTable(modifyModel,rolePermissionTable);
        rolePermissionTableRepository.save(rolePermissionTable);
        return true;
    }

    @Override
    public Boolean deleteRPTable(List<String> ids) {
        for (String id : ids) {
            rolePermissionTableRepository.deleteById(id);
        }
        return true;
    }

    @Override
    public Boolean changeRPTableSort(List<String> ids) {
        List<RolePermissionTable> rolePermissionTables = Lists.newArrayList(rolePermissionTableRepository.findAll(QRolePermissionTable.rolePermissionTable.id.in(ids)));
        if (rolePermissionTables == null || rolePermissionTables.size() != 2) {
            return false;
        }
        RolePermissionTable rolePermissionTable1 = rolePermissionTables.get(0);
        RolePermissionTable rolePermissionTable2 = rolePermissionTables.get(1);

        Integer sortNumber1 = rolePermissionTable1.getSortNumber();
        Integer sortNumber2 = rolePermissionTable2.getSortNumber();

        rolePermissionTable1.setSortNumber(sortNumber2);
        rolePermissionTable2.setSortNumber(sortNumber1);

        rolePermissionTableRepository.save(rolePermissionTable1);
        rolePermissionTableRepository.save(rolePermissionTable2);
        return true;
    }


}
