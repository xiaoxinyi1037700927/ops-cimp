package com.sinosoft.ops.cimp.service.user.impl;

import com.google.common.collect.Lists;
import com.querydsl.core.BooleanBuilder;
import com.sinosoft.ops.cimp.dto.PaginationViewModel;
import com.sinosoft.ops.cimp.entity.user.QUserCollectionTable;
import com.sinosoft.ops.cimp.entity.user.UserCollectionTable;
import com.sinosoft.ops.cimp.mapper.user.UserCollectionTableMapper;
import com.sinosoft.ops.cimp.repository.user.UserCollectionTableRepository;
import com.sinosoft.ops.cimp.service.user.UserCollectionTableService;
import com.sinosoft.ops.cimp.util.SecurityUtils;
import com.sinosoft.ops.cimp.vo.from.user.userCollectionTable.UCTableAddModel;
import com.sinosoft.ops.cimp.vo.from.user.userCollectionTable.UCTableSearchModel;
import com.sinosoft.ops.cimp.vo.to.user.userCollectionTable.UCTableModifyModel;
import com.sinosoft.ops.cimp.vo.to.user.userCollectionTable.UCTableViewModel;
import org.apache.commons.lang3.StringUtils;
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
public class UserCollectionTableServiceImpl implements UserCollectionTableService {
    @Autowired
    private UserCollectionTableRepository userCollectionTableRepository;

    @Override
    public PaginationViewModel<UCTableViewModel> findUCTablePageList(UCTableSearchModel searchModel) {
        int pageIndex = searchModel.getPageIndex();
        int pageSize = searchModel.getPageSize();
        if (pageIndex <= 0) pageIndex = 1;
        if (pageSize <= 0) pageSize = 10;

        QUserCollectionTable qUserCollectionTable = QUserCollectionTable.userCollectionTable;
        PageRequest pageRequest = PageRequest.of(pageIndex - 1, pageSize, new Sort(Sort.Direction.ASC, qUserCollectionTable.sortNumber.getMetadata().getName()));
        BooleanBuilder builder = new BooleanBuilder();
        builder = builder.and(qUserCollectionTable.userId.eq(searchModel.getUserId()));
        Page<UserCollectionTable> all = userCollectionTableRepository.findAll(builder, pageRequest);
        List<UCTableViewModel> collect = all.getContent().stream().map(x -> {
            UCTableViewModel ucTableViewModel = UserCollectionTableMapper.INSTANCE.userCollectionTableToViewModel(x);
            if (StringUtils.isEmpty(ucTableViewModel.getName())) {
                ucTableViewModel.setName(x.getNameCN());
            }
            return ucTableViewModel;
        }).collect(Collectors.toList());

        PaginationViewModel<UCTableViewModel> page = new PaginationViewModel<UCTableViewModel>();
        page.setPageIndex(searchModel.getPageIndex());
        page.setPageSize(searchModel.getPageSize());
        page.setTotalCount(all.getTotalElements());
        page.setData(collect);
        return page;
    }

    @Override
    public List<UCTableViewModel> findUCTableListByUserId(String userId) {
        List<UserCollectionTable> allByUserIdOrderBySortNumberAsc = userCollectionTableRepository.findAllByUserIdOrderBySortNumberAsc(userId);
        List<UCTableViewModel> collect = allByUserIdOrderBySortNumberAsc.stream().map(x -> {
            UCTableViewModel ucTableViewModel = UserCollectionTableMapper.INSTANCE.userCollectionTableToViewModel(x);
            if (StringUtils.isEmpty(ucTableViewModel.getName())) {
                ucTableViewModel.setName(x.getNameCN());
            }
            return ucTableViewModel;
        }).collect(Collectors.toList());
        return collect;
    }

    @Override
    public UCTableModifyModel findUCTableById(String id) {
        Optional<UserCollectionTable> userCollectionTableOptional = userCollectionTableRepository.findById(id);
        if (!userCollectionTableOptional.isPresent()) {
            return null;
        }
        UserCollectionTable userCollectionTable = userCollectionTableOptional.get();
        UCTableModifyModel ucTableModifyModel = UserCollectionTableMapper.INSTANCE.userCollectionTableToModiyModel(userCollectionTable);
        return ucTableModifyModel;
    }

    @Override
    public Boolean saveUCTable(UCTableAddModel addModel) {
        UserCollectionTable userCollectionTable = UserCollectionTableMapper.INSTANCE.addModelToUserCollectionTable(addModel);
        Integer sortNumber = userCollectionTableRepository.getSortNumberByUserId(addModel.getUserId());
        if (sortNumber != null) {
            sortNumber++;
        } else {
            sortNumber = 0;
        }
        userCollectionTable.setSortNumber(sortNumber);
        userCollectionTableRepository.save(userCollectionTable);
        return true;
    }

    @Override
    public Boolean modifyUCTable(UCTableViewModel modifyModel) {
        Optional<UserCollectionTable> userCollectionTableOptional = userCollectionTableRepository.findById(modifyModel.getId());
        if (!userCollectionTableOptional.isPresent()) {
            return false;
        }
        UserCollectionTable userCollectionTable = userCollectionTableOptional.get();
        UserCollectionTableMapper.INSTANCE.modifyUserCollectionTable(modifyModel,userCollectionTable);
        userCollectionTableRepository.save(userCollectionTable);
        return true;
    }

    @Override
    public Boolean deleteUCTable(List<String> ids) {
        String userId = SecurityUtils.getSubject().getCurrentUser().getId();
        Iterable<UserCollectionTable> all = userCollectionTableRepository.findAll(QUserCollectionTable.userCollectionTable.userId.eq(userId)
                .and(QUserCollectionTable.userCollectionTable.tableId.in(ids)));
        userCollectionTableRepository.deleteAll(all);

        return true;
    }

    @Override
    public Boolean changeUCTableSort(List<String> ids) {
        List<UserCollectionTable> userCollectionTables = Lists.newArrayList(userCollectionTableRepository.findAll(QUserCollectionTable.userCollectionTable.id.in(ids)));
        if (userCollectionTables == null || userCollectionTables.size() != 2) {
            return false;
        }
        UserCollectionTable userCollectionTable1 = userCollectionTables.get(0);
        UserCollectionTable userCollectionTable2 = userCollectionTables.get(1);
        Integer sortNumber1 = userCollectionTable1.getSortNumber();
        Integer sortNumber2 = userCollectionTable2.getSortNumber();
        userCollectionTable1.setSortNumber(sortNumber2);
        userCollectionTable2.setSortNumber(sortNumber1);
        userCollectionTableRepository.save(userCollectionTable1);
        userCollectionTableRepository.save(userCollectionTable2);
        return true;
    }
}
