package com.seenmovies.utils;

import java.io.IOException;
import java.net.URL;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.seenmovies.jtmdb.Movie;
import com.seenmovies.jtmdb.MoviePoster;
import com.seenmovies.jtmdb.MoviePoster.Size;

public class MovieUtils {
	public static final String MOVIE_EXTRA = "com.seenmovies.movie_extra";
	public static final String SETTINGS = "com.seenmovies.settings";
	public static final String THUMBNAIL_SETTING_KEY = "com.seenmovies.thumbnail.setting.key";

	public void setMovieImage(Context context, Movie movie, Size size){
		SharedPreferences preferences = context.getSharedPreferences(MovieUtils.SETTINGS, Context.MODE_PRIVATE);
		if(hasThumbnailPath(movie) || hasImage(movie, size) ){
			try {
				String path = (hasThumbnailPath(movie))? movie.getThumbnailPath(): getImagePath(movie, size);
				URL imageURL = new URL(path);
				Bitmap imageBitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
				if(size.equals(Size.THUMB) && preferences.getBoolean(MovieUtils.THUMBNAIL_SETTING_KEY, true)){
					movie.setThumbnail(imageBitmap);
				}
				if(size.equals(Size.COVER)){
					movie.setCover(imageBitmap);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private boolean hasThumbnailPath(Movie movie) {
		return movie.getThumbnailPath() != null && movie.getThumbnailPath() != "";
	}

	public String getImagePath(Movie movie, Size size) {
		return movie.getImages().posters.iterator().next().getImage(size).toString();
	}

	public boolean hasImage(Movie movie, Size size) {
		return movie != null &&
			movie.getImages() != null &&
			movie.getImages().posters != null &&
			movie.getImages().posters.size() != 0 &&
			movie.getImages().posters.iterator().next() != null && 
			movie.getImages().posters.iterator().next().getImage(size) != null;
	}
}
