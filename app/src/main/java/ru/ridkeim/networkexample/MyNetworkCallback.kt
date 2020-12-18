package ru.ridkeim.networkexample

import android.net.ConnectivityManager
import android.net.LinkProperties
import android.net.Network
import android.net.NetworkCapabilities
import android.util.Log

class MyNetworkCallback(private val listener : CurrentNetworkListener) : ConnectivityManager.NetworkCallback() {
    interface CurrentNetworkListener{
        fun setNetworkAvailable(value : Boolean)
    }

    companion object{
        val TAG : String? = MyNetworkCallback::class.java.canonicalName
    }

    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        Log.d(TAG,"onAvailable $network")
    }

    override fun onLosing(network: Network, maxMsToLive: Int) {
        Log.d(TAG,"loosing $network")
        super.onLosing(network, maxMsToLive)
    }

    override fun onLost(network: Network) {
        Log.d(TAG,"lost $network" )
        super.onLost(network)
    }

    override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
        Log.d(TAG,"network $network capabilitiesChanged $networkCapabilities")
        listener.setNetworkAvailable(true)
        super.onCapabilitiesChanged(network, networkCapabilities)
    }

    override fun onLinkPropertiesChanged(network: Network, linkProperties: LinkProperties) {
        Log.d(TAG,"network $network propertiesChanged $linkProperties")
        listener.setNetworkAvailable(true)
        super.onLinkPropertiesChanged(network, linkProperties)
    }

    override fun onBlockedStatusChanged(network: Network, blocked: Boolean) {
        Log.d(TAG,"network $network blockedStatusChanged=$blocked")
        super.onBlockedStatusChanged(network, blocked)
    }
}