package net.lyxnx.carcheck.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import net.lyxnx.carcheck.rest.model.Vehicle
import net.lyxnx.carcheck.util.gson.LocalDateTimeAdapter
import net.lyxnx.simplerest.request.RequestTask
import java.io.*
import java.nio.file.Files
import java.time.LocalDateTime

class HistoryViewModel(application: Application) : AndroidViewModel(application) {

    val readErrorMessage = MutableLiveData<String>()
    val writeErrorMessage = MutableLiveData<String>()
    val vehicleHistory = MutableLiveData<List<SavedVehicle>>()

    private val file = File(application.dataDir, "history.json")

    fun loadVehicleHistory() {
        ParseVehicleFileTask(file).observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    vehicleHistory.value = it?.toMutableList() ?: ArrayList()
                }, {
                    readErrorMessage.value = it.message
                })
    }

    private fun writeVehicleFile() {
        WriteVehicleFileTask(vehicleHistory.value!!, file).observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({}, {
                    writeErrorMessage.value = it.message
                })
    }

    fun push(vehicle: Vehicle) {
        val vehicles = vehicleHistory.value!!.toMutableList()

        vehicles.apply {
            add(SavedVehicle.ofNow(vehicle))
            vehicleHistory.value = this
        }

        writeVehicleFile()
    }

    fun clear() {
        val vehicles = vehicleHistory.value!!.toMutableList()

        if (vehicles.isEmpty()) {
            return
        }

        vehicles.apply {
            clear()
            vehicleHistory.value = this
        }

        writeVehicleFile()
    }

    fun isEmpty() = vehicleHistory.value!!.isEmpty()

    private class ParseVehicleFileTask(private val file: File) : RequestTask<List<SavedVehicle>>() {

        private val gson = GsonBuilder()
                .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
                .create()

        override fun buildObservable(): Observable<List<SavedVehicle>> {
            if (!file.exists()) {
                return try {
                    file.createNewFile()
                    Files.write(file.toPath(), "[]".toByteArray())
                    Observable.just(emptyList())
                } catch (e: IOException) {
                    Observable.error(e)
                }
            }

            Files.readAllLines(file.toPath()).joinToString(separator = "").let {
                val vehicles = gson.fromJson<List<SavedVehicle>>(it, object : TypeToken<List<SavedVehicle>>() {}.type)
                        ?: return Observable.error(IOException("Could not parse vehicle file"))

                return Observable.just(vehicles)
            }
        }
    }

    private class WriteVehicleFileTask(private val data: Any, private val file: File) : RequestTask<Unit>() {

        private val gson = GsonBuilder()
                .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
                .create()

        override fun buildObservable(): Observable<Unit> {
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