package com.exemple.android.popularmovies.utils;

import android.content.ContentValues;
import android.database.Cursor;

import com.exemple.android.popularmovies.data.MovieContract.MovieEntry;
import com.exemple.android.popularmovies.model.Movie;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    public static List<Movie> movieListFrom(Cursor cursor){
        ArrayList<Movie> movieList = new ArrayList<>();

        while (cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex(MovieEntry._ID));
            double voteAverange = cursor.getDouble(cursor.getColumnIndex(MovieEntry.COLUMN_VOTE_AVERANGE));
            String title = cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_TITLE));
            String posterPath = cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_POSTER_PATH));
            String originalTitle = cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_ORIGINAL_TITLE));
            String overview = cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_OVERVIEW));
            long longDate = cursor.getLong(cursor.getColumnIndex(MovieEntry.COLUMN_RELEASE_DATE));
            Date date = DateUtils.formatToDate(longDate);

            Movie movie = new Movie(id, voteAverange, title, posterPath, originalTitle, overview, date);
            movieList.add(movie);
        }

        return movieList;
    }
}
