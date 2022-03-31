package com.example.weathernew.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.weathernew.R
import com.example.weathernew.databinding.ActivityMainBinding
import com.example.weathernew.view.main.MainFragment

class MainActivity : AppCompatActivity() {


    lateinit var binding: ActivityMainBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if(savedInstanceState==null){
            supportFragmentManager.beginTransaction().replace(R.id.container, MainFragment.newInstance()).commit()
        }
    }
}