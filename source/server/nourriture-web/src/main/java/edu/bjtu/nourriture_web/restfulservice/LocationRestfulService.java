package edu.bjtu.nourriture_web.restfulservice;

import javax.ws.rs.DELETE;
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

import edu.bjtu.nourriture_web.bean.Customer;
import edu.bjtu.nourriture_web.bean.Location;
import edu.bjtu.nourriture_web.common.RestfulServiceUtil;
import edu.bjtu.nourriture_web.idao.ILocationDao;

@Path("location")
@Produces("application/json")
public class LocationRestfulService {
	//dao
	private ILocationDao locationDao;
	//direct children links
	private JsonArray locationChildrenLinks;
	private JsonArray idChildrenLinks;
	//get set method for spring IOC
	public ILocationDao getLocationDao() {
			return locationDao;
	}
	
	public void setLocationDao(ILocationDao locationDao) {
			this.locationDao = locationDao;
	}
	
	{
	//initialize direct children links
			locationChildrenLinks = new JsonArray();
			RestfulServiceUtil.addChildrenLinks(locationChildrenLinks, "search location", "/search", "GET");
			RestfulServiceUtil.addChildrenLinks(locationChildrenLinks, "get location according to id", "/{id}", "GET");
			RestfulServiceUtil.addChildrenLinks(locationChildrenLinks, "update location according to id", "/{id}", "PUT");
			idChildrenLinks = new JsonArray();
			RestfulServiceUtil.addChildrenLinks(idChildrenLinks, "get location", "/{interest}", "GET");
			RestfulServiceUtil.addChildrenLinks(idChildrenLinks, "add a customer interest", "/{interest}", "POST");
			RestfulServiceUtil.addChildrenLinks(idChildrenLinks, "delete a customer interest", "/{interest}", "DELETE");
    }
	/**add a location**/
	@POST
	public String addLocation(@FormParam("regionId") int regionId,@FormParam("detailAddress") String detailAddress,
			@FormParam("longitude") int longitude,@FormParam("latitude") int latitude){
		JsonObject ret = new JsonObject();
		//define error code
		final int ERROR_CODE_REGION_NOT_EXIST = -1;
		final int ERROR_CODE_BAD_PARAM = -2;
		//check request parameters
		if(regionId <= 0 || detailAddress == null || detailAddress.equals("")
		        || longitude < 0 || latitude < 0){
		    ret.addProperty("errorCode", ERROR_CODE_BAD_PARAM);
	        ret.add("links", locationChildrenLinks);
		    return ret.toString();
		}
		//check if region is not exist
		if(!locationDao.isRegionNotExist(regionId)){
			ret.addProperty("errorCode", ERROR_CODE_REGION_NOT_EXIST);
			ret.add("links", locationChildrenLinks);
			return ret.toString();
		}
		//add one row to database
		Location location = new Location();
		location.setRegionId(regionId);
		location.setDetailAddress(detailAddress);
		location.setLongitude(longitude);
		location.setLatitude(latitude);
		ret.addProperty("id", locationDao.add(location));
		ret.add("links", locationChildrenLinks);
		return ret.toString();
	}
	
	/** search location by id **/
	@GET
	@Path("{id}")
	public String getById(@PathParam("id") int id){
		JsonObject ret = new JsonObject();
		//define error code
		final int ERROR_CODE_LOCATION_NOT_EXIST = -1;
		//select from database
		Location location = locationDao.getById(id);
		if(location == null)
		{
			ret.addProperty("errorCode", ERROR_CODE_LOCATION_NOT_EXIST);
			ret.add("links", idChildrenLinks);
			return ret.toString();
		}
		ret.add("links", idChildrenLinks);
		return ret.toString();
	}
	
	/** update customer basic information **/
	@PUT
	@Path("{id}")
	public String updateLocation(@PathParam("id") int id,
			@FormParam("regionId") int regionId,@FormParam("detailAddress") String detailAddress,
			@FormParam("longitude") int longitude,@FormParam("latitude") int latitude){
		JsonObject ret = new JsonObject();
		//define error code
		final int ERROR_CODE_LOCATION_NOT_EXIST = -1;
		final int ERROR_CODE_BAD_PARAM = -2;
		//check request parameters
		if(regionId <= 0 || detailAddress == null || detailAddress.equals("")
		        || longitude < 0 || latitude < 0){
		    ret.addProperty("errorCode", ERROR_CODE_BAD_PARAM);
	        ret.add("links", locationChildrenLinks);
		    return ret.toString();
		}
		//check if Location exsit
		Location location = locationDao.getById(id);
		if(location == null)
		{
			ret.addProperty("errorCode", ERROR_CODE_LOCATION_NOT_EXIST);
			ret.add("links", idChildrenLinks);
			return ret.toString();
		}
		location.setRegionId(regionId);
		location.setDetailAddress(detailAddress);
		location.setLongitude(longitude);
		location.setLatitude(latitude);
		ret.addProperty("result", 0);
		ret.add("links", idChildrenLinks);
		return ret.toString();
	}
	/** delete a interest **/
	@DELETE
	@Path("{id}")
	public String deleteLocation(@PathParam("id") int id){
		JsonObject ret = new JsonObject();
		//define error code
		final int ERROR_CODE_LOCATION_NOT_EXIST = -1;
		final int ERROR_CODE_BAD_PARAM = -2;
		//check request parameters
		if(id <= 0){
			 ret.addProperty("errorCode", ERROR_CODE_BAD_PARAM);
		     ret.add("links", locationChildrenLinks);
			 return ret.toString();
		}
		//check if Location exsit
		Location location = locationDao.getById(id);
		if(location == null)
		{
			ret.addProperty("errorCode", ERROR_CODE_LOCATION_NOT_EXIST);
			ret.add("links", idChildrenLinks);
			return ret.toString();
		}
		//delete interest to database
		locationDao.deletebyid(id);
		locationDao.update(location);
		ret.addProperty("result", 0);
		ret.add("links", idChildrenLinks);
		return ret.toString();
	}


}

