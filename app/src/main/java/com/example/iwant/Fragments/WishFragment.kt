package com.example.iwant.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.LinearLayout
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.example.iwant.CustomListView_Wish
import com.example.iwant.CustomListView_YourWish
import com.example.iwant.Helpers.setListViewHeightBasedOnChildren
import com.example.iwant.Dialogs.showDialogWish
import com.example.iwant.Dialogs.showDialogYourWish
import com.example.iwant.Helpers.PermissionUtils
import com.example.iwant.R
import com.example.iwant.Wishs.AddWishActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class WishFragment : Fragment(), AdapterView.OnItemClickListener {

    private lateinit var container_wish: LinearLayout
    private lateinit var container_your_wish: LinearLayout
    private lateinit var listview_wish: ListView
    private lateinit var listview_yourWish: ListView
    private lateinit var btn_floating_action_button: FloatingActionButton

    private var your_wish_titles: ArrayList<String> = ArrayList()
    private var your_wish_description: ArrayList<String> = ArrayList()
    private var your_wish_timestamps: ArrayList<String> = ArrayList()
    private var your_wish_people_responses: ArrayList<Array<Array<String>>?> = arrayListOf(null)
    private var your_wish_latlng: ArrayList<ArrayList<Double>> = ArrayList()

    private var wish_titles: ArrayList<String> = ArrayList()
    private var wish_description: ArrayList<String> = ArrayList()
    private var wish_distances: ArrayList<String> = ArrayList()
    private var wish_timestamps: ArrayList<String> = ArrayList()
    private var wish_benefit: ArrayList<String> = ArrayList()
    private var wish_contact: ArrayList<String> = ArrayList()
    private var wish_latlng: ArrayList<ArrayList<Double>> = ArrayList()


    private fun initView(view: View, savedInstanceState: Bundle?): View {

        container_wish = view.findViewById(R.id.wish_container_wish)
        container_your_wish = view.findViewById(R.id.wish_container_your_wish)
        listview_wish = view.findViewById(R.id.wish_items_wish)
        listview_yourWish = view.findViewById(R.id.wish_items_your_wish)
        btn_floating_action_button = view.findViewById(R.id.wish_btn_floating_action_button)

        btn_floating_action_button.setOnClickListener{
            startActivity(Intent(requireContext(), AddWishActivity::class.java))
            requireActivity().overridePendingTransition(R.anim.slide_left,R.anim.no_change)
        }

        this.setDataToYourWishList()
        this.setDataToWishList()

        return view;
    }


    private fun setDataToYourWishList() {

        var statusHaveWishList: Boolean = false
        for (i in 0..1) {
            statusHaveWishList = true
            your_wish_titles.add("Title Title Title Title Title Title Title " + (i+1))
            your_wish_description.add("${i+1} days left for expire")
            your_wish_timestamps.add("now" + (i+1))
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
            requireContext(), your_wish_titles, your_wish_description, your_wish_timestamps, your_wish_people_responses
        )
        listview_yourWish.onItemClickListener = this;
        setListViewHeightBasedOnChildren(listview_yourWish)

    }


    private fun setDataToWishList() {
        for (i in 0..6) {
            wish_titles.add("Title Title Title Title Title Title Title " + (i+1))
            wish_description.add("Sub Title Allow Access port when you need something maybe you can")
            wish_distances.add("0." + (i+1).toString() + " km")
            wish_timestamps.add((i+1).toString() + " hour ago")
            wish_benefit.add("Give a 10 bath")
            wish_contact.add("098765432" + (i+1).toString())
            wish_latlng.add(arrayListOf(14.1508167 + (0.1 + i), 101.3611667 + (0.1 + i)))
        }

        listview_wish.adapter = CustomListView_Wish(requireContext(), wish_titles, wish_description, wish_distances, wish_timestamps)
        listview_wish.onItemClickListener = this;
        setListViewHeightBasedOnChildren(listview_wish)
    }


    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

        when(parent?.adapter) {
            listview_yourWish.adapter -> {

                showDialogYourWish(
                    requireContext(),
                    requireActivity(),
                    position,
                    "Title Title Title 1",
                    "Sula Sama Description Description Description 1",
                    "4 DAYS",
                    4,
                    your_wish_people_responses[position],
                    your_wish_latlng[position]
                )

            }
            listview_wish.adapter -> {

                showDialogWish(
                    requireContext(),
                    requireActivity(),
                    position,
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