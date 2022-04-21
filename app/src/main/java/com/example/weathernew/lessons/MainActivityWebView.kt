package com.example.weathernew.lessons


import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.weathernew.databinding.ActivityMainWebviewBinding
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

class MainActivityWebView : AppCompatActivity() {


    private lateinit var binding: ActivityMainWebviewBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainWebviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonOk.setOnClickListener {
            request(binding.editTextUrl.text.toString())
        }
    }

    private fun request(urlString: String) {

        val handlerMainUI = Handler(mainLooper)
        val handlerCurrent = Handler(Looper.myLooper()!!)

        Thread{
            try {
                val url = URL(urlString)
                val httpsURLConnection = (url.openConnection() as HttpsURLConnection).apply {
                    requestMethod = "GET"
                    readTimeout = 2000
                }

                val bufferedReader = BufferedReader(InputStreamReader(httpsURLConnection.inputStream))
                val result = convertBufferToResult(bufferedReader)

                runOnUiThread {
                    binding.webView.loadDataWithBaseURL(null ,result, "text/html; charset=utf-8", "utf-8",null)
                }

                handlerMainUI.post {
                    binding.webView.loadDataWithBaseURL(null ,result, "text/html; charset=utf-8", "utf-8",null)
                }

                httpsURLConnection.disconnect()  // FIXME httpsURLConnection.disconnect() не работает в файнали, надо что то сделать.
            }catch (e:Exception){
                e.printStackTrace()

            }finally{

            }
        }.start()


    }

    private  fun  convertBufferToResult(bufferedReader:BufferedReader):String{
        return bufferedReader.lines().collect(Collectors.joining("\n"))
    }
}