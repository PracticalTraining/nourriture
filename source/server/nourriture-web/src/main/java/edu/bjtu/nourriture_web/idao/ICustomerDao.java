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
	/** check if there exsit a row with given name and password
	 *  @return -1 if not exsit
	 *  @return customer id if exsit
	 */
	int login(String name,String password);
	/** select a row by id **/
	Customer getById(int id);
	/** update customer **/
	void update(Customer customer);
}
