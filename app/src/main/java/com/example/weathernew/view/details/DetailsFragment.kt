package com.example.weathernew.view.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.weathernew.databinding.FragmentDetailsBinding
import com.example.weathernew.model.Weather
import com.example.weathernew.model.WeatherDTO
import com.example.weathernew.utils.WeatherLoader

const val BUNDLE_KEY = "key"

class DetailsFragment : Fragment(), WeatherLoader.OnWeatherLoaded {       // имплементируем интерфейс OnWeatherLoaded из WeatherLoader

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

    private val weatherLoader = WeatherLoader(this)
    lateinit var localWeather : Weather
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            it.getParcelable<Weather>(BUNDLE_KEY)?.let {
                localWeather = it
                weatherLoader.loadWeather(it.city.lat,it.city.lon)     // передаём ссылку на реализацию интерфейса OnWeatherLoaded
            }
        }

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
    override fun onLoaded(weatherDTO: WeatherDTO?) {
        weatherDTO?.let {                           // функции которые мы добавили автоматически в результате имплементации интерфейса OnWeatherLoaded, сюда и передаётся ответ
            setWeatherData(weatherDTO)
        }

    }

    override fun onFailed() {
        TODO("Not yet implemented")
    }
//-----------------------------------------------------------------------------------------------
}