package net.lyxnx.carcheck.util;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class History {

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

    private Gson gson;
    private List<History.Item> items;
    private File historyFile;

    public History() {
        this.items = new ArrayList<>();
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
    }

    public void initialise(Context context) {
        this.historyFile = new File(context.getDataDir(), "history");

        readItems()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(
                        items::addAll,
                        RxUtils.ERROR_CONSUMER.apply(TAG, context)
                );
    }

    public void insert(String reg, String vehicleType) {
        items.add(new History.Item(reg, LocalDateTime.now(), vehicleType));
        saveItems();
    }

    public void remove(int index) {
        items.remove(index);
        saveItems();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public void clear() {
        items.clear();

        saveItems();
    }

    public List<Item> getItems() {
        return items;
    }

    private void saveItems() {
        Flowable.defer(() -> {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(historyFile))) {
                gson.toJson(items, writer);
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
                        RxUtils.ERROR_CONSUMER.apply(TAG, null)
                );
    }

    private Flowable<List<History.Item>> readItems() {
        return Flowable.defer(() -> {
            if (!historyFile.exists()) {
                try {
                    historyFile.createNewFile();
                    return Flowable.just(new ArrayList<>());
                } catch (IOException e) {
                    return Flowable.error(e);
                }
            }

            try (BufferedReader reader = new BufferedReader(new FileReader(historyFile))) {
                List<History.Item> items = gson.fromJson(
                        reader,
                        new TypeToken<List<History.Item>>() {
                        }.getType()
                );

                if (items == null) {
                    items = new ArrayList<>();
                }

                return Flowable.just(items);
            } catch (IOException e) {
                return Flowable.error(e);
            }
        });
    }

    public static class LocalDateTimeAdapter implements JsonDeserializer<LocalDateTime>,
            JsonSerializer<LocalDateTime> {

        @Override
        public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return LocalDateTime.parse(json.getAsString(), DateTimeFormatter.ofPattern(Util.DATE_PATTERN));
        }

        @Override
        public JsonElement serialize(LocalDateTime src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(Util.formatDate(src));
        }
    }

    public static class Item {
        private final String vrm;
        private final LocalDateTime date;
        private final String vehicleType;

        public Item(String vrm, LocalDateTime date, String vehicleType) {
            this.vrm = vrm;
            this.date = date;
            this.vehicleType = vehicleType;
        }

        public String getVrm() {
            return vrm;
        }

        public LocalDateTime getDate() {
            return date;
        }

        public String getVehicleType() {
            return vehicleType;
        }

        @Override
        public String toString() {
            return "Item{" +
                    "vrm='" + vrm + '\'' +
                    ", date=" + date +
                    ", vehicleType='" + vehicleType + '\'' +
                    '}';
        }
    }
}