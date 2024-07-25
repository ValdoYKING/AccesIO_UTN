package com.pixelfusion.accesio_utn.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseException
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.pixelfusion.accesio_utn.model.AccessUserModel
import com.pixelfusion.accesio_utn.model.QrAsistenciaModel
import com.pixelfusion.accesio_utn.model.QrEstudianteAsisteModel
import com.pixelfusion.accesio_utn.model.UsuarioData
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class ScanQRAssistViewModel : ViewModel() {
    var state by mutableStateOf(QrEstudianteAsisteModel())
        private set

    private lateinit var auth: FirebaseAuth
    private val database = Firebase.database.reference

    var user by mutableStateOf(UsuarioData())
        private set

    var asistencia by mutableStateOf(QrAsistenciaModel())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var isLoadingDialog by mutableStateOf(false)
        private set

    var isUserDetailDialogVisible by mutableStateOf(false)
        private set

    init {
        auth = FirebaseAuth.getInstance()
    }

    fun updateQrCodeContent(result: String, latitude: Double, longitude: Double) {
        // Actualiza el estado con los datos del QR y las coordenadas
        isLoading = true
        //val regex = """[a-zA-Z0-9]+""".toRegex()
        val regex = """[a-zA-Z0-9-_]+""".toRegex()
        val matchResult = regex.find(result)
        if (matchResult != null) {
            val uid_qr_asistencia = matchResult.value
            val uid_user = auth.currentUser?.uid ?: ""
            val currentTime = System.currentTimeMillis()
            val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
            val fecha = dateFormat.format(currentTime)
            val hora = timeFormat.format(currentTime)

            val asistenciaModel = QrEstudianteAsisteModel(
                uid_qr_asistencia = uid_qr_asistencia,
                uid_user = uid_user,
                fecha = fecha,
                hora = hora,
                latitude = latitude,
                longitude = longitude
            )
            state = asistenciaModel
            // Guardar datos en Firebase
            saveAttendanceData(asistenciaModel)
            fetchAsistenciaData(uid_qr_asistencia)
        } else {
            println("El formato del QR no coincide con el esperado")
        }
        isLoading = false
    }

    private fun saveAttendanceData(asistenciaModel: QrEstudianteAsisteModel) {
        val newAsistenciaUserRef = database.child("qr_estudiante_asist").push()
        newAsistenciaUserRef.setValue(asistenciaModel)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Mostrar diálogo con los detalles de la asistencia
                    isUserDetailDialogVisible = true
                } else {
                    // Manejar error
                }
            }
    }

    fun fetchAsistenciaData(id_asistencia: String) {
        isLoading = true
        val asistenciasRef = database.child("qr_asistencia").child(id_asistencia)
        asistenciasRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                try {
                    val asistenciaData = dataSnapshot.getValue(QrAsistenciaModel::class.java)
                    if (asistenciaData != null) {
                        asistencia = asistenciaData
                        isLoadingDialog = true

                        // Iniciar una corrutina para cerrar el indicador de progreso después de 2 segundos
                        viewModelScope.launch {
                            delay(1000L)
                            isLoadingDialog = false
                            isUserDetailDialogVisible = true

                            // Cerrar el AlertDialog después de otros 2 segundos
                            delay(10000L)
                            isUserDetailDialogVisible = false
                        }
                    } else {
                        println("No se encontraron datos para la asistencia con id: $id_asistencia")
                    }
                } catch (e: DatabaseException) {
                    println("Error al deserializar los datos de la asistencia: ${e.message}")
                } finally {
                    isLoading = false
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Manejar error
            }
        })
    }

    fun dismissUserDetailDialog() {
        isUserDetailDialogVisible = false
    }
}