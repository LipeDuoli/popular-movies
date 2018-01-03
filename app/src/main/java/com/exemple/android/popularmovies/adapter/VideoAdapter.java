package com.exemple.android.popularmovies.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.exemple.android.popularmovies.R;
import com.exemple.android.popularmovies.model.Video;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {

    private VideoAdapterOnClickHandler mClickHandler;
    private List<Video> mVideoList;

    public VideoAdapter(VideoAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    public interface VideoAdapterOnClickHandler {
        void onClickTrailer(Video video);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.video_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Video currentVideo = mVideoList.get(position);

        holder.videoName.setText(currentVideo.getName());
    }

    @Override
    public int getItemCount() {
        return mVideoList == null ? 0 : mVideoList.size();
    }

    public void setVideoList(List<Video> videoList) {
        mVideoList = videoList;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.video_name)
        TextView videoName;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mClickHandler.onClickTrailer(mVideoList.get(getAdapterPosition()));
        }
    }
}
