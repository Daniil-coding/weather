package com.example.weather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.io.FileNotFoundException
import java.net.URL

class MainActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        val WEATHER_KEY: String = "915c3c305bf5589dd0f2877ffa604ce2"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        progressBar.visibility = View.INVISIBLE
        getWeatherBtn.setOnClickListener(this)
        output.addTextChangedListener {
            progressBar.visibility = View.INVISIBLE
        }
    }

    override fun onClick(v: View?) {
        if (!isInternetConnected(this)) {
            Toast.makeText(this, getString(R.string.internetAccessError), Toast.LENGTH_LONG).show()
            return
        }
        val cityName = format(cityInField.text.toString())
        if (cityName == "") {
            Toast.makeText(this, getString(R.string.emptyInput), Toast.LENGTH_SHORT).show()
            return
        }
        cityInField.setText(cityName)
        progressBar.visibility = View.VISIBLE
        showWeather(cityName)
    }

    private fun showWeather(cityName: String) {
        Thread {
            Log.i("weather", "request for '$cityName'")
            val requestLink: String =
                "https://api.openweathermap.org/data/2.5/weather?q=$cityName&appid=$WEATHER_KEY"
            try {
                val responce = URL(requestLink).readText()
                Log.i("weather", responce)
                val data = JSONObject(responce)
                val temp = toDeg(data.getJSONObject("main").getString("temp"))
                val feelTemp = toDeg(data.getJSONObject("main").getString("feels_like"))
                val info = data.getJSONArray("weather")
                val description = info.getJSONObject(0).getString("description")
                val city_tr = getString(R.string.city)
                val temp_tr = getString(R.string.temperature)
                val feels_tr = getString(R.string.feels)
                output.text = "$city_tr: $cityName\n$temp_tr: $temp\n$feels_tr: $feelTemp\n\n$description"
            } catch (e: FileNotFoundException) {
                output.text = getString(R.string.invalidCity)
                Log.i("weather", "invalid request")
            }
        }.start()
    }
}