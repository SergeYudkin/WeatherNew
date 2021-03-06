package com.example.weathernew.lessons


import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.TextView
import com.example.weathernew.databinding.FragmentThreadsBinding
import com.example.weathernew.view.BaseFragment


class ThreadsFragment : BaseFragment<FragmentThreadsBinding>(FragmentThreadsBinding:: inflate) {

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
    }

    private val myThread = MyThread()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        myThread.start()
        binding.button.setOnClickListener {
            myThread.handler?.post {
                val result = startCalculations(3)
                //activity?.let{ activity->
                Handler(Looper.getMainLooper()).post {
                    Log.d("mylogs","происходит утечка памяти в якобы фрагменте с binding $binding")
                    binding.mainContainer.addView(TextView(it.context).apply {
                        text = result
                    })
                }

            }
        }
    }

    class MyThread:Thread(){
        var handler: Handler?= null
        override fun run() {
            Looper.prepare()
            handler = Handler(Looper.myLooper()!!)
            Looper.loop()
        }
    }

    private fun startCalculations(seconds: Int): String {
        Thread.sleep(seconds*1000L)
        return "${seconds.toString()} ${Thread.currentThread().name}"
    }


    override fun onDestroy() {
        super.onDestroy()
        //myThread.handler?.removeCallbacksAndMessages(null)
        //_binding = null
    }

    companion object {
        fun newInstance() = ThreadsFragment()
    }
}