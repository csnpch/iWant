package com.example.iwant.Helpers

class Helpers {

    fun subStringLength(value: String, length: Int, MoreDot: Boolean = false): String {
        if (value.length > length) {
            return value.substring(0, length) + if (MoreDot) "..." else ""
        }
        return value
    }
}