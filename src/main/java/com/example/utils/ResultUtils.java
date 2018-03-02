package com.example.utils;

import com.example.bean.BaseBean;

public class ResultUtils {

	public static <T> BaseBean<T> resultSucceed(T data) {
		BaseBean<T> baseBean = new BaseBean<>();
		baseBean.setCode(0);
		baseBean.setMessage("succeed");
		baseBean.setData(data);
		return baseBean;
	}
	
	public static <T> BaseBean<T> resultSucceed(String data) {
		BaseBean<T> baseBean = new BaseBean<>();
		baseBean.setCode(0);
		baseBean.setMessage(data);
		return baseBean;
	}

	public static <T> BaseBean<T> resultError(String error) {
		BaseBean<T> baseBean = new BaseBean<>();
		baseBean.setCode(1);
		baseBean.setMessage(error);
		return baseBean;
	}
	
}
