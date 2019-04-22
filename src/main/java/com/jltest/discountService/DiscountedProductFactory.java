package com.jltest.discountService;

import org.json.JSONArray;
import org.json.JSONObject;

import com.jltest.model.Product;

public class DiscountedProductFactory {

	ColorSwatchFactory colorSwatchFactory = new ColorSwatchFactory();
	
	public Product generateDiscountedProductFromJSON(JSONObject jsonProduct, String priceLabelType) {
		Product product = new Product();
		JSONObject price = (JSONObject)jsonProduct.get("price");
		double nowPriceValue;
		double wasPriceValue = price.getString("was").isEmpty() ? 0 : price.getDouble("was");
		if(wasPriceValue == 0) {
			return null;
		}
		String currency = price.getString("currency");
		
		/* 
		 * In the situation that we have a "from x to y" price value coming back for now prices
		 * use higher "maximum" price so that the text is not misleading. Will need to confirm if
		 * this is the best way to proceed, or if we should have different text such as
		 * "save up to ... " 
		 */
		if (containsLetters(price.get("now").toString())) {
			JSONObject nowPriceRange = price.getJSONObject("now");
			double nowPriceFrom = nowPriceRange.getDouble("from");
			double nowPriceTo = nowPriceRange.getDouble("to");
			nowPriceValue = nowPriceTo; //Can change per comment above
			product.setNowPrice("From "+getPriceString(nowPriceFrom, currency)+" to "+getPriceString(nowPriceTo, currency));
		} else {
			nowPriceValue=price.getDouble("now");
			product.setNowPrice(getPriceString(nowPriceValue, currency));
		}
		product.setProductId((String)jsonProduct.get("productId"));
		product.setTitle((String)jsonProduct.get("title"));
		product.setPriceReduction(wasPriceValue-nowPriceValue);
		product.setPriceLabel(getPriceLabel(price,nowPriceValue,priceLabelType));
		
		JSONArray jsonColorSwatches = (JSONArray)jsonProduct.get("colorSwatches");
		product.setColorSwatches(getColorSwatchFactory().generateColorSwatches(jsonColorSwatches));
		
		return product;
	}

	private boolean containsLetters(String string) {
		if(string.chars().anyMatch(Character::isLetter)) {
			return true;
		}else{
			return false;
		}
	}

	protected String getPriceString(double price, String currency) {
		StringBuffer priceString = new StringBuffer();
		if(currency.equals("GBP")) {
			priceString.append("£");
		}
		if(price < 10 || price != Math.floor(price)) {
			priceString.append(String.format("%.2f", price));
		}else {
			priceString.append(String.format("%.0f", price));
		}
		
		if(!currency.equals("GBP")) {
			priceString.append(currency);
		}
		return priceString.toString();
		
	}
	
	protected String getPriceLabel(JSONObject price, double nowPriceValue, String priceLabelType) {
		double wasPriceValue = price.getString("was").isEmpty() ? 0 : price.getDouble("was");
		double then1PriceValue = price.getString("then1").isEmpty() ? 0 : price.getDouble("then1");
		double then2PriceValue = price.getString("then2").isEmpty() ? 0 : price.getDouble("then2");
		String currency = price.getString("currency");
		if(priceLabelType!=null&&priceLabelType.equals("ShowWasThenNow")&&then1PriceValue+then2PriceValue>0) {
			if(then2PriceValue>0) {
				return "Was "+getPriceString(wasPriceValue, currency)+", then "+getPriceString(then2PriceValue, currency)+", now "+getPriceString(nowPriceValue, currency);
			} else {
				return "Was "+getPriceString(wasPriceValue, currency)+", then "+getPriceString(then1PriceValue, currency)+", now "+getPriceString(nowPriceValue, currency);
			}
		} else if(priceLabelType!=null&&priceLabelType.equals("ShowPercDscount")) {
			double percentDiscount = (wasPriceValue - nowPriceValue)*100 / wasPriceValue;
			return String.format("%.0f", percentDiscount)+"% off - now "+getPriceString(nowPriceValue, currency);
		} else {
			return "Was "+getPriceString(wasPriceValue, currency)+", now "+getPriceString(nowPriceValue, currency);
		}
	}

	public ColorSwatchFactory getColorSwatchFactory() {
		return colorSwatchFactory;
	}

	public void setColorSwatchFactory(ColorSwatchFactory colorSwatchFactory) {
		this.colorSwatchFactory = colorSwatchFactory;
	}
	
}
