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
import com.example.weathernew.BuildConfig
import com.example.weathernew.databinding.FragmentDetailsBinding
import com.example.weathernew.lessons.MyBroadcastReceiver
import com.example.weathernew.lessons.MyService
import com.example.weathernew.model.Weather
import com.example.weathernew.model.WeatherDTO
import com.example.weathernew.utils.*
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException


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

    private var client : OkHttpClient? = null   //создаём клиент
    private fun getWeather(){
        if (client==null)
            client = OkHttpClient()

        val builder = Request.Builder().apply {
            header(API_KEY,BuildConfig.WEATHER_API_KEY)                  //указываем хедеры и адрес
            url(YANDEX_API_URL+YANDEX_API_URL_END_POINT
                    + "?lat=${localWeather.city.lat}&lon=${localWeather.city.lon}")
        }
        val request =  builder.build()              //билдер переводим в реквест
        val call =  client?.newCall(request)         // реквест превращаем в вызов
        /*Thread{
            val response = call?.execute() //ошибка network in main thread exception если не засунуть его в отдельный поток
        }.start()*/

        call?.enqueue(object :Callback{               //вызываем вызов в данном случае асинхронно во вспомогательном потоке
            override fun onFailure(call: Call, e: IOException) {
                TODO("Not yet implemented")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful){
                     response.body()?.let {
                         val json  = it.string()
                         requireActivity().runOnUiThread{               //обработка в главном потоке
                             setWeatherData(Gson().fromJson(json, WeatherDTO::class.java))
                         }
                    }

                }else{
                    //TODO HW код ошибки
                }
            }
        })
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
                getWeather()
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


}