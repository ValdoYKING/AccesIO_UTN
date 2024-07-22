package com.pixelfusion.accesio_utn.helper

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices

// Función para obtener la ubicación del usuario
fun getLocation(context: android.content.Context, callback: (Location?) -> Unit) {
    val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    if (ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        fusedLocationProviderClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                // La ubicación se obtuvo exitosamente
                callback(location)
            }
            .addOnFailureListener { exception ->
                // Error al obtener la ubicación
                callback(null)
            }
    } else {
        callback(null)
    }
}