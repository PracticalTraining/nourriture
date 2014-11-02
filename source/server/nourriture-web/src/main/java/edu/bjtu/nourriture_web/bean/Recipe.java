package edu.bjtu.nourriture_web.bean;

public class Recipe {
	/** 自增序列 **/
	private int id;
	/** 菜谱名 **/
	private String name;
	/** 菜谱描述 **/
	private String description;
	/** 原料 **/
	private String ingredient;
	/** 图片 **/
	private String picture;
	/** 发布人 **/
	private int customerId;
	/** 类别 **/
	private int catogeryId;
	
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getIngredient() {
		return ingredient;
	}
	public void setIngredient(String ingredient) {
		this.ingredient = ingredient;
	}
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	public int getCustomerId() {
		return customerId;
	}
	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}
	public int getCatogeryId() {
		return catogeryId;
	}
	public void setCatogeryId(int catogeryId) {
		this.catogeryId = catogeryId;
	}
}
