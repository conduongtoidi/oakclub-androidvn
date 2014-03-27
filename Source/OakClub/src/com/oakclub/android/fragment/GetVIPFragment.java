package com.oakclub.android.fragment;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
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
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.oakclub.android.PurchaseConfirmedActivity;
import com.oakclub.android.R;
import com.oakclub.android.SlidingActivity;
import com.oakclub.android.core.RequestUI;
import com.oakclub.android.model.IabResult;
import com.oakclub.android.model.Inventory;
import com.oakclub.android.model.Purchase;
import com.oakclub.android.model.SenVIPRegisterReturnObject;
import com.oakclub.android.model.SkuDetails;
import com.oakclub.android.util.Constants;
import com.oakclub.android.util.IabHelper;
import com.oakclub.android.util.OakClubUtil;
import com.oakclub.android.util.RichTextHelper;
import com.oakclub.android.view.TextViewWithFont;

public class GetVIPFragment {
	private Button btn_get_vip_package;
	private ImageView vip_logo;
	// private TextView get_vip_title;
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

	// (arbitrary) request code for the purchase flow
	static final int RC_REQUEST = 10001;
	SlidingActivity activity;

	public GetVIPFragment(SlidingActivity activity) {
		this.activity = activity;
	}

	public void initGetVIP() {
		activity.init(R.layout.activity_get_vip);
		listProduct = (LinearLayout) activity.findViewById(R.id.list_product);

		btn_get_vip_package = (Button) activity.findViewById(R.id.btn_get_vip);
		tvLoadProductListFailed = (TextViewWithFont) activity
				.findViewById(R.id.error_load_products_title);
		btn_get_vip_package.setOnClickListener(buttonClick);
		// btn_get_vip_package.setEnabled(false);
		DisplayMetrics dimension = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dimension);
		int height = dimension.heightPixels;
		int padding = height / 55;

		vip_logo = (ImageView) activity.findViewById(R.id.vip_logo);
		vip_logo.setPadding(0, 0, padding, 0);
		monthsList = activity.getResources()
				.getStringArray(R.array.months_list);

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
						mHelper.getAllSKU(IabHelper.ITEM_TYPE_INAPP,
								mGetAllSkuDetailListener);
					}
				});
			}
		}, 1000);

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
			mHelper.launchPurchaseFlow(activity, SKU_ID, RC_REQUEST,
					mPurchaseFinishedListener, payload);
		} else {
			btn_get_vip_package.setEnabled(true);
			enableDialogWarning(activity,
					activity.getString(R.string.txt_vip_room),
					activity.getString(R.string.abnormal_error_message));
		}

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

	private static class ListProductHolder {
		public TextView tvVipPackage;
		public TextView tvPrice;
		public TextView tvBilled;
		public TextView tvSaved;
		public RadioButton rdButton;
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
		int i = 0;
		String priceOnMonth = "1";
		double minPrice = 100000;
		for (SkuDetails sku : productList) {
			if (Double.parseDouble(sku.getPriceInMicros()) < minPrice) {
				minPrice = Double.parseDouble(sku.getPriceInMicros());
			}
		}
		priceOnMonth = minPrice + "";
		for (SkuDetails sku : productList) {
			if (i >= monthsList.length)
				return;
			LayoutInflater mInflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

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
			if( i == 0){
				holder.rdButton.setChecked(true);
				mPosition = i;
			}
			holder.rdButton.setClickable(false);
			holder.tvVipPackage.setText(RichTextHelper.getRichText("{{b}}"
					+ sku.getTitle() + "{{/b}}"));
			if (sku.getPriceInMicros().equals(priceOnMonth)) {
				holder.tvPrice.setText(RichTextHelper.getRichText(String
						.format(activity.getString(R.string.txt_int_app_product_price), sku.getPrice() )));
				RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.tvPrice
						.getLayoutParams();
				layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT,
						RelativeLayout.TRUE);
				holder.tvPrice.setLayoutParams(layoutParams);
				holder.tvBilled.setVisibility(View.GONE);
				holder.tvSaved.setVisibility(View.GONE);
			} else {
				holder.tvPrice.setText(RichTextHelper.getRichText(String
						.format(activity
								.getString(R.string.txt_int_app_product_price), sku.getPrice())));

				holder.tvSaved.setText(RichTextHelper.getRichText(String
						.format(activity.getString(R.string.txt_save),
								saveRate(Double.parseDouble(priceOnMonth),
										Double.parseDouble(sku
												.getPriceInMicros()), Integer
												.parseInt(monthsList[i])))));
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
		if (payload.equals(this.payload) && p.getSku().equals(SKU_ID)
				&& p.getPackageName().equals(activity.getPackageName())) {
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
				// enableDialogWarning(activity,
				// activity.getString(R.string.txt_vip_room),
				// activity.getString(R.string.txt_purchase_cancel));
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
			btn_get_vip_package.setEnabled(true);
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
									activity,
									activity.getString(R.string.txt_vip_room),
									activity.getString(R.string.abnormal_error_message));
						}
					} else {
						btn_get_vip_package.setEnabled(true);
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
			obj = activity.oakClubApi.SendVIPRegister(purchase.getSku(),
					purchase.getToken());
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
				for (SkuDetails sku : productList) {
					if (sku.getSku().equals(purchase.getSku())) {
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
