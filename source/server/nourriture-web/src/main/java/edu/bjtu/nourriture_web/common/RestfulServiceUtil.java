package edu.bjtu.nourriture_web.common;

import java.util.ArrayList;
import java.util.List;

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
    
    /**
     * we part multible ids using ',',this is a method to delete a exsit id from multiple ids
     * @param deleteId the id been deleted
     * @param srcStr a string contains multible ids using ',' to part every id
     * @return a string not contains deleteId
     */
    public static String deleteIdFromIdList(int deleteId,String srcStr){
    	List<String> idList = new ArrayList<String>();
    	StringBuffer sb = new StringBuffer();
    	for(String id : srcStr.split(",")){
    		if(!id.equals(String.valueOf(deleteId)))
    		{
    			sb.append(id);
    			sb.append(",");
    		}
    	}
    	if(sb.length() > 1)
    		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
    }
}
