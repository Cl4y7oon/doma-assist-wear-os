package br.com.doma.assist

import android.app.Application
import android.content.Intent

class DomaApplication : Application() {
    lateinit var audio: AudioHelper
        private set
    lateinit var speech: SpeechPlayer
        private set
    val store by lazy { getSharedPreferences("doma", MODE_PRIVATE) }
    override fun onCreate() {
        super.onCreate()
        audio = AudioHelper(this)
        speech = SpeechPlayer(this, audio)
        audio.observe {
            if (!audio.hasOutput()) speech.stop()
            sendBroadcast(Intent(AUDIO_CHANGED).setPackage(packageName))
        }
    }
    companion object {
        const val CHANGED = "br.com.doma.assist.NOTIFICATION_CHANGED"
        const val AUDIO_CHANGED = "br.com.doma.assist.AUDIO_CHANGED"
    }
}
