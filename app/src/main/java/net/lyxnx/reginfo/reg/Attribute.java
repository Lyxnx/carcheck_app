package net.lyxnx.reginfo.reg;

import java.util.Arrays;
import java.util.function.Function;

public enum Attribute {
    MANUFACTURER,
    MODEL,
    COLOUR,
    ENGINE_SIZE,
    BHP(data -> data.substring(0, data.indexOf("B"))),
    REGISTERED_DATE(data -> data.substring(data.length() - 4)),
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
                .findFirst().orElse(null);
    }
}