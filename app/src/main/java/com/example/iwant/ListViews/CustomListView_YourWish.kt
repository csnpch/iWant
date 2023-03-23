package com.example.iwant

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.TextView
import com.example.iwant.Helpers.Helpers

class CustomListView_YourWish(
    context: Context,
    private val ids: ArrayList<String>,
    private val titles: ArrayList<String>,
    private val subtitles: ArrayList<String>,
    private val timestamps: ArrayList<String>,
    private val peoples_responses: ArrayList<Array<Array<String>>?>
) : ArrayAdapter<String>(
    context,
    R.layout.item_your_wish_list,
    titles
) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertViewVar = convertView

        if (convertViewVar == null) {
            val inflater = LayoutInflater.from(context)
            convertViewVar = inflater.inflate(R.layout.item_your_wish_list, parent, false)
        }

        val titleTextView = convertViewVar!!.findViewById<TextView>(R.id.your_wish_txt_title)
        val subtitleTextView = convertViewVar.findViewById<TextView>(R.id.your_wish_txt_subtitle)
        val timestampTextView = convertViewVar.findViewById<TextView>(R.id.your_wish_txt_timestamp)
        val status_responds = convertViewVar.findViewById<LinearLayout>(R.id.your_wish_status_someone_responds)

        val title = titles[position]
        titleTextView.text =
            if (title.length <= 42) title
            else Helpers().subStringLength(title, 42, true)
        subtitleTextView.text = "${subtitles[position]} left for expire"
        timestampTextView.text = timestamps[position]

        if (peoples_responses[position] != null)
            status_responds.visibility = View.VISIBLE

        return convertViewVar
    }
}