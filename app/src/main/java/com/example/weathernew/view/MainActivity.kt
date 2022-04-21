package com.example.weathernew.view


import android.content.Context
import android.os.Bundle
import android.preference.PreferenceManager.getDefaultSharedPreferences
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.weathernew.R
import com.example.weathernew.databinding.ActivityMainBinding
import com.example.weathernew.lessons.MyBroadcastReceiver
import com.example.weathernew.lessons.ThreadsFragment
import com.example.weathernew.model.WeatherDTO
import com.example.weathernew.utils.BUNDLE_KEY
import com.example.weathernew.utils.BUNDLE_KEY_WEATHER
import com.example.weathernew.view.details.DetailsFragment
import com.example.weathernew.view.main.MainFragment


class MainActivity : AppCompatActivity() {


    lateinit var binding: ActivityMainBinding

    private val receiver =  MyBroadcastReceiver()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(intent.getParcelableExtra<WeatherDTO>(BUNDLE_KEY_WEATHER)!=null){
        supportFragmentManager.beginTransaction().add(R.id.container,DetailsFragment.newInstance(
            Bundle().apply {
                putParcelable(BUNDLE_KEY,intent.getParcelableExtra<WeatherDTO>(BUNDLE_KEY_WEATHER))
            }
        )).addToBackStack("").commit()
        }


        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance()).commit()
        }

        val sp = getSharedPreferences("TAG",Context.MODE_PRIVATE) // классический вызов

        val activity = getPreferences(Context.MODE_PRIVATE)  // на уровне активити

        val app = getDefaultSharedPreferences(this)   // на уровне приложения

        app.getString("key","")

        app.edit().putString("key","value").apply()


        val editor  = app.edit()
        editor.putString("key1","value1")
        editor.putString("key2","value2")
        editor.putBoolean("key3",true)
        editor.apply()


    }



    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_screen_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return  when(item.itemId){
            R.id.menu_threads->{
                supportFragmentManager.beginTransaction().add(R.id.container, ThreadsFragment.newInstance()).addToBackStack("").commit()
                true
            }else->{
                super.onOptionsItemSelected(item)
            }
        }

    }
}