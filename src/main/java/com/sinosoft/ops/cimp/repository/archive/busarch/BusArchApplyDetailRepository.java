package com.sinosoft.ops.cimp.repository.archive.busarch;

import com.sinosoft.ops.cimp.entity.archive.BusArchApplyDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BusArchApplyDetailRepository extends JpaRepository<BusArchApplyDetail, String>, QuerydslPredicateExecutor<BusArchApplyDetail> {

    @Query("select a from BusArchApplyDetail a where a.personid=?1 ")
    public List<BusArchApplyDetail> findAllByPersonid(String personid);
}
