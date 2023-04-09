package com.ituy.iwant.Fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.ituy.iwant.Auths.AuthActivity
import com.ituy.iwant.Helpers.Validates
import com.ituy.iwant.MainActivity
import com.ituy.iwant.R
import com.ituy.iwant.MainActivity.Companion.myGlobalVar
import com.ituy.iwant.Stores.LocalStore
import com.ituy.iwant.api.member.MemberModel
import com.ituy.iwant.api.member.MemberService
import com.ituy.iwant.api.member.dto.MemberRequest
import com.ituy.iwant.api.member.dto.MemberResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProfileFragment : Fragment(), View.OnClickListener {

    // Form Section
    private lateinit var edt_fullname: EditText
    private lateinit var edt_phone: EditText
    private lateinit var edt_email: EditText

    // Action Section
    private lateinit var btn_edit: LinearLayout
    private lateinit var btn_save: LinearLayout
    private lateinit var btn_cancel: LinearLayout
    private lateinit var area_btn_default: LinearLayout
    private lateinit var area_btn_onEdit: LinearLayout

    // SignOut Section
    private lateinit var area_signout: LinearLayout
    private lateinit var logo_signIn: ImageView
    private lateinit var text_signIn: TextView
    private lateinit var btn_signOut: ImageView

    private var loginWith: String = "GOOGLE"
    private var statusOnEdit: Boolean = false

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val apiService = MemberService()


    // Loading
    private lateinit var containerLoading: LinearLayout


    private fun initView(view: View): View {

        // Form Section
        edt_fullname = view.findViewById(R.id.profile_edt_fullname)
        edt_phone = view.findViewById(R.id.profile_edt_phone)
        edt_email = view.findViewById(R.id.profile_edt_email)


        // Action Section
        btn_edit = view.findViewById(R.id.profile_btn_edit)
        btn_edit.setOnClickListener(this)
        btn_save = view.findViewById(R.id.profile_btn_save)
        btn_save.setOnClickListener(this)
        btn_cancel = view.findViewById(R.id.profile_btn_cancel)
        btn_cancel.setOnClickListener(this)

        area_btn_default = view.findViewById(R.id.profile_area_btn_default)
        area_btn_onEdit = view.findViewById(R.id.profile_area_btn_onEdit)
        area_btn_default.visibility = View.VISIBLE   // Default


        // SignOut Section
        area_signout = view.findViewById(R.id.profile_area_signout)
        logo_signIn = view.findViewById(R.id.profile_logo_signIn)
        text_signIn = view.findViewById(R.id.profile_text_signIn)
        btn_signOut = view.findViewById(R.id.profile_btn_signOut)
        btn_signOut.setOnClickListener(this)

        this.initLoading(view)

        return view
    }


    private fun initLoading(view: View) {
        containerLoading = view.findViewById(R.id.profile_loadingContainer)
        setStateLoading(true)
    }


    private fun setStateLoading(status: Boolean) {
        containerLoading.visibility = if (status) View.VISIBLE else View.INVISIBLE
    }


    private fun validateForm(): Boolean {
        val inputFields = listOf(
            "fullname" to edt_fullname,
            "phone" to edt_phone,
            "email" to edt_email
        )

        var statusValidate = true
        val validator = Validates()
        for (inputField in inputFields) {
            val key = inputField.first
            val editText = inputField.second

            val errorMessage = validator.input(key, editText.text.toString())
            if (errorMessage != null) {
                editText.error = errorMessage
                statusValidate = false
            }
        }
        return statusValidate;
    }


    private fun toggleFormVisibility() {

        fun setEditTextVisibility(status: Boolean) {
            val listEditText = arrayOf<EditText>(edt_email, edt_fullname, edt_phone)

            listEditText.forEach {
                edt: EditText ->
                    edt.isEnabled = status
                    edt.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            if (status) R.color.black else R.color.text_gray
                        )
                    )
            }
        }

        if (statusOnEdit) {
            setEditTextVisibility(true)
            area_signout.visibility = View.GONE
            area_btn_default.visibility = View.GONE
            area_btn_onEdit.visibility = View.VISIBLE
        } else {
            setEditTextVisibility(false)
            area_signout.visibility = View.VISIBLE
            area_btn_default.visibility = View.VISIBLE
            area_btn_onEdit.visibility = View.GONE
        }

    }


    private fun setDataToForm() {

        // Get data from some store to form?
        val token = LocalStore(requireContext()).getString("token", "")
        val call = apiService.profile(token)
        call.enqueue(object: Callback<MemberModel> {
            override fun onResponse(call: Call<MemberModel>, response: Response<MemberModel>) {
                val body = response.body()
                if (body?.fullName?.isNotEmpty() == true) {
                    edt_fullname.setText(body.fullName)
                }
                if (body?.tel?.isNotEmpty() == true) {
                    edt_phone.setText(body.tel)
                }
                if (body?.email?.isNotEmpty() == true) {
                    edt_email.setText(body.email)
                }
                loginWith = body?.authType.toString()
                changeViewSignout()
                // Disable loading
                setStateLoading(false)
            }

            override fun onFailure(call: Call<MemberModel>, t: Throwable) {
                MainActivity.setStateLoading(false, containerLoading)
                Toast.makeText(requireContext(), t.message, Toast.LENGTH_LONG).show()
            }

        })
    }


    private fun onEdit() {

        statusOnEdit = true
        this.toggleFormVisibility()
    }


    private fun onSave() {
        if (!this.validateForm()) return

        val token = LocalStore(requireContext()).getString("token", "")
        val call = apiService.update(token, MemberRequest(edt_fullname.text.toString(), edt_phone.text.toString(), edt_email.text.toString()))
        call.enqueue(object: Callback<MemberResponse> {
            override fun onResponse(
                call: Call<MemberResponse>,
                response: Response<MemberResponse>
            ) {
                if (response.body()?.status == false) {
                    Toast.makeText(requireContext(), response.body()?.message, Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<MemberResponse>, t: Throwable) {
                Toast.makeText(requireContext(), t.message, Toast.LENGTH_LONG).show()
            }

        })

        statusOnEdit = false
        this.toggleFormVisibility()
    }


    private fun changeViewSignout() {
        when (loginWith.uppercase()) {
            "LINE" -> {
                logo_signIn.setImageResource(R.drawable.line_logo)
                text_signIn.text = "You sign in with Line"
            }
            "GOOGLE" -> {
                logo_signIn.setImageResource(R.drawable.google_logo)
                text_signIn.text = "You sign in with Google"
            }
        }
    }


    override fun onClick(v: View?) {
        when(v?.id) {
            btn_edit.id ->
                this.onEdit()
            btn_save.id ->
                this.onSave()
            btn_cancel.id -> {
                statusOnEdit = false
                this.toggleFormVisibility()
            }
            btn_signOut.id -> {
                LocalStore(requireContext()).clearAll()
                val intent: Intent = Intent(requireContext(), AuthActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent);
                Toast.makeText(activity, "On Click SignOut", Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var root = inflater.inflate(R.layout.fragment_profile, container, false)
        root = this.initView(root)

        this.main()
        return root
    }


    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply { }
            }
    }


    private fun main() {
        this.setDataToForm()
        this.changeViewSignout()
    }

}