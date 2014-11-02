package edu.bjtu.nourriture_web.service;

import edu.bjtu.nourriture_web.idao.IRecipeCategoryDao;
import edu.bjtu.nourriture_web.iservice.IRecipeCategoryService;

public class RecipeCategoryService implements IRecipeCategoryService {
	private IRecipeCategoryDao recipeCategoryDao;

	public IRecipeCategoryDao getRecipeCategoryDao() {
		return recipeCategoryDao;
	}

	public void setRecipeCategoryDao(IRecipeCategoryDao recipeCategoryDao) {
		this.recipeCategoryDao = recipeCategoryDao;
	}
}
