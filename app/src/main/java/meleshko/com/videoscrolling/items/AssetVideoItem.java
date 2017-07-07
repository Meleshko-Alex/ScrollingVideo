package meleshko.com.videoscrolling.items;

import android.content.res.AssetFileDescriptor;
import android.net.Uri;
import android.view.View;

import com.squareup.picasso.Picasso;

import meleshko.com.videoscrolling.VideoViewHolder;
import meleshko.com.videoscrolling.video_player_manager.Config;
import meleshko.com.videoscrolling.video_player_manager.manager.VideoPlayerManager;
import meleshko.com.videoscrolling.video_player_manager.meta.MetaData;
import meleshko.com.videoscrolling.video_player_manager.ui.VideoPlayerView;
import meleshko.com.videoscrolling.video_player_manager.utils.Logger;

public class AssetVideoItem extends BaseVideoItem{

    private static final String TAG = AssetVideoItem.class.getSimpleName();
    private static final boolean SHOW_LOGS = Config.SHOW_LOGS;

    private final AssetFileDescriptor mAssetFileDescriptor;
    private final String mTitle;

    private final Picasso mImageLoader;
    private final int mImageResource;

    Uri uri = Uri.parse("https://tbdev-storage.s3.amazonaws.com/van-damme.mp4");
    String link = uri.toString();

    public AssetVideoItem(String title, AssetFileDescriptor assetFileDescriptor, VideoPlayerManager<MetaData> videoPlayerManager, Picasso imageLoader, int imageResource) {
        super(videoPlayerManager);
        mTitle = title;
        mAssetFileDescriptor = assetFileDescriptor;
        mImageLoader = imageLoader;
        mImageResource = imageResource;
    }

    @Override
    public void update(int position, final VideoViewHolder viewHolder, VideoPlayerManager videoPlayerManager) {
        if(SHOW_LOGS) Logger.v(TAG, "update, position " + position);

        viewHolder.mTitle.setText(mTitle);
        viewHolder.mCover.setVisibility(View.VISIBLE);
        mImageLoader.load(mImageResource).into(viewHolder.mCover);
    }

    @Override
    public void playNewVideo(MetaData currentItemMetaData, VideoPlayerView player, VideoPlayerManager<MetaData> videoPlayerManager) {
        videoPlayerManager.playNewVideo(currentItemMetaData, player, link);
    }

    @Override
    public void stopPlayback(VideoPlayerManager videoPlayerManager) {
        videoPlayerManager.stopAnyPlayback();
    }

    @Override
    public String toString() {
        return getClass() + ", mTitle[" + mTitle + "]";
    }
}
