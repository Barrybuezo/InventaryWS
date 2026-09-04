package com.example.inventaryws.ui.composables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.inventaryws.core.composables.InventaryWSText


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventaryWSTopAppBar(
    title: String,
    onBackPressed: () -> Unit
) {
    TopAppBar(
        title = {
            InventaryWSText(value = title, fontSize = 30.sp, fontWeight = FontWeight.Bold)
        },
        navigationIcon = {
            IconButton(onClick = onBackPressed) {
                Icon(imageVector = Icons.Default.ArrowBackIosNew, contentDescription = " Arrow Back")
            }
        }
    )
}