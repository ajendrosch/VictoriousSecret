package com.bjtu.al.summerschool;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class FileActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


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
            createPlayNotification();

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
        setContentView(R.layout.app_bar_not_main);

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

        // MediaPlayer
        final SeekBar mSeekBar = (SeekBar) findViewById(R.id.seekBar);
        final ListView listView = (ListView) findViewById(R.id.listView);
        final TextView progressView = (TextView) findViewById(R.id.progressView);
        final ImageButton playbutton = (ImageButton) findViewById(R.id.playButton2);

        ArrayAdapter itemsAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, FileManager.GetFiles());
        listView.setAdapter(itemsAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View v,int position, long arg3)
            {
                stopPlaying();
                save = position;
                String str = ((TextView) v).getText().toString();
                startPlaying(str);
                mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    public void onCompletion(MediaPlayer mp) {
                        playbutton.setBackgroundResource(R.drawable.ic_play_circle_filled_black_48dp);
                    }
                });
                playbutton.setBackgroundColor(Color.TRANSPARENT);
                playbutton.setBackgroundResource(R.drawable.ic_pause_circle_filled_black_48dp);
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
                    playbutton.setBackgroundResource(R.drawable.ic_play_circle_filled_black_48dp);
                } else {
                    mPlayer.start();
                    playbutton.setBackgroundResource(R.drawable.ic_pause_circle_filled_black_48dp);
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

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {
            Intent intent = new Intent(FileActivity.this, SettingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_record) {
            Intent intent = new Intent(FileActivity.this, MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_translate) {
            Intent intent = new Intent(FileActivity.this, TranslateActivity.class);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void createPlayNotification() {
        final NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.play)
                        .setContentTitle("Recorder App Notification")
                        .setContentText("App is playing");


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
        int mNotificationId = 002;
        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }

}
