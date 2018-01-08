package com.exemple.android.popularmovies.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.exemple.android.popularmovies.R;
import com.exemple.android.popularmovies.adapter.MovieAdapter;
import com.exemple.android.popularmovies.data.MovieContract;
import com.exemple.android.popularmovies.model.Movie;
import com.exemple.android.popularmovies.model.PageableList;
import com.exemple.android.popularmovies.service.FilterMovieType;
import com.exemple.android.popularmovies.service.MovieDbApiFactory;
import com.exemple.android.popularmovies.service.MovieDbService;
import com.exemple.android.popularmovies.utils.EndlessRecyclerViewScrollListener;
import com.exemple.android.popularmovies.utils.MovieUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements MovieAdapter.MovieAdapterOnClickHandler {

    private static final int FIRST_PAGE = 1;
    private static String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.rv_movie_posters)
    RecyclerView mMovieRecyclerView;
    @BindView(R.id.load_movie_erro_button)
    Button mReloadMovieButton;
    @BindView(R.id.load_movie_erro_frame)
    ConstraintLayout mLoadMovieErroFrame;
    @BindView(R.id.pb_loading_movies)
    ProgressBar mProgressBarLoadingMovies;

    private MovieAdapter mMovieAdapter;
    private EndlessRecyclerViewScrollListener mEndlessScrollListener;
    private @FilterMovieType.MovieType int mloadedMovieType;
    private Callback<PageableList<Movie>> retrofitMovieCallback = new Callback<PageableList<Movie>>() {
        @Override
        public void onResponse(Call<PageableList<Movie>> call, Response<PageableList<Movie>> response) {
            mProgressBarLoadingMovies.setVisibility(View.GONE);
            if (response.isSuccessful()) {
                displayErrorFrame(false);
                if (response.body().getPage() == FIRST_PAGE) {
                    mMovieAdapter.setMovieList(response.body().getList());
                    mEndlessScrollListener.resetState();
                } else {
                    mMovieAdapter.addMovies(response.body().getList());
                }
            }
        }

        @Override
        public void onFailure(Call<PageableList<Movie>> call, Throwable t) {
            Log.e(TAG, t.getMessage());
            mProgressBarLoadingMovies.setVisibility(View.GONE);
            displayErrorFrame(true);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mloadedMovieType = FilterMovieType.POPULAR;

        configureRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMovieData(mloadedMovieType, FIRST_PAGE);
    }

    private void loadMovieData(@FilterMovieType.MovieType int movieType, int pageNumber) {
        //only show loading progress bar on first load
        if (pageNumber == FIRST_PAGE) {
            mMovieRecyclerView.setVisibility(View.GONE);
            mProgressBarLoadingMovies.setVisibility(View.VISIBLE);
        }

        switch (movieType) {
            case FilterMovieType.POPULAR:
                loadPopularMovieData(pageNumber);
                break;
            case FilterMovieType.TOP_RATED:
                loadTopRatedMovieData(pageNumber);
                break;
            case FilterMovieType.FAVORIRED:
                loadFavoritedMovieData();
                break;
        }
    }

    private void loadFavoritedMovieData() {
        Cursor query = getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                MovieContract.MovieEntry.COLUMN_TITLE);

        List<Movie> movies = MovieUtils.movieListFrom(query);
        mProgressBarLoadingMovies.setVisibility(View.GONE);
        displayErrorFrame(false);
        mMovieAdapter.setMovieList(movies);
    }

    private void loadPopularMovieData(int pageNumber) {
        MovieDbService movieDbService = MovieDbApiFactory.getMovieDbService(this);
        movieDbService.getPopularMovies(pageNumber).enqueue(retrofitMovieCallback);
    }

    private void loadTopRatedMovieData(int pageNumber) {
        MovieDbService movieDbService = MovieDbApiFactory.getMovieDbService(this);
        movieDbService.getTopRatedMovies(pageNumber).enqueue(retrofitMovieCallback);
    }

    private void configureRecyclerView() {
        mMovieAdapter = new MovieAdapter(this, this);

        mMovieRecyclerView.setAdapter(mMovieAdapter);
        mMovieRecyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager =
                new GridLayoutManager(this, getResources().getInteger(R.integer.grid_spam_count));
        mMovieRecyclerView.setLayoutManager(gridLayoutManager);

        mEndlessScrollListener = new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (mloadedMovieType != FilterMovieType.FAVORIRED) {
                    loadMovieData(mloadedMovieType, page);
                }
            }
        };
        mMovieRecyclerView.addOnScrollListener(mEndlessScrollListener);
    }

    private void displayErrorFrame(boolean displayFrame) {
        if (displayFrame) {
            mLoadMovieErroFrame.setVisibility(View.VISIBLE);
            mMovieRecyclerView.setVisibility(View.GONE);
        } else {
            mLoadMovieErroFrame.setVisibility(View.GONE);
            mMovieRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filter_popular_movies:
                item.setChecked(true);
                mloadedMovieType = FilterMovieType.POPULAR;
                loadMovieData(mloadedMovieType, FIRST_PAGE);
                break;
            case R.id.action_filter_top_rated:
                item.setChecked(true);
                mloadedMovieType = FilterMovieType.TOP_RATED;
                loadMovieData(mloadedMovieType, FIRST_PAGE);
                break;
            case R.id.action_filter_favorited:
                item.setChecked(true);
                mloadedMovieType = FilterMovieType.FAVORIRED;
                loadMovieData(mloadedMovieType, FIRST_PAGE);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.load_movie_erro_button)
    public void onClickReloadButton() {
        displayErrorFrame(false);
        loadPopularMovieData(FIRST_PAGE);
    }

    @Override
    public void onClickMovie(Movie movie) {
        Intent detailIntent = new Intent(this, MovieDetailActivity.class);
        detailIntent.putExtra(MovieDetailActivity.EXTRA_MOVIE, movie);
        startActivity(detailIntent);
    }
}
