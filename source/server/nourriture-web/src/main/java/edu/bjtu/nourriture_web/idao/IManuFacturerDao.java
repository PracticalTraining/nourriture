package edu.bjtu.nourriture_web.idao;

import java.util.List;

import edu.bjtu.nourriture_web.bean.ManuFacturer;

public interface IManuFacturerDao {
	/** add one row **/
	int add(ManuFacturer manufacturer);
	/** check if name already exists **/
	boolean isNameExist(String name);
	/** search the manuFacturer according to name **/
	List<ManuFacturer> searchByName(String name);
	/** check if there exists a row with given name and password
	 *  @return -1 if not exists
	 *  @return manuFacturer id if exists
	 */
	int login(String name,String password);
	/** select a row by id **/
	ManuFacturer getById(int id);
	/** update manuFacturer **/
	void update(ManuFacturer manuFacturer);
}
