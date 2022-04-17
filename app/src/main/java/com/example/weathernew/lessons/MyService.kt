package com.example.weathernew.lessons

import android.app.IntentService
import android.content.Intent
import android.util.Log

const val MAIN_SERVICE_KEY_EXTRAS = "key"


class MyService(name:String = ""): IntentService(name) {

    private val TAG = "mayLogs"

    private fun createLogMessage(message: String){
        Log.d(TAG,message)
    }

    override fun onHandleIntent(intent: Intent?) {
        createLogMessage (" onHandleIntent ${intent?.getStringExtra(MAIN_SERVICE_KEY_EXTRAS)}")
    }

    override fun onCreate() {
        super.onCreate()
        createLogMessage("unCreate")
    }

    override fun onDestroy() {
        super.onDestroy()
        createLogMessage("onDestroy")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createLogMessage("onStartCommand ${flags}")
        //IntentService.START_STICKY_COMPATIBILITY
        return super.onStartCommand(intent, flags, startId)
    }

}