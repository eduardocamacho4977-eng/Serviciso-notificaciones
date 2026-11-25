package com.example.serviciosynotificaciones

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

/**
 * Ejercicio 5: ReceptorCancelarNotificacion
 * Cierra la notificación activa al pulsar la acción "Cancelar".
 */
class ReceptorCancelarNotificacion : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            val notificationManager = it.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(2) // Cancela la notificación con ID = 2
            Toast.makeText(it, "Notificación cancelada", Toast.LENGTH_SHORT).show()
        }
    }
}
