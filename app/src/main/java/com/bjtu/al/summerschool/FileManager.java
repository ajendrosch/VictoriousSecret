package com.bjtu.al.summerschool;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;

import java.io.File;

/**
 * Created by XtraPC on 2016-06-29.
 */
public class FileManager
{
    public static String path = Environment.getExternalStorageDirectory().toString()+"/BeijingRecordings";
    public static File f = new File(path);

    public static String[] GetFiles() {
        if (!f.exists()) {
            f.mkdirs();
        }
        File[] fileList = f.listFiles();
        if (fileList == null) {
            return new String[] {};
        }
        String[] theNamesOfFiles = new String[fileList.length];
        for (int i = 0; i < theNamesOfFiles.length; i++) {

            theNamesOfFiles[i] = fileList[i].getName();
        }
        return theNamesOfFiles;
    }

    public static String GetFileName(int index) {
        return path + "/" + GetFiles()[index];
    }

}
