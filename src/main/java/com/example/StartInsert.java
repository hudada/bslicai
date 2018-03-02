package com.example;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.example.bean.InTypeBean;
import com.example.bean.OutTypeBean;
import com.example.dao.InTypeDao;
import com.example.dao.OutTypeDao;
import com.example.dao.UserDao;

@Component
@Order(value = 1)
public class StartInsert implements CommandLineRunner {

	@Autowired
	private InTypeDao inTypeDao;
	@Autowired
	private OutTypeDao outTypeDao;

	@Override
	public void run(String... arg0) throws Exception {
		if (inTypeDao.findAll().size() <= 0) {
			String[] outNames = { "餐饮", "生活", "学习", "服装", "其他" };
			String[] inNames = { "工资", "奖金", "红包", "其他" };
			List<InTypeBean> inData = new ArrayList<>();
			List<OutTypeBean> outData = new ArrayList<>();

			for (String string : outNames) {
				OutTypeBean bean = new OutTypeBean();
				bean.setName(string);
				outData.add(bean);
			}
			outTypeDao.save(outData);

			for (String string : inNames) {
				InTypeBean bean = new InTypeBean();
				bean.setName(string);
				inData.add(bean);
			}
			inTypeDao.save(inData);
			
		}
	}

}
