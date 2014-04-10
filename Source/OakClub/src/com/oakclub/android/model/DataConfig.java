package com.oakclub.android.model;

import java.util.ArrayList;

public class DataConfig {
	private Sticker sticker;
	private Sticker gift;
	private SnapshotCounter snapshot_counter;
//	private Configs configs;


//	public Configs getConfigs() {
//		return configs;
//	}
//
//	public void setConfigs(Configs configs) {
//		this.configs = configs;
//	}

	public Sticker getSticker() {
		return sticker;
	}

	public void setSticker(Sticker sticker) {
		this.sticker = sticker;
	}

	public SnapshotCounter getSnapshot_counter() {
		return snapshot_counter;
	}

	public void setSnapshot_counter(SnapshotCounter snapshot_counter) {
		this.snapshot_counter = snapshot_counter;
	}

	public Sticker getGift() {
		return gift;
	}

	public void setGift(Sticker gift) {
		this.gift = gift;
	}

}
