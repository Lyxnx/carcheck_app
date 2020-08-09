package net.lyxnx.carcheck.managers;

import android.content.Context;

import java.io.File;

public class HistoryManager extends FileBasedManager {

    public HistoryManager(Context context) {
        super(new File(context.getFilesDir(), "history.json"));
    }
}