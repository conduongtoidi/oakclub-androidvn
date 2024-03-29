package com.oakclub.android;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import com.oakclub.android.base.OakClubBaseActivity;
import com.oakclub.android.core.RequestUI;
import com.oakclub.android.fragment.ProfileSettingFragment;
import com.oakclub.android.model.IabResult;
import com.oakclub.android.model.Inventory;
import com.oakclub.android.model.Purchase;
import com.oakclub.android.model.SenVIPRegisterReturnObject;
import com.oakclub.android.model.SkuDetails;
import com.oakclub.android.util.Constants;
import com.oakclub.android.util.CurrencyFormatter;
import com.oakclub.android.util.IabHelper;
import com.oakclub.android.util.OakClubUtil;
import com.oakclub.android.util.RichTextHelper;
import com.oakclub.android.view.TextViewWithFont;

public class PurchaseActivity extends OakClubBaseActivity{
	private Button btn_get_vip_package;
	private ImageView vip_logo;
	private String TAG = "Purchase proccess";
	public static IabHelper mHelper;
	private String payload = "";
	private String SKU_ID = "";
	private String price = "";
	private String[] monthsList;
	private ProgressDialog pd;
	private TextViewWithFont tvLoadProductListFailed;
	private ArrayList<SkuDetails> productList = new ArrayList<SkuDetails>();
	public LinearLayout listProduct;
	public int mPosition = -1;
	private Button btnBack;
	// (arbitrary) request code for the purchase flow
	static final int RC_REQUEST = 10001;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_purchase);
		listProduct = (LinearLayout) findViewById(R.id.list_product);
		btnBack = (Button) findViewById(R.id.btn_purchasing_back);
		btn_get_vip_package = (Button) findViewById(R.id.btn_get_vip);
		tvLoadProductListFailed = (TextViewWithFont) this
				.findViewById(R.id.error_load_products_title);
		btn_get_vip_package.setOnClickListener(buttonClick);
		DisplayMetrics dimension = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dimension);
		int height = dimension.heightPixels;
		int padding = height / 55;
		btnBack.setOnClickListener(buttonClick);
		vip_logo = (ImageView) findViewById(R.id.vip_logo);
		vip_logo.setPadding(0, 0, padding, 0);
		monthsList = getResources()
				.getStringArray(R.array.months_list);

		(new Handler()).postDelayed(new Runnable() {
			@Override
			public void run() {
				pd = new ProgressDialog(PurchaseActivity.this);
				pd.setMessage(getString(R.string.txt_loading));
				pd.setCancelable(false);
				pd.show();

				mHelper = new IabHelper(PurchaseActivity.this, DecodeKey(
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
						mHelper.getAllSKU(IabHelper.ITEM_TYPE_INAPP,
								mGetAllSkuDetailListener);
					}
				});
			}
		}, 0);
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
		if (mHelper != null) {
			mHelper.launchPurchaseFlow(this, SKU_ID, RC_REQUEST,
					mPurchaseFinishedListener, payload);
		} else {
			btn_get_vip_package.setEnabled(true);
			enableDialogWarning(this,
					getString(R.string.txt_vip_room),
					getString(R.string.abnormal_error_message));
		}

	}

	private String saveRate(double priceOneMonth, double price, int months) {
		double res = ((priceOneMonth * months - price)
				/ (priceOneMonth * months) * 100);
		return Math.round(res) + "";
	}
	private static DecimalFormat df;
	  static {
	    df = new DecimalFormat("#,###,##0.00");
	    DecimalFormatSymbols otherSymbols = new   DecimalFormatSymbols(Locale.ENGLISH);
	    otherSymbols.setDecimalSeparator('.');
	    otherSymbols.setGroupingSeparator(' ');
	    df.setDecimalFormatSymbols(otherSymbols);
	  }

	  public static <T extends Number> String roundTwoDecimals(T number) {
	     return df.format(number);
	  }

	private static class ListProductHolder {
		public TextView tvVipPackage;
		public TextView tvPrice;
		public TextView tvBilled;
		public TextView tvSaved;
		public RadioButton rdButton;
		public ImageView ivVIPSeparator;
	}

	private ArrayList<SkuDetails> CheckListProduct(ArrayList<SkuDetails> list) {
		ArrayList<SkuDetails> res = new ArrayList<SkuDetails>();
		Map<String, SkuDetails> mapList = new HashMap<String, SkuDetails>();
		String[] productIDs = Constants.PRODUCT_IDS;
		for (SkuDetails sku : list) {
			int idx = sku.getTitle().indexOf("(Oakclub)");
			if (idx > 0) {
				sku.setTitle(sku.getTitle().substring(0, idx));
			}
			sku = CurrencyFormatter.parse(sku);
			mapList.put(sku.getSku(), sku);
		}
		for (String id : productIDs) {
			if (mapList.containsKey(id)) {
				res.add(mapList.get(id));
			}
		}
		Collections.sort(res, new Comparator<SkuDetails>() {

			@Override
			public int compare(SkuDetails lhs, SkuDetails rhs) {
				return (Double.parseDouble(rhs.getPriceInMicros()) > Double
						.parseDouble(rhs.getPriceInMicros())) ? 1 : 0;
			}
		});
		return res;
	}

	private void viewProductList(ArrayList<SkuDetails> productList,
			String[] monthsList) {
		if(productList == null || (productList != null && productList.size() == 0)){
			return;
		}
		btn_get_vip_package.setVisibility(View.VISIBLE);
		int i = 0;
		String priceOnMonth = "1";
		double minPrice = 1000000000;
		SkuDetails skud = productList.get(0);
		if(skud != null && (skud.getCurrencySymple().equals("") || skud.getPriceInNumberString().equals(""))){
			for (SkuDetails pro : productList) {
				if (Double.parseDouble(pro.getPriceInMicros()) < minPrice) {
					minPrice = Double.parseDouble(pro.getPriceInMicros());
				}
			}
		}else{
			for (SkuDetails pro : productList) {
				if (Double.parseDouble(pro.getPriceInNumberString()) < minPrice) {
					minPrice = Double.parseDouble(pro.getPriceInNumberString());
				}
			}
		}
		priceOnMonth = minPrice + "";
		for (SkuDetails sku : productList) {
			if (i >= monthsList.length)
				return;
			LayoutInflater mInflater = (LayoutInflater) 
					getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			View convertView = mInflater.inflate(R.layout.item_in_app_product,
					listProduct, false);

			convertView.setLayoutParams(new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			ListProductHolder holder = new ListProductHolder();
			holder.tvVipPackage = (TextView) convertView
					.findViewById(R.id.txt_get_vip_package);
			holder.tvPrice = (TextView) convertView
					.findViewById(R.id.txt_price_package);
			holder.tvBilled = (TextView) convertView
					.findViewById(R.id.txt_billed_package);
			holder.tvSaved = (TextView) convertView
					.findViewById(R.id.txt_save_package);
			holder.rdButton = (RadioButton) convertView
					.findViewById(R.id.radio_get_vip_package);
			holder.ivVIPSeparator = (ImageView) convertView.findViewById(R.id.vip_separators);
			if( i == 0){
				holder.rdButton.setChecked(true);
				mPosition = i;
				holder.ivVIPSeparator.setVisibility(View.GONE);
			}
			holder.rdButton.setClickable(false);

			//holder.tvVipPackage.setText(RichTextHelper.getRichText(String.format(getString(R.string.txt_month), monthsList[i])));
			if(sku.getCurrencySymple().equals("") || sku.getPriceInNumberString().equals("")){
				if (sku.getPriceInMicros().equals(priceOnMonth)) {
					holder.tvVipPackage.setText(RichTextHelper.getRichText(String.format(getString(R.string.txt_month), monthsList[i])));
					holder.tvPrice.setText(RichTextHelper.getRichText(String
							.format(getString(R.string.txt_int_app_product_title),sku.getPriceInMicros() + " " +sku.getCurrency() +"("+sku.getPrice()+")" )));
					holder.tvBilled.setVisibility(View.GONE);
					holder.tvSaved.setVisibility(View.GONE);
				} else {
					holder.tvVipPackage.setText(RichTextHelper.getRichText(String.format(getString(R.string.txt_months), monthsList[i])));
					holder.tvPrice.setText(RichTextHelper.getRichText(String
							.format(getString(R.string.txt_price_per_month), roundTwoDecimals(Double.parseDouble(sku.getPriceInMicros() ) / Integer.parseInt(monthsList[i])) + " " +sku.getCurrency() )));
					holder.tvBilled.setText(RichTextHelper.getRichText(String
							.format(getString(R.string.txt_total_bill), sku.getPriceInMicros() +  " " + sku.getCurrency()  + "("+sku.getPrice()+")" )));
					holder.tvSaved.setText(RichTextHelper.getRichText(String
							.format(getString(R.string.txt_save),
									saveRate(Double.parseDouble(priceOnMonth),
											Double.parseDouble(sku
													.getPriceInMicros()), Integer
													.parseInt(monthsList[i])))));
				}
			}else{
				if (Double.parseDouble(sku.getPriceInNumberString()) == Double.parseDouble(priceOnMonth)) {
					holder.tvVipPackage.setText(RichTextHelper.getRichText(String.format(getString(R.string.txt_month), monthsList[i])));
					holder.tvPrice.setText(RichTextHelper.getRichText(String
							.format(getString(R.string.txt_int_app_product_title),sku.getPrice())));
					holder.tvBilled.setVisibility(View.GONE);
					holder.tvSaved.setVisibility(View.GONE);
				} else {
					holder.tvVipPackage.setText(RichTextHelper.getRichText(String.format(getString(R.string.txt_months), monthsList[i])));
					holder.tvPrice.setText(RichTextHelper.getRichText(String
							.format(getString(R.string.txt_price_per_month), roundTwoDecimals( Double.parseDouble(sku.getPriceInNumberString() ) / Integer.parseInt(monthsList[i]) ) + " " +sku.getCurrencySymple())));
					holder.tvBilled.setText(RichTextHelper.getRichText(String
							.format(getString(R.string.txt_total_bill), sku.getPrice() )));
					holder.tvSaved.setText(RichTextHelper.getRichText(String
							.format(getString(R.string.txt_save),
									saveRate(Double.parseDouble(priceOnMonth),
											Double.parseDouble(sku
													.getPriceInNumberString()), Integer
													.parseInt(monthsList[i])))));
				}
			}
			final int index = i;
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					btn_get_vip_package.setEnabled(true);
					if (index != mPosition) {
						if (mPosition != -1) {
							View old = listProduct.getChildAt(mPosition);
							RadioButton buttonOld = (RadioButton) old
									.findViewById(R.id.radio_get_vip_package);
							if (buttonOld != null) {
								buttonOld.setChecked(false);
							}
						}
						mPosition = index;
						RadioButton button = (RadioButton) v
								.findViewById(R.id.radio_get_vip_package);
						if (button != null) {
							button.setChecked(true);
						}
					}
				}
			});
			listProduct.addView(convertView);
			i++;
		}
	}

	IabHelper.GetSkuFinishedListener mGetAllSkuDetailListener = new IabHelper.GetSkuFinishedListener() {

		@Override
		public void onGetSkuFinishedListener(ArrayList<SkuDetails> skuList) {
			if (skuList == null || (skuList != null && skuList.size() == 0)) {
				tvLoadProductListFailed.setVisibility(View.VISIBLE);
				btn_get_vip_package.setEnabled(false);
			} else {
				productList = CheckListProduct(skuList);
				viewProductList(productList, monthsList);
				btn_get_vip_package.setEnabled(true);
			}
			if (pd != null && pd.isShowing()) {
				pd.dismiss();
				pd = null;
			}
		}
	};
	IabHelper.QueryInventoryFinishedListener mGotInventoryToConsumeListener = new IabHelper.QueryInventoryFinishedListener() {
		public void onQueryInventoryFinished(IabResult result,
				Inventory inventory) {
			btn_get_vip_package.setEnabled(true);
			if (mHelper == null)
				return;
			if (result.isFailure()) {
				return;
			}

			if (inventory.getPurchase(SKU_ID) != null) {
				mHelper.consumeAsync(inventory.getPurchase(SKU_ID),
						mConsumeFinishedListener);
			} else {
				Purchase(SKU_ID);
			}
		}
	};

	/** Verifies the developer payload of a purchase. */
	boolean verifyDeveloperPayload(Purchase p) {
		String payload = p.getDeveloperPayload();
		Log.d("getPackageName", getPackageName());
		Log.d("getPackageName", p.getPackageName());
		Log.d("SKU", SKU_ID);
		Log.d("sku",  p.getSku());
		Log.d("payload", this.payload);
		Log.d("payload", payload);
		if (payload.equals(this.payload) && p.getSku().equals(SKU_ID)
				&& p.getPackageName().equals(getPackageName())) {
			return true;
		}
		return false;
	}

	// Callback for when a purchase is finished
	IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
		public void onIabPurchaseFinished(IabResult result, final Purchase purchase) {
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
				// enableDialogWarning(activity,
				// activity.getString(R.string.txt_vip_room),
				// activity.getString(R.string.txt_purchase_cancel));
				return;
			}
			if (!verifyDeveloperPayload(purchase)) {
				enableDialogWarning(PurchaseActivity.this,
						getString(R.string.txt_vip_room),
						getString(R.string.txt_purchase_error));
				return;
			}

			Log.d(TAG, "Purchase successful.");
			// xac nhan la VIP user len server
			(new Handler()).postDelayed(new Runnable() {
				@Override
				public void run() {
					pd = new ProgressDialog(PurchaseActivity.this);
					pd.setMessage(getString(R.string.txt_loading));
					pd.setCancelable(false);
					pd.show();

					SendVIPRegister loader = new SendVIPRegister(
							Constants.VIP_REGISTER, PurchaseActivity.this, purchase);
					getRequestQueue().addRequest(loader);
				}
			}, 0);
			
		}
	};

	// Called when consumption is complete
	IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
		public void onConsumeFinished(Purchase purchase, IabResult result) {
			btn_get_vip_package.setEnabled(true);
			Log.d(TAG, "Consumption finished. Purchase: " + purchase
					+ ", result: " + result);
			if (mHelper == null)
				return;

			if (result.isSuccess()) {
				Log.d(TAG, "Consumption successful. Provisioning.");
				Purchase(SKU_ID);
			} else {
				enableDialogWarning(PurchaseActivity.this,
						getString(R.string.txt_vip_room),
					getString(R.string.abnormal_error_message));
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
				if (OakClubUtil.isInternetAccess(PurchaseActivity.this)) {
					if (mPosition != -1 && mPosition < productList.size()) {
						SKU_ID = productList.get(mPosition).getSku();
						price = productList.get(mPosition).getPrice();
					}
					if (SKU_ID != "") {
						if (mHelper != null) {
							mHelper.queryInventoryAsync(mGotInventoryToConsumeListener);
						} else {
							btn_get_vip_package.setEnabled(true);
							enableDialogWarning(
									PurchaseActivity.this,
									getString(R.string.txt_vip_room),
									getString(R.string.abnormal_error_message));
						}
					}
				} else {
					AlertDialog.Builder builder;
					builder = new AlertDialog.Builder(PurchaseActivity.this);
					final AlertDialog dialog = builder.create();
					LayoutInflater inflater = LayoutInflater.from(PurchaseActivity.this);
					View layout = inflater.inflate(R.layout.dialog_warning_ok,
							null);
					dialog.setView(layout, 0, 0, 0, 0);
					Button btnOK = (Button) layout
							.findViewById(R.id.dialog_internet_access_lltfooter_btOK);
					btnOK.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							dialog.dismiss();
							finish();
						}
					});
					dialog.setCancelable(false);
					dialog.show();
				}
				break;
			case R.id.btn_purchasing_back:
				finish();
				break;
			default:
				break;
			}
		}
	};

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
			Log.d("purchasing token", purchase.getToken());
			obj = oakClubApi.SendVIPRegister(purchase.getSku(),
					purchase.getToken());
		}

		@Override
		public void executeUI(Exception ex) {
			if(pd != null && pd.isShowing()){
				pd.dismiss();
				pd = null;
			}
			if (obj != null && obj.isStatus()) {
				// popup purchase seccess
				if (ProfileSettingFragment.profileInfoObj != null) {
					ProfileSettingFragment.profileInfoObj.setIs_vip(true);
				}
				Intent intent = new Intent(PurchaseActivity.this,
						PurchaseConfirmedActivity.class);
				for (SkuDetails sku : productList) {
					if (sku.getSku().equals(purchase.getSku())) {
						price = sku.getPrice();
						break;
					}
				}
				intent.putExtra(Constants.PURCHASE_PRICE, price);
				intent.putExtra(Constants.TRANSACTION_ID, purchase.getOrderId());
				startActivity(intent);
				finish();
			} else {
				enableDialogWarning(PurchaseActivity.this,
						getString(R.string.txt_vip_room),
						getString(R.string.abnormal_error_message));
			}
		}

	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(mHelper != null){
			mHelper.handleActivityResult(requestCode, resultCode, data);
		}
	}
	
	@Override
	protected void onDestroy() {
		if (mHelper != null) {
    		mHelper.dispose();
    		mHelper = null;
        }
		super.onDestroy();
	}
}
