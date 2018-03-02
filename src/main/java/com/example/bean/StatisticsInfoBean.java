package com.example.bean;

import java.util.List;

public class StatisticsInfoBean {

	private String tot;
	private List<StatisticsBean> data;
	public String getTot() {
		return tot;
	}
	public void setTot(String tot) {
		this.tot = tot;
	}
	public List<StatisticsBean> getData() {
		return data;
	}
	public void setData(List<StatisticsBean> data) {
		this.data = data;
	}
	
}
