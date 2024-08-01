package com.pixelfusion.accesio_utn.viewmodel

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.os.CancellationSignal
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.print.PageRange
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintDocumentInfo
import android.print.PrintManager
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.pixelfusion.accesio_utn.helper.FechaATexto
import com.pixelfusion.accesio_utn.model.QrEstudianteAsisteModel
import com.pixelfusion.accesio_utn.model.UsuarioData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.File

class ListAssistUsersQrViewModel : ViewModel() {
    private lateinit var auth: FirebaseAuth
    private val database = Firebase.database.reference

    private val _AsistenciaUserList =
        mutableStateOf<List<Pair<String, QrEstudianteAsisteModel>>>(emptyList())
    val AsistenciaUserList: List<Pair<String, QrEstudianteAsisteModel>> get() = _AsistenciaUserList.value

    private val _UserDetailsMap = mutableStateOf<Map<String, UsuarioData>>(emptyMap())
    val UserDetailsMap: Map<String, UsuarioData> get() = _UserDetailsMap.value

    private val _isLoading = mutableStateOf(false)
    val isLoading: Boolean get() = _isLoading.value

    init {
        auth = FirebaseAuth.getInstance()
    }

    fun fetchData(UidQrAssistencia: String?) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                if (UidQrAssistencia != null) {
                    val result = withContext(Dispatchers.IO) {
                        val snapshot = database.child("qr_estudiante_asist")
                            .orderByChild("uid_qr_asistencia")
                            .equalTo(UidQrAssistencia)
                            .get()
                            .await()

                        snapshot.children.mapNotNull { data ->
                            val asistenciaUser = data.getValue(QrEstudianteAsisteModel::class.java)
                            asistenciaUser?.let {
                                Pair(data.key ?: "", it)
                            }
                        }
                    }

                    _AsistenciaUserList.value = result
                    fetchUserDetails(result.map { it.second.uid_user })
                }
            } catch (e: Exception) {
                println("Error ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun fetchUserDetails(uidUserList: List<String>) {
        viewModelScope.launch {
            try {
                val userDetailsMap = uidUserList.associateWith { uidUser ->
                    withContext(Dispatchers.IO) {
                        val userSnapshot = database.child("users")
                            .child(uidUser)
                            .get()
                            .await()
                        userSnapshot.getValue(UsuarioData::class.java)
                    }
                }.filterValues { it != null } as Map<String, UsuarioData>

                _UserDetailsMap.value = userDetailsMap
            } catch (e: Exception) {
                println("Error fetching user details: ${e.message}")
            }
        }
    }
}
