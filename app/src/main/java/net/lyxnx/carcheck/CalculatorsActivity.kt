package net.lyxnx.carcheck

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.google.android.material.bottomnavigation.BottomNavigationView
import net.lyxnx.carcheck.adapter.CalculatorsPagerAdapter

class CalculatorsActivity : AppCompatActivity() {

    private lateinit var pager: ViewPager2
    private var previousItem: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calcs)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        pager = findViewById(R.id.main_content)

        val navigation = findViewById<BottomNavigationView>(R.id.navigation)
        navigation.setOnNavigationItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.action_journey -> {
                    pager.currentItem = CalculatorsPagerAdapter.JOURNEY
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.action_mpg -> {
                    pager.currentItem = CalculatorsPagerAdapter.MPG
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.action_finance -> {
                    pager.currentItem = CalculatorsPagerAdapter.FINANCE
                    return@setOnNavigationItemSelectedListener true
                }
                else -> return@setOnNavigationItemSelectedListener false
            }
        }

        val adapter = CalculatorsPagerAdapter(this)

        pager.offscreenPageLimit = 1
        pager.adapter = adapter
        pager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if (previousItem != null) {
                    previousItem!!.isChecked = false
                } else {
                    navigation.menu.getItem(0).isChecked = false
                }

                val item = navigation.menu.getItem(position)
                item.isChecked = true
                previousItem = item
                title = getString(R.string.title_calculators, adapter.getTitle(position))
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home) {
            finish()
            true
        } else super.onOptionsItemSelected(item)
    }

}