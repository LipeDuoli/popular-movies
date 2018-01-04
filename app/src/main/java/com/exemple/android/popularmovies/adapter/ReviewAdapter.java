package com.exemple.android.popularmovies.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.exemple.android.popularmovies.R;
import com.exemple.android.popularmovies.model.Review;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private ReviewAdapterOnClickHandler mClickHandler;
    private List<Review> mReviewList;

    public ReviewAdapter(ReviewAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    public interface ReviewAdapterOnClickHandler {
        void onClickReview(Review review);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.review_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Review currentReview = mReviewList.get(position);

        holder.reviewName.setText(currentReview.getAuthor());
        holder.reviewContent.setText(currentReview.getContent());
    }

    @Override
    public int getItemCount() {
        return mReviewList == null ? 0 : mReviewList.size();
    }

    public void setReviewList(List<Review> reviewList) {
        mReviewList = reviewList;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_review_name)
        TextView reviewName;
        @BindView(R.id.tv_review_content)
        TextView reviewContent;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mClickHandler.onClickReview(mReviewList.get(getAdapterPosition()));
        }
    }
}
