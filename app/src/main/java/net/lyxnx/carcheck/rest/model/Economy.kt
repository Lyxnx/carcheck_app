package net.lyxnx.carcheck.rest.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Economy(val urban: String, val extraUrban: String, val combined: String) : Parcelable