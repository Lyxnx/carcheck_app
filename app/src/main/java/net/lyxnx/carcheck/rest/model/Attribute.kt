package net.lyxnx.carcheck.rest.model

enum class Attribute(val mutator: (String) -> String = { s -> s }) {

    MANUFACTURER,
    MODEL,
    COLOUR,
    VEHICLE_TYPE,
    FUEL_TYPE, ENGINE_SIZE,
    EURO_STATUS,

    // need the contains check since some BHPs show as "Not Available", causing an exception
    // "XXX BHP"
    BHP({ data -> if (data.contains("B")) data.substring(0, data.indexOf("B")) else data }),
    // Eg 01 June 2025, only care about the year
    REGISTERED_DATE({ data -> data.substring(data.length - 4) }),
    V5C_ISSUE_DATE,
    REGISTERED_NEAR,
    VRM;

    fun mutate(data: String): String = mutator(data)

    companion object {
        fun of(string: String): Attribute?
                = values().firstOrNull { a -> a.name.equals(string.replace(" ", "_"), true)}
    }
}