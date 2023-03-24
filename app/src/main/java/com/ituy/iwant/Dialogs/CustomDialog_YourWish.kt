package com.ituy.iwant.Dialogs

import android.annotation.SuppressLint
import com.ituy.iwant.R
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.CountDownTimer
import android.text.Spannable
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.text.style.UnderlineSpan
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.ituy.iwant.CustomListView_DialogResponse
import com.ituy.iwant.Helpers.Helpers
import com.ituy.iwant.Maps.MapViewActivity

@SuppressLint("MissingInflatedId")
fun showDialogYourWish(
    Context: Context,
    Activity: Activity,
    Index: Int,
    Id: String,
    Title: String,
    Description: String,
    TimeLeft: String,
    UnitDayAddMoreExpire: Int,
    PeopleResponse: Array<Array<String>>?,
    LatLng: ArrayList<Double>
) {
    var dialogYourWish: AlertDialog? = null // create local variable to store dialog instance

    val displayMetrics = DisplayMetrics()
    val mBuilder: AlertDialog.Builder = AlertDialog.Builder(Context, R.style.CustomAlertDialog)
    val mView: View = LayoutInflater.from(Context).inflate(R.layout.dialog_detail_your_wish, null)

    val img_btn_map_icon = mView.findViewById<ImageView>(R.id.your_wish_dialog_map_preview)
    val btn_edit = mView.findViewById<LinearLayout>(R.id.your_wish_dialog_btn_edit)

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
    txt_expire_time_left.text = "$TimeLeft left"
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

        object: CountDownTimer(400, 400) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                builder.create().show()
            }
        }.start()

    }


    img_btn_map_icon.setOnClickListener {
        val intent = Intent(Context, MapViewActivity::class.java)
        intent.putExtra("LatLng", "lat:${LatLng[0]},lng:${LatLng[1]}")
        Activity.startActivity(intent)
        Activity.overridePendingTransition(R.anim.no_change,R.anim.no_change)
    }


//    btn_edit.setOnClickListener {
//        val intent = Intent(Activity, EditWishActivity::class.java)
//        intent.putExtra("latLngChooseLocation", "lat:${LatLng[0]},lng:${LatLng[1]}")
//        Activity.startActivity(intent)
//        Activity.overridePendingTransition(R.anim.slide_left,R.anim.no_change)
//    }


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
