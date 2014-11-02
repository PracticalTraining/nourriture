package edu.bjtu.nourriture_web.service;

import edu.bjtu.nourriture_web.idao.ICookingStepDao;
import edu.bjtu.nourriture_web.iservice.ICookingStepService;

public class CookingStepService implements ICookingStepService {
	private ICookingStepDao cookingStepDao;

	public ICookingStepDao getCookingStepDao() {
		return cookingStepDao;
	}

	public void setCookingStepDao(ICookingStepDao cookingStepDao) {
		this.cookingStepDao = cookingStepDao;
	}
}
