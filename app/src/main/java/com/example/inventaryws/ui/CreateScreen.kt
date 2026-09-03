package com.example.inventaryws.ui

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.example.inventaryws.R
import com.example.inventaryws.core.composables.InventaryWSText
import com.example.inventaryws.create.data.utils.createImageCaptureUri
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
    val context = LocalContext.current
    var pendingCameraUri by remember { mutableStateOf<Uri?>(null) }

    //Galery
    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let { image -> viewModel.onImageSelected(image) }
    }

    //Vamara
    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ){ success ->
        if(success){
            pendingCameraUri?.let { viewModel.onImageSelected(it) }
        }

    }

    LaunchedEffect(uiState.content) {
        when (val content = uiState.content) {
            is CreateContent.Success -> {

                Log.d("InventaryWSLog", "CreateScreen: Success Executed")
            }

            is CreateContent.Error -> {
                Log.d("InventaryWSLog", "CreateScreen: Error Executed${content.message}")
            }

            else -> Unit

        }
    }

    Scaffold(
        topBar = {
            InventaryWSTopAppBar(title = "Crear", onBackPressed = {})
        }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
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
                imageUri = uiState.localUri,
                onSaveCliked = viewModel::saveProduct,
                onCameraClicked = {
                    val uri = context.createImageCaptureUri()
                    pendingCameraUri = uri
                    cameraLauncher.launch(uri)
                },
                onGalleryClicked = {
                    galleryLauncher.launch(
                        PickVisualMediaRequest(
                            ActivityResultContracts.PickVisualMedia.ImageOnly
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
fun CreateScreenContent(
    name: String,
    onNameChanged: (String) -> Unit,
    description: String,
    onDescriptionChanged: (String) -> Unit,
    amount: String,
    onAmountChanged: (String) -> Unit,
    stock: String,
    onStockChanged: (String) -> Unit,
    minimumStock: String,
    onMinimumStockChanged: (String) -> Unit,
    imageUri: Uri,
    onCameraClicked: () -> Unit,
    onGalleryClicked: () -> Unit,
    onSaveCliked: () -> Unit,
    modifier: Modifier = Modifier
    ) {


    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        //name
        InventaryStockOutlinedTextField(
            value = name,
            onValueChange = onNameChanged,
            label = "Nombre",
            placeholder = "Ingresar nombre",
            modifier = modifier.fillMaxWidth()
        )

        //description
        InventaryStockOutlinedTextField(
            value = description,
            onValueChange = onDescriptionChanged,
            label = "Descripción",
            placeholder = "Ingresar descripción",
            modifier = modifier.fillMaxWidth()
        )

        //precio
        InventaryStockOutlinedTextField(
            value = amount,
            onValueChange = onAmountChanged,
            label = "Precio",
            placeholder = "0.00",
            modifier = modifier.fillMaxWidth()
        )

        //existencia
        InventaryStockOutlinedTextField(
            value = stock,
            onValueChange = onStockChanged,
            label = "Stock",
            placeholder = "0",
            modifier = modifier.fillMaxWidth()
        )

        //existencia minima
        InventaryStockOutlinedTextField(
            value = minimumStock,
            onValueChange = onMinimumStockChanged,
            label = "Stock minimo",
            placeholder = "0",
            modifier = modifier.fillMaxWidth()
        )

        AsyncImage(
            model = imageUri, //String de la Uri que Coil convierte
            contentDescription = "Foto del producto",
            contentScale = ContentScale.Crop,
            error = painterResource(R.drawable.ic_launcher_background),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clip(shape = MaterialTheme.shapes.medium)
                .background(color = Color.Gray.copy(alpha = 0.3f))
        )


        //Boton camara
        Button(onClick = onCameraClicked, Modifier.fillMaxWidth()) {
            InventaryWSText(value = "Camera")
        }

        //Boton galeria
        Button(onClick = onGalleryClicked, Modifier.fillMaxWidth()) {
            InventaryWSText(value = "Galeria")
        }

        //Boton para guardar
        Button(onClick = onSaveCliked, Modifier.fillMaxWidth()) {
            InventaryWSText(value = "Guardar")
        }
    }
}