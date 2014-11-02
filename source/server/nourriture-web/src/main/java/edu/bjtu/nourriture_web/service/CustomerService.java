package edu.bjtu.nourriture_web.service;

import edu.bjtu.nourriture_web.idao.ICustomerDao;
import edu.bjtu.nourriture_web.iservice.ICustomerService;

public class CustomerService implements ICustomerService {
	private ICustomerDao customerDao;

	public ICustomerDao getCustomerDao() {
		return customerDao;
	}

	public void setCustomerDao(ICustomerDao customerDao) {
		this.customerDao = customerDao;
	}
}
