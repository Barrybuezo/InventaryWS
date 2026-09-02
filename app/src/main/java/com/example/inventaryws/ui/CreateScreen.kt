package com.example.inventaryws.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.inventaryws.ui.composables.InventaryStockOutlinedTextField
import com.example.inventaryws.ui.composables.InventaryWSTopAppBar
import org.koin.compose.viewmodel.koinViewModel

@Preview(showBackground = true)
@Composable
fun CreateScreenPreview() {
    CreateScreen()
}

@Composable
fun CreateScreen(viewModel: CreateViewModel = koinViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            InventaryWSTopAppBar(title = "Crear", onBackPressed = {})
        }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ){
            CreateScreenContent(
                name = uiState.name,
                onNameChanged = viewModel::onNameChanged,
                description = uiState.description,
                onDescriptionChanged = viewModel::onDescriptionChanged,
                amount = uiState.amount,
                onAmountChanged = viewModel::onAmountChanged,
                stock = uiState.stock,
                onStockChanged = viewModel::onStockChanged,
                minimumStock = uiState.stock,
                onMinimumStockChanged = viewModel::onMinimumStockChanged,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp))
        }


    }
}

@Composable
fun CreateScreenContent(
    name:String,
    onNameChanged: (String) -> Unit,
    description:String,
    onDescriptionChanged: (String) -> Unit,
    amount: String,
    onAmountChanged: (String) -> Unit,
    stock: String,
    onStockChanged: (String) -> Unit,
    minimumStock: String,
    onMinimumStockChanged: (String) -> Unit,
    modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        //name
        InventaryStockOutlinedTextField(
            value = name,
            onValueChange = onNameChanged,
            label = "Nombre",
            placeholder = "Ingresar nombre",
            modifier = Modifier.fillMaxWidth()
        )

        //description
        InventaryStockOutlinedTextField(
            value = description,
            onValueChange = onDescriptionChanged,
            label = "Descripción",
            placeholder = "Ingresar descripción",
            modifier = Modifier.fillMaxWidth()
        )

        //precio
        InventaryStockOutlinedTextField(
            value = amount,
            onValueChange = onAmountChanged,
            label = "Precio",
            placeholder = "0.00",
            modifier = Modifier.fillMaxWidth()
        )

        //existencia
        InventaryStockOutlinedTextField(
            value = stock,
            onValueChange = onStockChanged,
            label = "Stock",
            placeholder = "0",
            modifier = Modifier.fillMaxWidth()
        )

        //existencia minima
        InventaryStockOutlinedTextField(
            value = minimumStock,
            onValueChange = onMinimumStockChanged,
            label = "Descripción",
            placeholder = "Ingresar descripción",
            modifier = Modifier.fillMaxWidth()
        )
    }
}