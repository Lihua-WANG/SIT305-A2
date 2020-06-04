package com.example.a2.About;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.a2.R;

public class AboutVideoFragment extends Fragment {
    View view; //Define the view used to set the layout of the fragment

    private static final String VIDEO_TUTORIAL =
            "https://r5---sn-ntq7yney.googlevideo.com/videoplayback?expire=1591274515&ei=s5fYXtrROIO98QOlna6YAQ&ip=103.111.29.250&id=o-AGjxIiuU5oMCzE6BE1rpwskAit0gVjLyCvNC04Tx1z7u&itag=18&source=youtube&requiressl=yes&vprv=1&mime=video%2Fmp4&gir=yes&clen=10483899&ratebypass=yes&dur=264.730&lmt=1563728721388818&fvip=5&c=WEB&txp=2211222&sparams=expire%2Cei%2Cip%2Cid%2Citag%2Csource%2Crequiressl%2Cvprv%2Cmime%2Cgir%2Cclen%2Cratebypass%2Cdur%2Clmt&sig=AOq0QJ8wRQIgULGujNrNQHj9Ub3uX_bVCWyc2DnqKLa47yP0fAl0jKwCIQCchSy5SypUKrng8uzypxVYUvDGh5NrnFVwNlOfYMgatg%3D%3D&rm=sn-pjvo2aopq-jb3e7l,sn-npos776&req_id=90b38b92ef2ba3ee&redirect_counter=2&cms_redirect=yes&ipbypass=yes&mh=Nr&mip=203.220.56.247&mm=29&mn=sn-ntq7yney&ms=rdu&mt=1591252834&mv=m&mvi=4&pl=24&lsparams=ipbypass,mh,mip,mm,mn,ms,mv,mvi,pl&lsig=AG3C_xAwRAIgELi5IDwJuYjjK3JZOOBJtYRVh3TPBCpO1Ma7Qm00K7QCIH71KDejGHegAxF6xKUbCZbPxjj1ecXPiO2-LRcvoDlZ";

    private VideoView mVideoView;
    private TextView mBufferingTextView;

    // Current playback position (in milliseconds).
    private int mCurrentPosition = 0;

    // Tag for the instance state bundle.
    private static final String PLAYBACK_TIME = "play_time";

    public AboutVideoFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_about_video, container, false);

        initVideoView();

        if (savedInstanceState != null) {
            mCurrentPosition = savedInstanceState.getInt(PLAYBACK_TIME);
        }

        // Set up the media controller widget and attach it to the video view.
        MediaController controller = new MediaController(getActivity());
        controller.setMediaPlayer(mVideoView);
        mVideoView.setMediaController(controller);

        // Inflate the layout for this fragment
        return view;
    }

    private void initVideoView() {
        mVideoView = view.findViewById(R.id.Tutorial_videoview);
        mBufferingTextView = view.findViewById(R.id.buffering_textview);
    }


    @Override
    public void onStart() {
        super.onStart();

        // Load the media each time onStart() is called.
        initializePlayer();
    }

    @Override
    public void onPause() {
        super.onPause();

        // This is not a problem for more recent versions of Android because
        // onStop() is now the end of the visual lifecycle, and that is where
        // most of the app teardown should take place.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            mVideoView.pause();
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        // Media playback takes a lot of resources, so everything should be
        // stopped and released at this time.
        releasePlayer();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the current playback position (in milliseconds) to the
        // instance state bundle.
        outState.putInt(PLAYBACK_TIME, mVideoView.getCurrentPosition());
    }

    private void initializePlayer() {
        // Show the "Buffering..." message while the video loads.
        mBufferingTextView.setVisibility(VideoView.VISIBLE);

        // Buffer and decode the video sample.
        Uri videoUri = getMedia(VIDEO_TUTORIAL);
        mVideoView.setVideoURI(videoUri);

        // Listener for onPrepared() event (runs after the media is prepared).
        mVideoView.setOnPreparedListener(
                new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {

                        // Hide buffering message.
                        mBufferingTextView.setVisibility(VideoView.INVISIBLE);

                        // Restore saved position, if available.
                        if (mCurrentPosition > 0) {
                            mVideoView.seekTo(mCurrentPosition);
                        } else {
                            // Skipping to 1 shows the first frame of the video.
                            mVideoView.seekTo(1);
                        }

                        // Start playing!
//                        mVideoView.start();
                    }
                });

        // Listener for onCompletion() event (runs after media has finished
        // playing).
        mVideoView.setOnCompletionListener(
                new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        Toast.makeText(getActivity(), R.string.toast_message, Toast.LENGTH_SHORT).show();
                        // Return the video position to the start.
                        mVideoView.seekTo(0);
                    }
                });
    }

    // Release all media-related resources. In a more complicated app this
    // might involve unregistering listeners or releasing audio focus.
    private void releasePlayer() {
        mVideoView.stopPlayback();
    }

    // Get a Uri for the media sample regardless of whether that sample is
    // embedded in the app resources or available on the internet.
    private Uri getMedia(String mediaName) {
        if (URLUtil.isValidUrl(mediaName)) {
            // Media name is an external URL.
            return Uri.parse(mediaName);
        } else {
            // Media name is a raw resource embedded in the app.
            return Uri.parse("android.resource://" + getActivity().getPackageName() +
                    "/raw/" + "gobangrule");
        }
    }
}