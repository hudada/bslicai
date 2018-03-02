package com.example.apicontroller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.bean.BaseBean;
import com.example.bean.InTypeBean;
import com.example.bean.OutTypeBean;
import com.example.bean.SaveBean;
import com.example.bean.SaveInfoBean;
import com.example.bean.StatisticsBean;
import com.example.bean.UserBean;
import com.example.dao.InTypeDao;
import com.example.dao.OutTypeDao;
import com.example.dao.SaveDao;
import com.example.dao.UserDao;
import com.example.utils.ResultUtils;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/api/money")
public class ApiMoneyController {

	@Autowired
	private UserDao userDao;
	@Autowired
	private SaveDao saveDao;
	@Autowired
	private InTypeDao inTypeDao;
	@Autowired
	private OutTypeDao outTypeDao;

	@RequestMapping(value = "/add", method = RequestMethod.POST, produces = "application/json")
	public BaseBean<SaveBean> add(@RequestBody SaveBean bean) {
		return ResultUtils.resultSucceed(saveDao.save(bean));
	}

	@RequestMapping(value = "/del/{id}", method = RequestMethod.POST)
	public BaseBean<SaveBean> del(@PathVariable String id) {
		saveDao.delete(Long.parseLong(id));
		return ResultUtils.resultSucceed("");
	}

	@RequestMapping(value = "/list/{start}/{end}", method = RequestMethod.GET)
	public BaseBean<SaveInfoBean> list(@PathVariable String start, @PathVariable String end) {
		long startL = Long.parseLong(start);
		long endL = Long.parseLong(end);
		List<SaveBean> data = (List<SaveBean>) ResultUtils.resultSucceed(saveDao.findByTime(startL, endL));
		if (data == null || data.size() <= 0) {
			return ResultUtils.resultError("");
		} else {
			Double totOut = 0.0, totIn = 0.0, totTwo = 0.0;
			for (SaveBean saveBean : data) {
				if (saveBean.getFlag() == 0) {
					totOut += saveBean.getMoney();
				} else {
					totIn += saveBean.getMoney();
				}
			}
			totTwo = totIn - totOut;
			SaveInfoBean bean = new SaveInfoBean();
			bean.setData(data);
			bean.setTotIn(totIn);
			bean.setTotOut(totOut);
			bean.setTotTwo(totTwo);
			return ResultUtils.resultSucceed(bean);
		}
	}

	@RequestMapping(value = "/typeIn", method = RequestMethod.GET)
	public BaseBean<List<InTypeBean>> typeIn() {
		return ResultUtils.resultSucceed(inTypeDao.findAll());
	}

	@RequestMapping(value = "/typeOut", method = RequestMethod.GET)
	public BaseBean<List<OutTypeBean>> typeOut() {
		return ResultUtils.resultSucceed(outTypeDao.findAll());
	}

	@RequestMapping(value = "/statistics/{start}/{end}/{type}", method = RequestMethod.GET)
	public BaseBean<List<StatisticsBean>> statistics(@PathVariable String start, @PathVariable String end,
			@PathVariable String type) {
		long startL = Long.parseLong(start);
		long endL = Long.parseLong(end);
		int typeI = Integer.parseInt(type);
		List<SaveBean> data = null;
		List<StatisticsBean> statisticsBeans = new ArrayList<>();
		switch (typeI) {
		case -1:
			data = (List<SaveBean>) ResultUtils.resultSucceed(saveDao.findByTime(startL, endL));
			if (data != null && data.size() > 0) {
				Double totOut = 0.0, totIn = 0.0;
				for (SaveBean saveBean : data) {
					if (saveBean.getFlag() == 0) {
						totOut += saveBean.getMoney();
					} else {
						totIn += saveBean.getMoney();
					}
				}
				StatisticsBean out = new StatisticsBean();
				out.setName("out");
				out.setMoney(totOut + "");
				statisticsBeans.add(out);

				StatisticsBean in = new StatisticsBean();
				in.setName("in");
				in.setMoney(totIn + "");
				statisticsBeans.add(in);
			}
			break;
		case 0:
			data = (List<SaveBean>) ResultUtils.resultSucceed(saveDao.findByTimeAndFlag(startL, endL, 0));
			if (data != null && data.size() > 0) {
				HashMap<String, Double> map = new HashMap<>();
				for (SaveBean saveBean : data) {
					if (map.containsKey(saveBean.getName())) {
						Double money = map.get(saveBean.getName());
						money += saveBean.getMoney();
						map.put(saveBean.getName(), money);
					} else {
						map.put(saveBean.getName(), saveBean.getMoney());
					}
				}
				for (String name : map.keySet()) {
					StatisticsBean bean = new StatisticsBean();
					bean.setName(name);
					bean.setMoney(map.get(name) + "");
					statisticsBeans.add(bean);
				}
			}
			break;
		case 1:
			data = (List<SaveBean>) ResultUtils.resultSucceed(saveDao.findByTimeAndFlag(startL, endL, 1));
			if (data != null && data.size() > 0) {
				HashMap<String, Double> map = new HashMap<>();
				for (SaveBean saveBean : data) {
					if (map.containsKey(saveBean.getName())) {
						Double money = map.get(saveBean.getName());
						money += saveBean.getMoney();
						map.put(saveBean.getName(), money);
					} else {
						map.put(saveBean.getName(), saveBean.getMoney());
					}
				}
				for (String name : map.keySet()) {
					StatisticsBean bean = new StatisticsBean();
					bean.setName(name);
					bean.setMoney(map.get(name) + "");
					statisticsBeans.add(bean);
				}
			}
			break;
		}

		if (statisticsBeans.size() > 0) {
			return ResultUtils.resultSucceed(statisticsBeans);
		} else {
			return ResultUtils.resultError("");
		}
	}

}
