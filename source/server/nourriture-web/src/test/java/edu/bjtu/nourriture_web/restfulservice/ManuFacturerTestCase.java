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
	
	/** search manufacturer by companyName **/
	@Test
	public void serachByCompanyName(){
		WebResource rs = client.resource(WSROOT + "/search");
		//test case: return successful
		//define parameters
		String companyName = "Manu";
		//get response
		JsonObject response = jsonParser.parse(
				rs.queryParam("companyName", companyName)
				.get(String.class)
				).getAsJsonObject();
		Assert.assertNotNull(response.get("manufacturers"));
		
		//test case: return with error ERROR_CODE_NO_RESULT
		final int ERROR_CODE_NO_RESULT = -1;
		//define parameters
		companyName = UUID.randomUUID().toString();
		//get response
		response = jsonParser.parse(
				rs.queryParam("companyName", companyName)
				.get(String.class)
				).getAsJsonObject();
		Assert.assertEquals(response.get("errorCode").getAsInt(),ERROR_CODE_NO_RESULT);
		
		//test case: return with error ERROR_CODE_NO_RESULT
		final int ERROR_CODE_BAD_PARAM = -2;
		//get response
		response = jsonParser.parse(
				rs.get(String.class)
				).getAsJsonObject();
		Assert.assertEquals(response.get("errorCode").getAsInt(),ERROR_CODE_BAD_PARAM);
	}
	
	/** login **/
	@Test
	public void login(){
		WebResource rs = client.resource(WSROOT + "/login");
		//test case: return successful
		//define parameters
		String name = "myManuFacturer";
		String password = "1234567";
		//get response
		JsonObject response = jsonParser.parse(
							rs.queryParam("name", name).queryParam("password", password)
							.get(String.class)
						).getAsJsonObject();
		Assert.assertNotNull(response.get("id"));
		
		//test case: return with error ERROR_CODE_NO_RESULT
		final int ERROR_CODE_NO_USER = -1;
		//define parameters
		name = "myManuFacturer1";
		password = "1234567";
		//get response
		response = jsonParser.parse(
					rs.queryParam("name", name).queryParam("password", password)
					.get(String.class)
			).getAsJsonObject();
		Assert.assertEquals(response.get("errorCode").getAsInt(),ERROR_CODE_NO_USER);
		
		//test case: return with error ERROR_CODE_NOT_VALIDATED
		final int ERROR_CODE_NOT_VALIDATED = -2;
		//define parameters
		name = "myManuFacturer";
		password = "12345678";
		//get response
		response = jsonParser.parse(
					rs.queryParam("name", name).queryParam("password", password)
					.get(String.class)
			).getAsJsonObject();
		Assert.assertEquals(response.get("errorCode").getAsInt(),ERROR_CODE_NOT_VALIDATED);
		
		//test case: return with error ERROR_CODE_NOT_VALIDATED
		final int ERROR_CODE_BAD_PARAM = -3;
		//get response
		response = jsonParser.parse(
				rs.get(String.class)
		).getAsJsonObject();
		Assert.assertEquals(response.get("errorCode").getAsInt(),ERROR_CODE_BAD_PARAM);
	}
	
	/** get detail information by id */
	@Test
	public void getById(){
		WebResource rs = client.resource(WSROOT + "/1");
	
		//test case: return successful
		//get response
		JsonObject response = jsonParser.parse(
							rs.get(String.class)
						).getAsJsonObject();
		Assert.assertNotNull(response.get("manuFacturer"));
		
		//test case: no result
		//get response
		rs = client.resource(WSROOT + "/1000");
		response = jsonParser.parse(
							rs.get(String.class)
						).getAsJsonObject();
		Assert.assertNull(response.get("manuFacturer"));
	}
	
	/** update information by id **/
	@Test
	public void updateById(){
		WebResource rs = client.resource(WSROOT + "/2");
		
		//test case: return successful
		//get response
		JsonObject response = jsonParser.parse(
							rs.get(String.class)
						).getAsJsonObject();
		//define parameters
		String companyName = "bjtu" + UUID.randomUUID();
		MultivaluedMap<String, String> params = new MultivaluedMapImpl();
		params.add("companyName", companyName);
		//get response
		response = jsonParser.parse(
							rs.put(String.class,params)
						).getAsJsonObject();
		Assert.assertEquals(response.get("result").getAsInt(),0);
		
		//test case: return with error ERROR_CODE_USER_NOT_EXSIT
		final int ERROR_CODE_USER_NOT_EXSIT = -1;
		rs = client.resource(WSROOT + "/20000");
		//get response
		response = jsonParser.parse(
							rs.get(String.class)
						).getAsJsonObject();
		//define parameters
		companyName = "bjtu" + UUID.randomUUID();
		params = new MultivaluedMapImpl();
		params.add("companyName", companyName);
		//get response
		response = jsonParser.parse(
							rs.put(String.class,params)
						).getAsJsonObject();
		Assert.assertEquals(response.get("errorCode").getAsInt(),ERROR_CODE_USER_NOT_EXSIT);
		
		//test case: return with error ERROR_CODE_BAD_PARAM
		final int ERROR_CODE_BAD_PARAM = -2;
		//get response
		response = jsonParser.parse(
							rs.get(String.class)
						).getAsJsonObject();
		//get response
		response = jsonParser.parse(
							rs.put(String.class)
						).getAsJsonObject();
		Assert.assertEquals(response.get("errorCode").getAsInt(),ERROR_CODE_BAD_PARAM);
	}
	
	/** getFoodCount **/
	@Test
	public void getFoodCount(){
		WebResource rs = client.resource(WSROOT + "/2/foodCount");
		//test case: return successful
		//get response
		JsonObject response = jsonParser.parse(
							rs.get(String.class)
						).getAsJsonObject();
		Assert.assertNotNull(response.get("foodCount"));
		
		//test case: return with error ERROR_CODE_USER_NOT_EXSIT
		final int ERROR_CODE_USER_NOT_EXSIT = -1;
		rs = client.resource(WSROOT + "/20000/foodCount");
		response = jsonParser.parse(
				rs.get(String.class)
			).getAsJsonObject();
		Assert.assertEquals(response.get("errorCode"),ERROR_CODE_USER_NOT_EXSIT);
	}
	
	/** get score **/
	@Test
	public void getSore(){
		WebResource rs = client.resource(WSROOT + "/2/score");
		//test case: return successful
		//get response
		JsonObject response = jsonParser.parse(
							rs.get(String.class)
						).getAsJsonObject();
		Assert.assertNotNull(response.get("sum"));
		
		//test case: return with error ERROR_CODE_USER_NOT_EXSIT
		final int ERROR_CODE_USER_NOT_EXSIT = -1;
		rs = client.resource(WSROOT + "/20000/score");
		response = jsonParser.parse(
				rs.get(String.class)
			).getAsJsonObject();
		Assert.assertEquals(response.get("errorCode"),ERROR_CODE_USER_NOT_EXSIT);
	}
}

