package com.example.inventaryws.core.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun IWSButtom(
    value: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    border: BorderStroke? = null,
    shape: Shape = MaterialTheme.shapes.medium,
    containerColor: Color = Color.Unspecified,
    contentColor: Color = Color.Unspecified
) {
    Button(
        modifier = modifier,
        shape = shape,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        border = border,
        contentPadding = PaddingValues(15.dp),
        onClick = onClick,
    ) {
        InventaryWSText(value = value, fontSize = 20.sp)
    }
}