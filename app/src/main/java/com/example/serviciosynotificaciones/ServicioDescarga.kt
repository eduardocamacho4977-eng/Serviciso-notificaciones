package com.example.serviciosynotificaciones

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.widget.Toast

/**
 * Ejercicio 3: ServicioDescarga
 * Simula una descarga pesada de 10 archivos en segundo plano sin bloquear la UI.
 */
class ServicioDescarga : Service() {

    override fun onCreate() {
        super.onCreate()
        Toast.makeText(this, "Iniciando servicio de descarga...", Toast.LENGTH_SHORT).show()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Hilo secundario para simular la descarga paso a paso
        Thread {
            for (i in 1..10) {
                try {
                    Thread.sleep(1000) // Simular la descarga de 1 archivo (1 segundo)
                } catch (_: InterruptedException) {
                    break
                }
                Log.d("ServicioDescarga", "Archivo descargado: $i de 10")
            }
            Log.d("ServicioDescarga", "Descarga completada")
            stopSelf()
        }.start()

        return START_NOT_STICKY
    }

    override fun onDestroy() {
        Toast.makeText(this, "ServicioDescarga finalizado", Toast.LENGTH_SHORT).show()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
