package com.jltest.discountService;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.ResourceConfig;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jltest.model.Product;

@Path("jltest")
public class DiscountedProductService extends ResourceConfig {
	private static final String JL_URL = "https://jl-nonprod-syst.apigee.net/v1/categories/600001506/products?key=2ALHCAAs6ikGRBoy6eTHA58RaG097Fma";
	DiscountedProductFactory factory = new DiscountedProductFactory();
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response returnDiscountedProducts() throws JSONException
	{
		return returnDiscountedProducts(null);
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{labelType}")
	public Response returnDiscountedProducts(@PathParam("labelType") String labelType) throws JSONException 
	{
		
		List<Product> listOfProducts = new ArrayList<Product>();
		try
		{
			
			JSONObject root = getJsonObjectFromJL();
			JSONArray jsonProducts = (JSONArray)root.get("products");
			for (Iterator<?> jsonProduct = jsonProducts.iterator(); jsonProduct.hasNext();) {
				Product discountedProduct = getFactory().generateDiscountedProductFromJSON((JSONObject) jsonProduct.next(), labelType);
				if(discountedProduct!=null) {
					listOfProducts.add(discountedProduct);
				}
			}
			
			
		} catch (Exception e) {
			return Response.status(500).entity(e.getMessage()).build();
		}
		Collections.sort(listOfProducts);
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			String jsonOutput = mapper.writeValueAsString(listOfProducts);
			return Response.status(200).entity(jsonOutput).build();
		} catch (Exception e) {
			return Response.status(500).entity(e.getMessage()).build();
		}
	}

	protected JSONObject getJsonObjectFromJL() throws URISyntaxException,IOException {
		URI url = new URI(JL_URL);
		JSONTokener tokener = new JSONTokener(url.toURL().openStream());
		return new JSONObject(tokener);
	}

	public DiscountedProductFactory getFactory() {
		return factory;
	}

	public void setFactory(DiscountedProductFactory factory) {
		this.factory = factory;
	}

}
