package net.lyxnx.carcheck.managers;

import android.content.Context;

import net.lyxnx.carcheck.model.SavedVehicle;
import net.lyxnx.carcheck.model.VehicleInfo;

import java.io.File;
import java.util.List;

public class SavedVehicleManager extends FileBasedManager {

    public SavedVehicleManager(Context context) {
        super(new File(context.getFilesDir(), "saved_vehicles.json"));
    }

    public void remove(VehicleInfo info) {
        List<SavedVehicle> vehicles = getSavedVehicles0();

        if (vehicles.removeIf(sv -> sv.getVrm().equals(info.getVrm()))) {
            getSavedVehicles().setValue(vehicles);
            updateSavedVehicles();
        }
    }
}