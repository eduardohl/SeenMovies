package com.seenmovies;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.seenmovies.jtmdb.BrowseOptions;
import com.seenmovies.jtmdb.Movie;
import com.seenmovies.jtmdb.MoviePoster.Size;
import com.seenmovies.utils.ConnectionUtils;
import com.seenmovies.utils.MovieComparator;
import com.seenmovies.utils.MovieUtils;

public class RegisterMovieActivity extends Activity {

	private EditText mQueryMovieName;
	private ListView mReturnedMoviesListView;
	private Button mSearchMoviesButton;
	private MovieListAdapter mMovieListAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registermovie);

		//Initialize returned Movies List
		mReturnedMoviesListView = (ListView) findViewById(R.id.returnedMoviesList);
		mReturnedMoviesListView.setClickable(true);
		mReturnedMoviesListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int posicao, long id) {
				Intent intent = new Intent(RegisterMovieActivity.this, MovieInfoActivity.class);
				intent.putExtra(MovieUtils.MOVIE_EXTRA, mMovieListAdapter.getItem(posicao));
				startActivity(intent);
			}
		});
		mMovieListAdapter = new MovieListAdapter(this, R.layout.movie_row, new ArrayList<Movie>());
		mReturnedMoviesListView.setAdapter(mMovieListAdapter);

		//Initialize search movies button
		mSearchMoviesButton = (Button) findViewById(R.id.search_movies_button);
		mSearchMoviesButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				searchMovies();
			}
		});

		//Initialize search edittext box
		mQueryMovieName = (EditText) findViewById(R.id.movieNameEditText);
		mQueryMovieName.setHint(R.string.type_movie_name);
		mQueryMovieName.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if((event.getAction() == KeyEvent.ACTION_DOWN) && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)){
					searchMovies();
					return true;
				}
				return false;
			}
		});
	}

	public void searchMovies(){
		new RetrieveMoviesByName().execute(null);
		mSearchMoviesButton.setVisibility(View.GONE);
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

	private class RetrieveMoviesByName extends AsyncTask<Object, Integer, Object>{

		private ProgressDialog progressDialog;
		private List<Movie> returnedMovieList;

		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(RegisterMovieActivity.this);
			progressDialog.setTitle(R.string.please_wait);
			progressDialog.setMessage(getResources().getString(R.string.searching_for_movies));
			progressDialog.show();
		}

		@Override
		protected Object doInBackground(Object... params) {
			BrowseOptions bo = new BrowseOptions();
			bo.setQuery(mQueryMovieName.getEditableText().toString());
			try {
				returnedMovieList = Movie.browse(bo);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Object result) {
			for (Movie movie : returnedMovieList){
				new MovieThumbnailSetter().execute(movie);
			}
			progressDialog.dismiss();	
		}
	}
	
	private class MovieThumbnailSetter extends AsyncTask<Movie, Integer, Object>{

		Movie movie;
		
		@Override
		protected Object doInBackground(Movie... params) {
			movie = params[0];
			new MovieUtils().setMovieImage(RegisterMovieActivity.this, movie, Size.THUMB);
			return null;
		}
		
		@Override
		protected void onPostExecute(Object result) {
			mMovieListAdapter.add(movie);
			mMovieListAdapter.sort(new MovieComparator());
		}
	}
}
