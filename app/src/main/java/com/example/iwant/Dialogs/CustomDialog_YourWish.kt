package com.example.iwant.Dialogs

import android.annotation.SuppressLint
import com.example.iwant.R
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.Spannable
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.text.style.UnderlineSpan
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.example.iwant.CustomListView_DialogResponse
import com.example.iwant.Helpers.Helpers
import java.sql.Time

@SuppressLint("MissingInflatedId")
fun showDialogYourWish(
    Context: Context,
    Activity: Activity,
    Index: Int, Title: String,
    Description: String, TimeLeft: String,
    UnitDayAddMoreExpire: Int,
    PeopleResponse: Array<Array<String>>?
) {
    var dialogYourWish: AlertDialog? = null // create local variable to store dialog instance

    val displayMetrics = DisplayMetrics()
    val mBuilder: AlertDialog.Builder = AlertDialog.Builder(Context, R.style.CustomAlertDialog)
    val mView: View = LayoutInflater.from(Context).inflate(R.layout.dialog_detail_your_wish, null)

    val txt_title = mView.findViewById<TextView>(R.id.your_wish_dialog_txt_title)
    val txt_description = mView.findViewById<TextView>(R.id.your_wish_dialog_txt_description)
    val txt_expire_time_left = mView.findViewById<TextView>(R.id.your_wish_dialog_txt_expire_time_left)

    val txt_btn_add_more_days = mView.findViewById<TextView>(R.id.your_wish_dialog_txt_btn_add_more_days_for_expire)
    val btn_add_more_days = mView.findViewById<LinearLayout>(R.id.your_wish_dialog_btn_add_more_days_for_expire)
    val btn_destroy = mView.findViewById<LinearLayout>(R.id.your_wish_dialog_btn_destroy)
    val btn_close = mView.findViewById<LinearLayout>(R.id.your_wish_dialog_btn_close)

    val listview_responses = mView.findViewById<ListView>(R.id.your_wish_dialog_listview_response)
    val container_peoples_reponses = mView.findViewById<LinearLayout>(R.id.your_wish_dialog_container_peoples_response)

    val tmpTitle = SpannableString(Title)
    tmpTitle.setSpan(UnderlineSpan(), 0, tmpTitle.length, 0)
    txt_title.text = tmpTitle
    txt_description.text = Description
    txt_expire_time_left.text = "$TimeLeft LEFTS"
    txt_btn_add_more_days.text = "Add $UnitDayAddMoreExpire more days for expire"

    if (PeopleResponse != null) {
        container_peoples_reponses.visibility = View.VISIBLE
        listview_responses.adapter = CustomListView_DialogResponse(
            Context,
            PeopleResponse.map { it[0] },
            PeopleResponse.map { it[1] },
            PeopleResponse.map { it[2] }
        )
    }

    // Event Area
    listview_responses.setOnItemClickListener { parent, view, position, id ->

        val txt_fullname = view.findViewById<TextView>(R.id.your_wish_dialog_listview_fullname)
        val txt_contact = view.findViewById<TextView>(R.id.your_wish_dialog_listview_contact)
        val builder = androidx.appcompat.app.AlertDialog.Builder(Context)

        val title = SpannableString("Call \" ${txt_fullname.text} \" ?")
        title.setSpan(AbsoluteSizeSpan(14, true), 0, title.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)

        builder.setTitle(title)
        builder.setPositiveButton("CALL") { dialog, which ->
            val cleanedPhoneNumber = Helpers().cleanPhoneNumber(txt_contact.text.toString().trim())
            Context.startActivity(
                Intent(Intent.ACTION_CALL, Uri.parse("tel:$cleanedPhoneNumber"))
            )
        }
        builder.setNegativeButton("Close") { _, _ -> }
        builder.create().show()
    }

    btn_close.setOnClickListener {
        dialogYourWish?.dismiss()
    }

    btn_add_more_days.setOnClickListener {
        Toast.makeText(Context, "On Click Add More $Index", Toast.LENGTH_SHORT).show()
    }

    btn_destroy.setOnClickListener {
        Toast.makeText(Context, "On Click Destroy Index = $Index", Toast.LENGTH_SHORT).show()
    }

    mBuilder.setView(mView)
    dialogYourWish = mBuilder.create()
    dialogYourWish.show()

    Activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
    dialogYourWish.window?.setLayout(displayMetrics.widthPixels - 140, WindowManager.LayoutParams.WRAP_CONTENT)
}
