package com.example.musicapp.fragments;

import android.animation.ObjectAnimator;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.musicapp.R;
import com.example.musicapp.Song.Song;

import java.util.ArrayList;
import java.util.List;

public class MusicPlayerFragment extends Fragment {

    private TextView songName, artistName, genreName, currentTime, totalTime;
    private ImageView songImage, playPauseButton, nextButton, prevButton;
    private SeekBar seekBar;
    private MediaPlayer mediaPlayer;
    private Handler handler = new Handler();
    private List<Song> songList = new ArrayList<>();
    private int currentSongIndex = 0;
    private boolean isSeekBarUpdating = false;
    private ObjectAnimator rotateAnimator;
    private boolean isPlaying = false; // Biến theo dõi trạng thái phát nhạc

    public MusicPlayerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            currentSongIndex = args.getInt("CURRENT_SONG_INDEX", 0);
            songList = (List<Song>) args.getSerializable("SONG_LIST");
        }

        // Ensure currentSongIndex is valid
        if (currentSongIndex < 0 || songList == null || currentSongIndex >= songList.size()) {
            currentSongIndex = 0; // Default to first song if invalid
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music_player, container, false);

        // Initialize UI components
        songName = view.findViewById(R.id.tvSongName);
        artistName = view.findViewById(R.id.tvArtistName);
        genreName = view.findViewById(R.id.tvGenre);
        currentTime = view.findViewById(R.id.tvCurrentTime);
        totalTime = view.findViewById(R.id.tvTotalTime);
        songImage = view.findViewById(R.id.imgSong);
        playPauseButton = view.findViewById(R.id.btnPlayPauss);
        nextButton = view.findViewById(R.id.btnNext);
        prevButton = view.findViewById(R.id.btnPrevious);
        seekBar = view.findViewById(R.id.seekBar);

        // Setup initial song
        setupSong(currentSongIndex);

        // Play/Pause button listener
        playPauseButton.setOnClickListener(v -> togglePlayPause());

        // Next button listener
        nextButton.setOnClickListener(v -> playNextSong());

        // Previous button listener
        prevButton.setOnClickListener(v -> playPreviousSong());

        // SeekBar listener
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && mediaPlayer != null) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isSeekBarUpdating = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isSeekBarUpdating = false;
            }
        });

        return view;
    }

    private void setupSong(int songIndex) {
        if (songList == null || songIndex < 0 || songIndex >= songList.size()) {
            return; // Do nothing if songList is null or index is invalid
        }

        if (mediaPlayer != null) {
            mediaPlayer.release();
        }

        Song currentSong = songList.get(songIndex);

        // Display song info
        songName.setText(currentSong.getName());
        artistName.setText(currentSong.getSinger());
        genreName.setText(currentSong.getGenre()); // Set genre
        Glide.with(getContext()).load(currentSong.getImageUrl()).into(songImage);

        // Get resource ID from the filename in the raw folder
        int resId = getResources().getIdentifier(currentSong.getUrl(), "raw", getContext().getPackageName());

        // Prepare MediaPlayer
        mediaPlayer = MediaPlayer.create(getContext(), resId);
        if (mediaPlayer != null) { // Ensure mediaPlayer is created successfully
            seekBar.setMax(mediaPlayer.getDuration());
            totalTime.setText(formatTime(mediaPlayer.getDuration())); // Display total time

            // Update SeekBar every second
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!isSeekBarUpdating && mediaPlayer != null) {
                        seekBar.setProgress(mediaPlayer.getCurrentPosition());
                        currentTime.setText(formatTime(mediaPlayer.getCurrentPosition())); // Update current time
                    }
                    handler.postDelayed(this, 1000);
                }
            }, 1000);

            mediaPlayer.setOnCompletionListener(mp -> playNextSong());
        }
    }

    private void togglePlayPause() {
        if (mediaPlayer != null) { // Check if mediaPlayer is not null
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                playPauseButton.setImageResource(R.drawable.play_icon);
                stopImageRotation(); // Dừng xoay khi tạm dừng
                isPlaying = false; // Cập nhật trạng thái
            } else {
                mediaPlayer.start();
                playPauseButton.setImageResource(R.drawable.pause_icon);
                startImageRotation(); // Bắt đầu xoay khi phát nhạc
                isPlaying = true; // Cập nhật trạng thái
            }
        }
    }

    private void playNextSong() {
        currentSongIndex++;
        if (currentSongIndex >= songList.size()) {
            currentSongIndex = 0; // Quay lại bài hát đầu tiên
        }
        setupSong(currentSongIndex);
        mediaPlayer.start();
        playPauseButton.setImageResource(R.drawable.pause_icon);
        startImageRotation(); // Bắt đầu xoay khi phát nhạc
        isPlaying = true; // Cập nhật trạng thái
    }

    private void playPreviousSong() {
        currentSongIndex--;
        if (currentSongIndex < 0) {
            currentSongIndex = songList.size() - 1; // Quay lại bài hát cuối cùng
        }
        setupSong(currentSongIndex);
        mediaPlayer.start();
        playPauseButton.setImageResource(R.drawable.pause_icon);
        startImageRotation(); // Bắt đầu xoay khi phát nhạc
        isPlaying = true; // Cập nhật trạng thái
    }

    private void startImageRotation() {
        if (rotateAnimator == null) {
            rotateAnimator = ObjectAnimator.ofFloat(songImage, "rotation", 0f, 360f);
            rotateAnimator.setDuration(3000); // Duration for one complete rotation
            rotateAnimator.setRepeatCount(ObjectAnimator.INFINITE);
            rotateAnimator.setInterpolator(null); // Use default interpolator
            rotateAnimator.start();
        } else if (!rotateAnimator.isRunning()) {
            rotateAnimator.start(); // Resume rotation if it was paused
        }
    }

    private void stopImageRotation() {
        if (rotateAnimator != null && rotateAnimator.isRunning()) {
            rotateAnimator.cancel(); // Dừng animation nhưng không reset về 0 độ
            // Không reset rotation về 0
        }
    }

    private String formatTime(int milliseconds) {
        int seconds = (milliseconds / 1000) % 60;
        int minutes = (milliseconds / (1000 * 60)) % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        stopImageRotation(); // Dừng xoay khi fragment bị hủy
    }
}
