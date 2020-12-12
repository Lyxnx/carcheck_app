package net.lyxnx.carcheck.viewmodels

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import net.lyxnx.carcheck.rest.model.Vehicle
import net.lyxnx.carcheck.rest.request.RequestTask
import net.lyxnx.carcheck.util.gson.LocalDateTimeAdapter
import java.io.*
import java.time.LocalDateTime
import java.util.*

class HistoryViewModel(application: Application) : AndroidViewModel(application) {

    val readErrorMessage = MutableLiveData<String>()
    val writeErrorMessage = MutableLiveData<String>()
    val vehicleHistory = MutableLiveData<MutableList<SavedVehicle>>()

    private val file = File(application.dataDir, "history.json")

    fun loadVehicleHistory() {
        ParseVehicleFileTask(getApplication(), file).observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ vehicles ->
                    vehicleHistory.value = vehicles?.toMutableList() ?: ArrayList()
                }, { throwable ->
                    readErrorMessage.value = throwable.message
                })
    }

    private fun writeVehicleFile() {
        WriteVehicleFileTask(getApplication(), vehicleHistory.value!!, file).observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                }, { throwable ->
                    writeErrorMessage.value = throwable.message
                })
    }

    fun push(vehicle: Vehicle) {
        val vehicles = vehicleHistory.value!!

        vehicles.apply {
            add(SavedVehicle.ofNow(vehicle))
            vehicleHistory.value = this
        }

        writeVehicleFile()
    }

    fun clear() {
        val vehicles = vehicleHistory.value!!

        if (vehicles.isEmpty()) {
            return
        }

        vehicles.apply {
            clear()
            vehicleHistory.value = this
        }

        writeVehicleFile()
    }

    fun isEmpty(): Boolean = vehicleHistory.value!!.isEmpty()

    private class ParseVehicleFileTask(context: Context, private val file: File) : RequestTask<List<SavedVehicle>>(context) {

        private val gson = GsonBuilder()
                .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
                .create()

        override fun buildObservable(context: Context): Observable<List<SavedVehicle>> {
            if (!file.exists()) {
                return try {
                    file.createNewFile()
                    Observable.just(emptyList())
                } catch (e: IOException) {
                    Observable.error(e)
                }
            }

            BufferedReader(FileReader(file)).use {
                val vehicles = gson.fromJson<List<SavedVehicle>>(it, object : TypeToken<List<SavedVehicle>>() {}.type)
                        ?: return Observable.error(IOException("Could not parse vehicle file"))

                return Observable.just(vehicles)
            }
        }
    }

    private class WriteVehicleFileTask(context: Context, private val data: Any, private val file: File) : RequestTask<Unit>(context) {

        private val gson = GsonBuilder()
                .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
                .create()

        override fun buildObservable(context: Context): Observable<Unit> {
            BufferedWriter(FileWriter(file)).use {
                return Observable.just(gson.toJson(data, it))
            }
        }
    }
}

data class SavedVehicle(val vrm: String, val date: LocalDateTime, var vehicle: Vehicle) {
    companion object {
        fun ofNow(vehicle: Vehicle): SavedVehicle {
            return SavedVehicle(vehicle.vrm, LocalDateTime.now(), vehicle)
        }
    }
}