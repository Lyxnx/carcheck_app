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
import java.util.*

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

        adapter = CardItemRecyclerAdapter(this)
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
        val items = ArrayList<MoreInfoCardItem>()

        items.add(MoreInfoCardItem(
                getString(R.string.moreinfo_header_general),
                listOf(
                        vehicle!!.euroStatus?.let {
                            Pair(getString(R.string.euro_status), it)
                        },
                        vehicle!!.v5CIssueDate?.let {
                            Pair(getString(R.string.v5c_issued), it)
                        },
                        vehicle!!.registryLocation?.let {
                            Pair(getString(R.string.registered_near), it)
                        },
                        vehicle!!.insuranceGroup?.let {
                            Pair(getString(R.string.insurance_group), it)
                        }
                )
        ))

        vehicle!!.taxStatus?.let { taxStatus ->
            items.add(MoreInfoCardItem(
                    getString(R.string.moreinfo_header_tax),
                    listOf(
                            taxStatus.status?.let {
                                Pair(getString(R.string.status), it)
                            },
                            taxStatus.daysLeft?.let {
                                Pair(getString(R.string.days_left), it)
                            }
                    )
            ))
        }

        vehicle!!.motStatus?.let { motStatus ->
            items.add(MoreInfoCardItem(
                    getString(R.string.moreinfo_header_mot),
                    listOf(
                            motStatus.status?.let {
                                Pair(getString(R.string.status), it)
                            },
                            motStatus.daysLeft?.let {
                                Pair(getString(R.string.days_left), it)
                            }
                    )
            ))
        }

        vehicle!!.emissions?.let {
            items.add(MoreInfoCardItem(
                    getString(R.string.moreinfo_header_emissions),
                    listOf(
                            Pair(getString(R.string.cost_12_months), it.cost12Months),
                            // Some tax can only be paid 12 mo at a time
                            Pair(getString(R.string.cost_6_months), if (it.cost6Months == "£N/A") "N/A" else it.cost6Months),
                            Pair(getString(R.string.output), it.co2Output)
                    )
            ))
        }

        vehicle!!.performance?.let { performance ->
            items.add(MoreInfoCardItem(
                    getString(R.string.moreinfo_header_performance),
                    listOf(
                            performance.acceleration?.let {
                                Pair(getString(R.string.zeroTo60), it)
                            },
                            Pair(getString(R.string.top_speed), performance.topSpeed)
                    )
            ))
        }

        vehicle!!.economy?.let { econ ->
            items.add(MoreInfoCardItem(
                    getString(R.string.moreinfo_header_fuel_economy),
                    listOf(
                            Pair(getString(R.string.urban), econ.urban),
                            Pair(getString(R.string.extra_urban), econ.extraUrban),
                            Pair(getString(R.string.combined), econ.combined)
                    )
            ))
        }

        adapter.items = items
    }
}