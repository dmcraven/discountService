package com.jltest.model;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;


@JsonInclude(Include.NON_EMPTY)
@JsonIgnoreProperties(value= {"priceReduction"})
public class Product implements Comparable<Product> {

    private String productId;
    private String title;
    private List<ColorSwatch> colorSwatches;
    private String nowPrice;
    private String priceLabel;
    private double priceReduction;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ColorSwatch> getColorSwatches() {
        return colorSwatches;
    }

    public void setColorSwatches(List<ColorSwatch> colorSwatches) {
        this.colorSwatches = colorSwatches;
    }

    public String getNowPrice() {
        return nowPrice;
    }

    public void setNowPrice(String nowPrice) {
        this.nowPrice = nowPrice;
    }

    public String getPriceLabel() {
        return priceLabel;
    }

    public void setPriceLabel(String priceLabel) {
        this.priceLabel = priceLabel;
    }

	public double getPriceReduction() {
		return priceReduction;
	}

	public void setPriceReduction(double priceReduction) {
		this.priceReduction = priceReduction;
	}

	@Override
	public int compareTo(Product anotherProduct) {
		if(getPriceReduction() < anotherProduct.getPriceReduction()) return 1;
		if(getPriceReduction() > anotherProduct.getPriceReduction()) return -1;
		return 0;
	}
    
}
