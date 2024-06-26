package uk.ac.warwick.cim.aiinstreet

import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.ToneGenerator
import android.util.Log

/**
 * Simple media player for testing.
 */
class AudioPlayer {
    private lateinit var mediaPlayer: MediaPlayer


    fun play(url:String) {
        //val url = "http://........" // your URL here
        Log.i("URL", url)

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
        mediaPlayer.release()
        //mediaPlayer = null
    }

    fun pause() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
        } else {
            mediaPlayer.start()
        }
    }

    //Tones stuff
    fun playSound() {
        val toneGenerator = ToneGenerator(AudioManager.STREAM_ALARM, 100)
        toneGenerator.startTone(ToneGenerator.TONE_DTMF_7, 200)
    }

    fun playSensedBLE() {
        val toneGenerator = ToneGenerator(AudioManager.STREAM_ALARM, 100)
        toneGenerator.startTone(ToneGenerator.TONE_DTMF_5, 200)
    }

    fun playSensedWifi() {
        val toneGenerator = ToneGenerator(AudioManager.STREAM_ALARM, 100)
        toneGenerator.startTone(ToneGenerator.TONE_DTMF_1, 200)
    }

    fun playSensedBase() {
        val toneGenerator = ToneGenerator(AudioManager.STREAM_ALARM, 100)
        toneGenerator.startTone(ToneGenerator.TONE_DTMF_4, 200)
    }
}