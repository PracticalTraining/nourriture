package edu.bjtu.nourriture_web.dao;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import edu.bjtu.nourriture_web.bean.User;
import edu.bjtu.nourriture_web.idao.ITestDao;

public class TestDao extends HibernateDaoSupport implements ITestDao {

	public void test() {
		User user = new User();
		user.setName("lilei");
		getHibernateTemplate().save(user);
	}

}
