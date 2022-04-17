package com.example.weathernew.view.details

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.weathernew.databinding.FragmentDetailsBinding
import com.example.weathernew.lessons.MyBroadcastReceiver
import com.example.weathernew.lessons.MyService
import com.example.weathernew.model.Weather
import com.example.weathernew.model.WeatherDTO
import com.example.weathernew.utils.*


class DetailsFragment : Fragment() {

//------------------------------------------------------------------------------------
private var _binding : FragmentDetailsBinding? = null    // привязываем макет
      private val binding : FragmentDetailsBinding     // binding не null
    get(){
        return _binding!!
    }


    private val receiver:BroadcastReceiver = object : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                val weatherDTO = it.getParcelableExtra<WeatherDTO>(BUNDLE_KEY_WEATHER)
                if (weatherDTO!=null){
                    setWeatherData(weatherDTO)
                }else{

                }
            }
        }
    }
//-------------------------------------------------------------------------------------

    override fun  onDestroy() {
        super.onDestroy()
        _binding = null
       // requireActivity().unregisterReceiver(receiver)             // закрываем глобальную регистрацию
        LocalBroadcastManager.getInstance(requireActivity()).unregisterReceiver(receiver)   // закрываем локальную регистрацию
    }
//--------------------------------------------------------------------------------------

    lateinit var localWeather : Weather
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            it.getParcelable<Weather>(BUNDLE_KEY)?.let {
                localWeather = it
                val  intent = Intent(requireActivity(),DetailsService::class.java)
                        intent.putExtra(BUNDLE_KEY_LAT, localWeather.city.lat)  // передаём координаты в детеилс сервис
                intent.putExtra(BUNDLE_KEY_LON, localWeather.city.lon)
                requireActivity().startService(intent)
                LocalBroadcastManager.getInstance(requireActivity())
                    .registerReceiver(receiver, IntentFilter(DETAILS_INTENT_FILTER))  // регистрируем ресивер локальный
            }
        }

            //requireActivity().registerReceiver(receiver, IntentFilter(BROADCAST_ACTION))   // регистрируем ресивер глобальный


    }

    private fun setWeatherData(weatherDTO: WeatherDTO) {
        with(binding){
            with(localWeather) {
                cityName.text =
                    city.name                                   // заполнение данными которые получил onViewCreated
                cityCoordinates.text = "${city.lat} ${city.lon}"
                temperatureValue.text = "${weatherDTO.fact.temp}"
                feelsLikeValue.text = "${weatherDTO.fact.feelsLike}"
            }
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
//---------------------------------------------------------------------------------------------------


}