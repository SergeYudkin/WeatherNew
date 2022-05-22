package com.example.weathernew.lessons

import android.location.Geocoder
import android.location.Location
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.weathernew.R
import com.example.weathernew.databinding.FragmentGoogleMapsMainBinding
import com.example.weathernew.databinding.FragmentMapsBinding
import com.example.weathernew.databinding.FragmentThreadsBinding

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MapsFragment : Fragment() {

    private var _binding: FragmentGoogleMapsMainBinding? = null
    private val binding: FragmentGoogleMapsMainBinding
        get() {
            return _binding!!
        }

    lateinit var map:GoogleMap
    val markers = arrayListOf<Marker>()

    private val callback = OnMapReadyCallback { googleMap ->

        map = googleMap

        val m = LatLng(54.0, 37.0)
        googleMap.addMarker(MarkerOptions().position(m).title("Marker in Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(m))

        googleMap.setOnMapLongClickListener {
            getAddress(it)
        }
    }

    private fun addMarker(location: LatLng){

    }

    private fun getAddress(location: LatLng){

        Thread{
            val geocoder = Geocoder(requireContext())
            val listAddress =  geocoder.getFromLocation(location.latitude, location.longitude,1)
            requireActivity().runOnUiThread {binding.textAddress.text = listAddress[0].getAddressLine(0)
            }
        }.start()


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGoogleMapsMainBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        binding.buttonSearch.setOnClickListener{

        }
    }
}