package cn.edu.bjtu.nourriture.bean;

public class Location {
	/** auto increment field (自增序列) **/
	private int id;
	/** region which the location is in (所在行政区) **/
	private int regionId;
    /** detailAddress in the region (行政区以下详细地址) **/
	private String detailAddress;
	/** longitude (经度，无经纬度信息用404表示) **/
	private double longitude;
	/** latitude (纬度，无经纬度信息用404表示) **/
	private double latitude;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getRegionId() {
		return regionId;
	}
	public void setRegionId(int regionId) {
		this.regionId = regionId;
	}
	public String getDetailAddress() {
		return detailAddress;
	}
	public void setDetailAddress(String detailAddress) {
		this.detailAddress = detailAddress;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}	
}
