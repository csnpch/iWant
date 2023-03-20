package com.example.iwant

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.TextView

class CustomListView_Wish(
    context: Context,
    private val titles: ArrayList<String>,
    private val subtitles: ArrayList<String>,
    private val distances: ArrayList<String>,
    private val timestamps: ArrayList<String>
) : ArrayAdapter<String>(
    context,
    R.layout.item_wish_list,
    titles
) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertViewVar = convertView

        if (convertViewVar == null) {
            val inflater = LayoutInflater.from(context)
            convertViewVar = inflater.inflate(R.layout.item_wish_list, parent, false)
        }

        val titleTextView = convertViewVar!!.findViewById<TextView>(R.id.wish_title)
        val subtitleTextView = convertViewVar.findViewById<TextView>(R.id.wish_subtitle)
        val distance = convertViewVar.findViewById<TextView>(R.id.wish_distance)
        val timestamp = convertViewVar.findViewById<TextView>(R.id.wish_timestamp)

        titleTextView.text = titles[position]
        subtitleTextView.text = subtitles[position]
        distance.text = distances[position]
        timestamp.text = timestamps[position]

        return convertViewVar
    }
}