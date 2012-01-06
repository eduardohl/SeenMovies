package com.seenmovies;

import java.io.Serializable;
import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.seenmovies.jtmdb.Movie;

public class MovieListAdapter extends ArrayAdapter<Movie> implements Serializable {

	private List<Movie> mMoviesList;
	private Activity mContext;

	public MovieListAdapter(Activity context, int textViewResourceId, List<Movie> movies) {
		super(context, textViewResourceId, movies);
		this.mMoviesList = movies;
		this.mContext = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (view == null){
			view = mContext.getLayoutInflater().inflate(R.layout.movie_row, null);
		}

		Movie movie = mMoviesList.get(position);
		if (movie != null){
			ImageView image = (ImageView) view.findViewById(R.id.movie_poster_row);
			TextView text = (TextView) view.findViewById(R.id.movie_name_row);

			image.setImageBitmap(movie.getThumbnail());
			text.setText(movie.getName());
		}
		return view;
	}

	@Override
	public long getItemId(int position) {
		return mMoviesList.get(position).getID();
	}

	@Override
	public int getCount() {
		return (mMoviesList != null)?mMoviesList.size():0;
	}
}