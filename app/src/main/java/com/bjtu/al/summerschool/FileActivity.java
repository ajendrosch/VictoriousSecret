package com.bjtu.al.summerschool;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.design.widget.NavigationView;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;


public class FileActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String LOG_TAG = "AudioRecordTest";
    private MediaPlayer mPlayer = null;
    private Handler mHandler = new Handler();
    private static int save = -1;
    private String currentFile;

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
        final ImageButton nextButton = (ImageButton) findViewById(R.id.nextButton);
        final ImageButton prevButton = (ImageButton) findViewById(R.id.prevButton);
        final ImageButton deletebutton = (ImageButton) findViewById(R.id.deleteButton);
        ArrayList<String> lst = new ArrayList<String>(Arrays.asList(FileManager.GetFiles()));
        final ArrayAdapter<String> itemsAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lst);
        listView.setAdapter(itemsAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View v,int position, long arg3)
            {
                //listView.setSelector(new ColorDrawable(Color.parseColor("#51B7A6")));
                //listView.setItemChecked(position, true);
                //listView.setSelection(position);
                Log.i("Test FileActivity", Integer.toString(position));
                stopPlaying();
                deletebutton.setVisibility(View.VISIBLE);
                save = position;
                currentFile = ((TextView) v).getText().toString();
                startPlaying(currentFile);
                mSeekBar.setMax(mPlayer.getDuration());
                mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    public void onCompletion(MediaPlayer mp) {
                        playbutton.setBackgroundResource(R.drawable.ic_play_circle_filled_black_48dp);
                    }
                });
                playbutton.setBackgroundResource(R.drawable.ic_pause_circle_filled_black_48dp);

                if (save >= listView.getAdapter().getCount())
                {
                     nextButton.setVisibility(View.INVISIBLE);
                }
                else {
                    nextButton.setVisibility(View.VISIBLE);
                }
                if (save < 1) {
                    prevButton.setVisibility(View.INVISIBLE);
                }
                else {
                    prevButton.setVisibility(View.VISIBLE);
                }
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
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                save += 1;
                listView.performItemClick(
                        listView.getAdapter().getView(save, null, null),
                        save,
                        listView.getAdapter().getItemId(save));
            }
        });
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                save -= 1;
                listView.performItemClick(
                        listView.getAdapter().getView(save, null, null),
                        save,
                        listView.getAdapter().getItemId(save));
            }
        });

        deletebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (currentFile == null) {
                    return;
                }
                File file = new File(FileManager.path + "/" + currentFile);
                file.delete();
                itemsAdapter.remove(currentFile);
                playbutton.setBackgroundColor(Color.TRANSPARENT);
                deletebutton.setBackgroundColor(Color.TRANSPARENT);
                nextButton.setBackgroundColor(Color.TRANSPARENT);
                prevButton.setBackgroundColor(Color.TRANSPARENT);

                //listView.setSelector(new ColorDrawable(Color.TRANSPARENT));
                itemsAdapter.notifyDataSetChanged();
                mPlayer.reset();
                currentFile = null;
            }
        });

        FileActivity.this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if(mPlayer != null){
                    mSeekBar.setProgress(mPlayer.getCurrentPosition());
                    progressView.setText(ToMinutes(mPlayer.getCurrentPosition()));
                }
                mHandler.postDelayed(this, 100);
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
                    mPlayer.seekTo(progress);
                }
            }
        });
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        /*
        listView.performItemClick(
                listView.getAdapter().getView(save, null, null),
                save,
                listView.getAdapter().getItemId(save));
        mPlayer.pause();*/

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
        } else if (id == R.id.nav_record) {
            Intent intent = new Intent(FileActivity.this, MainActivity.class);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
