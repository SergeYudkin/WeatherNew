package com.example.weathernew.lessons

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.weathernew.R
import com.example.weathernew.databinding.FragmentContentProviderBinding
import com.example.weathernew.databinding.FragmentMainBinding


class ContentProviderFragment : Fragment() {

    private var _binding : FragmentContentProviderBinding? = null     // привязываем макет
    private val binding : FragmentContentProviderBinding                 // binding не null
        get(){
            return _binding!!
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding =  FragmentContentProviderBinding.inflate(inflater, container, false)
        return binding.root
    }

    fun getContacts(){

    }

    companion object {

        @JvmStatic
        fun newInstance() = ContentProviderFragment()


    }
}