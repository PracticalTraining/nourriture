package edu.bjtu.nourriture_web.bean;

public class FoodCategory {
	/** auto increment field (自增序列) **/
	private int id;
	/** category name (类别名) **/
	private String name;
	/** Is the top classification (是否是顶级分类) **/
	private String topCategory;
	/** superior flavour , used for relating automatically (上级分类，用于自关联) **/
	private Object superiorCategoryId;
	
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
	public String isTopCategory() {
		return topCategory;
	}
	public void setTopCategory(String topcategory2) {
		this.topCategory = topcategory2;
	}
	public Object getSuperiorCategoryId() {
		return superiorCategoryId;
	}
	public void setSuperiorCategoryId(Object superiorCategoryId2) {
		this.superiorCategoryId = superiorCategoryId2;
	}
}
