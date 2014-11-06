package edu.bjtu.nourriture_web.bean;

public class Customer{
	/** auto increment field (自增序列) **/
	private int id;
	/** user name (用户名) **/
	private String name;
	/** password (密码) **/
	private String password;
	/** gender 0:male , 1: female  (性别 0:男，1:女) **/
	private int sex;
	/** age (年龄) **/
	private int age;
	/** interesting flavour  (感兴趣的口味) **/
	private String interestFlavourIds;
	/** interesting category of food (感兴趣的食物类别) **/
	private String interestFoodCategoryIds;
	/** interesting category of recipe  (感兴趣的食谱类别) **/
	private String interestRecipeCategoryIds;
	
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
	public String getInterestFlavourIds() {
		return interestFlavourIds;
	}
	public void setInterestFlavourIds(String interestFlavourIds) {
		this.interestFlavourIds = interestFlavourIds;
	}
	public String getInterestFoodCategoryIds() {
		return interestFoodCategoryIds;
	}
	public void setInterestFoodCategoryIds(String interestFoodCategoryIds) {
		this.interestFoodCategoryIds = interestFoodCategoryIds;
	}
	public String getInterestRecipeCategoryIds() {
		return interestRecipeCategoryIds;
	}
	public void setInterestRecipeCategoryIds(String interestRecipeCategoryIds) {
		this.interestRecipeCategoryIds = interestRecipeCategoryIds;
	}
}
