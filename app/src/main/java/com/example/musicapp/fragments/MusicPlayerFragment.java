package com.example.musicapp.fragments;

import android.animation.ObjectAnimator;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.animation.LinearInterpolator;
import com.example.musicapp.R;
import com.example.musicapp.Song.Song;

import java.util.List;

public class MusicPlayerFragment extends Fragment {
    private MediaPlayer mediaPlayer;
    private TextView songName, artistName, genre, currentTime, totalTime;
    private ImageView songImage;
    private SeekBar seekBar;
    private ImageButton btnPlayPause, btnNext, btnPrevious;
    private Handler handler = new Handler();
    private Runnable updateSeekBar;
    private List<Song> songList;
    private int currentSongIndex = 0;
    private ObjectAnimator rotateAnimator;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music_player, container, false);

        // Ánh xạ các thành phần giao diện
        songName = view.findViewById(R.id.tvSongName);
        artistName = view.findViewById(R.id.tvArtistName);
        genre = view.findViewById(R.id.tvGenre);
        songImage = view.findViewById(R.id.imgSong);
        currentTime = view.findViewById(R.id.tvCurrentTime);
        totalTime = view.findViewById(R.id.tvTotalTime);
        seekBar = view.findViewById(R.id.seekBar);
        btnPlayPause = view.findViewById(R.id.btnPlayPauss);
        btnNext = view.findViewById(R.id.btnNext);
        btnPrevious = view.findViewById(R.id.btnPrevious);

        // Nhận dữ liệu từ Bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            currentSongIndex = bundle.getInt("currentSongIndex", 0);
            songList = (List<Song>) bundle.getSerializable("songList");  // Lấy danh sách bài hát qua Serializable
            loadSong(currentSongIndex);  // Load bài hát đầu tiên khi tạo fragment
        }

        // Điều khiển nút Next
        btnNext.setOnClickListener(v -> {
            if (currentSongIndex < songList.size() - 1) {
                currentSongIndex++;
                loadSong(currentSongIndex);
            }
        });

        // Điều khiển nút Previous
        btnPrevious.setOnClickListener(v -> {
            if (currentSongIndex > 0) {
                currentSongIndex--;
                loadSong(currentSongIndex);
            }
        });

        // Điều khiển nút Play/Pause
        btnPlayPause.setOnClickListener(v -> {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                btnPlayPause.setImageResource(R.drawable.play_icon);  // Chuyển nút sang biểu tượng "Play"
                rotateAnimator.pause();  // Tạm dừng xoay
            } else {
                mediaPlayer.start();
                btnPlayPause.setImageResource(R.drawable.pause_icon);  // Chuyển nút sang biểu tượng "Pause"
                rotateAnimator.resume();  // Tiếp tục xoay
                handler.postDelayed(updateSeekBar, 0);  // Bắt đầu cập nhật tiến trình
            }
        });

        // Cập nhật SeekBar theo thời gian thực
        updateSeekBar = new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null) {
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    currentTime.setText(formatTime(mediaPlayer.getCurrentPosition()));
                    handler.postDelayed(this, 1000);  // Cập nhật mỗi giây
                }
            }
        };

        // Điều chỉnh SeekBar khi người dùng kéo thanh tiến trình
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && mediaPlayer != null) {
                    mediaPlayer.seekTo(progress);
                    currentTime.setText(formatTime(progress));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                handler.removeCallbacks(updateSeekBar);  // Dừng cập nhật khi người dùng kéo thanh
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                handler.postDelayed(updateSeekBar, 0);  // Tiếp tục cập nhật khi người dùng thả thanh
            }
        });

        return view;
    }

    // Hàm load bài hát
    private void loadSong(int songIndex) {
        Song song = songList.get(songIndex);
        songName.setText(song.getSongName());
        artistName.setText(song.getArtistName());
        genre.setText(song.getGenre());
        songImage.setImageResource(song.getSongImage());

        // Dừng và giải phóng bài hát cũ trước khi phát bài hát mới
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
        }

        // Khởi tạo MediaPlayer cho bài hát mới
        mediaPlayer = MediaPlayer.create(getContext(), song.getSongFile());
        mediaPlayer.start();
        btnPlayPause.setImageResource(R.drawable.pause_icon);  // Cập nhật nút thành "Pause"

        // Cập nhật lại giao diện SeekBar và thời gian
        seekBar.setProgress(0);  // Reset tiến trình SeekBar về 0
        seekBar.setMax(mediaPlayer.getDuration());  // Đặt thời lượng bài hát cho SeekBar
        currentTime.setText(formatTime(0));  // Reset thời gian hiện tại về 0
        totalTime.setText(formatTime(mediaPlayer.getDuration()));  // Hiển thị tổng thời gian của bài hát

        // Cập nhật tiến trình SeekBar mỗi giây
        handler.postDelayed(updateSeekBar, 0);

        // Tạo hiệu ứng xoay cho ảnh bài hát
        rotateAnimator = ObjectAnimator.ofFloat(songImage, "rotation", 0f, 360f);
        rotateAnimator.setDuration(10000);  // Thời gian xoay (10 giây cho một vòng)
        rotateAnimator.setInterpolator(new LinearInterpolator());  // Giữ tốc độ xoay đều
        rotateAnimator.setRepeatCount(ObjectAnimator.INFINITE);  // Lặp lại vô hạn
        rotateAnimator.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
            handler.removeCallbacks(updateSeekBar);
        }
        if (rotateAnimator != null) {
            rotateAnimator.cancel();  // Dừng xoay
        }
    }

    private String formatTime(int milliseconds) {
        int minutes = (milliseconds / 1000) / 60;
        int seconds = (milliseconds / 1000) % 60;
        return String.format("%d:%02d", minutes, seconds);
    }
}
