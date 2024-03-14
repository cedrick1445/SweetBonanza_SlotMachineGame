package dev.cidick.sweetbobo.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import dev.cidick.sweetbobo.R
import dev.cidick.sweetbobo.databinding.MainGameBinding
import dev.cidick.sweetbobo.provider.CustomLayout
import dev.cidick.sweetbobo.util.GLogic
import dev.cidick.sweetbobo.util.GSetting
import dev.cidick.sweetbobo.util.MainAdapter

class MainGame : AppCompatActivity() {
    private var position1 = 5
    private var position2 = 5
    private var position3 = 5
    private lateinit var layout1: CustomLayout
    private lateinit var layout2: CustomLayout
    private lateinit var layout3: CustomLayout
    private var myCoinsVal = 0
    private var betVal = 0
    private var jackpotVal = 0
    private lateinit var gLogic: GLogic
    private lateinit var gSetting: GSetting
    private lateinit var gmain : MainGameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(1)
        window.setFlags(1024, 1024)
        gmain = MainGameBinding.inflate(layoutInflater)
        setContentView(gmain.root)
        gSetting = GSetting(this)
        gLogic = GLogic()
        val playSoundValue = gSetting.playsound
        val spinAdapter = MainAdapter()
        layout1 = CustomLayout(this)
        layout1.setScrollEnabled(false)
        layout2 = CustomLayout(this)
        layout2.setScrollEnabled(false)
        layout3 = CustomLayout(this)
        layout3.setScrollEnabled(false)
        setText()
        updateText()
        gmain.spinnerXML.spinner1.apply {
            setHasFixedSize(true)
            layoutManager = layout1
            adapter = spinAdapter
            scrollToPosition(position1)
            addOnScrollListener(object : RecyclerView.OnScrollListener(){
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        scrollToPosition(gLogic.getPosition(0))
                        layout1.setScrollEnabled(false)
                    } } }) }

        gmain.spinnerXML.spinner2.apply {
            setHasFixedSize(true)
            layoutManager = layout2
            adapter = spinAdapter
            scrollToPosition(position2)
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        scrollToPosition(gLogic.getPosition(1))
                        layout2.setScrollEnabled(false)
                    } } }) }

        gmain.spinnerXML.spinner3.apply {
            setHasFixedSize(true)
            layoutManager = layout3
            adapter = spinAdapter
            scrollToPosition(position3)
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        scrollToPosition(gLogic.getPosition(2))
                        layout3.setScrollEnabled(false)
                        updateText()
                        if (gLogic.hasWon) {
                            if (playSoundValue == 1) {
                                gSetting.playWinSound()
                            }
                            val rootView = gmain.root
                            val snackbar = Snackbar.make(rootView, "", 1800)
                            val customSnackbarView = layoutInflater.inflate(R.layout.win_splash, null)

                            val winningPattern = gLogic.winningPattern
                            val winningView = layout3.findViewByPosition(winningPattern)
                            if (winningView != null) {
                                val winningImageView = winningView.findViewById<ImageView>(R.id.fruits)
                                val layoutParams = winningImageView.layoutParams
                                layoutParams.width = (layoutParams.width * 1.1).toInt()
                                layoutParams.height = (layoutParams.height * 1.1).toInt()
                                winningImageView.layoutParams = layoutParams
                            }
                            val winCoins = customSnackbarView.findViewById<TextView>(R.id.win_coins)
                            winCoins.text = gLogic.getPrize()
                            snackbar.view.setBackgroundColor(resources.getColor(android.R.color.transparent, theme))
                            val snackbarLayout = snackbar.view as Snackbar.SnackbarLayout
                            snackbarLayout.addView(customSnackbarView, 0)
                            snackbar.show()
                            gLogic.hasWon = false
                        } } } }) }


        gmain.btnBack.setOnClickListener {
            val intentBack = Intent(this@MainGame, GameMenu::class.java)
            startActivity(intentBack)
        }

        gmain.spinButton.setOnClickListener {
            gmain.spinButton.isEnabled = false
            if (playSoundValue == 1) {
                gSetting.playSpinSound()
            }
            layout3.setScrollEnabled(true)
            layout2.setScrollEnabled(true)
            layout1.setScrollEnabled(true)
             gLogic.spinResults
            position1 = gLogic.getPosition(0) + 72
            position2 = gLogic.getPosition(1) + 142
            position3 = gLogic.getPosition(2) + 212
            gmain.spinnerXML.spinner1.smoothScrollToPosition(position1)
            gmain.spinnerXML.spinner2.smoothScrollToPosition(position2)
            gmain.spinnerXML.spinner3.smoothScrollToPosition(position3)
            Handler(Looper.getMainLooper()).postDelayed({
                gmain.spinButton.isEnabled = true }, 4000)
        }

        gmain.plusButton.setOnClickListener {
            if (playSoundValue == 1) {
                gSetting.playSpinSound()
            }
            gLogic.betUp()
            updateText()
        }

        gmain.minusButton.setOnClickListener {
            if (playSoundValue == 1) {
                gSetting.playSpinSound()
            }
            gLogic.betDown()
            updateText()
        }

        gmain.settings.setOnClickListener {
            if (playSoundValue == 1) {
                gSetting.playSpinSound()
            }
            gSetting.showSettingsDialog(this)
        }
    }
    private fun setText() {
        if (gSetting.isFirstRun) {
            gLogic.setTheCoins(995)
            gLogic.setBet(5)
            gLogic.setJackpot(100000)
            val editor = gSetting.pref.edit()
            editor.putBoolean("firstRun", false)
            editor.apply()
        } else {
            val coins = gSetting.pref.getString("coins", "995")
            val betString = gSetting.pref.getString("bet", "5")
            val jackpotString = gSetting.pref.getString("jackpot", "100000")
            myCoinsVal = coins!!.toInt()
            betVal = betString!!.toInt()
            jackpotVal = jackpotString!!.toInt()
            gLogic.setTheCoins(myCoinsVal)
            gLogic.setBet(betVal)
            gLogic.setJackpot(jackpotVal)
        }
    }

    private fun updateText() {
        gmain.jackpot.text = gLogic.getJackpot()
        gmain.myCoins.text = gLogic.getTheCoins()
        gmain.bet.text = gLogic.getBet()
        val editor = gSetting.pref.edit()
        editor.putString("coins", gLogic.getTheCoins())
        editor.putString("bet", gLogic.getBet())
        editor.putString("jackpot", gLogic.getJackpot())
        editor.apply()
    }

    override fun onPause() {
        super.onPause()
        gSetting.stopSounds()
    }

    override fun onResume() {
        super.onResume()
        gSetting.resumeSounds()
    }
}