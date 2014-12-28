package edu.bjtu.nourriture_web.restfulservice;

import org.junit.Assert;
import org.junit.Test;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

/**
 * test common restful service
 * @author HJW
 *
 */
public class CommonRSTestCase {
//	/** root path for services about manufacturer **/
//	private final String WSROOT = "http://123.57.36.82/nourriture/ws/";
//	/** web service test client **/
//	private final Client client = Client.create();
//	/** parse string to json **/
//	private final JsonParser jsonParser = new JsonParser();
//	
//	/** test root restful servie **/
//	@Test
//	public void root(){
//		WebResource rs = client.resource(WSROOT);
//		//get response
//		JsonObject response = jsonParser.parse(
//				    rs.get(String.class)
//				).getAsJsonObject();
//		Assert.assertNotNull(response.get("links"));
//	}
}
