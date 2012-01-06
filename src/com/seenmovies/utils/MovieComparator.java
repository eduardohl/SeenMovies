package com.seenmovies.utils;

import java.util.Comparator;

import com.seenmovies.jtmdb.Movie;

public class MovieComparator implements Comparator<Movie>{

	@Override
	public int compare(Movie movie, Movie otherMovie) {
		return movie.getName().compareTo(otherMovie.getName());
	}
}
