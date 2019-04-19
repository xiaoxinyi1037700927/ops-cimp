package com.sinosoft.ops.cimp.service.user.impl;

import com.google.common.collect.Lists;
import com.sinosoft.ops.cimp.dto.PaginationViewModel;
import com.sinosoft.ops.cimp.entity.user.QRolePermissionPageSql;
import com.sinosoft.ops.cimp.entity.user.RolePermissionPageSql;
import com.sinosoft.ops.cimp.mapper.user.RolePermissionPageSqlMapper;
import com.sinosoft.ops.cimp.repository.user.RolePermissionPageSqlRepository;
import com.sinosoft.ops.cimp.service.user.RolePermissionPageSqlService;
import com.sinosoft.ops.cimp.vo.from.user.rolePermissionPageSql.RPPageSqlAddModel;
import com.sinosoft.ops.cimp.vo.from.user.rolePermissionPageSql.RPPageSqlSearchModel;
import com.sinosoft.ops.cimp.vo.to.user.rolePermissionPageSql.RPPageSqlViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class RolePermissionPageSqlServiceImpl implements RolePermissionPageSqlService {
    @Autowired
    private RolePermissionPageSqlRepository rolePermissionPageSqlRepository;


    @Override
    public PaginationViewModel<RPPageSqlViewModel> findRPPageSqlPageList(RPPageSqlSearchModel searchModel) {
        int pageIndex = searchModel.getPageIndex();
        int pageSize = searchModel.getPageSize();
        if (pageIndex <= 0) pageIndex = 1;
        if (pageSize <= 0) pageSize = 10;

        QRolePermissionPageSql qRolePermissionPageSql = QRolePermissionPageSql.rolePermissionPageSql;
//        PageRequest pageRequest = PageRequest.of(pageIndex - 1, pageSize, new Sort(Sort.Direction.ASC, qSysTableField.sort.getMetadata().getName()));
        PageRequest pageRequest = PageRequest.of(pageIndex - 1, pageSize);
        Page<RolePermissionPageSql> all = rolePermissionPageSqlRepository.findAll(pageRequest);
        List<RPPageSqlViewModel> collect = all.getContent().stream().map(rolePermissionPageSql -> RolePermissionPageSqlMapper.INSTANCE.rPPageSqlToViewModel(rolePermissionPageSql)).collect(Collectors.toList());

        PaginationViewModel<RPPageSqlViewModel> page = new PaginationViewModel<RPPageSqlViewModel>();
        page.setPageIndex(searchModel.getPageIndex());
        page.setPageSize(searchModel.getPageSize());
        page.setTotalCount(all.getTotalElements());
        page.setData(collect);
        return page;
    }

    @Override
    public RPPageSqlViewModel findRPPageSqlByRoleId(String roleId) {

        Optional<RolePermissionPageSql> rolePermissionPageSqlOptional = rolePermissionPageSqlRepository.findOne(QRolePermissionPageSql.rolePermissionPageSql.roleId.eq(roleId));
        if (!rolePermissionPageSqlOptional.isPresent()) {
            return null;
        }
        RolePermissionPageSql rolePermissionPageSql = rolePermissionPageSqlOptional.get();
        RPPageSqlViewModel rpPageSqlViewModel = RolePermissionPageSqlMapper.INSTANCE.rPPageSqlToViewModel(rolePermissionPageSql);

        return rpPageSqlViewModel;
    }

    @Override
    public List<RPPageSqlViewModel> findRPPageSqlListByRoleIds(RPPageSqlSearchModel searchModel) {
        List<String> roleIds = searchModel.getRoleIds();
        List<RolePermissionPageSql> all = Lists.newArrayList(rolePermissionPageSqlRepository.findAll(QRolePermissionPageSql.rolePermissionPageSql.roleId.in(roleIds)));
        List<RPPageSqlViewModel> collect = all.stream().map(x -> RolePermissionPageSqlMapper.INSTANCE.rPPageSqlToViewModel(x)).collect(Collectors.toList());
        return collect;
    }

    @Override
    public Boolean saveRPPageSql(RPPageSqlAddModel addModel) {
        RolePermissionPageSql rolePermissionPageSql = RolePermissionPageSqlMapper.INSTANCE.viewModelToRPPageSql(addModel);
        rolePermissionPageSqlRepository.save(rolePermissionPageSql);
        return true;
    }

    @Override
    public Boolean modifyRPPageSql(RPPageSqlViewModel modifyModel) {
        Optional<RolePermissionPageSql> rolePermissionPageSqlOptional = rolePermissionPageSqlRepository.findById(modifyModel.getId());
        if (!rolePermissionPageSqlOptional.isPresent()) {
            return  false;
        }
        RolePermissionPageSql rolePermissionPageSql = rolePermissionPageSqlOptional.get();
        RolePermissionPageSqlMapper.INSTANCE.modifyToRolePermissionPageSql(modifyModel,rolePermissionPageSql);
        rolePermissionPageSqlRepository.save(rolePermissionPageSql);
        return true;
    }

    @Override
    public Boolean deleteRPPageSql(List<String> ids) {
        for (String id : ids) {
            rolePermissionPageSqlRepository.deleteById(id);
        }
        return true;
    }


}
