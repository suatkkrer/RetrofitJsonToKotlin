package com.suatkkrer.retrofitpractice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.suatkkrer.retrofitpractice.api.ApiRequest
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

const val BASE_URL = "https://cat-fact.herokuapp.com/"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        getCurrentData()

        layout_generate_new_fact.setOnClickListener {
            getCurrentData()
        }


    }

    fun getCurrentData() {
        val api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiRequest::class.java)

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = api.getCatFacts().awaitResponse()
                if (response.isSuccessful) {

                    val data = response.body()!!
                    Log.d("TAG", data.toString())

                    withContext(Dispatchers.Main) {
                        tv_textView.visibility = View.VISIBLE
                        tv_timeStamp.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        tv_timeStamp.text = data.createdAt
                        tv_textView.text = data.text

                    }

                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        applicationContext,
                        "Seems like something went wrong...",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}