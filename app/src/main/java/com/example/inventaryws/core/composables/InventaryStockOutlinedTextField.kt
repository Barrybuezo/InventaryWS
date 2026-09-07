package com.example.inventaryws.core.composables

import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun InventaryStockOutlinedTextField(
    value: String,
    label: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true,
    readOnly: Boolean = false,
    trailingIcon: @Composable (() -> Unit)? = null,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    leadingIcon: @Composable (() -> Unit)? = null,
) {
    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        readOnly = readOnly,
        trailingIcon = trailingIcon,
        singleLine = singleLine,
        label = {
            InventaryWSText(value = label)
        },
        placeholder = {
            InventaryWSText(value = placeholder)
        },
        maxLines = maxLines,
        minLines = minLines,
        leadingIcon = leadingIcon
    )
}