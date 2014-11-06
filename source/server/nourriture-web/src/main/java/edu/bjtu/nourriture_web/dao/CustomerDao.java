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

}
