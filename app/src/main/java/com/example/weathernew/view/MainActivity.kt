package com.example.weathernew.view


import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager.getDefaultSharedPreferences
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.weathernew.R
import com.example.weathernew.R.drawable.*
import com.example.weathernew.databinding.ActivityMainBinding
import com.example.weathernew.lessons.MyBroadcastReceiver
import com.example.weathernew.lessons.ThreadsFragment
import com.example.weathernew.model.WeatherDTO
import com.example.weathernew.room.App
import com.example.weathernew.utils.BUNDLE_KEY
import com.example.weathernew.utils.BUNDLE_KEY_WEATHER
import com.example.weathernew.utils.KEY_CURRENT_CITIES
import com.example.weathernew.utils.KEY_SP
import com.example.weathernew.view.details.DetailsFragment
import com.example.weathernew.view.history.HistoryFragment
import com.example.weathernew.view.main.MainFragment
import com.example.weathernew.view.main.isRussian

import com.google.android.material.floatingactionbutton.FloatingActionButton

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_main.view.*
import kotlinx.android.synthetic.main.fragment_main.view.mainFragmentFAB




class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding

    private val receiver =  MyBroadcastReceiver()

    private lateinit var pref : SharedPreferences



    @RequiresApi(Build.VERSION_CODES.P)
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


       // getSharedPreferences(KEY_SP,Context.MODE_PRIVATE).also { isRussian = true }
          //  .getBoolean(KEY_CURRENT_CITIES,true)


      //  fab()

      /*  fun  fab(){
            var nameFAB: FloatingActionButton? = mainFragmentFAB
            var noNullFAB:FloatingActionButton
            var nullFAB:FloatingActionButton?

            if (nameFAB !=null) {
                noNullFAB = nameFAB

            }else{
                    nullFAB  = nameFAB
                }

        }*/



        /*    pref = getSharedPreferences(KEY_SP,Context.MODE_PRIVATE)
        binding .mainFragmentFAB.setImageResource(pref.getBoolean(KEY_CURRENT_CITIES,true))
        mainFragmentFAB.setOnClickListener{
            binding .mainFragmentFAB.setImageResource(R.drawable.ic_russia)
        }
        pref.edit()
            .putBoolean(KEY_SP,true)
            .putBoolean(KEY_SP,false)
            .apply()*/

        //isRussian = getSharedPreferences.getBoolean(KEY_SP,Context.MODE_PRIVATE)

    }

   /* private fun  fab(){
        mainFragmentFAB?.let {
            pref = getSharedPreferences(KEY_SP,Context.MODE_PRIVATE)
          when(isRussian){
              is
          }


            val editor = pref.edit()
                editor.putBoolean(KEY_CURRENT_CITIES,true)
                editor.putBoolean(KEY_CURRENT_CITIES,false)
                editor.apply()

        }
    }*/





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
            R.id.menu_threads-> {
                supportFragmentManager.beginTransaction()
                    .add(R.id.container, ThreadsFragment.newInstance()).addToBackStack("").commit()
                true
            } R.id.menu_history->{
                    supportFragmentManager.beginTransaction().add(R.id.container, HistoryFragment.newInstance()).addToBackStack("").commit()
                    true
            }else->{
                super.onOptionsItemSelected(item)
            }
        }

    }
}

private fun ImageView.setImageResource(boolean: Boolean) {

}
