package com.exemple.android.popularmovies.activities;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.exemple.android.popularmovies.R;
import com.exemple.android.popularmovies.adapter.ReviewAdapter;
import com.exemple.android.popularmovies.adapter.VideoAdapter;
import com.exemple.android.popularmovies.data.MovieContract;
import com.exemple.android.popularmovies.model.Movie;
import com.exemple.android.popularmovies.model.PageableList;
import com.exemple.android.popularmovies.model.PosterSize;
import com.exemple.android.popularmovies.model.Review;
import com.exemple.android.popularmovies.model.Video;
import com.exemple.android.popularmovies.service.MovieDbApiFactory;
import com.exemple.android.popularmovies.service.MovieDbService;
import com.exemple.android.popularmovies.utils.DateUtils;
import com.exemple.android.popularmovies.utils.MovieUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailActivity extends AppCompatActivity implements
        VideoAdapter.VideoAdapterOnClickHandler, ReviewAdapter.ReviewAdapterOnClickHandler {

    private static String TAG = MovieDetailActivity.class.getSimpleName();
    public static final String EXTRA_MOVIE = "extraMovie";

    @BindView(R.id.iv_movie_poster)
    ImageView mMoviePoster;
    @BindView(R.id.tv_movie_title)
    TextView mMovieTitle;
    @BindView(R.id.tv_movie_releade_date)
    TextView mMovieReleadeDate;
    @BindView(R.id.tv_movie_vote)
    TextView mMovieVote;
    @BindView(R.id.tv_movie_overview)
    TextView mMovieOverview;
    @BindView(R.id.rv_movie_trailers)
    RecyclerView mMovieTrailersRecylerView;
    @BindView(R.id.tv_video_title)
    TextView mVideoTitle;
    @BindView(R.id.tv_review_title)
    TextView mReviewTitle;
    @BindView(R.id.rv_movie_reviews)
    RecyclerView mMovieReviewsRecyclerView;

    private MovieDbService movieDbService;
    private Movie mMovie;
    private VideoAdapter mVideoAdapter;
    private ReviewAdapter mReviewAdapter;
    private Menu mMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);
        movieDbService = MovieDbApiFactory.getMovieDbService(this);

        if (getIntent().hasExtra(EXTRA_MOVIE)) {
            mMovie = getIntent().getParcelableExtra(EXTRA_MOVIE);
        }

        displayMovieDetails();

        configureTrailersRecylerView();
        loadMovieTrailers();

        configureReviewsRecyclerView();
        loadMovieReviews();
    }

    @SuppressLint("StringFormatInvalid")
    private void displayMovieDetails() {
        String moviePosterPath = MovieDbApiFactory.getPosterUrl(mMovie.getPosterPath(), PosterSize.W185);
        Picasso.with(this).load(moviePosterPath).into(mMoviePoster);
        mMovieTitle.setText(mMovie.getTitle());
        mMovieReleadeDate.setText(DateUtils.formatDate(mMovie.getReleaseDate()));
        mMovieVote.setText(getString(R.string.movie_vote_format, mMovie.getVoteAverange()));
        mMovieOverview.setText(mMovie.getOverview());
    }

    private void configureTrailersRecylerView() {
        mVideoAdapter = new VideoAdapter(this);

        mMovieTrailersRecylerView.setAdapter(mVideoAdapter);
        mMovieTrailersRecylerView.setHasFixedSize(true);
        mMovieTrailersRecylerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadMovieTrailers() {
        movieDbService.getMovieTrailers(mMovie.getId()).enqueue(new Callback<PageableList<Video>>() {
            @Override
            public void onResponse(Call<PageableList<Video>> call, Response<PageableList<Video>> response) {
                List<Video> trailerList = response.body().getList();
                if (trailerList.isEmpty()) {
                    hideTrailers();
                } else {
                    mVideoAdapter.setVideoList(trailerList);
                    showTrailers();
                }
            }

            @Override
            public void onFailure(Call<PageableList<Video>> call, Throwable t) {
                Log.e(TAG, t.getMessage());
                hideTrailers();
            }
        });
    }

    private void configureReviewsRecyclerView() {
        mReviewAdapter = new ReviewAdapter(this);

        mMovieReviewsRecyclerView.setAdapter(mReviewAdapter);
        mMovieReviewsRecyclerView.setHasFixedSize(true);
        mMovieReviewsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadMovieReviews() {
        movieDbService.getMovieReviews(mMovie.getId()).enqueue(new Callback<PageableList<Review>>() {
            @Override
            public void onResponse(Call<PageableList<Review>> call, Response<PageableList<Review>> response) {
                List<Review> reviews = response.body().getList();
                if (reviews.isEmpty()) {
                    hideReviews();
                } else {
                    mReviewAdapter.setReviewList(reviews);
                    showReviews();
                }
            }

            @Override
            public void onFailure(Call<PageableList<Review>> call, Throwable t) {
                Log.e(TAG, t.getMessage());
                hideReviews();
            }
        });

    }

    @Override
    public void onClickTrailer(Video video) {
        Intent trailerIntent = new Intent(Intent.ACTION_VIEW);
        trailerIntent.setData(MovieDbApiFactory.buildVideoUri(video.getKey()));

        startActivity(trailerIntent);
    }

    @Override
    public void onClickReview(Review review) {
        Intent reviewIntent = new Intent(Intent.ACTION_VIEW);
        reviewIntent.setData(Uri.parse(review.getUrl()));

        startActivity(reviewIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.move_detail_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (isMovieFavorited()){
            MenuItem item = menu.findItem(R.id.action_favorite_movie);
            item.setChecked(true);
            item.setIcon(R.drawable.ic_star_black_24dp);
        }
        return true;
    }

    private boolean isMovieFavorited() {
        Uri uri = ContentUris.withAppendedId(MovieContract.MovieEntry.CONTENT_URI, mMovie.getId());
        Cursor query = getContentResolver().query(uri, null, null, null, null);

        return query != null && query.getCount() >= 1;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_favorite_movie:
                if (!item.isChecked()){
                    item.setChecked(true);
                    item.setIcon(R.drawable.ic_star_black_24dp);
                    saveFavoriteMovie();
                } else {
                    item.setChecked(false);
                    item.setIcon(R.drawable.ic_star_border_black_24dp);
                    removeFavoriteMovie();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void removeFavoriteMovie() {
        Uri uri = ContentUris.withAppendedId(MovieContract.MovieEntry.CONTENT_URI, mMovie.getId());
        getContentResolver().delete(uri, null, null);
        Toast.makeText(this, getText(R.string.remove_favorite_movie), Toast.LENGTH_SHORT).show();
    }

    private void saveFavoriteMovie() {
        ContentValues values = MovieUtils.convertTo(mMovie);
        getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, values);
        Toast.makeText(this, getText(R.string.save_favorite_movie), Toast.LENGTH_SHORT).show();
    }

    private void showTrailers() {
        mVideoTitle.setVisibility(View.VISIBLE);
        mMovieTrailersRecylerView.setVisibility(View.VISIBLE);
    }

    private void hideTrailers() {
        mVideoTitle.setVisibility(View.GONE);
        mMovieTrailersRecylerView.setVisibility(View.GONE);
    }

    private void showReviews() {
        mReviewTitle.setVisibility(View.VISIBLE);
        mMovieReviewsRecyclerView.setVisibility(View.VISIBLE);
    }

    private void hideReviews() {
        mReviewTitle.setVisibility(View.GONE);
        mMovieReviewsRecyclerView.setVisibility(View.GONE);
    }
}
