package net.lyxnx.carcheck.model;

import java.util.Arrays;
import java.util.function.Function;

public enum Attribute {
    MANUFACTURER,
    MODEL,
    COLOUR,
    ENGINE_SIZE,
    EURO_STATUS,
    // need the contains check since some BHPs show as "Not Available", causing an exception
    BHP(data -> data.contains("B") ? data.substring(0, data.indexOf("B")) : data),
    REGISTERED_DATE(data -> data.substring(data.length() - 4)),
    V5C_ISSUE_DATE,
    REGISTERED_NEAR,
    REG;
    
    private final Function<String, String> mutator;
    
    Attribute() {
        this.mutator = s -> s;
    }
    
    Attribute(Function<String, String> mutator) {
        this.mutator = mutator;
    }
    
    public String mutate(String data) {
        return this.mutator.apply(data);
    }
    
    public static Attribute of(String string) {
        return Arrays.stream(values())
                .filter(a -> a.name().equalsIgnoreCase(string.replace(" ", "_")))
                .findFirst()
                .orElse(null);
    }
}