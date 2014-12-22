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
	boolean isRecipeCategoryExist(int id);

	/** search superior recipecatgory by name **/
	// RecipeCategory searchSuperiorRecipeById(int id);

	/** delete the recipecategory **/
	void delete(RecipeCategory deleteRecipeCategory);

	/** search the detail info of the recipecategory **/
	RecipeCategory searchRecipeCategoryDetailById(int id);

	/** update the recipecategory **/
	void update(RecipeCategory updateRecipeCategory);
	
	List<RecipeCategory> getChildren(int id);

}