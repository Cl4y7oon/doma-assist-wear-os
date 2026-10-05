package br.com.doma.assist

import android.app.Activity
import android.app.AlertDialog
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.speech.RecognizerIntent
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*

class MainActivity : Activity() {
    private val app get() = application as DomaApplication
    private val ink = Color.rgb(16, 28, 37)
    private val accent = Color.rgb(140, 232, 205)
    private lateinit var statusText: TextView
    private var audioText: TextView? = null
    private var onHome = true
    private var observing = false
    private var commandReadout = ""
    private val changed = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == DomaApplication.AUDIO_CHANGED) {
                audioText?.text = app.audio.description()
                if (!app.audio.hasOutput()) statusText.setText(R.string.audio_disconnected)
            } else if (onHome) showHome()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        app.speech.status = { message -> runOnUiThread { if (::statusText.isInitialized) statusText.text = message } }
        showHome()
        debugIntent(intent)
    }
    override fun onStart() {
        super.onStart()
        val filter = IntentFilter(DomaApplication.CHANGED).apply { addAction(DomaApplication.AUDIO_CHANGED) }
        if (Build.VERSION.SDK_INT >= 33) registerReceiver(changed, filter, Context.RECEIVER_NOT_EXPORTED)
        else @Suppress("UnspecifiedRegisterReceiverFlag") registerReceiver(changed, filter)
        observing = true
    }
    override fun onResume() { super.onResume(); if (onHome) showHome() }
    override fun onStop() {
        if (observing) { unregisterReceiver(changed); observing = false }
        super.onStop()
    }
    override fun onDestroy() {
        app.speech.status = {}
        if (!app.store.getBoolean("auto_read", false)) app.speech.stop()
        super.onDestroy()
    }
    override fun onNewIntent(intent: Intent) { super.onNewIntent(intent); setIntent(intent); debugIntent(intent) }
    @Deprecated("Legacy navigation for API 30")
    override fun onBackPressed() { if (!onHome) showHome() else super.onBackPressed() }

    private fun dp(value: Int) = (value * resources.displayMetrics.density).toInt()
    private fun text(value: String, size: Float = 14f, color: Int = Color.WHITE) = TextView(this).apply {
        this.text = value; textSize = size; setTextColor(color); gravity = Gravity.CENTER
        setPadding(dp(4), dp(5), dp(4), dp(5))
    }
    private fun button(label: String, action: () -> Unit) = Button(this).apply {
        text = label; textSize = 14f; isAllCaps = false; setTextColor(accent)
        minHeight = dp(48); setOnClickListener { action() }
        layoutParams = LinearLayout.LayoutParams(-1, -2)
    }
    private fun column() = LinearLayout(this).apply {
        orientation = LinearLayout.VERTICAL; gravity = Gravity.CENTER_HORIZONTAL
        setPadding(dp(26), dp(4), dp(26), dp(4))
    }
    private fun listenerAllowed(): Boolean {
        val component = ComponentName(this, DomaNotificationListener::class.java).flattenToString()
        return Settings.Secure.getString(contentResolver, "enabled_notification_listeners")
            ?.split(':')?.contains(component) == true
    }
    private fun lastNotification() = app.store.getString("last_notification", "").orEmpty()

    private fun showHome() {
        onHome = true; audioText = null
        setContentView(R.layout.activity_main)
        val list = findViewById<ListView>(R.id.main_list)
        val header = column()
        header.addView(text("DOMA", 20f, accent))
        audioText = text(app.audio.description(), 12f, accent).also(header::addView)
        header.addView(button("Falar comando", ::recognize))
        list.addHeaderView(header, null, false)
        val auto = app.store.getBoolean("auto_read", false)
        val labels = mutableListOf("Ler notificação", "Mensagens", "Alerta de segurança", "Instruções",
            "Repetir leitura", if (auto) "Pausar leitura automática" else "Ativar leitura automática",
            "Conectar Bluetooth", if (listenerAllowed()) "Acesso às notificações: ativo" else "Autorizar notificações",
            "Limpar última notificação")
        if (BuildConfig.DEBUG) labels.add("Teste por texto (debug)")
        val footer = column()
        statusText = text(commandReadout.ifBlank { if (auto) "Leitura automática ativa" else "Leitura automática pausada" }, 12f)
        footer.addView(statusText)
        footer.addView(button("Parar áudio") { app.speech.stop(); statusText.setText(R.string.audio_stopped) })
        list.addFooterView(footer, null, false)
        list.adapter = object : ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, labels) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View =
                (super.getView(position, convertView, parent) as TextView).apply {
                    setTextColor(Color.WHITE); textSize = 15f; gravity = Gravity.CENTER
                    minHeight = dp(52); setPadding(dp(26), dp(10), dp(26), dp(10))
                }
        }
        list.setOnItemClickListener { _, _, position, _ ->
            when (position - list.headerViewsCount) {
                0 -> dispatch(VoiceCommand.READ)
                1 -> dispatch(VoiceCommand.MESSAGE)
                2 -> dispatch(VoiceCommand.ALERT)
                3 -> dispatch(VoiceCommand.INSTRUCTIONS)
                4 -> dispatch(VoiceCommand.REPEAT)
                5 -> setAuto(!auto)
                6 -> bluetooth()
                7 -> authorizeNotifications()
                8 -> { app.store.edit().remove("last_notification").apply(); showHome(); statusText.setText(R.string.notification_cleared) }
                9 -> debugDialog()
            }
        }
    }
    private fun detail(title: String, content: String, read: Boolean = true) {
        onHome = false; audioText = null
        val scroll = ScrollView(this).apply { setBackgroundColor(ink); clipToPadding = false; setPadding(0, dp(32), 0, dp(36)) }
        val body = column()
        body.addView(text(title, 19f, accent))
        body.addView(text(content, 15f))
        statusText = text("", 12f); body.addView(statusText)
        body.addView(button("Ouvir novamente") { app.speech.speak(content) })
        body.addView(button("Parar áudio") { app.speech.stop(); statusText.setText(R.string.audio_stopped) })
        body.addView(button("Voltar", ::showHome))
        scroll.addView(body); setContentView(scroll)
        if (read) app.speech.speak(content)
    }
    internal fun dispatch(command: VoiceCommand) {
        when (command) {
            VoiceCommand.READ -> {
                val last = lastNotification()
                if (last.isNotBlank()) detail("Notificação", last)
                else detail("Notificações", if (listenerAllowed()) "Ainda não há notificação legível recebida."
                    else "Autorize o acesso às notificações no menu inicial.")
            }
            VoiceCommand.REPEAT -> app.speech.repeat()
            VoiceCommand.ALERT -> detail("Alerta de segurança", ALERT)
            VoiceCommand.INSTRUCTIONS -> detail("Instruções", INSTRUCTIONS)
            VoiceCommand.MESSAGE -> detail("Mensagem da Doma", MESSAGE)
            VoiceCommand.PAUSE -> setAuto(false)
            VoiceCommand.RESUME -> setAuto(true)
            VoiceCommand.BLUETOOTH -> bluetooth()
            VoiceCommand.UNKNOWN -> detail("Comando não reconhecido", "Diga: ler notificação, repetir, alerta de segurança, instruções ou pausar.")
        }
    }
    private fun setAuto(enabled: Boolean) {
        if (enabled && !listenerAllowed()) { authorizeNotifications(); return }
        app.store.edit().putBoolean("auto_read", enabled).apply()
        if (!enabled) app.speech.stop()
        commandReadout = if (enabled) "Leitura automática ativa" else "Leitura automática pausada"
        showHome()
        app.speech.speak(commandReadout)
    }
    private fun authorizeNotifications() {
        AlertDialog.Builder(this).setTitle("Leitura de notificações")
            .setMessage("O Doma Assist poderá ler o texto das notificações deste relógio. A leitura automática pode ser pausada. Apenas a última notificação é guardada no dispositivo.")
            .setPositiveButton("Autorizar") { _, _ -> openSettings(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)) }
            .setNegativeButton("Cancelar", null).show()
    }
    @android.annotation.SuppressLint("WearRecents") // Flags expressamente utilizadas no roteiro acadêmico.
    private fun bluetooth() {
        app.speech.stop()
        openSettings(Intent(Settings.ACTION_BLUETOOTH_SETTINGS).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            putExtra("EXTRA_CONNECTION_ONLY", true); putExtra("EXTRA_CLOSE_ON_CONNECT", true)
            putExtra("android.bluetooth.devicepicker.extra.FILTER_TYPE", 1)
        })
    }
    private fun openSettings(intent: Intent) {
        try { startActivity(intent) }
        catch (_: android.content.ActivityNotFoundException) {
            detail("Configurações", "Esta versão do Wear OS não oferece essa tela. As mensagens demonstrativas e as instruções continuam disponíveis no menu. Consulte o guia do projeto para a configuração de teste.", false)
        }
    }
    private fun recognize() {
        app.speech.stop()
        try {
            @Suppress("DEPRECATION")
            startActivityForResult(Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, "pt-BR")
                putExtra(RecognizerIntent.EXTRA_PROMPT, "Diga um comando para a Doma")
            }, 42)
        } catch (_: android.content.ActivityNotFoundException) {
            detail("Comando de voz", "Não há serviço de reconhecimento de voz disponível neste relógio. Use as opções do menu.", false)
        }
    }
    @Deprecated("Legacy voice activity result for API 30")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode != 42) return
        val phrase = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.firstOrNull()
        if (resultCode == RESULT_OK && !phrase.isNullOrBlank()) {
            commandReadout = "Comando: $phrase"; dispatch(CommandRouter.parse(phrase))
        } else statusText.setText(R.string.voice_empty)
    }
    private fun debugDialog() {
        if (!BuildConfig.DEBUG) return
        val entry = EditText(this).apply { hint = "Ex.: instruções"; setTextColor(Color.WHITE) }
        AlertDialog.Builder(this).setTitle("Transcrição simulada")
            .setMessage("Testa o comando por texto. Não testa o microfone.").setView(entry)
            .setPositiveButton("Executar") { _, _ -> dispatch(CommandRouter.parse(entry.text.toString())) }
            .setNeutralButton("Notificação demo") { _, _ -> postDemoNotification() }
            .setNegativeButton("Cancelar", null).show()
    }
    private fun debugIntent(intent: Intent?) {
        if (!BuildConfig.DEBUG) return
        intent?.getStringExtra("comandoTeste")?.let { dispatch(CommandRouter.parse(it)) }
        if (intent?.getBooleanExtra("notificacaoTeste", false) == true) postDemoNotification()
    }
    internal fun postDemoNotification() {
        if (!BuildConfig.DEBUG) return
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(NotificationChannel("doma_demo", "Demonstração acadêmica", NotificationManager.IMPORTANCE_DEFAULT))
        manager.notify(100, Notification.Builder(this, "doma_demo").setSmallIcon(R.drawable.ic_doma)
            .setContentTitle("Doma: treinamento").setContentText("Sua orientação de segurança começa às 14 horas.")
            .setStyle(Notification.BigTextStyle().bigText("Sua orientação de segurança começa às 14 horas. Dirija-se à sala de treinamento."))
            .build())
    }
    companion object {
        const val MESSAGE = "Mensagem demonstrativa da Doma: bem-vindo à equipe. Sua reunião de integração será às nove horas, na sala de treinamento."
        const val ALERT = "Alerta demonstrativo de segurança: mantenha a calma. Interrompa sua atividade e siga a rota de saída indicada. Procure o responsável pela segurança. Este é um exercício, não uma emergência real."
        const val INSTRUCTIONS = "Instruções de treinamento: primeiro, organize sua estação de trabalho. Segundo, confira os equipamentos de proteção. Terceiro, ouça a orientação do supervisor. Se precisar de ajuda, comunique-se com a equipe."
    }
}
