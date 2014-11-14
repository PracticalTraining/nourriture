package edu.bjtu.nourriture_web.idao;

import java.util.List;

import edu.bjtu.nourriture_web.bean.Region;

public interface IRegionDao {
	/** check if SuperiorRegionId exist **/
	boolean isSuperiorRegionIdExist(int superiorRegionId);

	/** add a row to database **/
	int add(Region region);

	/** search region by name **/
	List<Region> searchRegionByName(String name);

	/** search the detail info of the region by name **/
	List<Region> searchRegionDetailByName(String name);

	/** get region by id **/
	Region getById(int id);

	/** delect the region **/
	void delete(Region deleteRegion);

	boolean isRegionExist(int regionId);
}
