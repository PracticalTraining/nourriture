package edu.bjtu.nourriture_web.idao;

import java.util.List;

import edu.bjtu.nourriture_web.bean.Food;

import edu.bjtu.nourriture_web.bean.Food;

public interface IFoodDao {
	/** add one row **/
	int add(Food food);
	/** check if category is already exist **/
	boolean isCategoryExist(int categoryId);
	/** check if flavour is already exist **/
	boolean isFlavourExist(int flavourId);
	/** check if manufacturer is already exist **/
	boolean isManufacturerExist(int manufacturerId);
	/**get food by id**/
	Food getById(int id);
	/** search the food by manufacturerId **/
	List<Food> getByManufacturerId(int manufacturerId);
}
