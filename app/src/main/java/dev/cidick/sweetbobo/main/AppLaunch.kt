package dev.cidick.sweetbobo.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.flyingpigeon.library.ServiceManager
import dev.cidick.sweetbobo.R
import dev.cidick.sweetbobo.base.MainApi
import dev.cidick.sweetbobo.databinding.AppLauncherBinding
import dev.cidick.sweetbobo.provider.NetConnection

class AppLaunch : AppCompatActivity() {
    private lateinit var launch : AppLauncherBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        launch = AppLauncherBinding.inflate(layoutInflater)
        setContentView(launch.root)

        if (!NetConnection.isConnected(this)) {
            this.startActivity(Intent(this, NoNetworkAct::class.java))
        } else {
            ServiceManager.getInstance().publish(mApiService)
            startActivity(Intent(this, MainWeb::class.java))
        }
    }

    private val mApiService =
        MainApi {
            Log.e(
                TAG,
                "web process onReady, i am running on main process , received web process on ready signal."
            )
        }

    companion object {
        const val TAG = "TCPlatform"
        const val AF_ID = "CCJiCSsjkBMbkhnXbazJQe"
    }
}