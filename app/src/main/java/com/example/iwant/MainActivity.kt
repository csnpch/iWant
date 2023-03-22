package com.example.iwant

import android.graphics.fonts.FontFamily
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import androidx.appcompat.app.ActionBar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.iwant.Fragments.MapFragment
import com.example.iwant.Fragments.ProfileFragment
import com.example.iwant.Fragments.WishFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import nl.joery.animatedbottombar.AnimatedBottomBar


class MainActivity: AppCompatActivity(), OnTabSelectedListener, AnimatedBottomBar.OnTabSelectListener {

    private lateinit var navigation_bar: AnimatedBottomBar
    private lateinit var fragmentManager: FragmentManager


    private fun init() {
        navigation_bar = findViewById(R.id.navigation_bar)
        navigation_bar.setOnTabSelectListener(this@MainActivity)

        this.callFragment(WishFragment())
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
        this.init()
    }


    override fun onPause() {
        super.onPause()
        overridePendingTransition(R.anim.slide_left,R.anim.no_change)
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