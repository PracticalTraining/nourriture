package edu.bjtu.nourriture_web.bean;

public class CookingStep {
	/** 自增序列 **/
	private int id;
	/** 序号，如第一步，stepCount ＝ 1，第二步，stepCount ＝ 2 **/
	private int stepCount;
	/** 描述 **/
	private int description;
	/** 图片在服务器的路径 **/
	private String picture;
	/** 描述的菜谱 **/
	private int recipeId;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getStepCount() {
		return stepCount;
	}
	public void setStepCount(int stepCount) {
		this.stepCount = stepCount;
	}
	public int getDescription() {
		return description;
	}
	public void setDescription(int description) {
		this.description = description;
	}
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	public int getRecipeId() {
		return recipeId;
	}
	public void setRecipeId(int recipeId) {
		this.recipeId = recipeId;
	}
}
