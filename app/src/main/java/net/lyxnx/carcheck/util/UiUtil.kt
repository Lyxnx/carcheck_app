package net.lyxnx.carcheck.util

import androidx.annotation.DrawableRes
import net.lyxnx.carcheck.R


object UiUtil {

    @DrawableRes
    fun getVehicleType(type: String?): Int {
        if (type.isNullOrEmpty()) {
            return R.drawable.ic_vehicle_car
        }

        return when (type) {
            "HCV" -> R.drawable.ic_vehicle_hgv
            "LCV" -> R.drawable.ic_vehicle_lcv
            "Motorcycle" -> R.drawable.ic_vehicle_motorcycle
            else -> R.drawable.ic_vehicle_car
        }
    }
}