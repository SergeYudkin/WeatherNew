package com.example.weathernew.view.main

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
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
import com.example.weathernew.utils.*
import com.example.weathernew.view.details.DetailsFragment
import com.example.weathernew.viewmodel.AppState
import com.example.weathernew.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar
import org.xml.sax.Locator

var isRussian = true

class MainFragment : Fragment(),OnMyItemClickListener {      // привязали интерфейс OnMyItemClickListener который даст MainFragment способность принимать клики и реагировать на метод onItemClick


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
            mainFragmentFABLocation.setOnClickListener {
                checkPermission()
            }

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


    private fun checkPermission(){         //функция проверки и запроса разрешения

        context?.let {
            when{
                ContextCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION)    // проверяем наличие разрешения
                        == PackageManager.PERMISSION_GRANTED->{
                    getLocation()
                }
                shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)->{    // выводим диалоговое окно с обьяснением, почему необходимо предоставить доступ,
                    showDialogRatio()                                                              // если да, то выводится системное диалоговое окно с запросом разрешения
                                                                            // если пользователь отклонил второй запрос на разоешение, запросов больше не будет, придётся переустанавливать приложение
                }else->{
                myRequestPermission()                          //  выводим пользователю системное диалоговое окно запроса разрешения
            }
            }

        }

    }

    private fun getAddress(location: Location){



        Thread{
            val geocoder = Geocoder(requireContext())
           val listAddress =  geocoder.getFromLocation(location.latitude, location.longitude,1)
        }.start()


    }

    private val locationListener = object : LocationListener{

        override fun onLocationChanged(location: Location) {
            getAddress(location)
        }

        override fun onProviderDisabled(provider: String) {
            super.onProviderDisabled(provider)
        }

        override fun onProviderEnabled(provider: String) {
            super.onProviderEnabled(provider)
        }

    }


    private fun getLocation(){

        activity?.let {
            if (ContextCompat.checkSelfPermission(it,Manifest.permission.ACCESS_FINE_LOCATION)
            ==PackageManager.PERMISSION_GRANTED){
                val locationManager = it.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                    val providerGPS = locationManager.getProvider(LocationManager.GPS_PROVIDER)
                    providerGPS?.let {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        REFRESH_PERIOD,
                        MIN_DISTANCE,
                        locationListener)
                    }
                }else{
                    val lastLocation =  locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                    lastLocation?.let {
                        getAddress(it)
                    }
            }
            }
        }
    }

    private fun showDialog(){

    }

    private fun myRequestPermission(){

        requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE_FINE_LOCATION)    //Системный диалог запроса разрешения
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,                                       // обработка запросов и ответов
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        // super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_FINE_LOCATION){
            when {
                (grantResults[0] == PackageManager.PERMISSION_GRANTED) -> {       // если да, то getContacts()
                    getLocation()
                }
                shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {   // если нет, то наше диалоговое окно с обьяснением, почему необходимо предоставить доступ
                    showDialogRatio()

                }
                else -> {
                    // сюда попадаем в случае второго отказа в разрешении, это конец запросов больше не будет.
                }
            }

        }
    }

    private fun showDialogRatio(){
        // наше диалоговое окно с обьяснением, почему необходимо предоставить доступ
        AlertDialog.Builder(requireContext())
            .setTitle("Доступ к геолокации") //TODO HW вынести в ресурсы
            .setMessage("Обьяснение")
            .setPositiveButton("Предоставить доступ"){_,_->
                myRequestPermission()
            }
            .setNegativeButton("Не стоит"){dialog,_->dialog.dismiss()}
            .create()
            .show()
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
}




