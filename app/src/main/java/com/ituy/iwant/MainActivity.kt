package com.ituy.iwant

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.text.Spannable
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.ituy.iwant.Fragments.MapFragment
import com.ituy.iwant.Fragments.ProfileFragment
import com.ituy.iwant.Fragments.WishFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.ituy.iwant.Helpers.getCurrentLocation
import com.ituy.iwant.api.wish.WishService
import nl.joery.animatedbottombar.AnimatedBottomBar
import kotlin.system.exitProcess


class MainActivity: AppCompatActivity(), OnTabSelectedListener, AnimatedBottomBar.OnTabSelectListener {


    private lateinit var navigation_bar: AnimatedBottomBar
    private lateinit var fragmentManager: FragmentManager
    private var currentUserLocation = DoubleArray(2)

    private lateinit var containerLoading: LinearLayout;


    companion object {
        lateinit var myGlobalVar: String
        var statusLoading: Boolean = true
        // Can call in frage
        @JvmStatic
        fun setStateLoading(payload: Boolean, containerLoading: LinearLayout) {
            statusLoading = payload
            containerLoading.visibility = if (statusLoading) View.VISIBLE else View.INVISIBLE
        }
    }


    private fun getLatLngLocationDefault() {
        getCurrentLocation(this) { location ->
            currentUserLocation[0] = location.first!!
            currentUserLocation[1] = location.second!!
//            Toast.makeText(this, "${currentUserLocation[0]}:${currentUserLocation[1]}", Toast.LENGTH_SHORT).show()
        }
    }


    private fun init() {
        myGlobalVar = "My Global Variable : Value1"

        navigation_bar = findViewById(R.id.navigation_bar)
        navigation_bar.setOnTabSelectListener(this@MainActivity)

        containerLoading = findViewById(R.id.loadingContainer)

        // Add a global layout listener to the activity's layout
        val layout = findViewById<View>(android.R.id.content)
        layout.viewTreeObserver.addOnGlobalLayoutListener {
            val heightDiff = layout.rootView.height - layout.height
            if (heightDiff > dpToPx(this, 200f)) { // 200dp threshold for detecting keyboard is open
                navigation_bar.visibility = View.GONE
            } else {
                navigation_bar.visibility = View.VISIBLE
            }
        }

        this.callFragment(WishFragment())
        this.initLoading()
    }


    private fun initLoading() {
        setStateLoading(true, containerLoading)

        var simulateData: Any? = null
        var timeCount = 0
        object: CountDownTimer(10000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                // fetch data here
                if (timeCount >= 2000) {
                    simulateData = "Have Data"  // comment here
                }

                if (simulateData !== null) {
                    onFinish()
                }

                timeCount += 1000
            }

            override fun onFinish() {
                if (simulateData === null) {
                    Toast.makeText(this@MainActivity, "Can't fetch data from server", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    setStateLoading(false, containerLoading)
                }
            }
        }.start()

    }


    // Convert dp to px
    private fun dpToPx(context: Context, dp: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics).toInt()
    }


    private fun callFragment(fragment: Fragment) {
        fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction().replace(
            R.id.fragment_container,
            fragment
        ).commit()
    }


    override fun onTabSelected(
        lastIndex: Int,
        lastTab: AnimatedBottomBar.Tab?,
        newIndex: Int,
        newTab: AnimatedBottomBar.Tab
    ) {
        when (newTab.id) {
            R.id.menu_wish ->
                this.callFragment(WishFragment())
            R.id.menu_map ->
                this.callFragment(MapFragment())
            R.id.menu_profile ->
                this.callFragment(ProfileFragment())
        }
    }


    override fun onTabSelected(tab: TabLayout.Tab?) { }
    override fun onTabUnselected(tab: TabLayout.Tab?) { }
    override fun onTabReselected(tab: TabLayout.Tab?) { }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Custom Appbar
        supportActionBar?.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.shape_rounded_appbar))
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.appbar_layout)
        this.getLatLngLocationDefault()
        this.init()
    }


    override fun finish() {
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        val title = SpannableString("Your need to exit ?")
        title.setSpan(AbsoluteSizeSpan(14, true), 0, title.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)

        builder.setTitle(title)
        builder.setPositiveButton("EXIT") { dialog, which ->
            super.finish()
        }

        builder.setNegativeButton("NO") { _, _ -> }
        builder.create().show()
    }

}