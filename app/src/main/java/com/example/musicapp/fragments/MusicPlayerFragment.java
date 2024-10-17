package com.example.musicapp.fragments;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.musicapp.R;
import com.example.musicapp.Song.Song;
import java.util.ArrayList;

public class MusicPlayerFragment extends Fragment {
    private ArrayList<Song> songList;
    private int currentSongIndex;
    private MediaPlayer mediaPlayer;
    private SeekBar seekBar;
    private TextView tvCurrentTime, tvTotalTime;
    private ImageButton btnPlayPause, btnPrevious, btnNext;

    public MusicPlayerFragment() {
        // Constructor rỗng cần thiết cho việc khởi tạo fragment
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music_player, container, false);

        // Lấy dữ liệu từ Bundle
        if (getArguments() != null) {
            songList = getArguments().getParcelableArrayList("songList"); // Nhận danh sách bài hát
            currentSongIndex = getArguments().getInt("currentSongIndex"); // Nhận chỉ số bài hát hiện tại
        }

        // Kết nối các View
        seekBar = view.findViewById(R.id.seekBar);
        tvCurrentTime = view.findViewById(R.id.tvCurrentTime);
        tvTotalTime = view.findViewById(R.id.tvTotalTime);
        btnPlayPause = view.findViewById(R.id.btnPlayPauss);
        btnPrevious = view.findViewById(R.id.btnPrevious);
        btnNext = view.findViewById(R.id.btnNext);

        // Lấy bài hát hiện tại và thiết lập tổng thời gian
        Song currentSong = songList.get(currentSongIndex);
        mediaPlayer = MediaPlayer.create(getContext(), currentSong.getSongResource());
        tvTotalTime.setText(formatTime(mediaPlayer.getDuration()));

        // Thiết lập SeekBar
        seekBar.setMax(mediaPlayer.getDuration());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Thiết lập sự kiện cho nút Play/Pause
        btnPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    btnPlayPause.setImageResource(R.drawable.play_icon);
                } else {
                    mediaPlayer.start();
                    btnPlayPause.setImageResource(R.drawable.pause_icon);
                    updateSeekBar();
                }
            }
        });

        // Thiết lập sự kiện cho nút Previous
        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPrevious();
            }
        });

        // Thiết lập sự kiện cho nút Next
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playNext();
            }
        });

        return view;
    }

    private void updateSeekBar() {
        seekBar.setProgress(mediaPlayer.getCurrentPosition());
        tvCurrentTime.setText(formatTime(mediaPlayer.getCurrentPosition()));

        if (mediaPlayer.isPlaying()) {
            seekBar.postDelayed(new Runnable() {
                @Override
                public void run() {
                    updateSeekBar();
                }
            }, 1000);
        }
    }

    private void playPrevious() {
        if (currentSongIndex > 0) {
            currentSongIndex--;
            playSong();
        }
    }

    private void playNext() {
        if (currentSongIndex < songList.size() - 1) {
            currentSongIndex++;
            playSong();
        }
    }

    private void playSong() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(getContext(), songList.get(currentSongIndex).getSongResource());
        mediaPlayer.start();
        btnPlayPause.setImageResource(R.drawable.pause_icon);
        tvTotalTime.setText(formatTime(mediaPlayer.getDuration()));
        seekBar.setMax(mediaPlayer.getDuration());
        updateSeekBar();
    }

    private String formatTime(int milliseconds) {
        int seconds = (milliseconds / 1000) % 60;
        int minutes = (milliseconds / (1000 * 60)) % 60;
        return String.format("%d:%02d", minutes, seconds);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
