package net.lyxnx.carcheck.model;

import android.os.Parcel;
import android.os.Parcelable;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TaxInfo implements Parcelable {
    private final String status;
    private final String daysLeft;
    
    private final String cost;
    private final String co2Output;
    
    public TaxInfo(Parcel parcel) {
        this.status = parcel.readString();
        this.daysLeft = parcel.readString();
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
        parcel.writeString(this.daysLeft);
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