package com.seenmovies;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.seenmovies.actionbarcompat.ActionBarActivity;
import com.seenmovies.actionbarcompat.ActionBarHelperBase;
import com.seenmovies.utils.ConnectionUtils;

public class SeenMoviesActivity extends ActionBarActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dashboard);

		Button searchMovieButton = (Button) findViewById(R.id.home_btn_search);
		searchMovieButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent registerMovie = new Intent(getApplicationContext(), RegisterMovieActivity.class);
				startActivity(registerMovie);
			}
		});

		Button myMoviesButton = (Button) findViewById(R.id.home_btn_my_movies);
		myMoviesButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent myMoviesList = new Intent(getApplicationContext(), MyMoviesListActivity.class);
				startActivity(myMoviesList);
			}
		});

		Button settingsButton = (Button) findViewById(R.id.home_btn_settings);
		settingsButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent generalSettings = new Intent(getApplicationContext(), GeneralSettingActivity.class);
				startActivity(generalSettings);
			}
		});

		Button aboutButton = (Button) findViewById(R.id.home_btn_about);
		aboutButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent about = new Intent(getApplicationContext(), AboutActivity.class);
				startActivity(about);
			}
		});
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		new ConnectionUtils().checkInternetConnectivity(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.main, menu);

		// Calling super after populating the menu is necessary here to ensure that the
		// action bar helpers have a chance to handle this event.
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case ActionBarHelperBase.HOME_ITEM:
			Toast.makeText(this, "Tapped home", Toast.LENGTH_SHORT).show();
			break;

		case R.id.menu_refresh:
			Toast.makeText(this, "Fake refreshing...", Toast.LENGTH_SHORT).show();
			getActionBarHelper().setRefreshActionItemState(true);
			getWindow().getDecorView().postDelayed(
					new Runnable() {
						@Override
						public void run() {
							getActionBarHelper().setRefreshActionItemState(false);
						}
					}, 1000);
			break;

		case R.id.menu_search:
			Toast.makeText(this, "Tapped search", Toast.LENGTH_SHORT).show();
			break;

		case R.id.menu_share:
			Toast.makeText(this, "Tapped share", Toast.LENGTH_SHORT).show();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
