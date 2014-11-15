package edu.bjtu.nourriture_web.dao;

import java.util.List;

import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import edu.bjtu.nourriture_web.bean.Customer;
import edu.bjtu.nourriture_web.idao.ICustomerDao;

public class CustomerDao extends HibernateDaoSupport implements ICustomerDao {
	
	public int add(Customer customer) {
		getHibernateTemplate().save(customer);
		return customer.getId();
	}

	public boolean isNameExist(String name) {
		List<Customer> list = getHibernateTemplate().find("from Customer where name = ?", name);
		return !list.isEmpty();
	}

	public List<Customer> searchByName(String name) {
		List<Customer> list = getHibernateTemplate().find("from Customer where name like ?", "%" + name + "%");
		return list;
	}

	public int login(String name, String password) {
		List<Customer> list = getHibernateTemplate().find("from Customer where name = ? and password = ?",name,password);
		return list.isEmpty() ? -1 : list.get(0).getId();
	}

	public Customer getById(int id) {
		return getHibernateTemplate().get(Customer.class, id);
	}

	public void update(Customer customer) {
		getHibernateTemplate().saveOrUpdate(customer);
		
	}

}
