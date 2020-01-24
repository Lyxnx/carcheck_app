package net.lyxnx.reginfo.reg;

import android.util.Log;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RegFetcher {
    
    public static VehicleInfo getVehicleInfo(String reg) {
        try {
            return getVehicleInfo0(reg);
        } catch (IOException e) {
            Log.e("CARCHECK", "Fetch error", e);
            return null;
        }
    }
    
    private static VehicleInfo getVehicleInfo0(String reg) throws IOException {
        Element root = Jsoup.connect("https://totalcarcheck.co.uk/FreeCheck")
                .data("regno", reg)
                .get()
                .body();
        
        Elements tables = root.select("table.basicResults");
        
        if (tables == null || tables.isEmpty()) {
            return null;
        }
        
        Element infoTable = tables.get(8);
        Elements trs = infoTable.select("tr");
        trs.remove(0); // the first item is the Vehicle Details title
        
        Map<Attribute, String> attributes = new HashMap<>();
        
        for (Element el : trs) {
            Attribute attribute = Attribute.of(el.selectFirst("td.certLabel").text());
            
            if (attribute == null) {
                continue;
            }
            
            String data = el.selectFirst("td.certData").text();
            
            attributes.put(attribute, attribute.mutate(data));
        }
        
        attributes.put(Attribute.REG, reg.toUpperCase());
        
        return new VehicleInfo(attributes, getStatus(tables.get(6)), getTaxInfo(tables));
    }
    
    private static String getStatus(Element table) {
        return table.select("tr").get(1).selectFirst("td span").text();
    }
    
    private static TaxInfo getTaxInfo(Elements tables) {
        String status = getStatus(tables.get(7));
        
        Element costEl = tables.get(11).selectFirst("td > span");
        // is null if the tax info is unavailable
        if (costEl == null)
            return null;
        
        String cost = costEl.text();
        String co2Output = tables.get(12).selectFirst("td > span").text();
        
        return new TaxInfo(status, cost, co2Output);
    }
}