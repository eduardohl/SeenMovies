package com.seenmovies.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;

import com.seenmovies.R;

public class ConnectionUtils {
	public void checkInternetConnectivity(final Activity activity) {
		ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
		if (activeNetworkInfo == null){
			AlertDialog.Builder builder = new AlertDialog.Builder(activity);
			builder.setMessage(R.string.no_internet_connection_text);
			builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					activity.finish();
				}
			});
			builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
					activity.startActivity(intent);
				}
			});
			AlertDialog dialog = builder.create();
			dialog.setTitle(R.string.no_internet_connection_title);
			dialog.show();
		}
	}
}
