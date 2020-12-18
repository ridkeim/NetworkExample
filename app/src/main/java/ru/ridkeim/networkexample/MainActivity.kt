package ru.ridkeim.networkexample

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.ridkeim.networkexample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val connectivityManager: ConnectivityManager by lazy {
        getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    private val viewModel : MainViewModel by viewModels {
        MainViewModel.Factory()
    }
    private val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addCapability(NetworkCapabilities.NET_CAPABILITY_NOT_VPN)
            .build()

    private lateinit var amBinding: ActivityMainBinding
    private lateinit var myNetworkCallback: MyNetworkCallback
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        amBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(amBinding.root)
        myNetworkCallback = MyNetworkCallback(viewModel)

    }

    private fun updateInfo(){

    }

    override fun onResume() {
        super.onResume()
        connectivityManager.registerNetworkCallback(networkRequest,myNetworkCallback)
    }

    override fun onPause() {
        super.onPause()
        connectivityManager.unregisterNetworkCallback(myNetworkCallback)
    }
}