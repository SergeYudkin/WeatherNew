package com.example.weathernew.view.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.weathernew.R
import com.example.weathernew.databinding.FragmentMainBinding
import com.example.weathernew.model.Weather
import com.example.weathernew.view.details.BUNDLE_KEY
import com.example.weathernew.view.details.DetailsFragment
import com.example.weathernew.viewmodel.AppState
import com.example.weathernew.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar

class MainFragment : Fragment(),OnMyItemClickListener {      // привязали интерфейс OnMyItemClickListener который даст MainFragment способность принимать клики и реагировать на метод onItemClick

//------------------------------------------------------------------------------------
private var _binding : FragmentMainBinding? = null     // привязываем макет
      private val binding : FragmentMainBinding     // binding не null
    get(){
        return _binding!!
    }
//-------------------------------------------------------------------------------------


    private val adapter = MainFragmentAdapter(this)      // вызвали адаптер

//--------------------------------------------------------------------------------------

    private var isRussian = true

 //--------------------------------------------------------------------------------------
    override fun  onDestroy() {
        super.onDestroy()
        _binding = null
    }
//--------------------------------------------------------------------------------------

    private lateinit var viewModel : MainViewModel    //  ссылка на  MainViewModel



//---------------------------------------------------------------------------------------

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {                                 // Observer записывает в renderData все изменения
        super.onViewCreated(view, savedInstanceState)                                                   // инициализация viewModel
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)                       // ViewModelProvider следит что бы каждая viewModel существовала в единственном экземпляре
         viewModel.getLivaData().observe(viewLifecycleOwner, Observer <AppState> { renderData(it) })    // ViewModel автоматически воспринимает жизненный цикл и обрабатывает сохранение и восстановление данных

        binding.mainFragmentRecyclerView.adapter = adapter        // к RecyclerView подключаем адаптер
        binding.mainFragmentFAB.setOnClickListener{
            sentRequest()
        }
        viewModel.getWeatherFromLocalStorageRus()

    }
//-----------------------------------------------------------------------------------------------------
    private fun sentRequest() {
    isRussian = !isRussian            // меняем на противоположное (если false то true, если true то false)
        if (isRussian) {                                                // преключения между Российскими городами и миром
            viewModel.getWeatherFromLocalStorageRus()
            binding.mainFragmentFAB.setImageResource(R.drawable.ic_russia)
        } else {
            viewModel.getWeatherFromLocalStorageWorld()
            binding.mainFragmentFAB.setImageResource(R.drawable.ic_earth)
        }

    }

//----------------------------------------------------------------------------------------------

    private fun renderData(appState: AppState){
        when(appState){
            is AppState.Error -> {
                binding.mainFragmentLoadingLayout.visibility = View.GONE
                Snackbar.make(binding.root,"Ошибка",Snackbar.LENGTH_LONG).setAction("Попробовать ещё раз"){
                    sentRequest()
                }.show()
            }
            is AppState.Loading ->{
                binding.mainFragmentLoadingLayout.visibility = View.VISIBLE
            }
            is AppState.Success -> {
                binding.mainFragmentLoadingLayout.visibility = View.GONE

                adapter.setWeather(appState.weatherData)            // в случае успеха подгружаем адаптер

                Snackbar.make(binding.root,"Успех",Snackbar.LENGTH_LONG).show()
            }
        }

    }
//-----------------------------------------------------------------------------------------

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =  FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }
//---------------------------------------------------------------------------------------------
    companion object {

        fun newInstance() = MainFragment()       //  фабричный метод


    }

    override fun onItemClick(weather: Weather) {     // метод который нужно добавить (автоматически) при привязке OnMyItemClickListener
        val bundle = Bundle()
        bundle.putParcelable(BUNDLE_KEY,weather)
        requireActivity().supportFragmentManager.beginTransaction().add(
            R.id.container,DetailsFragment.newInstance(bundle)
        ).addToBackStack("").commit()
    }
//-----------------------------------------------------------------------------------------------
}