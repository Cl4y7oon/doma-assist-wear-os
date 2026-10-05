package br.com.doma.assist

import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioDeviceCallback
import android.media.AudioDeviceInfo
import android.media.AudioManager
import android.os.Handler
import android.os.Looper

class AudioHelper(private val context: Context) {
    private val manager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    private var callback: AudioDeviceCallback? = null
    fun audioOutputAvailable(type: Int): Boolean =
        context.packageManager.hasSystemFeature(PackageManager.FEATURE_AUDIO_OUTPUT) &&
            manager.getDevices(AudioManager.GET_DEVICES_OUTPUTS).any { it.type == type }
    fun hasOutput(): Boolean = speaker() || bluetooth()
    fun speaker() = audioOutputAvailable(AudioDeviceInfo.TYPE_BUILTIN_SPEAKER)
    fun bluetooth() = audioOutputAvailable(AudioDeviceInfo.TYPE_BLUETOOTH_A2DP)
    fun description(): String = when {
        bluetooth() -> "Fone Bluetooth ativo"
        speaker() -> "Alto-falante ativo"
        else -> "Conecte um fone"
    }
    fun observe(changed: () -> Unit) {
        if (callback != null) return
        callback = object : AudioDeviceCallback() {
            override fun onAudioDevicesAdded(added: Array<out AudioDeviceInfo>) = changed()
            override fun onAudioDevicesRemoved(removed: Array<out AudioDeviceInfo>) = changed()
        }.also { manager.registerAudioDeviceCallback(it, Handler(Looper.getMainLooper())) }
    }
    fun close() { callback?.let(manager::unregisterAudioDeviceCallback); callback = null }
}
