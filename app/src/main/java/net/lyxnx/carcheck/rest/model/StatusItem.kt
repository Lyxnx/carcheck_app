package net.lyxnx.carcheck.rest.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class StatusItem(val status: String?, val daysLeft: String?) : Parcelable