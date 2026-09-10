package com.example.inventaryws.create.ui

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Image
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
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusRestorer
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.example.inventaryws.core.composables.IWSDropDown
import com.example.inventaryws.core.domain.model.CreateProductCategory
import com.example.inventaryws.core.composables.IWSButtom
import com.example.inventaryws.core.composables.IWSButtomWithIcon
import com.example.inventaryws.core.composables.InventaryWSText
import com.example.inventaryws.core.util.image
import com.example.inventaryws.create.data.utils.createImageCaptureUri
import com.example.inventaryws.core.composables.InventaryStockOutlinedTextField
import com.example.inventaryws.core.composables.InventaryWSTopAppBar
import com.example.inventaryws.core.util.formImeAction
import com.example.inventaryws.core.util.formKeyboardActions
import com.example.inventaryws.core.util.rememberFocusRequesters
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CreateScreen(
    viewModel: CreateViewModel = koinViewModel()
) {
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
            InventaryWSTopAppBar(title = "Crear", onBackPressed = { })
        }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
        ) {
            CreateScreenContent(
                name = uiState.name,
                onNameChanged = viewModel::onNameChanged,
                nameError = uiState.nameError,
                description = uiState.description,
                onDescriptionChanged = viewModel::onDescriptionChanged,
                descriptionError = uiState.descriptionError,
                category = uiState.category,
                onCategoryChanged = viewModel::onCategory,
                categoryError = uiState.categoryError,
                amount = uiState.amount,
                onAmountChanged = viewModel::onAmountChanged,
                amountError = uiState.amountError,
                stock = uiState.stock,
                onStockChanged = viewModel::onStockChanged,
                stockError = uiState.stockError,
                minimumStock = uiState.minimumStock,
                onMinimumStockChanged = viewModel::onMinimumStockChanged,
                minimumStockError = uiState.minimumStockError,
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
    nameError: String?,
    description: String,
    onDescriptionChanged: (String) -> Unit,
    descriptionError: String?,
    category: CreateProductCategory,
    onCategoryChanged: (CreateProductCategory) -> Unit,
    categoryError: String?,
    amount: String,
    onAmountChanged: (String) -> Unit,
    amountError: String?,
    stock: String,
    onStockChanged: (String) -> Unit,
    stockError: String?,
    minimumStock: String,
    onMinimumStockChanged: (String) -> Unit,
    minimumStockError: String?,
    imageUri: Uri,
    onCameraClicked: () -> Unit,
    onGalleryClicked: () -> Unit,
    onSaveCliked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequesters = rememberFocusRequesters(5)

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
            isError = nameError != null,
            supportingText = nameError,
            modifier = modifier
                .fillMaxWidth()
                .focusRestorer(focusRequesters[0]),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = formImeAction(0, focusRequesters.lastIndex)
            ),
            keyboardActions = formKeyboardActions(
                0,
                focusRequesters,
                focusManager,
                keyboardController
            )
        )

        IWSDropDown(
            options = CreateProductCategory.entries,
            selected = category,
            label = { it.label },
            onSelected = onCategoryChanged,
            fieldLabel = "Categoría",
            modifier = modifier.fillMaxWidth()
        )
        if (categoryError != null) {
            InventaryWSText(
                value = categoryError,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }

        //description
        InventaryStockOutlinedTextField(
            value = description,
            onValueChange = onDescriptionChanged,
            label = "Descripción",
            placeholder = "Ingresar descripción",
            singleLine = false,
            minLines = 2,
            maxLines = 2,
            isError = descriptionError != null,
            supportingText = descriptionError,
            modifier = modifier
                .fillMaxWidth()
                .focusRestorer(focusRequesters[1]),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = formImeAction(1, focusRequesters.lastIndex)
            ),
            keyboardActions = formKeyboardActions(
                1,
                focusRequesters,
                focusManager,
                keyboardController
            )
        )

        //precio
        InventaryStockOutlinedTextField(
            value = amount,
            onValueChange = onAmountChanged,
            label = "Precio",
            placeholder = "0.00",
            isError = amountError != null,
            supportingText = amountError,
            modifier = modifier
                .fillMaxWidth()
                .focusRestorer(focusRequesters[2]),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = formImeAction(2, focusRequesters.lastIndex)
            ),
            keyboardActions = formKeyboardActions(
                2,
                focusRequesters,
                focusManager,
                keyboardController
            )
        )

        StockConten(
            stock = stock,
            onStockChanged = onStockChanged,
            stockError = stockError,
            minimumStock = minimumStock,
            onMinimumStockChanged = onMinimumStockChanged,
            minimumStockError = minimumStockError,
            focusRequesters = focusRequesters,
            focusManager = focusManager,
            keyboardController = keyboardController,
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
    stockError: String?,
    minimumStock: String,
    onMinimumStockChanged: (String) -> Unit,
    minimumStockError: String?,
    focusRequesters: List<FocusRequester>,
    focusManager: FocusManager,
    keyboardController: SoftwareKeyboardController?,
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
            isError = stockError != null,
            supportingText = stockError,
            modifier = modifier
                .weight(1f)
                .focusRequester(focusRequesters[3]),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = formImeAction(3, focusRequesters.lastIndex)
            ),
            keyboardActions = formKeyboardActions(
                3,
                focusRequesters,
                focusManager,
                keyboardController
            )
        )

        //existencia minima
        InventaryStockOutlinedTextField(
            value = minimumStock,
            onValueChange = onMinimumStockChanged,
            label = "Stock minimo",
            placeholder = "0",
            isError = minimumStockError != null,
            supportingText = minimumStockError,
            modifier = modifier
                .weight(1f)
                .focusRequester(focusRequesters[4]),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = formImeAction(4, focusRequesters.lastIndex)
            ),
            keyboardActions = formKeyboardActions(
                4,
                focusRequesters,
                focusManager,
                keyboardController
            )
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