package meleshko.com.videoscrolling;

import android.content.Context;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.Formatter;
import java.util.Locale;

import meleshko.com.videoscrolling.video_player_manager.ui.VideoPlayerView;

public class VideoControllerView {
    private static VideoViewHolder mHolder;
    private static SeekBar mProgress;
    private boolean mShowing;
    private boolean mDragging;
    private static VideoPlayerView mPlayer;
    private ImageView mPauseButton;
    private ImageView mPlayButton;
    private StringBuilder mFormatBuilder = new StringBuilder();
    private Formatter mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
    private Handler mHandler = new MessageHandler(this);
    private static int sDefaultTimeout = 3000;
    private static final int FADE_OUT = 1;
    private static final int SHOW_PROGRESS = 2;
    private Context mContext;

    public VideoControllerView(Context context, VideoViewHolder holder) {
        mHolder = holder;
        mContext = context;
    }

    public void show(int timeout) {
        mHolder.mMediacontroller.setVisibility(View.VISIBLE);
        mPauseButton.setVisibility(View.VISIBLE);
        if (!mShowing) {
            setProgress();
            if (mPauseButton != null) {
                mPauseButton.requestFocus();
            }
            mShowing = true;
        }
        //updateFullScreen();
        updatePausePlay();

        mHandler.sendEmptyMessage(SHOW_PROGRESS);

        Message msg = mHandler.obtainMessage(FADE_OUT);
        if (timeout != 0) {
            mHandler.removeMessages(FADE_OUT);
            mHandler.sendMessageDelayed(msg, timeout);
        }
    }

    public void show() {
        show(sDefaultTimeout);
    }

    private static class MessageHandler extends Handler {
        private final WeakReference<VideoControllerView> mView;

        MessageHandler(VideoControllerView view) {
            mView = new WeakReference<VideoControllerView>(view);
        }

        @Override
        public void handleMessage(Message msg) {
            VideoControllerView view = mView.get();
            if (view == null || mPlayer == null) {
                return;
            }

            int pos;
            switch (msg.what) {
                case FADE_OUT:
                    view.hide();
                    break;
                case SHOW_PROGRESS:
                    pos = view.setProgress();
                    mProgress.setVisibility(View.VISIBLE);
                    if (view.mShowing && view.mPlayer.isPlaying()) {
                        msg = obtainMessage(SHOW_PROGRESS);
                        sendMessageDelayed(msg, 1000 - (pos % 1000));
                    }
                    break;
            }
        }
    }

    int setProgress() {
        if (mPlayer == null || mDragging) {
            return 0;
        }

        int position = mPlayer.getCurrentPosition();
        int duration = mPlayer.getDuration();
        if (mProgress != null) {
            if (duration > 0) {
                // use long to avoid overflow
                long pos = 1000L * position / duration;
                mProgress.setProgress((int) pos);
            }
            int percent = getBufferPercentage();
            mProgress.setSecondaryProgress(percent * 10);
        }

        if (mHolder.mTime != null)
            mHolder.mTime.setText(stringForTime(duration));
        if (mHolder.mTimeCurrent != null)
            mHolder.mTimeCurrent.setText(stringForTime(position));

        mProgress.setOnSeekBarChangeListener(mSeekListener);

        return position;
    }

    public void hide() {
        mHolder.mMediacontroller.setVisibility(View.GONE);
        mPauseButton.setVisibility(View.GONE);
        mShowing = false;
    }

    private String stringForTime(int timeMs) {
        int totalSeconds = timeMs / 1000;

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

    //???
    public int getBufferPercentage() {
        return 0;
    }

    private OnSeekBarChangeListener mSeekListener = new OnSeekBarChangeListener() {
        public void onStartTrackingTouch(SeekBar bar) {
            show(3600000);

            mDragging = true;
            mHandler.removeMessages(SHOW_PROGRESS);
        }

        public void onProgressChanged(SeekBar bar, int progress, boolean fromuser) {
            if (mPlayer == null) {
                return;
            }

            if (!fromuser) {
                return;
            }

            long duration = mHolder.mPlayer.getDuration();
            long newposition = (duration * progress) / 1000L;
            mPlayer.seekTo((int) newposition);
            if (mHolder.mTimeCurrent != null)
                mHolder.mTimeCurrent.setText(stringForTime((int) newposition));
        }

        public void onStopTrackingTouch(SeekBar bar) {
            mDragging = false;
            setProgress();
            updatePausePlay();
            show(sDefaultTimeout);

            mHandler.sendEmptyMessage(SHOW_PROGRESS);
        }
    };

    public void initControllerView() {
        mPlayer = mHolder.mPlayer;
        OnSwipeTouchListener clickFrameSwipeListener = new OnSwipeTouchListener(){

            int startVolume;
            int maxVolume;
            int mInitialTextureWidth = mPlayer.getWidth();
            TextView mPositionTextView = mHolder.mVolume;
            private AudioManager am = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);

            @Override
            public void onMove(Direction dir, float diff) {
                if (dir == Direction.LEFT || dir == Direction.RIGHT) {
                        float diffVolume;
                        int finalVolume;

                        diffVolume = (float) maxVolume * diff / ((float) mInitialTextureWidth / 2);
                        if (dir == Direction.LEFT) {
                            diffVolume = -diffVolume;
                        }
                        finalVolume = startVolume + (int) diffVolume;
                        if (finalVolume < 0)
                            finalVolume = 0;
                        else if (finalVolume > maxVolume)
                            finalVolume = maxVolume;

                        //mPositionTextView.setVisibility(View.VISIBLE);
                        String progressText = "Volume: " + finalVolume;
                        mPositionTextView.setText(progressText);
                        am.setStreamVolume(AudioManager.STREAM_MUSIC, finalVolume, 0);
                }
            }

            @Override
            public void onClick() {
                show();
            }

            @Override
            public void onCancel() {
                mPositionTextView.setVisibility(View.GONE);
            }

            @Override
            public void onAfterMove() {
                mPositionTextView.setVisibility(View.GONE);
            }

            @Override
            public void onBeforeMove(Direction dir) {
                maxVolume = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                startVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
                mPositionTextView.setVisibility(View.VISIBLE);
            }
        };
        mPlayer.setOnTouchListener(clickFrameSwipeListener);

        mPauseButton = mHolder.mPause;
        if (mPauseButton != null) {
            //mPauseButton.requestFocus();
            mPauseButton.setOnClickListener(mPauseListener);
        }

        mPlayButton = mHolder.mPlay;
        /*if (mPlayButton != null) {
            //mPlayButton.requestFocus();
            mPlayButton.setOnClickListener(mPlayListener);
        }*/

        /*mFullscreenButton = (ImageButton) v.findViewById(R.id.fullscreen);
        if (mFullscreenButton != null) {
            mFullscreenButton.requestFocus();
            mFullscreenButton.setOnClickListener(mFullscreenListener);
        }*/

        mProgress = mHolder.mMediacontroller_progress;
        if (mProgress != null) {
            mProgress.setOnSeekBarChangeListener(mSeekListener);
        }

        updatePausePlay();
    }

    private View.OnClickListener mPauseListener = new View.OnClickListener() {
        public void onClick(View v) {
            doPauseResume();
            show(sDefaultTimeout);
        }
    };

    private void doPauseResume() {
        if (mPlayer == null) {
            return;
        }

        if (mPlayer.isPlaying()) {
            mPlayer.pause();
        } else {
            mPlayer.start();
        }
        updatePausePlay();
    }

    public void updatePausePlay() {
        if (mPlayer.isPlaying()) {
            mPauseButton.setImageResource(R.drawable.ic_pause_circle_white_72dp);
        } else {
            mPauseButton.setImageResource(R.drawable.ic_play_circle_white_72dp);
        }
    }

}

