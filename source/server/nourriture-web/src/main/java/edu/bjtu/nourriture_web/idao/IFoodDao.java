package edu.bjtu.nourriture_web.idao;

import java.util.List;

import edu.bjtu.nourriture_web.bean.Food;

public interface IFoodDao {
	/** search the food by manufacturerId **/
	List<Food> getByManufacturerId(int manufacturerId);
}
