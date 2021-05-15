package net.lyxnx.carcheck

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import net.lyxnx.carcheck.dialog.HistoryBottomSheetDialog
import net.lyxnx.carcheck.util.hideKeyboard
import net.lyxnx.carcheck.util.showError
import net.lyxnx.carcheck.util.toggleProgress
import net.lyxnx.carcheck.viewmodels.HistoryViewModel
import net.lyxnx.carcheck.viewmodels.VehicleDetailsViewModel

class MainActivity : AppCompatActivity() {

    private val vehicleDetailsViewModel: VehicleDetailsViewModel by viewModels()
    private val historyViewModel: HistoryViewModel by viewModels()

    private lateinit var historyDialog: HistoryBottomSheetDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        historyDialog = HistoryBottomSheetDialog()

        val go = findViewById<Button>(R.id.buttonGo)
        val input = findViewById<EditText>(R.id.input)

        input.setOnEditorActionListener { _, actionId, _ ->
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

        historyDialog.selectedListener.subscribe {
            val i = Intent(this, VehicleInfoActivity::class.java)
            i.putExtra(VehicleInfoActivity.PARAM_VEHICLE, it.vehicle)
            startActivity(i)
        }

        historyDialog.clearListener.subscribe {
            historyViewModel.clear()
            Toast.makeText(this, getString(R.string.history_cleared), Toast.LENGTH_SHORT).show()
        }

        setupObservers()
        historyViewModel.loadVehicleHistory()
    }

    private fun setupObservers() {
        vehicleDetailsViewModel.vehicleDetails.observe(this) {
            toggleProgress(false)

            historyViewModel.push(it)

            val intent = Intent(this, VehicleInfoActivity::class.java)
            intent.putExtra(VehicleInfoActivity.PARAM_VEHICLE, it)
            startActivity(intent)
        }

        vehicleDetailsViewModel.vehicleError.observe(this) {
            toggleProgress(false)
            showError(it)
        }

        historyViewModel.readErrorMessage.observe(this, ::showError)

        historyViewModel.writeErrorMessage.observe(this, ::showError)

        historyViewModel.vehicleHistory.observe(this) {
            historyDialog.vehicleHistory = it
        }
    }
}