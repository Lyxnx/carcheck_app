package net.lyxnx.reginfo.reg;

import android.os.Parcel;
import android.os.Parcelable;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class VehicleInfo implements Parcelable {
    private final Map<Attribute, String> attributes;
    private final MOTInfo motInfo;
    private final TaxInfo taxInfo;
    
    public String getMake() {
        return attributes.get(Attribute.MANUFACTURER);
    }
    
    public String getModel() {
        return attributes.get(Attribute.MODEL);
    }
    
    public String getColour() {
        return attributes.get(Attribute.COLOUR);
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
        
        parcel.writeParcelable(motInfo, flags);
        parcel.writeParcelable(taxInfo, flags);
    }
    
    private VehicleInfo(Parcel parcel) {
        int size = parcel.readInt();
    
        attributes = new HashMap<>(size);
        for (int i = 0; i < size; i++) {
            attributes.put(Attribute.valueOf(parcel.readString()), parcel.readString());
        }
        
        motInfo = parcel.readParcelable(getClass().getClassLoader());
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