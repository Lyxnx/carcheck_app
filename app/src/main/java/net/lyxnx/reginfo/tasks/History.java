package net.lyxnx.reginfo.tasks;

import android.content.Context;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class History {
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
    
    private List<HistoryItem> items;
    
    private History() {}
    
    private boolean inited = false;
    public void init(Context context) {
        // only want to do initialisation once
        if (inited)
            return;
        
        File historyFile = new File(context.getFilesDir(), "history");
        
        if (!historyFile.exists()) {
            try {
                historyFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        Gson gson = new Gson();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(historyFile))) {
            List<HistoryItem> list = gson.fromJson(reader, new TypeToken<List<HistoryItem>>(){}.getType());
            
            if (list == null) {
                this.items = new ArrayList<>();
            } else {
                this.items = list;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        inited = true;
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
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(context.getFilesDir(), "history")))) {
            new Gson().toJson(this.items, writer);
        } catch (IOException ex) {
            ex.printStackTrace();
            Toast.makeText(context, "Could not save history to file: " + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    
    /**
     * Returns a copy of the list, don't use any update methods from this
     * This will reverse it for display
     */
    public List<HistoryItem> getItems() {
        List<HistoryItem> list = new ArrayList<>(items);
        Collections.reverse(list);
        return list;
    }
    
    public static class HistoryItem {
        private final String vrm;
        private final String date;
    
        HistoryItem(String vrm, String date) {
            this.vrm = vrm;
            this.date = date;
        }
    
        public String getVrm() {
            return vrm;
        }
    
        public String getDate() {
            return date;
        }
    }
}