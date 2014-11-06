package edu.bjtu.nourriture_web.common;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class RestfulServiceUtil {
	/**
	 * add direct children links to srcLinks
	 * @param srcLinks a collection of direct children links
	 * @param description description of direct children link
	 * @param href url of direct children link
	 * @param method request url of direct children link
	 */
    public static void addChildrenLinks(JsonArray srcLinks,String description,String href,String method){
    	if(srcLinks != null){
    		JsonObject link = new JsonObject();
    		link.addProperty("description", description);
    		link.addProperty("href", href);
    		link.addProperty("method", method);
    		srcLinks.add(link);
    	}
    }
}
