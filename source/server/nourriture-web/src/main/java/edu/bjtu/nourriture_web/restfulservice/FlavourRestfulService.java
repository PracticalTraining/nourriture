package edu.bjtu.nourriture_web.restfulservice;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import com.google.gson.JsonObject;

import edu.bjtu.nourriture_web.bean.Flavour;
import edu.bjtu.nourriture_web.bean.FoodCategory;
import edu.bjtu.nourriture_web.idao.IFlavourDao;
import edu.bjtu.nourriture_web.idao.IFoodCategoryDao;

@Path("flavour")
public class FlavourRestfulService {
	private IFlavourDao flavourDao;

	public IFlavourDao getFlavourDao() {
		return flavourDao;
	}

	public void setFlavourDao(IFlavourDao flavourDao) {
		this.flavourDao = flavourDao;
	}
	
	
	

	
	
	/** update flavour  basic information by id**/
	@PUT
	@Path("{id}")
	public String updateFlavour(@PathParam("id") int id,
			@FormParam("name") @DefaultValue("-1") String name){
		JsonObject ret = new JsonObject();
		//define error code
		final int ERROR_CODE_FLAVOUR_NOT_EXIST = -1;
		final int ERROR_CODE_BAD_PARAM = -2;
		//check request parameters
		if((name 的请求参数不合法?){
			ret.addProperty("errorCode", ERROR_CODE_BAD_PARAM);
			ret.add("links", ret);
			return ret.toString();
		}
		//check if flavour exsit
		Flavour flavour =flavourDao.getById(id);
		if(flavour == null){
			ret.addProperty("errorCode", ERROR_CODE_FLAVOUR_NOT_EXIST);
			ret.add("links", ret);
			return ret.toString();
		}
		flavour.setName(name);
		((IFlavourDao) flavour).update(flavour);
		ret.addProperty("result", 0);
		ret.add("links", ret);
		return ret.toString();
	}
	

}
