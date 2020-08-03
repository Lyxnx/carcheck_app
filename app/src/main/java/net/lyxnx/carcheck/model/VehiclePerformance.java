package net.lyxnx.carcheck.model;

import android.os.Parcel;
import android.os.Parcelable;

public class VehiclePerformance implements Parcelable {
    private final String topSpeed;
    private final String zeroTo60;
    
    public VehiclePerformance(String topSpeed, String zeroTo60) {
        this.topSpeed = topSpeed;
        this.zeroTo60 = zeroTo60;
    }
    
    public String getTopSpeed() {
        return topSpeed;
    }
    
    public String getZeroTo60() {
        return zeroTo60;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(topSpeed);
        parcel.writeString(zeroTo60);
    }

    public VehiclePerformance(Parcel parcel) {
        topSpeed = parcel.readString();
        zeroTo60 = parcel.readString();
    }

    public static final Parcelable.Creator<VehiclePerformance> CREATOR = new Parcelable.Creator<VehiclePerformance>() {
        @Override
        public VehiclePerformance createFromParcel(Parcel parcel) {
            return new VehiclePerformance(parcel);
        }

        @Override
        public VehiclePerformance[] newArray(int i) {
            return new VehiclePerformance[i];
        }
    };
}