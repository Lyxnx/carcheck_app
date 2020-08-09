package net.lyxnx.carcheck.util;

import android.content.Context;

import net.lyxnx.carcheck.managers.HistoryManager;
import net.lyxnx.carcheck.managers.SavedVehicleManager;

public class Singletons {

    private Singletons() { }

    private static HistoryManager historyManager;
    private static SavedVehicleManager savedVehicleManager;

    public static HistoryManager getHistoryManager(Context context) {
        if (Singletons.historyManager == null) {
            Singletons.historyManager = new HistoryManager(context);
            Singletons.historyManager.loadSavedVehiclesResponse();
        }

        return historyManager;
    }

    public static SavedVehicleManager getSavedVehicleManager(Context context) {
        if (Singletons.savedVehicleManager == null) {
            Singletons.savedVehicleManager = new SavedVehicleManager(context);
            Singletons.savedVehicleManager.loadSavedVehiclesResponse();
        }

        return savedVehicleManager;
    }
}