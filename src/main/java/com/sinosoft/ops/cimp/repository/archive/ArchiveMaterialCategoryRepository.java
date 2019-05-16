package com.sinosoft.ops.cimp.repository.archive;

import com.sinosoft.ops.cimp.entity.archive.ArchiveMaterialCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArchiveMaterialCategoryRepository extends JpaRepository<ArchiveMaterialCategory,String>, QuerydslPredicateExecutor<ArchiveMaterialCategory> {


    @Query("select ArchiveMaterialCategory from ArchiveMaterialCategory where parentCode=?1 order by sn")
    public List<ArchiveMaterialCategory> getCategorysByCode(String parentCode);
    @Query("select ArchiveMaterialCategory from ArchiveMaterialCategory where parentCode='00' order by sn")
    public List<ArchiveMaterialCategory> getCategorysByCode();

    @Query(value = "select b.* from ARCHIVE_MATERIAL_CATEGORY b ,(select  a.code from  ARCHIVE_MATERIAL_CATEGORY  a where a.id=:categoryId) c where b.parent_code= c.code order by b.sn",nativeQuery=true)
    public List<ArchiveMaterialCategory> findArchiveMaterialCategorysByCategoryId(String categoryId);
}