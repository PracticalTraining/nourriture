package edu.bjtu.nourriture_web.idao;

import edu.bjtu.nourriture_web.bean.Customer;
import edu.bjtu.nourriture_web.bean.FoodCategory;
import edu.bjtu.nourriture_web.bean.RecipeCategory;

public interface IFoodCategoryDao {
	/** get detail information of the FoodCategory by id **/
	FoodCategory getById(int id);

	/** check if FoodCategoryexist **/
	boolean isSuperiorCategoryIdExist(int superiorCategoryId);

	/** add one row **/
	int add(FoodCategory category);

	/** check if category is already exist **/
	boolean isCategoryExist(int id);

	/** search superior FoodCategory by name **/
	// FoodCategory searchSuperiorRecipeById(int id);

	/** delete the FoodCategory **/
	void delete(FoodCategory deleteFoodCategory);

	/** search the detail info of the FoodCategory **/
	FoodCategory searchFoodCategoryDetailById(int id);

	/** update the FoodCategory **/
	void update(FoodCategory updateFoodCategory);
}

