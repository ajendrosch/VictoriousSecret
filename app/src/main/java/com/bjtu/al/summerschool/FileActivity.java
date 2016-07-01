package com.bjtu.al.summerschool;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class FileActivity extends AppCompatActivity {
    private static final String LOG_TAG = "AudioRecordTest";
    private MediaPlayer mPlayer = null;
    private Handler mHandler = new Handler();
    private static int save = -1;

    private void startPlaying(String mFileName) {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(FileManager.path + "/" + mFileName);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    private void stopPlaying() {
        if (mPlayer == null) {
            return;
        }
        mPlayer.release();
        mPlayer = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);

        final SeekBar mSeekBar = (SeekBar) findViewById(R.id.seekBar);
        final ListView listView = (ListView) findViewById(R.id.listView);
        final TextView progressView = (TextView) findViewById(R.id.progressView);
        final ImageButton playbutton = (ImageButton) findViewById(R.id.playButton2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ArrayAdapter itemsAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, FileManager.GetFiles());
        listView.setAdapter(itemsAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View v,int position, long arg3)
            {
                stopPlaying();
                parent.getChildAt(position).setBackgroundColor(Color.BLUE);

                if (save != -1 && save != position){
                    parent.getChildAt(save).setBackgroundColor(Color.WHITE);
                }

                save = position;
                String str = ((TextView) v).getText().toString();
                startPlaying(str);
                mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    public void onCompletion(MediaPlayer mp) {
                        playbutton.setBackgroundResource(R.drawable.ic_play_circle_filled_black_24dp);
                    }
                });
                playbutton.setBackgroundColor(Color.TRANSPARENT);
                playbutton.setBackgroundResource(R.drawable.ic_pause_circle_filled_black_24dp);
                mSeekBar.setMax(mPlayer.getDuration());
            }
        });
        playbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (mPlayer == null) {
                    return;
                }
                if (mPlayer.isPlaying()) {
                    mPlayer.pause();
                    playbutton.setBackgroundResource(R.drawable.ic_play_circle_filled_black_24dp);
                } else {
                    mPlayer.start();
                    playbutton.setBackgroundResource(R.drawable.ic_pause_circle_filled_black_24dp);
                }
            }
        });

        FileActivity.this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if(mPlayer != null){
                    int mCurrentPosition = mPlayer.getCurrentPosition() / 1000;
                    mSeekBar.setProgress(mPlayer.getCurrentPosition());
                    progressView.setText(ToMinutes((mCurrentPosition*1000)));
                }
                mHandler.postDelayed(this, 10);
            }
        });
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(mPlayer != null && fromUser){
                    mPlayer.seekTo(progress * 1000);
                }
            }
        });

    }
    public String ToMinutes(int millis) {
        return (new SimpleDateFormat("mm:ss")).format(new Date(millis));


    }

    }
