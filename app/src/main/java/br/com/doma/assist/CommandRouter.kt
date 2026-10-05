package br.com.doma.assist

import java.text.Normalizer
import java.util.Locale

enum class VoiceCommand { READ, REPEAT, ALERT, INSTRUCTIONS, MESSAGE, PAUSE, RESUME, BLUETOOTH, UNKNOWN }

object CommandRouter {
    fun parse(text: String): VoiceCommand {
        val key = Normalizer.normalize(text.lowercase(Locale.ROOT), Normalizer.Form.NFD)
            .replace(Regex("\\p{M}+"), "").replace(Regex("[^a-z0-9 ]"), " ")
            .replace(Regex("\\s+"), " ").trim()
        return when (key) {
            "ler notificacao", "ler notificacoes", "notificacao", "leia a notificacao" -> VoiceCommand.READ
            "repetir", "repita", "repetir leitura" -> VoiceCommand.REPEAT
            "alerta", "alerta de seguranca", "seguranca", "emergencia" -> VoiceCommand.ALERT
            "instrucoes", "ler instrucoes", "treinamento" -> VoiceCommand.INSTRUCTIONS
            "mensagem", "ler mensagem", "mensagens" -> VoiceCommand.MESSAGE
            "pausar", "pausar leitura", "parar" -> VoiceCommand.PAUSE
            "retomar", "ativar leitura", "continuar" -> VoiceCommand.RESUME
            "bluetooth", "conectar fone" -> VoiceCommand.BLUETOOTH
            else -> VoiceCommand.UNKNOWN
        }
    }
}
