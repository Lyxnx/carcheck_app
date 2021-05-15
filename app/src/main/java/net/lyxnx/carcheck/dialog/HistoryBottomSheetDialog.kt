package net.lyxnx.carcheck.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.reactivex.rxjava3.subjects.PublishSubject
import net.lyxnx.carcheck.R
import net.lyxnx.carcheck.adapter.HistoryBottomSheetAdapter
import net.lyxnx.carcheck.viewmodels.SavedVehicle
import java.util.*

class HistoryBottomSheetDialog : BottomSheetDialogFragment() {

    private lateinit var clearButton: TextView

    val clearListener = PublishSubject.create<Unit>()
    val selectedListener = PublishSubject.create<SavedVehicle>()

    var vehicleHistory: List<SavedVehicle> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.DialogStyle)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val view = inflater.inflate(R.layout.sheet_history, container, false)

        dialog!!.setOnShowListener { dialog ->
            val bottomSheet = (dialog as BottomSheetDialog).findViewById<View>(R.id.design_bottom_sheet)

            if (bottomSheet != null) {
                BottomSheetBehavior.from(bottomSheet).state = BottomSheetBehavior.STATE_HALF_EXPANDED

                clearButton.setOnClickListener {
                    popup(requireContext()) {
                        title(getString(R.string.confirm_clear_history_title))
                        message(getString(R.string.confirm_clear_history_text))
                        positiveButton(android.R.string.ok) { _, _ ->
                            clearListener.onNext(Unit)
                            BottomSheetBehavior.from(bottomSheet).state = BottomSheetBehavior.STATE_HIDDEN
                        }
                        negativeButton(android.R.string.cancel, null)
                    }.show()
                }
            }
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        clearButton = view!!.findViewById(R.id.clearButton)

        val historyRecyclerView = view!!.findViewById<RecyclerView>(R.id.historyRecyclerView)

        val items = vehicleHistory.reversed()

        val adapter = HistoryBottomSheetAdapter()
        adapter.vehicleHistory = items

        historyRecyclerView.layoutManager = LinearLayoutManager(context)
        historyRecyclerView.adapter = adapter

        adapter.clickListener.subscribe { item ->
            selectedListener.onNext(item)

            val dialog = dialog
            dialog?.dismiss()
        }
    }
}