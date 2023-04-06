package com.ituy.iwant.Stores

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


// How to use?

// EXAMPLE - data class Person(val name: String, val age: Int)

//    GET
//        val listPerson: ArrayList<Person> = LocalStore(this).getArrayList("personList", ArrayList())

//    SAVE
//        listPerson.add(Person("SamaHam", 25))
//        LocalStore(this).saveArrayList("personList", listPerson)



class LocalStore(context: Context) {

    private val gson = Gson()
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("myPreferences", Context.MODE_PRIVATE)


    // For array list
    fun <T> saveArrayList(key: String, value: ArrayList<T>): Boolean {
        try {
            with(sharedPreferences.edit()) {
                val json = gson.toJson(value)
                putString(key, json)  // to jsonString
                apply()
            }
        } catch (err: Error) {
            println(err)
            return false
        }
        return true
    }

    fun <T> getArrayList(key: String, defaultValue: ArrayList<T>): ArrayList<T> {
        val jsonString = sharedPreferences.getString(key, gson.toJson(defaultValue))
        val listType = object : TypeToken<ArrayList<T>>() {}.type
        return gson.fromJson(jsonString, listType) ?: defaultValue
    }


    // For string value
    fun saveString(key: String, value: String): Boolean {
        try {
            with(sharedPreferences.edit()) {
                putString(key, value)
                apply()
            }
        } catch (err: Error) {
            println(err)
            return false
        }
        return true
    }

    fun getString(key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }

}

