package edu.bjtu.nourriture_web.restfulservice;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import com.google.gson.JsonObject;

import edu.bjtu.nourriture_web.bean.Customer;
import edu.bjtu.nourriture_web.bean.FoodCategory;
import edu.bjtu.nourriture_web.dao.FoodCategoryDao;
import edu.bjtu.nourriture_web.idao.IFoodCategoryDao;

@Path("foodCategory")
public class FoodCategoryRestfulService {
	private IFoodCategoryDao foodCategoryDao;

	
	public IFoodCategoryDao getFoodCategoryDao() {
		return foodCategoryDao;
	}


	public void setFoodCategoryDao(IFoodCategoryDao foodCategoryDao) {
		this.foodCategoryDao = foodCategoryDao;
	}


	/** add a foodcategory **/
	@POST
	public String addFoodCategory(@FormParam("name") String name,@FormParam("topCategory") String topcategory,
			@FormParam("superiorCategoryId") String superiorCategory){
		JsonObject ret = new JsonObject();
		//define error code
		final int ERROR_CODE_UNTOPCATEGORY_SUPERIORCATEGORYID_UNEXIST = -1;
		final int ERROR_CODE_TOPCATEGORY_SUPERIORCATEGORYID_EXIST = -2;
		final int ERROR_CODE_PARAMETER_ILLEGAL = -3;
		Object superiorCategoryId;
		//check request parameters
		if(topcategory == null || topcategory.equals("") || superiorCategoryId == null || superiorCategoryId.equals(""))
				{
			ret.addProperty("errorCode", ERROR_CODE_PARAMETER_ILLEGAL);
			return ret.toString();
		}
		//-1 
		if(topcategory == null || superiorCategoryId == null )
		{
	ret.addProperty("errorCode",  ERROR_CODE_UNTOPCATEGORY_SUPERIORCATEGORYID_UNEXIST);
	return ret.toString();
}
		//add one row to database
		FoodCategory foodcategory = new FoodCategory();
		foodcategory.setName(name);
		foodcategory.setSuperiorCategoryId(superiorCategoryId);
		foodcategory.setTopCategory(topcategory);

		return ret.toString();
	}
	
	
	/** update foodcategory basic information by id**/
	@PUT
	@Path("{id}")
	public String updateFoodCategory(@PathParam("id") int id,
			@FormParam("name") @DefaultValue("-1") String name){
		JsonObject ret = new JsonObject();
		//define error code
		final int ERROR_CODE_FOODCATEGORY_NOT_EXIST = -1;
		final int ERROR_CODE_BAD_PARAM = -2;
		//check request parameters
		if((name 的请求参数不合法?){
			ret.addProperty("errorCode", ERROR_CODE_BAD_PARAM);
			ret.add("links", ret);
			return ret.toString();
		}
		//check if foodcategory exsit
		FoodCategory foodcategory =foodCategoryDao.getById(id);
		if(foodcategory == null){
			ret.addProperty("errorCode", ERROR_CODE_FOODCATEGORY_NOT_EXIST);
			ret.add("links", ret);
			return ret.toString();
		}
		foodcategory.setName(name);
		foodCategoryDao.update(foodcategory);
		ret.addProperty("result", 0);
		ret.add("links", ret);
		return ret.toString();
	}
	
}
