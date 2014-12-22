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
	List<Food> search(double fromPrice,double toPrice,String[] categoryIds,String[] flavourIds,String[] produceRegionIds,String[] buyRegionIds);
    /** search food by name **/
	List<Food> search(String name);
	
	List<Food> getPageFoods(int categoryId,int page);
}
