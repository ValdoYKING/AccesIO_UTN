@file:Suppress("UNUSED_EXPRESSION")

package com.pixelfusion.accesio_utn.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun OutlinedTextFormGeneraQR(value: String, onValueC: String, label: String) {
    OutlinedTextField(
        value = value,
        onValueChange = { onValueC },
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        maxLines = 1,
    )
}