package edu.bjtu.nourriture_web.service;

import edu.bjtu.nourriture_web.idao.IFoodDao;
import edu.bjtu.nourriture_web.iservice.IFoodService;

public class FoodService implements IFoodService {
	private IFoodDao foodDao;

	public IFoodDao getFoodDao() {
		return foodDao;
	}

	public void setFoodDao(IFoodDao foodDao) {
		this.foodDao = foodDao;
	}
}
