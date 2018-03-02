package com.example.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.bean.CommentBean;
import com.example.bean.UserBean;

public interface CommentDao extends JpaRepository<CommentBean, Long> {

    @Query("from CommentBean b order by b.time desc")
    List<CommentBean> findByTime();
    
}
