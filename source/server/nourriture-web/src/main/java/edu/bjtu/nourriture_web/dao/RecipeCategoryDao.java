package edu.bjtu.nourriture_web.dao;

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

	public boolean isRecipeCategoryExist(int id) {
		// TODO Auto-generated method stub
		List<RecipeCategory> list = getHibernateTemplate().find(
				"from RecipeCategory where id= ?", id);
		if (list.isEmpty()) {
			return false;
		} else {
			return true;
		}
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
		if (list.isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	public void update(RecipeCategory updateRecipeCategory) {
		// TODO Auto-generated method stub
		getHibernateTemplate().update(updateRecipeCategory);
	}

	public RecipeCategory searchRecipeCategoryDetailById(int id) {
		// TODO Auto-generated method stub
		List<RecipeCategory> recipeCategories = getHibernateTemplate().find(
				"from RecipeCategory where id=?", id);
		return recipeCategories.get(0);
	}

	// get recipeCategory's superiorCategoryId
	public int getSuperiorCategoryId(int id) {
		// TODO Auto-generated method stub
		List<Integer> superiorCategoryId = getHibernateTemplate().find(
				"superiorCategoryId from RecipeCategory where id=?", id);
		return superiorCategoryId.get(0);
	}

	public List<RecipeCategory> getChildrenRecipeCategory(int id) {
		// TODO Auto-generated method stub
		return getHibernateTemplate().find(
				"from RecipeCategory where superiorCategoryId=?", id);

	}

	public List<RecipeCategory> getAllRecipeCategory() {
		// TODO Auto-generated method stub
		return getHibernateTemplate().find("from RecipeCategory");
	}
}
