package com.example.serviciosynotificaciones

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat

/**
 * Servicio en segundo plano para reproducción de música (Foreground Service)
 * Guía de Aprendizaje SENA - Servicios y Notificaciones
 */
class ServicioMusica : Service() {

    private lateinit var reproductor: MediaPlayer

    companion object {
        const val CANAL_ID = "mi_canal"
        const val NOTIFICACION_ID = 1
    }

    override fun onCreate() {
        super.onCreate()
        Toast.makeText(this, "Servicio creado", Toast.LENGTH_SHORT).show()
        // Inicializar el reproductor con el archivo de audio alojado en res/raw/audio.wav
        reproductor = MediaPlayer.create(this, R.raw.audio).apply {
            isLooping = true
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        // 1. Crear el canal de notificación para Android 8.0 (API 26) o superior
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CANAL_ID,
                "Mis Notificaciones",
                NotificationManager.IMPORTANCE_DEFAULT,
            ).apply {
                description = "Descripción del canal"
            }
            notificationManager.createNotificationChannel(notificationChannel)
        }

        // 2. Verificar permisos de notificación para Android 13 (API 33) o superior
        if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) &&
            (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED)) {
            Toast.makeText(this, "No se tienen permisos para mostrar notificaciones", Toast.LENGTH_SHORT).show()
            return START_NOT_STICKY
        }

        // 3. Crear el PendingIntent para abrir MainActivity al presionar la notificación
        val intentPendiente = PendingIntent.getActivity(
            this,
            0,
            Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        // 4. Construir la notificación Foreground
        val notificacion = NotificationCompat.Builder(this, CANAL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Servicio de Música")
            .setContentText("Reproduciendo música en segundo plano.")
            .setContentIntent(intentPendiente)
            .build()

        // 5. Iniciar el servicio en primer plano (Foreground Service) y arrancar la música
        startForeground(NOTIFICACION_ID, notificacion)
        reproductor.start()

        Toast.makeText(this, "Servicio arrancado $startId", Toast.LENGTH_SHORT).show()
        return START_STICKY
    }

    override fun onDestroy() {
        Toast.makeText(this, "Servicio detenido", Toast.LENGTH_SHORT).show()
        if (::reproductor.isInitialized) {
            reproductor.stop()
            reproductor.release()
        }
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
