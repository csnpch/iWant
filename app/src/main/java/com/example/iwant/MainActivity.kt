package com.example.iwant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import nl.joery.animatedbottombar.AnimatedBottomBar


class MainActivity: AppCompatActivity(), OnTabSelectedListener,
    AnimatedBottomBar.OnTabSelectListener {

    private lateinit var navigation_bar: AnimatedBottomBar
    private lateinit var fragmentManager: FragmentManager

    private fun setAppbar() {
        supportActionBar?.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.rounded_appbar))
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.appbar_layout)
    }


    private fun init() {
        navigation_bar = findViewById(R.id.navigation_bar)
        navigation_bar.setOnTabSelectListener(this@MainActivity)

        this.callFragment(WishFragment())
    }


    private fun callFragment(fragment: Any) {
        fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction().replace(
            R.id.fragment_container,
            fragment as Fragment
        ).commit()
    }


    override fun onTabSelected(
        lastIndex: Int,
        lastTab: AnimatedBottomBar.Tab?,
        newIndex: Int,
        newTab: AnimatedBottomBar.Tab
    ) {
        when (newTab.id) {
            R.id.menu_wish -> this.callFragment(WishFragment())
            R.id.menu_map -> this.callFragment(WishFragment())
            R.id.menu_profile -> this.callFragment(ProfileFragment())
        }
    }
    override fun onTabSelected(tab: TabLayout.Tab?) { }
    override fun onTabUnselected(tab: TabLayout.Tab?) { }
    override fun onTabReselected(tab: TabLayout.Tab?) { }


//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.bottom_navigation_menu, menu)
//        return super.onCreateOptionsMenu(menu)
//    }


//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//
//        when (item.itemId) {
//            R.id.menu_wish -> {
//                Toast.makeText(this, "Menu Wish Wish", Toast.LENGTH_SHORT).show()
//                return true
//            }
//            R.id.menu_map -> {
//                Toast.makeText(this, "Menu Map Clicked", Toast.LENGTH_SHORT).show()
//                return true
//            }
//            R.id.menu_profile -> {
//                Toast.makeText(this, "Menu Profile Clicked", Toast.LENGTH_SHORT).show()
//                return true
//            }
//            else -> {
//                return super.onOptionsItemSelected(item)
//            }
//        }
//
//    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Check auth for show appBar
        val statusAuth = true
        when (statusAuth) {
            true -> this.setAppbar()             // Show appbar
            false -> supportActionBar?.hide()    // Hide appbar for login & register screen
        }

        setContentView(R.layout.activity_main)
        this.init()
    }

}