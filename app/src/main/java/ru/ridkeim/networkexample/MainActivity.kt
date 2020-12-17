package ru.ridkeim.networkexample

import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkRequest
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import ru.ridkeim.networkexample.databinding.ActivityMainBinding
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.SocketException
import java.util.*

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    private lateinit var networkRequest: NetworkRequest
    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var amBinding: ActivityMainBinding
    private val intentFilter  = IntentFilter().apply {
        addAction(ConnectivityManager.CONNECTIVITY_ACTION)
    }
    private val connectivityBR = ConnectivityBroadcastReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        amBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(amBinding.root)
        connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        amBinding.root.setOnClickListener{
            if(isOnline()){
                val info = amBinding.connectionInfo
                val activeNetworkInfo = connectivityManager.activeNetworkInfo
                if(activeNetworkInfo != null){
                    when(activeNetworkInfo.type){
                        ConnectivityManager.TYPE_MOBILE -> info.text = "Mobile"
                        ConnectivityManager.TYPE_WIMAX -> info.text = "LTE"
                        ConnectivityManager.TYPE_WIFI -> info.text = "Wi-fi"
                        else -> info.text = ""
                    }
                    val stateName = activeNetworkInfo.state?.name ?: ""
                    info.append("\n${stateName}")
                }
                Log.i("Network", "Network is available")
            }else{
                amBinding.connectionInfo.text = "Not connected"
                Log.i("Network", "Network is unavailable")
            }
            var ip = ""
            try {
                val enumNetworkInterfaces: Enumeration<NetworkInterface> = NetworkInterface
                        .getNetworkInterfaces()
                while (enumNetworkInterfaces.hasMoreElements()) {
                    val networkInterface: NetworkInterface = enumNetworkInterfaces
                            .nextElement()
                    val enumInetAddress: Enumeration<InetAddress> = networkInterface
                            .inetAddresses
                    while (enumInetAddress.hasMoreElements()) {
                        val inetAddress: InetAddress = enumInetAddress.nextElement()
                        var ipAddress = ""
                        if (inetAddress.isLoopbackAddress) {
                            ipAddress = "LoopbackAddress: "
                        } else if (inetAddress.isSiteLocalAddress) {
                            ipAddress = "SiteLocalAddress: "
                        } else if (inetAddress.isLinkLocalAddress) {
                            ipAddress = "LinkLocalAddress: "
                        } else if (inetAddress.isMulticastAddress) {
                            ipAddress = "MulticastAddress: "
                        }
                        ip += "\n$ipAddress${inetAddress.hostAddress}"
                    }
                }
            } catch (e: SocketException) {
                e.printStackTrace()
                ip += "Something Wrong! ${e}"
            }
            amBinding.connectionInfo.append("\n$ip")
        }
        networkRequest = NetworkRequest.Builder().build()
    }

    private fun isOnline(): Boolean {
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo?.isConnected ?: false
    }


    override fun onResume() {
        super.onResume()
        registerReceiver(connectivityBR, intentFilter)

    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(connectivityBR)
    }
}