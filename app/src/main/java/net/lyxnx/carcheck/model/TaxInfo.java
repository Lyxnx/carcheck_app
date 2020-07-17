package net.lyxnx.carcheck.model;

import android.os.Parcel;
import android.os.Parcelable;

public class TaxInfo implements Parcelable {
    private final String status;

    private final String cost;
    private final String co2Output;

    public TaxInfo(String status, String cost, String co2Output) {
        this.status = status;
        this.cost = cost;
        this.co2Output = co2Output;
    }

    public String getStatus() {
        return status;
    }

    public String getCost() {
        return cost;
    }

    public String getCo2Output() {
        return co2Output;
    }

    public TaxInfo(Parcel parcel) {
        this.status = parcel.readString();
        this.cost = parcel.readString();
        this.co2Output = parcel.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.status);
        parcel.writeString(this.cost);
        parcel.writeString(this.co2Output);
    }

    public static final Parcelable.Creator<TaxInfo> CREATOR = new Parcelable.Creator<TaxInfo>() {

        @Override
        public TaxInfo createFromParcel(Parcel parcel) {
            return new TaxInfo(parcel);
        }

        @Override
        public TaxInfo[] newArray(int i) {
            return new TaxInfo[i];
        }
    };
}