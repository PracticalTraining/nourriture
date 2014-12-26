package edu.bjtu.nourriture_web.restfulservice;

import java.util.UUID;

import javax.ws.rs.core.MultivaluedMap;

import junit.framework.Assert;

import org.junit.Test;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

/**
 * test restful service about customer
 * 
 * @author qstar
 *
 */
public class CustomerRSTestCase {
//	/** root path for services about manufacturer **/
//	private final String WSROOT = "http://123.57.36.82/nourriture/ws/customer";
//	/** web service test client **/
//	private final Client client = Client.create();
//	/** parse string to json **/
//	private final JsonParser jsonParser = new JsonParser();
//
//	/** add customer **/
//	@Test
//	public void addCustomer() {
//		WebResource rs = client.resource(WSROOT);
//		// test case: return success
//		// define parameters
//		String name = UUID.randomUUID().toString();
//		String password = "myCustomer";
//		int sex = 0;
//		int age = 22;
//		MultivaluedMap<String, String> params = new MultivaluedMapImpl();
//		params.add("name", name);
//		params.add("password", password);
//		params.add("sex", String.valueOf(sex));
//		params.add("age", String.valueOf(age));
//		// get response
//		JsonObject response = jsonParser.parse(rs.post(String.class, params))
//				.getAsJsonObject();
//		Assert.assertNotNull(response.get("id"));
//
//		// test case: return with error ERROR_CODE_USER_EXSIT
//		// define error code
//		final int ERROR_CODE_USER_EXSIT = -1;
//		// get response
//		response = jsonParser.parse(rs.post(String.class, params))
//				.getAsJsonObject();
//		Assert.assertEquals(response.get("errorCode").getAsInt(),
//				ERROR_CODE_USER_EXSIT);
//
//		// test case: return with error ERROR_CODE_BAD_PARAM
//		// define error code
//		final int ERROR_CODE_BAD_PARAM = -2;
//		// define parameters
//		params = new MultivaluedMapImpl();
//		params.add("name", null);
//		params.add("password", password);
//		params.add("sex", String.valueOf(sex));
//		params.add("age", String.valueOf(age));
//		// get response
//		response = jsonParser.parse(rs.post(String.class, params))
//				.getAsJsonObject();
//		Assert.assertEquals(response.get("errorCode").getAsInt(),
//				ERROR_CODE_BAD_PARAM);
//	}
//
//	/** search customer by name **/
//	@Test
//	public void searchByCustomerName() {
//		WebResource rs = client.resource(WSROOT + "/search");
//		// test case: return success
//		// define parameters
//		String customerName = "Cust";
//		// get response
//		JsonObject response = jsonParser.parse(
//				rs.queryParam("name", customerName).get(String.class))
//				.getAsJsonObject();
//		Assert.assertNotNull(response.get("customers"));
//
//		// test case: return with error ERROR_CODE_NO_RESULT
//		final int ERROR_CODE_NO_RESULT = -1;
//		// define parameters
//		customerName = UUID.randomUUID().toString();
//		// get response
//		response = jsonParser.parse(
//				rs.queryParam("name", customerName).get(String.class))
//				.getAsJsonObject();
//		Assert.assertEquals(response.get("errorCode").getAsInt(),
//				ERROR_CODE_NO_RESULT);
//
//		// test case: return with error ERROR_CODE_NO_RESULT
//		final int ERROR_CODE_BAD_PARAM = -2;
//		// get response
//		response = jsonParser.parse(rs.get(String.class)).getAsJsonObject();
//		Assert.assertEquals(response.get("errorCode").getAsInt(),
//				ERROR_CODE_BAD_PARAM);
//	}
//
//	/** login **/
//	@Test
//	public void login() {
//		WebResource rs = client.resource(WSROOT + "/login");
//		// test case: return successful
//		// define parameters
//		String name = "myCustomer";
//		String password = "1234567";
//		// get response
//		JsonObject response = jsonParser.parse(
//				rs.queryParam("name", name).queryParam("password", password)
//						.get(String.class)).getAsJsonObject();
//		Assert.assertNotNull(response.get("id"));
//
//		// test case: return with error ERROR_CODE_NO_RESULT
//		final int ERROR_CODE_NO_USER = -1;
//		// define parameters
//		name = "myCustomer1";
//		password = "1234567";
//		// get response
//		response = jsonParser.parse(
//				rs.queryParam("name", name).queryParam("password", password)
//						.get(String.class)).getAsJsonObject();
//		Assert.assertEquals(response.get("errorCode").getAsInt(),
//				ERROR_CODE_NO_USER);
//
//		// test case: return with error ERROR_CODE_NOT_VALIDATED
//		final int ERROR_CODE_NOT_VALIDATED = -2;
//		// define parameters
//		name = "myCustomer";
//		password = "12345678";
//		// get response
//		response = jsonParser.parse(
//				rs.queryParam("name", name).queryParam("password", password)
//						.get(String.class)).getAsJsonObject();
//		Assert.assertEquals(response.get("errorCode").getAsInt(),
//				ERROR_CODE_NOT_VALIDATED);
//
//		// test case: return with error ERROR_CODE_NOT_VALIDATED
//		final int ERROR_CODE_BAD_PARAM = -3;
//		// get response
//		response = jsonParser.parse(rs.get(String.class)).getAsJsonObject();
//		Assert.assertEquals(response.get("errorCode").getAsInt(),
//				ERROR_CODE_BAD_PARAM);
//	}
//
//	/** get detail information by id */
//	@Test
//	public void getById() {
//		WebResource rs = client.resource(WSROOT + "/1");
//
//		// test case: return success
//		// get response
//		JsonObject response = jsonParser.parse(rs.get(String.class))
//				.getAsJsonObject();
//		Assert.assertNotNull(response.get("customer"));
//
//		// test case: no result
//		// get response
//		rs = client.resource(WSROOT + "/1000");
//		response = jsonParser.parse(rs.get(String.class)).getAsJsonObject();
//		Assert.assertNull(response.get("customer"));
//	}
//
//	/** update information by id **/
//	@Test
//	public void updateById() {
//		WebResource rs = client.resource(WSROOT + "/2");
//
//		// test case: return success
//		// get response
//		JsonObject response = jsonParser.parse(rs.get(String.class))
//				.getAsJsonObject();
//		// define parameters
//		int age = 22;
//		MultivaluedMap<String, String> params = new MultivaluedMapImpl();
//		params.add("age", String.valueOf(age));
//		// get response
//		response = jsonParser.parse(rs.put(String.class, params))
//				.getAsJsonObject();
//		Assert.assertEquals(response.get("result").getAsInt(), 0);
//
//		// test case: return with error ERROR_CODE_USER_NOT_EXSIT
//		final int ERROR_CODE_USER_NOT_EXSIT = -1;
//		rs = client.resource(WSROOT + "/20000");
//		// get response
//		response = jsonParser.parse(rs.get(String.class)).getAsJsonObject();
//		// define parameters
//		age = 22;
//		params = new MultivaluedMapImpl();
//		params.add("age", String.valueOf(age));
//		// get response
//		response = jsonParser.parse(rs.put(String.class, params))
//				.getAsJsonObject();
//		Assert.assertEquals(response.get("errorCode").getAsInt(),
//				ERROR_CODE_USER_NOT_EXSIT);
//
//		// test case: return with error ERROR_CODE_BAD_PARAM
//		final int ERROR_CODE_BAD_PARAM = -2;
//		// get response
//		response = jsonParser.parse(rs.get(String.class)).getAsJsonObject();
//		// get response
//		response = jsonParser.parse(rs.put(String.class)).getAsJsonObject();
//		Assert.assertEquals(response.get("errorCode").getAsInt(),
//				ERROR_CODE_BAD_PARAM);
//	}
//
//	/** get interests **/
//	@Test
//	public void getInterests() {
////		WebResource rs = client.resource(WSROOT + "/2/sweet");
////		// test case: return success
////		// define parameters
////		JsonObject response = jsonParser.parse(rs.get(String.class))
////				.getAsJsonObject();
////		Assert.assertNotNull(response.get("foodCategorys"));
////
////		// test case: return with error ERROR_CODE_USER_NOT_EXSIT
////		final int ERROR_CODE_USER_NOT_EXIST = -1;
////		rs = client.resource(WSROOT + "/23/sweet");
////		response = jsonParser.parse(rs.get(String.class)).getAsJsonObject();
////		Assert.assertEquals(response.get("errorCode").getAsInt(),
////				ERROR_CODE_USER_NOT_EXIST);
////
////		// test case: return with error ERROR_CODE_INTEREST_NOT_SET
////		final int ERROR_CODE_INTEREST_NOT_SET = -2;
////		rs = client.resource(WSROOT + "/23/");
////		response = jsonParser.parse(rs.get(String.class)).getAsJsonObject();
////		Assert.assertEquals(response.get("errorCode").getAsInt(),
////				ERROR_CODE_INTEREST_NOT_SET);
////
////		// test case: return with error ERROR_CODE_INTEREST_NOT_SET
////		final int ERROR_CODE_BAD_PARAM = -3;
////		response = jsonParser.parse(rs.get(String.class)).getAsJsonObject();
////		Assert.assertEquals(response.get("errorCode").getAsInt(),
////				ERROR_CODE_BAD_PARAM);
//	}

}
