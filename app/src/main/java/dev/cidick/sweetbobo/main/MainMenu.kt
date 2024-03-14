package dev.cidick.sweetbobo.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import dev.cidick.sweetbobo.R
import dev.cidick.sweetbobo.databinding.GameMenuBinding
import dev.cidick.sweetbobo.main.PolicyGame
import dev.cidick.sweetbobo.util.GSetting

class MainMenu : AppCompatActivity() {
//    private lateinit var gameSetting: GSetting
//    private lateinit var menu : GameMenuBinding
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
//        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
//        menu = GameMenuBinding.inflate(layoutInflater)
//        setContentView(menu.root)
//
//        gameSetting = GSetting(this)
//
//        menu.start.setOnClickListener { _: View? ->
//            menu.start.visibility = View.INVISIBLE
//            menu.play.visibility = View.VISIBLE
//            menu.settings.visibility = View.VISIBLE
//            menu.policy.visibility = View.VISIBLE
//        }
//
//        menu.play.setOnClickListener { _: View? ->
//            val intent = Intent(this@MainMenu, MainGame::class.java)
//
//            startActivity(intent)
//        }
//        menu.settings.setOnClickListener { _: View? -> gameSetting.showSettingsDialog(this) }
//
//        menu.policy.setOnClickListener { _: View? ->
//            val intent = Intent(this@MainMenu, PolicyGame::class.java)
//            startActivity(intent)
//        }
//
//    }
//
//    override fun onPause() {
//        super.onPause()
//        gameSetting.stopSounds()
//    }
//
//    override fun onResume() {
//        super.onResume()
//        gameSetting.resumeSounds()
//    }
}
