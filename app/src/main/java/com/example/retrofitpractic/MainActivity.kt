package com.example.retrofitpractic

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.retrofitpractic.utils.RetrofitInstance
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpException
import com.squareup.picasso.Picasso
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException


class MainActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var imageView: ImageView
    private lateinit var downLoadBT: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        init()
        downLoadBT.setOnClickListener { getRandomDog() }

    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun getRandomDog() {
        GlobalScope.launch(Dispatchers.IO) {
            val response = try {
                RetrofitInstance.api.getRandomDog()
            } catch (e: IOException) {
                Toast.makeText(applicationContext, "app error ${e.message}", Toast.LENGTH_LONG)
                    .show()
                return@launch
            } catch (e: HttpException) {
                Toast.makeText(applicationContext, "http error ${e.message}", Toast.LENGTH_LONG)
                    .show()
                return@launch
            }
            if (response.isSuccessful && response.body() != null) {
                withContext(Dispatchers.Main) {
                    val data = response.body()
                    Picasso.get().load(data?.url).into(imageView)
                }
            }
        }
    }


    private fun init(){
        toolbar = findViewById(R.id.toolbar)
        toolbar.apply {
            title = "Загрузчик картинок"
            setNavigationIcon(R.drawable.ic_exit)
            setNavigationOnClickListener {
                finish()
            }
        }
        imageView = findViewById(R.id.imageView)
        downLoadBT = findViewById(R.id.downLoadBT)
    }

}