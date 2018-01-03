package com.exemple.android.popularmovies.service;

import com.exemple.android.popularmovies.model.Movie;
import com.exemple.android.popularmovies.model.PageableList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieDbService {

    String POPULAR_MOVIE_PATH = "movie/popular";
    String TOP_RATED_MOVIE_PATH = "movie/top_rated";
    String PAGE_NUMBER_PARAM = "page";

    @GET(POPULAR_MOVIE_PATH)
    Call<PageableList<Movie>> getPopularMovies(@Query(PAGE_NUMBER_PARAM) int pageNumber);

    @GET(TOP_RATED_MOVIE_PATH)
    Call<PageableList<Movie>> getTopRatedMovies(@Query(PAGE_NUMBER_PARAM) int pageNumber);
}
