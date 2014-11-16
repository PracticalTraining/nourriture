package edu.bjtu.nourriture_web.dao;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import edu.bjtu.nourriture_web.bean.Admin;
import edu.bjtu.nourriture_web.idao.IAdminDao;

public class AdminDao extends HibernateDaoSupport implements IAdminDao {
	public Admin getById(int id) {
		Admin f = getHibernateTemplate().get(Admin.class, id);
		return f;
	}

	public int login(String name, String password) {
		List<Admin> list = getHibernateTemplate().find(
				"from Admin  where name = ? and password = ?", name, password);
		return list.isEmpty() ? -1 : list.get(0).getId();
	}

	public boolean isNameExist(String name) {
		// TODO Auto-generated method stub
		List<Admin> list = getHibernateTemplate().find(
				"from Admin where name=?", name);
		return !list.isEmpty();
	}
	/**
	 * public void deleteById(int id){ Admin admin=new Admin(); admin.setId(id);
	 * getHibernateTemplate().delete(admin); }
	 */
	/**
	 * public void saveorupdate(Admin admin){
	 * getHibernateTemplate().saveOrUpdate(admin); }
	 */

	/**
	 * public void save(Admin admin) { getHibernateTemplate().save(admin); } {
	 * Admin admin=new Admin(); admin.setName("name");
	 * admin.setPassword("password"); save(admin); }
	 */
	/**
	 * public void update(Admin admin) { getHibernateTemplate().update(admin); }
	 * { Admin admin=new Admin(); admin.setId(1); admin.setName("name");
	 * admin.setPassword("password"); update(admin); }
	 */

}
