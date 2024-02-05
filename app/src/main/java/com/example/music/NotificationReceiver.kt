package com.example.music

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NotificationReceiver: BroadcastReceiver()  {
    override fun onReceive(context: Context?, intent: Intent?) {
//        when(intent?.action){
//            //only play next or prev song, when music list contains more than one song
//            ApplicationClass.PREVIOUS -> if(PlayerActivity.musicListPA.size > 1) prevNextSong(increment = false, context = context!!)
//            ApplicationClass.PLAY -> if(PlayerActivity.isPlaying) pauseMusic() else playMusic()
//            ApplicationClass.NEXT -> if(PlayerActivity.musicListPA.size > 1) prevNextSong(increment = true, context = context!!)
//            ApplicationClass.EXIT ->{
//                exitApplication()
//            }
//        }

    }
}