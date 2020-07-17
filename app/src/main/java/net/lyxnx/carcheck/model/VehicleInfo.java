package net.lyxnx.carcheck.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

public class VehicleInfo implements Parcelable {
    private final Map<Attribute, String> attributes;
    private final String motStatus;
    private final TaxInfo taxInfo;

    public VehicleInfo(Map<Attribute, String> attributes, String motStatus, TaxInfo taxInfo) {
        this.attributes = attributes;
        this.motStatus = motStatus;
        this.taxInfo = taxInfo;
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

    public String getReg() {
        return attributes.get(Attribute.REG);
    }

    public String getMotStatus() {
        return motStatus;
    }

    public TaxInfo getTaxInfo() {
        return taxInfo;
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

        parcel.writeString(motStatus);
        parcel.writeParcelable(taxInfo, flags);
    }

    public VehicleInfo(Parcel parcel) {
        int size = parcel.readInt();

        attributes = new HashMap<>(size);
        for (int i = 0; i < size; i++) {
            attributes.put(Attribute.valueOf(parcel.readString()), parcel.readString());
        }

        motStatus = parcel.readString();
        taxInfo = parcel.readParcelable(getClass().getClassLoader());
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