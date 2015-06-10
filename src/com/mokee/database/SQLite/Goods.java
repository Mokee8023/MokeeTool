package com.mokee.database.SQLite;

public class Goods {
	public int _id;
	public String goodsName = "";
	public String superMarketName1 = "";
	public String superMarketName2 = "";
	public String superMarketName3 = "";
	public String info = "";
	public String barCode = "";
	public float price1 = -1;
	public float price2 = -1;
	public float price3 = -1;
	
	public Goods(){
	}
	
	public Goods(String goodsName){
		this.goodsName = goodsName;
	}
	
	public String getGoodsName(){
		return goodsName;
	}
	public void setGoodsName(String value){
		this.goodsName = value;
	}
	public String getSuperMarketName1(){
		return superMarketName1;
	}
	public void setSuperMarketName1(String value){
		this.superMarketName1 = value;
	}
	public String getSuperMarketName2(){
		return superMarketName2;
	}
	public void setSuperMarketName2(String value){
		this.superMarketName2 = value;
	}
	public String getSuperMarketName3(){
		return superMarketName3;
	}
	public void setSuperMarketName3(String value){
		this.superMarketName3 = value;
	}
	public String getInfo(){
		return info;
	}
	public void setInfo(String value){
		this.info = value;
	}
	public String getBarCode(){
		return barCode;
	}
	public void setBarCode(String value){
		this.barCode = value;
	}
	public float getPrice1(){
		return price1;
	}
	public void setPrice1(float value){
		this.price1 = value;
	}
	public float getPrice2(){
		return price2;
	}
	public void setPrice2(float value){
		this.price2 = value;
	}
	public float getPrice3(){
		return price3;
	}
	public void setPrice3(float value){
		this.price3 = value;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Goods:");
		sb.append("\n");
		sb.append("_id:");
		sb.append(_id);
		sb.append("-->goodsName:");
		sb.append(goodsName);
		sb.append("-->barCode:");
		sb.append(barCode);
		sb.append("-->superMarketName1:");
		sb.append(superMarketName1);
		sb.append("-->price1:");
		sb.append(price1);
		sb.append("-->superMarketName2:");
		sb.append(superMarketName2);
		sb.append("-->price2:");
		sb.append(price2);
		sb.append("-->superMarketName3:");
		sb.append(superMarketName3);
		sb.append("-->price3:");
		sb.append(price3);
		sb.append("-->info:");
		sb.append(info);
		
		return sb.toString();
	}

}
