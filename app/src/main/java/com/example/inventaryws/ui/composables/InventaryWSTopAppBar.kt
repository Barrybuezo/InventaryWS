package com.example.inventaryws.ui.composables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.inventaryws.core.composables.InventaryWSText


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventaryWSTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit
) {
    TopAppBar(
        title = {
            InventaryWSText(value = title)
        },
        navigationIcon = {
            IconButton(onClick = onBackPressed) {
                Icon(imageVector = Icons.Default.ArrowBackIosNew, contentDescription = " Arrow Back")
            }
        }
    )
}