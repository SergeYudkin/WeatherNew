package com.example.weathernew.lessons

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.weathernew.R
import com.example.weathernew.databinding.FragmentContentProviderBinding
import com.example.weathernew.databinding.FragmentMainBinding
import com.example.weathernew.utils.REQUEST_CODE


class ContentProviderFragment : Fragment() {

    private var _binding : FragmentContentProviderBinding? = null     // привязываем макет
    private val binding : FragmentContentProviderBinding                 // binding не null
        get(){
            return _binding!!
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding =  FragmentContentProviderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermission()
    }

    private fun checkPermission(){         //функция проверки и запроса разрешения

        context?.let {
            when{
                ContextCompat.checkSelfPermission(it,Manifest.permission.READ_CONTACTS)    // проверяем наличие разрешения
                        ==PackageManager.PERMISSION_GRANTED->{
                    getContacts()
                        }
                shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)->{    // выводим диалоговое окно с обьяснением, почему необходимо предоставить доступ,
                    showDialog()                                                              // если да, то выводится системное диалоговое окно с запросом разрешения
                                                                                                // если пользователь отклонил второй запрос на разоешение, запросов больше не будет, придётся переустанавливать приложение
                }else->{
                myRequestPermission()       //  выводим пользователю системное диалоговое окно запроса разрешения
                }
            }

        }

    }

    private fun getContacts(){

    }

    private fun showDialog(){
                                                        // наше диалоговое окно с обьяснением, почему необходимо предоставить доступ
        AlertDialog.Builder(requireContext())
            .setTitle("Доступ к контактам")
            .setMessage("Обьяснение")
            .setPositiveButton("Предоставить доступ"){_,_->
                myRequestPermission()
            }
            .setNegativeButton("Не стоит"){dialog,_->dialog.dismiss()}
            .create()
            .show()



    }

    override fun onRequestPermissionsResult(
        requestCode: Int,                                       // обработка запросов и ответов
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
       // super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE){
            when{
                (grantResults[0]==PackageManager.PERMISSION_GRANTED)->{       // если да, то getContacts()
                    getContacts()
                }
                shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)->{   // если нет, то наше диалоговое окно с обьяснением, почему необходимо предоставить доступ
                    showDialog()

                }else->{
                                                    // сюда попадаем в случае второго отказа в разрешении, это конец запросов больше не будет.
                }
            }
        }
    }

    private fun myRequestPermission(){

        requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS),REQUEST_CODE)    //Системный диалог запроса разрешения
    }

    companion object {

        @JvmStatic
        fun newInstance() = ContentProviderFragment()


    }
}