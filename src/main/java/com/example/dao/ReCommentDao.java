package com.example.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.bean.ReCommentBean;
import com.example.bean.UserBean;

public interface ReCommentDao extends JpaRepository<ReCommentBean, Long> {

    @Query("from ReCommentBean b where b.pid=:pid")
    List<ReCommentBean> findByPid(@Param("pid") Long pid);
    
}
