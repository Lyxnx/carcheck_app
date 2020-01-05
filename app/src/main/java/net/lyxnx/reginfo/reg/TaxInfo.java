package net.lyxnx.reginfo.reg;

import java.io.Serializable;

public class TaxInfo implements Serializable {
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
}
