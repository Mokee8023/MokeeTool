package com.mokee.application.IdCard;

public class BaiduIDCardUtil {

	public static final String idCardHttpUrl = "http://apis.baidu.com/apistore/idservice/id";
	public static final String apikey = "4ba872837b81fcc2da69201bdf90c328";
	
	public static String getSexChina(String sex){
		String sexChina = null;
		if(sex.equals("M")){
			sexChina = "男";
		} else if(sex.equals("W")){
			sexChina = "女";
		} else {
			sexChina = "未知";
		}
		return sexChina;
	}
}
