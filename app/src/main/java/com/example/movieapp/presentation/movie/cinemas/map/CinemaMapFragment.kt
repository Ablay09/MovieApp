package com.example.movieapp.presentation.movie.cinemas.map

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.movieapp.R
import com.example.movieapp.base.BaseFragment
import com.example.movieapp.presentation.movie.cinemas.CinemaViewModel
import com.example.movieapp.utils.AppConstants
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import org.koin.android.ext.android.inject


class CinemaMapFragment : BaseFragment(),
    OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener {

    private lateinit var map: GoogleMap
    private val viewModel: CinemaViewModel by inject()
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
                val currentLatLng = cinema.latitude?.let { cinema.longitude?.let { it1 ->
                    LatLng(it,
                        it1
                    )
                    }
                }
                map.addMarker(
                    currentLatLng?.let { latLng ->
                        cinema.id?.let { id ->
                            MarkerOptions()
                                .position(latLng)
                                .title(cinema.name)
                                .zIndex(id.toFloat())
                        }
                    }
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

    override fun onMarkerClick(marker: Marker?): Boolean {
        val bundle = Bundle()
        marker?.zIndex?.toInt()?.let { id ->
            bundle.putInt(AppConstants.CINEMA_ID, id)
        }
        Log.d("CinemaId:", bundle.getInt(AppConstants.CINEMA_ID).toString())

        navController.navigate(
            R.id.action_cinemaFragment_to_cinemaDetailsFragment,
            bundle
        )
        return false
    }
}
