package net.lyxnx.carcheck.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import net.lyxnx.carcheck.BuildConfig
import net.lyxnx.carcheck.rest.model.Vehicle
import net.lyxnx.carcheck.rest.request.GetVehicleTask

class VehicleDetailsViewModel : ViewModel() {

    val vehicleDetails = MutableLiveData<Vehicle>()
    val vehicleError = MutableLiveData<String>()

    fun getVehicleDetails(vrm: String) {
        GetVehicleTask(vrm).observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (BuildConfig.DEBUG) {
                        Log.i("VehicleDetailsViewModel", "Vehicle details: $it")
                    }

                    if (it.success) {
                        vehicleDetails.value = it.vehicle
                    } else if (!it.error.isNullOrEmpty()) {
                        vehicleError.value = it.error
                    }
                }, {
                    vehicleError.value = it.message
                })
    }
}