package dev.cidick.sweetbobo.main

import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import dev.cidick.sweetbobo.databinding.NoNetworkBinding
import dev.cidick.sweetbobo.provider.NetConnection.isConnected
import dev.cidick.sweetbobo.provider.NetworkChangeReceiver

@Suppress("DEPRECATION")
class NoNetworkAct : AppCompatActivity(), NetworkChangeReceiver.NetworkChangeListener {
    private lateinit var networkChangeReceiver: NetworkChangeReceiver
    private lateinit var net : NoNetworkBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        net = NoNetworkBinding.inflate(layoutInflater)
        setContentView(net.root)

        // Initialize the network change receiver
        networkChangeReceiver = NetworkChangeReceiver(this)

        // Register the receiver to listen for network changes
        registerNetworkChangeReceiver()

        // Check if network connection is available when activity is created
        if (!isConnected(this)) {
            Toast.makeText(this, "Disconnected to Network", Toast.LENGTH_LONG).show()
            val offlineGame = Intent(this, MainMenu::class.java)
            startActivity(offlineGame)
            finish()
        } else {
            val refresh = Intent(this, AppLaunch::class.java)
            startActivity(refresh)
            finish()
            Toast.makeText(this, "Network has been restore", Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Unregister the receiver when the activity is destroyed
        unregisterNetworkChangeReceiver()
    }

    override fun onNetworkChanged() {
        // Handle network change event here, such as refreshing the activity
        if (isConnected(this)) {
            // Refresh the activity or reload data
            refreshActivity()
        }
    }

    private fun refreshActivity() {
        // Reload your activity content or perform necessary actions
        recreate() // This will restart the activity and refresh its content
    }

    private fun registerNetworkChangeReceiver() {
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(networkChangeReceiver, filter)
    }

    private fun unregisterNetworkChangeReceiver() {
        unregisterReceiver(networkChangeReceiver)
    }
}