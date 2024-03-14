package dev.cidick.sweetbobo.util

import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import dev.cidick.sweetbobo.R

class GSetting(context: Context) {
    val isFirstRun: Boolean
    private val mp: MediaPlayer?
    private val win: MediaPlayer?
    var pref: SharedPreferences

    init {
        pref = context.getSharedPreferences(PREFS_FIRST_RUN, Context.MODE_PRIVATE)
        isFirstRun = pref.getBoolean(PREFS_FIRST_RUN, true)
        if (isFirstRun) {
            playmusic = 1
            Companion.playsound = 1
            val editor = pref.edit()
            editor.putBoolean(PREFS_FIRST_RUN, false)
            editor.apply()
        } else {
            playmusic = pref.getInt(PREFS_MUSIC, 1)
            Companion.playsound = pref.getInt(PREFS_SOUND, 1)
        }
        bgsound = MediaPlayer.create(context, R.raw.bg_music)
        bgsound.isLooping = true
        mp = MediaPlayer.create(context, R.raw.spin)
        win = MediaPlayer.create(context, R.raw.win)
        if (playmusic == 1) {
            checkmusic()
        }
    }

    fun resumeSounds() {
        if (playmusic == 1) {
            bgsound.start()
        }
    }

    fun stopSounds() {
        if (bgsound.isPlaying) {
            bgsound.pause()
        }
        if (mp != null && mp.isPlaying) {
            mp.pause()
        }
        if (win != null && win.isPlaying) {
            win.pause()
        }
    }

    fun playSpinSound() {
        if (Companion.playsound == 1) {
            mp!!.start()
        }
    }

    fun playWinSound() {
        if (Companion.playsound == 1) {
            win!!.start()
        }
    }

    val playsound: Int
        get() = Companion.playsound

    companion object {
        const val PREFS_FIRST_RUN = "FirstRun"
        private const val PREFS_MUSIC = "music"
        private const val PREFS_SOUND = "sound"
        private var playmusic = 0
        private var playsound = 0
        lateinit var musicOff: ImageView
        lateinit var musicOn: ImageView
        lateinit var sfxon: ImageView
        lateinit var sfxoff: ImageView
        lateinit var bgsound: MediaPlayer
    }

    fun showSettingsDialog(context: Context?) {
        val dialog = Dialog(context!!, R.style.WinDialog)
        dialog.window!!.setContentView(R.layout.settings)
        dialog.window!!.setGravity(Gravity.CENTER_HORIZONTAL)
        dialog.window!!.setBackgroundDrawableResource(R.color.transparent)
        val close = dialog.findViewById<ImageView>(R.id.close)
        close.setOnClickListener { _: View? -> dialog.dismiss() }
        musicOn = dialog.findViewById(R.id.music_on)
        musicOn.setOnClickListener { _: View? ->
            playmusic = 0
            checkmusic()
            musicOn.setVisibility(View.INVISIBLE)
            musicOff.setVisibility(View.VISIBLE)
            val editor = pref.edit()
            editor.putInt(PREFS_MUSIC, playmusic)
            editor.apply()
        }
        musicOff = dialog.findViewById(R.id.music_off)
        musicOff.setOnClickListener { _: View? ->
            playmusic = 1
            bgsound.start()
            musicOn.setVisibility(View.VISIBLE)
            musicOff.setVisibility(View.INVISIBLE)
            val editor = pref.edit()
            editor.putInt(PREFS_MUSIC, playmusic)
            editor.apply()
        }
        sfxon = dialog.findViewById(R.id.sounds_on)
        sfxon.setOnClickListener { _: View? ->
            Companion.playsound = 0
            sfxon.setVisibility(View.INVISIBLE)
            sfxoff.setVisibility(View.VISIBLE)
            val editor = pref.edit()
            editor.putInt(PREFS_SOUND, Companion.playsound)
            editor.apply()
        }
        sfxoff = dialog.findViewById(R.id.sounds_off)
        sfxoff.setOnClickListener { _: View? ->
            Companion.playsound = 1
            dialog.show()
            sfxon.setVisibility(View.VISIBLE)
            sfxoff.setVisibility(View.INVISIBLE)
            val editor = pref.edit()
            editor.putInt(PREFS_SOUND, Companion.playsound)
            editor.apply()
        }
        checkmusicdraw()
        checksounddraw()
        dialog.show()
    }

    private fun checkmusic() {
        if (playmusic == 1) {
            bgsound.start()
        } else {
            bgsound.pause()
        }
    }

    private fun checkmusicdraw() {
        if (playmusic == 1) {
            musicOn.setVisibility(View.VISIBLE)
            musicOff.setVisibility(View.INVISIBLE)
        } else {
            musicOn.setVisibility(View.INVISIBLE)
            musicOff.setVisibility(View.VISIBLE)
        }
    }

    private fun checksounddraw() {
        if (Companion.playsound == 1) {
            sfxon.setVisibility(View.VISIBLE)
            sfxoff.setVisibility(View.INVISIBLE)
        } else {
            sfxon.setVisibility(View.INVISIBLE)
            sfxoff.setVisibility(View.VISIBLE)
        }
    }
}
