package com.example.weathernew.lessons


import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.google.firebase.messaging.FirebaseMessagingService
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.weathernew.R

import com.google.firebase.messaging.RemoteMessage

class MyFCMService : FirebaseMessagingService() {
    override fun onNewToken(s: String) {
        super.onNewToken(s)
        Log.d("mylogs", "token $s")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        val data = message.data.toMap()
        if(data.isNotEmpty()){
            val title =data[KEY_TITLE]
            val message =data[KEY_MESSAGE]
            if(!title.isNullOrBlank()&&!message.isNullOrBlank())
                pushNotification(title,message)
        }
    }


    companion object {
        private const val NOTIFICATION_ID_1 = 1
        private const val CHANNEL_ID_1 = "channel_id_1"
        private const val KEY_TITLE = "myTitle"
        private const val KEY_MESSAGE = "myMessage"
    }
    private fun pushNotification(title:String,message:String) {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationBuilder = NotificationCompat.Builder(this,CHANNEL_ID_1).apply {
            setSmallIcon(R.drawable.ic_kotlin_logo)
            setContentTitle(title)
            setContentText(message)
            priority = NotificationCompat.PRIORITY_MAX
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channelName_1 = "Name $CHANNEL_ID_1"
            val channelDescription_1 = "Description for $CHANNEL_ID_1"
            val channelPriority_1 = NotificationManager.IMPORTANCE_HIGH

            val channel = NotificationChannel(CHANNEL_ID_1,channelName_1,channelPriority_1).apply {
                description = channelDescription_1
            }
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(NOTIFICATION_ID_1,notificationBuilder.build())
    }
}
// Server key   AAATbq9f9Y:APA91bGiHCNkjHt8v3hjDPXKUJL_7-a9-O2lcaHuAj86W1vvGLfQs0HtIbLl39Gw41HUbcbxwlTdqLuqVwpzCqTEcVtDXqVNN7fLUGpqX3IWUmXkdO8v06wEgbWaqPoNTPMMriSYmupA
// Token        fiLyADNhRYqgSWiVx2HWdu:APA91bHtoj8eObhH3P6Xi1PGyOklm4HccRyG2RuSPQNAu-6WEY1JF353xlerC6aQCdJt1nliPw4-fgx6EaOX_ooGM0VirA0QHCJQTgmxR2Rch6QAwDedazq172d4--YEKH4QVw4AM-Bt

/*import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.weathernew.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFCMService : FirebaseMessagingService() {
    override fun onNewToken(s: String) {
        super.onNewToken(s)
        Log.d("maylogs","token $s")
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onMessageReceived(message: RemoteMessage) {
        val data = message.data.toMap()

        if (data.isNotEmpty()){
            val title = data[KEY_TITLE]
            val message = data[KEY_MESSAGE]
            if (!title.isNullOrBlank()&&!message.isNullOrBlank())
            pushNotification(title,message)
        }
    }




    companion object {
        private const val NOTIFICATION_ID_1 = 1
        private const val CHANNEL_ID_1 = "channel_id_1"
        private const val KEY_TITLE = "myTitle"
        private const val KEY_MESSAGE = "myMessage"


    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun  pushNotification(title: String, massage: String) {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID_1).apply {
            setSmallIcon(R.drawable.ic_kotlin_logo)
            setContentTitle(title)
            setContentText(massage)
            priority = NotificationCompat.PRIORITY_HIGH
        }


        //  if (Build.VERSION_CODES.SDK_INT>= Build.VERSION_CODES.O){ }
        val channelName = "Name $CHANNEL_ID_1"
        val channelDescription = "Description for $CHANNEL_ID_1"
        val channelPriority = NotificationManager.IMPORTANCE_HIGH

        val channel = NotificationChannel(CHANNEL_ID_1, channelName, channelPriority).apply {
            description = channelDescription
        }

        notificationManager.createNotificationChannel(channel)

        notificationManager.notify(NOTIFICATION_ID_1, notificationBuilder.build())

    }
}



//fiLyADNhRYqgSWiVx2HWdu:APA91bHtoj8eObhH3P6Xi1PGyOklm4HccRyG2RuSPQNAu-6WEY1JF353xlerC6aQCdJt1nliPw4-fgx6EaOX_ooGM0VirA0QHCJQTgmxR2Rch6QAwDedazq172d4--YEKH4QVw4AM-Bt*/