package edu.bjtu.nourriture_web.restfulservice;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.aspectj.util.FileUtil;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sun.jersey.core.header.FormDataContentDisposition;

import edu.bjtu.nourriture_web.common.RestfulServiceUtil;

@Path("/")
@Produces("application/json")
public class CommonRestfulService {
	/**
	 * return all services under root path
	 */
	@GET
	public String root(){
		JsonObject ret = new JsonObject();
		JsonArray links = new JsonArray();
	
		RestfulServiceUtil.addChildrenLinks(links, "add customer", "/customer", "POST");
		RestfulServiceUtil.addChildrenLinks(links, "add manuFacturer", "/manuFacturer", "POST");
		RestfulServiceUtil.addChildrenLinks(links, "get admin info", "/admin", "GET");
		RestfulServiceUtil.addChildrenLinks(links, "add foodCategory", "/foodCategory", "POST");
		RestfulServiceUtil.addChildrenLinks(links, "add flavour", "/flavour", "POST");
		RestfulServiceUtil.addChildrenLinks(links, "add region", "/region", "region");
		RestfulServiceUtil.addChildrenLinks(links, "add recipeCategory", "/recipeCategory", "POST");
		RestfulServiceUtil.addChildrenLinks(links, "add location", "/location", "POST");
		RestfulServiceUtil.addChildrenLinks(links, "add food", "/food", "POST");
		RestfulServiceUtil.addChildrenLinks(links, "add cookingStep", "/cookingStep", "POST");
		RestfulServiceUtil.addChildrenLinks(links, "add recipe", "/recipe", "POST");
		RestfulServiceUtil.addChildrenLinks(links, "add comments", "/comments", "POST");
		
		ret.add("links", links);
		
		return ret.toString();
	}
}
