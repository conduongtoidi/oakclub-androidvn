package com.oakclub.android.fragment;

import com.oakclub.android.R;
import com.oakclub.android.SlidingActivity;

public class VIPRoomFragment {
	SlidingActivity activity;

	public VIPRoomFragment(SlidingActivity activity) {
		this.activity = activity;
	}
	public void initVIPRoom() {
		activity.init(R.layout.activity_vip_room);
	}
}
