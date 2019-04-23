package com.sinosoft.ops.cimp.mapper.user;

import com.sinosoft.ops.cimp.entity.user.UserCollectionTable;
import com.sinosoft.ops.cimp.util.SecurityUtils;
import com.sinosoft.ops.cimp.vo.from.user.userCollectionTable.UCTableAddModel;
import com.sinosoft.ops.cimp.vo.to.user.userCollectionTable.UCTableModifyModel;
import com.sinosoft.ops.cimp.vo.to.user.userCollectionTable.UCTableViewModel;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Mapper
public interface UserCollectionTableMapper {
    UserCollectionTableMapper INSTANCE = Mappers.getMapper(UserCollectionTableMapper.class);

    @Mappings({
            @Mapping(source = "createId", target = "createId", qualifiedByName = "getCreateId"),
            @Mapping(source = "createTime", target = "createTime", qualifiedByName ="getCreateTime" )
    })
    UserCollectionTable addModelToUserCollectionTable(UCTableAddModel addModel);

    UCTableViewModel userCollectionTableToViewModel(UserCollectionTable userCollectionTable);

    UCTableModifyModel userCollectionTableToModiyModel(UserCollectionTable userCollectionTable);

    @Mappings({
            @Mapping(source = "modifyId", target = "modifyId", qualifiedByName = "getCreateId"),
            @Mapping(source = "modifyTime", target = "modifyTime", qualifiedByName ="getCreateTime" )
    })
    void modifyUserCollectionTable(UCTableViewModel modifyModel, @MappingTarget UserCollectionTable userCollectionTable);



    @Named(value = "getCreateId")
    default String getCreateId(String id) {
        return SecurityUtils.getSubject().getCurrentUser().getId();
    }

    @Named(value = "getCreateTime")
    default Date getCreateTime(Date modifyTime) {
        return Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
    }
}
