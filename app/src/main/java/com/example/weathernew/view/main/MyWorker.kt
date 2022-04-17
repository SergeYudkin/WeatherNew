package com.example.weathernew.view.main

import android.content.Context
import android.service.media.MediaBrowserService
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class MyWorker(context:Context,workerParameters: WorkerParameters): Worker(context,workerParameters) {
    override fun doWork(): Result {
        Log.d("myLogs","doWork")
        return Result.success()
    }


}