package edu.bjtu.nourriture_web.bean;

public class Customer{
	/** 自增序列 **/
	private int id;
	/** 用户名 **/
	private String name;
	/** 密码 **/
	private String password;
	/** 性别 0:男，1女 **/
	private int sex;
	/** 年龄 **/
	private int age;
	/** 感兴趣的口味 **/
	private int interestFlavourIds;
	/** 感兴趣的食物类别 **/
	private int interestFoodCategoryIds;
	/** 感兴趣的食谱类别 **/
	private int interestRecipeCategoryIds;
	
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
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getInterestFlavourIds() {
		return interestFlavourIds;
	}
	public void setInterestFlavourIds(int interestFlavourIds) {
		this.interestFlavourIds = interestFlavourIds;
	}
	public int getInterestFoodCategoryIds() {
		return interestFoodCategoryIds;
	}
	public void setInterestFoodCategoryIds(int interestFoodCategoryIds) {
		this.interestFoodCategoryIds = interestFoodCategoryIds;
	}
	public int getInterestRecipeCategoryIds() {
		return interestRecipeCategoryIds;
	}
	public void setInterestRecipeCategoryIds(int interestRecipeCategoryIds) {
		this.interestRecipeCategoryIds = interestRecipeCategoryIds;
	}
}
