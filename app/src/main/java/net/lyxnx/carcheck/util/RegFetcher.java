package net.lyxnx.carcheck.util;

import net.lyxnx.carcheck.model.Attribute;
import net.lyxnx.carcheck.model.MOTInfo;
import net.lyxnx.carcheck.model.TaxInfo;
import net.lyxnx.carcheck.model.VehicleInfo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.rxjava3.core.Flowable;

public class RegFetcher {

    // Omit the www or it'll throw a wobbly
    private static final String URL = "https://totalcarcheck.co.uk/FreeCheck";

    private static final String TR_NOT_COLSPAN = "tr:not(:has(td[colspan]))";
    private static final String REGNO = "regno";

    private static final int VEHICLE_MOT_SUMMARY_TABLE = 6;
    private static final int VEHICLE_TAX_SUMMARY_TABLE = 7;
    private static final int VEHICLE_DETAILS_TABLE = 8;
    private static final int VEHICLE_TAX_DETAIL_TABLE = 11;
    private static final int VEHICLE_EMISSION_TABLE = 12;

    public static Flowable<VehicleInfo> fetchVehicle(String reg) {
        return Flowable.defer(() -> {
            try {
                VehicleInfo info = getVehicleInfo0(reg);

                return info != null ? Flowable.just(info) : Flowable.empty();
            } catch (Exception ex) {
                return Flowable.error(ex);
            }
        });
    }

    private static VehicleInfo getVehicleInfo0(String reg) throws IOException {
        Element root = Jsoup.connect(URL)
                .data(REGNO, reg)
                .get()
                .body();

        Elements tables = root.select("table.basicResults");

        if (tables == null || tables.isEmpty()) {
            return null;
        }

        Element infoTable = tables.get(VEHICLE_DETAILS_TABLE);
        // The title row spans 2 columns, so select all but that
        Elements trs = infoTable.select(TR_NOT_COLSPAN);

        Map<Attribute, String> attributes = new HashMap<>();
        attributes.put(Attribute.REG, reg.toUpperCase());

        for (Element el : trs) {
            Attribute attribute = Attribute.of(el.selectFirst("td.certLabel").text());

            if (attribute == null) {
                continue;
            }

            String data = el.selectFirst("td.certData").text();

            attributes.put(attribute, attribute.mutate(data));
        }

        return new VehicleInfo(attributes, getMotInfo(tables), getTaxInfo(tables));
    }

    private static MOTInfo getMotInfo(Elements tables) {
        Elements data = tables.get(VEHICLE_MOT_SUMMARY_TABLE)
                .select(TR_NOT_COLSPAN);

        String status = getTableRowEntry(data.get(0));
        String daysLeft = getTableRowEntry(data.get(1));

        return new MOTInfo(status, daysLeft);
    }

    private static String getTableRowEntry(Element row) {
        return row.selectFirst("td span").text();
    }

    private static TaxInfo getTaxInfo(Elements tables) {
        Elements summaryData = tables.get(VEHICLE_TAX_SUMMARY_TABLE)
                .select(TR_NOT_COLSPAN);

        String status = getTableRowEntry(summaryData.get(0));
        String daysLeft = getTableRowEntry(summaryData.get(1));

        Elements taxInfoTable = tables.get(VEHICLE_TAX_DETAIL_TABLE)
                .select(TR_NOT_COLSPAN);

        Element costElement = taxInfoTable.select("td span").first();
        // Is null if the tax info is unavailable
        if (costElement == null) {
            return null;
        }

        String cost = costElement.text();
        String co2Output = tables.get(VEHICLE_EMISSION_TABLE).selectFirst("td span").text();

        return new TaxInfo(status, daysLeft, cost, co2Output);
    }
}