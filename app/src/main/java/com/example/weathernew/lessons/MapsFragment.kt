package com.example.weathernew.lessons

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Geocoder
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.example.weathernew.R
import com.example.weathernew.databinding.FragmentGoogleMapsMainBinding
import com.example.weathernew.view.BaseFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*

class MapsFragment : BaseFragment<FragmentGoogleMapsMainBinding>(FragmentGoogleMapsMainBinding:: inflate) {


    lateinit var map:GoogleMap
    private val markers = arrayListOf<Marker>()

    private val callback = OnMapReadyCallback { googleMap ->    //Интерфейс обратного вызова, когда карта готова к использованию
        map = googleMap
        val m = LatLng(54.0, 37.0)
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(m))
        googleMap.setOnMapLongClickListener {
            getAddress(it)
            addMarker(it)
            drawLine(it)

        }

        myLoc(googleMap)


        googleMap.uiSettings.isZoomControlsEnabled = true      // добавляем зум на карте
    }

    private fun myLoc(googleMap: GoogleMap){                // определение своего местоположения на карте
        activity?.let {
            if (ContextCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
            ) {
                googleMap.isMyLocationEnabled = true

            }
        }
    }

//----------------------------------------------------------------------------------------------------------
    private fun drawLine(location: LatLng){
        val last = markers.size
        if (last>1){
            map.addPolyline(PolylineOptions().add(markers[last-1].position,markers[last-2].position)   // рисуем линию между маркерами
                .color(Color.RED).width(5f))
        }
    }
//---------------------------------------------------------------------------------------------------------------
    private fun addMarker(location: LatLng){
        val marker = map.addMarker(MarkerOptions().position(location).icon(BitmapDescriptorFactory.fromResource(   // добавить маркер
            R.drawable.ic_map_marker)))
        markers.add(marker!!)
    }
//----------------------------------------------------------------------------------------------------------------
    private fun getAddress(location: LatLng){

        Thread{
            val geocoder = Geocoder(requireContext())
            val listAddress =  geocoder.getFromLocation(location.latitude, location.longitude,1)
            requireActivity().runOnUiThread {binding.textAddress.text = listAddress[0].getAddressLine(0)
            }
        }.start()

    }
//-----------------------------------------------------------------------------------------------------------

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        binding.buttonSearch.setOnClickListener{
            search()
        }
    }
//--------------------------------------------------------------------------------------------------------------
    private fun search(){

        Thread{
            val geocoder = Geocoder(requireContext())                                                           // поиск точки на карте по адресу
            val listAddress =  geocoder.getFromLocationName(binding.searchAddress.text.toString(),1)
            requireActivity().runOnUiThread {
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(listAddress[0].latitude,listAddress[0].longitude),
                8f))

                map.addMarker(MarkerOptions().position(LatLng(listAddress[0].latitude,listAddress[0].longitude)).title("")   // устанавливаем булавку в точке на карте которую тскали
                    .icon(BitmapDescriptorFactory.fromResource(
                        R.drawable.ic_map_pin)))
            }
        }.start()
    }
}