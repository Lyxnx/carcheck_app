package net.lyxnx.carcheck.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

public class VehicleInfo implements Parcelable {
    private final Map<Attribute, String> attributes;
    private final StatusItem motStatus;
    private final StatusItem taxStatus;
    private final CO2Info co2Info;
    private final String insuranceGroup;
    private final FuelEconomy fuelEconomy;
    private final VehiclePerformance performance;

    public VehicleInfo(Map<Attribute, String> attributes, StatusItem motStatus, StatusItem taxStatus,
                       CO2Info co2Info, String insuranceGroup, FuelEconomy fuelEconomy,
                       VehiclePerformance performance) {
        this.attributes = attributes;
        this.motStatus = motStatus;
        this.taxStatus = taxStatus;
        this.co2Info = co2Info;
        this.insuranceGroup = insuranceGroup;
        this.fuelEconomy = fuelEconomy;
        this.performance = performance;
    }

    public String getMake() {
        return attributes.get(Attribute.MANUFACTURER);
    }

    public String getModel() {
        return attributes.get(Attribute.MODEL);
    }

    public String getColour() {
        return attributes.get(Attribute.COLOUR);
    }

    public String getVehicleType() {
        return attributes.get(Attribute.VEHICLE_TYPE);
    }

    public String getFuelType() {
        return attributes.get(Attribute.FUEL_TYPE);
    }

    public String getBHP() {
        return attributes.get(Attribute.BHP);
    }

    public String getEngineSize() {
        return attributes.get(Attribute.ENGINE_SIZE);
    }

    public String getEuroStatus() {
        return attributes.get(Attribute.EURO_STATUS);
    }

    public String getRegisteredDate() {
        return attributes.get(Attribute.REGISTERED_DATE);
    }

    public String getV5CIssueDate() {
        return attributes.get(Attribute.V5C_ISSUE_DATE);
    }

    public String getRegistryLocation() {
        return attributes.get(Attribute.REGISTERED_NEAR);
    }

    public String getVrm() {
        return attributes.get(Attribute.REG);
    }

    public StatusItem getMotStatus() {
        return motStatus;
    }

    public StatusItem getTaxStatus() {
        return taxStatus;
    }

    public CO2Info getCo2Info() {
        return co2Info;
    }

    public String getInsuranceGroup() {
        return insuranceGroup;
    }

    public FuelEconomy getFuelEconomy() {
        return fuelEconomy;
    }

    public VehiclePerformance getPerformance() {
        return performance;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(attributes.size());
        for (Map.Entry<Attribute, String> e : attributes.entrySet()) {
            parcel.writeString(e.getKey().name());
            parcel.writeString(e.getValue());
        }

        parcel.writeParcelable(motStatus, flags);
        parcel.writeParcelable(taxStatus, flags);
        parcel.writeParcelable(co2Info, flags);
        parcel.writeString(insuranceGroup);
        parcel.writeParcelable(fuelEconomy, flags);
        parcel.writeParcelable(performance, flags);
    }

    public VehicleInfo(Parcel parcel) {
        int size = parcel.readInt();

        attributes = new HashMap<>(size);
        for (int i = 0; i < size; i++) {
            attributes.put(Attribute.valueOf(parcel.readString()), parcel.readString());
        }

        motStatus = parcel.readParcelable(getClass().getClassLoader());
        taxStatus = parcel.readParcelable(getClass().getClassLoader());
        co2Info = parcel.readParcelable(getClass().getClassLoader());
        insuranceGroup = parcel.readString();
        fuelEconomy = parcel.readParcelable(getClass().getClassLoader());
        performance = parcel.readParcelable(getClass().getClassLoader());
    }

    public static final Parcelable.Creator<VehicleInfo> CREATOR = new Parcelable.Creator<VehicleInfo>() {

        @Override
        public VehicleInfo createFromParcel(Parcel parcel) {
            return new VehicleInfo(parcel);
        }

        @Override
        public VehicleInfo[] newArray(int size) {
            return new VehicleInfo[size];
        }
    };
}