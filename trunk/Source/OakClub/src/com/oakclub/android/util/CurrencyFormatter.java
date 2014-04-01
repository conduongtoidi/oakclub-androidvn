package com.oakclub.android.util;

import com.oakclub.android.model.SkuDetails;

public class CurrencyFormatter {
	// Fuck google
	public static SkuDetails parse(SkuDetails product) {
		SkuDetails res = product;
		if (product == null) {
			return product;
		}
		String moneyString = product.getPrice();
		String currencyUnit = "";
		String price = "";
		for (char c : moneyString.toCharArray()) {
			if ((c >= '0' && c <= '9') || (c == ',') || (c == '.')) {
				price = price + c;
			} else if ((int) c != 160 && !Character.isWhitespace(c) && c != ' ') {
				currencyUnit = currencyUnit + c;
			}
		}
		currencyUnit = currencyUnit.trim();
		String tmp = "";
		if (currencyUnit.equalsIgnoreCase("$")
				|| currencyUnit.equalsIgnoreCase("¢")
				|| currencyUnit.equalsIgnoreCase("฿")
				|| currencyUnit.equalsIgnoreCase("£")
				|| currencyUnit.equalsIgnoreCase("RM")
				|| currencyUnit.equalsIgnoreCase("₤")
				|| currencyUnit.equalsIgnoreCase("﷼")
				|| currencyUnit.equalsIgnoreCase("₦")
				|| currencyUnit.equalsIgnoreCase("₱")
				|| currencyUnit.equalsIgnoreCase("د.إ")
				|| currencyUnit.equalsIgnoreCase("₹")
				|| currencyUnit.equalsIgnoreCase("€")
				|| currencyUnit.equalsIgnoreCase("S")
				|| currencyUnit.equalsIgnoreCase("NT$")
				|| currencyUnit.equalsIgnoreCase("د.ك")
				|| currencyUnit.equalsIgnoreCase("元")
				|| currencyUnit.equalsIgnoreCase("лв")
				|| currencyUnit.equalsIgnoreCase("Ft")
				|| currencyUnit.equalsIgnoreCase("¥")) {
			//#,###.## || # ###.##
			// Australia, Singapore, Thailand, United Kingdom, United States, Malaysian
			// Ghana, Turkey, Saudi Arabia, Nigeria, philippin, United Arab Emirates, India
			// Cac quoc gia dung dong Euro
			// South Africa Rand, Canada, Taiwan, Kuwaiti dinar, China Yuan Renminbi
			// 	Bulgarian Lev , china, japan
			for (char c : price.toCharArray()) {
				if (c != ',' && c != ' ') {
					tmp = tmp + c;
				}
			}
		} else if (currencyUnit.equalsIgnoreCase("₩")
				|| currencyUnit.equalsIgnoreCase("₫")) {
			// #,###,### || #.###.###
			//South Korea, VND
			for (char c : price.toCharArray()) {
				if (c != '.' && c != ',' && c != ' ') {
					tmp = tmp + c;
				}
			}
		} else if (currencyUnit.equalsIgnoreCase("Rp")
				|| currencyUnit.equalsIgnoreCase("руб")
				|| currencyUnit.equalsIgnoreCase("kr")
				|| currencyUnit.equalsIgnoreCase("L")
				|| currencyUnit.equalsIgnoreCase("zł")
				|| currencyUnit.equalsIgnoreCase("kn")) {
			// #.###,## || # ###,##
			// Indonesia, Russian, Danish Krone, Norwegian, Swedish krona, Croatian kuna
			for (char c : price.toCharArray()) {
				if (c != '.' && c != ',' && c != ' ') {
					tmp = tmp + c;
				} else if (c == ',') {
					tmp = tmp + '.';
				}
			}
		}
		int count = 0;
		for( char c : tmp.toCharArray()){
			if(c == ','){
				count += 2;
			}
			if( c > '9' || c < '0'){
				count ++;
			}
		}
		if (!tmp.equals("") && count <= 1) {
			price = tmp;
		} else {
			price = "";
			currencyUnit = "";
		}
		
		res.setCurrencySymple(currencyUnit);
		res.setPriceInNumberString(price);
		return res;
	}
}
