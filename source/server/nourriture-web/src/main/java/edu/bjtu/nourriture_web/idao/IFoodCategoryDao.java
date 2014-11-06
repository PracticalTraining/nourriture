package edu.bjtu.nourriture_web.idao;

import edu.bjtu.nourriture_web.bean.FoodCategory;

public interface IFoodCategoryDao {
	/** get detail information of food category by id **/
	FoodCategory getById(int id);
}
