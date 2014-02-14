package com.oakclub.android.model;

import java.util.ArrayList;

public class ListPhotoReturnDataObject {
	ArrayList<ListPhotoReturnDataItemObject> data;
	int index;
	int total;
	int $limit;
	public ArrayList<ListPhotoReturnDataItemObject> getData() {
		return data;
	}
	public void setData(ArrayList<ListPhotoReturnDataItemObject> data) {
		this.data = data;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int get$limit() {
		return $limit;
	}
	public void set$limit(int $limit) {
		this.$limit = $limit;
	}
	
}
