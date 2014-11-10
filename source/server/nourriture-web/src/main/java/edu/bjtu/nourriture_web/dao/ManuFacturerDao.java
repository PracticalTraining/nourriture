package edu.bjtu.nourriture_web.dao;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import edu.bjtu.nourriture_web.idao.IManuFacturerDao;
import edu.bjtu.nourriture_web.bean.ManuFacturer;

public class ManuFacturerDao extends HibernateDaoSupport implements IManuFacturerDao {
	
	public int add(ManuFacturer manuFacturer) {
		getHibernateTemplate().save(manuFacturer);
		return manuFacturer.getId();
	}
	
	public boolean isNameExist(String name) {
		List<ManuFacturer> list = getHibernateTemplate().find("from ManuFacturer where name = ?", name);
		return !list.isEmpty();
	}
	
	public List<ManuFacturer> searchByName(String name) {
		List<ManuFacturer> list = getHibernateTemplate().find("from ManuFacturer where name like ?", "%" + name + "%");
		return list;
	}
	
	public int login(String name, String password) {
		List<ManuFacturer> list = getHibernateTemplate().find("from ManuFacturer where name = ? and password = ?",name,password);
		return list.isEmpty() ? -1 : list.get(0).getId();
	}
	
	public ManuFacturer getById(int id) {
		return getHibernateTemplate().get(ManuFacturer.class, id);
	}
	
	public void update(ManuFacturer manuFacturer) {
		getHibernateTemplate().saveOrUpdate(manuFacturer);
	}
	
}
