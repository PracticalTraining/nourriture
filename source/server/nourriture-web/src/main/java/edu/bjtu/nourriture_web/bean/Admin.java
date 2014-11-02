package edu.bjtu.nourriture_web.bean;

public class Admin {
	/** 自增序列 **/
	private int id;
	/** 用户名 **/
	private String name;
	/** 密码 **/
	private String password;
	
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
}
