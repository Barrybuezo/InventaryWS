package com.example.inventaryws.home.ui

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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.inventaryws.R
import com.example.inventaryws.core.composables.IWSDropDown
import com.example.inventaryws.core.composables.InventaryStockOutlinedTextField
import com.example.inventaryws.core.composables.InventaryWSText
import com.example.inventaryws.core.domain.model.CreateProductCategory
import com.example.inventaryws.home.ui.composables.IWSProductItem
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(
    onProductClick: (String) -> Unit,
    onAddClick: () -> Unit,
    viewModel: HomeViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Agregar")
            }
        }
    ) { innerPadding ->
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
                item {
                    InventaryWSText(
                        value = stringResource(R.string.app_name),
                        style = MaterialTheme.typography.displaySmall
                    )
                }

                item {
                    IndicatorContent(
                        modifier = Modifier.fillMaxWidth(),
                        total = uiState.totalCount,
                        available = uiState.availableCount,
                        lowStock = uiState.lowStockCount
                    )
                }

                item {
                    SearchContent(
                        modifier = Modifier.fillMaxWidth(),
                        searchQuery = uiState.searchQuery,
                        onSearchQueryChanged = viewModel::onSearchQueryChanged,
                        selectedCategory = uiState.selectedCategory,
                        onCategoryChanged = viewModel::onCategoryChanged
                    )
                }

                items(
                    items = uiState.filteredProducts,
                    key = { product -> product.documentId }
                ) { product ->
                    IWSProductItem(
                        modifier = Modifier.fillMaxWidth(),
                        product = product,
                        onClick = { onProductClick(product.documentId)}
                    )
                }
            }
        }
    }
}

@Composable
fun SearchContent(
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    selectedCategory: String,
    onCategoryChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    // "Todos" + las mismas categorías que se usan al crear un producto,
    // así nunca se desincronizan las opciones entre las dos pantallas.
    val categoryOptions = remember {
        listOf(CATEGORY_ALL) + CreateProductCategory.entries
            .filter { it != CreateProductCategory.DEFAULT }
            .map { it.label }
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        InventaryStockOutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = searchQuery,
            label = "",
            placeholder = "Buscar producto...",
            onValueChange = onSearchQueryChanged,
            leadingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = null)
            }
        )

        IWSDropDown(
            options = categoryOptions,
            selected = selectedCategory,
            label = { it },
            onSelected = onCategoryChanged,
            fieldLabel = "Categoría",
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun IndicatorContent(
    total: Int,
    available: Int,
    lowStock: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        StockIndicatorBox(
            modifier = Modifier.weight(1f),
            title = "Total",
            value = total.toString(),
            color = Color(0xFF3B6EF5)
        )

        StockIndicatorBox(
            modifier = Modifier.weight(1f),
            title = "Disponible",
            value = available.toString(),
            color = Color(0xFF3BA558)
        )

        StockIndicatorBox(
            modifier = Modifier.weight(1f),
            title = "Stock bajo",
            value = lowStock.toString(),
            color = Color(0xFFE53935)
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