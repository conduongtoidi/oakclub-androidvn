package com.oakclub.android;

import java.util.ArrayList;
import java.util.Locale;

import com.oakclub.android.util.Constants;
import com.oakclub.android.util.OakClubUtil;
import com.oakclub.android.view.RadioButtonCustom;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager.LayoutParams;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class ChooseLanguageActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setTheme(android.R.style.Theme_Holo_NoActionBar);
		super.onCreate(savedInstanceState);
		SharedPreferences pref = getApplicationContext().getSharedPreferences(Constants.PREFERENCE_NAME, 0);
		int currentId = pref.getInt(Constants.PREFERENCE_LANGUAGE, -1);
		showLanguagueDialog(currentId);
	}
	
	public void showLanguagueDialog(final int currentId){
		final String[] stringList = getResources()
				.getStringArray(R.array.language_list);
		final ArrayList<RadioButton> radioButtons;
		AlertDialog.Builder builder;
		builder = new AlertDialog.Builder(ChooseLanguageActivity.this);
		final AlertDialog dialog = builder.create();
		final LayoutInflater inflater = LayoutInflater.from(this);
		View mainRelativeLayout = inflater.inflate(
				R.layout.dialog_choose_language, null);
		dialog.setView(mainRelativeLayout,0,0,0,0);
		
		final RadioGroup radioGroup = (RadioGroup)mainRelativeLayout.findViewById(R.id.radioGroup1);
		radioGroup.setOrientation(RadioGroup.VERTICAL);
		radioButtons = new ArrayList<RadioButton>();
		for (int i = 0; i < stringList.length; i++) {
			RadioButtonCustom radio = new RadioButtonCustom(getApplicationContext(), getResources().getDrawable(R.drawable.radiogroup_selector2));
			radio.setText(stringList[i]);
			radio.setTextColor(Color.BLACK);
            radio.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
			radio.setLayoutParams(new RadioGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			int padding = (int) OakClubUtil.convertDpToPixel(12,this);
			radio.setPadding(padding, padding, padding, padding);
			radioButtons.add(radio);
			radioGroup.addView(radio);
			ImageView separator = new ImageButton(this);
			separator.setBackgroundResource(R.drawable.separators);
			separator.setLayoutParams(new RadioGroup.LayoutParams(LayoutParams.MATCH_PARENT, 1));
			radioGroup.addView(separator);
			if (i==currentId){
				radio.setChecked(true);
			}
		}
		
		Button okBtn;
		okBtn = (Button)mainRelativeLayout.findViewById(R.id.ok_btn);
		okBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				int selectedButtonId = radioGroup
						.getCheckedRadioButtonId();
				int selectedId = -1;
				for (int i = 0; i < radioButtons.size(); i++) {
					if (radioButtons.get(i).getId() == selectedButtonId) {
						selectedId = i;
						break;
					}
				}
				if (currentId!=selectedId){
					switch (selectedId){
				    case 0:
                        setLocale("en");
                        break;
                    case 1:
                        setLocale("vi");
                        break;
                    case 2:
                        setLocale("de");
                        break;
                    case 3:
                        setLocale("id");
                        break;
                    case 4:
                        setLocale("th");
                        break;
                    case 5:
                        setLocale("ru");
                        break;
                    case 6:
                        setLocale("es");
                        break;
                    case 7:
                        setLocale("fr");
                        break;
                    case 8:
                        setLocale("tr");
                        break;
                    case 9:
                        setLocale("da");
                        break;
                    case 10:
                        setLocale("nb");
                        break;
                    case 11:
                        setLocale("sv");
                        break;
                    case 12:
                        setLocale("ro");
                        break;
                    case 13:
                        setLocale("pl");
                        break;
                    case 14:
                        setLocale("bg");
                        break;
                    case 15:
                        setLocale("hu");
                        break;
                    case 16:
                        setLocale("hr");
                        break;
                    case 17:
                        setLocale("el");
                        break;
                    case 18:
                        setLocale("nl");
                        break;
                    case 19:
                        setLocale("ar");
                        break;
                    case 20:
                        setLocale("ko");
                        break;
                    case 21:
                        setLocale("sw");
                        break;
                    case 22:
                        setLocale("ph");
                        break;
                    default:
                        setLocale("en");
                        break;
                    }
                    
					Editor editor = getSharedPreferences(
							Constants.PREFERENCE_NAME, 0)
							.edit();
					editor.putInt(
							Constants.PREFERENCE_LANGUAGE, selectedId);
					editor.commit();
				}
				
				Intent intent = new Intent(ChooseLanguageActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
				dialog.dismiss();
			}
		});	
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();
	}
    public void setLocale(String lang) {
        Locale myLocale;
        myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
//        Intent refresh = new Intent(this, AndroidLocalize.class);
//        startActivity(refresh);
    }
}
