package meleshko.com.videoscrolling;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import meleshko.com.videoscrolling.items.BaseVideoItem;
import meleshko.com.videoscrolling.video_player_manager.manager.VideoPlayerManager;

public class VideoRecyclerViewAdapter extends RecyclerView.Adapter<VideoViewHolder> {

    private final VideoPlayerManager mVideoPlayerManager;
    private final List<BaseVideoItem> mList;
    private final Context mContext;

    public VideoRecyclerViewAdapter(VideoPlayerManager videoPlayerManager, Context context, List<BaseVideoItem> list){
        mVideoPlayerManager = videoPlayerManager;
        mContext = context;
        mList = list;
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        BaseVideoItem videoItem = mList.get(position);
        View resultView = videoItem.createView(viewGroup, mContext.getResources().getDisplayMetrics().widthPixels);
        final VideoViewHolder mVideoViewHolder = new VideoViewHolder(resultView);
        mVideoViewHolder.mPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Play pressed
                if (mVideoViewHolder.mPause.getVisibility() == View.GONE && mVideoViewHolder.mPlay.getVisibility() == View.VISIBLE) {
                    mVideoViewHolder.mPlay.setVisibility(View.GONE);
                    mVideoViewHolder.mPause.setVisibility(View.VISIBLE);
                    Toast.makeText(mContext, "START", Toast.LENGTH_SHORT).show();
                    mVideoViewHolder.mPlayer.start();

                 // Pause pressed
                } else if (mVideoViewHolder.mPause.getVisibility() == View.VISIBLE && mVideoViewHolder.mPlay.getVisibility() == View.GONE) {
                    mVideoViewHolder.mPlay.setVisibility(View.VISIBLE);
                    mVideoViewHolder.mPause.setVisibility(View.GONE);
                    //mVideoPlayerManager.resetMediaPlayer();
                    Toast.makeText(mContext, "PAUSE", Toast.LENGTH_SHORT).show();
                    mVideoViewHolder.mPlayer.pause();
                    //mVideoViewHolder.mPlayer.start();
                 // First touch
                } else {
                    mVideoViewHolder.mPause.setVisibility(View.VISIBLE);
                    //mVideoViewHolder.mPlayer.pause();
                }
            }
        });
        return mVideoViewHolder;
    }

    @Override
    public void onBindViewHolder(VideoViewHolder viewHolder, int position) {
        BaseVideoItem videoItem = mList.get(position);
        videoItem.update(position, viewHolder, mVideoPlayerManager);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}