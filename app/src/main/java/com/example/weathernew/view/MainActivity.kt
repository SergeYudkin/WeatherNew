package com.example.weathernew.view


import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Build.VERSION.SDK_INT

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.example.weathernew.R
import com.example.weathernew.databinding.ActivityMainBinding
import com.example.weathernew.databinding.FragmentContentProviderBinding
import com.example.weathernew.lessons.ContentProviderFragment
import com.example.weathernew.lessons.MapsFragment
import com.example.weathernew.lessons.MyBroadcastReceiver
import com.example.weathernew.lessons.ThreadsFragment
import com.example.weathernew.model.WeatherDTO
import com.example.weathernew.utils.BUNDLE_KEY
import com.example.weathernew.utils.BUNDLE_KEY_WEATHER
import com.example.weathernew.view.details.DetailsFragment
import com.example.weathernew.view.history.HistoryFragment
import com.example.weathernew.view.main.MainFragment
import com.google.android.gms.maps.MapFragment


class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding

    private val receiver =  MyBroadcastReceiver()


    companion object {
        private const val NOTIFICATION_ID_1 = 1
        private const val NOTIFICATION_ID_2 = 2
        private const val CHANNEL_ID_1 = "channel_id_1"
        private const val CHANNEL_ID_2 = "channel_id_2"
    }




    @RequiresApi(Build.VERSION_CODES.O)
    private fun  pushNotification(){
        val notificationManager =
        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationBuilder_1 = NotificationCompat.Builder(this, CHANNEL_ID_1).apply {
            setSmallIcon(R.drawable.ic_kotlin_logo)
            setContentTitle("Заголовок для $CHANNEL_ID_1")
            setContentText("Сообщение для $CHANNEL_ID_1")
            priority = NotificationCompat.PRIORITY_MAX
        }

        val notificationBuilder_2 = NotificationCompat.Builder(this, CHANNEL_ID_2).apply {
            setSmallIcon(R.drawable.ic_kotlin_logo)
            setContentTitle("Заголовок для $CHANNEL_ID_2")
            setContentText("Сообщение для $CHANNEL_ID_2")
            priority = NotificationCompat.PRIORITY_MAX
        }

      //  if (Build.VERSION_CODES.SDK_INT>= Build.VERSION_CODES.O){ }
        val channelName_1 = "Name $CHANNEL_ID_1"
        val channelDescription_1 = "Description for $CHANNEL_ID_1"
        val channelPriority_1 = NotificationManager.IMPORTANCE_HIGH

        val channel_1 = NotificationChannel(CHANNEL_ID_1,channelName_1,channelPriority_1).apply {
            description = channelDescription_1
        }

        notificationManager.createNotificationChannel(channel_1)

        notificationManager.notify(NOTIFICATION_ID_1,notificationBuilder_1.build())

        val channelName_2 = "Name $CHANNEL_ID_2"
        val channelDescription_2 = "Description for $CHANNEL_ID_2"
        val channelPriority_2 = NotificationManager.IMPORTANCE_DEFAULT

        val channel_2 = NotificationChannel(CHANNEL_ID_2,channelName_2,channelPriority_2).apply {
            description = channelDescription_2
        }

        notificationManager.createNotificationChannel(channel_2)

        notificationManager.notify(NOTIFICATION_ID_2,notificationBuilder_2.build())
        notificationManager.notify(NOTIFICATION_ID_2+1,notificationBuilder_2.build())
        notificationManager.notify(NOTIFICATION_ID_2+2,notificationBuilder_2.build())
        notificationManager.notify(NOTIFICATION_ID_2+3,notificationBuilder_2.build())
        notificationManager.notify(NOTIFICATION_ID_2+4,notificationBuilder_2.build())

    }




    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pushNotification()


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


    }

    override fun onDestroy() {
        super.onDestroy()
       // unregisterReceiver(receiver)
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
            } R.id.menu_history-> {
                supportFragmentManager.beginTransaction()
                    .add(R.id.container, HistoryFragment.newInstance()).addToBackStack("").commit()
                true
            }
                R.id.menu_content-> {
                    supportFragmentManager.beginTransaction()
                        .add(R.id.container, ContentProviderFragment.newInstance())
                        .addToBackStack("").commit()
                    true
                }
                    R.id.menu_google_maps->{
                        supportFragmentManager.beginTransaction()
                            .add(R.id.container, MapsFragment()).addToBackStack("").commit()
                        true
            }else->{
                super.onOptionsItemSelected(item)
            }
        }

    }
}


