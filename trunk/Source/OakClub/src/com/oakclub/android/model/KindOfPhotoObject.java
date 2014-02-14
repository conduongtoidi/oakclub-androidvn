package com.oakclub.android.model;

import java.io.Serializable;

public class KindOfPhotoObject implements Serializable {
	private String src;
	private String src_a;
	private String src_l;
	public String getSrc() {
		return src;
	}
	public void setSrc(String src) {
		this.src = src;
	}
	public String getSrc_a() {
		return src_a;
	}
	public void setSrc_a(String src_a) {
		this.src_a = src_a;
	}
	public String getSrc_l() {
		return src_l;
	}
	public void setSrc_l(String src_l) {
		this.src_l = src_l;
	}
	
}
