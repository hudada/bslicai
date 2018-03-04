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

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public BaseBean<SaveBean> add(HttpServletRequest request) {
		SaveBean bean = new SaveBean();
		bean.setAddress(request.getParameter("address"));
		bean.setFlag(Integer.parseInt(request.getParameter("flag")) );
		bean.setMoney(Double.parseDouble(request.getParameter("money")));
		bean.setName(request.getParameter("name"));
		bean.setTime(Long.parseLong(request.getParameter("time")));
		bean.setUid(Long.parseLong(request.getParameter("uid")));
		return ResultUtils.resultSucceed(saveDao.save(bean));
	}

	@RequestMapping(value = "/del", method = RequestMethod.POST)
	public BaseBean<SaveBean> del(HttpServletRequest request) {
		saveDao.delete(Long.parseLong(request.getParameter("id")));
		return ResultUtils.resultSucceed("");
	}

	@RequestMapping(value = "/list/{start}/{end}/{uid}", method = RequestMethod.GET)
	public BaseBean<SaveInfoBean> list(@PathVariable String start, @PathVariable String end
			, @PathVariable String uid) {
		long startL = Long.parseLong(start);
		long endL = Long.parseLong(end);
		long uidL= Long.parseLong(uid);
		List<SaveBean> data = (List<SaveBean>)saveDao.findByTime(startL, endL,uidL);
		if (data == null || data.size() <= 0) {
			return ResultUtils.resultSucceed("");
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

	@RequestMapping(value = "/statistics/{start}/{end}/{type}/{uid}", method = RequestMethod.GET)
	public BaseBean<List<StatisticsBean>> statistics(@PathVariable String start, @PathVariable String end,
			@PathVariable String type	, @PathVariable String uid) {
		long startL = Long.parseLong(start);
		long endL = Long.parseLong(end);
		int typeI = Integer.parseInt(type);
		long uidL= Long.parseLong(uid);
		List<SaveBean> data = null;
		List<StatisticsBean> statisticsBeans = new ArrayList<>();
		switch (typeI) {
		case -1:
			data = (List<SaveBean>) saveDao.findByTime(startL, endL,uidL);
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
				out.setName("支出");
				out.setMoney(totOut + "");
				statisticsBeans.add(out);

				StatisticsBean in = new StatisticsBean();
				in.setName("收入");
				in.setMoney(totIn + "");
				statisticsBeans.add(in);
			}
			break;
		case 0:
			data = (List<SaveBean>) saveDao.findByTimeAndFlag(startL, endL, 0,uidL);
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
			data = (List<SaveBean>) saveDao.findByTimeAndFlag(startL, endL, 1,uidL);
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
			return ResultUtils.resultSucceed("");
		}
	}

}
