package net.lyxnx.reginfo.tasks;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import lombok.Data;
import net.lyxnx.reginfo.BuildConfig;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class History {
    private static final String TAG = "HISTORY";
    
    //////////////////////////////////////
    
    private History() {}
    
    private static volatile History instance;
    
    public static History getInstance() {
        if (instance == null) {
            synchronized (History.class) {
                if (instance == null) {
                    instance = new History();
                }
            }
        }
        
        return instance;
    }
    
    //////////////////////////////////////
    
    private static final String HISTORY_FILE = "history";
    
    private List<HistoryItem> items;
    
    private boolean initialised = false;
    
    public void initialise(Context context) {
        // We only want to do initialisation once
        if (initialised) {
            return;
        }
        
        readItems(context)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        historyItems -> History.this.items = historyItems,
                        throwable -> {
                            if (BuildConfig.DEBUG) {
                                Log.e(TAG, "Could not read data from history file", throwable);
                            }
                        }
                );
        
        initialised = true;
    }
    
    private Flowable<List<HistoryItem>> readItems(Context context) {
        File historyFile = new File(context.getFilesDir(), HISTORY_FILE);
        
        if (!historyFile.exists()) {
            try {
                historyFile.createNewFile();
            } catch (IOException e) {
                return Flowable.error(e);
            }
        }
        
        List<HistoryItem> historyItems;
        
        Gson gson = new Gson();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(historyFile))) {
            List<HistoryItem> list = gson.fromJson(reader, new TypeToken<List<HistoryItem>>(){}.getType());
            
            historyItems = list == null ? new ArrayList<>() : list;
        } catch (IOException e) {
            return Flowable.error(e);
        }
        
        return Flowable.just(historyItems);
    }
    
    public boolean isEmpty() {
        return items.isEmpty();
    }
    
    public void clear() {
        this.items.clear();
    }
    
    public void insert(String vrm) {
        LocalDateTime now = LocalDateTime.now();
        String date = now.format(DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm"));
        
        this.items.add(new HistoryItem(vrm, date));
    }
    
    public void remove(HistoryItem item) {
        this.items.remove(item);
    }
    
    public void save(Context context) {
        save0(context)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        v -> {},
                        t -> {
                            if (BuildConfig.DEBUG) {
                                Log.e(TAG, "Could not read data from history file", t);
                            }
                            
                            Toast.makeText(context, "Could not save history to file: " + t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                );
    }
    
    private Flowable<Void> save0(Context context) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(context.getFilesDir(), "history")))) {
            new Gson().toJson(this.items, writer);
            return Flowable.empty();
        } catch (IOException e) {
            return Flowable.error(e);
        }
    }
    
    /**
     * Returns a copy of the list, don't use any update methods from this
     * This will reverse it for display
     */
    public List<HistoryItem> getOrderedItems() {
        List<HistoryItem> list = new ArrayList<>(items);
        Collections.reverse(list);
        return list;
    }
    
    @Data
    public static class HistoryItem {
        private final String vrm;
        private final String date;
    }
}