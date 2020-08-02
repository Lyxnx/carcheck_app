package net.lyxnx.carcheck.model;

import android.os.Parcel;
import android.os.Parcelable;

public class StatusItem implements Parcelable {
    private final String status;
    private final String daysLeft;

    public StatusItem(String status, String daysLeft) {
        this.status = status;
        this.daysLeft = daysLeft;
    }

    public String getStatus() {
        return status;
    }

    public String getDaysLeft() {
        return daysLeft;
    }

    public StatusItem(Parcel parcel) {
        this.status = parcel.readString();
        this.daysLeft = parcel.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.status);
        parcel.writeString(this.daysLeft);
    }

    public static final Parcelable.Creator<StatusItem> CREATOR = new Parcelable.Creator<StatusItem>() {

        @Override
        public StatusItem createFromParcel(Parcel parcel) {
            return new StatusItem(parcel);
        }

        @Override
        public StatusItem[] newArray(int i) {
            return new StatusItem[i];
        }
    };
}