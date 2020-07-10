package net.lyxnx.carcheck.util;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import lombok.Data;

public class History extends ArrayList<History.Item> {
    
    private static final String TAG = History.class.getSimpleName();
    
    private static History instance;
    
    public static History getHistory() {
        if (instance == null) {
            synchronized (History.class) {
                if (instance == null) {
                    instance = new History();
                }
            }
        }
        
        return instance;
    }
    
    private History() {
        super();
    }
    
    private History(List<History.Item> items) {
        super(items);
    }
    
    private File historyFile;
    
    public void initialise(Context context) {
        this.historyFile = new File(context.getDataDir(), "history");
        
        readItems()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(
                        this::addAll,
                        RxUtils.ERROR_CONSUMER.apply(TAG)
                );
    }
    
    public void insert(String reg) {
        this.add(new History.Item(reg, LocalDateTime.now()));
    }

    @Override
    public boolean add(Item item) {
        super.add(0, item);

        saveItems();

        return true;
    }

    @Override
    public Item remove(int index) {
        Item val = super.remove(index);

        saveItems();

        return val;
    }

    @Override
    public void clear() {
        super.clear();
        
        saveItems();
    }

    public List<History.Item> getOrderedItems() {
        List<History.Item> items = new ArrayList<>(this);
        Collections.reverse(items);
        return items;
    }
    
    private void saveItems() {
        Flowable.defer(() -> {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(historyFile))) {
                new Gson().toJson(this, writer);
                return Flowable.empty();
            } catch (IOException ex) {
                return Flowable.error(ex);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(
                        RxUtils.EMPTY_CONSUMER,
                        RxUtils.ERROR_CONSUMER.apply(TAG)
                );
    }
    
    private Flowable<History> readItems() {
        return Flowable.defer(() -> {
            if (!historyFile.exists()) {
                try {
                    historyFile.createNewFile();
                    return Flowable.just(new History());
                } catch (IOException e) {
                    return Flowable.error(e);
                }
            }
            
            Gson gson = new Gson();
            
            try (BufferedReader reader = new BufferedReader(new FileReader(historyFile))) {
                List<History.Item> items = gson.fromJson(
                        reader,
                        new TypeToken<List<History.Item>>() {}.getType()
                );
                
                return Flowable.just(new History(items));
            } catch (IOException e) {
                return Flowable.error(e);
            }
        });
    }

    @Data
    public static class Item {
        private final String vrm;
        private final LocalDateTime date;
    }
}