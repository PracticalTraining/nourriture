package edu.bjtu.nourriture_web.dao;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import edu.bjtu.nourriture_web.bean.Food;
import edu.bjtu.nourriture_web.bean.Customer;
import edu.bjtu.nourriture_web.bean.Food;
import edu.bjtu.nourriture_web.idao.IFoodDao;

public class FoodDao extends HibernateDaoSupport implements IFoodDao {
	public int add(Food food) {
		getHibernateTemplate().save(food);
		return food.getId();
	}
	public Food getById(int id) {
		return getHibernateTemplate().get(Food.class, id);
	}
	public boolean isCategoryExist(int categoryId){
		List<Food> list = getHibernateTemplate().find("from Food where categoryId = ?",categoryId );
		return !list.isEmpty();
	}
	public boolean isFlavourExist(int flavourId){
		List<Food> list = getHibernateTemplate().find("from Food where flavourId = ?",flavourId );
		return !list.isEmpty();
	}
	public boolean isManufacturerExist(int manufacturerId){
		List<Food> list = getHibernateTemplate().find("from Food where manufacturerId = ?",manufacturerId );
		return !list.isEmpty();
	}
	/** search the food according to manufacturerId **/
	public List<Food> getByManufacturerId(int manufacturerId) {
		List<Food> list = getHibernateTemplate().find(
				"from food where manufacturerId=?", manufacturerId);
		return list;
	}
}
