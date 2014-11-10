package edu.bjtu.nourriture_web.dao;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import edu.bjtu.nourriture_web.bean.Food;
import edu.bjtu.nourriture_web.idao.IFoodDao;

public class FoodDao extends HibernateDaoSupport implements IFoodDao {
	/** search the food according to manufacturerId **/
	public List<Food> getByManufacturerId(int manufacturerId) {
		List<Food> list = getHibernateTemplate().find(
				"from food where manufacturerId=?", manufacturerId);
		return list;
	}
}
