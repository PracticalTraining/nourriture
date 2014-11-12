package edu.bjtu.nourriture_web.restfulservice;

import java.util.UUID;

import javax.ws.rs.core.MultivaluedMap;

import org.junit.Assert;
import org.junit.Test;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

/**
 * test restful service about manufacturer
 * @author HJW
 *
 */
public class ManuFacturerTestCase {
	/** root path for services about manufacturer **/
	private final String WSROOT = "http://123.57.36.82/nourriture/ws/manuFacturer";
	/** web service test client **/
	private final Client client = Client.create();
	/** parse string to json **/
	private final JsonParser jsonParser = new JsonParser();

	/** add manuFacturer */
	@Test
	public void addManuFacturer(){
		WebResource rs = client.resource(WSROOT);
		//test case: return successfull
		//define parameters
		String name = UUID.randomUUID().toString();
		String password = "myManuFacturer";
		String companyName = "bjtu";
		String description = "sell books";
		MultivaluedMap<String, String> params = new MultivaluedMapImpl();
		params.add("name", name);
		params.add("password", password);
		params.add("companyName", companyName);
		params.add("description", description);
		//get response
		JsonObject response = jsonParser.parse(
				    rs.post(String.class,params)
				).getAsJsonObject();
		Assert.assertNotNull(response.get("id"));
		
		//test case: return with error ERROR_CODE_USER_EXSIT
		//define error code
		final int ERROR_CODE_USER_EXSIT = -1;
		//get response
		response = jsonParser.parse(
			    rs.post(String.class,params)
			).getAsJsonObject();
	    Assert.assertEquals(response.get("errorCode").getAsInt(),ERROR_CODE_USER_EXSIT);
	    
	    //test case: return with error ERROR_CODE_BAD_PARAM
	    //define error code
	    final int ERROR_CODE_BAD_PARAM = -2;
	    //define parameters
	    params = new MultivaluedMapImpl();
		params.add("name", null);
		params.add("password", password);
		params.add("companyName", companyName);
		params.add("description", description);
		//get response
		response = jsonParser.parse(
			    rs.post(String.class,params)
			).getAsJsonObject();
	    Assert.assertEquals(response.get("errorCode").getAsInt(),ERROR_CODE_BAD_PARAM);
	}
	
	/** search manufacturer by name **/
	public void serach(){
		WebResource rs = client.resource(WSROOT + "/search");
		//test case: return successful
		//define parameters
		String name = "manu";
		rs.queryParam("name", name);
		//get response
		JsonObject response = jsonParser.parse(
				    rs.get(String.class)
				).getAsJsonObject();
		Assert.assertNotNull(response.get(""));
		
	}
}

