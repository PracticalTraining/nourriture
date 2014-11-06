package edu.bjtu.nourriture_web.dao;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import edu.bjtu.nourriture_web.bean.RecipeCategory;
import edu.bjtu.nourriture_web.idao.IRecipeCategoryDao;

public class RecipeCategoryDao extends HibernateDaoSupport implements IRecipeCategoryDao {

	public RecipeCategory getById(int id) {
		RecipeCategory rc = getHibernateTemplate().get(RecipeCategory.class, id);
		return rc;
	}

}
