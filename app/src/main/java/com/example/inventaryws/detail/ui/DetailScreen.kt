package com.example.inventaryws.detail.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.inventaryws.core.composables.IWSAsyncImage
import com.example.inventaryws.core.composables.IWSButtom
import com.example.inventaryws.core.composables.InventaryWSText
import com.example.inventaryws.core.composables.InventaryWSTopAppBar
import com.example.inventaryws.core.domain.model.Product
import com.example.inventaryws.home.domain.model.StockStatus
import com.example.inventaryws.home.domain.model.stockStatus
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun DetailScreen(
    productId: String,
    navigateToHome: () -> Unit,
    viewModel: DetailViewModel = koinViewModel(
        key = "detail_$productId"
    ) {
        parametersOf(productId)
    },
    navigateToEditScreen: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    //al borrar se regresa a home
    LaunchedEffect(uiState.detail) {
        if (uiState.detail is DetailEvent.Deleted) {
            navigateToHome()
        }
    }

    Scaffold(
        topBar = {
            InventaryWSTopAppBar(
                title = "Detalle",
                onBackPressed = navigateToHome
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(innerPadding)
        ) {
            when (val content = uiState.detail) {
                is DetailEvent.Detail -> {
                    DetailScreenContent(
                        data = content.productDetail,
                        onEditCliked = navigateToEditScreen,
                        onDeleteConfirmed = { viewModel.deleteProduct() },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                is DetailEvent.Error -> {
                    InventaryWSText(value = "Ocurrió un error al cargar el proudcto")
                }

                else -> Unit
            }

            if (uiState.isLoading || uiState.isDeleting) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }


        }
    }
}

@Composable
fun DetailScreenContent(
    modifier: Modifier,
    data: Product,
    onEditCliked: (String) -> Unit,
    onDeleteConfirmed: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        IWSAsyncImage(
            data = data.imgUrl,
            contentDescription = "Foto del producto",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .clip(MaterialTheme.shapes.medium)
        )
        InventaryWSText(
            value = data.name,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 12.dp)
        )

        CategoryBadge(category = data.category)

        InventaryWSText(
            value = "Descripción",
            style = MaterialTheme.typography.labelLarge,
            color = Color.Gray,
            modifier = Modifier.padding(top = 12.dp)
        )
        InventaryWSText(value = data.description)

        Column(
            modifier = Modifier.padding(top = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            DetailRow(label = "Precio", value = "Q%.2f".format(data.amount))
            DetailRow(label = "Existencia", value = data.stock.toString())
            DetailRow(label = "Stock mínimo", value = data.minimumStock.toString())

            val status = data.stockStatus()
            val (stateText, stateColor) = when (status) {
                StockStatus.LOW -> "Stock bajo" to Color(0xFFE53935)
                else -> "Disponible" to Color(0xFF3BA558)
            }
            DetailRow(label = "Estado", value = stateText, valueColor = stateColor)
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            IWSButtom(
                value = "Editar",
                onClick = { onEditCliked(data.documentId) },
                containerColor = Color(0xFF2E5FDC),
                contentColor = Color.White,
                modifier = Modifier.weight(1f),
            )

            IWSButtom(
                value = "Eliminar",
                onClick = { showDeleteDialog = true },
                containerColor = Color(0xFFE53935),
                contentColor = Color.White,
                modifier = Modifier.weight(1f),
            )
        }
    }

    if (showDeleteDialog) {
        DeleteConfirmationDialog(
            onConfirm = {
                showDeleteDialog = false
                onDeleteConfirmed()
            },
            onDismiss = { showDeleteDialog = false }
        )
    }
}

@Composable
fun DetailRow(
    label: String,
    value: String,
    valueColor: Color = Color.Unspecified
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        InventaryWSText(value = label, color = Color.Gray)
        InventaryWSText(value = value, color = valueColor, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun CategoryBadge(category: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20))
            .background(Color(0xFFDCEEFF))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        InventaryWSText(
            value = category,
            color = Color(0xFF2E5FDC),
            style = MaterialTheme.typography.labelSmall
        )
    }
}

@Composable
fun DeleteConfirmationDialog(onConfirm: () -> Unit, onDismiss: () -> Unit, ) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Filled.DeleteForever,
                contentDescription = null,
                tint = Color(0xFFE53935)
            )
        },
        title = {
            InventaryWSText(
                value = "¿Desea eliminar este producto?",
                fontWeight = FontWeight.Bold
            )
        },
        text = { InventaryWSText(value = "Esta acción no se puede deshacer", color = Color.Gray) },
        confirmButton = {
            IWSButtom(
                value = "Eliminar",
                onClick = onConfirm,
                containerColor = Color(0xFFE53935),
                contentColor = Color.White
            )
        },
        dismissButton = {
            IWSButtom(
                value = "Cancelar",
                onClick = onDismiss,
                containerColor = Color.LightGray,
                contentColor = Color.Black
            )
        }
    )
}