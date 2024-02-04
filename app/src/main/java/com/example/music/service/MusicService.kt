package com.example.music.service

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder

class MusicService:Service() {
    private var myBinder = MyBinder()
    var mediaPlayer : MediaPlayer? = null
    private lateinit var runnable: Runnable

    override fun onBind(intent: Intent?): IBinder {
        return myBinder
    }
    inner class MyBinder:Binder(){
        fun currentService(): MusicService {
            return this@MusicService
        }
    }

//    fun seekBarSetup(){
//        runnable = Runnable {
//            PlayerActivity.binding.tvSeekBarStart.text = formatDuration(mediaPlayer!!.currentPosition.toLong())
//            PlayerActivity.binding.seekBarPA.progress = mediaPlayer!!.currentPosition
//            Handler(Looper.getMainLooper()).postDelayed(runnable, 200)
//        }
//        Handler(Looper.getMainLooper()).postDelayed(runnable, 0)
//    }
}