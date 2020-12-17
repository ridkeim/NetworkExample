package ru.ridkeim.networkexample

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.widget.Toast


class ConnectivityBroadcastReceiver : BroadcastReceiver() {
    @Suppress("DEPRECATION")
    override fun onReceive(context: Context?, intent: Intent?) {
        val connectivityManager =
            context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        if (networkInfo?.type == ConnectivityManager.TYPE_WIFI && networkInfo.isConnected) {
            // Wifi is connected
            Toast.makeText(context, "Wi-FI подключен", Toast.LENGTH_SHORT).show()
        }
    }
}