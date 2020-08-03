package net.lyxnx.carcheck.model;

import android.os.Parcel;
import android.os.Parcelable;

public class FuelEconomy implements Parcelable {
    private final String urbanMpg;
    private final String extraUrbanMpg;
    private final String combined;

    public FuelEconomy(String urbanMpg, String extraUrbanMpg, String combined) {
        this.urbanMpg = urbanMpg;
        this.extraUrbanMpg = extraUrbanMpg;
        this.combined = combined;
    }

    public String getUrbanMpg() {
        return urbanMpg;
    }

    public String getExtraUrbanMpg() {
        return extraUrbanMpg;
    }

    public String getCombined() {
        return combined;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(urbanMpg);
        parcel.writeString(extraUrbanMpg);
        parcel.writeString(combined);
    }

    public FuelEconomy(Parcel parcel) {
        this.urbanMpg = parcel.readString();
        this.extraUrbanMpg = parcel.readString();
        this.combined = parcel.readString();
    }

    public static final Parcelable.Creator<FuelEconomy> CREATOR = new Parcelable.Creator<FuelEconomy>() {

        @Override
        public FuelEconomy createFromParcel(Parcel parcel) {
            return new FuelEconomy(parcel);
        }

        @Override
        public FuelEconomy[] newArray(int i) {
            return new FuelEconomy[i];
        }
    };
}