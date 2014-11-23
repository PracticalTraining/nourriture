package edu.bjtu.nourriture_web.dao;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import edu.bjtu.nourriture_web.idao.IRecipeDao;
import edu.bjtu.nourriture_web.bean.Recipe;


public class RecipeDao extends HibernateDaoSupport implements IRecipeDao {

	 public int add(Recipe recipe) {
		 getHibernateTemplate().save(recipe);
		
		 return recipe.getId();
	 }

	 public Recipe getById(int id) {
		 
		return getHibernateTemplate().get(Recipe.class, id);
	}

	public void deletebyid(int id) {
		
		Recipe my_recipe =	new Recipe();
		my_recipe.setId(id);
		getHibernateTemplate().delete(my_recipe);		
	}
	
	public void update(Recipe recipe) {
		getHibernateTemplate().saveOrUpdate(recipe);
	}

}
