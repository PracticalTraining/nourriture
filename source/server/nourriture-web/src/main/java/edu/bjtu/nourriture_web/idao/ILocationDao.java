package edu.bjtu.nourriture_web.idao;

import java.util.List;

import edu.bjtu.nourriture_web.bean.Customer;
import edu.bjtu.nourriture_web.bean.Location;

public interface ILocationDao {
	
    int add(Location location);
	/** search the customer according to name **/
	List<Location> searchById(int regionId);
	/** check if there exsit a row with given name and password
	 *  @return -1 if not exsit
	 *  @return customer id if exsit
	 */
	/** select a row by id **/
	Location getById(int id);
	/** update location **/
	void update(Location location);
	/**delete location by id**/
    void deletebyid(int id);
}
