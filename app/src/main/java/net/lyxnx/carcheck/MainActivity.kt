package net.lyxnx.carcheck

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import net.lyxnx.carcheck.dialog.HistoryBottomSheetDialog
import net.lyxnx.carcheck.util.hideKeyboard
import net.lyxnx.carcheck.util.showError
import net.lyxnx.carcheck.util.toggleProgress
import net.lyxnx.carcheck.viewmodels.HistoryViewModel
import net.lyxnx.carcheck.viewmodels.VehicleDetailsViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var vehicleDetailsViewModel: VehicleDetailsViewModel
    private lateinit var historyViewModel: HistoryViewModel

    private lateinit var historyDialog: HistoryBottomSheetDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        vehicleDetailsViewModel = ViewModelProvider(this).get(VehicleDetailsViewModel::class.java)
        historyViewModel = ViewModelProvider(this).get(HistoryViewModel::class.java)

        historyDialog = HistoryBottomSheetDialog()

        val go = findViewById<Button>(R.id.buttonGo)
        val input = findViewById<EditText>(R.id.input)

        input.setOnEditorActionListener { _, actionId: Int, _ ->
            if (actionId != EditorInfo.IME_ACTION_DONE) {
                return@setOnEditorActionListener false
            }

            go.performClick()
            return@setOnEditorActionListener true
        }

        go.setOnClickListener {
            val text = input.text.toString()

            if (text.isEmpty()) {
                Toast.makeText(this, R.string.empty_reg, Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            hideKeyboard(it)

            toggleProgress(true)
            vehicleDetailsViewModel.getVehicleDetails(text)
        }

        val calcs = findViewById<Button>(R.id.buttonCalculators)
        calcs.setOnClickListener {
            startActivity(Intent(this@MainActivity, CalculatorsActivity::class.java))
        }

        val historyButton = findViewById<Button>(R.id.buttonHistory)
        historyButton.setOnClickListener {
            if (historyViewModel.isEmpty()) {
                Toast.makeText(this, getString(R.string.empty_history), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!historyDialog.isAdded) {
                historyDialog.show(supportFragmentManager, HistoryBottomSheetDialog::class.java.simpleName)
            }
        }

        historyDialog.selectedListener.subscribe { vehicle ->
            val i = Intent(this, VehicleInfoActivity::class.java)
            i.putExtra(VehicleInfoActivity.PARAM_VEHICLE, vehicle.vehicle)
            startActivity(i)
        }

        historyDialog.clearListener.subscribe {
            historyViewModel.clear()
        }

        setupObservers()
        historyViewModel.loadVehicleHistory()
    }

    private fun setupObservers() {
        vehicleDetailsViewModel.vehicleDetails.observe(this, { vehicle ->
            toggleProgress(false)

            historyViewModel.push(vehicle)

            val intent = Intent(this, VehicleInfoActivity::class.java)
            intent.putExtra(VehicleInfoActivity.PARAM_VEHICLE, vehicle)
            startActivity(intent)
        })

        vehicleDetailsViewModel.vehicleError.observe(this, { error ->
            toggleProgress(false)
            showError(error)
        })

        historyViewModel.readErrorMessage.observe(this, { error -> showError(error) })

        historyViewModel.writeErrorMessage.observe(this, { error -> showError(error) })

        historyViewModel.vehicleHistory.observe(this, { history ->
            historyDialog.vehicleHistory = history
        })
    }
}