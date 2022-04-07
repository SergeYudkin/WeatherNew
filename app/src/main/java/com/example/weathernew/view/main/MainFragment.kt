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


    private val adapter: MainFragmentAdapter by lazy {          // вызвали адаптер
        MainFragmentAdapter(this)
    }

//--------------------------------------------------------------------------------------

    private var isRussian = true

 //--------------------------------------------------------------------------------------
    override fun  onDestroy() {
        super.onDestroy()
        _binding = null
    }
//--------------------------------------------------------------------------------------

    private  val viewModel : MainViewModel by lazy {                        //  ссылка на  MainViewModel
        ViewModelProvider(this).get(MainViewModel::class.java)
    }



//---------------------------------------------------------------------------------------

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {                                 // Observer записывает в renderData все изменения
        super.onViewCreated(view, savedInstanceState)                                                   // инициализация viewModel
        initView()                                                                                     // ViewModelProvider следит что бы каждая viewModel существовала в единственном экземпляре
         viewModel.getLivaData().observe(viewLifecycleOwner, Observer <AppState> { renderData(it) })    // ViewModel автоматически воспринимает жизненный цикл и обрабатывает сохранение и восстановление данных
        viewModel.getWeatherFromLocalStorageRus()

    }

    private fun initView() {
        with(binding){
            mainFragmentRecyclerView.adapter = adapter                 // к RecyclerView подключаем адаптер
            mainFragmentFAB.setOnClickListener {
                sentRequest()
            }
        }

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
        with(binding){
            when(appState){
                is AppState.Error -> {
                    mainFragmentLoadingLayout.visibility = View.GONE
                    root.actionError(getString(R.string.error),Snackbar.LENGTH_LONG)
                }
                is AppState.Loading ->{
                    mainFragmentLoadingLayout.visibility = View.VISIBLE
                }
                is AppState.Success -> {
                    mainFragmentLoadingLayout.visibility = View.GONE

                    adapter.setWeather(appState.weatherData)            // в случае успеха подгружаем адаптер

                    root.showSnackBarWithoutAction(getString(R.string.success),Snackbar.LENGTH_LONG)
                }
            }
        }
    }

    private fun View.actionError(text:String,length:Int){
        Snackbar.make(this,text ,length )
            .setAction(getString(R.string.again)) {
                sentRequest()
            }.show()
    }


    private fun View.showSnackBarWithoutAction(text:String,length:Int){
        Snackbar.make(this,text,length).show()
    }
//-----------------------------------------------------------------------------------------

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =  FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }
//---------------------------------------------------------------------------------------------
    companion object {

        fun newInstance() = MainFragment()       //  фабричный метод


    }

    override fun onItemClick(weather: Weather) {                // метод который нужно добавить (автоматически) при привязке OnMyItemClickListener
        activity?.run {
            supportFragmentManager.beginTransaction()
                .add(R.id.container, DetailsFragment.newInstance(
                    Bundle().apply {
                        putParcelable(BUNDLE_KEY, weather)                      // пакует данные в бандл
                    }
                )).addToBackStack("").commit()
        }
    }
//-----------------------------------------------------------------------------------------------
}