package edu.bjtu.nourriture_web.bean;

public class ManuFacturer {
	/** auto increment field (自增序列) **/
	private int id;
	/** manufacturer name (制造商用户名) **/
	private String name;
	/** manufacturer password (制造商密码) **/
	private String password;
	/** manufacturer company's name (制造商公司名) **/
	private String companyName;
	/** introduction (简介) **/
	private String description;
	
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
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
