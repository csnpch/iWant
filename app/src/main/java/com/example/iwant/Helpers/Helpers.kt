package com.example.iwant.Helpers

class Helpers {

    fun subStringLength(value: String, length: Int, MoreDot: Boolean = false): String {
        if (value.length > length) {
            return value.substring(0, length) + if (MoreDot) "..." else ""
        }
        return value
    }

    fun cleanPhoneNumber(value: String): String {
        val phoneNumber = value.trim()
        val phoneRegex = Regex("[^\\d+]") // remove character keep digit only
        return phoneRegex.replace(phoneNumber, "")
    }

}