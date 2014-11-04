package edu.bjtu.nourriture_web.bean;

public class FoodCategory {
	/** auto increment field (自增序列) **/
	private int id;
	/** category name (类别名) **/
	private int name;
	/** Is the top classification (是否是顶级分类) **/
	private boolean topCategory;
	/** superior flavour , used for relating automatically (上级分类，用于自关联) **/
	private int superiorCategoryId;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getName() {
		return name;
	}
	public void setName(int name) {
		this.name = name;
	}
	public boolean isTopCategory() {
		return topCategory;
	}
	public void setTopCategory(boolean topCategory) {
		this.topCategory = topCategory;
	}
	public int getSuperiorCategoryId() {
		return superiorCategoryId;
	}
	public void setSuperiorCategoryId(int superiorCategoryId) {
		this.superiorCategoryId = superiorCategoryId;
	}
}
