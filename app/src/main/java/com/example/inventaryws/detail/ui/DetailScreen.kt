package com.example.inventaryws.detail.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.inventaryws.core.composables.IWSButtomWithIcon
import com.example.inventaryws.core.composables.InventaryWSText
import com.example.inventaryws.core.composables.InventaryWSTopAppBar
import com.example.inventaryws.core.domain.model.Product
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
    navigateToEditScreen:(String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Scaffold(
        topBar = {
            InventaryWSTopAppBar(
                title = "Detail",
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
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                is DetailEvent.Error -> {}
                else -> Unit
            }


        }
    }
}

@Composable
fun DetailScreenContent(
    modifier: Modifier,
    data: Product,
    onEditCliked: (String) -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        InventaryWSText(value = data.name)
        InventaryWSText(value = data.category)
        InventaryWSText(value = data.amount.toString())
        InventaryWSText(value = data.stock.toString())
        InventaryWSText(value = data.minimumStock.toString())
        InventaryWSText(value = data.stockStatus().toString())

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            IWSButtomWithIcon(
                value = "Editar",
                onClick = {onEditCliked(data.documentId) },
                containerColor = Color.White,
                contentColor = Color.Blue,
                modifier = modifier.weight(1f),
            )

            IWSButtomWithIcon(
                value = "Eliminar",
                onClick = { },
                containerColor = Color.White,
                contentColor = Color.Blue,
                modifier = modifier.weight(1f),
            )
        }
    }
}