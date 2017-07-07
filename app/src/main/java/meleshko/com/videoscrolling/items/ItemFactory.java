package meleshko.com.videoscrolling.items;

import android.app.Activity;

import com.squareup.picasso.Picasso;

import java.io.IOException;

import meleshko.com.videoscrolling.video_player_manager.manager.VideoPlayerManager;
import meleshko.com.videoscrolling.video_player_manager.meta.MetaData;

public class ItemFactory {

    public static BaseVideoItem createItemFromAsset(String assetName, int imageResource, Activity activity, VideoPlayerManager<MetaData> videoPlayerManager) throws IOException {
        return new AssetVideoItem(assetName, activity.getAssets().openFd(assetName), videoPlayerManager, Picasso.with(activity), imageResource);
    }
}
