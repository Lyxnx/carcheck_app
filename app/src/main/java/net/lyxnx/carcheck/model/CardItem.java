package net.lyxnx.carcheck.model;

import java.util.HashMap;
import java.util.Map;

public class CardItem {
    private final String header;
    private Map<String, String> items;

    public CardItem(String header) {
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