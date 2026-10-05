package br.com.doma.assist

import android.app.Notification
import android.content.Intent
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification

class DomaNotificationListener : NotificationListenerService() {
    override fun onListenerConnected() = signal()
    override fun onListenerDisconnected() = signal()
    override fun onNotificationPosted(sbn: StatusBarNotification) {
        if (sbn.isOngoing || sbn.notification.flags and Notification.FLAG_GROUP_SUMMARY != 0) return
        if (sbn.packageName == packageName && sbn.notification.channelId != "doma_demo") return
        val content = extract(sbn.notification) ?: return
        val app = application as DomaApplication
        app.store.edit().putString("last_notification", content).apply()
        signal()
        if (app.store.getBoolean("auto_read", false)) app.speech.speak(content)
    }
    private fun signal() { sendBroadcast(Intent(DomaApplication.CHANGED).setPackage(packageName)) }
    companion object {
        fun extract(notification: Notification): String? {
            val extras = notification.extras
            val title = extras.getCharSequence(Notification.EXTRA_TITLE)?.toString().orEmpty()
            val messages = extras.getParcelableArray(Notification.EXTRA_MESSAGES)
                ?.let(Notification.MessagingStyle.Message::getMessagesFromBundleArray)
            val body = extras.getCharSequence(Notification.EXTRA_BIG_TEXT)?.toString()?.takeIf(String::isNotBlank)
                ?: extras.getCharSequence(Notification.EXTRA_TEXT)?.toString()?.takeIf(String::isNotBlank)
                ?: messages?.lastOrNull()?.text?.toString()?.takeIf(String::isNotBlank)
                ?: extras.getCharSequenceArray(Notification.EXTRA_TEXT_LINES)?.joinToString(". ")?.takeIf(String::isNotBlank)
            if (body == null) return null
            return listOf(title, body).filter(String::isNotBlank).distinct().joinToString(". ").take(3500)
        }
    }
}
