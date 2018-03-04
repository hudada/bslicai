package com.example.apicontroller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.bean.BaseBean;
import com.example.bean.CommentBean;
import com.example.bean.ReCommentBean;
import com.example.bean.UserBean;
import com.example.dao.CommentDao;
import com.example.dao.ReCommentDao;
import com.example.dao.UserDao;
import com.example.utils.ResultUtils;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/api/recomment")
public class ApiReCommentController {

	@Autowired
	private ReCommentDao reCommentDao;

	@RequestMapping(value = "/list/{pid}", method = RequestMethod.GET)
	public BaseBean<List<ReCommentBean>> list(@PathVariable String pid) {
		return ResultUtils.resultSucceed(reCommentDao.findByPid(Long.parseLong(pid)));
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public BaseBean<ReCommentBean> add(HttpServletRequest request) {
		ReCommentBean bean = new ReCommentBean();
		bean.setContent(request.getParameter("content"));
		bean.setName(request.getParameter("name"));
		bean.setPid(Long.parseLong(request.getParameter("pid")));
		bean.setTime(Long.parseLong(request.getParameter("time")));
		bean.setUid(Long.parseLong(request.getParameter("uid")));
		return ResultUtils.resultSucceed(reCommentDao.save(bean));
	}

}
