package com.example.music

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.music.databinding.ActivityPlayerBinding
import com.example.music.service.MusicService

class PlayerActivity : AppCompatActivity(), ServiceConnection {
    private lateinit var runnable: Runnable


    companion object{
        lateinit var musicListPA : ArrayList<Music>
        var songPosition: Int = 0
        var isPlaying : Boolean = false
        var musicService: MusicService? = null
    }

    private lateinit var binding: ActivityPlayerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //for starting service
        val intent = Intent(this, MusicService::class.java)
        bindService(intent, this, BIND_AUTO_CREATE)
        startService(intent)
        initializeLayout()
        binding.playPauseBtnPA.setOnClickListener {
            if (isPlaying) pauseMusic()
            else playMusic()
        }
        binding.previousBtnPA.setOnClickListener { prevNextSong(increment = false) }
        binding.nextBtnPA.setOnClickListener { prevNextSong(increment = true) }
        binding.seekBarPA.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

                if (fromUser) musicService!!.mediaPlayer!!.seekTo(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit

            override fun onStopTrackingTouch(seekBar: SeekBar?) = Unit

        })

    }

    private fun setLayout(){
        Glide.with(this)
            .load(musicListPA[songPosition].artUri)
            .apply(RequestOptions().placeholder(R.drawable.music_player_icon_slash_screen).centerCrop())
            .into(binding.songImgPA)
        binding.songNamePA.text = musicListPA[songPosition].title
    }
    private fun createMediaPlayer(){
      try {
          if (musicService!!.mediaPlayer == null) musicService!!.mediaPlayer = MediaPlayer()
          musicService!!.mediaPlayer!!.reset()
          musicService!!.mediaPlayer!!.setDataSource(musicListPA[songPosition].path)
          musicService!!.mediaPlayer!!.prepare()
          musicService!!.mediaPlayer!!.start()
          isPlaying = true
          binding.playPauseBtnPA.setIconResource(R.drawable.pause_icon)
          binding.tvSeekBarStart.text = formatDuration(musicService!!.mediaPlayer!!.currentPosition.toLong())
          binding.tvSeekBarEnd.text = formatDuration(musicService!!.mediaPlayer!!.duration.toLong())

          binding.seekBarPA.progress = 0
          binding.seekBarPA.max = musicService!!.mediaPlayer!!.duration
          seekBarSetup()

      }catch (e:Exception){return}

    }

     private  fun seekBarSetup(){
        runnable = Runnable {
            binding.tvSeekBarStart.text = formatDuration(musicService!!.mediaPlayer!!.currentPosition.toLong())
           binding.seekBarPA.progress = musicService!!.mediaPlayer!!.currentPosition
            Handler(Looper.getMainLooper()).postDelayed(runnable, 200)
        }
        Handler(Looper.getMainLooper()).postDelayed(runnable, 0)
    }
    private fun initializeLayout(){
        songPosition = intent.getIntExtra("index", 0)
        when(intent.getStringExtra("class")){
            "MusicAdapter" ->{
                musicListPA = ArrayList()
                musicListPA.addAll(MainActivity.MusicListMA)
                setLayout()

            }
            "MainActivity" ->{
                musicListPA = ArrayList()
                musicListPA.addAll(MainActivity.MusicListMA)
                setLayout()

            }
        }
    }

    private fun playMusic(){

        binding.playPauseBtnPA.setIconResource(R.drawable.pause_icon)
        isPlaying = true
        musicService!!.mediaPlayer!!.start()

    }
    private fun pauseMusic(){
        binding.playPauseBtnPA.setIconResource(R.drawable.play_icon)
        isPlaying = false
        musicService!!.mediaPlayer!!.pause()
    }

    private fun prevNextSong(increment:Boolean){
        if (increment){

            setSongPosition(increment = true)
            setLayout()
            createMediaPlayer()

        }
        else{
            setSongPosition(increment = false)
            setLayout()
            createMediaPlayer()

        }
    }

    private fun setSongPosition(increment: Boolean){
        if (increment){

            if (musicListPA.size - 1 == songPosition)
                songPosition = 0
            else ++songPosition

        }else{
            if (0 == songPosition)
                songPosition = musicListPA.size - 1
            else --songPosition
        }
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        val binder = service as MusicService.MyBinder
        musicService = binder.currentService()
        createMediaPlayer()
    }

    override fun onServiceDisconnected(name: ComponentName?) {
      musicService = null
    }
}