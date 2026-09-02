package com.example.inventaryws.ui.composables

import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.inventaryws.core.composables.InventaryWSText

@Composable
fun InventaryStockOutlinedTextField(
    value: String,
    label: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true
) {
    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        singleLine = singleLine,
        label = {
            InventaryWSText(value = label)
        },
        placeholder = {
            InventaryWSText(value = placeholder)
        }
    )
}