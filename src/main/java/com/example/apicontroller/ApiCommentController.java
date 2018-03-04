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
import com.example.bean.UserBean;
import com.example.dao.CommentDao;
import com.example.dao.ReCommentDao;
import com.example.dao.UserDao;
import com.example.utils.ResultUtils;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/api/comment")
public class ApiCommentController {

	@Value("${bs.imagesPath}")
	private String location;
	@Autowired
	private ResourceLoader resourceLoader;
	@Autowired
	private CommentDao commentDao;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public BaseBean<List<CommentBean>> list() {
		return ResultUtils.resultSucceed(commentDao.findByTime());
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public BaseBean<CommentBean> add(HttpServletRequest request) {
		String name = request.getParameter("name");
		Long uid = Long.parseLong(request.getParameter("uid"));
		String content = request.getParameter("content");
		CommentBean bean = new CommentBean();
		bean.setContent(content);
		bean.setName(name);
		bean.setTime(new Date().getTime());
		bean.setUid(uid);
		return ResultUtils.resultSucceed(commentDao.save(bean));
	}

	@RequestMapping(value = "/addwithimg", method = RequestMethod.POST)
	public BaseBean<CommentBean> uploadImg(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
		String name = request.getParameter("name");
		Long uid = Long.parseLong(request.getParameter("uid"));
		String content = request.getParameter("content");
		if (!file.isEmpty()) {
			try {
				String path = uid + "_" + System.currentTimeMillis() + "." + file.getOriginalFilename().split("\\.")[1];
				
				File root = new File(location);
		        if (!root.exists()) {
		        	root.mkdirs();
		        }
				
				
				Files.copy(file.getInputStream(), Paths.get(location, path));

				CommentBean bean = new CommentBean();
				bean.setContent(content);
				bean.setName(name);
				bean.setTime(new Date().getTime());
				bean.setUid(uid);
				bean.setImg("/img/" + path);
				return ResultUtils.resultSucceed(commentDao.save(bean));
			} catch (IOException | RuntimeException e) {
				return ResultUtils.resultError("");
			}
		} else {
			CommentBean bean = new CommentBean();
			bean.setContent(content);
			bean.setName(name);
			bean.setTime(new Date().getTime());
			bean.setUid(uid);
			return ResultUtils.resultSucceed(commentDao.save(bean));
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/img/{filename:.+}")
	public ResponseEntity<?> getFile(@PathVariable String filename) {
		try {
			return ResponseEntity.ok(resourceLoader.getResource("file:" + Paths.get(location, filename).toString()));
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
	}
}
