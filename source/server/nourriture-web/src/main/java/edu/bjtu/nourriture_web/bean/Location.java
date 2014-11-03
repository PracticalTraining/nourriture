package edu.bjtu.nourriture_web.bean;

public class Location {
	/** auto increment field (自增序列) **/
	private int id;
	/** province (省级行政区) **/
	private String province;
	/** city (市级行政区) **/
	private String city;
	/** district (县级行政区) **/
	private String distrinct;
	/** detailed address (县级以下详细地址) **/
	private String address;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getDistrinct() {
		return distrinct;
	}
	public void setDistrinct(String distrinct) {
		this.distrinct = distrinct;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
}
