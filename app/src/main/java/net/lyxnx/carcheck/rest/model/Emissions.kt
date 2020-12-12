package net.lyxnx.carcheck.rest.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Emissions(val cost12Months: String, val cost6Months: String, val co2Output: String) : Parcelable