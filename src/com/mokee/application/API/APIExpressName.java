package com.mokee.application.API;

import com.mokee.application.Express.QueryExpressActivity;
import com.mokee.tools.R;

public class APIExpressName {
	
	public static String GetExpressCode(int position) {
		String codeLang = "shunfeng";
		switch (position) {
		case 0:
			codeLang = "shunfeng";
			break;
		case 1:
			codeLang = "shentong";
			break;
		case 2:
			codeLang = "yuantong";
			break;
		case 3:
			codeLang = "yunda";
			break;
		case 4:
			codeLang = "zhongtong";
			break;
		case 5:
			codeLang = "tiantian";
			break;
		case 6:
			codeLang = "jingdong";
			break;
		case 7:
			codeLang = "huitong";
			break;
		case 8:
			codeLang = "quanfeng";
			break;
		case 9:
			codeLang = "ems";
			break;
		case 10:
			codeLang = "guotong";
			break;
		case 11:
			codeLang = "zhongtie";
			break;
		case 12:
			codeLang = "ups";
			break;
		case 13:
			codeLang = "kuaijie";
			break;
		case 14:
			codeLang = "ocs";
			break;
		case 15:
			codeLang = "zhongyou";
			break;
		case 16:
			codeLang = "zhaijisong";
			break;
		default:
			codeLang = "shunfeng";
			break;
		}
		return codeLang;
	}

	public static String getCompanyName(String companyCode) {
		QueryExpressActivity.expressContext.getResources().getTextArray(R.array.Express_Name);
		return companyCode;
	}
}
