package edu.bjtu.nourriture_web.dao;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import edu.bjtu.nourriture_web.bean.CookingStep;
import edu.bjtu.nourriture_web.bean.Location;
import edu.bjtu.nourriture_web.idao.ICookingStepDao;

public class CookingStepDao extends HibernateDaoSupport implements ICookingStepDao {

	public int add(CookingStep my_cookingstep) {
		getHibernateTemplate().save(my_cookingstep);
		return my_cookingstep.getId();
	}

	public CookingStep getById(int id) {
		return getHibernateTemplate().get(CookingStep.class, id);
	}

	public void update(CookingStep my_cookingstep) {
		getHibernateTemplate().saveOrUpdate(my_cookingstep);
	}

	public void deletebyid(int id) {
		CookingStep my_cookingstep =	new CookingStep();
		my_cookingstep.setId(id);
		getHibernateTemplate().delete(my_cookingstep);
	}

	

}
