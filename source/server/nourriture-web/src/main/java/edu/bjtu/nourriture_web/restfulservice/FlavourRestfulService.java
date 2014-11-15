package edu.bjtu.nourriture_web.restfulservice;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import edu.bjtu.nourriture_web.bean.Flavour;

import edu.bjtu.nourriture_web.common.JsonUtil;
import edu.bjtu.nourriture_web.common.RestfulServiceUtil;
import edu.bjtu.nourriture_web.dao.FlavourDao;
import edu.bjtu.nourriture_web.idao.IFlavourDao;


@Path("flavour")
public class FlavourRestfulService {
	FlavourDao FlavourDao;
	// direct children links
	private JsonArray FlavourChildrenLinks;

	// get set method for spring IOC

	public FlavourDao getFlavourDao() {
		return FlavourDao;
	}

	public void setFlavourDao(FlavourDao FlavourDao) {
		this.FlavourDao = FlavourDao;
	}

	{
		// initialize direct children links
		FlavourChildrenLinks = new JsonArray();
		RestfulServiceUtil.addChildrenLinks(FlavourChildrenLinks,
				"get superior food", "/getTop", "GET");
		RestfulServiceUtil.addChildrenLinks(FlavourChildrenLinks,
				"get flavour's detail information", "/{id}", "GET");
		RestfulServiceUtil.addChildrenLinks(FlavourChildrenLinks,
				"update classification of flavour", "/{id}", "PUT");
		RestfulServiceUtil.addChildrenLinks(FlavourChildrenLinks,
				"delete classification of flavour", "/{id}", "DELETE");
	}

	/** add Flavour **/
	@POST
	public String addFlavour(@FormParam("name") String name,
			@FormParam("topCategory") boolean topCategory,
			@FormParam("superiorCategoryId") int superiorCategoryId) {
		JsonObject ret = new JsonObject();
		// define errorCode
		final int ERROR_CODE_BAD_PARAM = -1;
		final int ERROR_CODE_NOTTOPCATEGORY_SUPERIORCATEGORY_NOTEXIST = -2;
		final int ERROR_CODE_TOPCATEGORY__SUPERIORCATEGORYEXIST = -3;

		// check bad request parameter
		if (name == null || "".equals(name) || superiorCategoryId < 0) {
			ret.addProperty("errorCode", ERROR_CODE_BAD_PARAM);
			ret.add("links", FlavourChildrenLinks);
			return ret.toString();
		}
		// check parameters
		if (!topCategory
				&& FlavourDao
						.isSuperiorCategoryIdExist(superiorCategoryId) == false) {
			ret.addProperty("errorCode",
					ERROR_CODE_NOTTOPCATEGORY_SUPERIORCATEGORY_NOTEXIST);
			ret.add("links", FlavourChildrenLinks);
			return ret.toString();
		}
		// check parameters
		if (topCategory
				&& FlavourDao
						.isSuperiorCategoryIdExist(superiorCategoryId) == true) {
			ret.addProperty("errorCode",
					ERROR_CODE_TOPCATEGORY__SUPERIORCATEGORYEXIST);
			ret.add("links", FlavourChildrenLinks);
			return ret.toString();
		}
		if (topCategory && superiorCategoryId == 0) {

			// add one row to database
			Flavour Flavour = new Flavour();
			Flavour.setName(name);
			Flavour.setTopCategory(topCategory);
			Flavour.setSuperiorCategoryId(superiorCategoryId);
			ret.addProperty("id", FlavourDao.add(Flavour));
			ret.add("links", FlavourChildrenLinks);
		}
		return ret.toString();

	}

	/** get the superior classfication **/
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
			ret.add("links", FlavourChildrenLinks);
			return ret.toString();
		}
		if (FlavourDao.isFlavourExist(id) == false) {
			ret.addProperty("errorCode", ERROR_CODE_RECIPECATEGORY_NOT_EXIST);
			ret.add("links", FlavourChildrenLinks);
			return ret.toString();
		}
		// search superiorCategoryId from the database
		// int superiorCategoryId = FlavourDao.getSuperiorCategoryId(id);
		Flavour Flavour = FlavourDao
				.searchFlavourDetailById(id);
		int superiorCategoryId = Flavour.getSuperiorCategoryId();
		if (superiorCategoryId == 0) {
			ret.addProperty("errorCode", ERROR_CODE_HASNOR_TOPCATEGROY);
			ret.add("links", FlavourChildrenLinks);
			return ret.toString();
		}
		Flavour FlavourDetailInfo = FlavourDao
				.searchFlavourDetailById(superiorCategoryId);
		JsonObject jSuperiorFlavour = transformFlavourToJson(FlavourDetailInfo);
		ret.add("superiorFlavour", jSuperiorFlavour);
		ret.add("links", FlavourChildrenLinks);
		return ret.toString();

	}

	/** get the detail info of Flavour by id **/
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
			ret.add("links", FlavourChildrenLinks);
			return ret.toString();
		}
		if (!FlavourDao.isFlavourExist(id)) {
			ret.addProperty("errorCode", ERROR_CODE_NO_RESULT);
			ret.add("links", FlavourChildrenLinks);
			return ret.toString();
		}
		// search the database
		Flavour FlavourDetailInfo = FlavourDao
				.searchFlavourDetailById(id);

		JsonObject jSuperiorFlavour = transformFlavourToJson(FlavourDetailInfo);
		ret.add("superiorFlavour", jSuperiorFlavour);
		ret.add("links", FlavourChildrenLinks);
		return ret.toString();
	}

	/** update the Flavour **/
	@PUT
	@Path("{id}")
	public String updateFlavour(@PathParam("id") int id,
			@FormParam("name") String name,
			@FormParam("topCategory") boolean topCategory,
			@FormParam("superiorCategoryId") int superiorCategoryId) {
		JsonObject ret = new JsonObject();
		// define errorCode
		final int ERROR_CODE_BAD_PARAM = -1;
		final int ERROR_CODE_NORPROVICE_SUPERIORREGIONNOTEXIST = -2;
		final int ERROR_CODE_PROVICE_SUPERIOREXIST = -3;

		final int ERROR_CODE_NO_RESULT = -4;
		// check bad request parameter
		if (name == null || "".equals(name) || superiorCategoryId < 0) {
			ret.addProperty("errorCode", ERROR_CODE_BAD_PARAM);
			ret.add("links", FlavourChildrenLinks);
			return ret.toString();
		}
		// check parameters
		if (!topCategory
				&& FlavourDao
						.isSuperiorCategoryIdExist(superiorCategoryId) == false) {
			ret.addProperty("errorCode",
					ERROR_CODE_NORPROVICE_SUPERIORREGIONNOTEXIST);
			ret.add("links", FlavourChildrenLinks);
			return ret.toString();
		}
		// check parameters
		if (topCategory
				&& FlavourDao
						.isSuperiorCategoryIdExist(superiorCategoryId) == true) {
			ret.addProperty("errorCode", ERROR_CODE_PROVICE_SUPERIOREXIST);
			ret.add("links", FlavourChildrenLinks);
			return ret.toString();
		}

		// search the database
		Flavour updateFlavour = FlavourDao.getById(id);
		if (updateFlavour == null) {
			ret.addProperty("errorCode", ERROR_CODE_NO_RESULT);
			ret.add("links", FlavourChildrenLinks);
			return ret.toString();
		}
		if (topCategory && superiorCategoryId == 0) {

			updateFlavour.setName(name);
			updateFlavour.setTopCategory(topCategory);
			updateFlavour.setSuperiorCategoryId(superiorCategoryId);
			FlavourDao.update(updateFlavour);
			ret.addProperty("id", id);
			ret.add("links", FlavourChildrenLinks);
		}
		return ret.toString();
	}

	/** delete the Flavour by id **/
	@DELETE
	@Path("{id}")
	public String deleteFlavourById(@PathParam("id") int id) {
		JsonObject ret = new JsonObject();
		// define errorCode
		final int ERROR_CODE_BAD_PARAM = -1;
		final int ERROR_CODE_RECIPECATEGORY_NOT_EXIST = -2;
		final int ERROR_CODE_NO_RESULT = -3;
		// check request parameters
		if (id < 0) {
			ret.addProperty("errorCode", ERROR_CODE_BAD_PARAM);
			ret.add("links", FlavourChildrenLinks);
			return ret.toString();
		}
		if (FlavourDao.isFlavourExist(id) == false) {
			ret.addProperty("errorCode", ERROR_CODE_RECIPECATEGORY_NOT_EXIST);
			ret.add("links", FlavourChildrenLinks);
			return ret.toString();
		}
		Flavour deleteFlavour = FlavourDao
				.searchFlavourDetailById(id);
		int superiorCategoryId = deleteFlavour.getSuperiorCategoryId();
		// this recipeCategoey is topRecipeCategory
		if (superiorCategoryId == 0) {
			List<Flavour> deleteFoodCategories = new ArrayList<Flavour>();
			List<Flavour> foodCategories = FlavourDao
					.getAllFlavour();
			for (Flavour Flavour : foodCategories) {
				if (Flavour.getSuperiorCategoryId() == id) {
					deleteFoodCategories.add(Flavour);
				}
			}
			for (Flavour Flavour : deleteFoodCategories) {
				FlavourDao.delete(Flavour);
				ret.addProperty("id", id);
				ret.add("links", FlavourChildrenLinks);
			}
			FlavourDao.delete(deleteFlavour);
			ret.addProperty("id", id);
			ret.add("links", FlavourChildrenLinks);
			return ret.toString();
		}
		FlavourDao.delete(deleteFlavour);
		ret.addProperty("id", id);
		ret.add("links", FlavourChildrenLinks);
		return ret.toString();

	}

	/**
	 * transform Flavour from bean to json
	 * 
	 * @param category
	 * @return
	 */
	private JsonObject transformFlavourToJson(Flavour category) {
		JsonObject jFlavour = JsonUtil.beanToJson(category);
		return jFlavour;
	}

}
