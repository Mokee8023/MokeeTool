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
			codeLang = "EMS";
			break;
		case 10:
			codeLang = "guotong";
			break;
		case 11:
			codeLang = "sutong";
			break;
		case 12:
			codeLang = "zhongtie";
			break;
		case 13:
			codeLang = "ups";
			break;
		case 14:
			codeLang = "feibang";
			break;
		case 15:
			codeLang = "kuaijie";
			break;
		case 16:
			codeLang = "minhang";
			break;
		case 17:
			codeLang = "ocs";
			break;
		case 18:
			codeLang = "wanxiang";
			break;
		case 19:
			codeLang = "hengyu";
			break;
		case 20:
			codeLang = "zhongyou";
			break;
		case 21:
			codeLang = "zhaijisong";
			break;
		default:
			codeLang = "shunfeng";
			break;
		}
		return codeLang;
	}

}
