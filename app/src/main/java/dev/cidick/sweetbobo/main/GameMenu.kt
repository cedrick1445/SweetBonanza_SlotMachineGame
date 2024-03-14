package dev.cidick.sweetbobo.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import dev.cidick.sweetbobo.databinding.GameMenuBinding
import dev.cidick.sweetbobo.util.GSetting

class GameMenu : AppCompatActivity() {
    private lateinit var gameSetting: GSetting
    private lateinit var menu : GameMenuBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        menu = GameMenuBinding.inflate(layoutInflater)
        setContentView(menu.root)

        gameSetting = GSetting(this)

        menu.start.setOnClickListener { _: View? ->
            menu.play.visibility = View.VISIBLE
            menu.settings.visibility = View.VISIBLE
            menu.policy.visibility = View.VISIBLE
        }

        menu.play.setOnClickListener { _: View? ->
            val intent = Intent(this@GameMenu, MainGame::class.java)

            startActivity(intent)
        }
        menu.settings.setOnClickListener { _: View? -> gameSetting.showSettingsDialog(this) }

        menu.policy.setOnClickListener { _: View? ->
            val intent = Intent(this@GameMenu, PolicyGame::class.java)
            startActivity(intent)
        }

    }

    override fun onPause() {
        super.onPause()
        gameSetting.stopSounds()
    }

    override fun onResume() {
        super.onResume()
        gameSetting.resumeSounds()
    }
}