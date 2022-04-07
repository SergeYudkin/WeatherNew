package com.example.weathernew.view.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.weathernew.databinding.FragmentDetailsBinding
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

        arguments?.let {
            it.getParcelable<Weather>(BUNDLE_KEY)?.run {
                setWeatherData(this)
            }
        }

    }

    private fun setWeatherData(weather: Weather) {
        with(binding){
            cityName.text = weather.city.name                                   // заполнение данными которые получил onViewCreated
            cityCoordinates.text = "${weather.city.lat} ${weather.city.lon}"
            temperatureValue.text = "${weather.temperature}"
            feelsLikeValue.text = "${weather.feelsLike}"
        }

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

        fun newInstance(bundle: Bundle) = DetailsFragment().apply{              //  фабричный метод
                                                                                    // получает данные в бандле из метода onItemClick находящегося в MainFragment и вытаскивает их из бандла
            arguments = bundle
        }

    }
//-----------------------------------------------------------------------------------------------
}