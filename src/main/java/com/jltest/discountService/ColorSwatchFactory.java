package com.jltest.discountService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.jltest.model.ColorSwatch;

public class ColorSwatchFactory {

	Map<String, String> basicColorToRGBMap;

	public ColorSwatchFactory() {
		super();
		basicColorToRGBMap = new HashMap<String, String>();
		basicColorToRGBMap.put("Black", "000000");
		basicColorToRGBMap.put("White", "FFFFFF");
		basicColorToRGBMap.put("Red", "FF0000");
		basicColorToRGBMap.put("Blue", "0000FF");
		basicColorToRGBMap.put("Green", "008000");
		basicColorToRGBMap.put("Grey", "808080");
		basicColorToRGBMap.put("Pink", "FFC0CB");
		basicColorToRGBMap.put("Orange", "FFA500");
		basicColorToRGBMap.put("Light Blue", "ADD8E6");
		basicColorToRGBMap.put("Purple", "800080");
		basicColorToRGBMap.put("Yellow", "FFFF00"); //TODO: Query "Multi" - for now, leave blank
	}

	public List<ColorSwatch> generateColorSwatches(JSONArray jsonColorSwatches) {
		List<ColorSwatch> colorSwatchList = new ArrayList<>();
		
		for (Iterator<?> swatch = jsonColorSwatches.iterator(); swatch.hasNext();) {
			colorSwatchList.add(generateColorSwatch((JSONObject) swatch.next()));
		}
		
		return colorSwatchList;
	}

	private ColorSwatch generateColorSwatch(JSONObject jsonColorSwatch) {
		ColorSwatch swatch = new ColorSwatch();
		swatch.setColor((String)jsonColorSwatch.get("color"));
		swatch.setSkuid((String)jsonColorSwatch.get("skuId"));
		swatch.setRgbColor(basicColorToRGBMap.get(jsonColorSwatch.get("basicColor")));
		if(swatch.getRgbColor()==null) {
			swatch.setRgbColor("");
		}
		return swatch;
	}

	public Map<String, String> getBasicColorToRGBMap() {
		return basicColorToRGBMap;
	}
}
