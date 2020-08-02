package net.lyxnx.carcheck.model;

import android.os.Parcel;
import android.os.Parcelable;

public class CO2Info implements Parcelable {
    private final String cost12Months;
    private final String cost6Months;
    private final String output;
    
    public CO2Info(String cost12Months, String cost6Months, String output) {
        this.cost12Months = cost12Months;
        this.cost6Months = cost6Months;
        this.output = output;
    }
    
    public String getCost12Months() {
        return cost12Months;
    }
    
    public String getCost6Months() {
        return cost6Months;
    }
    
    public String getOutput() {
        return output;
    }

    public CO2Info(Parcel parcel) {
        this.cost12Months = parcel.readString();
        this.cost6Months = parcel.readString();
        this.output = parcel.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.cost12Months);
        parcel.writeString(this.cost6Months);
        parcel.writeString(this.output);
    }

    public static final Parcelable.Creator<CO2Info> CREATOR = new Parcelable.Creator<CO2Info>() {

        @Override
        public CO2Info createFromParcel(Parcel parcel) {
            return new CO2Info(parcel);
        }

        @Override
        public CO2Info[] newArray(int size) {
            return new CO2Info[size];
        }
    };
}