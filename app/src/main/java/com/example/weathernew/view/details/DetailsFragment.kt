package com.example.weathernew.view.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.weathernew.databinding.FragmentDetailsBinding
import com.example.weathernew.databinding.FragmentMainBinding
import com.example.weathernew.model.Weather

const val BUNDLE_KEY = "key"

class DetailsFragment : Fragment() {

//------------------------------------------------------------------------------------
private var _binding : FragmentDetailsBinding? = null    // привязываем макет
      private val binding : FragmentDetailsBinding     // binding не null
    get(){
        return _binding!!
    }
//-------------------------------------------------------------------------------------

    override fun  onDestroy() {
        super.onDestroy()
        _binding = null
    }
//--------------------------------------------------------------------------------------


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val weather = arguments?.getParcelable<Weather>(BUNDLE_KEY)
        if (weather!= null){
            setWeatherData(weather)
        }

    }

    private fun setWeatherData(weather: Weather) {
        binding.cityName.text = weather.city.name
        binding.cityCoordinates.text = "${weather.city.lat} ${weather.city.lon}"
        binding.temperatureValue.text = "${weather.temperature}"
        binding.feelsLikeValue.text = "${weather.feelsLike}"
    }

//-----------------------------------------------------------------------------------------

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =  FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }
//---------------------------------------------------------------------------------------------
    companion object {

        fun newInstance(bundle: Bundle):DetailsFragment{
            val fragment = DetailsFragment()                //  фабричный метод
            fragment.arguments = bundle
            return fragment
        }


    }
//-----------------------------------------------------------------------------------------------
}