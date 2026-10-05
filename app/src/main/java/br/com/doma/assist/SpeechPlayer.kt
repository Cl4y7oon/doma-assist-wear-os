package br.com.doma.assist

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import java.util.Locale

class SpeechPlayer(context: Context, private val audio: AudioHelper) {
    private val main = Handler(Looper.getMainLooper())
    private val manager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    private var ready = false
    private var pending: String? = null
    private var failure: String? = null
    private var tts: TextToSpeech? = null
    var lastSpoken = ""
        private set
    var status: (String) -> Unit = {}
    private val attributes = AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ASSISTANCE_ACCESSIBILITY)
        .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH).build()
    private val focus = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK)
        .setAudioAttributes(attributes).setOnAudioFocusChangeListener { change ->
            if (change == AudioManager.AUDIOFOCUS_LOSS || change == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) stop()
        }.build()
    init {
        tts = TextToSpeech(context.applicationContext) { result ->
            main.post {
                if (result != TextToSpeech.SUCCESS) {
                    failure = "Não foi possível iniciar a voz. Confira o mecanismo de fala nas configurações."
                    pending = null; status(failure!!)
                } else {
                    val language = tts?.setLanguage(Locale.forLanguageTag("pt-BR"))
                    if (language == TextToSpeech.LANG_MISSING_DATA || language == TextToSpeech.LANG_NOT_SUPPORTED) {
                        failure = "Instale a voz em português nas configurações de texto para fala."
                        pending = null; status(failure!!)
                    } else {
                        ready = true
                        tts?.setAudioAttributes(attributes)
                        tts?.setSpeechRate(0.9f)
                        pending?.let { pending = null; speak(it) }
                    }
                }
            }
        }
        tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(id: String?) { main.post { status("Leitura em andamento") }; Log.i("DomaSpeech", "START") }
            override fun onDone(id: String?) {
                manager.abandonAudioFocusRequest(focus)
                main.post { status("Leitura concluída") }; Log.i("DomaSpeech", "DONE")
            }
            @Deprecated("API legacy callback")
            override fun onError(id: String?) {
                manager.abandonAudioFocusRequest(focus)
                main.post { status("Falha na reprodução da voz. Confira o mecanismo de fala.") }
                Log.e("DomaSpeech", "ERROR")
            }
        })
    }
    fun speak(text: String) {
        if (text.isBlank()) { status("Nenhum texto disponível para leitura."); return }
        if (!audio.hasOutput()) { status("Sem saída de áudio. Conecte um fone Bluetooth."); return }
        failure?.let { status(it); return }
        if (!ready) { pending = text; status("Preparando a voz..."); return }
        if (manager.requestAudioFocus(focus) != AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            status("Áudio ocupado. Tente novamente após a chamada ou reprodução atual."); return
        }
        lastSpoken = text.take(3500)
        if (tts?.speak(lastSpoken, TextToSpeech.QUEUE_FLUSH, null, "doma-${System.nanoTime()}") == TextToSpeech.ERROR) {
            manager.abandonAudioFocusRequest(focus); status("Não foi possível reproduzir a voz.")
        }
    }
    fun repeat() = speak(lastSpoken)
    fun stop() { pending = null; tts?.stop(); manager.abandonAudioFocusRequest(focus) }
    fun close() { stop(); tts?.shutdown(); tts = null }
}
