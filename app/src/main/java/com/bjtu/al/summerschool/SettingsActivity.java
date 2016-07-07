package com.bjtu.al.summerschool;

import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Spinner;

public class SettingsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

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

        Spinner formatSpinner = (Spinner) findViewById(R.id.format_spinner);
        formatSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // An item was selected. You can retrieve the selected item using

        // parent.getItemAtPosition(pos)
        // save the format

        SharedPreferences settings = getSharedPreferences("settings", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("format", (String) parent.getItemAtPosition(position));
        // Commit the edits!
        editor.commit();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_slideshow) {
            Intent intent = new Intent(SettingsActivity.this, FileActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_record) {
            Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_translate) {
            Intent intent = new Intent(SettingsActivity.this, TranslateActivity.class);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
}
