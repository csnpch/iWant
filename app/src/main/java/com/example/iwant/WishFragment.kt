package com.example.iwant

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.iwant.Helpers.setListViewHeightBasedOnChildren

class WishFragment : Fragment(), AdapterView.OnItemClickListener {


    private lateinit var container_wish: LinearLayout
    private lateinit var container_your_wish: LinearLayout
    private lateinit var listview_wish: ListView
    private lateinit var listview_yourWish: ListView

    private var your_wish_titles: ArrayList<String> = ArrayList()
    private var your_wish_subtitles: ArrayList<String> = ArrayList()
    private var your_wish_timestamps: ArrayList<String> = ArrayList()
    private var your_wish_response: ArrayList<Boolean> = ArrayList()

    private var wish_titles: ArrayList<String> = ArrayList()
    private var wish_subtitles: ArrayList<String> = ArrayList()
    private var wish_distances: ArrayList<String> = ArrayList()
    private var wish_timestamps: ArrayList<String> = ArrayList()


    private fun initView(view: View, savedInstanceState: Bundle?): View {
        container_wish = view.findViewById(R.id.wish_container_wish)
        container_your_wish = view.findViewById(R.id.wish_container_your_wish)
        listview_wish = view.findViewById(R.id.wish_items_wish)
        listview_yourWish = view.findViewById(R.id.wish_items_your_wish)

        this.setupYourWishList()
        this.setupWishList()
        return view;
    }


    private fun setupYourWishList() {
        var statusHaveWishList: Boolean = !false;

        if (!statusHaveWishList) {
            container_your_wish.visibility = View.GONE
            return
        } else {
            container_your_wish.visibility = View.VISIBLE
        }

        for (i in 0..1) {
            your_wish_titles.add("Title " + (i+1))
            your_wish_subtitles.add("Sub Title " + (i+1))
            your_wish_timestamps.add("now" + (i+1))
            your_wish_response.add(i % 2 == 0)
        }

        listview_yourWish.adapter = CustomListView_YourWish(requireContext(), your_wish_titles, your_wish_subtitles, your_wish_timestamps, your_wish_response)
        listview_yourWish.onItemClickListener = this;
        setListViewHeightBasedOnChildren(listview_yourWish)
    }


    private fun setupWishList() {
        for (i in 0..6) {
            wish_titles.add("Title " + (i+1))
            wish_subtitles.add("Sub Title " + (i+1))
            wish_distances.add("0." + (i+1).toString() + "km")
            wish_timestamps.add((i+1).toString() + "hour ago")
        }

        listview_wish.adapter = CustomListView_Wish(requireContext(), wish_titles, wish_subtitles, wish_distances, wish_timestamps)
        listview_wish.onItemClickListener = this;
        setListViewHeightBasedOnChildren(listview_wish)
    }


    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

        when(parent?.adapter) {
            listview_yourWish.adapter -> {
                Toast.makeText(requireContext(), "OnClickItem " + position + " on [YourWish]", Toast.LENGTH_SHORT).show();
            }
            listview_wish.adapter -> {
                Toast.makeText(requireContext(), "OnClickItem " + position + " on [Wish]", Toast.LENGTH_SHORT).show();
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