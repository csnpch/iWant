package com.ituy.iwant

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.TextView
import com.ituy.iwant.Helpers.Helpers

class CustomListView_DialogResponse(
    context: Context,
    private val fullname: List<String>,
    private val contact: List<String>,
    private val timestamps: List<String>
) : ArrayAdapter<String>(
    context,
    R.layout.item_response_you_want,
    fullname
) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertViewVar = convertView

        if (convertViewVar == null) {
            val inflater = LayoutInflater.from(context)
            convertViewVar = inflater.inflate(R.layout.item_response_you_want, parent, false)
        }

        val fullnameTextView = convertViewVar!!.findViewById<TextView>(R.id.your_wish_dialog_listview_fullname)
        val contactTextView = convertViewVar.findViewById<TextView>(R.id.your_wish_dialog_listview_contact)
        val timestamp = convertViewVar.findViewById<TextView>(R.id.your_wish_dialog_listview_timestamp)

        val title = fullname[position]
        val subtitle = contact[position]

        fullnameTextView.text =
            if (title.length <= 42) title
            else Helpers().subStringLength(title, 42, true)
        contactTextView.text =
            if (subtitle === "") "-"
            else if (subtitle.length <= 42) subtitle
            else Helpers().subStringLength(subtitle, 42, true)
        timestamp.text = timestamps[position]

        return convertViewVar
    }
}