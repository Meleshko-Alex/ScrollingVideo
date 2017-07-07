package meleshko.com.videoscrolling.video_player_manager.manager;


import meleshko.com.videoscrolling.video_player_manager.PlayerMessageState;
import meleshko.com.videoscrolling.video_player_manager.meta.MetaData;
import meleshko.com.videoscrolling.video_player_manager.ui.VideoPlayerView;

/**
 * This callback is used by {@link meleshko.com.videoscrolling.video_player_manager.player_messages}
 * to get and set data it needs
 */
public interface VideoPlayerManagerCallback {

    void setCurrentItem(MetaData currentItemMetaData, VideoPlayerView newPlayerView);

    void setVideoPlayerState(VideoPlayerView videoPlayerView, PlayerMessageState playerMessageState);

    PlayerMessageState getCurrentPlayerState();
}
