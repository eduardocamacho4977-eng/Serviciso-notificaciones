package com.example.serviciosynotificaciones

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat

/**
 * Actividad Principal - Guía SENA de Servicios y Notificaciones
 * Demuestra de forma independiente cada uno de los ejercicios de la guía.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var etNumero: EditText

    // Instancias de los BroadcastReceivers para los Ejercicios 7 y 8
    private val receptorCargador = ReceptorCargador()
    private val receptorModoAvion = ReceptorModoAvion()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Solicitud de permisos de notificación para Android 13+ (API 33+)
        solicitarPermisoNotificaciones()

        etNumero = findViewById(R.id.et_numero)

        // ----------------------------------------------------
        // SERVICIO BASE: REPRODUCCIÓN DE MÚSICA (FOREGROUND)
        // ----------------------------------------------------
        val btnArrancarMusica: Button = findViewById(R.id.btn_arrancar_musica)
        val btnDetenerMusica: Button = findViewById(R.id.btn_detener_musica)

        btnArrancarMusica.setOnClickListener {
            val intent = Intent(this, ServicioMusica::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent)
            } else {
                startService(intent)
            }
        }

        btnDetenerMusica.setOnClickListener {
            stopService(Intent(this, ServicioMusica::class.java))
        }

        // ----------------------------------------------------
        // EJERCICIO 1: SERVICIO CONTADOR
        // ----------------------------------------------------
        val btnIniciarContador: Button = findViewById(R.id.btn_iniciar_contador)
        btnIniciarContador.setOnClickListener {
            val intent = Intent(this, ServicioContador::class.java)
            startService(intent)
            Toast.makeText(this, "Revisa Logcat para ver el contador (1 al 10)", Toast.LENGTH_SHORT).show()
        }

        // ----------------------------------------------------
        // EJERCICIO 2 Y 4: SERVICIO OPERACIÓN (FACTORIAL Y CUADRADO)
        // ----------------------------------------------------
        val btnCalcularFactorial: Button = findViewById(R.id.btn_calcular_factorial)
        val btnCalcularCuadrado: Button = findViewById(R.id.btn_calcular_cuadrado)

        btnCalcularFactorial.setOnClickListener {
            val texto = etNumero.text.toString()
            if (texto.isEmpty()) {
                Toast.makeText(this, "Por favor ingresa un número", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val valInt = texto.toIntOrNull() ?: 0
            val intent = Intent(this, ServicioOperacion::class.java).apply {
                putExtra("numero", valInt)
                putExtra("operacion", "factorial")
            }
            startService(intent)
            Toast.makeText(this, "Calculando factorial en Logcat...", Toast.LENGTH_SHORT).show()
        }

        btnCalcularCuadrado.setOnClickListener {
            val texto = etNumero.text.toString()
            if (texto.isEmpty()) {
                Toast.makeText(this, "Por favor ingresa un número", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val valDouble = texto.toDoubleOrNull() ?: 0.0
            val intent = Intent(this, ServicioOperacion::class.java).apply {
                putExtra("numero_double", valDouble)
                putExtra("operacion", "cuadrado")
            }
            startService(intent)
            Toast.makeText(this, "Calculando cuadrado en Logcat...", Toast.LENGTH_SHORT).show()
        }

        // ----------------------------------------------------
        // EJERCICIO 3: SERVICIO DESCARGA
        // ----------------------------------------------------
        val btnIniciarDescarga: Button = findViewById(R.id.btn_iniciar_descarga)
        btnIniciarDescarga.setOnClickListener {
            val intent = Intent(this, ServicioDescarga::class.java)
            startService(intent)
            Toast.makeText(this, "Simulando descarga... Revisa Logcat", Toast.LENGTH_SHORT).show()
        }

        // ----------------------------------------------------
        // EJERCICIO 5: NOTIFICACIÓN CON BOTONES DE ACCIÓN
        // ----------------------------------------------------
        val btnNotificacionAcciones: Button = findViewById(R.id.btn_notificacion_acciones)
        btnNotificacionAcciones.setOnClickListener {
            mostrarNotificacionConAcciones()
        }

        // ----------------------------------------------------
        // EJERCICIO 6: NOTIFICACIÓN CON IMAGEN GRANDE (BigPictureStyle)
        // ----------------------------------------------------
        val btnNotificacionImagen: Button = findViewById(R.id.btn_notificacion_imagen)
        btnNotificacionImagen.setOnClickListener {
            mostrarNotificacionBigPicture()
        }

        // Registrar los BroadcastReceivers para Ejercicios 7 y 8
        registrarReceptores()
    }

    private fun solicitarPermisoNotificaciones() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    101,
                )
            }
        }
    }

    /**
     * Ejercicio 5: Notificación con botones "Ver detalles" y "Cancelar"
     */
    private fun mostrarNotificacionConAcciones() {
        val canalId = "canal_ejercicio_5"
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val canal = NotificationChannel(
                canalId,
                "Canal Ejercicio 5",
                NotificationManager.IMPORTANCE_DEFAULT,
            )
            notificationManager.createNotificationChannel(canal)
        }

        // Acción 1: Ver detalles (Abre DetalleActivity)
        val intentDetalle = Intent(this, DetalleActivity::class.java)
        val pendingIntentDetalle = PendingIntent.getActivity(
            this,
            0,
            intentDetalle,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
        )

        // Acción 2: Cancelar (Ejecuta ReceptorCancelarNotificacion)
        val intentCancelar = Intent(this, ReceptorCancelarNotificacion::class.java)
        val pendingIntentCancelar = PendingIntent.getBroadcast(
            this,
            1,
            intentCancelar,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
        )

        val notificacion = NotificationCompat.Builder(this, canalId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Ejercicio 5: Notificación")
            .setContentText("Selecciona una de las acciones disponibles:")
            .addAction(android.R.drawable.ic_menu_info_details, "Ver detalles", pendingIntentDetalle)
            .addAction(android.R.drawable.ic_menu_close_clear_cancel, "Cancelar", pendingIntentCancelar)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(2, notificacion)
    }

    /**
     * Ejercicio 6: Notificación con imagen grande mediante BigPictureStyle
     */
    private fun mostrarNotificacionBigPicture() {
        val canalId = "canal_ejercicio_6"
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val canal = NotificationChannel(
                canalId,
                "Canal Ejercicio 6",
                NotificationManager.IMPORTANCE_DEFAULT,
            )
            notificationManager.createNotificationChannel(canal)
        }

        // Decodificar una imagen de recursos (usando el icono de la app como ejemplo)
        val bitmapImagen = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)

        val estiloBigPicture = NotificationCompat.BigPictureStyle()
            .bigPicture(bitmapImagen)
            .setBigContentTitle("Imagen Grande (Ejercicio 6)")
            .setSummaryText("Vista ampliada de la notificación.")

        val notificacion = NotificationCompat.Builder(this, canalId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Ejercicio 6: Imagen Grande")
            .setContentText("Despliega la notificación para ver la imagen.")
            .setStyle(estiloBigPicture)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(3, notificacion)
    }

    /**
     * Registro dinámico de BroadcastReceivers para Ejercicios 7 y 8
     */
    private fun registrarReceptores() {
        // Ejercicio 7: ReceptorCargador
        val filtroCargador = IntentFilter().apply {
            addAction(Intent.ACTION_POWER_CONNECTED)
            addAction(Intent.ACTION_POWER_DISCONNECTED)
        }
        registerReceiver(receptorCargador, filtroCargador)

        // Ejercicio 8: ReceptorModoAvion
        val filtroModoAvion = IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED)
        registerReceiver(receptorModoAvion, filtroModoAvion)
    }

    override fun onDestroy() {
        super.onDestroy()
        // Desregistrar receptores para evitar fugas de memoria (memory leaks)
        try {
            unregisterReceiver(receptorCargador)
            unregisterReceiver(receptorModoAvion)
        } catch (_: Exception) {
        }
    }
}
