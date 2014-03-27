package com.oakclub.android.fragment;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.oakclub.android.R;
import com.oakclub.android.SlidingActivity;
import com.oakclub.android.SlidingActivity.MenuOakclub;

public class VIPRoomFragment {
	SlidingActivity activity;
	private Button btnMeetMorePeople;

	public VIPRoomFragment(SlidingActivity activity) {
		this.activity = activity;
	}

	public void initVIPRoom() {
		activity.init(R.layout.activity_vip_room);
		btnMeetMorePeople = (Button) activity
				.findViewById(R.id.btn_meet_more_people);
		btnMeetMorePeople.setOnClickListener(buttonClick);
	}

	private OnClickListener buttonClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_meet_more_people:
				activity.setMenu(MenuOakclub.SNAPSHOT);
				SnapshotFragment snapshot = new SnapshotFragment(
						activity);
				snapshot.initSnapshot();
				activity.snapshot = snapshot;
				break;
			default:
				break;
			}
		}
	};
}
