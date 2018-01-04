package com.exemple.android.popularmovies.service;

import com.exemple.android.popularmovies.model.Movie;
import com.exemple.android.popularmovies.model.PageableList;
import com.exemple.android.popularmovies.model.Review;
import com.exemple.android.popularmovies.model.Video;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieDbService {

    String POPULAR_MOVIE_PATH = "movie/popular";
    String TOP_RATED_MOVIE_PATH = "movie/top_rated";
    String TRAILER_PATH = "movie/{movie_id}/videos";
    String REVIEW_PATH = "movie/{movie_id}/reviews";

    String PAGE_NUMBER_PARAM = "page";
    String MOVIE_ID_PATH_PARAM = "movie_id";

    @GET(POPULAR_MOVIE_PATH)
    Call<PageableList<Movie>> getPopularMovies(@Query(PAGE_NUMBER_PARAM) int pageNumber);

    @GET(TOP_RATED_MOVIE_PATH)
    Call<PageableList<Movie>> getTopRatedMovies(@Query(PAGE_NUMBER_PARAM) int pageNumber);

    @GET(TRAILER_PATH)
    Call<PageableList<Video>> getMovieTrailers(@Path(MOVIE_ID_PATH_PARAM) int movieId);

    @GET(REVIEW_PATH)
    Call<PageableList<Review>> getMovieReviews(@Path(MOVIE_ID_PATH_PARAM) int movieId);
}
