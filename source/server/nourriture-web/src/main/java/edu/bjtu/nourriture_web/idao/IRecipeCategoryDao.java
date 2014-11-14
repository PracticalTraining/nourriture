package edu.bjtu.nourriture_web.idao;

import java.util.List;

import edu.bjtu.nourriture_web.bean.RecipeCategory;

public interface IRecipeCategoryDao {
	/** get detail information of the recipe category by id **/
	RecipeCategory getById(int id);

	/** check if recipeCategoryexist **/
	boolean isSuperiorCategoryIdExist(int superiorCategoryId);

	/** add one row **/
	int add(RecipeCategory category);

	/** check if category is already exist **/
	boolean isRecipeCategoryExist(String categoryName);

	/** search superior recipecatgory by name **/
	List<RecipeCategory> searchRecipeByName(String name);

	/** search the detail info of the recipeCategory by name **/
	List<RecipeCategory> searchRecipeCategoryDetailByName(String name);

	/** delete the recipe **/
	void delete(RecipeCategory deleteRecipeCategory);

}