package net.lyxnx.carcheck.model;

import java.time.LocalDateTime;

public class SavedVehicle {
    private final String vrm;
    private final LocalDateTime date;
    private final String vehicleType;
    private VehicleInfo info;

    public SavedVehicle(String vrm, LocalDateTime date, String vehicleType) {
        this.vrm = vrm;
        this.date = date;
        this.vehicleType = vehicleType;
    }

    public SavedVehicle(VehicleInfo info, LocalDateTime date) {
        this(info.getReg(), date, info.getVehicleType());
        this.info = info;
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

    public VehicleInfo getInfo() {
        return info;
    }

    public static SavedVehicle ofNow(VehicleInfo info) {
        return new SavedVehicle(info, LocalDateTime.now());
    }
}