package edu.bjtu.nourriture_web.restfulservice;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import edu.bjtu.nourriture_web.bean.Region;
import edu.bjtu.nourriture_web.common.JsonUtil;
import edu.bjtu.nourriture_web.common.RestfulServiceUtil;
import edu.bjtu.nourriture_web.dao.RegionDao;

@Path("region")
@Produces("application/json;charset=UTF-8")
public class RegionRestfulService {
	// dao
	private RegionDao regionDao;
	// direct children links
	private JsonArray regionChildrenLinks;

	// get set method for spring IOC
	public RegionDao getRegionDao() {
		return regionDao;
	}

	public void setRegionDao(RegionDao regionDao) {
		this.regionDao = regionDao;
	}

	{
		// initialize direct children links
		regionChildrenLinks = new JsonArray();
		// RestfulServiceUtil.addChildrenLinks(regionChildrenLinks,
		// "get superior region", "/superior", "GET");
		RestfulServiceUtil.addChildrenLinks(regionChildrenLinks,
				"get region's detail information", "/{id}", "GET");
		RestfulServiceUtil.addChildrenLinks(regionChildrenLinks,
				"update the information  of region", "/{id}", "PUT");
		RestfulServiceUtil.addChildrenLinks(regionChildrenLinks,
				"delete the region", "/{id}", "DELETE");

	}

	/** add region **/
	@POST
	public String addRegion(@FormParam("name") String name,
			@FormParam("province") boolean province,
			@FormParam("superiorRegionId") int superiorRegionId) {
		JsonObject ret = new JsonObject();
		// define errorCode
		final int ERROR_CODE_BAD_PARAM = -1;
		final int ERROR_CODE_NORPROVICE_SUPERIORREGIONNOTEXIST = -2;
		final int ERROR_CODE_PROVICE_SUPERIOREXIST = -3;

		// check bad request parameter
		if (name == null || "".equals(name) || superiorRegionId < 0) {
			ret.addProperty("errorCode", ERROR_CODE_BAD_PARAM);
			ret.add("links", regionChildrenLinks);
			return ret.toString();
		}
		// check parameters
		if (!province
				&& regionDao.isSuperiorRegionIdExist(superiorRegionId) == false) {
			ret.addProperty("errorCode",
					ERROR_CODE_NORPROVICE_SUPERIORREGIONNOTEXIST);
			ret.add("links", regionChildrenLinks);
			return ret.toString();
		}
		// check parameters
		if (province
				&& regionDao.isSuperiorRegionIdExist(superiorRegionId) == true) {
			ret.addProperty("errorCode", ERROR_CODE_PROVICE_SUPERIOREXIST);
			ret.add("links", regionChildrenLinks);
			return ret.toString();
		}

		// add one row to database
		if (province && superiorRegionId == 0) {
			Region region = new Region();
			region.setName(name);
			region.setProvince(province);
			region.setSuperiorRegionId(superiorRegionId);
			ret.addProperty("id", regionDao.add(region));
			ret.add("links", regionChildrenLinks);
		}
		return ret.toString();
	}

	/** get the superior region by id **/
	@GET
	@Path("getTop")
	public String getSuperiorById(@QueryParam("id") int id) {
		JsonObject ret = new JsonObject();
		// define errorCode
		final int ERROR_CODE_BAD_PARAM = -1;
		final int ERROR_CODE_RECIPECATEGORY_NOT_EXIST = -2;
		final int ERROR_CODE_HASNOR_TOPCATEGROY = -3;
		// check request parameters
		if (id < 0) {
			ret.addProperty("errorCode", ERROR_CODE_BAD_PARAM);
			ret.add("links", regionChildrenLinks);
			return ret.toString();
		}
		if (regionDao.isRegionExist(id) == false) {
			ret.addProperty("errorCode", ERROR_CODE_RECIPECATEGORY_NOT_EXIST);
			ret.add("links", regionChildrenLinks);
			return ret.toString();
		}
		Region region = regionDao.searchRegionDetailById(id);
		int superiorRegionId = region.getSuperiorRegionId();
		if (superiorRegionId == 0) {
			ret.addProperty("errorCode", ERROR_CODE_HASNOR_TOPCATEGROY);
			ret.add("links", regionChildrenLinks);
			return ret.toString();
		}
		Region superiorRegin = regionDao
				.searchRegionDetailById(superiorRegionId);
		JsonObject jSuperiorRecipe = transformRecipeToJson(superiorRegin);
		ret.add("superiorRecipe", jSuperiorRecipe);
		ret.add("links", regionChildrenLinks);
		return ret.toString();

	}

	/** get the detail info of region by name **/
	@GET
	@Path("{id}")
	public String getDetailInfoById(@PathParam("id") int id) {
		JsonObject ret = new JsonObject();
		// define errorCode
		final int ERROR_CODE_BAD_PARAM = -1;
		final int ERROR_CODE_NO_RESULT = -2;
		// check request parameters
		if (id < 0) {
			ret.addProperty("errorCode", ERROR_CODE_BAD_PARAM);
			ret.add("links", regionChildrenLinks);
			return ret.toString();
		}
		if (!regionDao.isRegionExist(id)) {
			ret.addProperty("errorCode", ERROR_CODE_NO_RESULT);
			ret.add("links", regionChildrenLinks);
			return ret.toString();
		}
		// search the database
		Region regionDetailInfo = regionDao.searchRegionDetailById(id);
		// if (regionDetailInfo.isEmpty()) {
		// ret.addProperty("errorCode", ERROR_CODE_NO_RESULT);
		// ret.add("links", regionChildrenLinks);
		// return ret.toString();
		// }

		JsonObject jSuperiorRecipe = transformRecipeToJson(regionDetailInfo);
		ret.add("superiorRecipe", jSuperiorRecipe);
		ret.add("links", regionChildrenLinks);
		return ret.toString();
	}

	/** update the region **/
	@PUT
	@Path("{id}")
	public String updateRegion(@PathParam("id") int id,
			@FormParam("name") String name,
			@FormParam("province") boolean province,
			@FormParam("superiorRegionId") int superiorRegionId) {
		JsonObject ret = new JsonObject();
		// define errorCode
		final int ERROR_CODE_BAD_PARAM = -1;
		final int ERROR_CODE_NORPROVICE_SUPERIORREGIONNOTEXIST = -2;
		final int ERROR_CODE_PROVICE_SUPERIOREXIST = -3;
		final int ERROR_CODE_NO_RESULT = -4;

		// check bad request parameter
		if (name == null || "".equals(name) || superiorRegionId < 0) {
			ret.addProperty("errorCode", ERROR_CODE_BAD_PARAM);
			ret.add("links", regionChildrenLinks);
			System.out.println(province);
			System.out.println(superiorRegionId);

			return ret.toString();
		}
		// check parameters
		if (!province
				&& regionDao.isSuperiorRegionIdExist(superiorRegionId) == false) {
			ret.addProperty("errorCode",
					ERROR_CODE_NORPROVICE_SUPERIORREGIONNOTEXIST);
			ret.add("links", regionChildrenLinks);
			return ret.toString();
		}
		// check parameters
		if (province
				&& regionDao.isSuperiorRegionIdExist(superiorRegionId) == true) {
			ret.addProperty("errorCode", ERROR_CODE_PROVICE_SUPERIOREXIST);
			ret.add("links", regionChildrenLinks);
			return ret.toString();
		}
		// search the database
		Region updateRegion = regionDao.getById(id);
		if (updateRegion == null) {
			ret.addProperty("errorCode", ERROR_CODE_NO_RESULT);
			ret.add("links", regionChildrenLinks);
			return ret.toString();
		}
		updateRegion.setName(name);
		updateRegion.setProvince(province);
		updateRegion.setSuperiorRegionId(superiorRegionId);
		regionDao.update(updateRegion);
		ret.addProperty("id", id);
		ret.add("links", regionChildrenLinks);
		return ret.toString();
	}

	/** delete the region by id **/
	@DELETE
	@Path("{id}")
	public String deleteRegionById(@PathParam("id") int id) {
		JsonObject ret = new JsonObject();
		// define errorCode
		final int ERROR_CODE_BAD_PARAM = -1;
		final int ERROR_CODE_RECIPECATEGORY_NOT_EXIST = -2;

		// check request parameters
		if (id < 0) {
			ret.addProperty("errorCode", ERROR_CODE_BAD_PARAM);
			ret.add("links", regionChildrenLinks);
			return ret.toString();
		}
		if (regionDao.isRegionExist(id) == false) {
			ret.addProperty("errorCode", ERROR_CODE_RECIPECATEGORY_NOT_EXIST);
			ret.add("links", regionChildrenLinks);
			return ret.toString();
		}
		Region deleteRegion = regionDao.searchRegionDetailById(id);
		int superiorRegionId = deleteRegion.getSuperiorRegionId();
		if (superiorRegionId == 0) {
			List<Region> deleteRegions = new ArrayList<Region>();
			List<Region> regions = regionDao.getAllRecipeCategory();
			for (Region region : regions) {
				if (region.getSuperiorRegionId() == id) {
					deleteRegions.add(region);
				}
			}
			for (Region regionToBedelete : deleteRegions) {
				regionDao.delete(regionToBedelete);
				ret.addProperty("id", id);
				ret.add("links", regionChildrenLinks);
			}
			regionDao.delete(deleteRegion);
			ret.addProperty("id", id);
			ret.add("links", regionChildrenLinks);
			return ret.toString();
		}
		regionDao.delete(deleteRegion);
		ret.addProperty("id", id);
		ret.add("links", regionChildrenLinks);
		return ret.toString();
	}

	/**
	 * transform region from bean to json
	 * 
	 * @param region
	 * @return
	 */
	private JsonObject transformRecipeToJson(Region region) {
		JsonObject jRegion = JsonUtil.beanToJson(region);
		return jRegion;
	}

}
