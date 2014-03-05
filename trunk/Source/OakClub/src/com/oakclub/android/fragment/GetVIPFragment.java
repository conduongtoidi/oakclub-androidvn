package com.oakclub.android.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.oakclub.android.R;
import com.oakclub.android.SlidingActivity;
import com.oakclub.android.util.OakClubUtil;
import com.oakclub.android.util.RichTextHelper;

public class GetVIPFragment{
	private Button btn_get_vip_package;
	private Button btn_cancel;
	private RadioButton rd_package_1;
	private RadioButton rd_package_2;
	private RadioButton rd_package_3;
	private RadioButton rd_package_4;
	private TextView tvVipPackage1;
	private TextView tvVipPackage2;
	private TextView tvVipPackage3;
	private TextView tvVipPackage4;
	
	private TextView tvPricePackage1;
	private TextView tvPricePackage2;
	private TextView tvPricePackage3;
	private TextView tvPricePackage4;
	
	private TextView tvBillPackage2;
	private TextView tvBillPackage3;
	private TextView tvBillPackage4;
	
	private TextView tvSavePackage2;
	private TextView tvSavePackage3;
	private TextView tvSavePackage4;
	
	private ImageView vip_logo;
	private TextView get_vip_title;
	private LinearLayout layout_package_1;
	private LinearLayout layout_package_2;
	private LinearLayout layout_package_3;
	private LinearLayout layout_package_4;
 	private String TAG = "Purchase proccess";
	//IabHelper mHelper;
	SlidingActivity activity;

	public GetVIPFragment(SlidingActivity activity) {
		this.activity = activity;
	}

	public void initGetVIP() {
		activity.init(R.layout.activity_get_vip);
		btn_get_vip_package = (Button) activity.findViewById(R.id.btn_get_vip);
		btn_cancel = (Button) activity.findViewById(R.id.btn_cencel_get_vip);
		rd_package_1 = (RadioButton) activity
				.findViewById(R.id.radio_get_vip_package_1);
		rd_package_2 = (RadioButton) activity
				.findViewById(R.id.radio_get_vip_package_2);
		rd_package_3 = (RadioButton) activity
				.findViewById(R.id.radio_get_vip_package_3);
		rd_package_4 = (RadioButton) activity
				.findViewById(R.id.radio_get_vip_package_4);
		btn_cancel.setOnClickListener(buttonClick);
		btn_get_vip_package.setOnClickListener(buttonClick);
		btn_get_vip_package.setEnabled(false);
		int padding = (int) OakClubUtil.convertDpToPixel(12, activity);
		
		vip_logo = (ImageView) activity.findViewById(R.id.vip_logo);
		vip_logo.setPadding(padding, padding, padding, padding);
		get_vip_title = (TextView) activity.findViewById(R.id.get_vip_title);
		get_vip_title.setPadding(padding, padding, padding, padding);
		btn_get_vip_package.setPadding(padding, padding, padding, padding);
		layout_package_1 = (LinearLayout) activity.findViewById(R.id.layout_package_1);
		layout_package_2 = (LinearLayout) activity.findViewById(R.id.layout_package_2);
		layout_package_3 = (LinearLayout) activity.findViewById(R.id.layout_package_3);
		layout_package_4 = (LinearLayout) activity.findViewById(R.id.layout_package_4);
		layout_package_1.setPadding(padding, padding, padding, padding);
		layout_package_2.setPadding(padding, padding, padding, padding);
		layout_package_3.setPadding(padding, padding, padding, padding);
		layout_package_4.setPadding(padding, padding, padding, padding);
		
		tvVipPackage1 = (TextView) activity.findViewById(R.id.txt_get_vip_package_1);
		tvVipPackage2 = (TextView) activity.findViewById(R.id.txt_get_vip_package_2);
		tvVipPackage3 = (TextView) activity.findViewById(R.id.txt_get_vip_package_3);
		tvVipPackage4 = (TextView) activity.findViewById(R.id.txt_get_vip_package_4);
		
		tvPricePackage1 = (TextView) activity.findViewById(R.id.txt_price_package_1);
		tvPricePackage2 = (TextView) activity.findViewById(R.id.txt_price_package_2);
		tvPricePackage3 = (TextView) activity.findViewById(R.id.txt_price_package_3);
		tvPricePackage4 = (TextView) activity.findViewById(R.id.txt_price_package_4);
		
		tvBillPackage2 = (TextView) activity.findViewById(R.id.txt_billed_package_2);
		tvBillPackage3 = (TextView) activity.findViewById(R.id.txt_billed_package_3);
		tvBillPackage4 = (TextView) activity.findViewById(R.id.txt_billed_package_4);
		
		tvSavePackage2 = (TextView) activity.findViewById(R.id.txt_save_package_2);
		tvSavePackage3 = (TextView) activity.findViewById(R.id.txt_save_package_3);
		tvSavePackage4 = (TextView) activity.findViewById(R.id.txt_save_package_4);
			
		String[] monthsList = activity.getResources().getStringArray(R.array.months_list);
		tvVipPackage1.setText(RichTextHelper
				.getRichText(String.format(activity.getString(R.string.txt_month),monthsList[0])));
		tvVipPackage2.setText(RichTextHelper
				.getRichText(String.format(activity.getString(R.string.txt_months),monthsList[1])));
		tvVipPackage3.setText(RichTextHelper
				.getRichText(String.format(activity.getString(R.string.txt_months),monthsList[2])));
		tvVipPackage4.setText(RichTextHelper
				.getRichText(String.format(activity.getString(R.string.txt_months),monthsList[3])));
		
		String[] priceList = activity.getResources().getStringArray(R.array.price_list);
		tvPricePackage1.setText(RichTextHelper
				.getRichText(String.format(activity.getString(R.string.txt_USD),priceList[0])));
		tvPricePackage2.setText(RichTextHelper
				.getRichText(String.format(activity.getString(R.string.txt_USD_month),priceList[1])));
		tvPricePackage3.setText(RichTextHelper
				.getRichText(String.format(activity.getString(R.string.txt_USD_month),priceList[2])));
		tvPricePackage4.setText(RichTextHelper
				.getRichText(String.format(activity.getString(R.string.txt_USD_month),priceList[3])));
		
		String[] billList = activity.getResources().getStringArray(R.array.bill_list);
		tvBillPackage2.setText(String.format(activity.getString(R.string.txt_billed), billList[0]));
		tvBillPackage3.setText(String.format(activity.getString(R.string.txt_billed), billList[1]));
		tvBillPackage4.setText(String.format(activity.getString(R.string.txt_billed), billList[2]));
		
		String[] saveList = activity.getResources().getStringArray(R.array.save_list);
		tvSavePackage2.setText(RichTextHelper
				.getRichText(String.format(activity.getString(R.string.txt_save),saveList[0])));
		tvSavePackage3.setText(RichTextHelper
				.getRichText(String.format(activity.getString(R.string.txt_save),saveList[1])));
		tvSavePackage4.setText(RichTextHelper
				.getRichText(String.format(activity.getString(R.string.txt_save),saveList[2])));
		
		rd_package_1.setOnClickListener(buttonClick);
		rd_package_2.setOnClickListener(buttonClick);
		rd_package_3.setOnClickListener(buttonClick);
		rd_package_4.setOnClickListener(buttonClick);
	}

//	private void Purchase() {
//		String base64EncodedPublicKey = "";
//		mHelper = new IabHelper(activity, base64EncodedPublicKey);
//
//		mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
//			@Override
//			public void onIabSetupFinished(IabResult result) {
//				if (!result.isSuccess()) {
//					// Oh noes, there was a problem.
//					Log.d(TAG, "Problem setting up In-app Billing: " + result);
//					return;
//				}
//				if (mHelper == null) return;
//				// IAB is fully set up. Now, let's get an inventory of stuff we own.
//                Log.d(TAG, "Setup successful. Querying inventory.");
//                mHelper.queryInventoryAsync(mGotInventoryListener);
//			}
//		});
//	}
//	// Listener that's called when we finish querying the items and subscriptions we own
//    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
//        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
//            Log.d(TAG, "Query inventory finished.");
//
//            // Have we been disposed of in the meantime? If so, quit.
//            if (mHelper == null) return;
//
//            // Is it a failure?
//            if (result.isFailure()) {
//            	alert("Failed to query inventory: " + result);
//                return;
//            }
//
//            Log.d(TAG, "Query inventory was successful.");
//
//            /*
//             * Check for items we own. Notice that for each purchase, we check
//             * the developer payload to see if it's correct! See
//             * verifyDeveloperPayload().
//             */
//
//            // Do we have the premium upgrade?
////            Purchase premiumPurchase = inventory.getPurchase(SKU_PREMIUM);
////            mIsPremium = (premiumPurchase != null && verifyDeveloperPayload(premiumPurchase));
////            Log.d(TAG, "User is " + (mIsPremium ? "PREMIUM" : "NOT PREMIUM"));
////
////            // Do we have the infinite gas plan?
////            Purchase infiniteGasPurchase = inventory.getPurchase(SKU_INFINITE_GAS);
////            mSubscribedToInfiniteGas = (infiniteGasPurchase != null &&
////                    verifyDeveloperPayload(infiniteGasPurchase));
////            Log.d(TAG, "User " + (mSubscribedToInfiniteGas ? "HAS" : "DOES NOT HAVE")
////                        + " infinite gas subscription.");
////            if (mSubscribedToInfiniteGas) mTank = TANK_MAX;
////
////            // Check for gas delivery -- if we own gas, we should fill up the tank immediately
////            Purchase gasPurchase = inventory.getPurchase(SKU_GAS);
////            if (gasPurchase != null && verifyDeveloperPayload(gasPurchase)) {
////                Log.d(TAG, "We have gas. Consuming it.");
////                mHelper.consumeAsync(inventory.getPurchase(SKU_GAS), mConsumeFinishedListener);
////                return;
////            }
////
////            updateUi();
////            setWaitScreen(false);
////            Log.d(TAG, "Initial inventory query finished; enabling main UI.");
//        }
//    };
    void alert(String message) {
        AlertDialog.Builder bld = new AlertDialog.Builder(activity);
        bld.setMessage(message);
        bld.setNeutralButton("OK", null);
        Log.d(TAG, "Showing alert dialog: " + message);
        bld.create().show();
    }
	private OnClickListener buttonClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_get_vip:
				btn_get_vip_package.setEnabled(false);
				if (OakClubUtil.isInternetAccess(activity)) {
					enableDialogWarning(activity, activity.getString(R.string.txt_vip_room), activity.getString(R.string.txt_vip_room_message));
					
				}else{
					AlertDialog.Builder builder;
					builder = new AlertDialog.Builder(activity);
					final AlertDialog dialog = builder.create();
					LayoutInflater inflater = LayoutInflater.from(activity);
					View layout = inflater
							.inflate(R.layout.dialog_warning_ok, null);
					dialog.setView(layout, 0, 0, 0, 0);
					Button btnOK = (Button) layout
							.findViewById(R.id.dialog_internet_access_lltfooter_btOK);
					btnOK.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							activity.finish();
						}
					});
					dialog.setCancelable(false);
					dialog.show();
				}
				break;

			case R.id.btn_cencel_get_vip:
				break;
			case R.id.radio_get_vip_package_1:
				SetUnCheck();
				rd_package_1.setChecked(true);
				btn_get_vip_package.setEnabled(true);
				break;
			case R.id.radio_get_vip_package_2:
				SetUnCheck();
				rd_package_2.setChecked(true);
				btn_get_vip_package.setEnabled(true);
				break;
			case R.id.radio_get_vip_package_3:
				SetUnCheck();
				rd_package_3.setChecked(true);
				btn_get_vip_package.setEnabled(true);
				break;
			case R.id.radio_get_vip_package_4:
				SetUnCheck();
				rd_package_4.setChecked(true);
				btn_get_vip_package.setEnabled(true);
				break;
			default:
				break;
			}
		}
	};

	private void SetUnCheck() {
		rd_package_1.setChecked(false);
		rd_package_2.setChecked(false);
		rd_package_3.setChecked(false);
		rd_package_4.setChecked(false);
	}
	public static void enableDialogWarning(final Context context, String title, final String message)
	 {
       AlertDialog.Builder builder;
       builder = new AlertDialog.Builder(context);
       final AlertDialog dialog = builder.create();
       LayoutInflater inflater = LayoutInflater
               .from(context);
       View layout = inflater.inflate(R.layout.dialog_warning_ok,
               null);
       dialog.setView(layout, 0, 0, 0, 0);
       TextView tvTitle = (TextView)layout.findViewById(R.id.dialog_warning_lltheader_tvTitle);
       tvTitle.setText(title);
       TextView tvQuestion = (TextView)layout.findViewById(R.id.dialog_warning_tvQuestion);
       tvQuestion.setText(message);
       Button btnOK = (Button) layout
               .findViewById(R.id.dialog_internet_access_lltfooter_btOK);
       btnOK.setOnClickListener(new OnClickListener() {

           @Override
           public void onClick(View v) {
               dialog.cancel();
           }
       });
       dialog.setCancelable(false);
       dialog.show();
	 }
}
