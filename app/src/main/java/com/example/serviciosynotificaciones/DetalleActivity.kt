package com.example.serviciosynotificaciones

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * Ejercicio 5: DetalleActivity
 * Actividad que se abre al pulsar la acción "Ver detalles" de la notificación.
 */
class DetalleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle)
    }
}
