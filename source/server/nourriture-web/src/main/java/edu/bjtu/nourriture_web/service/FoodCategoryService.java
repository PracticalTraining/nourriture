package edu.bjtu.nourriture_web.service;

import edu.bjtu.nourriture_web.idao.IFoodCategoryDao;
import edu.bjtu.nourriture_web.iservice.IFoodCategoryService;

public class FoodCategoryService implements IFoodCategoryService {
	private IFoodCategoryDao foodCategoryDao;

	public IFoodCategoryDao getFoodCategoryDao() {
		return foodCategoryDao;
	}

	public void setFoodCategoryDao(IFoodCategoryDao foodCategoryDao) {
		this.foodCategoryDao = foodCategoryDao;
	}
}
