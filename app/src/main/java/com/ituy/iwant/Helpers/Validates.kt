package com.ituy.iwant.Helpers

import java.util.*

class Validates {

    fun input(key: String, value: String): String? {

        val key = key.toLowerCase(Locale.ENGLISH)

        if (key === "phone") {
            if (!value.matches(Regex("\\d{10}")) || (value[0] !== '0')) {
                return "$key is not a valid phone number"
            }
        } else if (key === "email") {
            if (!value.matches(Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"))) {
                return "$key is not a valid email address"
            }
        } else if (key === "contact") {
            if (value.length < 6) {
                return "$key should greater than or equal to 6 letter"
            }
        }

        if (value === "" || value.isNullOrEmpty()) {
            return "$key is invalid"
        }

        return null
    }

}