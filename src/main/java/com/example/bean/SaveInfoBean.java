package com.example.bean;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class SaveInfoBean {

	private List<SaveBean> data;
	private Double totOut;
	private Double totIn;
	private Double totTwo;
	public List<SaveBean> getData() {
		return data;
	}
	public void setData(List<SaveBean> data) {
		this.data = data;
	}
	public Double getTotOut() {
		return totOut;
	}
	public void setTotOut(Double totOut) {
		this.totOut = totOut;
	}
	public Double getTotIn() {
		return totIn;
	}
	public void setTotIn(Double totIn) {
		this.totIn = totIn;
	}
	public Double getTotTwo() {
		return totTwo;
	}
	public void setTotTwo(Double totTwo) {
		this.totTwo = totTwo;
	}
	
}
