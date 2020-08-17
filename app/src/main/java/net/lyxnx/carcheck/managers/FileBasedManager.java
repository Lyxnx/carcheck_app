package net.lyxnx.carcheck.managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import net.lyxnx.carcheck.model.SavedVehicle;
import net.lyxnx.carcheck.model.VehicleInfo;
import net.lyxnx.carcheck.util.gson.LocalDateTimeAdapter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.MutableLiveData;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FileBasedManager {

    private final MutableLiveData<List<SavedVehicle>> savedVehicles = new MutableLiveData<>();

    private final File file;
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    public FileBasedManager(File file) {
        this.file = file;
    }

    public void loadSavedVehiclesResponse() {
        Flowable.defer(() -> {
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                    return Flowable.empty();
                }
            }

            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                List<SavedVehicle> items = gson.fromJson(reader, new TypeToken<List<SavedVehicle>>() {}.getType());

                if (items == null) {
                    items = new ArrayList<>();
                }

                return Flowable.just(items);
            } catch (IOException e) {
                e.printStackTrace();
                return Flowable.empty();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .defaultIfEmpty(new ArrayList<>())
                .subscribe(savedVehicles::setValue);
    }

    public void updateSavedVehicles() {
        if (savedVehicles.getValue() == null) {
            return;
        }

        Flowable.defer(() -> {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                gson.toJson(savedVehicles.getValue(), writer);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return Flowable.empty();
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public MutableLiveData<List<SavedVehicle>> getSavedVehicles() {
        return savedVehicles;
    }

    protected List<SavedVehicle> getSavedVehicles0() {
        List<SavedVehicle> vehicles = getSavedVehicles().getValue();

        if (vehicles == null) {
            vehicles = new ArrayList<>();
        }

        return vehicles;
    }

    public void insert(VehicleInfo info) {
        List<SavedVehicle> vehicles = getSavedVehicles0();
        vehicles.add(SavedVehicle.ofNow(info));

        getSavedVehicles().setValue(vehicles);
        updateSavedVehicles();
    }

    public boolean contains(String vrm) {
        return getSavedVehicles0().stream()
                .anyMatch(sv -> sv.getVrm().equalsIgnoreCase(vrm));
    }

    public void update(VehicleInfo info) {
        List<SavedVehicle> vehicles = getSavedVehicles0();

        if (vehicles.isEmpty()) {
            return;
        }

        SavedVehicle sv = vehicles.stream()
                .filter(saved -> saved.getVrm().equals(info.getVrm()))
                .findFirst()
                .orElse(null);

        if (sv == null) {
            return;
        }

        sv.setInfo(info);
    }

    public void clear() {
        List<SavedVehicle> vehicles = getSavedVehicles0();

        if (vehicles.isEmpty()) {
            return;
        }

        vehicles.clear();
        getSavedVehicles().setValue(vehicles);
        updateSavedVehicles();
    }

    public boolean isEmpty() {
        return getSavedVehicles0().isEmpty();
    }
}