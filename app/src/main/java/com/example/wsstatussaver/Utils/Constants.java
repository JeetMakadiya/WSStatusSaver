package com.example.wsstatussaver.Utils;

import android.os.Environment;

import java.io.File;

public class Constants {
    public static final File STATUS_DIRECTORY = new File(Environment.getExternalStorageDirectory() +File.separator +"WhatsApp/Media/.Statuses");
    public static final String APP_DIR = Environment.getExternalStorageDirectory() +File.separator +"Status-saver";
    public static final int THUMBSIZE = 128;
}
