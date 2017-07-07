package meleshko.com.videoscrolling.video_player_manager.manager;


import meleshko.com.videoscrolling.video_player_manager.meta.MetaData;

public interface PlayerItemChangeListener {
    void onPlayerItemChanged(MetaData currentItemMetaData);
}
