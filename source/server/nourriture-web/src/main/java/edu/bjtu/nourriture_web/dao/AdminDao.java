package edu.bjtu.nourriture_web.dao;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import edu.bjtu.nourriture_web.bean.Admin;
import edu.bjtu.nourriture_web.bean.Customer;
import edu.bjtu.nourriture_web.idao.IAdminDao;

public class AdminDao extends HibernateDaoSupport implements IAdminDao  {
	public Admin getById(int id) {
		Admin  f = getHibernateTemplate().get(Admin.class, id);
		return f;
	}
	public int login(String name, String password) {
		List<Admin> list = getHibernateTemplate().find("from Admin  where name = ? and password = ?",name,password);
		return list.isEmpty() ? -1 : list.get(0).getId();
	}
	public boolean isNameExist(String name) {
		// TODO Auto-generated method stub
		return false;
	}
}
