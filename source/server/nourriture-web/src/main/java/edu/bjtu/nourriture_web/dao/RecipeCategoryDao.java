package edu.bjtu.nourriture_web.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import edu.bjtu.nourriture_web.bean.RecipeCategory;
import edu.bjtu.nourriture_web.idao.IRecipeCategoryDao;

public class RecipeCategoryDao extends HibernateDaoSupport implements
		IRecipeCategoryDao {

	public RecipeCategory getById(int id) {
		RecipeCategory rc = getHibernateTemplate()
				.get(RecipeCategory.class, id);
		return rc;
	}

	public int add(RecipeCategory category) {
		// TODO Auto-generated method stub
		return (Integer) getHibernateTemplate().save(category);
	}

	public boolean isRecipeCategoryExist(String categoryName) {
		// TODO Auto-generated method stub
		List<RecipeCategory> list = getHibernateTemplate().find(
				"from RecipeCategory where name = ?", categoryName);
		if (list.isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	public List<RecipeCategory> searchRecipeByName(String name) {
		// TODO Auto-generated method stub
		return getHibernateTemplate().find(
				"from RecipeCategory where name = ?", name);

	}

	// public RecipeCategory searchRecipeById(int id) {
	// // TODO Auto-generated method stub
	// return null;
	// }

	/** delete the recipe **/
	public void delete(RecipeCategory deleteRecipeCategory) {
		// TODO Auto-generated method stub
		getHibernateTemplate().delete(deleteRecipeCategory);
	}

	public boolean isSuperiorCategoryIdExist(int superiorCategoryId) {
		// TODO Auto-generated method stub
		List<RecipeCategory> list = getHibernateTemplate().find(
				"from RecipeCategory  where superiorCategoryId=?",
				superiorCategoryId);
		if (list == null) {
			return false;
		} else {
			return true;
		}
	}

	public List<RecipeCategory> searchRecipeCategoryDetailByName(String name) {
		// TODO Auto-generated method stub
		List<RecipeCategory> regions = new ArrayList<RecipeCategory>();
		List<Integer> superiorRecipeCategoryIds = getHibernateTemplate().find(
				" superiorCategoryId from RecipeCategory where name=?", name);
		for (Integer integer : superiorRecipeCategoryIds) {
			RecipeCategory recipecategory = (RecipeCategory) getHibernateTemplate()
					.find("from RecipeCategory where id=? ", integer);
			regions.add(recipecategory);
		}
		return regions;

	}
}
