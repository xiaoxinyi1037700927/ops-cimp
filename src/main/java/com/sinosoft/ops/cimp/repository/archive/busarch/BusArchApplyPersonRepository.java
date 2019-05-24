package com.sinosoft.ops.cimp.repository.archive.busarch;

import com.sinosoft.ops.cimp.entity.archive.BusArchApplyPerson;
import oracle.sql.RAW;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BusArchApplyPersonRepository extends JpaRepository<BusArchApplyPerson, String>, QuerydslPredicateExecutor<BusArchApplyPerson> {

    @Query("select a from BusArchApplyPerson a where a.applyId=?1")
    public List<BusArchApplyPerson>  findAllByApplyId(String applyId);

}
