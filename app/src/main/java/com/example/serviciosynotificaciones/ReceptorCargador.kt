package com.example.serviciosynotificaciones

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

/**
 * Ejercicio 7: ReceptorCargador
 * BroadcastReceiver para detectar el estado de conexión del cargador del dispositivo.
 */
class ReceptorCargador : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            Intent.ACTION_POWER_CONNECTED -> {
                Toast.makeText(context, "Cargador CONECTADO", Toast.LENGTH_SHORT).show()
            }
            Intent.ACTION_POWER_DISCONNECTED -> {
                Toast.makeText(context, "Cargador DESCONECTADO", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
