package com.ituy.iwant.Fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.android.flexbox.FlexboxLayout
import com.ituy.iwant.CustomListView_Wish
import com.ituy.iwant.CustomListView_YourWish
import com.ituy.iwant.Helpers.setListViewHeightBasedOnChildren
import com.ituy.iwant.Dialogs.showDialogWish
import com.ituy.iwant.Dialogs.showDialogYourWish
import com.ituy.iwant.Helpers.PermissionUtils
import com.ituy.iwant.R
import com.ituy.iwant.Wishs.AddWishActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ituy.iwant.Helpers.Helpers
import com.ituy.iwant.Maps.PickupLocationActivity
import com.ituy.iwant.MainActivity.Companion.setStateLoading

class WishFragment : Fragment(), AdapterView.OnItemClickListener, View.OnClickListener {

    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var container_wish: LinearLayout
    private lateinit var container_your_wish: LinearLayout
    private lateinit var listview_wish: ListView
    private lateinit var listview_yourWish: ListView
    private lateinit var txt_your_location: TextView
    private lateinit var btn_floating_action_button: FloatingActionButton
    private lateinit var btn_location_choose_info: FlexboxLayout

    private var your_wish_ids: ArrayList<String> = ArrayList()
    private var your_wish_titles: ArrayList<String> = ArrayList()
    private var your_wish_description: ArrayList<String> = ArrayList()
    private var your_wish_time_for_expire: ArrayList<String> = ArrayList()
    private var your_wish_timestamps: ArrayList<String> = ArrayList()
    private var your_wish_people_responses: ArrayList<Array<Array<String>>?> = arrayListOf(null)
    private var your_wish_latlng: ArrayList<ArrayList<Double>> = ArrayList()

    private var wish_ids: ArrayList<String> = ArrayList()
    private var wish_titles: ArrayList<String> = ArrayList()
    private var wish_description: ArrayList<String> = ArrayList()
    private var wish_distances: ArrayList<String> = ArrayList()
    private var wish_timestamps: ArrayList<String> = ArrayList()
    private var wish_benefit: ArrayList<String> = ArrayList()
    private var wish_contact: ArrayList<String> = ArrayList()
    private var wish_latlng: ArrayList<ArrayList<Double>> = ArrayList()

    private var UNIT_DAY_ADD_MORE_EXPIRE: Int = 4
    private var currentUserLocation = DoubleArray(2)


    private fun initView(view: View, savedInstanceState: Bundle?): View {
        container_wish = view.findViewById(R.id.wish_container_wish)
        container_your_wish = view.findViewById(R.id.wish_container_your_wish)
        listview_wish = view.findViewById(R.id.wish_items_wish)
        listview_yourWish = view.findViewById(R.id.wish_items_your_wish)
        txt_your_location = view.findViewById(R.id.wish_txt_your_location)

        btn_floating_action_button = view.findViewById(R.id.wish_btn_floating_action_button)
        btn_floating_action_button.setOnClickListener(this)

        btn_location_choose_info = view.findViewById(R.id.wish_btn_location_choose_info)
        btn_location_choose_info.setOnClickListener(this)

        this.setDataToYourWishList()
        this.setDataToWishList()

        // import com.ituy.iwant.MainActivity.Companion.setStateLoading
        setStateLoading(false, view.findViewById(R.id.loadingContainer))
        return view;
    }


    // Your wish
    private fun setDataToYourWishList() {

        var statusHaveWishList: Boolean = false
        for (i in 0..1) {
            statusHaveWishList = true
            your_wish_ids.add("0$i");
            your_wish_titles.add("Title Title Title Title Title Title Title " + (i+1))
            your_wish_description.add("Sula Sama Description Description 1")
            your_wish_time_for_expire.add("${i+1} days") // dialog += "left for expire"
            your_wish_timestamps.add("${i+1} min")
            your_wish_latlng.add(arrayListOf(14.1508167 + (0.1 + i), 101.3611667 + (0.1 + i)))
            // if check data peoples responses then
            if (i % 2 == 0) {
                for (i in 0..1) {
                    val response1 = arrayOf(
                        arrayOf("Somjit Nimitmray$i", "0987654321", "03/03/2023, 20:24"),
                        arrayOf("John Doe$i", "0123456789", "03/03/2023, 20:24")
                    )
                    your_wish_people_responses.add(response1)
                }
            } else {
                your_wish_people_responses.add(null)
            }
        }


        if (statusHaveWishList)
            container_your_wish.visibility = View.VISIBLE


        listview_yourWish.adapter = CustomListView_YourWish(
            requireContext(), your_wish_ids, your_wish_titles, your_wish_time_for_expire, your_wish_timestamps, your_wish_people_responses
        )
        listview_yourWish.onItemClickListener = this;
        setListViewHeightBasedOnChildren(listview_yourWish)

    }

    // Wish public
    private fun setDataToWishList() {
        for (i in 0..6) {
            wish_ids.add("0$i")
            wish_titles.add("Title Title Title Title Title Title Title " + (i+1))
            wish_description.add("Sub Title Allow Access port when you need something maybe you can")
            wish_distances.add("0." + (i+1).toString() + " km")
            wish_timestamps.add((i+1).toString() + " hour ago")
            wish_benefit.add("Give a 10 bath")
            wish_contact.add("098765432" + (i+1).toString())
            wish_latlng.add(arrayListOf(14.1508167 + (0.1 + i), 101.3611667 + (0.1 + i)))
        }

        listview_wish.adapter = CustomListView_Wish(requireContext(), wish_ids, wish_titles, wish_description, wish_distances, wish_timestamps)
        listview_wish.onItemClickListener = this;
        setListViewHeightBasedOnChildren(listview_wish)
    }


    private fun showPickupLocationName() {
        // Check if view only not move location
        if (currentUserLocation[0] == currentUserLocation[0] && currentUserLocation[1] == currentUserLocation[1]) {
            txt_your_location.text = "Current location"
            return;
        }

        val helpers = Helpers()
        val strLat = helpers.subStringLength(currentUserLocation[0].toString(), 12)
        val strLng = helpers.subStringLength(currentUserLocation[1].toString(), 12)
        txt_your_location.text = "$strLat, $strLng"
    }


    private fun getUserLocation() {
    }


    override fun onClick(v: View?) {

        when (v?.id) {

            btn_floating_action_button.id -> {
                startActivity(Intent(requireContext(), AddWishActivity::class.java))
                requireActivity().overridePendingTransition(R.anim.slide_left,R.anim.no_change)
            }
            btn_location_choose_info.id -> {
                val intent = Intent(requireContext(), PickupLocationActivity::class.java)
                intent.putExtra("latLngChooseLocation", "lat:${currentUserLocation[0]},lng:${currentUserLocation[1]}")
                activityResultLauncher.launch(intent)
                requireActivity().overridePendingTransition(R.anim.no_change, R.anim.no_change)
            }

        }

    }


    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

        when(parent?.adapter) {
            listview_yourWish.adapter -> {

                showDialogYourWish(
                    requireContext(),
                    requireActivity(),
                    position,
                    your_wish_ids[position],
                    your_wish_titles[position],
                    your_wish_description[position],
                    your_wish_time_for_expire[position],
                    UNIT_DAY_ADD_MORE_EXPIRE,
                    your_wish_people_responses[position],
                    your_wish_latlng[position]
                )

            }
            listview_wish.adapter -> {

                showDialogWish(
                    requireContext(),
                    requireActivity(),
                    position,
                    wish_ids[position],
                    wish_titles[position],
                    wish_timestamps[position],
                    wish_distances[position],
                    wish_description[position],
                    wish_benefit[position],
                    wish_contact[position],
                    wish_latlng[position]
                )

            }
        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                val returnedValueLatLng = data?.getStringExtra("latLngChooseLocation")
                if (returnedValueLatLng != null) {
                    val latLng = returnedValueLatLng!!.split(",")
                    if (latLng.isNotEmpty()) {
                        currentUserLocation[0] = latLng[0].substring(4).toDouble()
                        currentUserLocation[1] = latLng[1].substring(4).toDouble()
                        this.showPickupLocationName()
                        Toast.makeText(requireContext(), "INTENT: lat = ${currentUserLocation[0]}, log = ${currentUserLocation[1]}", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), "Can't get latLng from previous activity", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var root = inflater.inflate(R.layout.fragment_wish, container, false)
        root = this.initView(root, savedInstanceState)

        this.getUserLocation()
        this.main()
        return root
    }


    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            WishFragment().apply {
                arguments = Bundle().apply { }
            }
    }


    private fun main() {
        PermissionUtils.checkLocationPermission(this@WishFragment);
    }


}