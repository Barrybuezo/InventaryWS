package com.example.inventaryws.core.composables

import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun InventaryWSText(
    value: String,
    modifier: Modifier = Modifier,
    color: Color = LocalContentColor.current,
    maxLines: Int = Int.MAX_VALUE,
    style: TextStyle = MaterialTheme.typography.bodyMedium
) {
    Text(
        text = value,
        modifier = modifier,
        color = color,
        maxLines = maxLines,
        style = style,
        overflow = TextOverflow.Ellipsis
    )
}