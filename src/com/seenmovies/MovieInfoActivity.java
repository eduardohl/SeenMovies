package com.seenmovies;

import java.text.SimpleDateFormat;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.seenmovies.jtmdb.Movie;
import com.seenmovies.jtmdb.MoviePoster.Size;
import com.seenmovies.utils.ConnectionUtils;
import com.seenmovies.utils.MovieUtils;

public class MovieInfoActivity extends Activity {

	enum Operation {
		INSERT,
		REMOVE
	}

	private Movie mMovie;
	private MovieDAO mMovieDAO;
	private Button mIveSeenThisMovieButton;
	private Button mRemoveThisMovieButton;
	private TextView mRegisteredMovieTV;
	private ImageButton mMoviePosterIV; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.movie_info);

		mMovie = (Movie) getIntent().getSerializableExtra(MovieUtils.MOVIE_EXTRA);

		new AsyncCoverLoader().execute(null);
		
		mMoviePosterIV = (ImageButton) findViewById(R.id.movie_poster);
		TextView movieNameTV = (TextView) findViewById(R.id.movie_name);
		TextView releasedDateTV = (TextView) findViewById(R.id.released_date);
		TextView movieRatingTV = (TextView) findViewById(R.id.movie_rating);
		TextView overviewTV = (TextView) findViewById(R.id.overview);

		Resources resources = this.getResources();
		String movieName = resources.getString(R.string.movie_name) + " " + mMovie.getName();
		String releasedDate = resources.getString(R.string.movie_released_date) + " " + new SimpleDateFormat("dd-MMM-yyyy").format(mMovie.getReleasedDate());
		String movieRating = resources.getString(R.string.movie_rating) + " " + mMovie.getRating() + "/10";
		String overview = resources.getString(R.string.movie_overview) + " " + mMovie.getOverview();

		movieNameTV.setText(movieName);
		releasedDateTV.setText(releasedDate);
		movieRatingTV.setText(movieRating);
		overviewTV.setText(overview);

		mIveSeenThisMovieButton = (Button) findViewById(R.id.i_ve_seen_this_movie_button);
		mRemoveThisMovieButton = (Button) findViewById(R.id.remove_movie_from_my_list);
		mRegisteredMovieTV = (TextView) findViewById(R.id.movie_already_registered);

		if(isMovieInMyList(mMovie.getID())){
			mRegisteredMovieTV.setVisibility(View.VISIBLE);
			mRemoveThisMovieButton.setVisibility(View.VISIBLE);
		} else {
			mIveSeenThisMovieButton.setVisibility(View.VISIBLE);
		}
		
		mMoviePosterIV.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse(mMovie.getUrl().toString()));
				startActivity(intent);
			}
		});

		mIveSeenThisMovieButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new AsyncMovieSaver().execute(Operation.INSERT);
			}
		});
		mRemoveThisMovieButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new AsyncMovieSaver().execute(Operation.REMOVE);
			}
		});
	}

	private boolean isMovieInMyList(int id) {
		mMovieDAO = MovieDAO.getInstance(MovieInfoActivity.this);
		boolean isMovieInMyList = (mMovieDAO.getAllMoviesIds().contains(id)) ? true : false;
		mMovieDAO.close();
		return isMovieInMyList;
	}

	private class AsyncCoverLoader extends AsyncTask<Object, Integer, Object>{

		@Override
		protected Object doInBackground(Object... params) {
			new MovieUtils().setMovieImage(MovieInfoActivity.this, mMovie, Size.COVER);
			return null;
		}
		
		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			mMoviePosterIV.setImageBitmap(mMovie.getCover());
		}
	}
	
	private class AsyncMovieSaver extends AsyncTask<Operation, Integer, Operation>{

		private Operation operation;

		@Override
		protected void onPreExecute() {
			//por spinner
			//			progressDialog = new ProgressDialog(MovieInfoActivity.this);
			//			progressDialog.setTitle("bla");
			//			progressDialog.setMessage("bli");
			//			progressDialog.show();
		}

		@Override
		protected Operation doInBackground(Operation... params) {
			operation = params[0];
			switch(operation){
			case INSERT:
				mMovieDAO = MovieDAO.getInstance(MovieInfoActivity.this);
				mMovie.setFavorite(false);
				mMovieDAO.insert(mMovie);
				mMovieDAO.close();
				break;
			case REMOVE:
				mMovieDAO = MovieDAO.getInstance(MovieInfoActivity.this);
				mMovieDAO.remove(mMovie);
				mMovieDAO.close();
				break;
			}
			return operation;
		}

		@Override
		protected void onPostExecute(Operation operation) {
			switch(operation){
			case INSERT:
				mRegisteredMovieTV.setVisibility(View.VISIBLE);
				mRemoveThisMovieButton.setVisibility(View.VISIBLE);
				mIveSeenThisMovieButton.setVisibility(View.GONE);
				//			progressDialog.dismiss();dismisspiner
				break;
			case REMOVE:
				mRegisteredMovieTV.setVisibility(View.GONE);
				mRemoveThisMovieButton.setVisibility(View.GONE);
				mIveSeenThisMovieButton.setVisibility(View.VISIBLE);
				//			progressDialog.dismiss();dismisspiner
				break;
			}
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		new ConnectionUtils().checkInternetConnectivity(this);
	}
	
}
