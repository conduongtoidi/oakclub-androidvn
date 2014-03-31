package com.oakclub.android.util;

import com.oakclub.android.model.SkuDetails;

public class CurrencyFormatter {
	//Fuck google
	public static SkuDetails parse(SkuDetails product) {
		SkuDetails res = product;
		if(product == null){
			return product;
		}
		String moneyString = product.getPrice();
		String currencyUnit = "";
		String price = "";
		for (char c : moneyString.toCharArray()) {
			if( (c >= '0' && c <= '9') || (c == ',') || (c == '.')){
				price = price + c;
			}else if( (int)c != 160 && !Character.isWhitespace(c) && c != ' '){
				currencyUnit = currencyUnit + c;
			}
		}
		currencyUnit = currencyUnit.trim();
		String tmp = "";
		if(currencyUnit.equalsIgnoreCase("$") || currencyUnit.equalsIgnoreCase("¢") || currencyUnit.equalsIgnoreCase("฿") || currencyUnit.equalsIgnoreCase("£")){
			//Australia, Singapore, Thailand, United Kingdom, United States, Ghana,
			for (char c : price.toCharArray()) {
				if( c != ','){
					tmp = tmp + c;
				}
			}
		}else if(currencyUnit.equalsIgnoreCase("₫")){
			//VND
			for (char c : price.toCharArray()) {
				if( c != '.' && c != ','){
					tmp = tmp + c;
				}
			}
		}else if(currencyUnit.equalsIgnoreCase("¥") || currencyUnit.equalsIgnoreCase("₩")){
			// Japan, South Korea
			for (char c : price.toCharArray()) {
				if( c != '.' && c != ','){
					tmp = tmp + c;
				}
			}
		}else if(currencyUnit.equalsIgnoreCase("Rp")){
			// Indonesia
			for (char c : price.toCharArray()) {
				if( c != '.' && c != ','){
					tmp = tmp + c;
				}else if( c == ','){
					tmp = tmp + '.';
				}
			}
		}
		if(!tmp.equals("")){
				price = tmp;
		}else{
				price = "";
				currencyUnit = "";
			}
		res.setCurrencySymple(currencyUnit);
		res.setPriceInNumberString(price);
		return res;
	}
}
