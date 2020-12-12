package net.lyxnx.carcheck.rest.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Performance(val topSpeed: String, val acceleration: String?) : Parcelable