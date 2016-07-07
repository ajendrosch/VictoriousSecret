package com.bjtu.al.summerschool;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PlayFilesFragment extends Fragment {

    public static PlayFilesFragment newInstance() {
        return new PlayFilesFragment();
    }

    public PlayFilesFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_file, container, false);
    }
}