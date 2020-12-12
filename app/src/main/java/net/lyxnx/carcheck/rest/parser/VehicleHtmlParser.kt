package net.lyxnx.carcheck.rest.parser

import net.lyxnx.carcheck.rest.model.*
import net.lyxnx.carcheck.rest.model.Attribute.Companion.of
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.util.*

object VehicleHtmlParser {

    private const val TR_NOT_COLSPAN = "tr:not(:has(td[colspan]))"
    private const val VEHICLE_REGISTRATION_TABLE = 0
    private const val VEHICLE_MOT_SUMMARY_TABLE = 6
    private const val VEHICLE_TAX_SUMMARY_TABLE = 7
    private const val VEHICLE_DETAILS_TABLE = 8
    private const val VEHICLE_TAX_DETAIL_TABLE = 11
    private const val VEHICLE_EMISSION_TABLE = 12
    private const val INSURANCE_GROUP = 0
    private val INVALID_STATUSES = listOf("Expired", "SORN", "No data available")

    fun parseVehicle(body: Element): ParseStatus {
        val tables = body.select("table.basicResults")

        if (tables.isNullOrEmpty()) {
            return ParseStatus.Error("No vehicle found")
        }

        // The title row spans 2 columns, so select all but that
        val trs = tables[VEHICLE_DETAILS_TABLE].select(TR_NOT_COLSPAN)
        val attributes = EnumMap<Attribute, String>(Attribute::class.java)
        attributes[Attribute.VRM] = tables[VEHICLE_REGISTRATION_TABLE].selectFirst("td.certData").text()

        for (el in trs) {
            val attribute = of(el.selectFirst("td.certLabel").text()) ?: continue

            val data = el.selectFirst("td.certData").text()
            attributes[attribute] = attribute.mutate(data)
        }

        val extraRows = body.selectFirst("table:not([class])").select(TR_NOT_COLSPAN)
        val insuranceGroup = getTableRowEntry(extraRows[INSURANCE_GROUP])

        return ParseStatus.Success(Vehicle(
                attributes, getMotStatus(tables), getTaxInfo(tables), getCo2Info(tables),
                insuranceGroup, getFuelEconomy(extraRows), getPerformance(extraRows)
        ))
    }

    private fun getMotStatus(tables: Elements): StatusItem? {
        val data = tables[VEHICLE_MOT_SUMMARY_TABLE].select(TR_NOT_COLSPAN)
        val status = getTableRowEntry(data[0])

        // If it is expired, data.get(1) will throw an error since there are no days left
        return if (status != null && isInvalidStatus(status)) StatusItem(status, null)
        else StatusItem(status!!, getTableRowEntry(data[1])!!)
    }

    private fun isInvalidStatus(status: String): Boolean {
        return INVALID_STATUSES.any { s -> status.contains(s) }
    }

    private fun getTableRowEntry(row: Element): String? {
        return row.selectFirst("td span")?.text()
    }

    private fun getTaxInfo(tables: Elements): StatusItem {
        val data = tables[VEHICLE_TAX_SUMMARY_TABLE].select(TR_NOT_COLSPAN)
        val status = getTableRowEntry(data[0])

        // If it is expired, data.get(1) will throw an error since there are no days left
        return if (status != null && isInvalidStatus(status)) StatusItem(status, null)
        else StatusItem(status!!, getTableRowEntry(data[1])!!)
    }

    private fun getCo2Info(tables: Elements): Emissions {
        val taxInfoTable = tables[VEHICLE_TAX_DETAIL_TABLE].select(TR_NOT_COLSPAN)

        val costElements = taxInfoTable.select("td span")

        val cost12: String
        val cost6: String

        // Is null if the tax info is unavailable
        if (costElements.isNullOrEmpty()) {
            cost12 = "N/A"
            cost6 = "N/A"
        } else {
            cost12 = costElements[0].text()
            cost6 = costElements[1].text()
        }

        val co2Output = tables[VEHICLE_EMISSION_TABLE].selectFirst("td span").text()
        return Emissions(cost12, cost6, co2Output)
    }

    private fun getFuelEconomy(rows: Elements): Economy? {
        val mpgRows = rows.select("td:contains(mpg)")

        return if (mpgRows.isEmpty()) null
        else Economy(
                getTableRowEntry(mpgRows[0])!!,
                getTableRowEntry(mpgRows[1])!!,
                getTableRowEntry(mpgRows[2])!!
        )
    }

    private fun getPerformance(rows: Elements): Performance? {
        val perfRows = rows.select("td.certData:contains(mph),td.certData:contains(secs)")

        if (perfRows.isEmpty()) {
            return null
        }

        return Performance(getTableRowEntry(perfRows[0])!!, if (perfRows.size == 1) null else getTableRowEntry(perfRows[1]))
    }

    sealed class ParseStatus {
        class Success(val vehicle: Vehicle) : ParseStatus()
        class Error(val error: String) : ParseStatus()
    }
}