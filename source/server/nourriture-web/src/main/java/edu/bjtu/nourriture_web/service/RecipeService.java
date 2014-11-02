package edu.bjtu.nourriture_web.service;

import edu.bjtu.nourriture_web.idao.IRecipeDao;
import edu.bjtu.nourriture_web.iservice.IRecipeService;

public class RecipeService implements IRecipeService {
	private IRecipeDao recipeDao;

	public IRecipeDao getRecipeDao() {
		return recipeDao;
	}

	public void setRecipeDao(IRecipeDao recipeDao) {
		this.recipeDao = recipeDao;
	}
}
