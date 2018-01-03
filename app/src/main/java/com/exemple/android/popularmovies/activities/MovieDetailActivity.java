package com.exemple.android.popularmovies.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.exemple.android.popularmovies.R;
import com.exemple.android.popularmovies.adapter.VideoAdapter;
import com.exemple.android.popularmovies.model.Movie;
import com.exemple.android.popularmovies.model.PageableList;
import com.exemple.android.popularmovies.model.PosterSize;
import com.exemple.android.popularmovies.model.Video;
import com.exemple.android.popularmovies.service.MovieDbApiFactory;
import com.exemple.android.popularmovies.service.MovieDbService;
import com.exemple.android.popularmovies.utils.DateUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailActivity extends AppCompatActivity implements VideoAdapter.VideoAdapterOnClickHandler {

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

    private Movie mMovie;
    private VideoAdapter mVideoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        if (getIntent().hasExtra(EXTRA_MOVIE)) {
            mMovie = getIntent().getParcelableExtra(EXTRA_MOVIE);
        }

        displayMovieDetails();

        configureRecylerView();
        loadMovieTrailers();
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

    private void configureRecylerView() {
        mVideoAdapter = new VideoAdapter(this);

        mMovieTrailersRecylerView.setAdapter(mVideoAdapter);
        mMovieTrailersRecylerView.setHasFixedSize(true);
        mMovieTrailersRecylerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadMovieTrailers() {
        MovieDbService movieDbService = MovieDbApiFactory.getMovieDbService(this);
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

    @Override
    public void onClickTrailer(Video video) {
        Intent trailerIntent = new Intent(Intent.ACTION_VIEW);
        trailerIntent.setData(MovieDbApiFactory.buildVideoUri(video.getKey()));

        startActivity(trailerIntent);
    }

    private void showTrailers() {
        mVideoTitle.setVisibility(View.VISIBLE);
        mMovieTrailersRecylerView.setVisibility(View.VISIBLE);
    }

    private void hideTrailers() {
        mVideoTitle.setVisibility(View.GONE);
        mMovieTrailersRecylerView.setVisibility(View.GONE);
    }
}
