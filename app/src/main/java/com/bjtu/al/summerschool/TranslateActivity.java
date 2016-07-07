package com.bjtu.al.summerschool;

import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;

import org.json.JSONObject;
import 	java.net.URLConnection;

public class TranslateActivity extends AppCompatActivity implements OnClickListener, OnInitListener, NavigationView.OnNavigationItemSelectedListener {

    protected static final int REQUEST_OK = 1;
    private TextToSpeech tts;

    String translated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);
        tts = new TextToSpeech(this, this);
        findViewById(R.id.button1).setOnClickListener(this);

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
    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");
        try {
            startActivityForResult(i, REQUEST_OK);
        } catch (Exception e) {
            Toast.makeText(this, "Error initializing speech to text engine.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUEST_OK  && resultCode==RESULT_OK) {
            ArrayList<String> thingsYouSaid = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            // Speech text is stored in thingsYouSaid
            // Translate
            Toast.makeText(this, thingsYouSaid.get(0), Toast.LENGTH_LONG).show();
            // String translated = translate(thingsYouSaid.get(0));
            new TranslateTask().execute(thingsYouSaid.get(0));

        }
    }

    @Override
    public void onInit(int code) {
        if (code == TextToSpeech.SUCCESS) {
            tts.setLanguage(Locale.getDefault());
        } else {
            tts = null;
            Toast.makeText(this, "Failed to initialize TTS engine.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {


        //Close the Text to Speech Library
        if(tts != null) {

            tts.stop();
            tts.shutdown();
            Log.d("MainActivity", "TTS Destroyed");
        }
        super.onDestroy();
    }

    /** Translate a given text between a source and a destination language */
    /*
    public String translate(String text) {
        String translated = null;
        HttpURLConnection urlConnection = null;
        URL url;

        try {
            String srcLang = "en";
            String dstLang = "de"; // dstLanguage.getLanguage()
            String query = URLEncoder.encode(text, "UTF-8");
            String langpair = URLEncoder.encode(srcLang+"|"+dstLang, "UTF-8");
            String stringurl = "http://mymemory.translated.net/api/get?q="+query+"&langpair="+langpair;
            url = new URL(stringurl);

            urlConnection = (HttpURLConnection) url
                    .openConnection();


            InputStream in = urlConnection.getInputStream();

            InputStreamReader isw = new InputStreamReader(in);

            int data = isw.read();
            String txt = "";
            while (data != -1) {
                char current = (char) data;
                txt += current;
                data = isw.read();
                System.out.print(current);
            }
            Log.d("MainActivity Text", txt);
            JSONObject response = new JSONObject(txt);
            translated = response.getJSONObject("responseData").getString("translatedText");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        Log.e("MainActivity Translated", "Hallo");

        return translated;
    }
    */


    // AsyncTask to Translate language
    class TranslateTask extends AsyncTask<String, Void, String> {

        private Exception exception;
        //String translated = null;
        HttpURLConnection urlConnection = null;
        URL url;

        protected String doInBackground(String... texts) {
            try {
                String text = texts[0];
                String srcLang = "en";
                String dstLang = "de"; // dstLanguage.getLanguage()
                String query = URLEncoder.encode(text, "UTF-8");
                String langpair = URLEncoder.encode(srcLang+"|"+dstLang, "UTF-8");
                String stringurl = "http://mymemory.translated.net/api/get?q="+query+"&langpair="+langpair;
                url = new URL(stringurl);

                urlConnection = (HttpURLConnection) url
                        .openConnection();

                InputStream in = urlConnection.getInputStream();

                InputStreamReader isw = new InputStreamReader(in);

                int data = isw.read();
                String txt = "";
                while (data != -1) {
                    char current = (char) data;
                    txt += current;
                    data = isw.read();
                    //System.out.print(current);
                }
                System.out.println(txt);

                Log.e("MainActivity Text1", txt);
                JSONObject response = new JSONObject(txt);
                String translated2 = response.getJSONObject("responseData").getString("translatedText");
                Log.e("MainActivity Text2", translated2);
                return translated2;
            } catch (Exception e) {
                this.exception = e;
                return null;
            }
        }

        protected void onPostExecute(String result) {
            // TODO: check this.exception
            // TODO: do something with the feed
            translated = result;
            if (urlConnection != null) {
                urlConnection.disconnect();
            }

            //((TextView)findViewById(R.id.text1)).setText(thingsYouSaid.get(0));
            ((TextView)findViewById(R.id.text1)).setText(translated);
            // Say what you got!
            if (tts!=null) {
                //String text = thingsYouSaid.toString();
                if (translated!=null) {
                    if (!tts.isSpeaking()) {
                        tts.speak(translated, TextToSpeech.QUEUE_FLUSH, null);
                    }
                }
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_slideshow) {
            Intent intent = new Intent(TranslateActivity.this, FileActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_record) {
            Intent intent = new Intent(TranslateActivity.this, MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_translate) {
            Intent intent = new Intent(TranslateActivity.this, TranslateActivity.class);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
}
