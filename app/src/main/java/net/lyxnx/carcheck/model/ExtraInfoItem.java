package net.lyxnx.carcheck.model;

import java.util.HashMap;
import java.util.Map;

public class ExtraInfoItem {
    private final String header;
    private final Map<String, String> items;

    public ExtraInfoItem(String header) {
        this.header = header;
        this.items = new HashMap<>();
    }

    public String getHeader() {
        return header;
    }

    public Map<String, String> getItems() {
        return items;
    }

    public void addItem(String header, String value) {
        this.items.put(header, value);
    }
}