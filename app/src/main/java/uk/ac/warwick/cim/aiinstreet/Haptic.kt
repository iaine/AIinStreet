package uk.ac.warwick.cim.aiinstreet

import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator

class Haptic {

    fun vibrate(vibrator: Vibrator, vibeTime: Long) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(
                VibrationEffect.createOneShot(
                    vibeTime,
                    VibrationEffect.DEFAULT_AMPLITUDE
                )
            )
        }

    }
}