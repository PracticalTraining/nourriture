package edu.bjtu.nourriture_web.idao;

import java.util.List;

import edu.bjtu.nourriture_web.bean.Region;

public interface IRegionDao {
	/** check if SuperiorRegionId exist **/
	boolean isSuperiorRegionIdExist(int superiorRegionId);

	/** add a row to database **/
	int add(Region region);

	/** search region by id **/
	List<Region> searchSuperiorRegionById(int id);

	/** search the detail info of the region by id **/
	Region searchRegionDetailById(int id);

	/** get region by id **/
	Region getById(int id);

	/** delect the region **/
	void delete(Region deleteRegion);

	/** update region **/
	void update(Region updateRegion);

	boolean isRegionExist(int regionId);
	
	List<Region> getChildren(int pId);
}
