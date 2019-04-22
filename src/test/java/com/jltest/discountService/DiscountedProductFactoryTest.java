package com.jltest.discountService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.jltest.model.ColorSwatch;
import com.jltest.model.Product;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class DiscountedProductFactoryTest {

	
	private JSONObject priceJson;
	private JSONObject priceJsonWithNoWasPrice;
	private JSONObject rangePrice;
	private JSONObject priceJsonWithRangePrice;
	private JSONObject priceJsonWith2Thens;
	private JSONObject productJson;
	private JSONArray colorSwatches;
	
	private DiscountedProductFactory discountedProductFactory;
	
	@Mock 
	private ColorSwatchFactory colorSwatchFactoryMock;

	
	
	@BeforeEach
	void setUp()  {
		discountedProductFactory = spy(new DiscountedProductFactory());
		priceJson = new JSONObject();
		priceJson.put("was", "100");
		priceJson.put("then1", "80");
		priceJson.put("then2", "");
		priceJson.put("now", "60");
		priceJson.put("currency", "GBP");
		
		priceJsonWithNoWasPrice = new JSONObject();
		priceJsonWithNoWasPrice.put("was", "");
		priceJsonWithNoWasPrice.put("then1", "");
		priceJsonWithNoWasPrice.put("then2", "");
		priceJsonWithNoWasPrice.put("now", "60");
		priceJsonWithNoWasPrice.put("currency", "GBP");
		
		
		rangePrice = new JSONObject();
		rangePrice.put("from", "60");
		rangePrice.put("to", "80");
		
		priceJsonWithRangePrice = new JSONObject();
		priceJsonWithRangePrice.put("was", "100");
		priceJsonWithRangePrice.put("then1", "");
		priceJsonWithRangePrice.put("then2", "");
		priceJsonWithRangePrice.put("now", rangePrice);
		priceJsonWithRangePrice.put("currency", "GBP");
		
		
		
		priceJsonWith2Thens = new JSONObject();
		priceJsonWith2Thens.put("was", "100");
		priceJsonWith2Thens.put("then1", "80");
		priceJsonWith2Thens.put("then2", "75");
		priceJsonWith2Thens.put("now", "60");
		priceJsonWith2Thens.put("currency", "GBP");
		
		productJson = new JSONObject();
		productJson.put("title", "correctProduct");
		productJson.put("productId", "correctProductId");
		productJson.put("colorSwatches", new JSONArray());
	}
	

	@Test
	public void ignoreNonDiscountedProduct() {
		productJson.put("price",priceJsonWithNoWasPrice);
		Product returnedProduct = discountedProductFactory.generateDiscountedProductFromJSON(productJson, null);
		assertTrue(returnedProduct==null);
	}
	
	
	@Test
	public void generateADiscountedProduct() {
		productJson.put("price",priceJson);
		doReturn(colorSwatchFactoryMock).when(discountedProductFactory).getColorSwatchFactory();
		when(colorSwatchFactoryMock.generateColorSwatches(any(JSONArray.class))).thenReturn(new ArrayList<ColorSwatch>());
		Product returnedProduct = discountedProductFactory.generateDiscountedProductFromJSON(productJson, null);
		
		assertTrue(returnedProduct.getTitle().equalsIgnoreCase("correctProduct"));
		assertTrue(returnedProduct.getNowPrice().equalsIgnoreCase("£60"));
		assertTrue(returnedProduct.getPriceLabel().equalsIgnoreCase("Was £100, now £60"));
		assertTrue(returnedProduct.getPriceReduction()==40);
		assertTrue(returnedProduct.getProductId().equalsIgnoreCase("correctProductId"));
	}
	
	@Test
	public void generateADiscountedProductWithRangePrice() {
		productJson.put("price",priceJsonWithRangePrice);
		doReturn(colorSwatchFactoryMock).when(discountedProductFactory).getColorSwatchFactory();
		when(colorSwatchFactoryMock.generateColorSwatches(any(JSONArray.class))).thenReturn(new ArrayList<ColorSwatch>());
		Product returnedProduct = discountedProductFactory.generateDiscountedProductFromJSON(productJson, null);
		
		assertTrue(returnedProduct.getTitle().equalsIgnoreCase("correctProduct"));
		assertTrue(returnedProduct.getNowPrice().equalsIgnoreCase("From £60 to £80"));
		assertTrue(returnedProduct.getPriceLabel().equalsIgnoreCase("Was £100, now £80"));
		assertTrue(returnedProduct.getPriceReduction()==20);
		assertTrue(returnedProduct.getProductId().equalsIgnoreCase("correctProductId"));
	}
	
	@Test
	public void priceStringForPriceOver10Exact( ) {
		String priceString = discountedProductFactory.getPriceString(15, "GBP");
		assertTrue(priceString.equalsIgnoreCase("£15"));
	}

	@Test
	public void priceStringForPriceUnder10Exact( ) {
		String priceString = discountedProductFactory.getPriceString(9, "GBP");
		assertTrue(priceString.equalsIgnoreCase("£9.00"));
	}
	
	@Test
	public void priceStringForPriceOver10WithPence( ) {
		String priceString = discountedProductFactory.getPriceString(33.3333, "GBP");
		assertTrue(priceString.equalsIgnoreCase("£33.33"));
	}
	
	@Test
	public void priceStringForAlternateCurrency( ) {
		String priceString = discountedProductFactory.getPriceString(15, "EUR");
		assertTrue(priceString.equalsIgnoreCase("15EUR"));
	}
	
	@Test
	public void priceLabelForShowWasThenNow( ) {
		String priceLabel = discountedProductFactory.getPriceLabel(priceJson, 60, "ShowWasThenNow");
		assertTrue(priceLabel.equalsIgnoreCase("Was £100, then £80, now £60"));
	}
	
	@Test
	public void priceLabelForShowWasThenNowWithTwoThens( ) {
		String priceLabel = discountedProductFactory.getPriceLabel(priceJsonWith2Thens, 60, "ShowWasThenNow");
		assertTrue(priceLabel.equalsIgnoreCase("Was £100, then £75, now £60"));
	}
	
	@Test
	public void priceLabelForShowWasNow( ) {
		String priceLabel = discountedProductFactory.getPriceLabel(priceJson, 60, "ShowWasNow");
		assertTrue(priceLabel.equalsIgnoreCase("Was £100, now £60"));
	}
	
	@Test
	public void priceLabelForShowPercDscount( ) {
		String priceLabel = discountedProductFactory.getPriceLabel(priceJson, 60, "ShowPercDscount");
		assertTrue(priceLabel.equalsIgnoreCase("40% off - now £60"));
	}
	
}
