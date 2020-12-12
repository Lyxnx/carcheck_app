package net.lyxnx.carcheck

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TableLayout
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import net.lyxnx.carcheck.rest.model.Vehicle
import net.lyxnx.carcheck.util.*
import net.lyxnx.carcheck.viewmodels.HistoryViewModel
import net.lyxnx.carcheck.viewmodels.VehicleDetailsViewModel

class VehicleInfoActivity : InfoActivity() {

    companion object {
        val TAG = VehicleInfoActivity::class.java.simpleName
        const val PARAM_VEHICLE = "vehicle"
    }

    private var vehicle: Vehicle? = null

    private lateinit var vehicleDetailsViewModel: VehicleDetailsViewModel
    private lateinit var historyViewModel: HistoryViewModel

    private lateinit var regInput: EditText
    private lateinit var infoTable: TableLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        vehicleDetailsViewModel = ViewModelProvider(this).get(VehicleDetailsViewModel::class.java)
        historyViewModel = ViewModelProvider(this).get(HistoryViewModel::class.java)

        if (intent != null && !intent.hasExtra(PARAM_VEHICLE)) {
            Log.e(TAG, "$PARAM_VEHICLE intent extra not passed in; finishing")
            finish();
            return
        }

        vehicle = intent.getParcelableExtra(PARAM_VEHICLE)

        // Shouldn't be null at this point but just in case something has gone horribly wrong
        if (vehicle == null) {
            showError(getString(R.string.null_info))
            finish()
            return
        }

        title = vehicle!!.vrm

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        regInput = findViewById(R.id.reg)
        infoTable = findViewById(R.id.infoTable)

        populateTable()

        regInput.setText(vehicle!!.vrm)

        regInput.setOnEditorActionListener { v, actionId: Int, _ ->
            if (actionId != EditorInfo.IME_ACTION_DONE) {
                return@setOnEditorActionListener false
            }

            val text = regInput.text.toString()

            if (text.isEmpty()) {
                Toast.makeText(this, R.string.empty_reg, Toast.LENGTH_LONG).show()
                return@setOnEditorActionListener false
            }

            hideKeyboard(v)

            toggleProgress(true)

            vehicleDetailsViewModel.getVehicleDetails(text)

            return@setOnEditorActionListener true
        }

        setupObservers()
        historyViewModel.loadVehicleHistory()
    }

    private fun setupObservers() {
        vehicleDetailsViewModel.vehicleDetails.observe(this, { vehicle ->
            toggleProgress(false)

            this.vehicle = vehicle
            historyViewModel.push(vehicle)

            title = vehicle.vrm
            populateTable()
        })

        vehicleDetailsViewModel.vehicleError.observe(this, { error -> showError(error) })

        historyViewModel.readErrorMessage.observe(this, { error -> showError(error) })

        historyViewModel.writeErrorMessage.observe(this, { error -> showError(error) })
    }

    private fun populateTable() {
        if (infoTable.childCount > 0) {
            infoTable.removeAllViews()
        }

        vehicle!!.make?.let { infoTable.add(getString(R.string.make), it) }
        vehicle!!.model?.let { infoTable.add(getString(R.string.model), it) }
        vehicle!!.colour?.let { infoTable.add(getString(R.string.colour), it) }
        vehicle!!.vehicleType?.let { infoTable.add(getString(R.string.type), it) }
        vehicle!!.bhp?.let { infoTable.add(getString(R.string.bhp), it) }
        vehicle!!.engineSize?.let { infoTable.add(getString(R.string.engine_size), it) }
        vehicle!!.fuelType?.let { infoTable.add(getString(R.string.fuel_type), it) }
        vehicle!!.registeredDate?.let { infoTable.add(getString(R.string.year), it) }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (vehicle != null) {
            menuInflater.inflate(R.menu.menu_info, menu)
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_more -> {
                val i = Intent(this, MoreInfoActivity::class.java)
                i.putExtra(PARAM_VEHICLE, vehicle)
                startActivity(i)
                return true
            }
            R.id.action_gallery -> {
                val make = vehicle!!.make!!
                val model = vehicle!!.model!!
                val colour = vehicle!!.colour!!
                val year = vehicle!!.registeredDate!!

                if (listOf(make, model, year).any { it.isEmpty() || it == "N/A" }) {
                    Toast.makeText(this, R.string.insufficient_vrm_info, Toast.LENGTH_SHORT).show()
                    return true
                }

                CustomTabs.launchUrl(this,
                        "http://google.com/search?tbm=isch&q=".join("+",
                                make, model.replace(" ", "+"), colour, year)
                )

                return true
            }
            android.R.id.home -> {
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}