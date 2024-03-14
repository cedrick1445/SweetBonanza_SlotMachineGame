package dev.cidick.sweetbobo.provider

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NetworkChangeReceiver(private val listener: NetworkChangeListener?) : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        listener?.onNetworkChanged()
    }

    interface NetworkChangeListener {
        fun onNetworkChanged()
    }
}
