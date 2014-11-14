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
import edu.bjtu.nourriture_web.idao.IRegionDao;

@Path("location")
@Produces("application/json")
public class LocationRestfulService {
	//dao
	private ILocationDao locationDao;
	private IRegionDao regionDao;
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
	public IRegionDao getRegionDao() {
		return regionDao;
    }

     public void setRegionDao(IRegionDao regionDao) {
		this.regionDao = regionDao;
    }
	
	{
	//initialize direct children links
			locationChildrenLinks = new JsonArray();
			RestfulServiceUtil.addChildrenLinks(locationChildrenLinks, "get location according to id", "/{id}", "GET");
			RestfulServiceUtil.addChildrenLinks(locationChildrenLinks, "update location according to id", "/{id}", "PUT");
			RestfulServiceUtil.addChildrenLinks(locationChildrenLinks, "delete location according to id", "/{id}", "DELETE");
			idChildrenLinks = new JsonArray();
    }
	/**add a location**/
	@POST
	public String addLocation(@FormParam("regionId") int regionId,@FormParam("detailAddress") String detailAddress,
			@FormParam("longitude") @DefaultValue("404") double longitude,@FormParam("latitude") @DefaultValue("404") double latitude){
		JsonObject ret = new JsonObject();
		//define error code
		final int ERROR_CODE_REGION_NOT_EXIST = -1;
		final int ERROR_CODE_BAD_PARAM = -2;
		//check request parameters
	    if(regionId <= 0 || detailAddress == null || detailAddress.equals("")
	        || !((longitude > -180 && longitude < 180 && latitude > -90 && latitude < 90) || (longitude == 404 && latitude == 404))){
	           ret.addProperty("errorCode", ERROR_CODE_BAD_PARAM);
               ret.add("links", locationChildrenLinks);
	           return ret.toString();
	    }
		//check if region is not exist
		if(!regionDao.isRegionExist(regionId)){
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
	public String searchLocationById(@PathParam("id") int id){
		JsonObject ret = new JsonObject();
		//define error code
		final int ERROR_CODE_LOCATION_NOT_EXIST = -1;
		Location location = locationDao.getById(id);
		if(location == null)
		{
			ret.addProperty("errorCode", ERROR_CODE_LOCATION_NOT_EXIST);
			ret.add("links", idChildrenLinks);
			return ret.toString();
		}
		ret.addProperty("id", id);
		ret.addProperty("regionId", location.getRegionId());
		ret.addProperty("detailAddress", location.getDetailAddress());
		ret.addProperty("longitude", location.getLongitude());
		ret.addProperty("latitude", location.getLatitude());
		ret.add("links", idChildrenLinks);
		return ret.toString();
	}
	
	/** update location information **/
	@PUT
	@Path("{id}")
	public String updateLocation(@PathParam("id") int id,
			@FormParam("regionId") int regionId,@FormParam("detailAddress") String detailAddress,
			@FormParam("longitude") int longitude,@FormParam("latitude") int latitude){
		JsonObject ret = new JsonObject();
		//define error code
		final int ERROR_CODE_LOCATION_NOT_EXIST = -1;
		final int ERROR_CODE_BAD_PARAM = -2;
		final int ERROR_CODE_REGION_NOT_EXIST= -3;
		//check request parameters
		if(regionId <= 0 || detailAddress == null || detailAddress.equals("")
		        || !((longitude > -180 && longitude < 180 && latitude > -90 && latitude < 90))|| (longitude == 404 && latitude == 404)){
		           ret.addProperty("errorCode", ERROR_CODE_BAD_PARAM);
	               ret.add("links", idChildrenLinks);
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
		//check if region exsit
		if(!regionDao.isRegionExist(regionId)){
			ret.addProperty("errorCode", ERROR_CODE_REGION_NOT_EXIST);
			ret.add("links", idChildrenLinks);
			return ret.toString();
		}
		//update the database
		location.setRegionId(regionId);
		location.setDetailAddress(detailAddress);
		location.setLongitude(longitude);
		location.setLatitude(latitude);
		locationDao.update(location);
		ret.addProperty("result", 0);
		ret.add("links", idChildrenLinks);
		return ret.toString();
	}
	/** delete a location **/
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
		     ret.add("links", idChildrenLinks);
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
		ret.addProperty("result", 0);
		ret.add("links", idChildrenLinks);
		return ret.toString();
	}


}

