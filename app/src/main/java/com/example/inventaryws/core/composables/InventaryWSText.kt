package com.example.inventaryws.core.composables

import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnit.Companion

@Composable
fun InventaryWSText(
    value: String,
    modifier: Modifier = Modifier,
    color: Color = LocalContentColor.current,
    maxLines: Int = Int.MAX_VALUE,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontWeight: FontWeight? = null,
    style: TextStyle = MaterialTheme.typography.bodyMedium
) {
    Text(
        text = value,
        modifier = modifier,
        color = color,
        maxLines = maxLines,
        fontSize = fontSize,
        fontWeight = fontWeight,
        style = style,
        overflow = TextOverflow.Ellipsis
    )
}