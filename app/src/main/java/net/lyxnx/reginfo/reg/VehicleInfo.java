package net.lyxnx.reginfo.reg;

import java.io.Serializable;
import java.util.Map;

public class VehicleInfo implements Serializable {
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
    
    public String getBHP() {
        return attributes.get(Attribute.BHP);
    }
    
    public String getEngineSize() {
        return attributes.get(Attribute.ENGINE_SIZE);
    }
    
    public String getRegisteredDate() {
        return attributes.get(Attribute.REGISTERED_DATE);
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
}