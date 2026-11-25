package com.example.serviciosynotificaciones

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.widget.Toast

/**
 * Ejercicio 2 y 4: ServicioOperacion
 * Recibe información desde MainActivity mediante Intent y realiza operaciones
 * matemáticas (factorial o cuadrado) en un hilo secundario.
 */
class ServicioOperacion : Service() {

    override fun onCreate() {
        super.onCreate()
        Toast.makeText(this, "ServicioOperacion Creado", Toast.LENGTH_SHORT).show()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Toast.makeText(this, "ServicioOperacion Iniciado", Toast.LENGTH_SHORT).show()

        // Recibir los datos enviados desde MainActivity
        val numeroInt = intent?.getIntExtra("numero", 0) ?: 0
        val numeroDouble = intent?.getDoubleExtra("numero_double", 0.0) ?: 0.0
        val operacion = intent?.getStringExtra("operacion") ?: "factorial"

        // Ejecutar el cálculo en un hilo secundario
        Thread {
            if ((operacion == "cuadrado") || (intent?.hasExtra("numero_double") == true)) {
                // Ejercicio 4: Calcular el cuadrado de un número
                val cuadrado = numeroDouble * numeroDouble
                Log.d("ServicioOperacion", "El cuadrado de $numeroDouble es: $cuadrado")
            } else {
                // Ejercicio 2: Calcular el factorial de un número entero
                val factorial = calcularFactorial(numeroInt)
                Log.d("ServicioOperacion", "El factorial de $numeroInt es: $factorial")
            }
            stopSelf() // Detener el servicio al finalizar el cálculo
        }.start()

        return START_NOT_STICKY
    }

    private fun calcularFactorial(n: Int): Long {
        var resultado = 1L
        for (i in 1..n) {
            resultado *= i
        }
        return resultado
    }

    override fun onDestroy() {
        Toast.makeText(this, "ServicioOperacion Detenido", Toast.LENGTH_SHORT).show()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
