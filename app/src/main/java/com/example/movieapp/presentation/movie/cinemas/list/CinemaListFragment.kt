package com.example.movieapp.presentation.movie.cinemas.list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.movieapp.R
import com.example.movieapp.base.BaseFragment
import com.example.movieapp.data.room.Cinema
import com.example.movieapp.presentation.movie.cinemas.CinemaViewModel
import com.example.movieapp.presentation.movie.cinemas.adapters.CinemaListAdapter
import com.example.movieapp.utils.AppConstants

class CinemaListFragment : BaseFragment() {

    private lateinit var navController: NavController
    private lateinit var listViewModel: CinemaViewModel
    private lateinit var rvCinemas: RecyclerView
    private val TAG = "CinemaTag:"

    companion object {
        fun newInstance() : CinemaListFragment =
            CinemaListFragment()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cinema_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listViewModel = ViewModelProviders.of(this).get(CinemaViewModel::class.java)
        bindViews(view)
        setAdapter()
        setData()
    }

    private val onClickListener = object:
        CinemaListAdapter.ItemClickListener {
        override fun onItemClick(item: Cinema) {
            val bundle = Bundle()
            item.id?.let{
                bundle.putInt(AppConstants.CINEMA_ID, item.id)
            }

            navController.navigate(
                R.id.action_cinemaFragment_to_cinemaDetailsFragment,
                bundle
            )
            Log.d(TAG, bundle.getInt(AppConstants.CINEMA_ID).toString())
        }
    }
    private val cinemaListAdapter by lazy {
        CinemaListAdapter (
            context = context,
            itemClickListener = onClickListener
        )
    }

    override fun bindViews(view: View) = with(view) {
        navController = Navigation.findNavController(this)
        rvCinemas = findViewById(R.id.recyclerView)
        rvCinemas.layoutManager = LinearLayoutManager(context)
    }

    override fun setData() {
        listViewModel.liveData.observe(viewLifecycleOwner, Observer { cinemaList ->
            cinemaListAdapter.setCinemas(cinemaList)
        })
    }

    private fun setAdapter() {
        rvCinemas.adapter = cinemaListAdapter
    }
}