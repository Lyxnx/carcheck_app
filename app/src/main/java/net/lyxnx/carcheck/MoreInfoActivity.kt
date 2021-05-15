package net.lyxnx.carcheck

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import net.lyxnx.carcheck.adapter.CardItemRecyclerAdapter
import net.lyxnx.carcheck.adapter.MoreInfoCardItem
import net.lyxnx.carcheck.rest.model.Vehicle
import net.lyxnx.carcheck.util.showError

class MoreInfoActivity : InfoActivity() {

    companion object {
        private val TAG = MoreInfoActivity::class.java.simpleName
    }

    private lateinit var adapter: CardItemRecyclerAdapter

    private var vehicle: Vehicle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_moreinfo)

        if (intent != null && !intent.hasExtra(VehicleInfoActivity.PARAM_VEHICLE)) {
            Log.e(TAG, "${VehicleInfoActivity.PARAM_VEHICLE} intent extra not passed in; finishing")
            finish();
            return
        }

        vehicle = intent.getParcelableExtra(VehicleInfoActivity.PARAM_VEHICLE)

        // Shouldn't be null at this point but just in case something has gone horribly wrong
        if (vehicle == null) {
            showError(getString(R.string.null_info))
            finish()
            return
        }

        title = getString(R.string.title_extra_info)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        val recyclerView = findViewById<RecyclerView>(R.id.card_list)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = CardItemRecyclerAdapter()
        recyclerView.adapter = adapter
        populateAdapter()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home) {
            finish()
            true
        } else super.onOptionsItemSelected(item)
    }

    private fun populateAdapter() {
        val items = mutableListOf<MoreInfoCardItem>()

        items.add(MoreInfoCardItem(
                getString(R.string.moreinfo_header_general),
                listOf(
                        vehicle!!.euroStatus?.let {
                            getString(R.string.euro_status) to it
                        },
                        vehicle!!.v5CIssueDate?.let {
                            getString(R.string.v5c_issued) to it
                        },
                        vehicle!!.registryLocation?.let {
                            getString(R.string.registered_near) to it
                        },
                        vehicle!!.insuranceGroup?.let {
                            getString(R.string.insurance_group) to it
                        }
                )
        ))

        vehicle!!.taxStatus?.let { taxStatus ->
            items.add(MoreInfoCardItem(
                    getString(R.string.moreinfo_header_tax),
                    listOf(
                            taxStatus.status?.let {
                                getString(R.string.status) to it
                            },
                            taxStatus.daysLeft?.let {
                                getString(R.string.days_left) to it
                            }
                    )
            ))
        }

        vehicle!!.motStatus?.let { motStatus ->
            items.add(MoreInfoCardItem(
                    getString(R.string.moreinfo_header_mot),
                    listOf(
                            motStatus.status?.let {
                                getString(R.string.status) to it
                            },
                            motStatus.daysLeft?.let {
                                getString(R.string.days_left) to it
                            }
                    )
            ))
        }

        vehicle!!.emissions?.let {
            items.add(MoreInfoCardItem(
                    getString(R.string.moreinfo_header_emissions),
                    listOf(
                            getString(R.string.cost_12_months) to it.cost12Months,
                            // Some tax can only be paid 12 mo at a time
                            getString(R.string.cost_6_months) to if (it.cost6Months == "£N/A") "N/A" else it.cost6Months,
                            getString(R.string.output) to it.co2Output
                    )
            ))
        }

        vehicle!!.performance?.let {
            items.add(MoreInfoCardItem(
                    getString(R.string.moreinfo_header_performance),
                    listOf(
                            it.acceleration?.let { acceleration ->
                                getString(R.string.zeroTo60) to acceleration
                            },
                            getString(R.string.top_speed) to it.topSpeed
                    )
            ))
        }

        vehicle!!.economy?.let {
            items.add(MoreInfoCardItem(
                    getString(R.string.moreinfo_header_fuel_economy),
                    listOf(
                            getString(R.string.urban) to it.urban,
                            getString(R.string.extra_urban) to it.extraUrban,
                            getString(R.string.combined) to it.combined
                    )
            ))
        }

        adapter.items = items
    }
}