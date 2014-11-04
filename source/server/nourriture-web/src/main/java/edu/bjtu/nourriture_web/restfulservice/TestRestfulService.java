package edu.bjtu.nourriture_web.restfulservice;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import com.google.gson.JsonObject;

@Path("test")
public class TestRestfulService {
	@GET
	@Path("{name}/{id}")
    public String sayHello(@PathParam("name")String name) {
		System.out.println("sayHello()");
		JsonObject jObj = new JsonObject();
		JsonObject jUser = new JsonObject();
		jObj.add("user", jUser);
        return jObj.toString();
    }
	
	@GET
	@Path("{name}/id")
    public String sayHello2(@PathParam("name")String name) {
		System.out.println("sayHello2()");
		System.out.println(name);
		JsonObject jObj = new JsonObject();
		JsonObject jUser = new JsonObject();
		jObj.add("user", jUser);
        return jObj.toString();
    }
}
