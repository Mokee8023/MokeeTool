package com.mokee.API;

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
			codeLang = "zjs";
			break;
		case 10:
			codeLang = "guotong";
			break;
		case 11:
			codeLang = "stwl";
			break;
		case 12:
			codeLang = "zhongtie";
			break;
		case 13:
			codeLang = "chengji";
			break;
		case 14:
			codeLang = "gtsd";
			break;
		case 15:
			codeLang = "ups";
			break;
		case 16:
			codeLang = "bgn";
			break;
		case 17:
			codeLang = "yxwl";
			break;
		case 18:
			codeLang = "fbwl";
			break;
		case 19:
			codeLang = "kuaijie";
			break;
		case 20:
			codeLang = "minhang";
			break;
		case 21:
			codeLang = "ocs";
			break;
		case 22:
			codeLang = "wxwl";
			break;
		case 23:
			codeLang = "yuntong";
			break;
		case 24:
			codeLang = "zhongyou";
			break;
		default:
			codeLang = "shunfeng";
			break;
		}
		return codeLang;
	}

}
