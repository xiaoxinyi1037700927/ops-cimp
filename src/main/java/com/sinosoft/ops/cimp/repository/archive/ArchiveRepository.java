package com.sinosoft.ops.cimp.repository.archive;

import com.sinosoft.ops.cimp.entity.archive.Archive;
import org.hibernate.sql.Select;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArchiveRepository extends JpaRepository<Archive,String> , QuerydslPredicateExecutor<Archive> {


    @Query("select Archive from Archive  where personId=?1")
    public Archive getArchivebyEmpID(String persionId);
    @Query("select Archive from Archive  where archiveCode=?1")
    public List<Archive> getArchivebyCardNum(String CardNum);

    @Query(nativeQuery=true,value = "update archive_material temp set emp_id =(select t1.emp_id from ARCHIVE tinner join emp_a001 a on A001003 = archive_code and t.person_id!=a.emp_id inner join archive_material t1 on t.id=t1.archive_id where temp.id=t1.id) where exists ( select t1.emp_id from ARCHIVE t inner join emp_a001 a on A001003 = archive_code and t.person_id!=a.emp_id inner join archive_material t1 on t.id=t1.archive_id where temp.id=t1.id)")
    void DealArchLinkEmp();

    @Query(nativeQuery=true,value = "update ARCHIVE temp set person_id =( select a.emp_id from ARCHIVE t inner join emp_a001 a on A001003 = archive_code and t.person_id!=a.emp_id where temp.id=t.id) where exists ( select a.emp_id from ARCHIVE t inner join emp_a001 a on A001003 = archive_code and t.person_id!=a.emp_id where temp.id=t.id)")
    void DealArchLinkEmp1();
}
