package com.example.weathernew.lessons

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class MyWorker(context:Context,workerParameters: WorkerParameters): Worker(context,workerParameters) {
    override fun doWork(): Result {
        Log.d("myLogs","doWork")
        return Result.success()
    }


}