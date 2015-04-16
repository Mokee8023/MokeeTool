package com.mokee.API;

public class ApiLanguage {

	public static String GetLanguageCode(int position) {
		String codeLang = "auto";
		switch (position) {
		case 0:
			codeLang = "auto";
			break;
		case 1:
			codeLang = "zh";
			break;
		case 2:
			codeLang = "en";
			break;
		case 3:
			codeLang = "jp";
			break;
		case 4:
			codeLang = "kor";
			break;
		case 5:
			codeLang = "wyw";
			break;
		case 6:
			codeLang = "zh";
			break;
		case 7:
			codeLang = "spa";
			break;
		case 8:
			codeLang = "fra";
			break;
		case 9:
			codeLang = "th";
			break;
		case 10:
			codeLang = "ara";
			break;
		case 11:
			codeLang = "ru";
			break;
		case 12:
			codeLang = "pt";
			break;
		case 13:
			codeLang = "yue";
			break;
		case 14:
			codeLang = "de";
			break;
		case 15:
			codeLang = "it";
			break;
		case 16:
			codeLang = "nl";
			break;
		case 17:
			codeLang = "el";
			break;
		default:
			codeLang = "auto";
			break;
		}
		return codeLang;
	}

}
