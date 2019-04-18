package com.sinosoft.ops.cimp.repository.user;

import com.sinosoft.ops.cimp.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Collection;
import java.util.List;

public interface UserRepository extends JpaRepository<User,String>, QuerydslPredicateExecutor<User> {


    List<User> findByReserveCadreIdIn(Collection reserveCadreIdList);

    List<User> findByReserveCadreId(String reserveCadreId);
}
