package edu.bjtu.nourriture_web.dao;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import edu.bjtu.nourriture_web.bean.Flavour;
import edu.bjtu.nourriture_web.idao.IFlavourDao;

public class FlavourDao extends HibernateDaoSupport implements IFlavourDao {

	public Flavour getById(int id) {
		Flavour f = getHibernateTemplate().get(Flavour.class, id);
		return f;
	}

}
