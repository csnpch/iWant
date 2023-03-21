package com.example.iwant.Dialogs

import com.example.iwant.R
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.ThemedSpinnerAdapter.Helper
import com.example.iwant.Helpers.Helpers
import com.example.iwant.Helpers.PermissionUtils
import com.google.android.flexbox.FlexboxLayout

fun showDialogWish(
    Context: Context,
    Activity: Activity,
    Index: Int,
    Title: String,
    When: String,
    Distance: String,
    Description: String = "",
    Benefit: String = "",
    Contact: String,
    LatLog: ArrayList<Double>
) {
    var dialogWish: AlertDialog? = null // create local variable to store dialog instance

    val displayMetrics = DisplayMetrics()
    val mBuilder: AlertDialog.Builder = AlertDialog.Builder(Context, R.style.CustomAlertDialog)
    val mView: View = LayoutInflater.from(Context).inflate(R.layout.dialog_detail_wish, null)

    val txt_title: TextView = mView.findViewById(R.id.wish_dialog_txt_title)
    val txt_when: TextView = mView.findViewById(R.id.wish_dialog_txt_when)
    val txt_distance: TextView = mView.findViewById(R.id.wish_dialog_txt_distance)
    val txt_description: TextView = mView.findViewById(R.id.wish_dialog_txt_description)
    val txt_benefit: TextView = mView.findViewById(R.id.wish_dialog_txt_benefit)
    val txt_contact: TextView = mView.findViewById(R.id.wish_dialog_txt_contact)
    val btn_call: ImageView = mView.findViewById(R.id.wish_dialog_btn_call)
    val btn_exit: ImageView = mView.findViewById(R.id.wish_dialog_btn_exit)
    val btn_open_navigator: FlexboxLayout = mView.findViewById(R.id.wish_dialog_btn_open_navigator)
    val btn_accept_to_deliver: FlexboxLayout = mView.findViewById(R.id.wish_dialog_btn_accept_to_deliver)

    val tmpTitle = SpannableString(Title)
    tmpTitle.setSpan(UnderlineSpan(), 0, tmpTitle.length, 0)
    txt_title.text = tmpTitle

    txt_when.text = "Post When $When"
    txt_distance.text = Distance
    txt_description.text = if (Description === "") "-" else Description
    txt_benefit.text = if (Benefit === "") "-" else Benefit
    txt_contact.text = Contact

    // Event Area
    btn_exit.setOnClickListener {
        dialogWish?.dismiss()
    }

    btn_call.setOnClickListener {
        if (PermissionUtils.isCallPhonePermissionGranted(Context as Activity)) {
            // Permission already
            val cleanedPhoneNumber = Helpers().cleanPhoneNumber(txt_contact.text.toString().trim())
            Context.startActivity(
                Intent(Intent.ACTION_DIAL, Uri.parse("tel:$cleanedPhoneNumber"))
            )
        } else {
            // Request permission
            PermissionUtils.requestCallPhonePermission(Context as Activity)
        }
    }

    btn_open_navigator.setOnClickListener {
        Context.startActivity(
            Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?daddr=${LatLog[0]},${LatLog[1]}"))
        )
    }

    btn_accept_to_deliver.setOnClickListener {
        Toast.makeText(Context, "onAcceptToDeliver Index = $Index", Toast.LENGTH_SHORT).show()
    }

    mBuilder.setView(mView)
    dialogWish = mBuilder.create()
    dialogWish.show()

    Activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
    dialogWish.window?.setLayout(displayMetrics.widthPixels - 140, WindowManager.LayoutParams.WRAP_CONTENT)
}
