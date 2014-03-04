package com.oakclub.android.model;

import java.util.ArrayList;

public class DataConfig {
	private ArrayList<Sticker> stickers;
	private Configs configs;

	public ArrayList<Sticker> getStickers() {
		return stickers;
	}

	public void setStickers(ArrayList<Sticker> stickers) {
		this.stickers = stickers;
	}

	public Configs getConfigs() {
		return configs;
	}

	public void setConfigs(Configs configs) {
		this.configs = configs;
	}
}
