package uk.ac.warwick.cim.aiinstreet

import android.media.AudioAttributes
import android.media.MediaPlayer

class AudioPlayer {
    private lateinit var mediaPlayer: MediaPlayer
    fun play(url:String?) {
        val url = "http://........" // your URL here
         mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
            setDataSource(url)
            prepare() // might take long! (for buffering, etc)
            start()
        }
    }

    fun stop () {
        mediaPlayer?.release()
        //mediaPlayer = null
    }

    fun pause() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
        } else {
            mediaPlayer.start()
        }
    }
}