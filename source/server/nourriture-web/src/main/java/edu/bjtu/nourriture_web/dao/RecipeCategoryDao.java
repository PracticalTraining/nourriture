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
		getHibernateTemplate().save(category);
		return (category.getId());
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

	/** delete the recipe **/
	public void delete(RecipeCategory deleteRecipeCategory) {
		// TODO Auto-generated method stub
		getHibernateTemplate().delete(deleteRecipeCategory);
	}

	public boolean isSuperiorCategoryIdExist(int superiorCategoryId) {
		// TODO Auto-generated method stub
		List<RecipeCategory> list = getHibernateTemplate().find(
				"from RecipeCategory  where id=?", superiorCategoryId);
		if (list == null) {
			return false;
		} else {
			return true;
		}
	}

	public List<RecipeCategory> searchRecipeCategoryDetailByName(int id) {
		// TODO Auto-generated method stub
		List<RecipeCategory> recipeCategorys = new ArrayList<RecipeCategory>();
		List<Integer> superiorRecipeCategoryIds = getHibernateTemplate().find(
				" superiorCategoryId from RecipeCategory where id=?", id);
		for (Integer superiorRecipeCategoryId : superiorRecipeCategoryIds) {
			RecipeCategory recipeCategory = (RecipeCategory) getHibernateTemplate()
					.find("from RecipeCategory where id=? ",
							superiorRecipeCategoryId);
			recipeCategorys.add(recipeCategory);
		}
		return recipeCategorys;

	}

	public void update(RecipeCategory updateRecipeCategory) {
		// TODO Auto-generated method stub
		getHibernateTemplate().update(updateRecipeCategory);
	}

	public List<RecipeCategory> searchRecipeCategoryDetailById(int id) {
		// TODO Auto-generated method stub
		return getHibernateTemplate()
				.find("from RecipeCategory where id=?", id);
	}
}
