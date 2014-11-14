package edu.bjtu.nourriture_web.idao;

import java.util.List;

import edu.bjtu.nourriture_web.bean.Food;


public interface IFoodDao {
	/** add one row **/
	int add(Food food);
	/**get food by id**/
	Food getById(int id);
	/** search the food by manufacturerId **/
	List getByManufacturerId(int manufacturerId);
	/** delete the food by id **/
	void deletebyid(int id);
	/** update the food **/
	void update(Food food);
	/** sift the food by price **/
	List<Food> siftByPrice(double fromPrice,double toPrice);
	/** sift the food by categoryId **/
	List<Food> siftByCategoryId(List<Food> list,int categoryId);
	//List<Food> initialList();
}
