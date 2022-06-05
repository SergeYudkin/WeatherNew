package com.example.weathernew.view.history

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.weathernew.databinding.FragmentHistoryBinding
import com.example.weathernew.model.Weather
import com.example.weathernew.view.BaseFragment
import com.example.weathernew.view.main.OnMyItemClickListener
import com.example.weathernew.viewmodel.AppState
import com.example.weathernew.viewmodel.HistoryViewModel

class HistoryFragment : BaseFragment<FragmentHistoryBinding>(FragmentHistoryBinding:: inflate), OnMyItemClickListener {      // привязали интерфейс OnMyItemClickListener который даст MainFragment способность принимать клики и реагировать на метод onItemClick


    private val adapter: CitiesHistoryAdapter by lazy {          // вызвали адаптер
        CitiesHistoryAdapter(this)
    }


//--------------------------------------------------------------------------------------

    private  val viewModel : HistoryViewModel by lazy {
        ViewModelProvider(this).get(HistoryViewModel::class.java)
    }

//---------------------------------------------------------------------------------------

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

         viewModel.getLivaData().observe(viewLifecycleOwner, Observer <AppState> { renderData(it) })
        viewModel.getAllHistory()
        binding.historyFragmentRecyclerview.adapter = adapter

    }

//----------------------------------------------------------------------------------------------

    private fun renderData(appState: AppState){
        with(binding){
            when(appState){
                is AppState.Error -> {
                    appState.error
                }
                is AppState.Loading -> {
                   appState.progress
                }
                is AppState.SuccessCity -> {
                    adapter.setWeather(appState.weatherData)
                }
                is AppState.SuccessDetails -> {
                    adapter.setWeather(appState.weatherData)
                }
            }
        }
    }


//---------------------------------------------------------------------------------------------
    companion object {

        fun newInstance() = HistoryFragment()       //  фабричный метод

    }

    override fun onItemClick(weather: Weather) {                // метод который нужно добавить (автоматически) при привязке OnMyItemClickListener

    }
//-----------------------------------------------------------------------------------------------
}