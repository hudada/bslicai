package com.example.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.bean.UserBean;

public interface UserDao extends JpaRepository<UserBean, Long> {

    @Query("from UserBean b where b.number=:number")
    UserBean findUserByNumber(@Param("number") String number);
    
    @Query("from UserBean b where b.number=:number and b.pwd=:pwd")
    UserBean findUserByNumberAndPwd(@Param("number") String number,@Param("pwd") String pwd);
}
