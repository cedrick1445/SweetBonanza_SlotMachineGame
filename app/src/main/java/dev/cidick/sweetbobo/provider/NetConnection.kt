package dev.cidick.sweetbobo.provider

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log

object NetConnection {
    @JvmStatic
    fun isConnected(activity: Activity): Boolean {
        var connected = false
        val connectivityManager = activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val network = connectivityManager.activeNetwork
            val capabilities = connectivityManager.getNetworkCapabilities(network)
            connected =
                capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || capabilities.hasTransport(
                    NetworkCapabilities.TRANSPORT_CELLULAR
                ))
        }
        if (!connected) {
            Log.w("NetworkConnection", "Disconnected to Network")
        } else {
            Log.w("NetworkConnection", "Network Restore")
        }
        return connected
    }
}