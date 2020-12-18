package ru.ridkeim.networkexample

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.ridkeim.networkexample.databinding.ActivityMainBinding
import java.lang.StringBuilder

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

        viewModel.networkChanged.observe(this){
            if(it){
                updateInfo()
            }else{
                viewModel.setSomethingChanged(false)
            }
        }
    }

    private fun updateInfo(){
        val builder = StringBuilder()
        for (network in connectivityManager.allNetworks){
            val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
            val linkProperties = connectivityManager.getLinkProperties(network)
            builder.append("\n$linkProperties\n$networkCapabilities\n")
        }
        amBinding.connectionInfo.text = if(builder.isEmpty()){
            getString(R.string.info)
        }else{
            builder.toString()
        }

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