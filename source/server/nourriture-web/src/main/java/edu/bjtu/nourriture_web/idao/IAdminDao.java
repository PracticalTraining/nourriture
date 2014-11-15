package edu.bjtu.nourriture_web.idao;

import edu.bjtu.nourriture_web.bean.Admin;

public interface IAdminDao {
	/** get detail information of the Admin by id **/
	Admin getById(int id);
	/** check if there exsit a row with given name and password
	 *  @return -1 if not exsit
	 *  @return customer id if exsit
	 */
	int login(String name,String password);
	boolean isNameExist(String name);
	//void deleteById(int id);
}
