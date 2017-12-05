package com.exemple.android.popularmovies.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.exemple.android.popularmovies.R;
import com.exemple.android.popularmovies.model.Movie;
import com.exemple.android.popularmovies.model.PosterSize;
import com.exemple.android.popularmovies.service.MovieDbApiFactory;
import com.exemple.android.popularmovies.utils.DateUtils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailActivity extends AppCompatActivity {

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
    private Movie mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        if (getIntent().hasExtra(EXTRA_MOVIE)) {
            mMovie = getIntent().getParcelableExtra(EXTRA_MOVIE);
        }

        displayMovieDetails();
    }

    @SuppressLint("StringFormatInvalid")
    private void displayMovieDetails() {
        String moviePosterPath = MovieDbApiFactory.getPosterUrl(mMovie.getPosterPath(), PosterSize.w185);
        Picasso.with(this).load(moviePosterPath).into(mMoviePoster);
        mMovieTitle.setText(mMovie.getTitle());
        mMovieReleadeDate.setText(DateUtils.formatDate(mMovie.getReleaseDate()));
        mMovieVote.setText(getString(R.string.movie_vote_format, mMovie.getVoteAverange()));
        mMovieOverview.setText(mMovie.getOverview());
    }
}
