package com.example.inventaryws.create.utils

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.example.inventaryws.core.composables.CategoryDropDown
import com.example.inventaryws.core.composables.IWSButtom
import com.example.inventaryws.core.composables.IWSButtomWithIcon
import com.example.inventaryws.core.composables.InventaryWSText
import com.example.inventaryws.core.domain.model.CreateProductCategory
import com.example.inventaryws.core.util.image
import com.example.inventaryws.create.data.utils.createImageCaptureUri
import com.example.inventaryws.core.composables.InventaryStockOutlinedTextField
import com.example.inventaryws.create.ui.composables.InventaryWSTopAppBar
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
    ) { success ->
        if (success) {
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
                category = uiState.category,
                onCategoryChanged = viewModel::onCategory,
                amount = uiState.amount,
                onAmountChanged = viewModel::onAmountChanged,
                stock = uiState.stock,
                onStockChanged = viewModel::onStockChanged,
                minimumStock = uiState.minimumStock,
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
    category: CreateProductCategory,
    onCategoryChanged: (CreateProductCategory) -> Unit,
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
            singleLine = false,
            minLines = 2,
            maxLines = 2,
            modifier = modifier.fillMaxWidth()
        )

        CategoryDropDown(
            selectedCategory = category,
            onCategorySelected = onCategoryChanged,
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

        StockConten(
            stock = stock,
            onStockChanged = onStockChanged,
            minimumStock = minimumStock,
            onMinimumStockChanged = onMinimumStockChanged,
            modifier = Modifier.fillMaxWidth()
        )

        ProductImageSection(
            imageUri = imageUri,
            onCameraClicked = onCameraClicked,
            onGalleryClicked = onGalleryClicked,
            modifier = Modifier.weight(1f)
        )

        //Boton para guardar
        IWSButtom(
            value = "Guardar",
            onClick = onSaveCliked,
            containerColor = Color(0xFF3BA558),
            contentColor = Color.White,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
fun ImageContentSelector(
    onCameraClicked: () -> Unit,
    onGalleryClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        //Boton camara
        IWSButtomWithIcon(
            value = "Camara",
            imageVector = Icons.Default.Camera,
            onClick = onCameraClicked,
            containerColor = Color.White,
            contentColor = Color.Blue,
            border = BorderStroke(width = 1.dp, color = Color.Gray.copy(alpha = 0.5f)),
            modifier = modifier.weight(1f),
        )

        //Boton gallery
        IWSButtomWithIcon(
            value = "Galleria",
            imageVector = Icons.Default.Image,
            onClick = onGalleryClicked,
            containerColor = Color.White,
            contentColor = Color.Blue,
            border = BorderStroke(width = 1.dp, color = Color.Gray.copy(alpha = 0.5f)),
            modifier = modifier.weight(1f)
        )
    }

}

@Composable
fun StockConten(
    stock: String,
    onStockChanged: (String) -> Unit,
    minimumStock: String,
    onMinimumStockChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        //existencia
        InventaryStockOutlinedTextField(
            value = stock,
            onValueChange = onStockChanged,
            label = "Stock",
            placeholder = "0",
            modifier = modifier.weight(1f) //weight(1f)
        )

        //existencia minima
        InventaryStockOutlinedTextField(
            value = minimumStock,
            onValueChange = onMinimumStockChanged,
            label = "Stock minimo",
            placeholder = "0",
            modifier = modifier.weight(1f)
        )
    }

}




@Composable
fun ProductImageSection(
    imageUri: Uri,
    onCameraClicked: () -> Unit,
    onGalleryClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        InventaryWSText(
            value = "Imagen del producto",
            modifier = Modifier.fillMaxWidth()
        )

        // Botones de cámara y galería
        ImageContentSelector(
            onCameraClicked = onCameraClicked,
            onGalleryClicked = onGalleryClicked,
            modifier = Modifier.fillMaxWidth()
        )

        if (imageUri == Uri.EMPTY) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(MaterialTheme.shapes.medium)
                    .background(Color.Gray.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = image,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(140.dp)
                )
            }
        } else {
            AsyncImage(
                model = imageUri,
                contentDescription = "Foto del producto",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(MaterialTheme.shapes.medium)
            )
        }
    }
}