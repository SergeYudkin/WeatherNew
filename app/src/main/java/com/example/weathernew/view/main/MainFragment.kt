package com.example.weathernew.view.main

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.weathernew.R
import com.example.weathernew.databinding.FragmentMainBinding
import com.example.weathernew.model.Weather
import com.example.weathernew.utils.BUNDLE_KEY
import com.example.weathernew.view.details.DetailsFragment
import com.example.weathernew.viewmodel.AppState
import com.example.weathernew.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar

var isRussian = true

class MainFragment : Fragment(),OnMyItemClickListener {      // привязали интерфейс OnMyItemClickListener который даст MainFragment способность принимать клики и реагировать на метод onItemClick




    //------------------------------------------------------------------------------------
private var _binding : FragmentMainBinding? = null     // привязываем макет
      private val binding : FragmentMainBinding     // binding не null
    get(){
        return _binding!!
    }
//-------------------------------------------------------------------------------------


    private val adapter: CitiesAdapter by lazy {          // вызвали адаптер
        CitiesAdapter(this)
    }

//--------------------------------------------------------------------------------------




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

     /*   private fun checkPermission(){         //функция проверки и запроса разрешения

            context?.let {
                when{
                    ContextCompat.checkSelfPermission(it, Manifest.permission.READ_CONTACTS)    // проверяем наличие разрешения
                            == PackageManager.PERMISSION_GRANTED->{
                        getLocation()
                    }
                    shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)->{    // выводим диалоговое окно с обьяснением, почему необходимо предоставить доступ,
                        showDialog()                                                              // если да, то выводится системное диалоговое окно с запросом разрешения
                        // если пользователь отклонил второй запрос на разоешение, запросов больше не будет, придётся переустанавливать приложение
                    }else->{
                    myRequestPermission()       //  выводим пользователю системное диалоговое окно запроса разрешения
                }
                }

            }

        }*/

         fun getLocation(){

        }

        isRussian = requireActivity().getSharedPreferences("sp",Context.MODE_PRIVATE)
            .getBoolean("isRussian",true)
                                                                                // чтение из SharedPreferences
        if (isRussian) {
            viewModel.getWeatherFromLocalStorageRus()
            binding.mainFragmentFAB.setImageResource(R.drawable.ic_russia)



        } else {
            viewModel.getWeatherFromLocalStorageWorld()
            binding.mainFragmentFAB.setImageResource(R.drawable.ic_earth)
        }


    }



    //-----------------------------------------------------------------------------------------------------
    private fun sentRequest() {
    isRussian = !isRussian

        val shPref = requireActivity().getSharedPreferences("sp",Context.MODE_PRIVATE)
        val editor = shPref.edit()                                                              // запись в SharedPreferences
        editor.putBoolean("isRussian", isRussian)
        editor.apply()

        if (isRussian) {
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
                is AppState.SuccessDetails -> {
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




