package br.com.doma.assist

import android.app.Notification
import android.content.Context
import android.content.pm.PackageManager
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import java.io.FileInputStream
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WearIntegrationTest {
    @Test fun runsOnWatchWithBuiltInAudio() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        assertTrue(context.packageManager.hasSystemFeature(PackageManager.FEATURE_WATCH))
        assertTrue(AudioHelper(context).speaker())
    }
    @Test fun extractsLongNotificationAndRejectsEmptyBody() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val long = Notification.Builder(context, "test").setContentTitle("Equipe")
            .setContentText("Resumo").setStyle(Notification.BigTextStyle().bigText("Instrução completa")).build()
        assertEquals("Equipe. Instrução completa", DomaNotificationListener.extract(long))
        assertNull(DomaNotificationListener.extract(Notification.Builder(context, "test").setContentTitle("Sem texto").build()))
    }
    @Test fun notificationActuallyPassesThroughAuthorizedSystemListener() {
        val app = ApplicationProvider.getApplicationContext<DomaApplication>()
        // A imagem Wear OS API 30 não expõe a tela de autorização. O acesso é
        // habilitado somente pelo processo de testes, nunca pelo aplicativo.
        val automation = InstrumentationRegistry.getInstrumentation().uiAutomation
        automation.executeShellCommand("cmd notification allow_listener br.com.doma.assist/br.com.doma.assist.DomaNotificationListener").use {
            FileInputStream(it.fileDescriptor).use { input -> input.readBytes() }
        }
        app.store.edit().remove("last_notification").putBoolean("auto_read", false).commit()
        ActivityScenario.launch(MainActivity::class.java).use { scenario ->
            scenario.onActivity { it.postDemoNotification() }
            val deadline = System.currentTimeMillis() + 10000
            while (app.store.getString("last_notification", "").isNullOrBlank() && System.currentTimeMillis() < deadline) Thread.sleep(100)
            assertTrue(app.store.getString("last_notification", "").orEmpty().contains("sala de treinamento"))
        }
    }
}
