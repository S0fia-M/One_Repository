package com.example.api

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class MainActivity : AppCompatActivity() {
    //Указываем адрес для подключения к API
    val URL = "https://api.icndb.com/jokes/random"
    var okHttpClient: OkHttpClient = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nextBtn.setOnClickListener {
            //Пока идет загрузка "шутки", показывается progressBar
            runOnUiThread {
                progressBar.visibility = View.VISIBLE
            }
            //Отправляем запрос по URL
            val request: Request = Request.Builder().url(URL).build()
            okHttpClient.newCall(request).enqueue(object : Callback{
                //Функция сработает при ошибке в момент запроса
                override fun onFailure(call: Call?, e: IOException?) {
                    Log.e("Error", e.toString())
                }
                //Функция сработает если нет ошибок
                override fun onResponse(call: Call?, response: Response?) {
                    //Получение шутки из JSON по ключам value -> joke
                    val joke = (JSONObject(response!!.body()!!
                        .string()).getJSONObject("value").get("joke")).toString()
                    //После загрузки "шутки", убираем progressBar и выводим "шутку" на экран
                    runOnUiThread {
                        progressBar.visibility = View.GONE
                        jock.text = joke
                    }
                }
            })
        }
    }
}
