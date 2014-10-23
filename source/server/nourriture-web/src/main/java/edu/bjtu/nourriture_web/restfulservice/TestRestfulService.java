package edu.bjtu.nourriture_web.restfulservice;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import com.google.gson.JsonObject;

@Path("test")
public class TestRestfulService {
	@GET
    public String sayHello() {
		JsonObject jObj = new JsonObject();
		JsonObject jUser = new JsonObject();
		jObj.add("user", jUser);
        return jObj.toString();

    }
}
