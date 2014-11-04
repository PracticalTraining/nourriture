package edu.bjtu.nourriture_web.bean;

public class Region {
	/** auto increment field (自增序列) **/
	private int id;
	/** name of region (行政区名称) **/
	private String name;
	/** is province (是否是省级行政区) **/
	private boolean province;
	/** superior region (上级行政区) **/
	private int superiorRegionId;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isProvince() {
		return province;
	}
	public void setProvince(boolean province) {
		this.province = province;
	}
	public int getSuperiorRegionId() {
		return superiorRegionId;
	}
	public void setSuperiorRegionId(int superiorRegionId) {
		this.superiorRegionId = superiorRegionId;
	}
}
