package edu.bjtu.nourriture_web.idao;

import java.util.List;

import edu.bjtu.nourriture_web.bean.Customer;

public interface ICustomerDao {
	/** add one row **/
	int add(Customer customer);
	/** check if name is already exist **/
	boolean isNameExist(String name);
	/** search the customer according to name **/
	List<Customer> searchByName(String name);
}
