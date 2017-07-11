package meleshko.com.videoscrolling;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import meleshko.com.videoscrolling.video_player_manager.ui.VideoPlayerView;


public class VideoViewHolder extends RecyclerView.ViewHolder{

    public final VideoPlayerView mPlayer;
    public final TextView mTitle;
    public final ImageView mCover;
    public final ImageView mPause;
    public final ImageView mPlay;
    public final LinearLayout mMediacontroller;
    public final TextView mTimeCurrent;
    public final TextView mTime;
    public final SeekBar mMediacontroller_progress;

    public VideoViewHolder(View view) {
        super(view);
        mPlayer = (VideoPlayerView) view.findViewById(R.id.player);
        mTitle = (TextView) view.findViewById(R.id.title);
        mCover = (ImageView) view.findViewById(R.id.cover);
        mPause = (ImageView) view.findViewById(R.id.iv_pause);
        mPlay = (ImageView) view.findViewById(R.id.iv_play);
        mMediacontroller = (LinearLayout) view.findViewById(R.id.ll_mediacontroller);
        mTimeCurrent = (TextView) view.findViewById(R.id.time_current);
        mTime = (TextView) view.findViewById(R.id.time);
        mMediacontroller_progress = (SeekBar) view.findViewById(R.id.mediacontroller_progress);
    }
}
