package com.example.weather

import android.content.Context
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import java.lang.StringBuilder
import kotlin.math.round

internal fun format(str: String): String {
    val space = {c: Char -> c == ' ' || c == '\n'}
    val spaceAt = {i: Int -> space(str[i])}
    val res = StringBuilder()
    var i = 0
    while (i < str.length && spaceAt(i))
        i++
    while (i < str.length) {
        if (spaceAt(i)) {
            do
                i++
            while (i < str.length && spaceAt(i))
            if (i < str.length)
                res.append(' ')
        } else {
            res.append(str[i])
            i++
        }
    }
    return res.toString().lowercase().capitalize()
}

internal fun toDeg(temp: String): Int {
    val degVal: Double = temp.toDouble() - 273.15
    return round(degVal).toInt()
}

internal fun isInternetConnected(activity: AppCompatActivity): Boolean {
    val connectivityManager=activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo = connectivityManager.activeNetworkInfo
    return  networkInfo != null && networkInfo.isConnected
}