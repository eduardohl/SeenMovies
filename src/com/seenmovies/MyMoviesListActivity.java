package com.seenmovies;

import java.io.IOException;
import java.util.List;

import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.seenmovies.jtmdb.Movie;
import com.seenmovies.jtmdb.MoviePoster.Size;
import com.seenmovies.utils.ConnectionUtils;
import com.seenmovies.utils.MovieComparator;
import com.seenmovies.utils.MovieUtils;

public class MyMoviesListActivity extends Activity {

	private MovieDAO mMovieDAO;
	private ListView mMyMoviesListView;
	private MovieListAdapter mMoviesListAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mymovieslist);

		//Initialize My Movies List
		mMyMoviesListView = (ListView)findViewById(R.id.mymovieslist);
		mMyMoviesListView.setClickable(true);
		mMyMoviesListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
				Movie movie = null;
				try {
					movie = Movie.getInfo(mMoviesListAdapter.getItem(position).getID());
				} catch (IOException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}
				Intent movieInfoActivity = new Intent(MyMoviesListActivity.this, MovieInfoActivity.class);
				movieInfoActivity.putExtra(MovieUtils.MOVIE_EXTRA, movie);
				startActivity(movieInfoActivity);
			}
		});
		if(mMoviesListAdapter == null){
			new ReloadListAsync().execute(null);
		}

	}

	@Override
	protected void onPause() {
		super.onPause();
		//precisa de mais inteligencia pra recuperar no lugar certo?s
	}

	@Override
	protected void onResume() {
		super.onResume();
		new ConnectionUtils().checkInternetConnectivity(this);
		//precisa de mais inteligencia pra recuperar no lugar certo?
	}

	private class ReloadListAsync extends AsyncTask<Object, Integer, Object>{

		private List<Movie> myMovies;

		@Override
		protected Object doInBackground(Object... params) {
			//Load movie Ids from DB
			mMovieDAO = MovieDAO.getInstance(MyMoviesListActivity.this);
			myMovies = mMovieDAO.getAllMovies();
			mMovieDAO.close();

			return null;
		}

		@Override
		protected void onPostExecute(Object result) {
			if (myMovies.size() == 0 ){
				TextView tv = (TextView)findViewById(R.id.mymoviesmessage);
				tv.setVisibility(View.VISIBLE);
				tv.setText(R.string.empty_my_movies);
			} else {
				mMoviesListAdapter = new MovieListAdapter(MyMoviesListActivity.this, R.layout.movie_row, myMovies);
				mMyMoviesListView.setAdapter(mMoviesListAdapter);
				for(Movie movie:myMovies){
					new MovieThumbnailSetter().execute(movie);
				}
			}
		}
	}
	
	private class MovieThumbnailSetter extends AsyncTask<Movie, Integer, Object>{

		Movie movie;
		
		@Override
		protected Object doInBackground(Movie... params) {
			movie = params[0];
			new MovieUtils().setMovieImage(MyMoviesListActivity.this, movie, Size.THUMB);
			return null;
		}
		
		@Override
		protected void onPostExecute(Object result) {
			mMoviesListAdapter.notifyDataSetChanged();
		}
	}
}

