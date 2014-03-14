/* Copyright (c) 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.oakclub.android.model;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import com.oakclub.android.util.IabHelper;

/**
 * Represents an in-app product's listing details.
 */
public class SkuDetails {
    String itemType;
    String sku;
    String type;
    String price;
    String title;
    String description;
    String json;
    String currency;

    public SkuDetails(String jsonSkuDetails) throws JSONException {
        this(IabHelper.ITEM_TYPE_INAPP, jsonSkuDetails);
    }

    public SkuDetails(String itemType, String jsonSkuDetails) throws JSONException {
    	this.itemType = itemType;
        this.json = jsonSkuDetails;
        JSONObject o = new JSONObject(this.json);
        this.sku = o.optString("productId");
        this.type = o.optString("type");
        this.price = o.optString("price");
        this.title = o.optString("title");
        this.description = o.optString("description");
        //price();
    }
    private void price(){
    	//Locale us = new Locale("us", "US");
    	NumberFormat numberFormat = NumberFormat.getCurrencyInstance();;
    	try {
            Number  d = numberFormat.parse(this.price);
            BigDecimal bd = new BigDecimal(d.toString());
            this.price = bd.toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    public String getSku() { return sku; }
    public String getType() { return type; }
    public String getPrice() { return price; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getCurrency() { return currency; }
    @Override
    public String toString() {
        return "SkuDetails:" + json;
    }
}