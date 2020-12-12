package net.lyxnx.carcheck.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import net.lyxnx.carcheck.BuildConfig
import net.lyxnx.carcheck.rest.model.Vehicle
import net.lyxnx.carcheck.rest.request.GetVehicleTask

class VehicleDetailsViewModel(application: Application) : AndroidViewModel(application) {

    val vehicleDetails = MutableLiveData<Vehicle>()
    val vehicleError = MutableLiveData<String>()

    fun getVehicleDetails(vrm: String) {
        GetVehicleTask(getApplication(), vrm).observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    if (BuildConfig.DEBUG) {
                        Log.i("VehicleDetailsViewModel", "Vehicle details: $response")
                    }

                    if (response.success) {
                        vehicleDetails.value = response.vehicle
                    } else if (!response.error.isNullOrEmpty()) {
                        vehicleError.value = response.error
                    }
                }, { throwable ->
                    vehicleError.value = throwable.message
                })
    }
}