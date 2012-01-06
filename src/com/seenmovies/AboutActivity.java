package com.seenmovies;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class AboutActivity extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		
		ImageButton aboutTmdbIcon = (ImageButton) findViewById(R.id.about_icon);
		aboutTmdbIcon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				Intent aboutTmdbExternalWebsite = new Intent(Intent.ACTION_VIEW);
				aboutTmdbExternalWebsite.setData(Uri.parse("http://www.themoviedb.org/"));
				startActivity(aboutTmdbExternalWebsite);
			}
		});
	}
}
