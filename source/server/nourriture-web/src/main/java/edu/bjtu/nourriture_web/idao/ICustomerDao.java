package edu.bjtu.nourriture_web.idao;

import edu.bjtu.nourriture_web.bean.Customer;

public interface ICustomerDao {
	/** add one row **/
	public int add(Customer customer);
	/** check if name is already exist **/
	public boolean isNameExist(String name);
}
