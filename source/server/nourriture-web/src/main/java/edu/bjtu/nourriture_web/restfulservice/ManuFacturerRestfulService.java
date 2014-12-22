package edu.bjtu.nourriture_web.restfulservice;

import java.util.List;

import javax.ws.rs.DefaultValue;
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

import edu.bjtu.nourriture_web.bean.Comments;
import edu.bjtu.nourriture_web.bean.Food;
import edu.bjtu.nourriture_web.bean.ManuFacturer;
import edu.bjtu.nourriture_web.common.JsonUtil;
import edu.bjtu.nourriture_web.common.RestfulServiceUtil;
import edu.bjtu.nourriture_web.idao.ICommentsDao;
import edu.bjtu.nourriture_web.idao.IFoodDao;
import edu.bjtu.nourriture_web.idao.IManuFacturerDao;

@Path("/manuFacturer")
@Produces("application/json;charset=UTF-8")
public class ManuFacturerRestfulService {
	// dao
	private IManuFacturerDao manuFacturerDao;
	private IFoodDao foodDao;
	private ICommentsDao commentsDao;

	// direct children links
	private JsonArray manuFacturerChildrenLinks;
	private JsonArray searchChildrenLinks;
	private JsonArray loginChildrenLinks;
	private JsonArray idChildrenLinks;
	private JsonArray numChildrenLinks;
	private JsonArray sumChildrenLinks;
	private JsonArray interestingChildrenLinks;

	// get set method for spring IOC
	public IManuFacturerDao getManuFacturerDao() {
		return manuFacturerDao;
	}

	public void setManuFacturerDao(IManuFacturerDao manuFacturerDao) {
		this.manuFacturerDao = manuFacturerDao;
	}

	public IFoodDao getFoodDao() {
		return foodDao;
	}

	public void setFoodDao(IFoodDao foodDao) {
		this.foodDao = foodDao;
	}

	public ICommentsDao getCommentsDao() {
		return commentsDao;
	}

	public void setCommentsDao(ICommentsDao commentsDao) {
		this.commentsDao = commentsDao;
	}

	{
		// initialize direct children links
		manuFacturerChildrenLinks = new JsonArray();
		RestfulServiceUtil.addChildrenLinks(manuFacturerChildrenLinks,
				"search manuFacturer", "/search", "GET");
		RestfulServiceUtil.addChildrenLinks(manuFacturerChildrenLinks, "login",
				"/login", "GET");
		RestfulServiceUtil.addChildrenLinks(manuFacturerChildrenLinks,
				"get manuFacturer according to id", "/{id}", "GET");
		RestfulServiceUtil.addChildrenLinks(manuFacturerChildrenLinks,
				"update manuFacturer according to id", "/{id}", "PUT");
		searchChildrenLinks = new JsonArray();
		loginChildrenLinks = new JsonArray();
	}

	/** add a manufacturer **/
	@POST
	public String addManuFacturer(@FormParam("name") String name,
			@FormParam("password") String password,
			@FormParam("companyName") String companyName,
			@FormParam("description") String description) {
		JsonObject ret = new JsonObject();

		// define error code
		final int ERROR_CODE_MANUFACTURER_EXIST = -1;
		final int ERROR_CODE_BAD_PARAM = -2;

		// check request parameters
		if (name == null || name.equals("") || password == null
				|| password.equals("") || companyName == null
				|| companyName.equals("") || description == null
				|| description.equals("")) {
			ret.addProperty("errorCode", ERROR_CODE_BAD_PARAM);
			ret.add("links", manuFacturerChildrenLinks);
			return ret.toString();
		}

		// check if user name is already exist
		if (manuFacturerDao.isNameExist(name)) {
			ret.addProperty("errorCode", ERROR_CODE_MANUFACTURER_EXIST);
			ret.add("links", manuFacturerChildrenLinks);
			return ret.toString();
		}

		// add one row to database
		ManuFacturer manuFacturer = new ManuFacturer();
		manuFacturer.setName(name);
		manuFacturer.setPassword(password);
		manuFacturer.setCompanyName(companyName);
		manuFacturer.setDescription(description);
		ret.addProperty("id", manuFacturerDao.add(manuFacturer));
		ret.add("links", manuFacturerChildrenLinks);
		return ret.toString();
	}

	/** search manufacturer by name **/
	@GET
	@Path("search")
	public String searchByName(@QueryParam("companyName") String companyName) {
		JsonObject ret = new JsonObject();

		// define error code
		final int ERROR_CODE_NO_RESULT = -1;
		final int ERROR_CODE_BAD_PARAM = -2;

		// check request parameters
		if (companyName == null || companyName.equals("")) {
			ret.addProperty("errorCode", ERROR_CODE_BAD_PARAM);
			ret.add("links", searchChildrenLinks);
			return ret.toString();
		}

		// search in the database
		List<ManuFacturer> list = manuFacturerDao
				.searchByCompanyName(companyName);
		if (list.isEmpty()) {
			ret.addProperty("errorCode", ERROR_CODE_NO_RESULT);
			ret.add("links", searchChildrenLinks);
			return ret.toString();
		}

		JsonArray manuFacturers = new JsonArray();
		for (ManuFacturer manuFacturer : list) {
			JsonObject jManuFacturer = transformManuFacturer(manuFacturer);
			manuFacturers.add(jManuFacturer);
		}
		ret.add("manufacturers", manuFacturers);
		ret.add("links", searchChildrenLinks);
		return ret.toString();
	}

	/** login by name and password **/
	@GET
	@Path("login")
	public String login(@QueryParam("name") String name,
			@QueryParam("password") String password) {
		JsonObject ret = new JsonObject();

		// define error code
		final int ERROR_CODE_MANUFACTURER_NOT_EXIST = -1;
		final int ERROR_CODE_PASSWORD_NOT_VALIDATED = -2;
		final int ERROR_CODE_BAD_PARAM = -3;

		// check request parameters
		if (name == null || name.equals("") || password == null
				|| password.equals("")) {
			ret.addProperty("errorCode", ERROR_CODE_BAD_PARAM);
			ret.add("links", loginChildrenLinks);
			return ret.toString();
		}

		// check if user name is exist
		if (!manuFacturerDao.isNameExist(name)) {
			ret.addProperty("errorCode", ERROR_CODE_MANUFACTURER_NOT_EXIST);
			ret.add("links", loginChildrenLinks);
			return ret.toString();
		}

		// check password
		int loginResult = manuFacturerDao.login(name, password);
		if (loginResult == -1) {
			ret.addProperty("errorCode", ERROR_CODE_PASSWORD_NOT_VALIDATED);
			ret.add("links", loginChildrenLinks);
			return ret.toString();
		}
		ret.addProperty("id", loginResult);
		ret.add("links", loginChildrenLinks);
		return ret.toString();
	}

	/** get detail information about a manuFacturer by id **/
	@GET
	@Path("{id}")
	public String getById(@PathParam("id") int id) {
		JsonObject ret = new JsonObject();
		// select from database
		ManuFacturer manuFacturer = manuFacturerDao.getById(id);

		if (manuFacturer != null) {
			JsonObject jManuFacturer = transformManuFacturer(manuFacturer);
			ret.add("manuFacturer", jManuFacturer);
		}
		ret.add("links", idChildrenLinks);
		return ret.toString();
	}

	private JsonObject transformManuFacturer(ManuFacturer manuFacturer) {
		JsonObject jManuFacturer = JsonUtil.beanToJson(manuFacturer);
		return jManuFacturer;
	}

	/** get the sum of food of a manufacturer by id **/
	@GET
	@Path("{id}/foodCount")
	public String getSumById(@PathParam("id") int id) {
		JsonObject ret = new JsonObject();
		// define error code
		final int ERROR_CODE_MANUFACTURER_NOT_EXIST = -1;

		if (manuFacturerDao.getById(id) == null) {
			ret.addProperty("errorCode", ERROR_CODE_MANUFACTURER_NOT_EXIST);
			ret.add("links", numChildrenLinks);
			return ret.toString();
		}

		// select from database
		List<Food> food = foodDao.getByManufacturerId(id);
		int foodCount = food.size();
		ret.addProperty("foodCount", foodCount);
		ret.add("links", numChildrenLinks);
		return ret.toString();
	}

	/** get the score a manufacturer by id **/
	@GET
	@Path("{id}/score")
	public String getScoreById(@PathParam("id") int id) {
		JsonObject ret = new JsonObject();
		// define error code
		final int ERROR_CODE_MANUFACTURER_NOT_EXIST = -1;

		if (manuFacturerDao.getById(id) == null) {
			ret.addProperty("errorCode", ERROR_CODE_MANUFACTURER_NOT_EXIST);
			ret.add("links", numChildrenLinks);
			return ret.toString();
		}

		// select from database
		List<Food> food = foodDao.getByManufacturerId(id);

		int sum = 0;
		int foodCount = food.size();
		int[] foodIdList = new int[foodCount];
		int[] score = new int[foodCount];
		for (int i = 0; i < foodCount; i++) {
			foodIdList[i] = food.get(i).getId();
			List<Comments> comments = commentsDao.getByRefId(foodIdList[i]);
			int commentsCount = comments.size();
			for (int j = 0; j < commentsCount; j++) {
				score[i] += comments.get(j).getScore() / commentsCount;
			}
		}

		for (int k = 0; k < foodCount; k++) {
			sum += score[k] / foodCount;
		}

		ret.addProperty("sum", sum);
		ret.add("links", sumChildrenLinks);
		return ret.toString();
	}

	/** get the score a manufacturer by id **/
	@GET
	@Path("{id}/interestingNumber")
	public String getInterestingNumber(@PathParam("id") int id) {
		JsonObject ret = new JsonObject();
		// define error code
		final int ERROR_CODE_MANUFACTURER_NOT_EXIST = -1;

		// select from database
		List<Food> food = foodDao.getByManufacturerId(id);

		int commentsCount = 0;
		int foodCount = food.size();
		int[] foodIdList = new int[foodCount];

		for (int i = 0; i < foodCount; i++) {
			foodIdList[i] = food.get(i).getId();
			List<Comments> comments = commentsDao.getByRefId(foodIdList[i]);
			commentsCount += comments.size();
		}

		if ((food.equals(null)) || commentsCount < 0) {
			ret.addProperty("errorCode", ERROR_CODE_MANUFACTURER_NOT_EXIST);
			ret.add("links", interestingChildrenLinks);
		}
		ret.addProperty("commentsCount", commentsCount);
		ret.add("links", interestingChildrenLinks);
		return ret.toString();
	}

	/** update manuFacturer basic information **/
	@PUT
	@Path("{id}")
	public String updateManuFacturer(@PathParam("id") int id,
			@FormParam("companyName") @DefaultValue("") String companyName,
			@FormParam("description") @DefaultValue("") String description) {
		JsonObject ret = new JsonObject();

		// define error code
		final int ERROR_CODE_MANUFACTURER_NOT_EXIST = -1;
		final int ERROR_CODE_BAD_PARAM = -2;

		// check request parameters
		if ((companyName == null || companyName.equals(""))
				&& (description == null || description.equals(""))) {
			ret.addProperty("errorCode", ERROR_CODE_BAD_PARAM);
			ret.add("links", idChildrenLinks);
			return ret.toString();
		}

		// check if manuFacturer exist
		ManuFacturer manuFacturer = manuFacturerDao.getById(id);
		if (manuFacturer == null) {
			ret.addProperty("errorCode", ERROR_CODE_MANUFACTURER_NOT_EXIST);
			ret.add("links", idChildrenLinks);
			return ret.toString();
		}
		manuFacturer.setCompanyName(companyName);
		manuFacturer.setDescription(description);
		manuFacturerDao.update(manuFacturer);
		ret.addProperty("result", 0);
		ret.add("links", idChildrenLinks);
		return ret.toString();
	}

	/** update manuFacturer password **/
	@PUT
	@Path("password/{id}")
	public String updateManuFacturerPassword(@PathParam("id") int id,
			@FormParam("newpassword") @DefaultValue("") String newpassword) {
		JsonObject ret = new JsonObject();

		// define error code
		final int ERROR_CODE_MANUFACTURER_NOT_EXIST = -1;
		final int ERROR_CODE_BAD_PARAM = -2;

		// check request parameters
		if ((newpassword == null || newpassword.equals(""))) {
			ret.addProperty("errorCode", ERROR_CODE_BAD_PARAM);
			ret.add("links", idChildrenLinks);
			return ret.toString();
		}

		// check if manuFacturer exist
		ManuFacturer manuFacturer = manuFacturerDao.getById(id);
		if (manuFacturer == null) {
			ret.addProperty("errorCode", ERROR_CODE_MANUFACTURER_NOT_EXIST);
			ret.add("links", idChildrenLinks);
			return ret.toString();
		}
		manuFacturer.setPassword(newpassword);
		manuFacturerDao.update(manuFacturer);
		ret.addProperty("result", 0);
		ret.add("links", idChildrenLinks);
		return ret.toString();
	}
}
