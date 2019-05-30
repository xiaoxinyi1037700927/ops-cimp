package com.sinosoft.ops.cimp.repository.archive;

import com.sinosoft.ops.cimp.entity.archive.ArchiveMaterialFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface ArchiveMaterialFileRepository extends JpaRepository<ArchiveMaterialFile,String>, QuerydslPredicateExecutor<ArchiveMaterialFile> {

    @Query("select a from ArchiveMaterialFile a where a.archiveMaterialId=?1 order by a.archiveMaterialId,a.id,a.pageNumber")
    public List<ArchiveMaterialFile> findByArchiveMaterialIdOrderByaAndArchiveMaterialIdAndIdAndPageNumber(String archiveMaterialId);

    @Query("select a from ArchiveMaterialFile a where a.archiveMaterialId=?1 and a.pageNumber=?2 and a.fileType=?3")
    public ArchiveMaterialFile findAllByArchiveMaterialIdAndPageNumberAndAndFileType(String archiveMaterialId,Integer pageNumber,String type);

    @Query("select a from ArchiveMaterialFile a where a.archiveMaterialId in (?1) order by a.archiveMaterialId,a.id,a.pageNumber")
    public List<ArchiveMaterialFile> findAllByArchiveMaterialIdOrderByArchiveMaterialIdAndIdAndPageNumber(List<String> idList);




}
