package com.jltest.discountService;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import com.jltest.model.ColorSwatch;
import com.jltest.discountService.ColorSwatchFactory;

public class ColorSwatchFactoryTest {

	private ColorSwatchFactory colorSwatchFactory;
	private JSONArray jsonColorSwatches;
	private JSONObject jsonColorSwatch;
	private JSONObject altJsonColorSwatch;
	
	@Before
	public void setup() {
		colorSwatchFactory = new ColorSwatchFactory();
		jsonColorSwatch = new JSONObject();
		jsonColorSwatch.put("color", "General Colour");
		jsonColorSwatch.put("basicColor", "White");
		jsonColorSwatch.put("skuId", "12131");
		
		altJsonColorSwatch = new JSONObject();
		altJsonColorSwatch.put("color", "Second Colour");
		altJsonColorSwatch.put("basicColor", "Black");
		altJsonColorSwatch.put("skuId", "31213");
		
		jsonColorSwatches = new JSONArray();
		jsonColorSwatches.put(jsonColorSwatch);
		
	}
	
	@Test
	public void checkRGBValuesMappedCorrectly() {
	
		Map<String, String> colorMap = colorSwatchFactory.getBasicColorToRGBMap();
		assertTrue(colorMap.get("Black").equalsIgnoreCase("000000"));
		assertTrue(colorMap.get("White").equalsIgnoreCase("FFFFFF"));
		assertTrue(colorMap.get("Red").equalsIgnoreCase("FF0000"));
		assertTrue(colorMap.get("Blue").equalsIgnoreCase("0000FF"));
		assertTrue(colorMap.get("Green").equalsIgnoreCase("008000"));
		assertTrue(colorMap.get("Grey").equalsIgnoreCase("808080"));
		assertTrue(colorMap.get("Pink").equalsIgnoreCase("FFC0CB"));
		assertTrue(colorMap.get("Orange").equalsIgnoreCase("FFA500"));
		assertTrue(colorMap.get("Light Blue").equalsIgnoreCase("ADD8E6"));
		assertTrue(colorMap.get("Purple").equalsIgnoreCase("800080"));
		assertTrue(colorMap.get("Yellow").equalsIgnoreCase("FFFF00"));
		
		assertNull(colorMap.get("Multi"));
		
	}
	
	@Test
	public void checkSingleObjectReturnedCorrectly() {
		List<ColorSwatch> returnedSwatches = colorSwatchFactory.generateColorSwatches(jsonColorSwatches);
		assertEquals(returnedSwatches.size(), 1);
		assertTrue(returnedSwatches.get(0).getSkuid().equals("12131"));
		assertTrue(returnedSwatches.get(0).getColor().equals("General Colour"));
		assertTrue(returnedSwatches.get(0).getRgbColor().equals("FFFFFF"));
	}
	
	@Test
	public void checkMultipleObjectReturnedCorrectly() {
		jsonColorSwatches.put(altJsonColorSwatch);
		List<ColorSwatch> returnedSwatches = colorSwatchFactory.generateColorSwatches(jsonColorSwatches);
		assertEquals(returnedSwatches.size(), 2);
		assertTrue(returnedSwatches.get(1).getSkuid().equals("31213"));
		assertTrue(returnedSwatches.get(1).getColor().equals("Second Colour"));
		assertTrue(returnedSwatches.get(1).getRgbColor().equals("000000"));
	}
	

}
