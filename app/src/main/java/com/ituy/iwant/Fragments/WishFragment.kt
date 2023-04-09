package com.ituy.iwant.Fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.android.flexbox.FlexboxLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ituy.iwant.CustomListView_Wish
import com.ituy.iwant.CustomListView_YourWish
import com.ituy.iwant.Dialogs.showDialogWish
import com.ituy.iwant.Dialogs.showDialogYourWish
import com.ituy.iwant.Helpers.Helpers
import com.ituy.iwant.Helpers.PermissionUtils
import com.ituy.iwant.Helpers.setListViewHeightBasedOnChildren
import com.ituy.iwant.Maps.PickupLocationActivity
import com.ituy.iwant.R
import com.ituy.iwant.Stores.LocalStore
import com.ituy.iwant.Wishs.AddWishActivity
import com.ituy.iwant.api.wish.WishModel
import com.ituy.iwant.api.wish.WishService
import io.jsonwebtoken.Jwts.parser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit


class WishFragment : Fragment(), AdapterView.OnItemClickListener, View.OnClickListener {

    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var container_wish: LinearLayout
    private lateinit var container_your_wish: LinearLayout
    private lateinit var listview_wish: ListView
    private lateinit var listview_yourWish: ListView
    private lateinit var txt_your_location: TextView
    private lateinit var btn_floating_action_button: FloatingActionButton
    private lateinit var btn_location_choose_info: FlexboxLayout

    private lateinit var wish_no_data_wish: TextView

    private var your_wish_ids: ArrayList<String> = ArrayList()
    private var your_wish_titles: ArrayList<String> = ArrayList()
    private var your_wish_description: ArrayList<String> = ArrayList()
    private var your_wish_time_for_expire: ArrayList<String> = ArrayList()
    private var your_wish_timestamps: ArrayList<String> = ArrayList()
    private var your_wish_people_responses: ArrayList<Array<Array<String>>?> = arrayListOf()
    private var your_wish_latlng: ArrayList<ArrayList<Double>> = ArrayList()

    private var wish_ids: ArrayList<String> = ArrayList()
    private var wish_titles: ArrayList<String> = ArrayList()
    private var wish_description: ArrayList<String> = ArrayList()
    private var wish_distances: ArrayList<String> = ArrayList()
    private var wish_timestamps: ArrayList<String> = ArrayList()
    private var wish_benefit: ArrayList<String> = ArrayList()
    private var wish_contact: ArrayList<String> = ArrayList()
    private var wish_latlng: ArrayList<ArrayList<Double>> = ArrayList()

    private var UNIT_DAY_ADD_MORE_EXPIRE: Int = 1
    private var currentUserLocation = DoubleArray(2)

    private val apiWish = WishService()

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

        wish_no_data_wish = view.findViewById(R.id.wish_no_data_wish)

        this.setDataToYourWishList()
        this.setDataToWishList()

        this.initLoading()

        return view;
    }


    private fun initLoading() {
    }


    // Your wish
    private fun setDataToYourWishList() {

        var statusHaveWishList: Boolean = false
        val token = LocalStore(requireContext()).getString("token", "")
        val call = apiWish.getWishByMe(token)
        call.enqueue(object: Callback<List<WishModel>> {
            override fun onResponse(
                call: Call<List<WishModel>>,
                response: Response<List<WishModel>>
            ) {
                response.body()?.listIterator()?.forEach { item ->
                    statusHaveWishList = true
                    val formatter = SimpleDateFormat("MM/dd/yyyy")
                    val now: String = formatter.format(Date())
                    val created: String = formatter.format(item.createdAt)
                    val expire: String = formatter.format(item.expire)

                    if (now.compareTo(expire) < 0) {
                        val dateFormatter: DateTimeFormatter =  DateTimeFormatter.ofPattern("MM/dd/yyyy")
                        val now_format = LocalDate.parse(now, dateFormatter)
                        val expire_format = LocalDate.parse(expire, dateFormatter)
                        val cal_expire = Period.between(now_format, expire_format)
                        val cal_created =  TimeUnit.MILLISECONDS.toMinutes(Date().time - item.createdAt.time)

                        your_wish_ids.add(item.id.toString())
                        your_wish_titles.add(item.title.toString())
                        your_wish_description.add(item.description.toString())

                        your_wish_time_for_expire.add("${cal_expire.days} days") // dialog += "left for expire"

                        your_wish_timestamps.add("$cal_created mins")
                        val loc = item.location.split(",")
                        your_wish_latlng.add(arrayListOf(loc[0].toDouble(), loc[1].toDouble()))
                        if (item.deliverers.isNotEmpty()) {
                            val peoples = Array<Array<String>>(item.deliverers.size) { Array<String>(3) { "" } }
                            item.deliverers.forEachIndexed { index, item2 ->
                                val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
                                val output = formatter.format(item2.createdAt)
                                val people = arrayOf(item2.fullName, item2.tel, output)
                                peoples[index] = people;
                            }
                            your_wish_people_responses.add(peoples)
                        } else {
                            your_wish_people_responses.add(null)
                        }
                    }
                }


                listview_yourWish.adapter = CustomListView_YourWish(
                    requireContext(), your_wish_ids, your_wish_titles, your_wish_time_for_expire, your_wish_timestamps, your_wish_people_responses
                )

                if (statusHaveWishList)

                    container_your_wish.visibility = View.VISIBLE

                setListViewHeightBasedOnChildren(listview_yourWish)
            }

            override fun onFailure(call: Call<List<WishModel>>, t: Throwable) {
                Toast.makeText(requireContext(), t.message, Toast.LENGTH_LONG).show()
                statusHaveWishList = false
            }

        })

        listview_yourWish.adapter = CustomListView_YourWish(
            requireContext(), your_wish_ids, your_wish_titles, your_wish_time_for_expire, your_wish_timestamps, your_wish_people_responses
        )

        if (statusHaveWishList)
            container_your_wish.visibility = View.VISIBLE

        setListViewHeightBasedOnChildren(listview_yourWish)

        listview_yourWish.onItemClickListener = this;
    }

    private fun clearDataYourWish() {
        your_wish_ids = ArrayList()
        your_wish_titles = ArrayList()
        your_wish_description = ArrayList()
        your_wish_time_for_expire = ArrayList()
        your_wish_timestamps = ArrayList()
        your_wish_people_responses = arrayListOf(null)
        your_wish_latlng = ArrayList()
    }

    private fun clearDataWish() {
        wish_ids = ArrayList()
        wish_titles = ArrayList()
        wish_description = ArrayList()
        wish_distances = ArrayList()
        wish_timestamps = ArrayList()
        wish_benefit = ArrayList()
        wish_contact = ArrayList()
        wish_latlng = ArrayList()
    }

    // Wish public
    private fun setDataToWishList() {
        var statusHaveDataWishList = false
        wish_no_data_wish.visibility = View.VISIBLE

        val token = LocalStore(requireContext()).getString("token", "")
        val locationSearch = (currentUserLocation[0].toString() + "," +currentUserLocation[1].toString())
        val call = apiWish.getWishByLocation(token, locationSearch)
        call.enqueue(object: Callback<List<WishModel>> {
            override fun onResponse(
                call: Call<List<WishModel>>,
                response: Response<List<WishModel>>
            ) {
                response.body()?.listIterator()?.forEach { item ->
                    if (!statusHaveDataWishList)
                        wish_no_data_wish.visibility = View.GONE
                    statusHaveDataWishList = true

                    val now: Date = Date()
                    val created: Date = item.createdAt
                    val expire: Date = item.expire

                    if (now.compareTo(expire) < 0) {
                        val created_time = now.time - created.time
                        val cal_created = TimeUnit.MINUTES.convert(created_time, TimeUnit.MILLISECONDS)
                        wish_ids.add(item.id.toString())
                        wish_titles.add(item.title)
                        wish_description.add(item.description)
                        wish_timestamps.add("$cal_created mins")
                        wish_benefit.add(item.benefit)
                        wish_contact.add(item.contact)
                        val loc = item.location.split(",")
                        wish_latlng.add(arrayListOf(loc[0].toDouble(), loc[1].toDouble()))
                        val results = FloatArray(1)
                        Location.distanceBetween(currentUserLocation[0], currentUserLocation[1], loc[0].toDouble(), loc[1].toDouble(), results)
                        wish_distances.add("${results[0]} km")
                    }
                }
                listview_wish.adapter = CustomListView_Wish(requireContext(), wish_ids, wish_titles, wish_description, wish_distances, wish_timestamps)
                setListViewHeightBasedOnChildren(listview_wish)
            }

            override fun onFailure(call: Call<List<WishModel>>, t: Throwable) {
                Toast.makeText(requireContext(), t.message, Toast.LENGTH_LONG).show()
            }

        })


        listview_wish.onItemClickListener = this;
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
                startActivityForResult(Intent(requireContext(), AddWishActivity::class.java), 100)
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
                ) {
                    clearDataYourWish()
                    setDataToYourWishList()
                }

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
                ) {
                    clearDataWish()
                    setDataToWishList()
                }

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
                        clearDataWish()
                        setDataToWishList()
//                        Toast.makeText(requireContext(), "INTENT: lat = ${currentUserLocation[0]}, log = ${currentUserLocation[1]}", Toast.LENGTH_SHORT).show()
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

        val location = LocalStore(requireActivity()).getString("CurrentLocationLatLng", "${14.158904701557415},${101.34582541674533}")
        val locSplit = location.split(",")
        currentUserLocation[0] = locSplit[0].toDouble()
        currentUserLocation[1] = locSplit[1].toDouble()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100) {
            clearDataYourWish()
            clearDataWish()
            setDataToYourWishList()
            setDataToWishList()
        }
    }


}