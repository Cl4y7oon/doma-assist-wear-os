package br.com.doma.assist
import org.junit.Assert.assertEquals
import org.junit.Test
class CommandRouterTest {
    @Test fun acceptsAccentsCaseAndPunctuation() {
        assertEquals(VoiceCommand.READ, CommandRouter.parse("  LER NOTIFICAÇÃO!  "))
        assertEquals(VoiceCommand.INSTRUCTIONS, CommandRouter.parse("Instruções"))
        assertEquals(VoiceCommand.ALERT, CommandRouter.parse("alerta de segurança"))
    }
    @Test fun rejectsUnknownAndEmptyCommands() {
        assertEquals(VoiceCommand.UNKNOWN, CommandRouter.parse(""))
        assertEquals(VoiceCommand.UNKNOWN, CommandRouter.parse("apagar tudo"))
        assertEquals(VoiceCommand.UNKNOWN, CommandRouter.parse("não quero alerta"))
    }
    @Test fun distinguishesPauseFromResume() {
        assertEquals(VoiceCommand.PAUSE, CommandRouter.parse("pausar leitura"))
        assertEquals(VoiceCommand.RESUME, CommandRouter.parse("retomar"))
        assertEquals(VoiceCommand.REPEAT, CommandRouter.parse("repetir"))
    }
}
