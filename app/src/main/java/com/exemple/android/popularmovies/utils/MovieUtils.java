package com.exemple.android.popularmovies.utils;

import android.content.ContentValues;

import com.exemple.android.popularmovies.data.MovieContract.MovieEntry;
import com.exemple.android.popularmovies.model.Movie;

public class MovieUtils {

    public static ContentValues convertTo(Movie movie){
        ContentValues values = new ContentValues();
        values.put(MovieEntry._ID, movie.getId());
        values.put(MovieEntry.COLUMN_TITLE, movie.getTitle());
        values.put(MovieEntry.COLUMN_ORIGINAL_TITLE, movie.getOriginalTitle());
        values.put(MovieEntry.COLUMN_VOTE_AVERANGE, movie.getVoteAverange());
        values.put(MovieEntry.COLUMN_POSTER_PATH, movie.getPosterPath());
        values.put(MovieEntry.COLUMN_RELEASE_DATE, DateUtils.dateToLong(movie.getReleaseDate()));
        values.put(MovieEntry.COLUMN_OVERVIEW, movie.getOverview());

        return values;
    }
}
