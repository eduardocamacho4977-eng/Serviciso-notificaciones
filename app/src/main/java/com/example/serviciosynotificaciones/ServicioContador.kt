package com.example.serviciosynotificaciones

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.widget.Toast

/**
 * Ejercicio 1: ServicioContador
 * Ejecuta un hilo en segundo plano que cuenta del 1 al 10 sin bloquear la interfaz.
 */
class ServicioContador : Service() {

    private var hiloContador: Thread? = null
    @Volatile
    private var ejecutando = false

    override fun onCreate() {
        super.onCreate()
        Toast.makeText(this, "ServicioContador Creado", Toast.LENGTH_SHORT).show()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Toast.makeText(this, "ServicioContador Iniciado", Toast.LENGTH_SHORT).show()
        ejecutando = true

        // Iniciar un hilo secundario para no bloquear la interfaz de usuario (UI Thread)
        hiloContador = Thread {
            for (i in 1..10) {
                if (!ejecutando) break
                Log.d("ServicioContador", "Valor: $i")
                try {
                    Thread.sleep(1000) // Pausa de 1 segundo entre cada número
                } catch (_: InterruptedException) {
                    Log.d("ServicioContador", "Hilo interrumpido")
                    break
                }
            }
            Log.d("ServicioContador", "Conteo finalizado")
            stopSelf() // Autodetener el servicio al finalizar la tarea
        }
        hiloContador?.start()

        return START_NOT_STICKY
    }

    override fun onDestroy() {
        ejecutando = false
        hiloContador?.interrupt() // Detener el hilo correctamente
        Toast.makeText(this, "ServicioContador Detenido", Toast.LENGTH_SHORT).show()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
