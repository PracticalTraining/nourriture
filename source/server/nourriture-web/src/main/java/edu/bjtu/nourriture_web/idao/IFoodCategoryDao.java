package edu.bjtu.nourriture_web.idao;

import edu.bjtu.nourriture_web.bean.Customer;
import edu.bjtu.nourriture_web.bean.FoodCategory;

public interface IFoodCategoryDao {
	/** get detail information of food category by id **/
	FoodCategory getById(int id);
	/** add one row **/
	int add(FoodCategory foodcategory);
	/** update foodcategory **/
	void update(FoodCategory foodcategory);
	/** check if the category is exsit **/
	boolean isCategoryExist(int categoryId);
}

