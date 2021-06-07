package net.lyxnx.carcheck.auto

import androidx.car.app.CarContext
import androidx.car.app.CarToast
import androidx.car.app.Screen
import androidx.car.app.model.Action
import androidx.car.app.model.SearchTemplate
import androidx.car.app.model.Template
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import net.lyxnx.carcheck.R
import net.lyxnx.carcheck.rest.model.Vehicle
import net.lyxnx.carcheck.rest.request.GetVehicleTask

class CarCheckHomeScreen(context: CarContext) : Screen(context) {

    private var vehicleData: Vehicle? = null
    private var vehicleError: String? = null

    override fun onGetTemplate(): Template {
        return SearchTemplate.Builder(SearchCallback(carContext)).apply {
            setHeaderAction(Action.APP_ICON)
            setSearchHint(carContext.getString(R.string.enter_reg))
        }.build()
    }

    inner class SearchCallback(private val carContext: CarContext) : SearchTemplate.SearchCallback {
        override fun onSearchTextChanged(searchText: String) {
        }

        override fun onSearchSubmitted(searchText: String) {
            getVehicleDetails(searchText)
        }
    }

    private fun getVehicleDetails(vrm: String) {
        GetVehicleTask(vrm).observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.success) {
                        vehicleData = it.vehicle
                    } else if (!it.error.isNullOrEmpty()) {
                        vehicleError = it.error
                    }
                }, {
                    vehicleError = it.message
                })
    }
}