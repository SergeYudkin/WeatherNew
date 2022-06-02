package com.example.weathernew.view.details

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.load
import coil.request.ImageRequest
import com.example.weathernew.databinding.FragmentDetailsBinding
import com.example.weathernew.model.Weather
import com.example.weathernew.utils.*
import com.example.weathernew.view.BaseFragment
import com.example.weathernew.viewmodel.AppState
import com.example.weathernew.viewmodel.DetailsViewModel
import kotlinx.android.synthetic.main.fragment_details.*
import okhttp3.*


class DetailsFragment : BaseFragment<FragmentDetailsBinding>(FragmentDetailsBinding:: inflate){

//-------------------------------------------------------------------------------------------


   private val viewModel: DetailsViewModel by lazy {
        ViewModelProvider(this).get(DetailsViewModel::class.java)
    }


    @RequiresApi(Build.VERSION_CODES.P)
    private fun renderData(appState: AppState){
        with(binding){
            when(appState){
                is AppState.Error -> {

                }
                is AppState.Loading ->{
                    appState.progress
                }
                is AppState.SuccessCity -> {
                    val weather = appState.weatherData[0]
                    setWeatherData(weather)
                }
                else -> {}
            }
        }
    }

//--------------------------------------------------------------------------------------

    private lateinit var localWeather : Weather
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getLivaData().observe(viewLifecycleOwner,{
            renderData(it)
        })
        arguments?.let {
            it.getParcelable<Weather>(BUNDLE_KEY)?.let {
                localWeather = it
                viewModel.getWeatherFromRemoteServer(localWeather.city.lat,localWeather.city.lon)

            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun setWeatherData(weather: Weather) {

       // weatherIcon.setOnClickListener{   // кнопка для сохранения погоды
            weather.city = localWeather.city     // погода сохраняется автоматически после открытия фрагмента
            viewModel.saveWeather(weather)
        //}

        with(binding){
            with(localWeather) {
                cityName.text =
                    city.name                                   // заполнение данными которые получил onViewCreated
                cityCoordinates.text = "${city.lat} ${city.lon}"
                temperatureValue.text = "${weather.temperature}"
                feelsLikeValue.text = "${weather.feelsLike}"

//---------------------------------------------------------------------------------------------------------------
               /* Glide.with(headerIcon.context).load("https://freepngimg.com/thumb/city/36275-3-city-hd.png")
                    .into(headerIcon)*/

              /*  Picasso.get()
                    .load("https://freepngimg.com/thumb/city/36275-3-city-hd.png")          // подгружаесм картинки с помищью разных библиотек
                    .into(headerIcon)*/

                headerIcon.load("https://freepngimg.com/thumb/city/36275-3-city-hd.png")
                weatherIcon.loadUrl("https://yastatic.net/weather/i/icons/funky/dark/${weather.icon}.svg")
            }
        }

    }

    private fun ImageView.loadUrl(url: String) {

        val imageLoader = ImageLoader.Builder(this.context)
            .componentRegistry { add(SvgDecoder(this@loadUrl.context)) }    //загрузка векторных изображений
            .build()

        val request = ImageRequest.Builder(this.context)
            .data(url)
            .target(this)
            .build()

        imageLoader.enqueue(request)
    }

//---------------------------------------------------------------------------------------------
    companion object {

        fun newInstance(bundle: Bundle) = DetailsFragment().apply{              //  фабричный метод
                                                                                    // получает данные в бандле из метода onItemClick находящегося в MainFragment и вытаскивает их из бандла
            arguments = bundle
        }

    }


}