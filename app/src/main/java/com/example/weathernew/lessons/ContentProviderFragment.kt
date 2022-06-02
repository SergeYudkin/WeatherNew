package com.example.weathernew.lessons

import android.Manifest
import android.app.AlertDialog
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.example.weathernew.R
import com.example.weathernew.databinding.FragmentContentProviderBinding
import com.example.weathernew.databinding.FragmentMainBinding

import com.example.weathernew.utils.REQUEST_CODE_CALL
import com.example.weathernew.utils.REQUEST_CODE_CONT
import com.example.weathernew.view.BaseFragment
import com.google.android.material.snackbar.Snackbar


class ContentProviderFragment : BaseFragment<FragmentContentProviderBinding>(FragmentContentProviderBinding:: inflate) {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

        context?.let { it ->

            val contentResolver = it.contentResolver    // Получаем ContentResolver у контекста

            val cursor = contentResolver.query(               // Отправляем запрос на получение контактов и получаем ответ в виде Cursor
                ContactsContract.Contacts.CONTENT_URI,
                null,
                null,
                null,
                ContactsContract.Contacts.DISPLAY_NAME + " ASC"   // сортировка контактов по имени

            )
            cursor?.let { cursor->             // курсор должен двигается по таблице в базе строчка за строчкой

                for (i in 0 until  cursor.count){     // пробегаем курсором по всем строчкам
                    cursor.moveToPosition(i)
                    val name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                    val contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                    val number = getNumberFromID(contentResolver,contactId)
                    addView(name, number)
                }
            }
            cursor?.close()

        }

    }


    private fun getNumberFromID(contentResolver: ContentResolver,contactId:String):String{
        val phones = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null
        )
        var number: String = "none"
        phones?.let { cursor ->
            while (cursor.moveToNext()) {
                number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
            }
        }
        return number

    }

    private fun addView(name: String,number:String){

        binding.containerForContacts.addView(TextView(requireContext()).apply {

            text = "$name:$number"
            textSize = 20f
            setOnClickListener{
                numberCurrent =  number
                makeCall()
            }
        })

    }

    private var numberCurrent: String = "none"
    private fun makeCall() {
        if(ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CALL_PHONE
            ) == PackageManager.PERMISSION_GRANTED){
            val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$numberCurrent"))
            startActivity(intent)
        }else{
            requestPermissions(arrayOf(Manifest.permission.CALL_PHONE), REQUEST_CODE_CALL)
        }
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
        if (requestCode == REQUEST_CODE_CONT){
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
        }else if(requestCode == REQUEST_CODE_CALL){
            when{
                (grantResults[0]== PackageManager.PERMISSION_GRANTED)->{
                    makeCall()
                }
                shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)->{
                    showDialog()
                }else->{
                makeCall()
                }
            }
        }
    }

    private fun myRequestPermission(){

        requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS), REQUEST_CODE_CONT)    //Системный диалог запроса разрешения
    }

    companion object {

        @JvmStatic
        fun newInstance() = ContentProviderFragment()


    }


}