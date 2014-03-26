package com.oakclub.android.fragment;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

import com.oakclub.android.PurchaseConfirmedActivity;
import com.oakclub.android.R;
import com.oakclub.android.SlidingActivity;
import com.oakclub.android.SlidingActivity.MenuOakclub;
import com.oakclub.android.core.RequestUI;
import com.oakclub.android.model.IabResult;
import com.oakclub.android.model.Inventory;
import com.oakclub.android.model.SenVIPRegisterReturnObject;
import com.oakclub.android.model.SkuDetails;
import com.oakclub.android.model.Purchase;
import com.oakclub.android.util.Constants;
import com.oakclub.android.util.IabHelper;
import com.oakclub.android.util.OakClubUtil;
import com.oakclub.android.util.RichTextHelper;
import com.oakclub.android.view.TextViewWithFont;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class GetVIPFragment {
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
	//private TextView get_vip_title;
	private LinearLayout layout_package_1;
	private LinearLayout layout_package_2;
	private LinearLayout layout_package_3;
	private LinearLayout layout_package_4;
	private String TAG = "Purchase proccess";
	public static IabHelper mHelper;
	private String payload = "";
	private String SKU_ID = "";
	private String price = "";
	private String[] billList;
	private String[] monthsList;
	private String[] priceList;
	private ProgressDialog pd;
	private LinearLayout layoutProducInfor;
	private TextViewWithFont tvLoadProductListFailed;
	private ArrayList<SkuDetails> productList = new ArrayList<SkuDetails>();

	// (arbitrary) request code for the purchase flow
	static final int RC_REQUEST = 10001;
	SlidingActivity activity;

	public GetVIPFragment(SlidingActivity activity) {
		this.activity = activity;
	}

	public void initGetVIP() {
		activity.init(R.layout.activity_get_vip);
		btn_get_vip_package = (Button) activity.findViewById(R.id.btn_get_vip);
		btn_cancel = (Button) activity.findViewById(R.id.btn_cencel_get_vip);
		layoutProducInfor = (LinearLayout) activity.findViewById(R.id.get_vip_product_infor);
		tvLoadProductListFailed = (TextViewWithFont) activity.findViewById(R.id.error_load_products_title);
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
		//btn_get_vip_package.setEnabled(false);
		DisplayMetrics dimension = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dimension);
        int height = dimension.heightPixels;
		int padding = height / 55;

		vip_logo = (ImageView) activity.findViewById(R.id.vip_logo);
		vip_logo.setPadding(0, 0, padding, 0);
		//get_vip_title = (TextView) activity.findViewById(R.id.get_vip_title);
		//get_vip_title.setPadding(padding, padding, padding, padding);
		//btn_get_vip_package.setPadding(padding, padding, padding, padding);
		layout_package_1 = (LinearLayout) activity
				.findViewById(R.id.layout_package_1);
		layout_package_2 = (LinearLayout) activity
				.findViewById(R.id.layout_package_2);
		layout_package_3 = (LinearLayout) activity
				.findViewById(R.id.layout_package_3);
		layout_package_4 = (LinearLayout) activity
				.findViewById(R.id.layout_package_4);
		layout_package_1.setPadding(padding, 0, 0, 0);
		layout_package_2.setPadding(padding, 0, 0, 0);
		layout_package_3.setPadding(padding, 0, 0, 0);
		layout_package_4.setPadding(padding, 0, 0, 0);

		tvVipPackage1 = (TextView) activity
				.findViewById(R.id.txt_get_vip_package_1);
		tvVipPackage2 = (TextView) activity
				.findViewById(R.id.txt_get_vip_package_2);
		tvVipPackage3 = (TextView) activity
				.findViewById(R.id.txt_get_vip_package_3);
		tvVipPackage4 = (TextView) activity
				.findViewById(R.id.txt_get_vip_package_4);

		tvPricePackage1 = (TextView) activity
				.findViewById(R.id.txt_price_package_1);
		tvPricePackage2 = (TextView) activity
				.findViewById(R.id.txt_price_package_2);
		tvPricePackage3 = (TextView) activity
				.findViewById(R.id.txt_price_package_3);
		tvPricePackage4 = (TextView) activity
				.findViewById(R.id.txt_price_package_4);

		tvBillPackage2 = (TextView) activity
				.findViewById(R.id.txt_billed_package_2);
		tvBillPackage3 = (TextView) activity
				.findViewById(R.id.txt_billed_package_3);
		tvBillPackage4 = (TextView) activity
				.findViewById(R.id.txt_billed_package_4);

		tvSavePackage2 = (TextView) activity
				.findViewById(R.id.txt_save_package_2);
		tvSavePackage3 = (TextView) activity
				.findViewById(R.id.txt_save_package_3);
		tvSavePackage4 = (TextView) activity
				.findViewById(R.id.txt_save_package_4);

		monthsList = activity.getResources()
				.getStringArray(R.array.months_list);
		tvVipPackage1.setText(RichTextHelper.getRichText(String.format(
				activity.getString(R.string.txt_month), monthsList[0])));
		tvVipPackage2.setText(RichTextHelper.getRichText(String.format(
				activity.getString(R.string.txt_months), monthsList[1])));
		tvVipPackage3.setText(RichTextHelper.getRichText(String.format(
				activity.getString(R.string.txt_months), monthsList[2])));
		tvVipPackage4.setText(RichTextHelper.getRichText(String.format(
				activity.getString(R.string.txt_months), monthsList[3])));

		priceList = activity.getResources().getStringArray(R.array.price_list);
		tvPricePackage1.setText(RichTextHelper.getRichText(String.format(
				activity.getString(R.string.txt_USD), priceList[0])));
		tvPricePackage2.setText(RichTextHelper.getRichText(String.format(
				activity.getString(R.string.txt_USD_month), priceList[1])));
		tvPricePackage3.setText(RichTextHelper.getRichText(String.format(
				activity.getString(R.string.txt_USD_month), priceList[2])));
		tvPricePackage4.setText(RichTextHelper.getRichText(String.format(
				activity.getString(R.string.txt_USD_month), priceList[3])));

		billList = activity.getResources().getStringArray(R.array.bill_list);
		tvBillPackage2.setText(String.format(
				activity.getString(R.string.txt_billed), billList[1]));
		tvBillPackage3.setText(String.format(
				activity.getString(R.string.txt_billed), billList[2]));
		tvBillPackage4.setText(String.format(
				activity.getString(R.string.txt_billed), billList[3]));

		String[] saveList = activity.getResources().getStringArray(
				R.array.save_list);
		tvSavePackage2.setText(RichTextHelper.getRichText(String.format(
				activity.getString(R.string.txt_save), saveList[0])));
		tvSavePackage3.setText(RichTextHelper.getRichText(String.format(
				activity.getString(R.string.txt_save), saveList[1])));
		tvSavePackage4.setText(RichTextHelper.getRichText(String.format(
				activity.getString(R.string.txt_save), saveList[2])));
		rd_package_1.setClickable(false);
		rd_package_2.setClickable(false);
		rd_package_3.setClickable(false);
		rd_package_4.setClickable(false);
		layout_package_1.setOnClickListener(buttonClick);
		layout_package_2.setOnClickListener(buttonClick);
		layout_package_3.setOnClickListener(buttonClick);
		layout_package_4.setOnClickListener(buttonClick);
		(new Handler()).postDelayed(new Runnable() {
			@Override
			public void run() {
				pd = new ProgressDialog(activity);
				pd.setMessage(activity.getString(R.string.txt_loading));
				pd.setCancelable(false);
				pd.show();

				mHelper = new IabHelper(activity, DecodeKey(
						Constants.PUSHLISH_KEY[0], 1)
						+ DecodeKey(Constants.PUSHLISH_KEY[1], 2)
						+ DecodeKey(Constants.PUSHLISH_KEY[2], 3)
						+ DecodeKey(Constants.PUSHLISH_KEY[3], 4)
						+ DecodeKey(Constants.PUSHLISH_KEY[4], 5));

				mHelper.enableDebugLogging(true);

				// Start setup. This is asynchronous and the specified listener
				// will be called once setup completes.
				Log.d(TAG, "Starting setup.");
				mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
					public void onIabSetupFinished(IabResult result) {
						Log.d(TAG, "Setup finished.");
						if (mHelper == null)
							return;
						if (!result.isSuccess()) {
							return;
						}
						Log.d(TAG, "Setup successful. Querying inventory.");
						mHelper.getAllSKU( IabHelper.ITEM_TYPE_INAPP, mGetAllSkuDetailListener);					
					}
				});
			}
		}, 1000);

	}

	private void setPrice(ArrayList<SkuDetails> allSKU) {
		if(allSKU == null ||  allSKU.size() == 0) {
			layoutProducInfor.setVisibility(View.GONE);
			tvLoadProductListFailed.setVisibility(View.VISIBLE);
			return;
		}
		String priceOnMonth = priceList[0];
		for (SkuDetails sku : allSKU) {
			if (sku.getSku().equals(Constants.PRODUCT_IDS[0])) {
				priceOnMonth = sku.getPriceInMicros();
				break;
			}
		}
		for (SkuDetails sku : allSKU) {
			if (sku.getSku().equals(Constants.PRODUCT_IDS[0])) {
				tvPricePackage1.setText(RichTextHelper.getRichText(String
						.format(activity.getString(R.string.txt_USD),
								(Double.parseDouble(sku.getPriceInMicros())))+ " ("+ sku.getPrice()+ ")"));
				SKU_ID = sku.getSku();
				rd_package_1.setChecked(true);
			} else if (sku.getSku().equals(Constants.PRODUCT_IDS[1])) {
				tvPricePackage2
						.setText(RichTextHelper.getRichText(String.format(
								activity.getString(R.string.txt_USD_month),
								roundTwoDecimals(Double.parseDouble(sku.getPriceInMicros()) / Integer.parseInt(monthsList[1])))));
				if(sku.getCurrency().equals("USD")){
					tvBillPackage2
					.setText(String.format(
							activity.getString(R.string.txt_billed),
							sku.getPriceInMicros()));
				}else{
					tvBillPackage2
					.setText(String.format(
							activity.getString(R.string.txt_billed),
							sku.getPriceInMicros()) + " ("+ sku.getPrice()+ ")");
				}
				
				tvSavePackage2.setText(RichTextHelper.getRichText(String
						.format(activity.getString(R.string.txt_save),
								saveRate(Double.parseDouble(priceOnMonth),
										Double.parseDouble(sku.getPriceInMicros()),
										Integer.parseInt(monthsList[1])))));
			} else if (sku.getSku().equals(Constants.PRODUCT_IDS[2])) {
				tvPricePackage3
						.setText(RichTextHelper.getRichText(String.format(
								activity.getString(R.string.txt_USD_month),
								roundTwoDecimals(Double.parseDouble(sku.getPriceInMicros()) / Integer.parseInt(monthsList[2])))));
				if(sku.getCurrency().equals("USD")){
					tvBillPackage3
					.setText(String.format(
							activity.getString(R.string.txt_billed),
							sku.getPriceInMicros()));
				}else{
					tvBillPackage3
					.setText(String.format(
							activity.getString(R.string.txt_billed),
							sku.getPriceInMicros()) + " ("+ sku.getPrice()+ ")");
				}
				
				tvSavePackage3.setText(RichTextHelper.getRichText(String
						.format(activity.getString(R.string.txt_save),
								saveRate(Double.parseDouble(priceOnMonth),
										Double.parseDouble(sku.getPriceInMicros()),
										Integer.parseInt(monthsList[2])))));

			} else if (sku.getSku().equals(Constants.PRODUCT_IDS[3])) {
				tvPricePackage4
						.setText(RichTextHelper.getRichText(String.format(
								activity.getString(R.string.txt_USD_month),
								roundTwoDecimals(Double.parseDouble(sku.getPriceInMicros()) / Integer.parseInt(monthsList[3])) )));
				if(sku.getCurrency().equals("USD")){
					tvBillPackage4
					.setText(String.format(
							activity.getString(R.string.txt_billed),
							sku.getPriceInMicros()));
				}else{
					tvBillPackage4
					.setText(String.format(
							activity.getString(R.string.txt_billed),
							sku.getPriceInMicros()) + " ("+ sku.getPrice()+ ")");
				}
				
				tvSavePackage4.setText(RichTextHelper.getRichText(String
						.format(activity.getString(R.string.txt_save),
								saveRate(Double.parseDouble(priceOnMonth),
										Double.parseDouble(sku.getPriceInMicros()),
										Integer.parseInt(monthsList[3])))));
			}
		}
	}

	private String RandomString(int length) {
		String characters = "q1w2e3r4t5y6u7i8o9plkj0hgfdsamnbvcxz/?>.,<!@#$%^&*()_+=-";
		Random r = new Random();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			char c = characters.charAt(r.nextInt((int) (characters.length())));
			sb.append(c);
		}
		return sb.toString();

	}

	private String DecodeKey(String source, int mode) {
		String res = "";
		switch (mode) {
		case 1:
			for (int i = 0; i < source.length(); i++) {
				res += (char) (source.charAt(i) ^ (i % 26));
			}
			break;
		case 2:
			for (int i = source.length() - 1; i >= 0; i--) {
				res += (char) (source.charAt(source.length() - i - 1) ^ (i % 26));
			}
			break;
		case 3:
			for (int i = 0; i < source.length(); i++) {
				res += (char) (source.charAt(i) ^ (2 * i) % 26);
			}
			break;
		case 4:
			for (int i = 0; i < source.length(); i++) {
				res += (char) (source.charAt(i) ^ (2 * (i + 1)) % 26);
			}
			break;
		case 5:
			for (int i = source.length() - 1; i >= 0; i--) {
				res += (char) (source.charAt(source.length() - i - 1) ^ (2 * i) % 26);
			}
			break;
		default:
			break;
		}
		return res;
	}

	private void Purchase(String SKU_ID) {

		payload = RandomString(36);
		mHelper.launchPurchaseFlow(activity, SKU_ID, RC_REQUEST,
				mPurchaseFinishedListener, payload);

	}

	private String saveRate(double priceOneMonth, double price, int months) {
		double res = ((priceOneMonth * months - price)
				/ (priceOneMonth * months) * 100);
		return Math.round(res) + "";
	}
	String roundTwoDecimals(double d) {
        DecimalFormat twoDForm = new DecimalFormat(".##");
        return twoDForm.format(d) + "";
	}
	IabHelper.GetSkuFinishedListener mGetAllSkuDetailListener = new IabHelper.GetSkuFinishedListener() {
		
		@Override
		public void onGetSkuFinishedListener(ArrayList<SkuDetails> skuList) {
			if(skuList == null){
				Toast.makeText(activity, activity.getString(R.string.abnormal_error_message), Toast.LENGTH_LONG).show();
				if (pd != null && pd.isShowing()) {
					pd.dismiss();
					pd = null;
				}
				activity.setMenu(MenuOakclub.SNAPSHOT);
				SnapshotFragment snapshot = new SnapshotFragment(
						activity);
				snapshot.initSnapshot();
				activity.snapshot = snapshot;
			}else{
				productList = skuList;
				setPrice(productList);
				if (pd != null && pd.isShowing()) {
					pd.dismiss();
					pd = null;
				}
			}			
		}
	};
	IabHelper.QueryInventoryFinishedListener mGotInventoryToConsumeListener = new IabHelper.QueryInventoryFinishedListener() {
		public void onQueryInventoryFinished(IabResult result,
				Inventory inventory) {
			if (mHelper == null)
				return;
			if (result.isFailure()) {
				return;
			}	

			if (inventory.getPurchase(SKU_ID) != null) {
				mHelper.consumeAsync(inventory.getPurchase(SKU_ID),
						mConsumeFinishedListener);
			}else{
				Purchase(SKU_ID);
			}
		}
	};

	/** Verifies the developer payload of a purchase. */
	boolean verifyDeveloperPayload(Purchase p) {
		String payload = p.getDeveloperPayload();
		if (payload.equals(this.payload) && p.getSku().equals(SKU_ID) && p.getPackageName().equals(activity.getPackageName()))
			{
				return true;
			}
		return false;
	}

	// Callback for when a purchase is finished
	IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
		public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
			btn_get_vip_package.setEnabled(true);
			Log.d(TAG, "Purchase finished: " + result + ", purchase: "
					+ purchase);

			// if we were disposed of in the meantime, quit.
			if (mHelper == null)
				return;

			if (result.isFailure()) {
				// Popup hear............
				// enableDialogWarning(activity,activity.getString(R.string.txt_vip_room),"Error purchasing: "
				// + result);
				enableDialogWarning(activity,
						activity.getString(R.string.txt_vip_room),
						activity.getString(R.string.txt_purchase_cancel));
				return;
			}
			if (!verifyDeveloperPayload(purchase)) {
				enableDialogWarning(activity,
						activity.getString(R.string.txt_vip_room),
						activity.getString(R.string.txt_purchase_error));
				return;
			}

			Log.d(TAG, "Purchase successful.");
			// xac nhan la VIP user len server
			SendVIPRegister loader = new SendVIPRegister(
					Constants.VIP_REGISTER, activity, purchase);			
			activity.getRequestQueue().addRequest(loader);
		}
	};

	// Called when consumption is complete
	IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
		public void onConsumeFinished(Purchase purchase, IabResult result) {
			Log.d(TAG, "Consumption finished. Purchase: " + purchase
					+ ", result: " + result);
			 if (mHelper == null)
				 return;
			
			 if (result.isSuccess()) {
				 Log.d(TAG, "Consumption successful. Provisioning.");
				 Purchase(SKU_ID);
			 } else {
				  enableDialogWarning(activity,
				  activity.getString(R.string.txt_vip_room),
				  activity.getString(R.string.abnormal_error_message));
			 }
			 Log.d(TAG, "End consumption flow.");
		}
	};

	private OnClickListener buttonClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_get_vip:
				btn_get_vip_package.setEnabled(false);
				if (ProfileSettingFragment.profileInfoObj != null
						&& ProfileSettingFragment.profileInfoObj.isIs_vip()) {
					enableDialogWarning(activity,
							activity.getString(R.string.txt_vip_room),
							activity.getString(R.string.txt_you_VIP_member));
					break;
				}
				if (OakClubUtil.isInternetAccess(activity)) {
					if (SKU_ID != "") {
						mHelper.queryInventoryAsync(mGotInventoryToConsumeListener);
					} else {
						enableDialogWarning(
								activity,
								activity.getString(R.string.txt_vip_room),
								activity.getString(R.string.txt_choose_package_msg));
					}

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

			case R.id.btn_cencel_get_vip:
				break;
			case  R.id.layout_package_1:
				price = billList[0];
				SKU_ID = Constants.PRODUCT_IDS[0];
				SetUnCheck();
				rd_package_1.setChecked(true);
				break;
			case R.id.layout_package_2:
				SKU_ID = Constants.PRODUCT_IDS[1];
				price = billList[1];
				SetUnCheck();
				rd_package_2.setChecked(true);
				break;
			case R.id.layout_package_3: 
				SKU_ID = Constants.PRODUCT_IDS[2];
				price = billList[2];
				SetUnCheck();
				rd_package_3.setChecked(true);
				break;
			case  R.id.layout_package_4:
				SKU_ID = Constants.PRODUCT_IDS[3];
				price = billList[3];
				SetUnCheck();
				rd_package_4.setChecked(true);
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
		btn_get_vip_package.setEnabled(true);
	}

	public static void enableDialogWarning(final Context context, String title,
			final String message) {
		AlertDialog.Builder builder;
		builder = new AlertDialog.Builder(context);
		final AlertDialog dialog = builder.create();
		LayoutInflater inflater = LayoutInflater.from(context);
		View layout = inflater.inflate(R.layout.dialog_warning_ok, null);
		dialog.setView(layout, 0, 0, 0, 0);
		TextView tvTitle = (TextView) layout
				.findViewById(R.id.dialog_warning_lltheader_tvTitle);
		tvTitle.setText(title);
		TextView tvQuestion = (TextView) layout
				.findViewById(R.id.dialog_warning_tvQuestion);
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

	class SendVIPRegister extends RequestUI {
		SenVIPRegisterReturnObject obj;
		private Purchase purchase;

		public SendVIPRegister(Object key, Activity activity, Purchase purchase) {
			super(key, activity);
			this.purchase = purchase;
		}

		@Override
		public void execute() throws Exception {
			obj = activity.oakClubApi.SendVIPRegister(purchase.getSku(), purchase.getToken());
		}

		@Override
		public void executeUI(Exception ex) {
			
			if (obj != null && obj.isStatus()) {
				// popup purchase seccess
				if (ProfileSettingFragment.profileInfoObj != null) {
					ProfileSettingFragment.profileInfoObj.setIs_vip(true);
				}
				Intent intent = new Intent(activity,
						PurchaseConfirmedActivity.class);
				for(SkuDetails sku : productList){
					if(sku.getSku().equals(purchase.getSku())){
						price = sku.getPrice();
						break;
					}
				}
				intent.putExtra(Constants.PURCHASE_PRICE, price);
				intent.putExtra(Constants.TRANSACTION_ID, purchase.getOrderId());
				activity.startActivityForResult(intent, Constants.GETVIP);
			} else {
				enableDialogWarning(activity,
						activity.getString(R.string.txt_vip_room),
						activity.getString(R.string.abnormal_error_message));
			}
		}

	}
}
