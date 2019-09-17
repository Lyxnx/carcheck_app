package net.lyxnx.reginfo.reg;

import net.lyxnx.reginfo.reg.mot.MOTInfo;
import net.lyxnx.reginfo.reg.mot.MOTResult;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegFetcher {
    
    public static MOTInfo getMOTInfo(String reg) {
        try {
            return getMOTInfo0(reg);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private static MOTInfo getMOTInfo0(String reg) throws IOException {
        List<MOTResult> results = new ArrayList<>();
    
        Element body = Jsoup.connect("https://hpicheck.com/mot-history-check/results")
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.119 Safari/537.36")
                .data("vrm", reg)
                .post().body();
        
        if (!body.hasClass("no-hero"))
            return new MOTInfo("N/A", new ArrayList<>());
        
        Elements root = body.select("div.pane--alt div.container div div div.mot-container div.mot-info");
    
        String dueDate = root.select("div.row div.mot-container__title div.box p span").get(1).text();
    
        Elements events = root.select("div.events div.event");
    
        for (Element e : events) {
            Element content  = e.selectFirst("div.content");
        
            String date = content.selectFirst("div.date p").text();
            String mileage = content.selectFirst("div.mileage p span").text();
        
            Element expirySec = content.selectFirst("div.expiry p span");
            String expiry = expirySec == null ? null : expirySec.text();
        
            Element notices = content.selectFirst("div.summary");
        
            Element refusal = notices.selectFirst("div.refusal div.list ol");
            List<String> refusalNotices = new ArrayList<>();
            if (refusal != null)
                refusal.select("li").stream().map(Element::text).forEach(refusalNotices::add);
        
            Element advisories = notices.selectFirst("div.advisory div.list ol");
            List<String> advisoryNotices = new ArrayList<>();
            if (advisories != null)
                advisories.select("li").stream().map(Element::text).forEach(advisoryNotices::add);
        
            results.add(new MOTResult(
                    mileage,
                    date,
                    expiry,
                    refusalNotices,
                    advisoryNotices
            ));
        }
        
        return new MOTInfo(dueDate, results);
    }
    
    public static Map<Attribute, String> getVehicleInfo(String reg) {
        try {
            return getVehicleInfo0(reg);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private static Map<Attribute, String> getVehicleInfo0(String reg) throws IOException {
        Map<Attribute, String> map = new HashMap<>();
        
        Element body = Jsoup.connect("https://www.instantcarcheck.co.uk/product-selection")
                .header("Cookie", "hpi.vrm=" + reg)
                .get().body();
        
        // not a valid reg
        if (!body.select("div.search-reg").isEmpty())
            return null;
        
        // vehicle info
        Elements panes = body.select("div.ui-component div.js-cart-wrapper div.container div.vehicle " +
                "div.row div.col-sm-9 div.vehicle__info--row");
        
        for (Element elem : panes) {
            Elements keys = elem.select("div.col-xs-2");
            Elements vals = elem.select("div.col-xs-4");
            
            for (int i = 0; i < keys.size(); i++) {
                Attribute a = Attribute.of(keys.get(i).text());
                String val = vals.get(i).text();
                
                if (a == null)
                    continue;
                
                if (val.equalsIgnoreCase("N/A")) {
                    map.put(a, "N/A");
                    continue;
                }
                
                switch (a) {
                    case ENGINE_SIZE:
                        val += "cc";
                        break;
                    case BHP:
                        val = String.valueOf((int) Double.parseDouble(val));
                        break;
                }
                
                if (a.shouldCapitalise())
                    val = Utils.capitalizeFully(val);
                
                map.put(a, val);
            }
        }
        
        return map;
    }
}