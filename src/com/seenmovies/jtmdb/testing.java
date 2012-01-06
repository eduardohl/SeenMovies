package com.seenmovies.jtmdb;

import java.io.IOException;
import java.util.List;

import org.json.JSONException;

public class testing {
	public static void main(String[] args) throws IOException, JSONException {
		GeneralSettings.setApiKey("???");
		BrowseOptions bo = new BrowseOptions();
		bo.setQuery("inception");
		List<Movie> movies = Movie.browse(bo);
		System.out.println(movies.size());
	}
}
