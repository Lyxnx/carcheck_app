package net.lyxnx.carcheck.rest.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Vehicle(
        val attributes: MutableMap<Attribute, String>,
        val motStatus: StatusItem?,
        val taxStatus: StatusItem?,
        val emissions: Emissions?,
        val insuranceGroup: String?,
        val economy: Economy?,
        val performance: Performance?
) : Parcelable {

    val make
        get() = attributes[Attribute.MANUFACTURER]
    val model
        get() = attributes[Attribute.MODEL]
    val colour
        get() = attributes[Attribute.COLOUR]
    val vehicleType
        get() = attributes[Attribute.VEHICLE_TYPE]
    val fuelType
        get() = attributes[Attribute.FUEL_TYPE]
    val bhp
        get() = attributes[Attribute.BHP]
    val engineSize
        get() = attributes[Attribute.ENGINE_SIZE]
    val euroStatus
        get() = attributes[Attribute.EURO_STATUS]
    val registeredDate
        get() = attributes[Attribute.REGISTERED_DATE]
    val v5CIssueDate
        get() = attributes[Attribute.V5C_ISSUE_DATE]
    val registryLocation
        get() = attributes[Attribute.REGISTERED_NEAR]
    val vrm
        get() = attributes[Attribute.VRM]!!

}