package com.example.iwant

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.iwant.Helpers.setListViewHeightBasedOnChildren
import com.example.iwant.Dialogs.showDialogWish
import com.example.iwant.Dialogs.showDialogYourWish

class WishFragment : Fragment(), AdapterView.OnItemClickListener {

    private lateinit var container_wish: LinearLayout
    private lateinit var container_your_wish: LinearLayout
    private lateinit var listview_wish: ListView
    private lateinit var listview_yourWish: ListView

    private var your_wish_titles: ArrayList<String> = ArrayList()
    private var your_wish_description: ArrayList<String> = ArrayList()
    private var your_wish_timestamps: ArrayList<String> = ArrayList()
    private var your_wish_people_responses: ArrayList<Array<Array<String>>?> = arrayListOf(null)

    private var wish_titles: ArrayList<String> = ArrayList()
    private var wish_description: ArrayList<String> = ArrayList()
    private var wish_distances: ArrayList<String> = ArrayList()
    private var wish_timestamps: ArrayList<String> = ArrayList()
    private var wish_benefit: ArrayList<String> = ArrayList()
    private var wish_contact: ArrayList<String> = ArrayList()
    private var wish_latlog: ArrayList<ArrayList<Double>> = ArrayList()


    private fun initView(view: View, savedInstanceState: Bundle?): View {

        container_wish = view.findViewById(R.id.wish_container_wish)
        container_your_wish = view.findViewById(R.id.wish_container_your_wish)
        listview_wish = view.findViewById(R.id.wish_items_wish)
        listview_yourWish = view.findViewById(R.id.wish_items_your_wish)

        this.setDataToYourWishList()
        this.setDataToWishList()

        return view;
    }


    private fun setDataToYourWishList() {

        var statusHaveWishList: Boolean = !false;

        if (!statusHaveWishList) {
            container_your_wish.visibility = View.GONE
            return
        } else {
            container_your_wish.visibility = View.VISIBLE
        }


        for (i in 0..1) {
            your_wish_titles.add("Title Title Title Title Title Title Title " + (i+1))
            your_wish_description.add("5 days left for expire")
            your_wish_timestamps.add("now" + (i+1))
            if (i % 2== 0) {
                for (i in 0..1) {
                    val response1 = arrayOf(
                        arrayOf("Somjit Nimitmray$i", "0987654321", "03/03/2023, 20:24"),
                        arrayOf("John Doe$i", "0123456789", "03/03/2023, 20:24")
                    )
                    your_wish_people_responses.add(response1)
                }
            }
        }


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
            wish_latlog.add(arrayListOf(14.1508167 + (0.1 + i), 101.3611667 + (0.1 + i)))
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
                    0,
                    "Title Title Title 1",
                    "Sula Sama Description Description Description 1",
                    "4 DAYS",
                    4,
                    your_wish_people_responses[position]
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
                    wish_latlog[position]
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

    }


}