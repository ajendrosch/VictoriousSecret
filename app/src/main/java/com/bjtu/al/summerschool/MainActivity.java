package com.bjtu.al.summerschool;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
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

    String format;



    // TODO onPAUSE onContinue etc. integrieren.

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
        mFileName += date + format;

        mRecorder.setOutputFile(mFileName);
        switch(format){
            case "AAC":
                mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
                break;
            case "AAC_ELD":
                mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC_ELD);
                break;
            case "AMR_NB":
                mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                break;
            case "AMR_WB":
                mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_WB);
                break;
            case "HE_AAC":
                mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.HE_AAC);
                break;
            case "VORBIS":
                mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.VORBIS);
                break;
            default:
                mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        }

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

    private void saveRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }

    private void deleteRecording() {
        mRecorder.stop();
        mRecorder.reset();
        mRecorder = null;
    }

    private void continueRecording() {
        mRecorder.start();
    }

    private void pauseRecording() {
        mRecorder.stop();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

       if (id == R.id.nav_slideshow) {
            Intent intent = new Intent(MainActivity.this, FileActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_manage) {
           Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
           startActivity(intent);
        } else if (id == R.id.nav_translate) {
           Intent intent = new Intent(MainActivity.this, TranslateActivity.class);
           startActivity(intent);
       } else if (id == R.id.nav_record) {
       }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ImageButton imageButton1;
        final ImageButton imageButton2;
        final ImageButton deleteButton;
        final ImageButton saveButton;
        // LOAD PREFERENCES
        SharedPreferences settings = getSharedPreferences("settings", 0);
        format = settings.getString("format", "AAC");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rotateLoading = (RotateLoading) findViewById(R.id.rotateloading);

        //NAVBAR
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // save button
        saveButton = (ImageButton) findViewById(R.id.saveButton);
        //saveButton.setBackgroundResource(R.drawable.ic_pause_circle_filled_black_24dp);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                Toast.makeText(MainActivity.this, "Save Pressed", Toast.LENGTH_SHORT).show();
            }
        });

        // delete button
        deleteButton = (ImageButton) findViewById(R.id.deleteButton);
        //deleteButton.setBackgroundResource(R.drawable.ic_pause_circle_filled_black_24dp);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                Toast.makeText(MainActivity.this, "Delete Pressed", Toast.LENGTH_SHORT).show();
            }
        });

        // play button
        imageButton2 = (ImageButton) findViewById(R.id.playButton);
        //imageButton2.setBackgroundResource(R.drawable.ic_pause_circle_filled_black_24dp);
        imageButton2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                String showString = "Record button is clicked!";
                onPlay(mStartPlaying);
                if (mStartPlaying) {
                    showString = "Started playing";
                    imageButton2.setBackgroundResource(R.drawable.ic_pause_circle_filled_black_48dp);
                } else {
                    showString = "Stopped playing";
                    imageButton2.setBackgroundResource(R.drawable.ic_play_circle_filled_black_48dp);
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

                //Notification
                createRecordNotification();

                imageButton2.setBackgroundResource(R.drawable.ic_play_circle_filled_black_48dp);
                saveButton.setBackgroundResource(R.drawable.ic_add_circle_black_48dp);
                deleteButton.setBackgroundResource(R.drawable.ic_delete_black_48dp);

                String showString = "";
                onRecord(mStartRecording);
                if (mStartRecording) {
                    if (rotateLoading.isStart()) {
                        rotateLoading.stop();
                    } else {
                        rotateLoading.start();
                    }
                    showString = ("Started recording");
                } else {
                    if (rotateLoading.isStart()) {
                        rotateLoading.stop();
                    } else {
                        rotateLoading.start();
                    }
                    showString = ("Stopped recording");
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

    public void createRecordNotification() {
        final NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.play)
                        .setContentTitle("Recorder App Notification")
                        .setContentText("App is recording");


        Intent resultIntent = new Intent(this, MainActivity.class);
        // Because clicking the notification opens a new ("special") activity, there's
        // no need to create an artificial back stack.
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        // Sets an ID for the notification
        int mNotificationId = 001;
        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }


}
