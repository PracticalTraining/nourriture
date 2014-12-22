package edu.bjtu.nourriture_web.dao;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import edu.bjtu.nourriture_web.idao.IRecipeDao;
import edu.bjtu.nourriture_web.bean.Food;
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

	public List<Recipe> search(String name) {
		return getHibernateTemplate().find("from Recipe where name like ?","%" + name + "%");
	}

	public List<Recipe> getPageRecipes(final int categoryId, final int page) {
		return getHibernateTemplate().executeFind(new HibernateCallback<List<Recipe>>() {

			public List<Recipe> doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.createQuery("from Recipe where catogeryId = ?"); 
				query.setParameter(0, categoryId);
				query.setFirstResult(page * 10); 
				query.setMaxResults(10); 
				List<Recipe> list = query.list();
				return list; 
			}
		});
	}

}
