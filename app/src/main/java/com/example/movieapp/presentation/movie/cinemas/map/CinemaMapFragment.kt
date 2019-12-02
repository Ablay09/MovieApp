package com.example.movieapp.presentation.movie.cinemas.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.movieapp.R
import com.example.movieapp.base.BaseFragment
import com.example.movieapp.presentation.movie.cinemas.CinemaViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions


class CinemaMapFragment : BaseFragment(),
    OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener {

    private lateinit var map: GoogleMap
    private lateinit var viewModel: CinemaViewModel
    private lateinit var navController: NavController

    companion object {
        fun newInstance() : CinemaMapFragment =
            CinemaMapFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cinema_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(CinemaViewModel::class.java)
        bindViews(view)
        setData()
    }

    override fun bindViews(view: View) = with(view) {
        navController = Navigation.findNavController(this)
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this@CinemaMapFragment)
    }

    override fun setData() {
        viewModel.liveData.observe(viewLifecycleOwner, Observer { cinemaList ->
            cinemaList.map { cinema ->
                val currentLatLng = LatLng(cinema.latitude!!, cinema.longitude!!)
                map.addMarker(
                    MarkerOptions()
                        .position(currentLatLng)
                        .title(cinema.name)
                )
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
            }
        })
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.uiSettings.isZoomControlsEnabled = true
        map.setOnMarkerClickListener(this)
    }

    override fun onMarkerClick(marker: Marker?): Boolean  = false
}
