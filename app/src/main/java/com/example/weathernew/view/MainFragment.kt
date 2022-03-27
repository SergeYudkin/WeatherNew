package com.example.weathernew.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.weathernew.R
import com.example.weathernew.databinding.FragmentMainBinding
import com.example.weathernew.viewmodel.MainViewModel

class MainFragment : Fragment() {

//------------------------------------------------------------------------------------
     var _binding : FragmentMainBinding? = null
      private val binding : FragmentMainBinding     // binding не null
    get(){
        return _binding!!
    }
//-------------------------------------------------------------------------------------

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
//--------------------------------------------------------------------------------------

    private lateinit var viewModel : MainViewModel    //  ссылка на  MainViewModel


//---------------------------------------------------------------------------------------

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)                                                   // инициализация viewModel
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)                       // ViewModelProvider следит что бы каждая viewModel существовала в единственном экземпляре
         viewModel.getLivaData().observe(viewLifecycleOwner, Observer <Any> { renderData(it) })        // ViewModel автоматически воспринимает жизненный цикл и обрабатывает сохранение и восстановление данных
        viewModel.getWeatherFromServer()
    }

    fun renderData(data:Any){
        Toast.makeText(requireContext(),"Работает", Toast.LENGTH_SHORT).show()
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
//-----------------------------------------------------------------------------------------------
}