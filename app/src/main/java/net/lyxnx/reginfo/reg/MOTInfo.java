package net.lyxnx.reginfo.reg;

import android.os.Parcel;
import android.os.Parcelable;
import lombok.Data;

@Data
public class MOTInfo implements Parcelable {
    private final String status;
    private final String daysLeft;
    
    private MOTInfo(Parcel parcel) {
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
    
    public static final Parcelable.Creator<MOTInfo> CREATOR = new Parcelable.Creator<MOTInfo>() {
        
        @Override
        public MOTInfo createFromParcel(Parcel parcel) {
            return new MOTInfo(parcel);
        }
        
        @Override
        public MOTInfo[] newArray(int i) {
            return new MOTInfo[i];
        }
    };
}