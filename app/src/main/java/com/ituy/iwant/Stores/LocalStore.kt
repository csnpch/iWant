package com.ituy.iwant.Stores

import android.content.Context
import android.content.SharedPreferences

class LocalStore(context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("myPreferences", Context.MODE_PRIVATE)

    fun saveString(key: String, value: String) {
        with(sharedPreferences.edit()) {
            putString(key, value)
            apply()
        }
    }

    fun getString(key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }

}


// How to use
// Follow this

// Initialize views and preferences manager
//      private lateinit var preferencesManager: PreferencesManager
//      preferencesManager = LocalStore(this)


// PUSH
//      preferencesManager.saveString("key_strValue", value)


//  GET
//      val savedString = preferencesManager.getString("key_strValue", "")


