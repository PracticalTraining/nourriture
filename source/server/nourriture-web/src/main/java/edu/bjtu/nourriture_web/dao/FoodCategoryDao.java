package edu.bjtu.nourriture_web.dao;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import edu.bjtu.nourriture_web.bean.Customer;
import edu.bjtu.nourriture_web.bean.FoodCategory;
import edu.bjtu.nourriture_web.bean.RecipeCategory;
import edu.bjtu.nourriture_web.idao.IFoodCategoryDao;
import edu.bjtu.nourriture_web.idao.IRecipeCategoryDao;

public class FoodCategoryDao extends HibernateDaoSupport implements
IFoodCategoryDao {

	public FoodCategory getById(int id) {
		FoodCategory rc = getHibernateTemplate()
				.get(FoodCategory.class, id);
		return rc;
	}

	public int add(FoodCategory category) {
		// TODO Auto-generated method stub
		getHibernateTemplate().save(category);
		return (category.getId());
	}

	public boolean isFoodCategoryExist(int id) {
		// TODO Auto-generated method stub
		List<FoodCategory> list = getHibernateTemplate().find(
				"from FoodCategory where id= ?", id);
		if (list.isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	/** delete the FoodCategory **/
	public void delete(FoodCategory deleteFoodCategory) {
		// TODO Auto-generated method stub
		getHibernateTemplate().delete(deleteFoodCategory);
	}

	public boolean isSuperiorCategoryIdExist(int superiorCategoryId) {
		// TODO Auto-generated method stub
		List<FoodCategory> list = getHibernateTemplate().find(
				"from FoodCategory  where id=?", superiorCategoryId);
		if (list.isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	public void update(FoodCategory updateFoodCategory) {
		// TODO Auto-generated method stub
		getHibernateTemplate().update(updateFoodCategory);
	}

	public FoodCategory searchFoodCategoryDetailById(int id) {
		// TODO Auto-generated method stub
		List<FoodCategory> FoodCategories = getHibernateTemplate().find(
				"from FoodCategory where id=?", id);
		return FoodCategories.get(0);
	}

	// get FoodCategory's superiorCategoryId
	public int getSuperiorCategoryId(int id) {
		// TODO Auto-generated method stub
		List<Integer> superiorCategoryId = getHibernateTemplate().find(
				"superiorCategoryId from FoodCategory where id=?", id);
		return superiorCategoryId.get(0);
	}

	public List<RecipeCategory> getChildrenFoodCategory(int id) {
		// TODO Auto-generated method stub
		return getHibernateTemplate().find(
				"from FoodCategory where superiorCategoryId=?", id);

	}

	public List<FoodCategory> getAllFoodCategory() {
		// TODO Auto-generated method stub
		return getHibernateTemplate().find("from FoodCategory");
	}


	
}
