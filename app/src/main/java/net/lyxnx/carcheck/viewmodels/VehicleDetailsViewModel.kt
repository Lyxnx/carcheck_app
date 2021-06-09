package net.lyxnx.carcheck.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import net.lyxnx.carcheck.rest.model.Vehicle
import net.lyxnx.carcheck.rest.request.GetVehicleTask
import net.lyxnx.carcheck.util.UiUtil

class VehicleDetailsViewModel(application: Application) : AndroidViewModel(application) {

    val vehicleDetails = MutableLiveData<Vehicle>()
    val vehicleError = MutableLiveData<String>()

    fun getVehicleDetails(vrm: String) {
        GetVehicleTask(vrm).observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.success) {
                        vehicleDetails.value = it.vehicle
                    } else if (!it.error.isNullOrEmpty()) {
                        vehicleError.value = it.error
                    } else {
                        vehicleError.value = UiUtil.getGenericErrorMessage(getApplication())
                    }
                }, {
                    vehicleError.value = it.message
                })
    }
}