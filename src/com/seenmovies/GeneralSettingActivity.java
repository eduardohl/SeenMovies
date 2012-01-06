package com.seenmovies;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;

import com.seenmovies.utils.MovieUtils;

public class GeneralSettingActivity extends PreferenceActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.layout.preferences);
		
		CheckBoxPreference thumbnailSetting = (CheckBoxPreference) findPreference("thumbnail_setting");
		thumbnailSetting.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				SharedPreferences sharedPreferences = getSharedPreferences(MovieUtils.SETTINGS, MODE_PRIVATE);
				SharedPreferences.Editor editor = sharedPreferences.edit();
				CheckBoxPreference cbPreference = (CheckBoxPreference) preference;
				if (cbPreference.isChecked()){
					editor.putBoolean(MovieUtils.THUMBNAIL_SETTING_KEY, true);
				} else {
					editor.putBoolean(MovieUtils.THUMBNAIL_SETTING_KEY, false);
				}
				editor.commit();
				return true;
			}
		});
	}
}
