package meleshko.com.videoscrolling;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

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
                VideoControllerView vcv = new VideoControllerView(mContext, mVideoViewHolder);
                vcv.initControllerView();
                vcv.show();
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