package com.exemple.android.popularmovies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.exemple.android.popularmovies.R;
import com.exemple.android.popularmovies.model.Movie;
import com.exemple.android.popularmovies.model.PosterSize;
import com.exemple.android.popularmovies.service.MovieDbApiFactory;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private Context mContext;
    private MovieAdapterOnClickHandler mClickHandler;
    private List<Movie> mMovieList;

    public MovieAdapter(Context context, MovieAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }

    public interface MovieAdapterOnClickHandler {
        void onClickMovie(Movie movie);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.movie_posters_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Movie currentMovie = mMovieList.get(position);

        String posterUrl = MovieDbApiFactory.getPosterUrl(currentMovie.getPosterPath(), PosterSize.W185);

        Picasso.with(mContext).load(posterUrl)
                .error(R.drawable.ic_error_outline_black_24dp)
                .into(holder.moviePoster);
    }

    @Override
    public int getItemCount() {
        return mMovieList == null ? 0 : mMovieList.size();
    }

    public void setMovieList(List<Movie> movieList) {
        mMovieList = movieList;
        notifyDataSetChanged();
    }

    public void addMovies(List<Movie> movieList) {
        mMovieList.addAll(movieList);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.iv_movie_poster)
        ImageView moviePoster;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mClickHandler.onClickMovie(mMovieList.get(getAdapterPosition()));
        }
    }
}
