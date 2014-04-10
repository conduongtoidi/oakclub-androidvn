package com.oakclub.android.model;

import java.util.ArrayList;

public class Groups {
	private String name;
	private Icon icon;
	private String url;
	private ArrayList<List> list;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Icon getIcon() {
		return icon;
	}

	public void setIcon(Icon icon) {
		this.icon = icon;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public ArrayList<List> getList() {
		return list;
	}

	public void setList(ArrayList<List> list) {
		this.list = list;
	}

}
