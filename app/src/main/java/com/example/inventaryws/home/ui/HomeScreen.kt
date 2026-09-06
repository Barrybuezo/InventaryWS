package com.example.inventaryws.home.ui

import android.R.attr.data
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.inventaryws.R
import com.example.inventaryws.core.composables.IWSAsyncImage
import com.example.inventaryws.core.composables.InventaryWSText
import com.example.inventaryws.create.ui.composables.InventaryStockOutlinedTextField
import com.example.inventaryws.home.ui.composables.IWSProductItem
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Scaffold { innerPadding ->
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(innerPadding)
//        ) {
//            InventaryWSText(value = "Home")
//        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                //titulo
                item {
                    InventaryWSText(
                        value = stringResource(R.string.app_name),
                        style = MaterialTheme.typography.displaySmall
                    )
                }

                //indicadores
                item {
                    IndicatorContent(modifier = Modifier.fillMaxWidth())
                }

                //busqueda
                item {
                    SearchContent(modifier = Modifier)
                }

                //productos
                items(
                    items = uiState.products,
                    key = { product -> product.documentId }
                ) { product ->
                    IWSProductItem(
                        modifier = Modifier.fillMaxWidth(),
                        product = product
                    )
                }
            }
        }
    }
}

@Composable
fun SearchContent(modifier: Modifier.Companion) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        InventaryStockOutlinedTextField(
            value = "",
            label = "",
            placeholder = "Buscar producto...",
            onValueChange = {},
            leadingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = null)
            }
        )
    }
}

@Composable
fun IndicatorContent(modifier: Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        StockIndicatorBox(
            modifier = Modifier.weight(1f),
            title = "Total",
            value = "100",
            color = Color.Red
        )

        StockIndicatorBox(
            modifier = Modifier.weight(1f),
            title = "Disponible",
            value = "75",
            color = Color.Blue
        )

        StockIndicatorBox(
            modifier = Modifier.weight(1f),
            title = "Stock bajo",
            value = "25",
            color = Color.Green
        )
    }
}

@Composable
fun StockIndicatorBox(modifier: Modifier, title: String, value: String, color: Color) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .height(100.dp)
            .clip(shape = MaterialTheme.shapes.medium)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            InventaryWSText(value = title, color = color)
            InventaryWSText(value = value, color = color)
        }
    }
}