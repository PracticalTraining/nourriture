package edu.bjtu.nourriture_web.dao;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import edu.bjtu.nourriture_web.bean.FoodCategory;
import edu.bjtu.nourriture_web.idao.IFoodCategoryDao;

public class FoodCategoryDao extends HibernateDaoSupport implements IFoodCategoryDao {

	public FoodCategory getById(int id) {
		FoodCategory fc = getHibernateTemplate().get(FoodCategory.class, id);
		return fc;
	}

}
