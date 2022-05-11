package com.example.weathernew.view.history

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.weathernew.databinding.FragmentHistoryBinding
import com.example.weathernew.model.Weather
import com.example.weathernew.view.main.OnMyItemClickListener
import com.example.weathernew.viewmodel.AppState
import com.example.weathernew.viewmodel.HistoryViewModel

class HistoryFragment : Fragment(), OnMyItemClickListener {      // привязали интерфейс OnMyItemClickListener который даст MainFragment способность принимать клики и реагировать на метод onItemClick

//------------------------------------------------------------------------------------
private var _binding : FragmentHistoryBinding? = null     // привязываем макет
      private val binding : FragmentHistoryBinding     // binding не null
    get(){
        return _binding!!
    }
//-------------------------------------------------------------------------------------


    private val adapter: CitiesHistoryAdapter by lazy {          // вызвали адаптер
        CitiesHistoryAdapter(this)
    }

 //--------------------------------------------------------------------------------------
    override fun  onDestroy() {
        super.onDestroy()
        _binding = null
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
                    //TODO HW
                }
                is AppState.Loading -> {}
                is AppState.SuccessCity -> {}
                is AppState.SuccessDetails -> {
                    adapter.setWeather(appState.weatherData)
                }
            }
        }
    }


//-----------------------------------------------------------------------------------------

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =  FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }
//---------------------------------------------------------------------------------------------
    companion object {

        fun newInstance() = HistoryFragment()       //  фабричный метод


    }

    override fun onItemClick(weather: Weather) {                // метод который нужно добавить (автоматически) при привязке OnMyItemClickListener

    }
//-----------------------------------------------------------------------------------------------
}