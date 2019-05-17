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



}
