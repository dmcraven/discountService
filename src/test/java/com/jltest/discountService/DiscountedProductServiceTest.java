package com.jltest.discountService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.internal.util.io.IOUtil;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skyscreamer.jsonassert.JSONAssert;

import com.jltest.model.Product;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class DiscountedProductServiceTest {

	private DiscountedProductService service;
	@Mock 
	private DiscountedProductFactory factoryMock;
	
	private JSONObject rootJsonObject;
	private JSONArray jsonProducts;
	private Product genericProduct;
	
	
	@BeforeEach
	public void setUp()  {
		service = spy(new DiscountedProductService());
		
		jsonProducts = new JSONArray();
		jsonProducts.put(new JSONObject());
		
		rootJsonObject = new JSONObject();
		rootJsonObject.put("products", jsonProducts);
		
		genericProduct = new Product();
		genericProduct.setTitle("correct");
		
		doReturn(factoryMock).when(service).getFactory();
		
	}
	
	@Test
	public void success_withNullLabelType() throws URISyntaxException, IOException{
		doReturn(rootJsonObject).when(service).getJsonObjectFromJL();
		when(factoryMock.generateDiscountedProductFromJSON(any(JSONObject.class), eq(null))).thenReturn(genericProduct);
		Response response = service.returnDiscountedProducts();
		String jsonResponse = (String)response.getEntity();
		
		assertTrue(response.getStatus()==200);
		assertTrue(jsonResponse.contains("correct"));		
	}
	
	@Test
	public void success_withDifferentLabelType() throws URISyntaxException, IOException{
		doReturn(rootJsonObject).when(service).getJsonObjectFromJL();
		when(factoryMock.generateDiscountedProductFromJSON(any(JSONObject.class), eq("ShowPerc"))).thenReturn(genericProduct);
		Response response = service.returnDiscountedProducts("ShowPerc");
		String jsonResponse = (String)response.getEntity();
		
		assertTrue(response.getStatus()==200);
		assertTrue(jsonResponse.contains("correct"));		
	}

	
}
