package com.oakclub.android.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import com.oakclub.android.PurchaseActivity;
import com.oakclub.android.R;
import com.oakclub.android.SlidingActivity;
import com.oakclub.android.util.Constants;
import com.oakclub.android.util.OakClubUtil;

public class GetVIPFragment {
	public static Button btn_get_vip_package;
	private ImageView vip_logo;
	SlidingActivity activity;

	public GetVIPFragment(SlidingActivity activity) {
		this.activity = activity;
	}

	public void initGetVIP() {
		activity.init(R.layout.activity_get_vip);
		btn_get_vip_package = (Button) activity.findViewById(R.id.btn_get_vip);
		btn_get_vip_package.setOnClickListener(buttonClick);
		DisplayMetrics dimension = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dimension);
		int height = dimension.heightPixels;
		int padding = height / 55;
		vip_logo = (ImageView) activity.findViewById(R.id.vip_logo);
		vip_logo.setPadding(0, 0, padding, 0);
	}

	private OnClickListener buttonClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_get_vip:
				btn_get_vip_package.setEnabled(false);
				if (OakClubUtil.isInternetAccess(activity)) {
					Intent intent = new Intent(activity, PurchaseActivity.class);
					activity.startActivityForResult(intent, Constants.GETVIP);
				} else {
					AlertDialog.Builder builder;
					builder = new AlertDialog.Builder(activity);
					final AlertDialog dialog = builder.create();
					LayoutInflater inflater = LayoutInflater.from(activity);
					View layout = inflater.inflate(R.layout.dialog_warning_ok,
							null);
					dialog.setView(layout, 0, 0, 0, 0);
					Button btnOK = (Button) layout
							.findViewById(R.id.dialog_internet_access_lltfooter_btOK);
					btnOK.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							dialog.dismiss();
							activity.finish();
						}
					});
					dialog.setCancelable(false);
					dialog.show();
				}
				break;
			default:
				break;
			}
		}
	};
}
