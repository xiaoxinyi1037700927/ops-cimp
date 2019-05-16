package com.sinosoft.ops.cimp.repository.archive;

import com.sinosoft.ops.cimp.entity.archive.ArchiveMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArchiveMaterialRepository extends JpaRepository<ArchiveMaterial,String>, QuerydslPredicateExecutor<ArchiveMaterial> {



        @Query("select ArchiveMaterial from ArchiveMaterial where empId=?1 order by categoryId,seqNum")
        public List<ArchiveMaterial> getArchiveMaterialListByEmpId(String empId);

        @Query("select ArchiveMaterial from ArchiveMaterial where archiveId=?1 order by categoryId,seqNum")
        public List<ArchiveMaterial> getArchiveMaterialListByArchId(String archiveId);



        @Query("select id from ArchiveMaterial  where empId=?1 and categoryId=?2")
        public List<String> getArchiveMaterialIDs(String empId,String categoryId);

        @Query("select ArchiveMaterial from ArchiveMaterial  where empId=?1 and categoryId=?2 order by seqNum")
        public List<ArchiveMaterial> getArchiveListByEmpIdAndCategoryId(String empId,String categoryId);

        @Query("select t1.A36013 from EMP_A36 t1 where ( 1=1 ) and t1.STATUS=0 and upper(t1.EMP_ID)=?1")
        public List<String> getIdCardsByEmpid(String empId);

        @Query("select  a.a001003 from  emp_a001 a where upper(a.emp_id)=?1 and a.STATUS=0")
        public String getidcardforone(String empId);

        @Query("SELECT  EMP_ID FROM  SYS_USER  WHERE ID=?1 AND STATUS=0")
        public String findEmpidByUserID(String userid);
}
