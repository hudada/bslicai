package com.example.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.bean.InTypeBean;
import com.example.bean.SaveBean;

public interface SaveDao extends JpaRepository<SaveBean, Long> {

	@Query("from SaveBean b where b.time<:end and b.time>=:start and b.uid=:uid")
	List<SaveBean> findByTime(@Param("start") Long start, @Param("end") Long end, @Param("uid") Long uid);

	@Query("from SaveBean b where b.time<:end and b.time>=:start and b.flag=:flag and b.uid=:uid")
	List<SaveBean> findByTimeAndFlag(@Param("start") Long start, @Param("end") Long end, @Param("flag") int flag
			, @Param("uid") Long uid);
}
