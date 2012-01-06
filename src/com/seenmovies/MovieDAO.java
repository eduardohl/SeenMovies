package com.seenmovies;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.seenmovies.jtmdb.Movie;
import com.seenmovies.jtmdb.MoviePoster.Size;
import com.seenmovies.utils.MovieUtils;

public class MovieDAO extends SQLiteOpenHelper{

	private static final int VERSAO = 1;
	private static final String TABELA = "Movie";
	
	private static final String ID = "id";
	private static final String NAME = "name";
	private static final String THUMBNAIL_PATH = "thumbnailPath";
	private static final String IMDB_ID = "imdbid";
	private static final String FAVORITE = "favorite";
	
	private static final String[] COLUMNS = {ID, NAME, THUMBNAIL_PATH, IMDB_ID, FAVORITE};

	public static MovieDAO getInstance(Context context){
		return new MovieDAO(context);
	}
	
	private MovieDAO(Context context) {
		super(context, TABELA, null, VERSAO);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TABLE " + TABELA + " ")
		.append("( " + ID + " integer primary key, ")
		.append( NAME + " text not null, ")
		.append( THUMBNAIL_PATH + " text null, ")
		.append(IMDB_ID + " text null, ")
		.append(FAVORITE + " integer null)");
		db.execSQL(sb.toString());
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		StringBuilder sb = new StringBuilder();
		sb.append("DROP TABLE IF EXISTS " + TABELA);
		db.execSQL(sb.toString());
		onCreate(db);
	}

	public void insert(Movie movie){
		MovieUtils movieUtils = new MovieUtils();
		ContentValues values = new ContentValues();
		values.put(ID, movie.getID());
		values.put(NAME, movie.getName());
		if (movieUtils.hasImage(movie, Size.THUMB)){
			values.put(THUMBNAIL_PATH, movieUtils.getImagePath(movie, Size.THUMB));
		}
		values.put(IMDB_ID, movie.getImdbID());
		values.put(FAVORITE, movie.getFavorite());
		getWritableDatabase().insert(TABELA, null, values);
	}
	
	public void remove(Movie movie) {
		getWritableDatabase().delete(TABELA, "id=?", new String[] {Long.toString(movie.getID())});
	}
	
	public List<Integer> getAllMoviesIds(){
		List<Integer> movieIds = new ArrayList<Integer>();
		Cursor c = getReadableDatabase().query(TABELA, COLUMNS, null, null, null, null, null);
		try{
			while(c.moveToNext()){
				movieIds.add(c.getInt(0));
			}
		} catch (Exception e){
			Log.e("seenm", "[MovieDAO] -> Error getting movies Ids from db");
		} finally {
			c.close();
		}
		return movieIds;
	}
	
	public List<Movie> getAllMovies(){
		List<Movie> movies = new ArrayList<Movie>();
		Cursor c = getReadableDatabase().query(TABELA, COLUMNS, null, null, null, null, NAME);
		try{
			while(c.moveToNext()){
				movies.add(new Movie(c.getInt(0), c.getString(1), c.getString(2), c.getString(3), c.getInt(4)));
			}
		} catch (Exception e){
			Log.e("seenm", "[MovieDAO] -> Error getting movies Ids from db");
		} finally {
			c.close();
		}
		return movies;
	}
}
