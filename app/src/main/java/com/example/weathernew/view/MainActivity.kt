package com.example.weathernew.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.weathernew.R
import com.example.weathernew.databinding.ActivityMainBinding
import com.example.weathernew.view.main.MAIN_SERVICE_KEY_EXTRAS
import com.example.weathernew.view.main.MainFragment
import com.example.weathernew.view.main.MyService
import com.example.weathernew.view.main.ThreadsFragment

class MainActivity : AppCompatActivity() {


    lateinit var binding: ActivityMainBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if(savedInstanceState==null){
            supportFragmentManager.beginTransaction().replace(R.id.container, MainFragment.newInstance()).commit()
        }
        startService(Intent(this,MyService::class.java).apply {
            putExtra(MAIN_SERVICE_KEY_EXTRAS,"Hello")
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_screen_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return return   when(item.itemId){
            R.id.menu_threads->{
                supportFragmentManager.beginTransaction().add(R.id.container, ThreadsFragment.newInstance()).addToBackStack("").commit()
                true
            }else->{
                super.onOptionsItemSelected(item)
            }
        }

    }
}