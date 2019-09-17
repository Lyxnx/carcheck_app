package net.lyxnx.reginfo.reg;

import java.util.Arrays;

public enum Attribute {
    MAKE,
    MODEL,
    BODY(true),
    COLOUR(true),
    BHP,
    ENGINE_SIZE,
    YEAR,
    REG;
    
    private final boolean capitalise;
    
    Attribute(boolean capitalise) {
        this.capitalise = capitalise;
    }
    
    Attribute() {
        this(false);
    }
    
    public boolean shouldCapitalise() {
        return capitalise;
    }
    
    public static Attribute of(String string) {
        return Arrays.stream(values())
                .filter(a -> a.name().equalsIgnoreCase(string.replace(" ", "_")))
                .findFirst().orElse(null);
    }
}