package edu.bjtu.nourriture_web.idao;

import edu.bjtu.nourriture_web.bean.RecipeCategory;

public interface IRecipeCategoryDao {
	/** get detail information of the recipe category by id **/
	RecipeCategory getById(int id);
}
