package com.bjtu.al.summerschool;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.victor.loading.rotate.RotateLoading;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String LOG_TAG = "AudioRecordTest";
    private String mFileName;
    private ImageButton mRecordButton = null;
    private MediaRecorder mRecorder = null;
    private MediaPlayer mPlayer = null;

    boolean mStartPlaying = true;
    boolean mStartRecording = true;

    private RotateLoading rotateLoading;


    private void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    private void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }

    private void startPlaying() {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    private void stopPlaying() {
        mPlayer.release();
        mPlayer = null;
    }

    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.reset();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/BeijingRecordings/" ;
        String date = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        mFileName += date + ".aac";

        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        mRecorder.start();
    }

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {
            Intent intent = new Intent(MainActivity.this, FileActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_manage) {
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ImageButton imageButton1;
        final ImageButton imageButton2;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rotateLoading = (RotateLoading) findViewById(R.id.rotateloading);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // play button
        imageButton2 = (ImageButton) findViewById(R.id.playButton);
        //imageButton2.setBackgroundResource(R.drawable.ic_pause_circle_filled_black_24dp);
        imageButton2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                String showString = "Record button is clicked!";
                onPlay(mStartPlaying);
                if (mStartPlaying) {
                    showString = "Start playing";
                    imageButton2.setBackgroundResource(R.drawable.ic_pause_circle_filled_black_24dp);
                } else {
                    showString = "Stop playing";
                    imageButton2.setBackgroundResource(R.drawable.ic_play_circle_filled_black_24dp);
                }
                mStartPlaying = !mStartPlaying;

                Toast.makeText(MainActivity.this, showString, Toast.LENGTH_SHORT).show();
            }

        });

        // record button
        imageButton1 = (ImageButton) findViewById(R.id.recordButton);
        imageButton1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                imageButton2.setBackgroundResource(R.drawable.ic_play_circle_filled_black_24dp);

                String showString = "Record button is clicked!";
                onRecord(mStartRecording);
                if (mStartRecording) {
                    if (rotateLoading.isStart()) {
                        rotateLoading.stop();
                    } else {
                        rotateLoading.start();
                    }
                    showString = ("Start recording");
                } else {
                    if (rotateLoading.isStart()) {
                        rotateLoading.stop();
                    } else {
                        rotateLoading.start();
                    }
                    showString = ("Stop recording");
                }
                mStartRecording = !mStartRecording;

                Toast.makeText(MainActivity.this, showString, Toast.LENGTH_SHORT).show();
            }

        });

    }

    @Override
    public void onPause() {
        super.onPause();
        if (mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
        }

        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

}