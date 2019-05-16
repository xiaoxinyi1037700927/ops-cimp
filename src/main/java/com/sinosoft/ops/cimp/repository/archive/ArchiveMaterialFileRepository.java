package com.sinosoft.ops.cimp.repository.archive;

import com.sinosoft.ops.cimp.entity.archive.ArchiveMaterialFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import javax.persistence.Id;
import java.util.List;

@Repository
public interface ArchiveMaterialFileRepository extends JpaRepository<ArchiveMaterialFile,String>, QuerydslPredicateExecutor<ArchiveMaterialFile> {

    @Query("select ArchiveMaterialFile from ArchiveMaterialFile  where archiveMaterialId=?1 order by archiveMaterialId,id,pageNumber")
    public List<ArchiveMaterialFile> getArchiveMaterialFilebyAchiveMaterialID(String IDofAchiveMaterial);

    @Query("select ArchiveMaterialFile from ArchiveMaterialFile  where archiveMaterialId=?1 and pageNumber=?2 and fileType=?3")
    public ArchiveMaterialFile findbyPageNo(String archiveMaterialId,String pageNumber,String type);

    @Query("select ArchiveMaterialFile from ArchiveMaterialFile  where archiveMaterialId in (?1) order by archiveMaterialId,id,pageNumber")
    public List<ArchiveMaterialFile> getFilebyAchiveMaterialIDs(List<String> idList);




}
